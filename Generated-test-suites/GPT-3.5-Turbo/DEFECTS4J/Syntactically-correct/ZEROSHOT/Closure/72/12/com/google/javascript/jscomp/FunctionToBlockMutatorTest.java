package com.google.javascript.jscomp;

import com.google.common.base.Supplier;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {
  private FunctionToBlockMutator mutator;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
        @Override
        public String get() {
            return "1";
        }
    };
    mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutate_withEmptyFunction_returnEmptyBlock() {
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    Node result = mutator.mutate("fnName", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    Node expectedResult = new Node(Token.BLOCK);
    assertEquals(expectedResult, result);
  }
  
  @Test
  public void testMutate_withFunctionWithReturn_returnBlockWithReturnExpression() {
    Node fnNode = new Node(Token.FUNCTION);
    Node returnNode = new Node(Token.RETURN, NodeUtil.newName("a"));
    fnNode.addChildToBack(returnNode);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    Node result = mutator.mutate("fnName", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    Node expectedResult = new Node(Token.BLOCK);
    expectedResult.addChildToBack(NodeUtil.newName("a"));
    Node breakNode = new Node(Token.BREAK, Node.newString(Token.LABEL_NAME, "JSCompiler_inline_label_fnName_1"));
    expectedResult.addChildToBack(breakNode);
    assertEquals(expectedResult, result);
  }
  
  @Test
  public void testMutate_withFunctionWithoutReturn_returnBlockWithBreak() {
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    Node result = mutator.mutate("fnName", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    Node expectedResult = new Node(Token.BLOCK);
    Node breakNode = new Node(Token.BREAK, Node.newString(Token.LABEL_NAME, "JSCompiler_inline_label_fnName_1"));
    expectedResult.addChildToBack(breakNode);
    assertEquals(expectedResult, result);
  }
}