import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.FunctionToBlockMutator.*;

import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = new CompilerOptions().newCompiler();
    safeNameIdSupplier = () -> "testId";
  }

  @Test
  public void testMutate_BasicFunctionCall() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, fnName, null, null);
    Node callNode = getFunctionCallNode("testFunctionCall", fnNode);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // TODO: Add assertions to check the mutated node.
  }

  @Test
  public void testMutate_FunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, fnName, null, null);
    Node callNode = getFunctionCallNode("testFunctionCall", fnNode);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("arg1", Node.newString("arg1"));
    argMap.put("arg2", Node.newNumber(1));

    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // TODO: Add assertions to check the mutated node and the argument map.
  }

  @Test
  public void testMutate_FunctionInLoop() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, fnName, null, null);
    Node callNode = getFunctionCallNode("testFunctionCall", fnNode);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // TODO: Add assertions to check the mutated node and the argument map.
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_NullFnNode() {
    String fnName = null;
    Node fnNode = null;
    Node callNode = Node.newFunctionCall(Token.CALL, Node.newString("test"));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_NullCallNode() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, fnName, null, null);
    Node callNode = null;
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test
  public void testMutate_FunctionWithModifiedParameters() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, fnName, null, null);
    Node callNode = getFunctionCallNode("testFunctionCall", fnNode);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Set<String> namesToAlias = Sets.newHashSet("arg1");
    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("arg1", Node.newString("arg1"));
    argMap.put("arg2", Node.newNumber(1));

    Node newFnNode = mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // TODO: Add assertions to check the mutated node and the argument map.
  }

  private Node getFunctionCallNode(String name, Node fnNode) {
    Node callNode = Node.newFunctionCall(Token.CALL, Node.newString(name));
    callNode.setParent(NodeUtil.getFunctionBody(fnNode));
    Node argNode = NodeUtil.newString("arg");
    Node argList = Node.newList(Token.LIST, argNode);
    callNode.setChildren(Lists.newArrayList(argList));
    return callNode;
  }

  @Test
  public void testReplaceReturns_BasicFunction() {
    String labelName = "testLabel";
    Node block = Node.newBlock(Token.BLOCK);
    String resultName = "result";

    Node result = replaceReturns(block, resultName, labelName, false);

    // TODO: Add assertions to check the result.
  }

  @Test
  public void testReplaceReturns_FunctionWithResult() {
    String labelName = "testLabel";
    Node block = Node.newBlock(Token.BLOCK);
    Node resultNode = Node.newNumber(1);
    String resultName = "result";

    Node result = replaceReturns(block, resultName, labelName, true);

    // TODO: Add assertions to check the result.
  }

  @Test
  public void testReplaceReturns_FunctionWithoutResult() {
    String labelName = "testLabel";
    Node block = Node.newBlock(Token.BLOCK);

    Node result = replaceReturns(block, null, labelName, false);

    // TODO: Add assertions to check the result.
  }

  @Test
  public void testFixUnitializedVarDeclarations_SimpleBlock() {
    Node block = Node.newBlock(Token.BLOCK);
    Node varNode = NodeUtil.newVarNode("var", Node.newString("var"));
    block.addChildToBack(varNode);

    fixUnitializedVarDeclarations(block);

    // TODO: Add assertions to check the block.
  }

  @Test
  public void testFixUnitializedVarDeclarations_FunctionBlock() {
    Node fnNode = Node.newFunctionNode(Token.FUNCTION, null, null, null);
    Node block = NodeUtil.getFunctionBody(fnNode);
    Node varNode = NodeUtil.newVarNode("var", Node.newString("var"));
    block.addChildToBack(varNode);

    fixUnitializedVarDeclarations(block);

    // TODO: Add assertions to check the block.
  }

  @Test
  public void testFixUnitializedVarDeclarations_LoopStructure() {
    Node loopNode = Node.newLoop(Token.FOR, Node.newString("loop"));
    Node block = NodeUtil.newBlock(Token.BLOCK);
    Node varNode = NodeUtil.newVarNode("var", Node.newString("var"));
    block.addChildToBack(varNode);
    loopNode.setBody(block);

    fixUnitializedVarDeclarations(loopNode);

    // TODO: Add assertions to check the loopNode.
  }
}