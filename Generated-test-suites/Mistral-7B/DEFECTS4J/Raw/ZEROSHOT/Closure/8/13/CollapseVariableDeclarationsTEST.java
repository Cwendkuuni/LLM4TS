import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.traverse.AbstractPostOrderCallback;
import com.google.javascript.jscomp.traverse.NodeTraversal;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

 private AbstractCompiler compiler;
 private CollapseVariableDeclarations collapseVariableDeclarations;

 @org.junit.Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
 }

 @org.junit.Test
 public void testCollapseSingleDeclaration() {
 Node rootNode = new Node(Token.PROGRAM);
 Node varNode = new Node(Token.VAR);
 varNode.setString("a");
 Node valueNode = new Node(Token.NUMBER, 1);
 varNode.addChildToBack(valueNode);
 rootNode.addChildToBack(varNode);

 NodeTraversal traverse = new NodeTraversal(compiler, rootNode);
 traverse.traverse(collapseVariableDeclarations);

 assertEquals(1, rootNode.getChildCount());
 }

 @org.junit.Test
 public void testCollapseMultipleDeclarations() {
 Node rootNode = new Node(Token.PROGRAM);

 Node varNode1 = new Node(Token.VAR);
 varNode1.setString("a");
 Node valueNode1 = new Node(Token.NUMBER, 1);
 varNode1.addChildToBack(valueNode1);
 rootNode.addChildToBack(varNode1);

 Node varNode2 = new Node(Token.VAR);
 varNode2.setString("b");
 Node valueNode2 = new Node(Token.NUMBER, 2);
 varNode2.addChildToBack(valueNode2);
 rootNode.addChildToBack(varNode2);

 NodeTraversal traverse = new NodeTraversal(compiler, rootNode);
 traverse.traverse(collapseVariableDeclarations);

 assertEquals(1, rootNode.getChildCount());
 Node collapsedVarNode = rootNode.getFirstChild();
 assertEquals("a, b", collapsedVarNode.getString());
 assertEquals(2, collapsedVarNode.getChildCount());
 }

 @org.junit.Test
 public void testCollapseMultipleAssignments() {
 Node rootNode = new Node(Token.PROGRAM);

 Node varNode = new Node(Token.VAR);
 varNode.setString("a");
 rootNode.addChildToBack(varNode);

 Node assign1 = new Node(Token.ASSIGN);
 Node lhs1 = new Node(Token.NAME, "a");
 Node rhs1 = new Node(Token.NUMBER, 1);
 assign1.addChildToBack(lhs1);
 assign1.addChildToBack(rhs1);
 rootNode.addChildToBack(assign1);

 Node assign2 = new Node(Token.ASSIGN);
 Node lhs2 = new Node(Token.NAME, "a");
 Node rhs2 = new Node(Token.NUMBER, 2);
 assign2.addChildToBack(lhs2);
 assign2.addChildToBack(rhs2);
 rootNode.addChildToBack(assign2);

 Node assign3 = new Node(Token.ASSIGN);
 Node lhs3 = new Node(Token.NAME, "a");
 Node rhs3 = new Node(Token.NUMBER, 3);
 assign3.addChildToBack(lhs3);
 assign3.addChildToBack(rhs3);
 rootNode.addChildToBack(assign3);

 NodeTraversal traverse = new NodeTraversal(compiler, rootNode);
 traverse.traverse(collapseVariableDeclarations);

 assertEquals(1, rootNode.getChildCount());
 Node collapsedAssignNode = rootNode.getFirstChild();
 Node collapsedVarNode = collapsedAssignNode.getFirstChild();
 assertEquals("a = 1, a = 2, a = 3", collapsedVarNode.getString());
 assertEquals(1, collapsedAssignNode.getChildCount());
 }

 @org.junit.Test
 public void testCollapseMultipleDeclarationsAndAssignments() {
 Node rootNode = new Node(Token.PROGRAM);

 Node varNode1 = new Node(Token.VAR);
 varNode1.setString("a");
 Node valueNode1 = new Node(Token.NUMBER, 1);
 varNode1.addChildToBack(valueNode1);
 rootNode.addChildToBack(varNode1);

 Node assign1 = new Node(Token.ASSIGN);
 Node lhs1 = new Node(Token.NAME, "a");
 Node rhs1 = new Node(Token.NUMBER, 1);
 assign1.addChildToBack(lhs1);
 assign1.addChildToBack(rhs1);
 rootNode.addChildToBack(assign1);

 Node varNode2 = new Node(Token.VAR);
 varNode2.setString("b");
 Node valueNode2 = new Node(Token.NUMBER, 2);
 varNode2.addChildToBack(valueNode2);
 rootNode.addChildToBack(varNode2);

 Node assign2 = new Node(Token.ASSIGN);
 Node lhs2 = new Node(Token.NAME, "a");
 Node rhs2 = new Node(Token.NUMBER, 2);
 assign2.addChildToBack(lhs2);
 assign2.addChildToBack(rhs2);
 rootNode.addChildToBack(assign2);

 Node assign3 = new Node(Token.ASSIGN);
 Node lhs3 = new Node(Token.NAME, "b");
 Node rhs3 = new Node(Token.NUMBER, 3);
 assign3.addChildToBack(lhs3);
 assign3.addChildToBack(rhs3);
 rootNode.addChildToBack(assign3);

 NodeTraversal traverse = new NodeTraversal(compiler, rootNode);
 traverse.traverse(collapseVariableDeclarations);

 assertEquals(1, rootNode.getChildCount());
 Node collapsedAssignNode = rootNode.getFirstChild();
 Node collapsedVarNode = collapsedAssignNode.getFirstChild();
 assertEquals("a = 1, a = 2, b = 3", collapsedVarNode.getString());
 assertEquals(1, collapsedAssignNode.getChildCount());
 }

 @org.junit.Test
 public void testScopeCollapsing() {
 Node rootNode = new Node(Token.PROGRAM);
 Node functionNode = new Node(Token.FUNCTION, null, null, null, null);
 rootNode.addChildToBack(functionNode);

 Node varNode1 = new Node(Token.VAR);
 varNode1.setString("a");
 Node valueNode1 = new Node(Token.NUMBER, 1);
 varNode1.addChildToBack(valueNode1);
 functionNode.addChildToBack(varNode1);

 Node varNode2 = new Node(Token.VAR);
 varNode2.setString("b");
 Node valueNode2 = new Node(Token.NUMBER, 2);
 varNode2.addChildToBack(valueNode2);
 functionNode.addChildToBack(varNode2);

 NodeTraversal traverse = new NodeTraversal(compiler, rootNode);
 Scope functionScope = traverse.getScope();
 assertEquals(2, functionScope.getVariables().size());

 collapseVariableDeclarations.process(rootNode, rootNode);
 assertEquals(1, functionScope.getVariables().size());
 Node collapsedVarNode = functionScope.getVariable("a");
 assertEquals("a, b", collapsedVarNode.getString());
 assertEquals(2, collapsedVarNode.getChildCount());
 }

 @org.junit.Test
 public void testCollapsingInsideFunction() {
 Node rootNode = new Node(Token.PROGRAM);
 Node functionNode = new Node(Token.FUNCTION, null, null, null, null);
 rootNode.addChildToBack(functionNode);

 Node ifNode = new Node(Token.IF);
 Node conditionNode = new Node(Token.BOOL, true);
 ifNode.addChildToBack(conditionNode);
 functionNode.addChildToBack(ifNode);

 Node varNode1 = new Node(Token.VAR);
 varNode1.setString("a");
 Node valueNode1 = new Node(Token.NUMBER, 1);
 varNode1.addChildToBack(valueNode1);
 ifNode.addChildToBack(varNode1);

 Node varNode2 = new Node(Token.VAR);
 varNode2.setString("b");
 Node valueNode2 = new Node(Token.NUMBER, 2);
 varNode2.addChildToBack(valueNode2);
 ifNode.addChildToBack(varNode2);

 NodeTraversal traverse = new NodeTraversal(compiler, rootNode);
 Scope functionScope = traverse.getScope();
 assertEquals(2, functionScope.getVariables().size());

 collapseVariableDeclarations.process(rootNode, rootNode);
 assertEquals(1, functionScope.getVariables().size());
 Node ifScope = traverse.getScope();
 assertEquals(2, ifScope.getVariables().size());
 Node collapsedVarNode = ifScope.getVariable("a");
 assertEquals("a, b", collapsedVarNode.getString());
 assertEquals(2, collapsedVarNode.getChildCount());
 }

 @org.junit.Test
 public void testCollapsingInsideLoop() {
 Node rootNode = new Node(Token.PROGRAM);
 Node functionNode = new Node(Token.FUNCTION, null, null, null, null);
 rootNode.addChildToBack(functionNode);

 Node forNode = new Node(Token.FOR);
 Node initNode = new Node(Token.NAME, "i");
 Node conditionNode = new Node(Token.BOOL, true);
 Node incrementNode = new Node(Token.ASSIGN, new Node(Token.NAME, "i"), new Node(Token.PLUS, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 1)));
 forNode.addChildToBack(initNode);
 forNode.addChildToBack(conditionNode);
 forNode.addChildToBack(incrementNode);
 functionNode.addChildToBack(forNode);

 Node varNode1 = new Node(Token.VAR);
 varNode1.setString("a");
 Node valueNode1 = new Node(Token.NUMBER, 1);
 varNode1.addChildToBack(valueNode1);
 forNode.addChildToBack(varNode1);

 Node varNode2 = new Node(Token.VAR);
 varNode2.setString("b");
 Node valueNode2 = new Node(Token.NUMBER, 2);
 varNode2.addChildToBack(valueNode2);
 forNode.addChildToBack(varNode2);

 NodeTraversal traverse = new NodeTraversal(compiler, rootNode);
 Scope functionScope = traverse.getScope();
 assertEquals(2, functionScope.getVariables().size());

 collapseVariableDeclarations.process(rootNode, rootNode);
 assertEquals(1, functionScope.getVariables().size());
 Node forScope = traverse.getScope();
 assertEquals(2, forScope.getVariables().size());
 Node collapsedVarNode = forScope.getVariable("a");
 assertEquals("a, b", collapsedVarNode.getString());
 assertEquals(2, collapsedVarNode.getChildCount());
 }
}