package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {
  // 

  @Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = compiler.parseJs("function test() {}");
    functionRewriter.process(null, root);

    Node emptyFunction = root.getFirstChild();
    Node emptyFunctionCall = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);

    Node expectedEmptyFunctionCall = new Node(Token.CALL, emptyFunctionCall.getLineno(), emptyFunctionCall.getCharno());
    expectedEmptyFunctionCall.putBooleanProp(Node.FREE_CALL, true);
    expectedEmptyFunctionCall.addChildToBack(Node.newString(Token.NAME, EmptyFunctionReducer.FACTORY_METHOD_NAME));

    assertNotNull("Empty function should be replaced with emptyFn helper call",
        NodeUtil.areNodesEqual(emptyFunctionCall, expectedEmptyFunctionCall));
  }

  @Test
  public void testIdentityReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = compiler.parseJs("function test(a) { return a }");

    functionRewriter.process(null, root);

    Node identityFunctionCall = functionRewriter.parseHelperCode(IdentityReducer.class);
    Node identityFunctionArgumentNode = new Node(Token.NAME, "a");

    Node expectedIdentityFunctionCall = new Node(Token.CALL, identityFunctionCall.getLineno(), identityFunctionCall.getCharno());
    expectedIdentityFunctionCall.putBooleanProp(Node.FREE_CALL, true);
    expectedIdentityFunctionCall.addChildToBack(Node.newString(Token.NAME, IdentityReducer.FACTORY_METHOD_NAME));
    expectedIdentityFunctionCall.addChildToBack(identityFunctionArgumentNode.cloneTree());

    assertNotNull("Identity function should be replaced with identityFn helper call",
        NodeUtil.areNodesEqual(root.getLastChild(), expectedIdentityFunctionCall));
  }

  @Test
  public void testReturnConstantReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = compiler.parseJs("function test() { return 10 }");

    functionRewriter.process(null, root);

    Node returnConstantHelperCall = functionRewriter.parseHelperCode(ReturnConstantReducer.class);
    Node constantValueNode = new Node(Token.INT_LITERAL, 10);

    Node expectedReturnConstantHelperCall = new Node(Token.CALL, returnConstantHelperCall.getLineno(), returnConstantHelperCall.getCharno());
    expectedReturnConstantHelperCall.putBooleanProp(Node.FREE_CALL, true);
    expectedReturnConstantHelperCall.addChildToBack(Node.newString(Token.NAME, ReturnConstantReducer.FACTORY_METHOD_NAME));
    expectedReturnConstantHelperCall.addChildToBack(constantValueNode);

    assertNotNull("Constant return statement should be replaced with returnArg helper call",
        NodeUtil.areNodesEqual(root.getLastChild(), expectedReturnConstantHelperCall));
  }

  @Test
  public void testGetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = compiler.parseJs("function test() { return this.a }");

    functionRewriter.process(null, root);

    Node getterHelperCall = functionRewriter.parseHelperCode(GetterReducer.class);
    Node propertyNameNode = new Node(Token.STRING, "a");

    Node expectedGetterHelperCall = new Node(Token.CALL, getterHelperCall.getLineno(), getterHelperCall.getCharno());
    expectedGetterHelperCall.putBooleanProp(Node.FREE_CALL, true);
    expectedGetterHelperCall.addChildToBack(Node.newString(Token.NAME, GetterReducer.FACTORY_METHOD_NAME));
    expectedGetterHelperCall.addChildToBack(propertyNameNode);

    assertNotNull("Getter should be replaced with getter helper call",
        NodeUtil.areNodesEqual(root.getLastChild(), expectedGetterHelperCall));
  }

  @Test
  public void testSetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = compiler.parseJs("function test(a) { this.a = a }");

    functionRewriter.process(null, root);

    Node setterHelperCall = functionRewriter.parseHelperCode(SetterReducer.class);
    Node propertyNameNode = new Node(Token.STRING, "a");

    Node expectedSetterHelperCall = new Node(Token.CALL, setterHelperCall.getLineno(), setterHelperCall.getCharno());
    expectedSetterHelperCall.putBooleanProp(Node.FREE_CALL, true);
    expectedSetterHelperCall.addChildToBack(Node.newString(Token.NAME, SetterReducer.FACTORY_METHOD_NAME));
    expectedSetterHelperCall.addChildToBack(propertyNameNode);

    assertNotNull("Setter should be replaced with setter helper call",
        NodeUtil.areNodesEqual(root.getLastChild(), expectedSetterHelperCall));
  }

  // 
}