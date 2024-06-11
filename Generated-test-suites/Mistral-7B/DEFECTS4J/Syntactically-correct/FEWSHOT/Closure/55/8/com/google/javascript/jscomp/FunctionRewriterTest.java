package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.FunctionRewriter;
import org.apache.commons.cli.FunctionRewriter.Reducer;
import org.apache.commons.cli.FunctionRewriter.Reduction;
import org.junit.Test;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.compile("function f() {}");
    assertEquals(11, root.getChildCount());

    Reducer reducer = new FunctionRewriter.EmptyFunctionReducer();
    FunctionRewriter rewriterWithReducer = new FunctionRewriter(rewriter);
    rewriterWithReducer.process(null, root);

    Node result = rewriterWithReducer.compiler.getRootNode();
    assertEquals(7, result.getChildCount());
    assertEquals("emptyFn", result.getFirstChild().getString());
    assertEquals(Token.CALL, result.getFirstChild().getType());
  }

  @Test
  public void testIdentityReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.compile("function f(x) { return x; }");
    assertEquals(12, root.getChildCount());

    Reducer reducer = new FunctionRewriter.IdentityReducer();
    FunctionRewriter rewriterWithReducer = new FunctionRewriter(rewriter);
    rewriterWithReducer.process(null, root);

    Node result = rewriterWithReducer.compiler.getRootNode();
    assertEquals(7, result.getChildCount());
    assertEquals("identityFn", result.getFirstChild().getString());
    assertEquals(Token.CALL, result.getFirstChild().getType());
  }

  @Test
  public void testGetterReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.compile("function f() { return this.x; }");
    assertEquals(12, root.getChildCount());

    Reducer reducer = new FunctionRewriter.GetterReducer();
    FunctionRewriter rewriterWithReducer = new FunctionRewriter(rewriter);
    rewriterWithReducer.process(null, root);

    Node result = rewriterWithReducer.compiler.getRootNode();
    assertEquals(7, result.getChildCount());
    assertEquals("getter", result.getFirstChild().getString());
    assertEquals(Token.CALL, result.getFirstChild().getType());
  }

  @Test
  public void testSetterReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.compile("function f(x) { this.x = x; }");
    assertEquals(13, root.getChildCount());

    Reducer reducer = new FunctionRewriter.SetterReducer();
    FunctionRewriter rewriterWithReducer = new FunctionRewriter(rewriter);
    rewriterWithReducer.process(null, root);

    Node result = rewriterWithReducer.compiler.getRootNode();
    assertEquals(7, result.getChildCount());
    assertEquals("setter", result.getFirstChild().getString());
    assertEquals(Token.CALL, result.getFirstChild().getType());
  }

  @Test
  public void testReductionGatherer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.compile("function f() { return this.x; }");
    assertEquals(12, root.getChildCount());

    List<Reducer> reducers = ImmutableList.of(new FunctionRewriter.IdentityReducer());
    Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();

    NodeTraversal.traverse(rewriter, root, new FunctionRewriter.ReductionGatherer(reducers, reductionMap));

    Collection<Reduction> reductions = reductionMap.get(new FunctionRewriter.IdentityReducer());
    assertEquals(1, reductions.size());

    Reduction reduction = reductions.iterator().next();
    Node parent = reduction.parent;
    Node oldChild = reduction.oldChild;
    Node newChild = reduction.newChild;

    assertEquals(12, parent.getChildCount());
    assertEquals(11, oldChild.getChildCount());
    assertEquals(7, newChild.getChildCount());
    assertEquals("identityFn", newChild.getString());
    assertEquals(Token.CALL, newChild.getType());
  }
}