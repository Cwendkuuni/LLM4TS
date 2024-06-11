import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Nodeops;

public class CollapseVariableDeclarationsTest {

 @Test
 public void testCollapseSingleDeclaration() {
 Compiler compiler = new Compiler();
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

 String sourceCode = "function foo() {" +
 "var x;";
 Node root = Nodeops.parseJsonString(sourceCode);
 pass.process(null, root);
 Node expected = Nodeops.parseJsonString("function foo() {" +
 "var x;");
 Node actual = root;
 assertEquals("Expected single declaration to be collapsed to itself", expected, actual);
 }

 @Test
 public void testCollapseMultipleDeclarations() {
 Compiler compiler = new Compiler();
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

 String sourceCode = "function foo() {" +
 "var a; " +
 "var b = 1; " +
 "var c = 2; " +
 "}";
 Node root = Nodeops.parseJsonString(sourceCode);
 pass.process(null, root);
 Node expected = Nodeops.parseJsonString("function foo() {" +
 "var a, b = 1, c = 2;" +
 "}");
 Node actual = root;
 assertEquals("Expected multiple declarations to be collapsed into a single one", expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testCollapseInvalidDeclaration() {
 Compiler compiler = new Compiler();
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

 String sourceCode = "function foo() {" +
 "var a = 1; " +
 "var a; " +
 "}";
 Node root = Nodeops.parseJsonString(sourceCode);
 pass.process(null, root);
 fail("Expected compilation error due to invalid declaration");
 }

 @Test
 public void testCollapseAssignments() {
 Compiler compiler = new Compiler();
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

 String sourceCode = "function foo() {" +
 "var a = true; " +
 "var b = true; " +
 "var c = true; " +
 "}";
 Node root = Nodeops.parseJsonString(sourceCode);
 pass.process(null, root);
 Node expected = Nodeops.parseJsonString("function foo() {" +
 "var c = b = a = true;" +
 "}");
 Node actual = root;
 assertEquals("Expected assignments to be collapsed", expected, actual);
 }

 @Test
 public void testCollapseVariableInFunction() {
 Compiler compiler = new Compiler();
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

 String sourceCode = "function foo(x) {" +
 "var x; " +
 "x = 1; " +
 "}";
 Node root = Nodeops.parseJsonString(sourceCode);
 pass.process(null, root);
 Node expected = Nodeops.parseJsonString("function foo(x) {" +
 "var x; " +
 "x = 1;" +
 "}");
 Node actual = root;
 assertEquals("Expected variable in function to be collapsed to itself", expected, actual);
 }

 @Test
 public void testCollapseVariableInFunctionWithAssignments() {
 Compiler compiler = new Compiler();
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

 String sourceCode = "function foo(x) {" +
 "var x; " +
 "x = 1; " +
 "x = 2; " +
 "}";
 Node root = Nodeops.parseJsonString(sourceCode);
 pass.process(null, root);
 Node expected = Nodeops.parseJsonString("function foo(x) {" +
 "var x; " +
 "x = 1;" +
 "x = 2;" +
 "}");
 Node actual = root;
 assertEquals("Expected variable in function with assignments to be collapsed to itself", expected, actual);
 }

 @Test
 public void testCollapseMultipleDeclarationsInFunction() {
 Compiler compiler = new Compiler();
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

 String sourceCode = "function foo(x, y) {" +
 "var x; " +
 "var y = 1; " +
 "var z = 2;" +
 "}";
 Node root = Nodeops.parseJsonString(sourceCode);
 pass.process(null, root);
 Node expected = Nodeops.parseJsonString("function foo(x, y) {" +
 "var x, y = 1, z = 2;" +
 "}");
 Node actual = root;
 assertEquals("Expected multiple declarations in function to be collapsed into a single one", expected, actual);
 }

 @Test
 public void testCollapseVariableWithSameNameInDifferentScopes() {
 Compiler compiler = new Compiler();
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

 String sourceCode = "function foo() {" +
 "function bar() {" +
 "var a; " +
 "} " +
 "var a = 1; " +
 "}";
 Node root = Nodeops.parseJsonString(sourceCode);
 pass.process(null, root);
 Node expected = Nodeops.parseJsonString("function foo() {" +
 "function bar() {" +
 "} " +
 "var a; " +
 "var a = 1;" +
 "}");
 Node actual = root;
 assertEquals("Expected variables with the same name but in different scopes to be collapsed separately", expected, actual);
 }
}