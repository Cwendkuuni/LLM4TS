package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParserTestUtil;
import com.google.javascript.jscomp.test_util.ProcessingEnvironment;
import com.google.javascript.rhino.IR;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private static final String TEST_FILE = "CrossModuleMethodMotionTest.js";

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private ProcessingEnvironment processingEnv;

  @Before
  public void setup() {
    processingEnv = new ProcessingEnvironment();
    CompilerOptions options = new CompilerOptions(processingEnv);
    options.setWarningLevel(CompilerOptions.WarningLevel.VERBOSE);
    compiler = new compiler.AbstractCompiler(processingEnv, options);
    passManager = new CompilerPassManager(compiler);
  }

  @Test
  public void testMoveMethod() {
    String source = "(" +
        "function Foo() {}" +
        "Foo.prototype.bar = function() {" +
        "  return 1;" +
        "};" +
        ")" +
        "(" +
        "function Bar() {}" +
        ")" +
        "(" +
        "function Baz() {" +
        "  Foo.prototype.bar = function() {" +
        "    return 2;" +
        "  };" +
        "}" +
        ")";

    Node externRoot = ParserTestUtil.parseExterns(processingEnv, source);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, null, true);
    passManager.addPass(pass);
    Node root = ParserTestUtil.parse(processingEnv, source);
    passManager.runPasses(externRoot, root);

    JSModule fooModule = moduleGraph.getModuleForName("Foo");
    JSModule barModule = moduleGraph.getModuleForName("Bar");
    JSModule bazModule = moduleGraph.getModuleForName("Baz");

    Node fooProto = fooModule.getType("Foo").getPrototypeNode();
    Node barFunction = fooProto.getChild(0);
    Node bazProto = bazModule.getType("Baz").getPrototypeNode();
    Node bazBarFunction = bazProto.getChild(0);

    assertTrue(barFunction.getCallCount() > 0);
    assertSame(barFunction, bazBarFunction);
  }

  @Test
  public void testMoveMethodWithStub() {
    String source = "(" +
        "function Foo() {}" +
        "Foo.prototype.bar = function() {" +
        "  return 1;" +
        "};" +
        ")" +
        "(" +
        "function Bar() {}" +
        ")" +
        "(" +
        "function Baz() {" +
        "  Foo.prototype.bar = JSCompiler_stubMethod(1);" +
        "}" +
        ")";

    Node externRoot = ParserTestUtil.parseExterns(processingEnv, source);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, null, true);
    passManager.addPass(pass);
    Node root = ParserTestUtil.parse(processingEnv, source);
    passManager.runPasses(externRoot, root);

    JSModule fooModule = moduleGraph.getModuleForName("Foo");
    JSModule barModule = moduleGraph.getModuleForName("Bar");
    JSModule bazModule = moduleGraph.getModuleForName("Baz");

    Node fooProto = fooModule.getType("Foo").getPrototypeNode();
    Node fooBarFunction = fooProto.getChild(0);
    Node bazProto = bazModule.getType("Baz").getPrototypeNode();
    Node bazBarFunction = bazProto.getChild(0);

    assertTrue(fooBarFunction.isFunction());
    assertFalse(fooBarFunction.getCallCount() > 0);
    assertNull(fooBarFunction.getCalls());

    assertEquals(1, bazBarFunction.getCallCount());
    assertSame(fooBarFunction, bazBarFunction);
  }

  @Test
  public void testMoveMethodWithNoModuleDependencies() {
    String source = "(" +
        "function Foo() {}" +
        "Foo.prototype.bar = function() {" +
        "  return 1;" +
        "};" +
        ")" +
        "(" +
        "function Bar() {" +
        "  Foo.prototype.bar = function() {" +
        "    return 2;" +
        "  };" +
        "}" +
        ")";

    Node externRoot = ParserTestUtil.parseExterns(processingEnv, source);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, null, true);
    passManager.addPass(pass);
    Node root = ParserTestUtil.parse(processingEnv, source);
    passManager.runPasses(externRoot, root);

    JSModule fooModule = moduleGraph.getModuleForName("Foo");
    JSModule barModule = moduleGraph.getModuleForName("Bar");

    Node fooProto = fooModule.getType("Foo").getPrototypeNode();
    Node fooBarFunction = fooProto.getChild(0);
    Node barProto = barModule.getType("Bar").getPrototypeNode();
    Node barBarFunction = barProto.getChild(0);

    assertTrue(fooBarFunction.getCallCount() > 0);
    assertSame(fooBarFunction, barBarFunction);
  }

  @Test
  public void testMoveMethodWithNoModuleDependenciesAndStub() {
    String source = "(" +
        "function Foo() {}" +
        "Foo.prototype.bar = function() {" +
        "  return 1;" +
        "};" +
        ")" +
        "(" +
        "function Bar() {" +
        "  Foo.prototype.bar = JSCompiler_stubMethod(1);" +
        "}" +
        ")";

    Node externRoot = ParserTestUtil.parseExterns(processingEnv, source);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, null, true);
    passManager.addPass(pass);
    Node root = ParserTestUtil.parse(processingEnv, source);
    passManager.runPasses(externRoot, root);

    JSModule fooModule = moduleGraph.getModuleForName("Foo");
    JSModule barModule = moduleGraph.getModuleForName("Bar");

    Node fooProto = fooModule.getType("Foo").getPrototypeNode();
    Node fooBarFunction = fooProto.getChild(0);
    Node barProto = barModule.getType("Bar").getPrototypeNode();

    assertTrue(fooBarFunction.isFunction());
    assertFalse(fooBarFunction.getCallCount() > 0);
    assertNull(fooBarFunction.getCalls());

    assertTrue(barProto.getChildCount() > 0);
  }

  @Test
  public void testMoveMethodWithNoModuleDependenciesAndStubAndUnstub() {
    String source = "(" +
        "function Foo() {}" +
        "Foo.prototype.bar = JSCompiler_stubMethod(1);" +
        ")" +
        "(" +
        "function Bar() {" +
        "  Foo.prototype.bar = JSCompiler_unstubMethod(1, function() {" +
        "    return 2;" +
        "  });" +
        "}" +
        ")";

    Node externRoot = ParserTestUtil.parseExterns(processingEnv, source);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, null, true);
    passManager.addPass(pass);
    Node root = ParserTestUtil.parse(processingEnv, source);
    passManager.runPasses(externRoot, root);

    JSModule fooModule = moduleGraph.getModuleForName("Foo");
    JSModule barModule = moduleGraph.getModuleForName("Bar");

    Node fooProto = fooModule.getType("Foo").getPrototypeNode();
    Node fooBarFunction = fooProto.getChild(0);
    Node barProto = barModule.getType("Bar").getPrototypeNode();
    Node barBarFunction = barProto.getChild(0);

    assertFalse(fooBarFunction.isFunction());
    assertNull(fooBarFunction);

    assertTrue(barBarFunction.isFunction());
    assertTrue(barBarFunction.getCallCount() > 0);
    assertSame(fooBarFunction, barBarFunction);
  }
}