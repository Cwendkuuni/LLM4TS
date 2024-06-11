import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Supplier;
import com.google.common.base.Preconditions;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class FunctionToBlockMutatorTest {

 private AbstractCompiler compiler;
 private Supplier<String> safeNameIdSupplier;
 private FunctionToBlockMutator functionToBlockMutator;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 safeNameIdSupplier = () -> "test_id";
 functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 }

 @Test
 public void testMutateFunctionWithNoArguments() {
 Node functionNode = Node.newFunction(
 "testFunction",
 Node.newList(Token.VOID, Node.newList(), Node.newList()),
 Node.newBlock(
 Node.newStatement(Node.newReturn(Node.newNumberLiteral(5)))
 )
 );
 Node callNode = Node.newCall(Node.newName("testFunction"), Lists.newArrayList());
 String resultName = "result";
 boolean isCallInLoop = false;
 Node expected = functionToBlockMutator.mutate(
 null, functionNode, callNode, resultName, false, isCallInLoop
 );

 // Basic functionality test
 Node actual = NodeTraversal.deepClone(expected);
 Node testFunction = NodeUtil.getFunctionFromName(actual, "testFunction");
 Preconditions.checkState(testFunction != null);
 Node result = NodeUtil.getFirstChildOfType(testFunction, Token.VAR);
 Preconditions.checkState(result != null);
 Preconditions.checkState(result.getFirstChild().isNumber());
 Preconditions.checkState(result.getFirstChild().getInt() == 5);
 }

 @Test
 public void testMutateFunctionWithArguments() {
 Node functionNode = Node.newFunction(
 "testFunction",
 Node.newList(Node.newList(Token.STRING, "arg1"), Token.INT, 1),
 Node.newList(),
 Node.newBlock(
 Node.newStatement(Node.newReturn(Node.newName("arg1")))
 )
 );
 Node callNode = Node.newCall(
 Node.newName("testFunction"),
 Lists.newArrayList(Node.newString("testArg1"), Node.newNumberLiteral(1))
 );
 String resultName = "result";
 boolean isCallInLoop = false;
 Node expected = functionToBlockMutator.mutate(
 null, functionNode, callNode, resultName, false, isCallInLoop
 );

 // Basic functionality test
 Node actual = NodeTraversal.deepClone(expected);
 Node testFunction = NodeUtil.getFunctionFromName(actual, "testFunction");
 Preconditions.checkState(testFunction != null);
 Node arg1 = NodeUtil.getFirstChildOfType(testFunction, Token.ARG);
 Preconditions.checkState(arg1 != null);
 Preconditions.checkState(arg1.getString() == "testArg1");
 Node result = NodeUtil.getFirstChildOfType(testFunction, Token.VAR);
 Preconditions.checkState(result != null);
 Preconditions.checkState(result.getFirstChild().getName() == "arg1");
 }

 @Test(expected = AssertionError.class)
 public void testMutateFunctionWithNullName() {
 Node functionNode = Node.newFunction(
 "testFunction",
 Node.newList(),
 Node.newList(),
 Node.newBlock(
 Node.newStatement(Node.newReturn(Node.newNumberLiteral(5)))
 )
 );
 Node callNode = Node.newCall(Node.newName("testFunction"), Lists.newArrayList());
 String resultName = null;
 boolean isCallInLoop = false;
 functionToBlockMutator.mutate(null, functionNode, callNode, resultName, false, isCallInLoop);
 }

 @Test(expected = AssertionError.class)
 public void testMutateFunctionWithNullFunctionNode() {
 Node callNode = Node.newCall(Node.newName("testFunction"), Lists.newArrayList());
 String resultName = "result";
 boolean isCallInLoop = false;
 functionToBlockMutator.mutate(null, null, callNode, resultName, false, isCallInLoop);
 }

 @Test(expected = AssertionError.class)
 public void testMutateFunctionWithNullCallNode() {
 Node functionNode = Node.newFunction(
 "testFunction",
 Node.newList(Node.newList(Token.STRING, "arg1"), Token.INT, 1),
 Node.newList(),
 Node.newBlock(
 Node.newStatement(Node.newReturn(Node.newNumberLiteral(5)))
 )
 );
 String resultName = "result";
 boolean isCallInLoop = false;functionToBlockMutator.mutate(functionNode, null, resultName, false, isCallInLoop);
 }

 @Test
 public void testMutateFunctionWithInlineRenamer() {
 Node functionNode = Node.newFunction(
 "testFunction",
 Node.newList(Node.newList(Token.STRING, "arg1"), Token.INT, 1),
 Node.newList(),
 Node.newBlock(
 Node.newStatement(Node.newReturn(Node.newName("arg1")))
 )
 );
 Node callNode = Node.newCall(
 Node.newName("testFunction"),
 Lists.newArrayList(Node.newString("testArg1"), Node.newNumberLiteral(1))
 );
 String resultName = "result";
 boolean isCallInLoop = false;
 Node expected = functionToBlockMutator.mutate(
 null, functionNode, callNode, resultName, false, isCallInLoop
 );

 // Basic functionality test
 Node actual = NodeTraversal.deepClone(expected);
 Node testFunction = NodeUtil.getFunctionFromName(actual, "testFunction");
 Preconditions.checkState(testFunction != null);
 Node arg1 = NodeUtil.getFirstChildOfType(testFunction, Token.ARG);
 Preconditions.checkState(arg1 != null);
 Preconditions.checkState(arg1.getString() == "testArg1_inline_test_id_0");
 Node result = NodeUtil.getFirstChildOfType(testFunction, Token.VAR);
 Preconditions.checkState(result != null);
 Preconditions.checkState(result.getFirstChild().getName() == "arg1_inline_test_id_1");
 }

 @Test
 public void testMutateFunctionWithLoop() {
 Node functionNode = Node.newFunction(
 "testFunction",
 Node.newList(Node.newList(Token.STRING, "arg1"), Token.INT, 1),
 Node.newList(),
 Node.newBlock(
 Node.newStatement(Node.newReturn(Node.newName("arg1")))
 ),
 Node.newStatement(Node.newLabel("loop"))
 );
 Node callNode = Node.newCall(
 Node.newName("testFunction"),
 Lists.newArrayList(Node.newString("testArg1"), Node.newNumberLiteral(1))
 );
 String resultName = "result";
 boolean isCallInLoop = true;
 Node expected = functionToBlockMutator.mutate(
 null, functionNode, callNode, resultName, false, isCallInLoop
 );

 // Basic functionality test
 Node actual = NodeTraversal.deepClone(expected);
 Node testFunction = NodeUtil.getFunctionFromName(actual, "testFunction");
 Preconditions.checkState(testFunction != null);
 Node arg1 = NodeUtil.getFirstChildOfType(testFunction, Token.ARG);
 Preconditions.checkState(arg1 != null);
 Preconditions.checkState(arg1.getString() == "testArg1_inline_test_id_0");
 Node result = NodeUtil.getFirstChildOfType(testFunction, Token.VAR);
 Preconditions.checkState(result != null);
 Preconditions.checkState(result.getFirstChild().getName() == "arg1_inline_test_id_1");
 Node loopLabel = NodeUtil.getFirstChildOfType(testFunction, Token.LABEL);
 Preconditions.checkState(loopLabel != null);
 Preconditions.checkState(loopLabel.getString() == "loop_inline_test_id_2");
 Node statement = NodeUtil.getFirstChildOfType(testFunction, Token.STATEMENT);
 Preconditions.checkState(statement != null);
 Preconditions.checkState(statement.getFirstChild().getType() == Token.BREAK);
 }

 @Test
 public void testMutateFunctionWithResultMustBeSet() {
 Node functionNode = Node.newFunction(
 "testFunction",
 Node.newList(Node.newList(Token.STRING, "arg1"), Token.INT, 1),
 Node.newList(),
 Node.newBlock(
 Node.newStatement(Node.newReturn(Node.newName("arg1"))),
 Node.newStatement(Node.newAssign(Node.newName("result"), Node.newName("arg1")))
 )
 );
 Node callNode = Node.newCall(
 Node.newName("testFunction"),
 Lists.newArrayList(Node.newString("testArg1"), Node.newNumberLiteral(1))
 );
 String resultName = "result";
 boolean isCallInLoop = false;
 Node expected = functionToBlockMutator.mutate(
 null, functionNode, callNode, resultName, true, isCallInLoop
 );

 // Basic functionality test
 Node actual = NodeTraversal.deepClone(expected);
 Node testFunction = NodeUtil.getFunctionFromName(actual, "testFunction");
 Preconditions.checkState(testFunction != null);
 Node arg1 = NodeUtil.getFirstChildOfType(testFunction, Token.ARG);
 Preconditions.checkState(arg1 != null);
 Preconditions.checkState(arg1.getString() == "testArg1");
 Node result = NodeUtil.getFirstChildOfType(testFunction, Token.VAR);
 Preconditions.checkState(result != null);
 Preconditions.checkState(result.getFirstChild().getName() == "arg1");
 Node resultNode = NodeUtil.getFirstChildOfType(testFunction, Token.VAR);
 Preconditions.checkState(resultNode != null);
 Preconditions.checkState(resultNode.getFirstChild().getName() == "result");
 Preconditions.checkState(NodeUtil.isStatementBlock(testFunction.getLastChild()));
 Node statement = NodeUtil.getFirstChildOfType(testFunction.getLastChild(), Token.STATEMENT);
 Preconditions.checkState(statement != null);
 Preconditions.checkState(statement.getFirstChild().getType() == Token.ASSIGN);
 Preconditions.checkState(statement.getFirstChild().getFirstChild().getName() == "result");
 Preconditions.checkState(statement.getFirstChild().getLastChild().getName() == "arg1");
 }
}