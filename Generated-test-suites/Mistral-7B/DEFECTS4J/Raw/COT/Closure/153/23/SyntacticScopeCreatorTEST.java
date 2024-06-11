import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

 private AbstractCompiler compiler;
 private ScopeCreator syntacticScopeCreator;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 syntacticScopeCreator = new SyntacticScopeCreator(compiler);
 }

 @Test
 public void testCreateScope() {
 Node rootNode = createTestNode("function test() {}");
 Scope expectedScope = new Scope(null, rootNode);
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testCreateScopeWithParent() {
 Node rootNode = createTestNode("function test() { var x = 1; }");
 Node childNode = createTestNode("function innerTest() { var y = 2; }");
 Scope parentScope = new Scope(rootNode, null);
 Scope expectedScope = new Scope(parentScope, childNode);
 Scope actualScope = syntacticScopeCreator.createScope(childNode, parentScope);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testDeclareVariableWithoutName() {
 Node n = createTestNode("var x;");
 try {
 syntacticScopeCreator.declareVar("", n, null, null, null, n);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithNullParent() {
 Node n = createTestNode("var x;");
 syntacticScopeCreator.declareVar("x", n, null, null, null, n);
 }

 @Test
 public void testDeclareVariableLocal() {
 Node n = createTestNode("function test() { var x = 1; }");
 syntacticScopeCreator.declareVar("x", n.getFirstChild(), n, null, null, n);
 Var xVar = scope.getVar("x");
 assertEquals("x", xVar.getName());
 assertEquals(n.getLocation(), xVar.getLocation());
 assertEquals(JSType.ANY, xVar.getType());
 }

 @Test
 public void testDeclareVariableGlobal() {
 Node n = createTestNode("var x = 1;");
 syntacticScopeCreator.declareVar("x", n, null, null, null, n);
 Var xVar = scope.getVar("x");
 assertEquals("x", xVar.getName());
 assertEquals(NodeUtil.getGlobalScope(), xVar.getParentNode());
 assertEquals(JSType.ANY, xVar.getType());
 }

 @Test
 public void testDeclareVariableGlobalWithDuplicate() {
 Node n = createTestNode("var x = 1; var x = 2;");
 try {
 syntacticScopeCreator.declareVar("x", n, null, null, null, n);
 fail("Expected a JSError");
 } catch (JSError e) {
 assertEquals(VAR_MULTIPLY_DECLARED_ERROR, e.getType());
 assertEquals("x", e.getMessage(0));
 assertEquals("test", e.getMessage(1));
 }
 }

 @Test
 public void testDeclareVariableFunction() {
 Node functionNode = createTestNode("function test() {}");
 Node variableNode = functionNode.getFirstChild();
 syntacticScopeCreator.declareVar("test", variableNode, functionNode, null, null, functionNode);
 Var testVar = scope.getVar("test");
 assertEquals("test", testVar.getName());
 assertEquals(functionNode, testVar.getLocation());
 assertEquals(JSType.FUNCTION, testVar.getType());
 }

 @Test
 public void testDeclareVariableFunctionWithDuplicate() {
 Node functionNode = createTestNode("function test() {} function test() {}");
 try {
 syntacticScopeCreator.declareVar("test", functionNode.getFirstChild(), functionNode, null, null, functionNode);
 fail("Expected a JSError");
 } catch (JSError e) {
 assertEquals(VAR_MULTIPLY_DECLARED_ERROR, e.getType());
 assertEquals("test", e.getMessage(0));
 assertEquals("test", e.getMessage(1));
 }
 }

 @Test
 public void testDeclareVariableCatch() {
 Node functionNode = createTestNode("function test() { try { catch (x) {} } }");
 Node catchNode = functionNode.getLastChild();
 syntacticScopeCreator.declareVar("x", catchNode, null, functionNode, null, functionNode);
 Var xVar = scope.getVar("x");
 assertEquals("x", xVar.getName());
 assertEquals(catchNode, xVar.getLocation());
 assertEquals(JSType.ANY, xVar.getType());
 }

 @Test
 public void testDeclareVariableArgument() {
 Node functionNode = createTestNode("function test(x) {}");
 Node argumentsNode = functionNode.getFirstChild();
 try {
 syntacticScopeCreator.declareVar("arguments", argumentsNode, null, null, null, functionNode);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableArgumentWithDuplicate() {
 Node functionNode = createTestNode("function test(x) { arguments = 1; }");
 try {
 syntacticScopeCreator.declareVar("arguments", functionNode.getFirstChild(), null, null, null, functionNode);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testDeclareVariableArgumentInFunctionExpression() {
 Node functionExpressionNode = createTestNode("(function(x) {})(1)");
 Node argumentsNode = functionExpressionNode.getFirstChild().getFirstChild();
 try {
 syntacticScopeCreator.declareVar("arguments", argumentsNode, null, null, null, functionExpressionNode);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableArgumentInFunctionExpressionWithDuplicate() {
 Node functionExpressionNode = createTestNode("(function(x) { arguments = 1; })()");
 try {
 syntacticScopeCreator.declareVar("arguments", functionExpressionNode.getFirstChild().getFirstChild(), null, null, null, functionExpressionNode);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testHandleVariableRedeclaration() {
 Node nodeWithRedeclaration = createTestNode("function test() { var x = 1; var x = 2; }");
 ScopeCreator spyScopeCreator = spy(new SyntacticScopeCreator(compiler));
 doReturn(new DefaultRedeclarationHandler()).when(spyScopeCreator).new DefaultRedeclarationHandler();
 Scope scope = spyScopeCreator.createScope(nodeWithRedeclaration, null);

 assertEquals(1, scope.size());
 Var firstXVar = scope.getVar(0);
 assertEquals("x", firstXVar.getName());
 assertEquals(nodeWithRedeclaration.getFirstChild(), firstXVar.getLocation());
 assertEquals(JSType.ANY, firstXVar.getType());

 verify(spyScopeCreator).declareVar("x", nodeWithRedeclaration.getFirstChild(), nodeWithRedeclaration, null, null, nodeWithRedeclaration);
 verify(spyScopeCreator).declareVar("x", nodeWithRedeclaration.getLastChild(), nodeWithRedeclaration, null, null, nodeWithRedeclaration);
 verify(spyScopeCreator).redeclarationHandler().onRedeclaration(eq(scope), eq("x"), eq(nodeWithRedeclaration.getLastChild()), eq(nodeWithRedeclaration), any(), any());
 }

 @Test
 public void testHandleVariableRedeclarationGlobal() {
 Node nodeWithRedeclaration = createTestNode("var x = 1; var x = 2;");
 ScopeCreator spyScopeCreator = spy(new SyntacticScopeCreator(compiler));
 doReturn(new DefaultRedeclarationHandler()).when(spyScopeCreator).new DefaultRedeclarationHandler();
 Scope scope = spyScopeCreator.createScope(nodeWithRedeclaration, null);

 assertEquals(1, scope.size());
 Var firstXVar = scope.getVar(0);
 assertEquals("x", firstXVar.getName());
 assertEquals(nodeWithRedeclaration.getFirstChild(), firstXVar.getLocation());
 assertEquals(JSType.ANY, firstXVar.getType());

 verify(spyScopeCreator).declareVar("x", nodeWithRedeclaration.getFirstChild(), nodeWithRedeclaration, null, null, nodeWithRedeclaration);
 verify(spyScopeCreator).declareVar("x", nodeWithRedeclaration.getLastChild(), nodeWithRedeclaration, null, null, nodeWithRedeclaration);
 verify(spyScopeCreator).redeclarationHandler().onRedeclaration(eq(scope), eq("x"), eq(nodeWithRedeclaration.getLastChild()), eq(nodeWithRedeclaration), any(), any());

 assertEquals(DiagnosticType.error, spyScopeCreator.redeclarationHandler().getLastError().getType());
 assertEquals("JSC_VAR_MULTIPLE_DECLARED_ERROR", spyScopeCreator.redeclarationHandler().getLastError().getMessage());
 }

 @Test
 public void testHandleVariableRedeclarationFunction() {
 Node nodeWithRedeclaration = createTestNode("function test() { function test() {} }");
 ScopeCreator spyScopeCreator = spy(new SyntacticScopeCreator(compiler));
 doReturn(new DefaultRedeclarationHandler()).when(spyScopeCreator).new DefaultRedeclarationHandler();
 Scope scope = spyScopeCreator.createScope(nodeWithRedeclaration, null);

 assertEquals(1, scope.size());
 Var firstTestVar = scope.getVar(0);
 assertEquals("test", firstTestVar.getName());
 assertEquals(nodeWithRedeclaration.getFirstChild().getFirstChild(), firstTestVar.getLocation());
 assertEquals(JSType.FUNCTION, firstTestVar.getType());

 verify(spyScopeCreator).declareVar("test", nodeWithRedeclaration.getFirstChild().getFirstChild(), nodeWithRedeclaration.getFirstChild(), null, null, nodeWithRedeclaration.getFirstChild());
 verify(spyScopeCreator).declareVar("test", nodeWithRedeclaration.getLastChild(), nodeWithRedeclaration.getLastChild(), null, null, nodeWithRedeclaration.getLastChild());
 verify(spyScopeCreator).redeclarationHandler().onRedeclaration(eq(scope), eq("test"), eq(nodeWithRedeclaration.getLastChild()), eq(nodeWithRedeclaration.getLastChild()), any(), any());

 assertEquals(DiagnosticType.error, spyScopeCreator.redeclarationHandler().getLastError().getType());
 assertEquals("JSC_VAR_MULTIPLE_DECLARED_ERROR", spyScopeCreator.redeclarationHandler().getLastError().getMessage());
 }

 @Test
 public void testHandleVariableRedeclarationCatch() {
 Node nodeWithRedeclaration = createTestNode("function test() { try { catch (x) {} catch (x) {} } }");
 ScopeCreator spyScopeCreator = spy(new SyntacticScopeCreator(compiler));
 doReturn(new DefaultRedeclarationHandler()).when(spyScopeCreator).new DefaultRedeclarationHandler();
 Scope scope = spyScopeCreator.createScope(nodeWithRedeclaration, null);

 assertEquals(1, scope.size());
 Var firstXVar = scope.getVar(0);
 assertEquals("x", firstXVar.getName());
 assertEquals(nodeWithRedeclaration.getLastChild().getLastChild(), firstXVar.getLocation());
 assertEquals(JSType.ANY, firstXVar.getType());

 verify(spyScopeCreator).declareVar("x", nodeWithRedeclaration.getLastChild().getLastChild(), nodeWithRedeclaration.getLastChild().getLastChild(), null, null, nodeWithRedeclaration.getLastChild().getLastChild());
 verify(spyScopeCreator).declareVar("x", nodeWithRedeclaration.getLastChild().getLastChild().getPreviousSibling(), nodeWithRedeclaration.getLastChild().getLastChild(), null, null, nodeWithRedeclaration.getLastChild().getLastChild());
 verify(spyScopeCreator).redeclarationHandler().onRedeclaration(eq(scope), eq("x"), eq(nodeWithRedeclaration.getLastChild().getLastChild()), eq(nodeWithRedeclaration.getLastChild().getLastChild()), any(), any());

 verify(spyScopeCreator).redeclarationHandler().onRedeclaration(eq(scope), eq("x"), eq(nodeWithRedeclaration.getLastChild().getLastChild().getPreviousSibling()), eq(nodeWithRedeclaration.getLastChild().getLastChild()), any(), any());
 }

 @Test
 public void testHandleVariableRedeclarationArgument() {
 Node nodeWithRedeclaration = createTestNode("function test(x) { arguments = x; }");
 ScopeCreator spyScopeCreator = spy(new SyntacticScopeCreator(compiler));
 doReturn(new DefaultRedeclarationHandler()).when(spyScopeCreator).new DefaultRedeclarationHandler();
 Scope scope = spyScopeCreator.createScope(nodeWithRedeclaration, null);

 assertEquals(1, scope.size());
 Var argumentsVar = scope.getVar(0);
 assertEquals("arguments", argumentsVar.getName());
 assertEquals(nodeWithRedeclaration.getFirstChild(), argumentsVar.getLocation());
 assertEquals(JSType.ARRAY, argumentsVar.getType());

 verify(spyScopeCreator).declareVar("arguments", nodeWithRedeclaration.getFirstChild(), nodeWithRedeclaration, null, null, nodeWithRedeclaration);
 verify(spyScopeCreator).redeclarationHandler().onRedeclaration(eq(scope), eq("arguments"), eq(nodeWithRedeclaration.getFirstChild()), eq(nodeWithRedeclaration), any(), any());

 assertEquals(DiagnosticType.error, spyScopeCreator.redeclarationHandler().getLastError().getType());
 assertEquals("JSC_VAR_ARGUMENTS_SHADOWED_ERROR", spyScopeCreator.redeclarationHandler().getLastError().getMessage());
 }

 private Node createTestNode(String nodeContent) {
 NodeFactory nodeFactory = new NodeFactory();
 return nodeFactory.createFromString(nodeContent, null);
 }

}