import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Traversal;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ScopedAliasesTest {

 @Test
 public void testBasicFunctionality() {
 ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
 Node rootNode = new Node(); // Dummy root node
 scopedAliases.process(null, rootNode);
 Traversal traversal = new ScopedAliases.Traversal();
 NodeTraversal.traverse(scopedAliases, rootNode, traversal);

 // Assert that no errors were reported
 assertTrue(traversal.hasErrors() == false);
 }

 @Test
 public void testGoogScopeFunctionCalledImproperly() {
 Node rootNode = new Node();
 Node googScopeCall = new Node(Token.CALL, "goog.scope");
 googScopeCall.addChild(new Node(Token.FUNCTION, null, null));
 rootNode.addChild(googScopeCall);

 ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
 scopedAliases.process(null, rootNode);
 Traversal traversal = new ScopedAliases.Traversal();
 NodeTraversal.traverse(scopedAliases, rootNode, traversal);

 // Assert that an error was reported
 assertTrue(traversal.hasErrors());
 assertFalse(traversal.getScopeCalls().isEmpty());
}

 @Test
 public void testGoogScopeFunctionWithBadParameters() {
 Node rootNode = new Node();
 Node googScopeCall = new Node(Token.CALL, "goog.scope");
 Node functionNode = new Node(Token.FUNCTION, null, null);
 Node parameterNode = new Node(Token.VAR, null, null);
 Node parameterNameNode = new Node(Token.NAME, "param");
 parameterNameNode.addChild(parameterNameNode);
 functionNode.addChild(parameterNameNode);
 googScopeCall.addChild(functionNode);
 rootNode.addChild(googScopeCall);

 ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
 scopedAliases.process(null, rootNode);
 Traversal traversal = new ScopedAliases.Traversal();
 NodeTraversal.traverse(scopedAliases, rootNode, traversal);

 // Assert that an error was reported
 assertTrue(traversal.hasErrors());
 assertFalse(traversal.getScopeCalls().isEmpty());
}

 @Test
 public void testGoogScopeFunctionWithReturnStatement() {
 Node rootNode = new Node();
 Node googScopeCall = new Node(Token.CALL, "goog.scope");
 Node functionNode = new Node(Token.FUNCTION, null, null);
 Node returnStatementNode = new Node(Token.RETURN, null, null);
 functionNode.addChild(returnStatementNode);
 googScopeCall.addChild(functionNode);
 rootNode.addChild(googScopeCall);

 ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
 scopedAliases.process(null, rootNode);
 Traversal traversal = new ScopedAliases.Traversal();
 NodeTraversal.traverse(scopedAliases, rootNode, traversal);

 // Assert that an error was reported
 assertTrue(traversal.hasErrors());
 assertFalse(traversal.getScopeCalls().isEmpty());
}

 @Test
 public void testGoogScopeFunctionWithThisKeyword() {
 Node rootNode = new Node();
 Node googScopeCall = new Node(Token.CALL, "goog.scope");
 Node functionNode = new Node(Token.FUNCTION, null, null);
 Node thisKeywordNode = new Node(Token.THIS, null, null);
 Node statementNode = new Node(Token.ASSIGN, null, thisKeywordNode);
 functionNode.addChild(statementNode);
 googScopeCall.addChild(functionNode);
 rootNode.addChild(googScopeCall);

 ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
 scopedAliases.process(null, rootNode);
 Traversal traversal = new ScopedAliases.Traversal();
 NodeTraversal.traverse(scopedAliases, rootNode, traversal);

 // Assert that an error was reported
 assertTrue(traversal.hasErrors());
 assertFalse(traversal.getScopeCalls().isEmpty());
}

 @Test
 public void testGoogScopeFunctionWithThrowStatement() {
 Node rootNode = new Node();
 Node googScopeCall = new Node(Token.CALL, "goog.scope");
 Node functionNode = new Node(Token.FUNCTION, null, null);
 Node throwStatementNode = new Node(Token.THROW, null, null);
 functionNode.addChild(throwStatementNode);
 googScopeCall.addChild(functionNode);
 rootNode.addChild(googScopeCall);

 ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
 scopedAliases.process(null, rootNode);
 Traversal traversal = new ScopedAliases.Traversal();
 NodeTraversal.traverse(scopedAliases, rootNode, traversal);

 // Assert that an error was reported
 assertTrue(traversal.hasErrors());
 assertFalse(traversal.getScopeCalls().isEmpty());
}

 @Test
 public void testGoogScopeFunctionWithAliasRedefinition() {
 Node rootNode = new Node();
 Node googScopeCall = new Node(Token.CALL, "goog.scope");
 Node functionNode = new Node(Token.FUNCTION, null, null);
 Node aliasNode = new Node(Token.NAME, "alias");
 Node assignmentNode = new Node(Token.ASSIGN, null, aliasNode);
 Node aliasAssignmentNode = new Node(Token.ASSIGN, null, aliasNode);
 Node aliasValueNode = new Node(Token.NAME, "value");
 aliasAssignmentNode.addChild(aliasValueNode);
 functionNode.addChild(assignmentNode);
 functionNode.addChild(aliasAssignmentNode);
 googScopeCall.addChild(functionNode);
 rootNode.addChild(googScopeCall);

 ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
 scopedAliases.process(null, rootNode);
 Traversal traversal = new ScopedAliases.Traversal();
 NodeTraversal.traverse(scopedAliases, rootNode, traversal);

 // Assert that an error was reported
 assertTrue(traversal.hasErrors());
 assertFalse(traversal.getScopeCalls().isEmpty());
}

 @Test
 public void testGoogScopeFunctionWithNonAliasLocalVariable() {
 Node rootNode = new Node();
 Node googScopeCall = new Node(Token.CALL, "goog.scope");
 Node functionNode = new Node(Token.FUNCTION, null, null);
 Node variableNode = new Node(Token.VAR, null, null);
 Node variableNameNode = new Node(Token.NAME, "variable");
 variableNode.addChild(variableNameNode);
 functionNode.addChild(variableNode);
 Node assignmentNode = new Node(Token.ASSIGN, null, variableNameNode);
 Node valueNode = new Node(Token.NAME, "value");
 assignmentNode.addChild(valueNode);
 functionNode.addChild(assignmentNode);
 googScopeCall.addChild(functionNode);
 rootNode.addChild(googScopeCall);

 ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
 scopedAliases.process(null, rootNode);
 Traversal traversal = new ScopedAliases.Traversal();
 NodeTraversal.traverse(scopedAliases, rootNode, traversal);

 // Assert that an error was reported
 assertTrue(traversal.hasErrors());
 assertFalse(traversal.getScopeCalls().isEmpty());
}
}