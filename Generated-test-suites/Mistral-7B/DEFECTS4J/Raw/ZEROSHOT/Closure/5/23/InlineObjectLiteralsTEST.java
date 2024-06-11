import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new InlineObjectLiterals(compiler, () -> "temp_"));
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: function() {}," +
        "    d: null," +
        "    e: {f: 3}" +
        "  };" +
        "  var x = obj;" +
        "  x.c();" +
        "}";

    Node rootNode = compiler.parseTree(jsCode, null, null);
    passManager.process(rootNode, null);
    String outputCode = compiler.getSourceOutput();

    String expectedCode = "function foo() {" +
        "  var a, b, c, d, e, f;" +
        "  a = 1;" +
        "  b = 2;" +
        "  c = function() {};" +
        "  d = null;" +
        "  e = {" +
        "    f: 3" +
        "  }" +
        "  var x = a;" +
        "  c();" +
        "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new InlineObjectLiterals(compiler, () -> "temp_"));
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    getA: function() { return this.a; }," +
        "    a: 1" +
        "  };" +
        "  var x = obj.getA();" +
        "}";

    Node rootNode = compiler.parseTree(jsCode, null, null);
    passManager.process(rootNode, null);
    String outputCode = compiler.getSourceOutput();

    String expectedCode = "function foo() {" +
        "  var a, getA;" +
        "  a = 1;" +
        "  getA = function() { return a; };" +
        "  var x = getA();" +
        "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new InlineObjectLiterals(compiler, () -> "temp_"));
    String jsCode = "export var obj = {" +
        "  a: 1," +
        "  b: 2," +
        "  c: function() {}," +
        "  d: null," +
        "  e: {f: 3}" +
        "};";

    Node rootNode = compiler.parseTree(jsCode, null, null);
    passManager.process(rootNode, null);
    String outputCode = compiler.getSourceOutput();

    // Exported variable cannot be inlined.
    assertTrue(outputCode.contains("export var obj = "));
  }

  @Test
  public void testInlineObjectLiteralsWithExternVariable() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new InlineObjectLiterals(compiler, () -> "temp_"));
    String jsCode = "var obj = {" +
        "  //jshint unused:vars" +
        "  a: 1," +
        "  b: 2," +
        "  c: function() {}," +
        "  d: null," +
        "  e: {f: 3}" +
        "};";

    Node rootNode = compiler.parseTree(jsCode, null, null);
    passManager.process(rootNode, null);
    String outputCode = compiler.getSourceOutput();

    // Extern variables cannot be inlined.
    assertTrue(outputCode.contains("var obj = "));
  }

  @Test
  public void testInlineObjectLiteralsWithRenamePropertyFunctionName() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new InlineObjectLiterals(compiler, () -> "temp_"));
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    __$RenamePropertyFunctionName$: function() { return this.a; }," +
        "    a: 1" +
        "  };" +
        "  var x = obj.__$RenamePropertyFunctionName$();" +
        "}";

    Node rootNode = compiler.parseTree(jsCode, null, null);
    passManager.process(rootNode, null);
    String outputCode = compiler.getSourceOutput();

    String expectedCode = "function foo() {" +
        "  var a;" +
        "  a = 1;" +
        "  var x = a;" +
        "}";

    // RenamePropertyFunctionName should not be inlined.
    assertFalse(outputCode.contains("__$RenamePropertyFunctionName$"));
    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testInlineObjectLiteralsWithDeletedProperty() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new InlineObjectLiterals(compiler, () -> "temp_"));
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    delete b" +
        "  };" +
        "  var x = obj.a;" +
        "}";

    Node rootNode = compiler.parseTree(jsCode, null, null);
    passManager.process(rootNode, null);
    String outputCode = compiler.getSourceOutput();

    String expectedCode = "function foo() {" +
        "  var a, x;" +
        "  a = 1;" +
        "  x = a;" +
        "}";

    assertEquals(expectedCode, outputCode);
  }
}