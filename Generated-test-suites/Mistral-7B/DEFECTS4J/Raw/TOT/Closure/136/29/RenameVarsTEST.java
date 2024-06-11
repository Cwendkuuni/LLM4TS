package com.google.javascript.jscomp.test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.Node;

import junit.framework.TestCase;

public class RenameVarsTest extends TestCase {

  public void testRenameVars() throws Exception {
    String testCode =
        "function foo(x, y) {" +
        "  var a = x + y;" +
        "  var b = 5;" +
        "  return a + b;" +
        "}" +
        "function bar() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  function baz(x, y) {" +
        "    return x + y;" +
        "  }" +
        "  return baz(a, a);" +
        "}" +
        "var x = 10;" +
        "var y = 20;" +
        "function main() {" +
        "  print(foo(x, y));" +
        "  print(bar());" +
        "}" +
        "main();";

    String expectedOutput =
        "function foo(x, y) {" +
        "  var a = _0;" +
        "  var b = _1;" +
        "  return a + b;" +
        "}" +
        "function bar() {" +
        "  var a = _2;" +
        "  var b = _3;" +
        "  function baz(x, y) {" +
        "    return x + y;" +
        "  }" +
        "  return baz(a, a);" +
        "}" +
        "var x = _4;" +
        "var y = _5;" +
        "function main() {" +
        "  print(foo(x, y));" +
        "  print(bar());" +
        "}" +
        "main();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameVars(compiler, "", false, false, false, null, null, null);
    CompilerResult result = compiler.compileExternal(testCode, options, pass);

    assertEquals(expectedOutput, result.toString());
  }

  public void testRenameVarsWithPrefix() throws Exception {
    String testCode =
        "function foo(x, y) {" +
        "  var a = x + y;" +
        "  var b = 5;" +
        "  return a + b;" +
        "}" +
        "function bar() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  function baz(x, y) {" +
        "    return x + y;" +
        "  }" +
        "  return baz(a, a);" +
        "}" +
        "var x = 10;" +
        "var y = 20;" +
        "function main() {" +
        "  print(foo(x, y));" +
        "  print(bar());" +
        "}" +
        "main();";

    String expectedOutput =
        "function foo(x, y) {" +
        "  var a = L_0;" +
        "  var b = _1;" +
        "  return a + b;" +
        "}" +
        "function bar() {" +
        "  var a = L_2;" +
        "  var b = L_3;" +
        "  function baz(x, y) {" +
        "    return x + y;" +
        "  }" +
        "  return baz(a, a);" +
        "}" +
        "var x = L_4;" +
        "var y = L_5;" +
        "function main() {" +
        "  print(foo(x, y));" +
        "  print(bar());" +
        "}" +
        "main();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameVars(compiler, "R_", false, false, false, null, null, null);
    CompilerResult result = compiler.compileExternal(testCode, options, pass);

    assertEquals(expectedOutput, result.toString());
  }

  public void testRenameVarsWithPreserveAnonymousFunctionNames() throws Exception {
    String testCode =
        "function foo(x, y) {" +
        "  var a = x + y;" +
        "  function bar() { return a; }" +
        "  return bar();" +
        "}" +
        "function main() {" +
        "  print(foo(2, 3));" +
        "}" +
        "main();";

    String expectedOutput =
        "function foo(x, y) {" +
        "  var a = x + y;" +
        "  function bar() { return a; }" +
        "  return bar();" +
        "}" +
        "function main() {" +
        "  print(foo(2, 3));" +
        "}" +
        "main();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameVars(compiler, "", false, true, false, null, null, null);
    CompilerResult result = compiler.compileExternal(testCode, options, pass);

    assertEquals(expectedOutput, result.toString());
  }

  public void testRenameVarsWithGeneratePseudoNames() throws Exception {
    String testCode =
        "function foo(x, y) {" +
        "  var a = x + y;" +
        "  return a;" +
        "}" +
        "function main() {" +
        "  print(foo(2, 3));" +
        "}" +
        "main();";

    String expectedOutput =
        "function foo(x, y) {" +
        "  var a = $2;" +
        "  return a;" +
        "}" +
        "function main() {" +
        "  print(foo(2, 3));" +
        "}" +
        "main();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameVars(compiler, "", false, false, true, null, null, null);
    CompilerResult result = compiler.compileExternal(testCode, options, pass);

    assertEquals(expectedOutput, result.toString());
  }

  public void testRenameVarsWithLocalRenamingOnly() throws Exception {
    String testCode =
        "function foo(x, y) {" +
        "  var a = x + y;" +
        "  return a;" +
        "}" +
        "function main() {" +
        "  print(foo(2, 3));" +
        "}" +
        "var x = 2;" +
        "var y = 3;" +
        "function bar() {" +
        "  print(foo(x, y));" +
        "}" +
        "bar();";

    String expectedOutput =
        "function foo(x, y) {" +
        "  var a = _0;" +
        "  return a;" +
        "}" +
        "function main() {" +
        "  print(foo(2, 3));" +
        "}" +
        "var x = _1;" +
        "var y = _2;" +
        "function bar() {" +
        "  print(foo(x, y));" +
        "}" +
        "bar();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameVars(compiler, "", true, false, false, null, null, null);
    CompilerResult result = compiler.compileExternal(testCode, options, pass);

    assertEquals(expectedOutput, result.toString());
  }

  public void testRenameVarsWithExterns() throws Exception {
    String testCode =
        "String x = 'hello';" +
        "function foo() {" +
        "  return x;" +
        "}" +
        "function main() {" +
        "  print(foo());" +
        "}" +
        "main();";

    String expectedOutput =
        "String x = 'hello';" +
        "function foo() {" +
        "  return _0;" +
        "}" +
        "function main() {" +
        "  print(foo());" +
        "}" +
        "main();";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameVars(compiler, "", false, false, false, null, null, Sets.newHashSet("x"));
    CompilerResult result = compiler.compileExternal(testCode, options, pass);

    assertEquals(expectedOutput, result.toString());
  }

  public void testRenameVarsWithReservedNames() throws Exception {
    String testCode =
        "function foo() {" +
        "  var x = 1;" +
        "  var if = 1;" +
        "  return x + if;" +
        "}" +
        "function main() {" +
        "  print(foo());" +
        "}" +
        "main();";

    String expectedOutput =
        "function foo() {" +
        "  var x = _0;" +
        "  var if = _1;" +
        "  return x + if;" +
        "}" +
        "function main() {" +
        "  print(foo());" +
        "}" +
        "main();";

    Set<String> reservedNames = new HashSet<String>();
    reservedNames.add("if");

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameVars(compiler, "", false, false, false, null, null, reservedNames);
    CompilerResult result = compiler.compileExternal(testCode, options, pass);

    assertEquals(expectedOutput, result.toString());
  }

  public void testRenameVarsWithReservedCharacters() throws Exception {
    String testCode =
        "function foo() {" +
        "  var x = 1;" +
        "  var $ = 1;" +
        "  return x + $;" +
        "}" +
        "function main() {" +
        "  print(foo());" +
        "}" +
        "main();";

    String expectedOutput =
        "function foo() {" +
        "  var x = _0;" +
        "  var $ = _1;" +
        "  return x + $;" +
        "}" +
        "function main() {" +
        "  print(foo());" +
        "}" +
        "main();";

    char[] reservedCharacters = {'$'};

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameVars(compiler, "", false, false, false, null, reservedCharacters, null);
    CompilerResult result = compiler.compileExternal(testCode, options, pass);

    assertEquals(expectedOutput, result.toString());
  }

  public void testRenameVarsWithEmptyCode() throws Exception {
    String testCode = "";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameVars(compiler, "", false, false, false, null, null, null);
    CompilerResult result = compiler.compileExternal(testCode, options, pass);

    // The compiler should not produce any output
    assertEquals("", result.toString());
  }
}