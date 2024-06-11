import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.JSError.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;

public class SyntacticScopeCreatorTest {

 private AbstractCompiler compiler;
 private ScopeCreator sc;

 @Before
 public void setUp() {
 compiler = new Compiler();
 sc = new SyntacticScopeCreator(compiler);
 }

 @Test
 public void testCreateScope_noDeclarations() {
 Scope parent = new Scope(null, null);
 Scope scope = sc.createScope(new Node(), parent);
 assertEquals(0, scope.size());
 }

 @Test
 public void testCreateScope_singleDeclaration() {
 String sourceName = "test.js";
 Node root = new Node(Token.SCRIPT, sourceName);
 Node varNode = new Node(Token.VAR, null);
 varNode.setString("x");
 Node valueNode = new Node(Token.NUMBER, null, "1");
 varNode.addChild(valueNode);
 Node declNode = new Node(Token.VAR, null);
 declNode.addChild(varNode);
 Node body = new Node(Token.BLOCK, null);
 root.addChild(declNode);
 root.addChild(body);

 Scope parent = new Scope(null, root);
 Scope scope = sc.createScope(root, parent);
 Var x = scope.getVar("x");
 assertEquals(1, scope.size());
 assertEquals(JSType.NUMBER, x.getType());
 }

 @Test
 public void testCreateScope_multipleDeclarations() {
 String sourceName = "test.js";
 Node root = new Node(Token.SCRIPT, sourceName);
 Node varNode1 = new Node(Token.VAR, null);
 varNode1.setString("x");
 Node valueNode1 = new Node(Token.NUMBER, null, "1");
 varNode1.addChild(valueNode1);
 Node declNode1 = new Node(Token.VAR, null);
 declNode1.addChild(varNode1);
 Node varNode2 = new Node(Token.VAR, null);
 varNode2.setString("y");
 Node valueNode2 = new Node(Token.NUMBER, null, "2");
 varNode2.addChild(valueNode2);
 Node declNode2 = new Node(Token.VAR, null);
 declNode2.addChild(varNode2);
 Node body = new Node(Token.BLOCK, null);
 root.addChild(declNode1);
 root.addChild(declNode2);
 root.addChild(body);

 Scope parent = new Scope(null, root);
 Scope scope = sc.createScope(root, parent);
 Var x = scope.getVar("x");
 Var y = scope.getVar("y");
 assertEquals(2, scope.size());
 assertEquals(JSType.NUMBER, x.getType());
 assertEquals(JSType.NUMBER, y.getType());
 }

 @Test
 public void testCreateScope_functionDeclaration() {
 String sourceName = "test.js";
 Node root = new Node(Token.SCRIPT, sourceName);
 Node fnNode = new Node(Token.FUNCTION, null);
 Node nameNode = fnNode.addChild(new Node(Token.NAME, null, "myFunction"));
 Node body = new Node(Token.BLOCK, null);
 fnNode.addChild(body);
 Node declNode = new Node(Token.VAR, null);
 declNode.addChild(nameNode);
 root.addChild(declNode);
 root.addChild(fnNode);

 Scope parent = new Scope(null, root);
 Scope scope = sc.createScope(root, parent);
 Var myFunction = scope.getVar("myFunction");
 assertEquals(1, scope.size());
 assertEquals(JSType.FUNCTION, myFunction.getType());
 }

 @Test
 public void testCreateScope_variableRedeclaration() {
 String sourceName = "test.js";
 Node root = new Node(Token.SCRIPT, sourceName);
 Node varNode1 = new Node(Token.VAR, null);
 varNode1.setString("x");
 Node valueNode1 = new Node(Token.NUMBER, null, "1");
 varNode1.addChild(valueNode1);
 Node declNode1 = new Node(Token.VAR, null);
 declNode1.addChild(varNode1);
 Node varNode2 = new Node(Token.VAR, null);
 varNode2.setString("x");
 Node valueNode2 = new Node(Token.NUMBER, null, "2");
 varNode2.addChild(valueNode2);
 Node declNode2 = new Node(Token.VAR, null);
 declNode2.addChild(varNode2);
 Node body = new Node(Token.BLOCK, null);
 root.addChild(declNode1);
 root.addChild(declNode2);
 root.addChild(body);

 Scope parent = new Scope(null, root);
 SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler, new RedeclarationHandler() {
 @Override public void onRedeclaration(
     Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
 // Do nothing
 }
 });
 Scope scope = sc.createScope(root, parent);
 Var x1 = scope.getVar("x");
 Var x2 = scope.getVar("x");
 assertEquals(1, scope.size());
 assertEquals(JSType.NUMBER, x1.getType());
 assertEquals(JSType.NUMBER, x2.getType());
 assertSame(x1, x2);
 }

 @Test
 public void testCreateScope_functionVariableRedeclaration() {
 String sourceName = "test.js";
 Node root = new Node(Token.SCRIPT, sourceName);
 Node fnNode = new Node(Token.FUNCTION, null);
 Node nameNode = fnNode.addChild(new Node(Token.NAME, null, "myFunction"));
 Node body = new Node(Token.BLOCK, null);
 fnNode.addChild(body);
 Node declNode1 = new Node(Token.VAR, null);
 declNode1.addChild(nameNode);
 root.addChild(declNode1);
 Node declNode2 = new Node(Token.VAR, null);
 declNode2.addChild(nameNode);
 root.addChild(declNode2);

 Scope parent = new Scope(null, root);
 SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler, new RedeclarationHandler() {
 @Override public void onRedeclaration(
     Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
 // Do nothing
 }
 });
 Scope scope = sc.createScope(root, parent);
 Var myFunction = scope.getVar("myFunction");
 assertEquals(1, scope.size());
 assertEquals(JSType.FUNCTION, myFunction.getType());
 }

 @Test
 public void testCreateScope_globalVariableRedeclaration() {
 String sourceName = "test.js";
 Node root = new Node(Token.SCRIPT, sourceName);
 Node varNode1 = new Node(Token.VAR, null);
 varNode1.setString("x");
 Node valueNode1 = new Node(Token.NUMBER, null, "1");
 varNode1.addChild(valueNode1);
 Node declNode1 = new Node(Token.VAR, null);
 declNode1.addChild(varNode1);
 root.addChild(declNode1);
 Node varNode2 = new Node(Token.VAR, null);
 varNode2.setString("x");
 Node valueNode2 = new Node(Token.NUMBER, null, "2");
 varNode2.addChild(valueNode2);
 Node declNode2 = new Node(Token.VAR, null);
 declNode2.addChild(varNode2);
 root.addChild(declNode2);

 Scope parent = new Scope(null, root);
 SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
 try {
 Scope scope = sc.createScope(root, parent);
 fail("Expected duplicate declaration error");
 } catch (IllegalStateException e) {
 String expectedMessage = "Duplicate declaration: " + "x";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.startsWith(expectedMessage));
 }
 }

 @Test
 public void testCreateScope_functionWithArguments() {
 String sourceName = "test.js";
 Node root = new Node(Token.SCRIPT, sourceName);
 Node fnNode = new Node(Token.FUNCTION, null);
 Node nameNode = fnNode.addChild(new Node(Token.NAME, null, "myFunction"));
 Node argsNode = fnNode.addChild(new Node(Token.LP, null));
 Node argNode1 = argsNode.addChild(new Node(Token.NAME, null, "a"));
 Node argNode2 = argsNode.addChild(new Node(Token.NAME, null, "b"));
 argsNode.addChild(new Node(Token.RP, null));
 Node body = new Node(Token.BLOCK, null);
 fnNode.addChild(body);
 Node declNode = new Node(Token.VAR, null);
 declNode.addChild(nameNode);
 root.addChild(declNode);
 root.addChild(fnNode);

 Scope parent = new Scope(null, root);
 Scope scope = sc.createScope(root, parent);
 Var myFunction = scope.getVar("myFunction");
 Var a = scope.getVar("a");
 Var b = scope.getVar("b");
 assertEquals(3, scope.size());
 assertEquals(JSType.FUNCTION, myFunction.getType());
 assertEquals(JSType.UNKNOWN, a.getType());
 assertEquals(JSType.UNKNOWN, b.getType());
 }

 @Test
 public void testCreateScope_functionWithGlobalArguments() {
 String sourceName = "test.js";
 Node root = new Node(Token.SCRIPT, sourceName);
 Node fnNode = new Node(Token.FUNCTION, null);
 Node nameNode = fnNode.addChild(new Node(Token.NAME, null, "myFunction"));
 Node argsNode = fnNode.addChild(new Node(Token.LP, null));
 Node argNode1 = argsNode.addChild(new Node(Token.NAME, null, "a"));
 Node argNode2 = argsNode.addChild(new Node(Token.NAME, null, "b"));
 argsNode.addChild(new Node(Token.RP, null));
 Node body = new Node(Token.BLOCK, null);
 fnNode.addChild(body);
 Node declNode = new Node(Token.VAR, null);
 declNode.addChild(nameNode);
 root.addChild(declNode);
 Node globalVarNode1 = new Node(Token.VAR, null);
 globalVarNode1.setString("a");
 Node globalVarValueNode1 = new Node(Token.NUMBER, null, "1");
 globalVarNode1.addChild(globalVarValueNode1);
 root.addChild(globalVarNode1);
 Node globalVarNode2 = new Node(Token.VAR, null);
 globalVarNode2.setString("b");
 Node globalVarValueNode2 = new Node(Token.NUMBER, null, "2");
 globalVarNode2.addChild(globalVarValueNode2);
 root.addChild(globalVarNode2);

 Scope parent = new Scope(null, root);
 Scope scope = sc.createScope(root, parent);
 Var myFunction = scope.getVar("myFunction");
 Var a = scope.getVar("a");
 Var b = scope.getVar("b");
 assertEquals(3, scope.size());
 assertEquals(JSType.FUNCTION, myFunction.getType());
 assertEquals(JSType.NUMBER, a.getType());
 assertEquals(JSType.NUMBER, b.getType());
 assertEquals(a, globalVarNode1.getInput().getVar("a"));
 assertEquals(b, globalVarNode2.getInput().getVar("b"));
 }

 @Test
 public void testCreateScope_functionWithGlobalArgumentShadowing() {
 String sourceName = "test.js";
 Node root = new Node(Token.SCRIPT, sourceName);
 Node fnNode = new Node(Token.FUNCTION, null);
 Node nameNode = fnNode.addChild(new Node(Token.NAME, null, "myFunction"));
 Node argsNode = fnNode.addChild(new Node(Token.LP, null));
 Node argNode = argsNode.addChild(new Node(Token.NAME, null, "arguments"));
 argsNode.addChild(new Node(Token.RP, null));
 Node body = new Node(Token.BLOCK, null);
 fnNode.addChild(body);
 Node declNode = new Node(Token.VAR, null);
 declNode.addChild(nameNode);
 root.addChild(declNode);
 root.addChild(fnNode);
 root.addChild(new Node(Token.VAR, null).setString("arguments").setType(JSType.UNKNOWN));

 Scope parent = new Scope(null, root);
 try {
 Scope scope = sc.createScope(root, parent);
 fail("Expected 'arguments' shadowing error");
 } catch (IllegalStateException e) {
 String expectedMessage = "Shadowing 'arguments' is not allowed";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.startsWith(expectedMessage));
 }
 }
}