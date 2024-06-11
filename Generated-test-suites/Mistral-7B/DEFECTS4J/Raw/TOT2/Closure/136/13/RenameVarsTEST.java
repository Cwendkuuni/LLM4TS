import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeTraversal;

public class RenameVarsTest {

 @Test
 public void testRenameVars() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();

 // Test case for a simple variable renaming
 String testCode = "function testRename() {" +
 "  var a = 1;" +
 "  var b = 2;" +
 "  var c = 3;" +
 "}";

 CompilerInput input = new CompilerInput(testCode, "TestFile.js", null);
 RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
 renameVars.process(input.getExterns(), input.getRootNode());

 NodeTraversal traversal = new NodeTraversal(compiler, input, options, null);
 Node testFunction = traversal.getTopLevelNodes().get(0);

 // Check if variables 'a', 'b' and 'c' have been renamed
 assertEquals("a should be renamed", "a1", testFunction.getFirstChild().getFirstChild().getString());
 assertEquals("b should be renamed", "b1", testFunction.getFirstChild().getLastChild().getFirstChild().getString());
 assertEquals("c should be renamed", "c1", testFunction.getLastChild().getFirstChild().getString());
 }

 @Test
 public void testRenameExternVars() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();

 String testCode = "var a = 1;";

 CompilerInput input = new CompilerInput(testCode, "TestFile.js", null);
 RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
 renameVars.process(input.getExterns(), input.getRootNode());

 // Check if variable 'a' has not been renamed
 NodeTraversal traversal = new NodeTraversal(compiler, input, options, null);
 Node externs = traversal.getTopLevelNodes().get(0);

 Node aNode = null;
 for (Node child : externs.getChildren()) {
 if (child.getString().equals("a")) {
 aNode = child;
 break;
 }
 }
 assertNotNull("a should not be renamed", aNode);
 assertEquals("a should not be renamed", "a", aNode.getString());
 }

 @Test
 public void testRenameVarsWithLocalRenamingOnly() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();

 String testCode = "function testRename() {" +
 "  var a = 1;" +
 "  var b = 2;" +
 "  var c = function() {}" +
 "}";

 CompilerInput input = new CompilerInput(testCode, "TestFile.js", null);
 RenameVars renameVars = new RenameVars(compiler, "", true, false, false, null, null, null);
 renameVars.process(input.getExterns(), input.getRootNode());

 NodeTraversal traversal = new NodeTraversal(compiler, input, options, null);
 Node testFunction = traversal.getTopLevelNodes().get(0);

 // Check if local variables 'a' and 'b' have been renamed
 assertEquals("a should be renamed", "a0", testFunction.getFirstChild().getFirstChild().getString());
 assertEquals("b should be renamed", "b0", testFunction.getFirstChild().getLastChild().getFirstChild().getString());

 // Check if anonymous function has not been renamed
 assertEquals("anonymous function should not be renamed", "function() {}", testFunction.getLastChild().getFirstChild().getString());
 }

 @Test
 public void testRenameVarsWithPreserveAnonymousFunctionNames() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();

 String testCode = "function testRename() {" +
 "  var a = 1;" +
 "  var b = 2;" +
 "  var c = function anonymous() {}" +
 "}";

 CompilerInput input = new CompilerInput(testCode, "TestFile.js", null);
 RenameVars renameVars = new RenameVars(compiler, "", false, true, false, null, null, null);
 renameVars.process(input.getExterns(), input.getRootNode());

 NodeTraversal traversal = new NodeTraversal(compiler, input, options, null);
 Node testFunction = traversal.getTopLevelNodes().get(0);

 // Check if anonymous function has been preserved
 assertEquals("anonymous function should be preserved", "anonymous", testFunction.getLastChild().getFirstChild().getString());
 }

 @Test
 public void testRenameVarsWithGeneratePseudoNames() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();

 String testCode = "function testRename() {" +
 "  var a = 1;" +
 "  var b = 2;" +
 "}";

 CompilerInput input = new CompilerInput(testCode, "TestFile.js", null);
 RenameVars renameVars = new RenameVars(compiler, "", false, false, true, null, null, null);
 renameVars.process(input.getExterns(), input.getRootNode());

 NodeTraversal traversal = new NodeTraversal(compiler, input, options, null);
 Node testFunction = traversal.getTopLevelNodes().get(0);

 // Check if variables 'a' and 'b' have been assigned pseudo names
 assertEquals("a should be assigned a pseudo name", "$a$$", testFunction.getFirstChild().getFirstChild().getString());
 assertEquals("b should be assigned a pseudo name", "$b$$", testFunction.getFirstChild().getLastChild().getFirstChild().getString());
 }

 @Test(expected = AssertionError.class)
 public void testRenameVarsWithInvalidCharacters() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();

 String testCode = "function testRename() {" +
 "  var a = 1;" +
 "  var ¢b = 2;" +
 "}";

 CompilerInput input = new CompilerInput(testCode, "TestFile.js", null);
 RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, new char[] { '¢' }, null);
 try {
 renameVars.process(input.getExterns(), input.getRootNode());
 fail("Should have thrown an error");
 } catch (AssertionError e) {
 // Expected
 }
 }
}