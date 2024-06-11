package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;
import com.google.javascript.rhino.*;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node prototype = new Node(Token.PROTOTYPE, 1, 1);
    prototype.setProperty("constructor", new Node(Token.NAME, "Object", 1, 1));
    prototype.setProperty("prototype", new Node(Token.NAME, "prototype", 1, 1));

    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    functionNode.setProperty("prototype", prototype);
    prototype.addChildToBack(functionNode);

    Node getA = new Node(Token.NAME, "getA", 1, 1);
    functionNode.addChildToBack(getA);
    Node getAFunctionBody = new Node(Token.BLOCK, 1, 1);
    getAFunctionBody.addChildToBack(new Node(Token.RETURN, 1, 1));
    getA.setProperty("value", getAFunctionBody);

    Node setA = new Node(Token.NAME, "setA", 1, 1);
    functionNode.addChildToBack(setA);
    Node setAFunctionBody = new Node(Token.BLOCK, 1, 1);
    Node assign = new Node(Token.ASSIGN, 1, 1);
    assign.setProperty("lhs", new Node(Token.NAME, "a_", 1, 1));
    assign.setProperty("rhs", new Node(Token.NAME, "newValue", 1, 1));
    setAFunctionBody.addChildToBack(assign);
    setA.setProperty("value", setAFunctionBody);

    Node externs = new Node(Token.PROGRAM, 1, 1);
    Node rootNode = new Node(Token.BLOCK, 1, 1);
    externs.addChildToBack(rootNode);
    rootNode.addChildToBack(prototype);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(externs, root);

    Node getAReduced = root.getFirstChild().getFirstChild();
    assertTrue(getAReduced instanceof Node);
    assertEquals(Token.CALL, getAReduced.getType());
    assertEquals("JSCompiler_get", getAReduced.getString());
    Node getAArgument = ((Node) getAReduced.getLastChild());
    assertEquals("a_", getAArgument.getString());

    Node setAReduced = root.getFirstChild().getNext();
    assertTrue(setAReduced instanceof Node);
    assertEquals(Token.CALL, setAReduced.getType());
    assertEquals("JSCompiler_set", setAReduced.getString());
    Node setAArgument = ((Node) setAReduced.getLastChild());
    assertEquals("a_", setAArgument.getString());
  }

  @Test
  public void testIdentityReducer() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node prototype = new Node(Token.PROTOTYPE, 1, 1);
    prototype.setProperty("constructor", new Node(Token.NAME, "Object", 1, 1));
    prototype.setProperty("prototype", new Node(Token.NAME, "prototype", 1, 1));

    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    functionNode.setProperty("prototype", prototype);
    prototype.addChildToBack(functionNode);

    Node functionBody = new Node(Token.BLOCK, 1, 1);
    Node identityFunction = new Node(Token.FUNCTION, 1, 1);
    identityFunction.setProperty("prototype", prototype);
    Node identityFunctionPrototype = identityFunction.getLastChild();
    Node identityFunctionName = new Node(Token.NAME, "identityFunction", 1, 1);
    identityFunctionPrototype.addChildToBack(identityFunctionName);
    Node identityFunctionBody = new Node(Token.BLOCK, 1, 1);
    identityFunctionBody.addChildToBack(new Node(Token.RETURN, 1, 1));
    identityFunctionBody.addChildToBack(new Node(Token.VAR, 1, 1, identityFunctionName.getString(), null, null));
    identityFunctionBody.addChildToBack(new Node(Token.ASSIGN, 1, 1, identityFunctionName, new Node(Token.NAME, identityFunctionName.getString(), 1, 1)));
    identityFunctionBody.addChildToBack(new Node(Token.RETURN, 1, 1, new Node(Token.NAME, identityFunctionName.getString(), 1, 1)));
    identityFunction.setProperty("value", identityFunctionBody);

    functionNode.addChildToBack(identityFunction);
    Node functionArgument = new Node(Token.NAME, "value", 1, 1);
    identityFunctionBody.addChildToBack(functionArgument);
    prototype.addChildToBack(functionNode);

    Node externs = new Node(Token.PROGRAM, 1, 1);
    Node rootNode = new Node(Token.BLOCK, 1, 1);
    externs.addChildToBack(rootNode);
    rootNode.addChildToBack(prototype);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(externs, root);

    Node identityFunctionReduced = root.getFirstChild().getFirstChild();
    assertTrue(identityFunctionReduced instanceof Node);
    assertEquals(Token.CALL, identityFunctionReduced.getType());
    assertEquals("JSCompiler_identityFn", identityFunctionReduced.getString());
    Node identityFunctionArgument = ((Node) identityFunctionReduced.getLastChild());
    assertTrue(identityFunctionArgument instanceof Node);
    assertEquals(Token.VAR, identityFunctionArgument.getType());
    assertEquals("value", identityFunctionArgument.getString());
  }

  @Test
  public void testReturnConstantReducer() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node prototype = new Node(Token.PROTOTYPE, 1, 1);
    prototype.setProperty("constructor", new Node(Token.NAME, "Object", 1, 1));
    prototype.setProperty("prototype", new Node(Token.NAME, "prototype", 1, 1));

    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    functionNode.setProperty("prototype", prototype);
    prototype.addChildToBack(functionNode);

    Node functionBody = new Node(Token.BLOCK, 1, 1);
    Node constant = new Node(Token.INTEGER, 10, 1);
    Node returnStatement = new Node(Token.RETURN, 1, 1);
    returnStatement.setProperty("value", constant);
    functionBody.addChildToBack(returnStatement);
    functionNode.setProperty("value", functionBody);

    Node externs = new Node(Token.PROGRAM, 1, 1);
    Node rootNode = new Node(Token.BLOCK, 1, 1);
    externs.addChildToBack(rootNode);
    rootNode.addChildToBack(prototype);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(externs, root);

    Node returnConstantReduced = root.getFirstChild().getFirstChild();
    assertTrue(returnConstantReduced instanceof Node);
    assertEquals(Token.CALL, returnConstantReduced.getType());
    assertEquals("JSCompiler_returnArg", returnConstantReduced.getString());
    Node returnConstantArgument = ((Node) returnConstantReduced.getLastChild());
    assertEquals(Token.INTEGER, returnConstantArgument.getType());
    assertEquals(10, returnConstantArgument.getInt());
  }

  @Test
  public void testGetterReducer() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node prototype = new Node(Token.PROTOTYPE, 1, 1);
    prototype.setProperty("constructor", new Node(Token.NAME, "Object", 1, 1));
    prototype.setProperty("prototype", new Node(Token.NAME, "prototype", 1, 1));

    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    functionNode.setProperty("prototype", prototype);
    prototype.addChildToBack(functionNode);

    Node getPropertyName = new Node(Token.STRING, "b_", 1, 1);
    Node getterFunction = new Node(Token.FUNCTION, 1, 1);
    getterFunction.setProperty("prototype", prototype);
    Node getterFunctionPrototype = getterFunction.getLastChild();
    Node getterFunctionName = new Node(Token.NAME, "getterFunction", 1, 1);
    getterFunctionPrototype.addChildToBack(getterFunctionName);
    Node getterFunctionBody = new Node(Token.BLOCK, 1, 1);
    getterFunctionBody.addChildToBack(
        new Node(Token.RETURN, 1, 1, new Node(Token.GETPROP, 1, 1, this, getPropertyName)));
    getterFunction.setProperty("value", getterFunctionBody);

    functionNode.addChildToBack(getterFunction);
    prototype.addChildToBack(functionNode);

    Node externs = new Node(Token.PROGRAM, 1, 1);
    Node rootNode = new Node(Token.BLOCK, 1, 1);
    externs.addChildToBack(rootNode);
    rootNode.addChildToBack(prototype);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(externs, root);

    Node getterFunctionReduced = root.getFirstChild().getFirstChild();
    assertTrue(getterFunctionReduced instanceof Node);
    assertEquals(Token.CALL, getterFunctionReduced.getType());
    assertEquals("JSCompiler_get", getterFunctionReduced.getString());
    Node getterFunctionArgument = ((Node) getterFunctionReduced.getLastChild());
    assertEquals("b_", getterFunctionArgument.getString());
  }

  @Test
  public void testSetterReducer() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node prototype = new Node(Token.PROTOTYPE, 1, 1);
    prototype.setProperty("constructor", new Node(Token.NAME, "Object", 1, 1));
    prototype.setProperty("prototype", new Node(Token.NAME, "prototype", 1, 1));

    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    functionNode.setProperty("prototype", prototype);
    prototype.addChildToBack(functionNode);

    Node setPropertyName = new Node(Token.STRING, "b_", 1, 1);
    Node setterFunction = new Node(Token.FUNCTION, 1, 1);
    setterFunction.setProperty("prototype", prototype);
    Node setterFunctionPrototype = setterFunction.getLastChild();
    Node setterFunctionName = new Node(Token.NAME, "setterFunction", 1, 1);
    setterFunctionPrototype.addChildToBack(setterFunctionName);
    Node setterFunctionBody = new Node(Token.BLOCK, 1, 1);
    Node assign = new Node(Token.ASSIGN, 1, 1, new Node(Token.GETPROP, 1, 1, this, setPropertyName),
                          new Node(Token.NAME, "newValue", 1, 1));
    setterFunctionBody.addChildToBack(assign);
    setterFunction.setProperty("value", setterFunctionBody);

    functionNode.addChildToBack(setterFunction);
    prototype.addChildToBack(functionNode);

    Node externs = new Node(Token.PROGRAM, 1, 1);
    Node rootNode = new Node(Token.BLOCK, 1, 1);
    externs.addChildToBack(rootNode);
    rootNode.addChildToBack(prototype);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(externs, root);

    Node setterFunctionReduced = root.getFirstChild().getNext();
    assertTrue(setterFunctionReduced instanceof Node);
    assertEquals(Token.CALL, setterFunctionReduced.getType());
    assertEquals("JSCompiler_set", setterFunctionReduced.getString());
    Node setterFunctionArgument = ((Node) setterFunctionReduced.getLastChild());
    assertEquals("b_", setterFunctionArgument.getString());
  }
}