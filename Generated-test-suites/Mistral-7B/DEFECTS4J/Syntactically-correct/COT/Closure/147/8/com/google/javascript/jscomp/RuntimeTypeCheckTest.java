package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class RuntimeTypeCheckTest {

  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() throws IOException {
    compiler = new Compiler();
    rootNode = Node.parseJsFile("test.js");
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, rootNode);
  }

  @Test
  public void testFunctionTypeChecker() {
    FunctionType functionType = new FunctionType(null, ImmutableList.of(), null);
    Node functionNode = NodeUtil.newFunctionNode(functionType, null);

    Node checkTypeCallNode = RuntimeTypeCheck.createCheckTypeCallNode(functionType, functionNode.cloneTree());
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testNullTypeChecker() {
    Node nullNode = Node.newNull();
    Node checkTypeCallNode = RuntimeTypeCheck.createCheckTypeCallNode(
            JSType.NULL_TYPE, nullNode.cloneTree());
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testBooleanValueTypeChecker() {
    Node booleanNode = Node.newBoolean(true);
    Node checkTypeCallNode = RuntimeTypeCheck.createCheckTypeCallNode(
            JSType.BOOLEAN_VALUE_TYPE, booleanNode.cloneTree());
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testNumberValueTypeChecker() {
    Node numberNode = Node.newNumber(1);
    Node checkTypeCallNode = RuntimeTypeCheck.createCheckTypeCallNode(
            JSType.NUMBER_VALUE_TYPE, numberNode.cloneTree());
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testStringValueTypeChecker() {
    Node stringNode = Node.newString("test");
    Node checkTypeCallNode = RuntimeTypeCheck.createCheckTypeCallNode(
            JSType.STRING_VALUE_TYPE, stringNode.cloneTree());
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testVoidTypeChecker() {
    Node voidNode = Node.newVoid();
    Node checkTypeCallNode = RuntimeTypeCheck.createCheckTypeCallNode(
            JSType.VOID_TYPE, voidNode.cloneTree());
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testInstanceTypeChecker() {
    ObjectType instanceType = new ObjectType("Foo", new Node());
    Node instanceNode = NodeUtil.newQualifiedNameNode("Foo", -1, -1);

    Node checkTypeCallNode = RuntimeTypeCheck.createCheckTypeCallNode(instanceType, instanceNode.cloneTree());
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testNullChecker() {
    Node checkTypeCallNode = Node.newCall(
            RuntimeTypeCheck.jsCode("nullChecker"), Node.newNull());
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testValueChecker() {
    JSType valueType = JSType.BOOLEAN_VALUE_TYPE;
    Node valueNode = Node.newBoolean(true);

    Node checkerNode = RuntimeTypeCheck.createCheckerNode(valueType);
    Node checkTypeCallNode = new Node(Token.CALL, checkerNode, valueNode);
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testExternClassChecker() {
    ObjectType externType = new ObjectType("Bar", new Node());
    String sourceName = "extern_file.js";

    Node checkerNode = RuntimeTypeCheck.createCheckerNode(externType);
    Node checkTypeCallNode = new Node(Token.CALL, checkerNode, NodeUtil.newQualifiedNameNode("Bar", -1, -1));
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testClassChecker() {
    ObjectType userDefinedType = new ObjectType("Foo", new Node());
    Node constructorNode = userDefinedType.getConstructor();
    String className = NodeUtil.getFunctionName(constructorNode.getSource());
    Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);

    Node checkerNode = RuntimeTypeCheck.createCheckerNode(userDefinedType);
    Node checkTypeCallNode = new Node(Token.CALL, checkerNode, classNode);
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testInterfaceChecker() {
    ObjectType userDefinedInterface = new ObjectType("Foo", new Node());
    Node constructorNode = userDefinedInterface.getConstructor();
    String interfaceName = NodeUtil.getFunctionName(constructorNode.getSource());
    Node interfaceNode = NodeUtil.newQualifiedNameNode(interfaceName, -1, -1);

    Node checkerNode = RuntimeTypeCheck.createCheckerNode(userDefinedInterface);
    Node checkTypeCallNode = new Node(Token.CALL, checkerNode, interfaceNode);
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testUnionTypeChecker() {
    UnionType unionType = new UnionType(ImmutableList.of(JSType.NUMBER_VALUE_TYPE, JSType.STRING_VALUE_TYPE));
    Node unionNode = Node.newNumber(1);

    Node checkerNode = RuntimeTypeCheck.createCheckerNode(unionType);
    Node checkTypeCallNode = Node.newCall(checkerNode, unionNode.cloneTree());
    assertNotNull(checkTypeCallNode);
  }

  @Test
  public void testCheckTypeCallWithMismatchedType() {
    FunctionType functionType = new FunctionType(null, ImmutableList.of(), null);
    Node functionNode = NodeUtil.newFunctionNode(functionType, null);
    Node numberNode = Node.newNumber(1);

    Node checkTypeCallNode = RuntimeTypeCheck.createCheckTypeCallNode(JSType.STRING_VALUE_TYPE, functionNode.cloneTree());
    assertFalse(checkTypeCallNode.equals(checkTypeCallNode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckTypeCallWithUncheckedType() {
    FunctionType functionType = new FunctionType(null, ImmutableList.of(), null);
    Node functionNode = NodeUtil.newFunctionNode(functionType, null);
    Node checkTypeCallNode = RuntimeTypeCheck.createCheckTypeCallNode(null, functionNode.cloneTree());
  }
}