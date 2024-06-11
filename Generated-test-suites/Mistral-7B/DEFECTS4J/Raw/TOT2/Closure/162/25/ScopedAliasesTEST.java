package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.GOOG_SCOPE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.test.TestCase;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class ScopedAliasesTest extends TestCase {

  @Test
  public void testScopedAliases() throws Exception {
    String jsSource = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    Node expected = Node.newExpression(Node.OP_IDENT, "goog.dom.createElement");
    Node actual = root.getFirstChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testScopedAliasesWithInvalidFunctionCall() throws Exception {
    String jsSource = "goog.scope(function() {" +
        "  function foo() {" +
        "    goog.scope(function() {" +
        "      goog.dom.createElement('DIV'); " +
        "    });" +
        "  } " +
        "  foo(); " +
        "});";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // The first goog.scope call is valid, so no error should be reported.
    // However, the second call is nested inside a function, which is not allowed.
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliasesWithInvalidFunctionDefinition() throws Exception {
    String jsSource = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    function foo() {" +
        "    } " +
        "  });" +
        "});";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // The first goog.scope call is valid, so no error should be reported.
    // However, the second call contains a function definition, which is not allowed.
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliasesWithInvalidAssignment() throws Exception {
    String jsSource = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var goog = {};" +
        "  }); " +
        "  goog.dom = {'TagName': {'DIV': null}};" +
        "});";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // The first goog.scope call is valid, so no error should be reported.
    // However, the second call assigns to a global variable, which is not allowed.
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliasesWithInvalidReturnStatement() throws Exception {
    String jsSource = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    return \"Hello World\";" +
        "  });" +
        "});";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // The first goog.scope call is valid, so no error should be reported.
    // However, the second call contains a return statement, which is not allowed.
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliasesWithInvalidThrowStatement() throws Exception {
    String jsSource = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    throw new Error(\"Hello World\");" +
        "  });" +
        "});";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // The first goog.scope call is valid, so no error should be reported.
    // However, the second call contains a throw statement, which is not allowed.
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliasesWithAliasRedefinition() throws Exception {
    String jsSource = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var goog = {};" +
        "    goog.dom = {'TagName': {'DIV': null}};" +
        "    goog.Scope = com.google.javascript.jscomp.Scope;" +
        "  }); " +
        "  goog.Scope = function() {};" +
        "});";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // The first goog.scope call is valid, so no error should be reported.
    // However, the second call redefines the alias "goog", which is not allowed.
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliasesWithAliasUsage() throws Exception {
    String jsSource = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "  }); " +
        "  dom.createElement('DIV');" +
        "});";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // The first goog.scope call is valid, so no error should be reported.
    // The alias "dom" is used in the second call to createElement, so it should be reported as an alias usage.
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliasesWithAliasDefinitionInGlobalScope() throws Exception {
    String jsSource = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var goog = {};" +
        "  }); " +
        "  goog.dom = {'TagName': {'DIV': null}};" +
        "}); " +
        "goog.dom.createElement('DIV');";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // The first goog.scope call is valid, so no error should be reported.
    // The alias "dom" is defined inside the first goog.scope block, but it is later used in the global scope,
    // so it should be reported as an alias definition in the global scope.
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliasesWithAliasDefinitionInFunctionScope() throws Exception {
    String jsSource = "function foo() {" +
        "  goog.scope(function() {" +
        "    var goog = {};" +
        "  }); " +
        "  goog.dom = {'TagName': {'DIV': null}};" +
        "  return goog.dom.createElement('DIV');" +
        "}";

    SourceFile file = new SourceFile("Test.js", jsSource);
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new HotSwapCompilerPass.AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(String fileName, SourcePosition position,
          String alias, String from) {
        System.out.println("Logged alias: " + alias + " from " + from);
      }
    });

    Compiler compiler = new Compiler();
    compiler.compile(file, options);

    Node root = file.getTree();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // The first goog.scope call is valid, so no error should be reported.
    // The alias "dom" is defined inside the first goog.scope block, but it is not reported as an alias usage
    // because the function returns before it is used.
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }
}