import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.common.base.Preconditions;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private boolean removeNoOpStatements;

 @BeforeClass
 public void setup() {
 compiler = new Compiler();
 removeNoOpStatements = true;
 }

 @Test
 public void testConstructor() {
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 assertNull(elimination.curCfg);
 assertNull(elimination.cfgStack.peekFirst());
 }

 @Test
 public void testEnterScope() {
 Node root = new Node(Token.SCRIPT);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.enterScope(new NodeTraversal(compiler, root));
 assertNotNull(elimination.curCfg);
 assertSame(elimination.curCfg, elimination.cfgStack.peekFirst());
 }

 @Test
 public void testExitScope() {
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.enterScope(new NodeTraversal(compiler, new Node(Token.SCRIPT)));
 elimination.exitScope(new NodeTraversal(compiler, new Node(Token.SCRIPT)));
 assertNull(elimination.curCfg);
 assertNull(elimination.cfgStack.peekFirst());
 }

 @Test
 public void testProcess() {
 Node root = new Node(Token.SCRIPT);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.process(null, root);
 assertSame(elimination.cfgStack.peekFirst(), elimination.curCfg);
 }

 @Test
 public void testVisitNull() {
 NodeTraversal traversal = new NodeTraversal(compiler, new Node(Token.SCRIPT));
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(traversal, null, null);
 }

 @Test
 public void testVisitFunctionOrScript() {
 Node root = new Node(Token.SCRIPT);
 Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "func"), new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))));
 Node functionCall = new Node(Token.CALL, functionNode, new Node(Token.NULL));
 root.addChild(functionCall);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), functionCall, root);
 assertNull(elimination.curCfg);
 }

 @Test
 public void testVisitStatementWithNoSideEffects() {
 Node root = new Node(Token.SCRIPT);
 Node statement = new Node(Token.ASSIGN, new Node(Token.IDENTIFIER, "a"), new Node(Token.NULL));
 Node statementWithoutSideEffects = new Node(Token.ASSIGN, new Node(Token.IDENTIFIER, "a"), new Node(Token.NUMBER, 0));
 root.addChild(statementWithoutSideEffects);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), statement, root);
 assertNull(elimination.curCfg);
 }

 @Test
 public void testVisitStatementWithSideEffects() {
 Node root = new Node(Token.SCRIPT);
 Node statement = new Node(Token.ASSIGN, new Node(Token.IDENTIFIER, "a"), new Node(Token.IDENTIFIER, "b"));
 Node statementWithSideEffects = new Node(Token.ASSIGN, new Node(Token.IDENTIFIER, "a"), statement);
 root.addChild(statementWithSideEffects);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), statementWithSideEffects, root);
 assertNotNull(elimination.curCfg);
 }

 @Test
 public void testRemoveUnconditionalBranching() {
 Node root = new Node(Token.SCRIPT);
 Node breakNode = new Node(Token.BREAK);
 Node continueNode = new Node(Token.CONTINUE);
 Node returnNode = new Node(Token.RETURN);
 Node target = new Node(Token.RETURN);
 Node statementBreak = new Node(Token.BLOCK, breakNode);
 Node statementContinue = new Node(Token.BLOCK, continueNode);
 Node statementReturn = new Node(Token.BLOCK, returnNode);
 Node loop = new Node(Token.BLOCK, new Node(Token.WHILE, new Node(Token.IDENTIFIER, "i"), new Node(Token.LESS, new Node(Token.IDENTIFIER, "i"), new Node(Token.NUMBER, 5))), statementBreak);
 root.addChild(loop);
 root.addChild(statementReturn);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), statementReturn, root);
 Node targetAfterBreak = elimination.tryRemoveUnconditionalBranching(breakNode);
 Node targetAfterContinue = elimination.tryRemoveUnconditionalBranching(continueNode);
 assertSame(target, targetAfterBreak);
 assertSame(target, targetAfterContinue);
 assertSame(target, elimination.curCfg.getDirectedGraphNode(returnNode).getAnnotation());
 }

 @Test
 public void testTryRemoveUnconditionalBranchingWithNull() {
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 assertNull(elimination.tryRemoveUnconditionalBranching(null));
 }

 @Test
 public void testTryRemoveUnconditionalBranchingWithNonBranchNode() {
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 assertSame(elimination, elimination.tryRemoveUnconditionalBranching(new Node(Token.IDENTIFIER, "identifier")));
 }

 @Test
 public void testTryRemoveUnconditionalBranchingWithConditionalBranch() {
 Node root = new Node(Token.SCRIPT);
 Node ifNode = new Node(Token.IF, new Node(Token.IDENTIFIER, "condition"), new Node(Token.BLOCK, new Node(Token.RETURN)), new Node(Token.BLOCK, new Node(Token.RETURN)));
 Node elseNode = new Node(Token.ELSE, new Node(Token.BLOCK, new Node(Token.RETURN)));
 root.addChild(ifNode);
 root.addChild(elseNode);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), ifNode, root);
 assertFalse(NodeUtil.mayHaveSideEffects(ifNode));
 assertFalse(NodeUtil.mayHaveSideEffects(elseNode));
 }

 @Test(expected = AssertionError.class)
 public void testTryRemoveUnconditionalBranchingWithInvalidBranchType() {
 Node root = new Node(Token.SCRIPT);
 Node ifNode = new Node(Token.IF, new Node(Token.IDENTIFIER, "condition"), new Node(Token.BLOCK, new Node(Token.RETURN)), new Node(Token.BLOCK, new Node(Token.RETURN), new Node(Token.RETURN, new Node(Token.NULL))));
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), ifNode, root);
 elimination.tryRemoveUnconditionalBranching(ifNode);
 }

 @Test
 public void testRemoveDeadExprStatementSafely() {
 Node root = new Node(Token.SCRIPT);
 Node deadStatement = new Node(Token.RETURN);
 Node statement = new Node(Token.BLOCK, deadStatement);
 root.addChild(statement);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), statement, root);
 assertNull(elimination.curCfg);
 }

 @Test
 public void testRemoveDeadExprStatementSafelyWithFunction() {
 Node root = new Node(Token.SCRIPT);
 Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "func"), new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))));
 Node functionCall = new Node(Token.CALL, functionNode, new Node(Token.NULL));
 Node statement = new Node(Token.BLOCK, new Node(Token.ASSIGN, new Node(Token.IDENTIFIER, "a"), functionCall));
 root.addChild(statement);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), statement, root);
 assertNull(elimination.curCfg);
 }

 @Test
 public void testRemoveDeadExprStatementSafelyWithBlock() {
 Node root = new Node(Token.SCRIPT);
 Node block = new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL)));
 Node statement = new Node(Token.BLOCK, block);
 root.addChild(statement);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), statement, root);
 assertNull(elimination.curCfg);
 }

 @Test
 public void testRemoveDeadExprStatementSafelyWithTryCatch() {
 Node root = new Node(Token.SCRIPT);
 Node tryNode = new Node(Token.TRY, new Node(Token.BLOCK, new Node(Token.RETURN)));
 Node catchNode = new Node(Token.CATCH, new Node(Token.IDENTIFIER, "exception"), new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))));
 Node finallyNode = new Node(Token.FINALLY, new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))));
 Node statement = new Node(Token.BLOCK, new Node(Token.RETURN), tryNode, catchNode, finallyNode);
 root.addChild(statement);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), statement, root);
 assertNull(elimination.curCfg);
 }

 @Test
 public void testRemoveDeadExprStatementSafelyWithDoWhile() {
 Node root = new Node(Token.SCRIPT);
 Node doNode = new Node(Token.DO, new Node(Token.BLOCK, new Node(Token.RETURN)), new Node(Token.WHILE, new Node(Token.IDENTIFIER, "condition")));
 Node statement = new Node(Token.BLOCK, doNode);
 root.addChild(statement);
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
 elimination.visit(new NodeTraversal(compiler, root), statement, root);
 assertNull(elimination.curCfg);
 }

 private static Compiler compiler() {
 return new Compiler();
 }

 private static Node newNode(int type, Node... children) {
 return new Node(type, children);
 }

 private static Node newNode(int type) {
 return new Node(type);
 }

 private static Node newNode(int type, int value) {
 return new Node(type, new Node(Token.NUMBER, value));
 }

 private static Node newNode(int type, String value) {
 return new Node(type, new Node(Token.STRING, value));
 }

 private static Node newNode(int type, Node value) {
 return new Node(type, value);
 }

 private static Node newNode(int type, Node left, Node right) {
 return new Node(type, left, right);
 }

 private static Node newNode(int type, Node left, Node right, Node... moreChildren) {
 return new Node(type, left, right, moreChildren);
 }

 private static Node newNode(int type, Node... children) {
 return new Node(type, children);
 }

 private static void assertNotNull(Node node) {
 assertNotNull(node, "Node is null");
 }

 private static void assertSame(Node expected, Node actual) {
 assertSame(expected, actual, "Nodes do not match");
 }

 private static void assertSame(Object expected, Object actual) {
 Assert.assertEquals(expected, actual);
 }

 private static void assertNull(Node node) {
 assertNull(node, "Node is not null");
 }

}