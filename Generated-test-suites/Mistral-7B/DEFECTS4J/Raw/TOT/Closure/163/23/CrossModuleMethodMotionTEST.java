import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.jscomp.NodeFlags.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParserOptions;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testMoveMethodAcrossModules() {
    String sourceCode =
        "class A {\n" +
        "  static function foo() {\n" +
        "    return 'A.foo';\n" +
        "  }\n" +
        "  static function bar() {\n" +
        "    return 'A.bar';\n" +
        "  }\n" +
        "}\n" +
        "\n" +
        "class B extends A {\n" +
        "  static function baz() {\n" +
        "    return 'B.baz';\n" +
        "  }\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");
    options.setCompilerFlags(--allowAllDeclarations);

    ParserOptions parserOptions = new ParserOptions();
    Compiler compiler = new Compiler();
    compiler.init();
    compiler.parseAndCompile(parserOptions, sourceCode, options, null);

    Node root = compiler.getRootNode();

    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, idGenerator, true);
    pass.process(null, root);
    compiler.process();

    Node outRoot = compiler.getRootNode();
    Node aPrototype = outRoot.getChild(0).getChild(0).getChild(0);
    Node bPrototype = outRoot.getChild(1).getChild(0).getChild(0);

    Node aFoo = aPrototype.getChild(1);
    Node aBar = aPrototype.getChild(2);
    Node bBaz = bPrototype.getChild(1);

    // Check that A.foo and A.bar are stubbed in module B
    assertEquals("Expected A.foo to be stubbed in module B",
        "function JSCompiler_stubMethod(JSCompiler_stubMethod_id) { " +
            "return function() { " +
            " JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(this, arguments); " +
            "};}",
        bPrototype.toString(PrintFlags.none()));

    assertEquals("Expected A.bar to be stubbed in module B",
        "function JSCompiler_stubMethod(JSCompiler_stubMethod_id) { " +
            "return function() { " +
            " JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(this, arguments); " +
            "};}",
        bPrototype.getChild(2).toString(PrintFlags.none()));

    // Check that B.baz is moved to module A
    Node aBaz = aPrototype.getChild(3);
    assertEquals("Expected B.baz to be moved to module A",
        "function baz() { return 'B.baz'; }",
        aPrototype.getChild(3).toString(PrintFlags.none()));
  }

  @Test
  public void testMoveMethodToSameModule() {
    String sourceCode =
        "class A {\n" +
        "  static function foo() {\n" +
        "    return 'A.foo';\n" +
        "  }\n" +
        "  static function bar() {\n" +
        "    return 'A.bar';\n" +
        "  }\n" +
        "  static function baz() {\n" +
        "    return 'A.baz';\n" +
        "  }\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");
    options.setCompilerFlags(--allowAllDeclarations);

    ParserOptions parserOptions = new ParserOptions();
    Compiler compiler = new Compiler();
    compiler.init();
    compiler.parseAndCompile(parserOptions, sourceCode, options, null);

    Node root = compiler.getRootNode();

    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, idGenerator, true);
    pass.process(null, root);
    compiler.process();

    Node outRoot = compiler.getRootNode();
    Node aPrototype = outRoot.getChild(0).getChild(0).getChild(0);

    // Check that nothing changes when moving methods within the same module
    Node aFoo = aPrototype.getChild(1);
    Node aBar = aPrototype.getChild(2);
    Node aBaz = aPrototype.getChild(3);

    assertEquals("Expected A.foo to remain the same",
        "function foo() { return 'A.foo'; }",
        aFoo.toString(PrintFlags.none()));

    assertEquals("Expected A.bar to remain the same",
        "function bar() { return 'A.bar'; }",
        aBar.toString(PrintFlags.none()));

    assertEquals("Expected A.baz to remain the same",
        "function baz() { return 'A.baz'; }",
        aBaz.toString(PrintFlags.none()));
  }

  @Test
  public void testMoveMethodWithClosureVariable() {
    String sourceCode =
        "class A {\n" +
        "  static function foo() {\n" +
        "    var x = 1;\n" +
        "    return function() { return x; };\n" +
        "  }\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");
    options.setCompilerFlags(--allowAllDeclarations);

    ParserOptions parserOptions = new ParserOptions();
    Compiler compiler = new Compiler();
    compiler.init();
    compiler.parseAndCompile(parserOptions, sourceCode, options, null);

    Node root = compiler.getRootNode();

    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, idGenerator, true);
    pass.process(null, root);
    compiler.process();

    Node outRoot = compiler.getRootNode();
    Node aPrototype = outRoot.getChild(0).getChild(0).getChild(0);

    // Check that the closure variable remains the same
    Node aFoo = aPrototype.getChild(1);
    Node closure = aFoo.getChild(0);

    assertEquals("Expected closure variable to remain the same",
        "var x = 1",
        closure.toString(PrintFlags.none()));
  }

  @Test
  public void testNullDeepestCommonModuleRef() {
    String sourceCode =
        "class A {\n" +
        "  static function foo() {\n" +
        "    return null;\n" +
        "  }\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");
    options.setCompilerFlags(--allowAllDeclarations);

    ParserOptions parserOptions = new ParserOptions();
    Compiler compiler = new Compiler();
    compiler.init();
    compiler.parseAndCompile(parserOptions, sourceCode, options, null);

    Node root = compiler.getRootNode();

    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, idGenerator, true);
    pass.process(null, root);

    try {
      compiler.process();
      fail("Expected compiler to report an error for null deepest common module");
    } catch (RuntimeException e) {
      String expectedError =
          "com.google.javascript.jscomp.CrossModuleMethodMotion$" +
          "NULL_COMMON_MODULE_ERROR: null deepest common module";
      assertTrue(e.getMessage().contains(expectedError));
    }
  }

  @Test
  public void testMoveMethodToExterns() {
    String sourceCode =
        "/**\n" +
        " * @extern\n" +
        " */\n" +
        "function foo() {\n" +
        "  return 'foo';\n" +
        "}\n" +
        "\n" +
        "class A {\n" +
        "  static function bar() {\n" +
        "    return 'A.bar';\n" +
        "  }\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("out.js");
    options.setCompilerFlags(--allowAllDeclarations);

    ParserOptions parserOptions = new ParserOptions();
    Compiler compiler = new Compiler();
    compiler.init();
    compiler.parseAndCompile(parserOptions, sourceCode, options, null);

    Node root = compiler.getRootNode();

    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, idGenerator, true);
    pass.process(null, root);
    compiler.process();

    Node outRoot = compiler.getRootNode();
    Node aPrototype = outRoot.getChild(0).getChild(0).getChild(0);

    // Check that foo remains in the externs
    Node externsRoot = outRoot.getFirstChild();

    // Check that A.bar is stubbed in module A
    Node aBar = aPrototype.getChild(1);
    assertEquals("Expected A.bar to be stubbed in module A",
        "function JSCompiler_stubMethod(JSCompiler_stubMethod_id) { " +
            "return function() { " +
            " JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(this, arguments); " +
            "};}",
        aBar.toString(PrintFlags.none()));
  }
}