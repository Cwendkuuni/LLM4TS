package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique.InlineRenamer;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.rhino.Token;

public class FunctionToBlockMutatorTest {

  

  @org.junit.Before
  public void setUp() {
    // Initialize test objects
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_id";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);
  }

  @org.junit.Test
  public void testMutate_BasicFunctionWithNoArgs() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newCall(Token.CALL, null, fnNode, null);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        "result", false, false);

    // Verify the result
    assertEquals("Expected a block node", Node.BLOCK_TYPE, resultNode.getType());
  }

  @org.junit.Test
  public void testMutate_BasicFunctionWithArgs() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node argNode1 = Node.newString("arg1");
    Node argNode2 = Node.newString("arg2");
    Node callNode = Node.newCall(Token.CALL, null, fnNode, argNode1, argNode2);
    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", argNode1);
    argMap.put("arg2", argNode2);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        "result", false, false);

    // Verify the result
    assertEquals("Expected a block node", Node.BLOCK_TYPE, resultNode.getType());

    Node argumentBlock = NodeUtil.getFunctionBody(resultNode);
    Node arg1Assign = NodeUtil.findChildByType(argumentBlock, Token.ASSIGN);
    Node arg2Assign = NodeUtil.findChildByType(argumentBlock, Token.ASSIGN, 1);

    // Verify the arguments are assigned correctly
    assertEquals("Unexpected arg1 value", argNode1, arg1Assign.getFirstChild());
    assertEquals("Unexpected arg2 value", argNode2, arg2Assign.getFirstChild());
  }

  @org.junit.Test
  public void testAliasAndInlineArguments_Basic() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node argNode1 = Node.newString("arg1");
    Node argNode2 = Node.newString("arg2");
    Node callNode = Node.newCall(Token.CALL, null, fnNode, argNode1, argNode2);
    LinkedHashMap<String, Node> argMap = FunctionArgumentInjector.getFunctionCallParameterMap(
        fnNode, callNode, safeNameIdSupplier);

    Node resultNode = functionToBlockMutator.aliasAndInlineArguments(
        NodeUtil.getFunctionBody(fnNode), argMap, null);

    // Verify the result
    assertEquals("Expected the same node", fnNode, resultNode);
  }

  @org.junit.Test
  public void testAliasAndInlineArguments_WithAliases() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node argNode1 = Node.newString("arg1");
    Node argNode2 = Node.newString("arg2");
    Node unsafeArgNode1 = Node.newString("unsafeArg1");
    Node unsafeArgNode2 = Node.newString("unsafeArg2");
    Node callNode = Node.newCall(Token.CALL, null, fnNode, argNode1, argNode2);
    Set<String> namesToAlias = Lists.newArrayList(unsafeArgNode1.toString(),
        unsafeArgNode2.toString()).stream().map(String::new).collect(Collectors.toSet());
    LinkedHashMap<String, Node> argMap = FunctionArgumentInjector.getFunctionCallParameterMap(
        fnNode, callNode, safeNameIdSupplier);

    Node resultNode = functionToBlockMutator.aliasAndInlineArguments(
        NodeUtil.getFunctionBody(fnNode), argMap, namesToAlias);

    // Verify the result
    Node newArgMap =FunctionArgumentInjector.getFunctionCallParameterMap(
        resultNode, null, safeNameIdSupplier);
    assertEquals("Unexpected number of arguments", 2, newArgMap.size());
    Map.Entry<String, Node> argEntry1 = newArgMap.entrySet().iterator().next();
    assertEquals("Unexpected arg1 name", argNode1.toString(), argEntry1.getKey());
    assertEquals("Unexpected arg1 value", argNode1, argEntry1.getValue());
    Map.Entry<String, Node> argEntry2 = newArgMap.entrySet().iterator().next();
    assertEquals("Unexpected arg2 name", argNode2.toString(), argEntry2.getKey());
    assertEquals("Unexpected arg2 value", argNode2, argEntry2.getValue());

    Node localAliasNode1 = NodeUtil.findChildByType(resultNode, Token.VAR);
    Node localAliasNode2 = NodeUtil.findChildByType(resultNode, Token.VAR, 1);

    // Verify the aliases are created
    assertEquals("Unexpected alias1 name", "alias_testFunction_unsafeArg1_" + safeNameIdSupplier.get(), localAliasNode1.getFirstChild().toString());
    assertEquals("Unexpected alias2 name", "alias_testFunction_unsafeArg2_" + safeNameIdSupplier.get(), localAliasNode2.getFirstChild().toString());
  }

  @org.junit.Test
  public void testReplaceReturns_Basic() {
    String labelName = "testLabel";
    String resultName = "result";
    Node blockNode = Node.newBlock(Token.BLOCK, null);
    Node returnNode = Node.newReturn(Token.RETURN, null);

    Node resultNode = functionToBlockMutator.replaceReturns(
        blockNode, resultName, labelName, false);

    // Verify the result
    assertEquals("Expected a block node", Node.BLOCK_TYPE, resultNode.getType());
    Node labelNode = NodeUtil.findChildByType(resultNode, Token.LABEL);
    Node breakNode = NodeUtil.findChildByType(resultNode, Token.BREAK);

    // Verify the label and break nodes are created
    assertEquals("Unexpected label name", labelName, labelNode.getFirstChild().toString());
    assertEquals("Unexpected break name", labelName, breakNode.getFirstChild().toString());

    // Verify the returnNode is removed
    assertNull("Unexpected return node", NodeUtil.findChildByType(resultNode, Token.RETURN));
  }

  @org.junit.Test
  public void testReplaceReturns_WithResult() {
    String labelName = "testLabel";
    String resultName = "result";
    Node blockNode = Node.newBlock(Token.BLOCK, null);
    Node returnNode = Node.newReturn(Token.RETURN, Node.newNumber(Token.INT, 5));

    Node resultNode = functionToBlockMutator.replaceReturns(
        blockNode, resultName, labelName, true);

    // Verify the result
    assertEquals("Expected a block node", Node.BLOCK_TYPE, resultNode.getType());
    Node labelNode = NodeUtil.findChildByType(resultNode, Token.LABEL);
    Node breakNode = NodeUtil.findChildByType(resultNode, Token.BREAK);
    Node resultAssignNode = NodeUtil.findChildByType(resultNode, Token.ASSIGN);

    // Verify the label and break nodes are created
    assertEquals("Unexpected label name", labelName, labelNode.getFirstChild().toString());
    assertEquals("Unexpected break name", labelName, breakNode.getFirstChild().toString());

    // Verify the returnNode is replaced with an assignment
    assertEquals("Unexpected result name", resultName, resultAssignNode.getFirstChild().toString());
    assertEquals("Unexpected result value", Node.newNumber(Token.INT, 5), resultAssignNode.getLastChild());
  }

  
}