package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassPriority;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PeepholeOptimizationsPassTest {

  @Mock private AbstractCompiler compiler;

  private PeepholeOptimizationsPass pass;
  private List<AbstractPeepholeOptimization> optimizations;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    optimizations = Arrays.asList(mock(AbstractPeepholeOptimization.class), mock(AbstractPeepholeOptimization.class));
    pass = new PeepholeOptimizationsPass(compiler, optimizations.toArray(new AbstractPeepholeOptimization[0]));
  }

  @Test
  public void testProcess_RootNode() {
    Node root = NodeOperations.createTopLevelExpressions(Arrays.asList(NodeOperations.createIdentifier("x")));
    pass.process(null, root);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }
  }

  @Test
  public void testProcess_FunctionNode() {
    Node functionNode = NodeOperations.createTopLevelFunctionDeclaration("foo", Arrays.asList(NodeOperations.createParameter("x")), NodeOperations.createBlock(NodeOperations.createReturn(NodeOperations.createNumberLiteral(42))));
    pass.process(null, functionNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }
  }

  @Test
  public void testProcess_ScriptNode() {
    Node scriptNode = NodeOperations.createScript("function foo(x) { return x + 1; }");
    pass.process(null, scriptNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }
  }

  @Test
  public void testProcess_FunctionWithChildNodes() {
    Node functionNode = NodeOperations.createTopLevelFunctionDeclaration("foo", Arrays.asList(NodeOperations.createParameter("x")), NodeOperations.createBlock(
        NodeOperations.createVariableDeclarationStatement("y", NodeOperations.createNumberLiteral(1)),
        NodeOperations.createVariableDeclarationStatement("z", NodeOperations.createNumberLiteral(2)),
        NodeOperations.createReturnStatement(NodeOperations.createAdd(NodeOperations.createVariableReference("x"), NodeOperations.createAdd(NodeOperations.createVariableReference("y"), NodeOperations.createVariableReference("z"))))
    ));
    pass.process(null, functionNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }
  }

  @Test
  public void testProcess_ScriptWithChildNodes() {
    Node scriptNode = NodeOperations.createScript("function foo(x) {" +
        "  var y = 1;" +
        "  var z = 2;" +
        "  return x + y + z;" +
        "}" +
        "function bar() {" +
        "  return 42;" +
        "}" +
        "bar();" +
        "foo(4);" +
        "foo(5);"
    );
    pass.process(null, scriptNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }
  }

  @Test
  public void testProcess_FunctionWithChangedChildNode() {
    Node functionNode = NodeOperations.createTopLevelFunctionDeclaration("foo", Arrays.asList(NodeOperations.createParameter("x")), NodeOperations.createBlock(
        NodeOperations.createVariableDeclarationStatement("y", NodeOperations.createNumberLiteral(1)),
        NodeOperations.createReturnStatement(NodeOperations.createAdd(NodeOperations.createVariableReference("x"), NodeOperations.createNumberLiteral(2)))
    ));
    pass.process(null, functionNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }

    Node changedChildNode = functionNode.getFirstChild();
    Node modifiedChildNode = NodeOperations.createAdd(NodeOperations.createVariableReference("x"), NodeOperations.createNumberLiteral(1));
    changedChildNode.replaceChild(changedChildNode.getFirstChild(), modifiedChildNode);

    pass.process(null, functionNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertCalled();
    }
  }

  @Test
  public void testProcess_ScriptWithChangedChildNode() {
    Node scriptNode = NodeOperations.createScript("function foo(x) {" +
        "  var y = 1;" +
        "  return x + y;" +
        "}" +
        "function bar() {" +
        "  return 42;" +
        "}" +
        "bar();" +
        "foo(4);" +
        "foo(5)"
    );
    pass.process(null, scriptNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }

    Node changedChildNode = scriptNode.getChildAt(5);
    Node modifiedChildNode = NodeOperations.createIdentifier("foo");
    changedChildNode.replaceChild(changedChildNode.getFirstChild(), modifiedChildNode);

    pass.process(null, scriptNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertCalled();
    }
  }

  @Test
  public void testProcess_FunctionWithChangedParentNode() {
    Node parentNode = NodeOperations.createScript("function foo(x) {" +
        "  var y = 1;" +
        "  return x + y;" +
        "}" +
        "function bar() {" +
        "  return 42;" +
        "}" +
        "bar();" +
        "foo(4);" +
        "foo(5)"
    );
    Node functionNode = NodeOperations.createTopLevelFunctionDeclaration("qux", Arrays.asList(NodeOperations.createParameter("x")), NodeOperations.createBlock(NodeOperations.createReturnStatement(NodeOperations.createIdentifier("foo"))));
    pass.process(null, functionNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }

    Node modifiedParentNode = NodeOperations.createAdd(parentNode, NodeOperations.createExpressionStatement(NodeOperations.createCall(NodeOperations.createIdentifier("console"), NodeOperations.createArray("log"), NodeOperations.createIdentifier("qux"))));
    pass.process(null, modifiedParentNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertCalled();
    }
  }

  @Test
  public void testProcess_ScriptWithChangedParentNode() {
    Node parentNode = NodeOperations.createScript("function foo(x) {" +
        "  var y = 1;" +
        "  return x + y;" +
        "}" +
        "function bar() {" +
        "  return 42;" +
        "}" +
        "bar();" +
        "foo(4);" +
        "foo(5)"
    );
    Node scriptNode = NodeOperations.createScript("console.log(foo);");
    pass.process(null, scriptNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }

    Node modifiedParentNode = NodeOperations.createAdd(parentNode, NodeOperations.createExpressionStatement(NodeOperations.createCall(NodeOperations.createIdentifier("console"), NodeOperations.createArray("log"), NodeOperations.createIdentifier("foo"))));
    pass.process(null, modifiedParentNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertCalled();
    }
  }

  private void assertCalled(AbstractPeepholeOptimization optimization) {
    optimization.assertCalledAfterTraversal();
  }

  private static class TestAbstractPeepholeOptimization extends AbstractPeepholeOptimization {
    private int callCount = 0;

    TestAbstractPeepholeOptimization() {
      super("test", CompilerPassPriority.ALWAYS);
    }

    @Override
    public void optimizeSubtree(Node node) {
      callCount++;
    }

    public void assertCalled() {
      assertCalled(this);
    }

    public void assertCalledAfterTraversal() {
      Preconditions.checkState(callCount > 0, "optimization not called");
    }

    public void assertNotCalled() {
      Preconditions.checkState(callCount == 0, "optimization called unintentionally");
    }

    public void reset() {
      callCount = 0;
    }
  }

  @Test
  public void testBeginTraversal() {
    pass.beginTraversal();
    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertCalled();
    }

    pass.beginTraversal();
    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertCalled();
    }
  }

  @Test
  public void testEndTraversal() {
    pass.process(null, NodeOperations.createScript("function foo() {}"));
    pass.endTraversal();

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.reset();
    }

    pass.process(null, NodeOperations.createScript("function foo() {}"));
    pass.endTraversal();

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertCalled();
    }
  }

  @Test
  public void testDontVisitScriptNodeWhenChildFunctionHasNotBeenTraversed() {
    Node parentNode = NodeOperations.createScript("function foo() {" +
        "  function bar() {" +
        "    return 42;" +
        "  }" +
        "  bar();" +
        "}"
    );
    pass.process(null, parentNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }
  }

  @Test
  public void testVisitFunctionNodeWithNullParent() {
    Node functionNode = NodeOperations.createTopLevelFunctionDeclaration("foo", Arrays.asList(NodeOperations.createParameter("x")), NodeOperations.createBlock(NodeOperations.createReturnStatement(NodeOperations.createIdentifier("x"))));
    pass.process(null, functionNode);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.assertNotCalled();
    }
  }

  @Test(expected = NullPointerException.class)
  public void testVisitNullNode() {
    pass.process(null, null);
  }
}