import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.FunctionRewriter.Reducer;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import java.util.Collections;
import java.util.List;

public class FunctionRewriterTest {
  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
  }

  @org.junit.Test
  public void testFunctionRewriterProcess() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = NodeFactory.createIdent("root");

    List<Reducer> reducers = ImmutableList.of(
        new FunctionRewriter.EmptyFunctionReducer(),
        new FunctionRewriter.IdentityReducer(),
        new FunctionRewriter.ReturnConstantReducer(),
        new FunctionRewriter.GetterReducer(),
        new FunctionRewriter.SetterReducer()
    );

    Multimap<Reducer, FunctionRewriter.Reduction> reductionMap =
        HashMultimap.create();

    NodeTraversal.traverse(compiler, root,
        new FunctionRewriter.ReductionGatherer(reducers, reductionMap));

    functionRewriter.process(null, root);

    assertFalse("No reductions were applied", reductionMap.isEmpty());
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node emptyFunction = NodeFactory.createFunction(
        "emptyFunction",
        NodeFactory.createList(Collections.emptyList()),
        NodeFactory.createBlock(Collections.emptyList()));

    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("JSCompiler_emptyFn"),
        null);

    Node actualHelperCode = functionRewriter.parseHelperCode(
        new FunctionRewriter.EmptyFunctionReducer());

    assertEquals("Helper code generated incorrectly",
        expectedHelperCode, actualHelperCode);

    FunctionRewriter.Reducer reduction = new FunctionRewriter.EmptyFunctionReducer();
    FunctionRewriter.Reduction reductionResult = reduction.reduce(emptyFunction);

    Node actualReducedCode = reductionResult.newChild;

    assertEquals("Function was not reduced correctly",
        expectedHelperCode, actualReducedCode);
  }

  @org.junit.Test
  public void testIdentityReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node identityFunction = NodeFactory.createFunction(
        "identityFunction",
        NodeFactory.createList(Collections.singletonList(
            NodeFactory.createIdent("value"))),
        NodeFactory.createBlock(List.of(
            NodeFactory.createReturn(NodeFactory.createIdent("value")))));

    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("JSCompiler_identityFn"),
        NodeFactory.createList(List.of(
            NodeFactory.createIdent("value"))));

    Node actualHelperCode = functionRewriter.parseHelperCode(
        new FunctionRewriter.IdentityReducer());

    assertEquals("Helper code generated incorrectly",
        expectedHelperCode, actualHelperCode);

    FunctionRewriter.Reducer reduction = new FunctionRewriter.IdentityReducer();
    FunctionRewriter.Reduction reductionResult = reduction.reduce(identityFunction);

    Node actualReducedCode = reductionResult.newChild;

    assertEquals("Function was not reduced correctly",
        expectedHelperCode, actualReducedCode);
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node returnConstantFunction = NodeFactory.createFunction(
        "returnConstantFunction",
        Collections.emptyList(),
        NodeFactory.createBlock(List.of(
            NodeFactory.createReturn(NodeFactory.createIntLiteral(42)))));

    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("JSCompiler_returnArg"),
        NodeFactory.createIntLiteral(42));

    Node actualHelperCode = functionRewriter.parseHelperCode(
        new FunctionRewriter.ReturnConstantReducer());

    assertEquals("Helper code generated incorrectly",
        expectedHelperCode, actualHelperCode);

    FunctionRewriter.Reducer reduction = new FunctionRewriter.ReturnConstantReducer();
    FunctionRewriter.Reduction reductionResult = reduction.reduce(returnConstantFunction);

    Node actualReducedCode = reductionResult.newChild;

    assertEquals("Function was not reduced correctly",
        expectedHelperCode, actualReducedCode);
  }

  @org.junit.Test
  public void testGetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node getterFunction = NodeFactory.createFunction(
        "getterFunction",
        Collections.emptyList(),
        NodeFactory.createBlock(List.of(
            NodeFactory.createReturn(NodeFactory.createGetProp(
                NodeFactory.createThis(), "getterProperty")))));

    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("JSCompiler_get"),
        NodeFactory.createString("getterProperty"));

    Node actualHelperCode = functionRewriter.parseHelperCode(
        new FunctionRewriter.GetterReducer());

    assertEquals("Helper code generated incorrectly",
        expectedHelperCode, actualHelperCode);

    FunctionRewriter.Reducer reduction = new FunctionRewriter.GetterReducer();
    FunctionRewriter.Reduction reductionResult = reduction.reduce(getterFunction);

    Node actualReducedCode = reductionResult.newChild;

    assertEquals("Function was not reduced correctly",
        expectedHelperCode, actualReducedCode);
  }

  @org.junit.Test
  public void testSetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node setterFunction = NodeFactory.createFunction(
        "setterFunction",
        List.of(NodeFactory.createIdent("value")),
        NodeFactory.createBlock(List.of(
            NodeFactory.createSetProp(
                NodeFactory.createThis(), "setterProperty",
                NodeFactory.createIdent("value")))));

    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("JSCompiler_set"),
        NodeFactory.createString("setterProperty"));

    Node actualHelperCode = functionRewriter.parseHelperCode(
        new FunctionRewriter.SetterReducer());

    assertEquals("Helper code generated incorrectly",
        expectedHelperCode, actualHelperCode);

    FunctionRewriter.Reducer reduction = new FunctionRewriter.SetterReducer();
    FunctionRewriter.Reduction reductionResult = reduction.reduce(setterFunction);

    Node actualReducedCode = reductionResult.newChild;

    assertEquals("Function was not reduced correctly",
        expectedHelperCode, actualReducedCode);
  }

  @org.junit.Test
  public void testFunctionRewriterThrowsIALifGivenNullCompiler() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    assertThrows(IllegalArgumentException.class, () -> functionRewriter.process(null, null));
  }

  @org.junit.Test
  public void testFunctionRewriterThrowsIALifGivenNullRoot() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    assertThrows(IllegalArgumentException.class, () -> functionRewriter.process(null, null));
  }

  @org.junit.Test
  public void testFunctionRewriterIgnoresNonFunctionExpressions() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node nonFunctionExpression = NodeFactory.createCall(
        TOKEN_ADD, NodeFactory.createIntLiteral(2), NodeFactory.createIntLiteral(3));

    functionRewriter.process(null, nonFunctionExpression);

    assertTrue("Function was incorrectly reduced",
        nonFunctionExpression.equals(NodeFactory.createCall(
            TOKEN_ADD, NodeFactory.createIntLiteral(2), NodeFactory.createIntLiteral(3))));
  }
}