import static com.google.javascript.jscomp.CompilerPasses.CrossModuleMethodMotion.STUB_METHOD_NAME;
import static com.google.javascript.jscomp.CompilerPasses.CrossModuleMethodMotion.UNSTUB_METHOD_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPasses;
import com.google.javascript.jscomp.IDiagnosticReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParserTestingUtil;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeOperators;

/**
 * Tests for {@code CrossModuleMethodMotion} pass.
 */
@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  @Test
  public void testSimpleCase() {
    String sourceCode = ""
        + "/** @constructor */\n"
        + "function Foo() {\n"
        + "  this.bar = function() { return 42; };\n"
        + "}\n"
        + "\n"
        + "/** @constructor */\n"
        + "function Bar() {}\n"
        + "\n"
        + "Foo.prototype.qux = function() { return 1; };\n";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setOutputWarningLevel("-W:verbose");
    IDiagnosticReporter diagnostics = new IDiagnosticReporter() {
      @Override
      public void reportDiagnostics(Node tree, CompilerPass pass) {
        for (Node diagnostic : tree.getDiagnostics()) {
          System.out.println(diagnostic.toString());
        }
      }
    };

    compiler.setOptionsForTest(options);
    compiler.setOptions(options);
    compiler.setErrorReporter(diagnostics);

    SourceFile source = new SourceFile("test.js", sourceCode);
    Node root = ParserTestingUtil.parseAndCompile(compiler, source);

    CompilerPass passes = new CompilerPasses();
    passes.addPass(new CrossModuleMethodMotion(compiler, passes.newIdGenerator(), true));
    passes.process(null, root);

    assertNull(root.getFirstChildByName(STUB_METHOD_NAME));
    Node qux = root.getFirstChildByName("Foo.prototype.qux");
    assertNotNull(qux);
    Node stub = qux.getChild(0);
    assertEquals("JSCompiler_stubMethod(JSCompiler_stubMethod_id)", stub.getClass().getName());
    Node unstub = root.getFirstChildByName("Foo.prototype.qux").getLastChild();
    assertEquals("JSCompiler_unstubMethod(JSCompiler_stubMethod_id, body)", unstub.getClass().getName());
    assertEquals(Arrays.asList("return 1;"), NodeOperators.getCallArguments(unstub));
  }

  @Test
  public void testComplexCase() {
    // This test case is more complex as it involves multiple modules and
    // prototype chains with circular references.
    String sourceCode = ""
        + "/** @constructor */\n"
        + "function Foo() {}\n"
        + "\n"
        + "Foo.prototype.bar = function() {\n"
        + "  return function() {\n"
        + "    return this.baz();\n"
        + "  }.bind(this);\n"
        + "}\n"
        + "\n"
        + "Foo.prototype.baz = function() {\n"
        + "  return 42;\n"
        + "}\n"
        + "\n"
        + "/** @constructor */\n"
        + "function Bar() {\n"
        + "  this.qux = function() {\n"
        + "    return function() {\n"
        + "      return this.foo.bar();\n"
        + "    }.bind(this);\n"
        + "  }.bind(this);\n"
        + "}\n"
        + "\n"
        + "Bar.prototype.foo = new Foo();\n";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setOutputWarningLevel("-W:verbose");
    IDiagnosticReporter diagnostics = new IDiagnosticReporter() {
      @Override
      public void reportDiagnostics(Node tree, CompilerPass pass) {
        for (Node diagnostic : tree.getDiagnostics()) {
          System.out.println(diagnostic.toString());
        }
      }
    };

    compiler.setOptionsForTest(options);
    compiler.setOptions(options);
    compiler.setErrorReporter(diagnostics);

    SourceFile source = new SourceFile("test.js", sourceCode);
    Node root = ParserTestingUtil.parseAndCompile(compiler, source);

    CompilerPass passes = new CompilerPasses();
    passes.addPass(new CrossModuleMethodMotion(compiler, passes.newIdGenerator(), true));
    passes.process(null, root);

    // The test checks if the method stubbing and unstubbing have been
    // performed correctly.
    Node bar = root.getFirstChildByName("Foo.prototype.bar");
    assertNotNull(bar);
    Node stub = bar.getChild(0);
    assertEquals("JSCompiler_stubMethod(JSCompiler_stubMethod_id)", stub.getClass().getName());
    Node unstub = root.getFirstChildByName("Foo.prototype.bar").getLastChild();
    assertEquals("JSCompiler_unstubMethod(JSCompiler_stubMethod_id, body)", unstub.getClass().getName());
    assertEquals(Arrays.asList("return this.baz();"), NodeOperators.getCallArguments(unstub));

    Node qux = root.getFirstChildByName("Bar.prototype.qux");
    assertNotNull(qux);
    stub = qux.getChild(0);
    assertEquals("JSCompiler_stubMethod(JSCompiler_stubMethod_id)", stub.getClass().getName());
    unstub = root.getFirstChildByName("Bar.prototype.qux").getLastChild();
    assertEquals("JSCompiler_unstubMethod(JSCompiler_stubMethod_id, body)", unstub.getClass().getName());
    assertEquals(Arrays.asList("return this.foo.bar();"), NodeOperators.getCallArguments(unstub));
  }

  @Test
  public void testErrorScenario() {
    String sourceCode = ""
        + "/** @constructor */\n"
        + "function Foo() {\n"
        + "}\n"
        + "\n"
        + "Foo.prototype.bar = function() {\n"
        + "  return function() {\n"
        + "    return this.baz();\n"
        + "  }.bind(null);\n" // null reference
        + "}\n"
        + "\n"
        + "Foo.prototype.baz = function() {\n"
        + "  return 42;\n"
        + "}\n";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setOutputWarningLevel("-W:verbose");
    IDiagnosticReporter diagnostics = new IDiagnosticReporter() {
      @Override
      public void reportDiagnostics(Node tree, CompilerPass pass) {
        for (Node diagnostic : tree.getDiagnostics()) {
          System.out.println(diagnostic.toString());
        }
      }
    };

    compiler.setOptionsForTest(options);
    compiler.setOptions(options);
    compiler.setErrorReporter(diagnostics);

    SourceFile source = new SourceFile("test.js", sourceCode);
    Node root = ParserTestingUtil.parseAndCompile(compiler, source);

    CompilerPass passes = new CompilerPasses();
    passes.addPass(new CrossModuleMethodMotion(compiler, passes.newIdGenerator(), true));

    try {
      passes.process(null, root);
      fail("Expected error but no error was reported.");
    } catch (RuntimeException e) {
      // Expected error
    }
  }
}