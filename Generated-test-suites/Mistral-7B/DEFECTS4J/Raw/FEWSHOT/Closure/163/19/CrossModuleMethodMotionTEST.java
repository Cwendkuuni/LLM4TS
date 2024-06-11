package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CompilerOptions.WarningLevel.QUIET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Logger;
import com.google.javascript.jscomp.ModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.IR;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private Node root;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(QUIET, Diagnostics.Type.ALL);
    options.setExperimentalOptions(ParserOptions.ExperimentalOption.STRICT_MODE_CHECKS, true);
    compiler = new Compiler(options);
  }

  @Test
  public void testMoveMethods() {
    String sourceCode =
        "var Foo = function() {};\n" +
        "Foo.prototype.bar = function() { return 1; };\n" +
        "var A = function() {};\n" +
        "A.prototype.baz = function() { return Foo.prototype.bar(); };\n" +
        "var B = function() {};\n" +
        "B.prototype.qux = function() { return A.prototype.baz(); };";

    root = compiler.parseJavaScriptText(sourceCode, "test.js");

    AbstractCompiler abstractCompiler = compiler;
    AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(
        abstractCompiler, abstractCompiler.getModuleGraph(), false, false);

    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(
        abstractCompiler, new CrossModuleMethodMotion.IdGenerator(), false);

    crossModuleMethodMotion.process(null, root);

    JSModule aModule = abstractCompiler.getModuleGraph().getModuleByName("A");
    JSModule bModule = abstractCompiler.getModuleGraph().getModuleByName("B");

    assertEquals(3, aModule.getExportedNames().size());
    assertEquals("baz", aModule.getExportedNames().get(0).getName());
    assertEquals("Function", aModule.getExportedNames().get(1).getName());
    assertEquals("Foo", aModule.getExportedNames().get(2).getName());

    assertEquals(2, bModule.getExportedNames().size());
    assertEquals("qux", bModule.getExportedNames().get(0).getName());
    assertEquals("Function", bModule.getExportedNames().get(1).getName());

    Node aPrototype = aModule.getSourceNode().getFirstChild();
    Node bPrototype = bModule.getSourceNode().getFirstChild();

    assertEquals("Function", aPrototype.getClass().getName());
    assertEquals("Function", bPrototype.getClass().getName());

    Node aBar = aPrototype.getFirstChild();
    Node bQux = bPrototype.getFirstChild();

    assertNull(aBar);
    assertNull(bQux.getFirstChild());

    Node aBarStub = aPrototype.getLastChild();
    Node bQuxStub = bPrototype.getLastChild();

    assertEquals("Function", aBarStub.getClass().getName());
    assertEquals("Function", bQuxStub.getClass().getName());

    assertEquals("JSCompiler_stubMethod", aBarStub.getFirstChild().getFirstChild().getName().getIdentifier());
    assertEquals("JSCompiler_stubMethod", bQuxStub.getFirstChild().getFirstChild().getName().getIdentifier());

    assertEquals("function JSCompiler_unstubMethod(id, body) { return body; }", aBarStub.getLastChild().toString());
    assertEquals("function JSCompiler_unstubMethod(id, body) { return body; }", bQuxStub.getLastChild().toString());
  }
}

class CrossModuleMethodMotionTest {

  // ... (the rest of your code)
}