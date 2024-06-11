package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunctionWithNoArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null);
    Node callNode = Node.newIdentifierNode("testCall");
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, null, null, null, Lists.newArrayList(
        Node.newIdentifierNode("arg1"),
        Node.newIdentifierNode("arg2")
    ));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", Node.newIdentifierNode("arg1Value"));
    argMap.put("arg2", Node.newIdentifierNode("arg2Value"));

    Node mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);
  }

  @Test
  public void testMutateFunctionWithModifiedParameters() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, null, null, null, Lists.newArrayList(
        Node.newIdentifierNode("arg1"),
        Node.newIdentifierNode("arg2")
    ));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Set<String> namesToAlias = Sets.newHashSet("arg1");
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", Node.newIdentifierNode("arg1Value"));
    argMap.put("arg2", Node.newIdentifierNode("arg2Value"));

    Node mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);
  }

  @Test
  public void testAliasAndInlineArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node fnTemplateRoot = Node.newBlock(null);
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", Node.newIdentifierNode("arg1Value"));
    argMap.put("arg2", Node.newIdentifierNode("arg2Value"));

    Set<String> namesToAlias = Sets.newHashSet("arg1");

    Node result = functionToBlockMutator.aliasAndArguments(fnTemplateRoot, argMap, namesToAlias);

    assertEquals(fnTemplateRoot, result);
  }

  @Test
  public void testReplaceReturns() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String labelName = "testLabel";
    Node block = Node.newBlock(null);

    String resultName = "result";
    boolean needsDefaultResult = true;

    Node result = functionToBlockMutator.replaceReturns(block, resultName, labelName, needsDefaultResult);

    assertNotNull(result);
  }
}