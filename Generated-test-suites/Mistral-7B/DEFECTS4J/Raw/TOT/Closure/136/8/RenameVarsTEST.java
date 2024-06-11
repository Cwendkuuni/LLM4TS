import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.VariableMap;

public class RenameVarsTest {

 private AbstractCompiler compiler;
 private CompilerPass renameVars;
 private Node node;
 private CompilerInput input;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 renameVars = new RenameVars(compiler, "test_prefix", false, false, false, null, null, null);
 }

 @Test
 public void testRenameVars() {
 input = new CompilerInput("test.js", null);
 node = new Node("test"); // empty node for testing purpose

 // test case for renaming local variables
 String originalCode = "function testFunction(a, b) {" +
 "var c = a + b;" +
 "var d = 42;" +
 "var e = function() {}" +
 "return c + d;}" +
 "testFunction(1, 2);";
 compiler.parseAndCompile(originalCode, input);
 NodeTraversal traversal = compiler.getTraversal();
 renameVars.process(input.getExterns(), node);
 String expectedCode = "function testFunction(a, b) {" +
 "var f = a;" +
 "var g = b;" +
 "var h = function() {}" +
 "return f + g;}" +
 "testFunction(1, 2);";
 assertEquals(expectedCode, compiler.getSourceCode());

 // test case for renaming global variables
 originalCode = "var a = 1;" +
 "function testFunction() {" +
 "var b = 2;" +
 "return a + b;" +
 "}" +
 "testFunction();";
 compiler.parseAndCompile(originalCode, input);
 renameVars.process(input.getExterns(), node);
 expectedCode = "var c = 1;" +
 "function testFunction() {" +
 "var d = 2;" +
 "return c + d;" +
 "}" +
 "testFunction();";
 assertEquals(expectedCode, compiler.getSourceCode());

 // test case for renaming global variables with same name as local variables
 originalCode = "function testFunction() {" +
 "var a = 1;" +
 "return a;}" +
 "var a = 2;" +
 "testFunction();";
 compiler.parseAndCompile(originalCode, input);
 renameVars.process(input.getExterns(), node);
 expectedCode = "function testFunction() {" +
 "var b = 1;" +
 "return b;" +
 "}" +
 "var a = 2;" +
 "testFunction();";
 assertEquals(expectedCode, compiler.getSourceCode());

 // test case for renaming variables that are assigned the same value multiple times
 originalCode = "function testFunction() {" +
 "var a = 1;" +
 "a = a;" +
 "return a;" +
 "}" +
 "testFunction();";
 compiler.parseAndCompile(originalCode, input);
 renameVars.process(input.getExterns(), node);
 expectedCode = "function testFunction() {" +
 "var b = 1;" +
 "b = b;" +
 "return b;" +
 "}" +
 "testFunction();";
 assertEquals(expectedCode, compiler.getSourceCode());

 // test case for renaming variables with reserved keywords as names
 originalCode = "function testFunction() {" +
 "var function = 1;" +
 "return function;" +
 "}" +
 "testFunction();";
 compiler.parseAndCompile(originalCode, input);
 renameVars.process(input.getExterns(), node);
 expectedCode = "function testFunction() {" +
 "var x = 1;" +
 "return x;" +
 "}" +
 "testFunction();";
 assertEquals(expectedCode, compiler.getSourceCode());

 // test case for renaming variables with very long names
 originalCode = "function testFunction() {" +
 "var aVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongVariableName = 1;" +
 "return aVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongVariableName;" +
 "}" +
 "testFunction();";
 compiler.parseAndCompile(originalCode, input);
 renameVars.process(input.getExterns(), node);
 expectedCode = "function testFunction() {" +
 "var b = 1;" +
 "return b;" +
 "}" +
 "testFunction();";
 assertEquals(expectedCode, compiler.getSourceCode());

 // test case for preserving anonymous function names
 originalCode = "function testFunction() {" +
 "return (function() {return 1;})();" +
 "}" +
 "testFunction();";
 renameVars = new RenameVars(compiler, "test_prefix", false, true, false, null, null, null);
 compiler.parseAndCompile(originalCode, input);
 renameVars.process(input.getExterns(), node);
 expectedCode = "function testFunction() {" +
 "return (function() {return 1;})();" +
 "}" +
 "testFunction();";
 assertEquals(expectedCode, compiler.getSourceCode());
 }
}