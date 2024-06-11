import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;

import java.util.List;

public class CollapseVariableDeclarationsTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 options.setPrintErrorMessages(true);
 options.setWarningLevel(Compiler.WarningLevel.VERBOSE);
 }

 @Test
 public void testCollapseSimpleVariableDeclarations() {
 Node root = toNode("var a; var b = 1; var c = 2;");
 List<Node> ast = compiler.compile(ImmutableList.of(root), options).get(0);
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, ast.get(0));

 assertFalse(ast.get(0).hasChildren());
 Node collapsedVar = ast.get(0).getFirstChild();
 assertEquals(3, collapsedVar.getNumChildren());
 Node a = collapsedVar.getFirstChild();
 Node bInit = collapsedVar.getChildren().get(1);
 Node cInit = collapsedVar.getChildren().get(2);

 assertEquals("a", a.getString());
 assertEquals("b = 1", bInit.toString());
 assertEquals("c = 2", cInit.toString());
 }

 @Test
 public void testCollapseVariableAssignments() {
 Node root = toNode("a = true; b = true; var c = true;");
 List<Node> ast = compiler.compile(ImmutableList.of(root), options).get(0);
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, ast.get(0));

 assertFalse(ast.get(0).hasChildren());
 Node collapsedVar = ast.get(0).getFirstChild();
 assertEquals(3, collapsedVar.getNumChildren());
 Node aAssign = collapsedVar.getFirstChild();
 Node bAssign = collapsedVar.getChildren().get(1);
 Node c = collapsedVar.getChildren().get(2);

 assertEquals("var c = b = a = true;", collapsedVar.toString());

 assertEquals("b =", bAssign.toString());
 assertEquals("a =", aAssign.toString());
 assertEquals("true", c.toString());
 }

 @Test
 public void testCollapseVariableDeclarationsInsideFunction() {
 Node root = toNode("function foo() {" +
     "var a; var b = 1; var c = 2;" +
     "}");
 List<Node> ast = compiler.compile(ImmutableList.of(root), options).get(0);
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, ast.get(0));

 Node functionDeclaration = ast.get(0).getFirstChild();
 Node body = functionDeclaration.getLastChild();
 assertFalse(functionDeclaration.hasChildren());
 Node collapsedVars = body.getFirstChild();
 assertEquals(3, collapsedVars.getNumChildren());
 Node a = collapsedVars.getFirstChild();
 Node bInit = collapsedVars.getChildren().get(1);
 Node cInit = collapsedVars.getChildren().get(2);

 assertEquals("var a, b = 1, c = 2;", collapsedVars.toString());

 assertEquals("a", a.getString());
 assertEquals("b = 1", bInit.toString());
 assertEquals("c = 2", cInit.toString());
 }

 @Test(expected = AssertionError.class)
 public void testCollapseVariableDeclarationsInsideIfStatement() {
 Node root = toNode("if (true) {" +
     "var a; var b = 1;" +
     "}");
 List<Node> ast = compiler.compile(ImmutableList.of(root), options).get(0);
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, ast.get(0));
 fail("Collapsing variables inside if statements is not allowed.");
 }

 @Test
 public void testCollapseDuplicateVariables() {
 Node root = toNode("var a; var a = 1;");
 List<Node> ast = compiler.compile(ImmutableList.of(root), options).get(0);
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, ast.get(0));

 assertFalse(ast.get(0).hasChildren());
 Node collapsedVar = ast.get(0).getFirstChild();
 assertEquals(2, collapsedVar.getNumChildren());
 Node varDecl = collapsedVar.getFirstChild();
 Node init = collapsedVar.getChildren().get(1);

 assertEquals("var a;", varDecl.toString());
 assertEquals("a = 1", init.toString());
 }

 @Test
 public void testCollapseVariablesWithJSDocInfo() {
 Node root = toNode("/** @type {number} */ var a; /** @type {string} */ var b;");
 List<Node> ast = compiler.compile(ImmutableList.of(root), options).get(0);
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, ast.get(0));

 assertFalse(ast.get(0).hasChildren());
 Node collapsedVar = ast.get(0).getFirstChild();
 assertEquals(3, collapsedVar.getNumChildren());
 Node a = collapsedVar.getFirstChild();
 Node bInit = collapsedVar.getChildren().get(1);

 assertEquals("var a, /** @type {number} */ b;", collapsedVar.toString());
 assertEquals("a", a.getString());
 assertEquals("", bInit.toString());
 }

 @Test
 public void testCollapseVariablesInsideFunctionWithJSDocInfo() {
 Node root = toNode("function foo() {" +
     "/** @param {number} x */" +
     "/** @return {string} */" +
     "var a; /** @type {number} */ var b = 1;" +
     "}");
 List<Node> ast = compiler.compile(ImmutableList.of(root), options).get(0);
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, ast.get(0));

 Node functionDeclaration = ast.get(0).getFirstChild();
 Node body = functionDeclaration.getLastChild();
 assertFalse(functionDeclaration.hasChildren());
 Node collapsedVars = body.getFirstChild();
 assertEquals(3, collapsedVars.getNumChildren());
 Node a = collapsedVars.getFirstChild();
 Node bInit = collapsedVars.getChildren().get(1);

 assertEquals("var a, /** @type {number} */ b;", collapsedVars.toString());
 assertEquals("a", a.getString());
 assertEquals("b = 1", bInit.toString());
 }

}