package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseHelperCode(FunctionRewriter.EmptyFunctionReducer.class);
    assertNotNull(root);

    Node node = new Node(Token.FUNCTION, 1, 1);
    FunctionRewriter.EmptyFunctionReducer reducer = new FunctionRewriter.EmptyFunctionReducer();
    Node reducedNode = reducer.reduce(node);

    assertTrue(reducedNode.getType() == Token.CALL);
    assertEquals(FunctionRewriter.EmptyFunctionReducer.FACTORY_METHOD_NAME, reducedNode.getFirstChild().getString());
    assertNull(reducedNode.getLastChild());

    Node parent = new Node(Token.ASSIGN, 1, 1);
    parent.addChildToBack(node);
    functionRewriter.process(null, parent);

    assertTrue(parent.getLastChild().getType() == Token.CALL);
    assertEquals(FunctionRewriter.EmptyFunctionReducer.FACTORY_METHOD_NAME, parent.getLastChild().getFirstChild().getString());
    assertNull(parent.getLastChild().getLastChild());
  }

  @Test
  public void testIdentityReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseHelperCode(FunctionRewriter.IdentityReducer.class);
    assertNotNull(root);

    Node node = new Node(Token.FUNCTION, 1, 1);
    Node argNode = new Node(Token.NAME, "arg");
    Node functionBody = new Node(Token.RETURN, 1, 1);
    functionBody.addChildToBack(argNode);
    node.addChildToBack(functionBody);

    FunctionRewriter.IdentityReducer reducer = new FunctionRewriter.IdentityReducer();
    Node reducedNode = reducer.reduce(node);

    assertTrue(reducedNode.getType() == Token.CALL);
    assertEquals(FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME, reducedNode.getFirstChild().getString());
    assertTrue(reducedNode.getLastChild().getType() == Token.NAME);
    assertEquals("arg", reducedNode.getLastChild().getString());

    Node parent = new Node(Token.ASSIGN, 1, 1);
    parent.addChildToBack(node);
    functionRewriter.process(null, parent);

    assertTrue(parent.getLastChild().getType() == Token.CALL);
    assertEquals(FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME, parent.getLastChild().getFirstChild().getString());
    assertTrue(parent.getLastChild().getLastChild().getType() == Token.NAME);
    assertEquals("arg", parent.getLastChild().getLastChild().getString());
  }

  @Test
  public void testReturnConstantReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseHelperCode(FunctionRewriter.ReturnConstantReducer.class);
    assertNotNull(root);

    Node node = new Node(Token.FUNCTION, 1, 1);
    Node constantNode = new Node(Token.NUM, 10, 1);
    Node functionBody = new Node(Token.RETURN, 1, 1);
    functionBody.addChildToBack(constantNode);
    node.addChildToBack(functionBody);

    FunctionRewriter.ReturnConstantReducer reducer = new FunctionRewriter.ReturnConstantReducer();
    Node reducedNode = reducer.reduce(node);

    assertTrue(reducedNode.getType() == Token.CALL);
    assertEquals(FunctionRewriter.ReturnConstantReducer.FACTORY_METHOD_NAME, reducedNode.getFirstChild().getString());
    assertTrue(reducedNode.getLastChild().getType() == Token.NUM);
    assertEquals(10, reducedNode.getLastChild().getNumber());

    Node parent = new Node(Token.ASSIGN, 1, 1);
    parent.addChildToBack(node);
    functionRewriter.process(null, parent);

    assertTrue(parent.getLastChild().getType() == Token.CALL);
    assertEquals(FunctionRewriter.ReturnConstantReducer.FACTORY_METHOD_NAME, parent.getLastChild().getFirstChild().getString());
    assertTrue(parent.getLastChild().getLastChild().getType() == Token.NUM);
    assertEquals(10, parent.getLastChild().getLastChild().getNumber());
  }

  @Test
  public void testGetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseHelperCode(FunctionRewriter.GetterReducer.class);
    assertNotNull(root);

    Node node = new Node(Token.FUNCTION, 1, 1);
    Node getPropNode = new Node(Token.GETPROP, 1, 1);
    Node thisNode = new Node(Token.THIS, 1, 1);
    getPropNode.addChildToBack(thisNode);
    getPropNode.addChildToBack(new Node(Token.STRING, "name", 1, 1));
    Node functionBody = new Node(Token.RETURN, 1, 1);
    functionBody.addChildToBack(getPropNode);
    node.addChildToBack(functionBody);

    FunctionRewriter.GetterReducer reducer = new FunctionRewriter.GetterReducer();
    Node reducedNode = reducer.reduce(node);

    assertTrue(reducedNode.getType() == Token.CALL);
    assertEquals(FunctionRewriter.GetterReducer.FACTORY_METHOD_NAME, reducedNode.getFirstChild().getString());
    assertTrue(reducedNode.getLastChild().getType() == Token.STRING);
    assertEquals("name", reducedNode.getLastChild().getString());

    Node parent = new Node(Token.ASSIGN, 1, 1);
    parent.addChildToBack(node);
    functionRewriter.process(null, parent);

    assertTrue(parent.getLastChild().getType() == Token.CALL);
    assertEquals(FunctionRewriter.GetterReducer.FACTORY_METHOD_NAME, parent.getLastChild().getFirstChild().getString());
    assertTrue(parent.getLastChild().getLastChild().getType() == Token.STRING);
    assertEquals("name", parent.getLastChild().getLastChild().getString());
  }

  @Test
  public void testSetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseHelperCode(FunctionRewriter.SetterReducer.class);
    assertNotNull(root);

    Node node = new Node(Token.FUNCTION, 1, 1);
    Node getPropNode = new Node(Token.GETPROP, 1, 1);
    Node thisNode = new Node(Token.THIS, 1, 1);
    getPropNode.addChildToBack(thisNode);
    getPropNode.addChildToBack(new Node(Token.STRING, "name", 1, 1));
    Node setPropNode = new Node(Token.SETPROP, 1, 1);
    Node valueNode = new Node(Token.NUM, 10, 1);
    setPropNode.addChildToBack(getPropNode);
    setPropNode.addChildToBack(valueNode);
    Node functionBody = new Node(Token.RETURN, 1, 1);
    functionBody.addChildToBack(setPropNode);
    node.addChildToBack(functionBody);

    FunctionRewriter.SetterReducer reducer = new FunctionRewriter.SetterReducer();
    Node reducedNode = reducer.reduce(node);

    assertTrue(reducedNode.getType() == Token.CALL);
    assertEquals(FunctionRewriter.SetterReducer.FACTORY_METHOD_NAME, reducedNode.getFirstChild().getString());
    assertTrue(reducedNode.getLastChild().getType() == Token.NUM);
    assertEquals(10, reducedNode.getLastChild().getNumber());

    Node parent = new Node(Token.ASSIGN, 1, 1);
    parent.addChildToBack(node);
    functionRewriter.process(null, parent);

    assertTrue(parent.getLastChild().getType() == Token.CALL);
    assertEquals(FunctionRewriter.SetterReducer.FACTORY_METHOD_NAME, parent.getLastChild().getFirstChild().getString());
    assertTrue(parent.getLastChild().getLastChild().getType() == Token.NUM);
    assertEquals(10, parent.getLastChild().getLastChild().getNumber());
  }

  @Test
  public void testNonReducibleFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node node = new Node(Token.FUNCTION, 1, 1);
    Node functionBody = new Node(Token.RETURN, 1, 1);
    functionBody.addChildToBack(new Node(Token.NUM, 10, 1));
    node.addChildToBack(functionBody);

    FunctionRewriter.EmptyFunctionReducer emptyFunctionReducer = new FunctionRewriter.EmptyFunctionReducer();
    FunctionRewriter.IdentityReducer identityReducer = new FunctionRewriter.IdentityReducer();
    FunctionRewriter.ReturnConstantReducer returnConstantReducer = new FunctionRewriter.ReturnConstantReducer();
    FunctionRewriter.GetterReducer getterReducer = new FunctionRewriter.GetterReducer();
    FunctionRewriter.SetterReducer setterReducer = new FunctionRewriter.SetterReducer();

    List<FunctionRewriter.Reducer> reducers = ImmutableList.of(
        emptyFunctionReducer, identityReducer, returnConstantReducer, getterReducer, setterReducer);

    Multimap<FunctionRewriter.Reducer, FunctionRewriter.Reduction> reductions = HashMultimap.create();
    NodeTraversal.traverse(functionRewriter, node, new FunctionRewriter.ReductionGatherer(reducers, reductions));

    for (FunctionRewriter.Reducer reducer : reducers) {
      Collection<FunctionRewriter.Reduction> reductionsForReducer = reductions.get(reducer);
      assertFalse(reductionsForReducer.isEmpty());
    }

    functionRewriter.process(null, node);

    assertTrue(node.getType() == Token.FUNCTION);
  }
}