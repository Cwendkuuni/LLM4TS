import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.ErrorReporter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

 private Scope scope;
 private ErrorReporter errorReporter;

 @Before
 public void setUp() {
 scope = new Scope(null, null);
 errorReporter = new ErrorReporter();
 }

 @Test
 public void testCreateScope() {
 Scope scope = new Scope(null, null);
 assertNull(scope.getParent());
 assertNotNull(scope.getTypeOfThis());
 assertTrue(scope.isBottom());
 assertEquals(0, scope.getDepth());
 }

 @Test
 public void testCreateScopeWithParent() {
 Node rootNode = new Node();
 Scope parentScope = new Scope(null, rootNode);
 Scope scope = new Scope(parentScope, rootNode);
 assertEquals(parentScope, scope.getParent());
 assertNotNull(scope.getTypeOfThis());
 assertFalse(scope.isBottom());
 assertEquals(parentScope.getDepth() + 1, scope.getDepth());
 }

 @Test
 public void testDeclareVariable() {
 String variableName = "testVariable";
 Node nameNode = new Node(Token.NAME, variableName);
 JSType type = JSType.UNDEFINED;
 CompilerInput input = null;
 Var variable = scope.declare(variableName, nameNode, type, input);
 assertTrue(scope.isDeclared(variableName, false));
 assertEquals(variable, scope.getVar(variableName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithNullNameNode() {
 String variableName = "testVariable";
 JSType type = JSType.UNDEFINED;
 CompilerInput input = null;
 scope.declare(variableName, null, type, input);
 fail("An IllegalArgumentException should have been thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithNullType() {
 String variableName = "testVariable";
 Node nameNode = new Node(Token.NAME, variableName);
 CompilerInput input = null;
 scope.declare(variableName, nameNode, null, input);
 fail("An IllegalArgumentException should have been thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithNullInput() {
 String variableName = "testVariable";
 Node nameNode = new Node(Token.NAME, variableName);
 JSType type = JSType.UNDEFINED;
 scope.declare(variableName, nameNode, type, null);
 fail("An IllegalArgumentException should have been thrown");
 }

 @Test
 public void testDeclareVariableWithDuplicateName() {
 String variableName = "testVariable";
 Node nameNode = new Node(Token.NAME, variableName);
 JSType type = JSType.UNDEFINED;
 CompilerInput input = null;
 Var firstVariable = scope.declare(variableName, nameNode, type, input);
 Var secondVariable = scope.declare(variableName, nameNode, type, input);
 assertEquals(firstVariable, secondVariable);
 }

 @Test
 public void testUndeclareVariable() {
 String variableName = "testVariable";
 Node nameNode = new Node(Token.NAME, variableName);
 JSType type = JSType.UNDEFINED;
 CompilerInput input = null;
 Var variable = scope.declare(variableName, nameNode, type, input);
 scope.undeclare(variable);
 assertFalse(scope.isDeclared(variableName, false));
 }

 @Test
 public void testGetVar() {
 String variableName = "testVariable";
 Node nameNode = new Node(Token.NAME, variableName);
 JSType type = JSType.UNDEFINED;
 CompilerInput input = null;
 Var variable = scope.declare(variableName, nameNode, type, input);
 assertEquals(variable, scope.getVar(variableName));
 }

 @Test
 public void testGetVarNull() {
 assertNull(scope.getVar("nonExistentVariable"));
 }

 @Test
 public void testGetSlot() {
 String variableName = "testVariable";
 Node nameNode = new Node(Token.NAME, variableName);
 JSType type = JSType.UNDEFINED;
 CompilerInput input = null;
 Var variable = scope.declare(variableName, nameNode, type, input);
 assertEquals(variable, scope.getSlot(variableName));
 }

 @Test
 public void testGetSlotNull() {
 assertNull(scope.getSlot("nonExistentVariable"));
 }

 @Test
 public void testIsDeclared() {
 String variableName1 = "testVariable1";
 String variableName2 = "testVariable2";
 Node nameNode1 = new Node(Token.NAME, variableName1);
 Node nameNode2 = new Node(Token.NAME, variableName2);
 JSType type = JSType.UNDEFINED;
 CompilerInput input = null;
 scope.declare(variableName1, nameNode1, type, input);
 assertFalse(scope.isDeclared(variableName2, false));
 scope.declare(variableName2, nameNode2, type, input);
 assertTrue(scope.isDeclared(variableName2, false));
 }

 @Test
 public void testGetVars() {
 String variableName1 = "testVariable1";
 String variableName2 = "testVariable2";
 Node nameNode1 = new Node(Token.NAME, variableName1);
 Node nameNode2 = new Node(Token.NAME, variableName2);
 JSType type = JSType.UNDEFINED;
 CompilerInput input = null;
 scope.declare(variableName1, nameNode1, type, input);
 scope.declare(variableName2, nameNode2, type, input);
 Iterator<Var> vars = scope.getVars();
 assertEquals(2, vars.size());
 }

 @Test
 public void testGetVarCount() {
 String variableName1 = "testVariable1";
 String variableName2 = "testVariable2";
 Node nameNode1 = new Node(Token.NAME, variableName1);
 Node nameNode2 = new Node(Token.NAME, variableName2);
 JSType type = JSType.UNDEFINED;
 CompilerInput input = null;
 scope.declare(variableName1, nameNode1, type, input);
 scope.declare(variableName2, nameNode2, type, input);
 assertEquals(2, scope.getVarCount());
 }

 @Test
 public void testIsGlobal() {
 assertTrue(scope.isGlobal());
 }

 @Test
 public void testIsLocal() {
 assertFalse(scope.isLocal());
 }

 @Test
 public void testJoin() {
 Scope parentScope = new Scope(null, null);
 Scope childScope = new Scope(parentScope, null);
 parentScope.declare("parentVar", null, JSType.STRING, null);
 childScope.declare("childVar", null, JSType.NUMBER, null);
 Map<String, Var> parentMap = parentScope.getVariables();
 Map<String, Var> childMap = childScope.getVariables();
 Map<String, Var> joinedMap = childScope.join(parentMap);
 assertEquals(3, joinedMap.size());
 assertEquals(parentScope.getVar("parentVar"), joinedMap.get("parentVar"));
 assertEquals(childScope.getVar("childVar"), joinedMap.get("childVar"));
 }

 @Test
 public void testJoinWithErrorReporter() {
 Scope parentScope = new Scope(null, null);
 Scope childScope = new Scope(parentScope, null);
 parentScope.declare("parentVar", null, JSType.STRING, errorReporter);
 childScope.declare("childVar", null, JSType.NUMBER, errorReporter);
 Map<String, Var> parentMap = parentScope.getVariables();
 Map<String, Var> childMap = childScope.getVariables();
 Map<String, Var> joinedMap = childScope.join(parentMap, errorReporter);
 assertEquals(3, joinedMap.size());
 assertEquals(parentScope.getVar("parentVar"), joinedMap.get("parentVar"));
 assertEquals(childScope.getVar("childVar"), joinedMap.get("childVar"));
 }
}