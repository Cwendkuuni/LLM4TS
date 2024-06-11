import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

 private ScopeCreator syntacticScopeCreator;
 private Scope parentScope;
 private Node rootNode;

 @Before
 public void setup() {
 syntacticScopeCreator = new SyntacticScopeCreator(new NoOpCompiler());
 parentScope = new Scope(null, null);
 rootNode = NodeUtil.createFunctionNode("testFunction", null, null);
 }

 @Test
 public void testCreateScope() {
 Scope scope = syntacticScopeCreator.createScope(rootNode, parentScope);
 assertNotNull(scope);
 assertSame(parentScope, scope.getParent());
 }

 @Test
 public void testDeclarationOfSingleVariable() {
 Node variableNode = NodeUtil.createVarNode("x");
 syntacticScopeCreator.declareVar("x", variableNode, rootNode, null, null, rootNode);
 Scope scope = syntacticScopeCreator.createScope(rootNode, parentScope);
 assertNotNull(scope.getVar("x"));
 }

 @Test
 public void testDeclarationOfMultipleVariables() {
 Node variablesNode = NodeUtil.createVarNode("x, y, z");
 syntacticScopeCreator.declareVar("x", variablesNode.getFirstChild(), variablesNode, rootNode, null, rootNode);
 syntacticScopeCreator.declareVar("y", variablesNode.getNext().getNext(), variablesNode, rootNode, null, rootNode);
 syntacticScopeCreator.declareVar("z", variablesNode.getNext(), variablesNode, rootNode, null, rootNode);
 Scope scope = syntacticScopeCreator.createScope(rootNode, parentScope);
 assertNotNull(scope.getVar("x"));
 assertNotNull(scope.getVar("y"));
 assertNotNull(scope.getVar("z"));
 }

 @Test
 public void testDeclarationOfFunction() {
 Node functionNode = NodeUtil.createFunctionNode("myFunction", null, null);
 syntacticScopeCreator.declareVar("myFunction", functionNode.getFirstChild(), functionNode, rootNode, null, rootNode);
 Scope scope = syntacticScopeCreator.createScope(rootNode, parentScope);
 assertNotNull(scope.getVar("myFunction"));
 }

 @Test
 public void testDeclarationOfVariableWithJSDocInfo() {
 JSDocInfo docInfo = NodeUtil.createJSDocInfo("@param {number} x");
 Node variableNode = NodeUtil.createVarNode("x", docInfo);
 syntacticScopeCreator.declareVar("x", variableNode, rootNode, null, null, rootNode);
 Scope scope = syntacticScopeCreator.createScope(rootNode, parentScope);
 assertEquals("number", scope.getVar("x").getType().getClass().getSimpleName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateDeclarationOfVariable() {
 Node variableNode = NodeUtil.createVarNode("x");
 Node duplicateVariableNode = NodeUtil.createVarNode("x");
 syntacticScopeCreator.declareVar("x", variableNode, rootNode, null, null, rootNode);
 syntacticScopeCreator.declareVar("x", duplicateVariableNode, rootNode, null, null, rootNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateDeclarationOfFunction() {
 Node functionNode1 = NodeUtil.createFunctionNode("myFunction", null, null);
 Node functionNode2 = NodeUtil.createFunctionNode("myFunction", null, null);
 syntacticScopeCreator.declareVar("myFunction", functionNode1.getFirstChild(), functionNode1, rootNode, null, rootNode);
 syntacticScopeCreator.declareVar("myFunction", functionNode2.getFirstChild(), functionNode2, rootNode, null, rootNode);
 }

 @Test
 public void testRedeclarationOfVariableWithDifferentParents() {
 Node variableNode1 = NodeUtil.createVarNode("x");
 Node variableNode2 = NodeUtil.createVarNode("x");
 Node parent1 = NodeUtil.createFunctionNode("parent1");
 Node parent2 = NodeUtil.createFunctionNode("parent2");
 syntacticScopeCreator.declareVar("x", variableNode1, variableNode1, parent1, null, variableNode1);
 syntacticScopeCreator.declareVar("x", variableNode2, variableNode2, parent2, null, variableNode2);
 Scope scope = syntacticScopeCreator.createScope(parent1, null);
 assertNotNull(scope.getVar("x"));
 }

 @Test
 public void testRedeclarationOfVariableWithSameParent() {
 Node variableNode1 = NodeUtil.createVarNode("x");
 Node variableNode2 = NodeUtil.createVarNode("x");
 Node parent = NodeUtil.createFunctionNode("parent");
 syntacticScopeCreator.declareVar("x", variableNode1, variableNode1, parent, null, variableNode1);
 syntacticScopeCreator.declareVar("x", variableNode2, variableNode2, parent, null, variableNode2);
 Scope scope = syntacticScopeCreator.createScope(parent, null);
 assertNotNull(scope.getVar("x"));
 }

 @Test
 public void testDeclarationOfVariableWithSameNameButDifferentLocation() {
 Node variableNode1 = NodeUtil.createVarNode("x");
 Node variableNode2 = NodeUtil.createVarNode("x");
 Node parent = NodeUtil.createFunctionNode("parent");
 Node rootNode1 = NodeUtil.createFunctionNode("rootNode1");
 Node rootNode2 = NodeUtil.createFunctionNode("rootNode2");
 syntacticScopeCreator.declareVar("x", variableNode1, variableNode1, parent, null, rootNode1);
 syntacticScopeCreator.declareVar("x", variableNode2, variableNode2, parent, null, rootNode2);
 Scope scope = syntacticScopeCreator.createScope(rootNode1, null);
 assertNotNull(scope.getVar("x"));
 }

 @Test
 public void testDeclarationOfVariableWithSameNameButDifferentType() {
 Node variableNode1 = NodeUtil.createVarNode("x", null, NodeUtil.createJSDocInfo("@type {string}"));
 Node variableNode2 = NodeUtil.createVarNode("x", null, NodeUtil.createJSDocInfo("@type {number}"));
 Node parent = NodeUtil.createFunctionNode("parent");
 syntacticScopeCreator.declareVar("x", variableNode1, variableNode1, parent, null, parent);
 syntacticScopeCreator.declareVar("x", variableNode2, variableNode2, parent, null, parent);
 Scope scope = syntacticScopeCreator.createScope(parent, null);
 assertEquals("number", scope.getVar("x").getType().getClass().getSimpleName());
 }

 @Test
 public void testDeclarationOfVariableWithSameNameButDifferentJSDocInfo() {
 Node variableNode1 = NodeUtil.createVarNode("x");
 Node variableNode2 = NodeUtil.createVarNode("x");
 Node parent = NodeUtil.createFunctionNode("parent");
 JSDocInfo docInfo1 = NodeUtil.createJSDocInfo("@param {string} x");
 JSDocInfo docInfo2 = NodeUtil.createJSDocInfo("@param {number} x");
 syntacticScopeCreator.declareVar("x", variableNode1, variableNode1, parent, docInfo1, parent);
 syntacticScopeCreator.declareVar("x", variableNode2, variableNode2, parent, docInfo2, parent);
 Scope scope = syntacticScopeCreator.createScope(parent, null);
 assertEquals("number", scope.getVar("x").getType().getClass().getSimpleName());
 }

 @Test
 public void testDeclarationOfVariableWithSameNameButDifferentJSDocInfoLocation() {
 Node variableNode1 = NodeUtil.createVarNode("x");
 Node variableNode2 = NodeUtil.createVarNode("x");
 Node parent = NodeUtil.createFunctionNode("parent");
 JSDocInfo docInfo1 = NodeUtil.createJSDocInfo("@param {string} x");
 JSDocInfo docInfo2 = NodeUtil.createJSDocInfo("@type {number}");
 Node rootNode1 = NodeUtil.createFunctionNode("rootNode1");
 Node rootNode2 = NodeUtil.createFunctionNode("rootNode2");
 syntacticScopeCreator.declareVar("x", variableNode1, variableNode1, parent, docInfo1, rootNode1);
 syntacticScopeCreator.declareVar("x", variableNode2, variableNode2, parent, docInfo2, rootNode2);
 Scope scope = syntacticScopeCreator.createScope(rootNode1, null);
 assertEquals("number", scope.getVar("x").getType().getClass().getSimpleName());
 }

 @Test
 public void testDeclarationOfFunctionWithSameNameButDifferentLocation() {
 Node functionNode1 = NodeUtil.createFunctionNode("myFunction");
 Node functionNode2 = NodeUtil.createFunctionNode("myFunction");
 Node parent = NodeUtil.createFunctionNode("parent");
 syntacticScopeCreator.declareVar("myFunction", functionNode1.getFirstChild(), functionNode1, parent, null, parent);
 syntacticScopeCreator.declareVar("myFunction", functionNode2.getFirstChild(), functionNode2, parent, null, parent);
 Scope scope = syntacticScopeCreator.createScope(parent, null);
 assertNotNull(scope.getVar("myFunction"));
 }

 @Test
 public void testDeclarationOfVariableWithSameNameButDifferentLocationAndType() {
 Node variableNode1 = NodeUtil.createVarNode("x", null, NodeUtil.createJSDocInfo("@type {string}"));
 Node variableNode2 = NodeUtil.createVarNode("x", null, NodeUtil.createJSDocInfo("@type {number}"));
 Node parent = NodeUtil.createFunctionNode("parent");
 Node rootNode1 = NodeUtil.createFunctionNode("rootNode1");
 Node rootNode2 = NodeUtil.createFunctionNode("rootNode2");
 syntacticScopeCreator.declareVar("x", variableNode1, variableNode1, parent, null, rootNode1);
 syntacticScopeCreator.declareVar("x", variableNode2, variableNode2, parent, null, rootNode2);
 Scope scope = syntacticScopeCreator.createScope(rootNode1, null);
 assertEquals("number", scope.getVar("x").getType().getClass().getSimpleName());
 }

 @Test
 public void testDeclarationOfVariableWithSameNameButDifferentLocationAndJSDocInfo() {
 Node variableNode1 = NodeUtil.createVarNode("x");
 Node variableNode2 = NodeUtil.createVarNode("x");
 Node parent = NodeUtil.createFunctionNode("parent");
 JSDocInfo docInfo1 = NodeUtil.createJSDocInfo("@param {string} x");
 JSDocInfo docInfo2 = NodeUtil.createJSDocInfo("@type {number}");
 Node rootNode1 = NodeUtil.createFunctionNode("rootNode1");
 Node rootNode2 = NodeUtil.createFunctionNode("rootNode2");
 syntacticScopeCreator.declareVar("x", variableNode1, variableNode1, parent, docInfo1, rootNode1);
 syntacticScopeCreator.declareVar("x", variableNode2, variableNode2, parent, docInfo2, rootNode2);
 Scope scope = syntacticScopeCreator.createScope(rootNode1, null);
 assertEquals("number", scope.getVar("x").getType().getClass().getSimpleName());
 }

 @Test
 public void testDeclarationOfFunctionWithSameNameButDifferentLocationAndJSDocInfo() {
 Node functionNode1 = NodeUtil.createFunctionNode("myFunction", null, NodeUtil.createJSDocInfo("@param {number} x"));
 Node functionNode2 = NodeUtil.createFunctionNode("myFunction", null, NodeUtil.createJSDocInfo("@param {string} x"));
 Node parent = NodeUtil.createFunctionNode("parent");
 syntacticScopeCreator.declareVar("myFunction", functionNode1.getFirstChild(), functionNode1, parent, null, parent);
 syntacticScopeCreator.declareVar("myFunction", functionNode2.getFirstChild(), functionNode2, parent, null, parent);
 Scope scope = syntacticScopeCreator.createScope(parent, null);
 assertNotNull(scope.getVar("myFunction"));
 }
}

class NoOpCompiler implements AbstractCompiler {}