import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Nodeops;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollapseVariableDeclarationsTest {

  private static class TestCompiler extends AbstractCompiler {

    @Override
    protected CompilerPass getPassForSourceUnit(SourceUnit sourceUnit) {
      return new CollapseVariableDeclarations(this);
    }
  }

  @org.junit.Test
  public void testCollapseSingleVariableDeclaration() {
    TestCompiler compiler = new TestCompiler();
    AbstractCompiler abstractCompiler = mock(AbstractCompiler.class);
    when(abstractCompiler.getOptions()).thenReturn(new CompilerOptions());
    when(abstractCompiler.getSourceUnit()).thenReturn(new SourceUnit("test.js"));
    when(abstractCompiler.getLifeCycleState()).thenReturn(new LifeCycleState());

    Node root = Nodeops.createRootNode("var a = 1;");

    CompilerPass pass = compiler.getPassForSourceUnit(new SourceUnit("test.js"));
    pass.process(null, root);

    List<Node> nodes = new ArrayList<>();
    NodeTraversal.traverse(abstractCompiler, root, new NodeTraversal.AbstractPreOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    Node expected = Nodeops.createNode("var a = 1;");
    assertEquals(expected.toString(), nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseMultipleVariableDeclarations() {
    TestCompiler compiler = new TestCompiler();
    AbstractCompiler abstractCompiler = mock(AbstractCompiler.class);
    when(abstractCompiler.getOptions()).thenReturn(new CompilerOptions());
    when(abstractCompiler.getSourceUnit()).thenReturn(new SourceUnit("test.js"));
    when(abstractCompiler.getLifeCycleState()).thenReturn(new LifeCycleState());

    Node root = Nodeops.createRootNode("var a; var b = 1; var c = 2;");

    CompilerPass pass = compiler.getPassForSourceUnit(new SourceUnit("test.js"));
    pass.process(null, root);

    List<Node> nodes = new ArrayList<>();
    NodeTraversal.traverse(abstractCompiler, root, new NodeTraversal.AbstractPreOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    Node expected = Nodeops.createNode("var a, b = 1, c = 2;");
    assertEquals(expected.toString(), nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseMultipleVariableAssignments() {
    TestCompiler compiler = new TestCompiler();
    AbstractCompiler abstractCompiler = mock(AbstractCompiler.class);
    when(abstractCompiler.getOptions()).thenReturn(new CompilerOptions());
    when(abstractCompiler.getSourceUnit()).thenReturn(new SourceUnit("test.js"));
    when(abstractCompiler.getLifeCycleState()).thenReturn(new LifeCycleState());

    Node root = Nodeops.createRootNode("a = true; b = true; var c = true;");

    CompilerPass pass = compiler.getPassForSourceUnit(new SourceUnit("test.js"));
    pass.process(null, root);

    List<Node> nodes = new ArrayList<>();
    NodeTraversal.traverse(abstractCompiler, root, new NodeTraversal.AbstractPreOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    Node expected = Nodeops.createNode("var c = b = a = true;");
    assertEquals(expected.toString(), nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsInsideFunction() {
    TestCompiler compiler = new TestCompiler();
    AbstractCompiler abstractCompiler = mock(AbstractCompiler.class);
    when(abstractCompiler.getOptions()).thenReturn(new CompilerOptions());
    when(abstractCompiler.getSourceUnit()).thenReturn(new SourceUnit("test.js"));
    when(abstractCompiler.getLifeCycleState()).thenReturn(new LifeCycleState());

    Node root = Nodeops.createRootNode("function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}");

    CompilerPass pass = compiler.getPassForSourceUnit(new SourceUnit("test.js"));
    pass.process(null, root);

    List<Node> nodes = new ArrayList<>();
    NodeTraversal.traverse(abstractCompiler, root, new NodeTraversal.AbstractPreOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    Node expected = Nodeops.createNode("function foo() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}");
    assertEquals(expected.toString(), nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsWithJSDocs() {
    TestCompiler compiler = new TestCompiler();
    AbstractCompiler abstractCompiler = mock(AbstractCompiler.class);
    when(abstractCompiler.getOptions()).thenReturn(new CompilerOptions());
    when(abstractCompiler.getSourceUnit()).thenReturn(new SourceUnit("test.js"));
    when(abstractCompiler.getLifeCycleState()).thenReturn(new LifeCycleState());

    Node root = Nodeops.createRootNode("/** @type {!Array} */ var a; /** @type {!Number} */ var b;");

    CompilerPass pass = compiler.getPassForSourceUnit(new SourceUnit("test.js"));
    pass.process(null, root);

    List<Node> nodes = new ArrayList<>();
    NodeTraversal.traverse(abstractCompiler, root, new NodeTraversal.AbstractPreOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    Node expected = Nodeops.createNode("/** @type {!Array|!Number} */ var a, b;");
    assertEquals(expected.toString(), nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsWithinConditionalStatement() {
    TestCompiler compiler = new TestCompiler();
    AbstractCompiler abstractCompiler = mock(AbstractCompiler.class);
    when(abstractCompiler.getOptions()).thenReturn(new CompilerOptions());
    when(abstractCompiler.getSourceUnit()).thenReturn(new SourceUnit("test.js"));
    when(abstractCompiler.getLifeCycleState()).thenReturn(new LifeCycleState());

    Node root = Nodeops.createRootNode("if (true) {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "}");

    CompilerPass pass = compiler.getPassForSourceUnit(new SourceUnit("test.js"));
    pass.process(null, root);

    List<Node> nodes = new ArrayList<>();
    NodeTraversal.traverse(abstractCompiler, root, new NodeTraversal.AbstractPreOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    Node expected = Nodeops.createNode("// empty");
    assertEquals(expected.toString(), nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsWithinLoopStatement() {
    TestCompiler compiler = new TestCompiler();
    AbstractCompiler abstractCompiler = mock(AbstractCompiler.class);
    when(abstractCompiler.getOptions()).thenReturn(new CompilerOptions());
    when(abstractCompiler.getSourceUnit()).thenReturn(new SourceUnit("test.js"));
    when(abstractCompiler.getLifeCycleState()).thenReturn(new LifeCycleState());

    Node root = Nodeops.createRootNode("for (var i = 0; i < 10; i++) {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "}");

    CompilerPass pass = compiler.getPassForSourceUnit(new SourceUnit("test.js"));
    pass.process(null, root);

    List<Node> nodes = new ArrayList<>();
    NodeTraversal.traverse(abstractCompiler, root, new NodeTraversal.AbstractPreOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    Node expected = Nodeops.createNode("// empty");
    assertEquals(expected.toString(), nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsWithinSwitchStatement() {
    TestCompiler compiler = new TestCompiler();
    AbstractCompiler abstractCompiler = mock(AbstractCompiler.class);
    when(abstractCompiler.getOptions()).thenReturn(new CompilerOptions());
    when(abstractCompiler.getSourceUnit()).thenReturn(new SourceUnit("test.js"));
    when(abstractCompiler.getLifeCycleState()).thenReturn(new LifeCycleState());

    Node root = Nodeops.createRootNode("switch (1) {\n" +
        "  case 1:\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "    break;\n" +
        "  default:\n" +
        "    var c;\n" +
        "}");

    CompilerPass pass = compiler.getPassForSourceUnit(new SourceUnit("test.js"));
    pass.process(null, root);

    List<Node> nodes = new ArrayList<>();
    NodeTraversal.traverse(abstractCompiler, root, new NodeTraversal.AbstractPreOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    Node expected = Nodeops.createNode("// empty");
    assertEquals(expected.toString(), nodes.get(0).toString());
  }
}