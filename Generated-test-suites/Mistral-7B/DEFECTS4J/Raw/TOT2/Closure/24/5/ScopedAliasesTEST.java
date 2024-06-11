import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.rhino.SourceFile;

import org.junit.BeforeClass;
import org.junit.Test;

public class ScopedAliasesTest extends CompilationTestBase {

  private static SourceFile inputSource;
  private static Node root;

  @BeforeClass
  public static void setUp() throws Exception {
    inputSource = new SourceFile("TestScopedAliases.java",
        "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  static void testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      var dom = goog.dom;\n" +
        "      var DIV = dom.TagName.DIV;\n" +
        "      dom.createElement(DIV);\n" +
        "    });\n" +
        "  }\n" +
        "}\n");

    compile(inputSource, null, getDefaultOptions().with(AliasTransformation.ENABLED));
    root = getRootNode(inputSource);
  }

  @Test
  public void testProcess_NoScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(inputSource.getProgram(), root);
    assertNoCompilationErrors();
  }

  @Test
  public void testProcess_ImproperUsageOfGoogScope() {
    // Create a test case for the GOOG_SCOPE_USED_IMPROPERLY error
    SourceFile inputSourceWithError = new SourceFile("TestScopedAliasesError.java",
        "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  static void testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      var dom = goog.dom;\n" +
        "      var DIV = dom.TagName.DIV;\n" +
        "      dom.createElement(DIV);\n" +
        "    });\n" +
        "    goog.scope(function() {\n" +
        "    });\n" +
        "  }\n" +
        "}\n");

    compile(inputSourceWithError, null, getDefaultOptions().with(AliasTransformation.ENABLED));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(inputSourceWithError.getProgram(), root);

    List<DiagnosticInfo> diagnostics = getDiagnostics();
    assertEquals("Expected one error, but got " + diagnostics.size(), 1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY: The call to goog.scope must be alone in a single statement.",
        diagnostics.get(0).getMessage(), diagnostics.get(0).getMessage());
  }

  @Test
  public void testProcess_BadParametersInGoogleScope() {
    // Create a test case for the GOOG_SCOPE_HAS_BAD_PARAMETERS error
    SourceFile inputSourceWithError = new SourceFile("TestScopedAliasesError.java",
        "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  static void testScopedAliases() {\n" +
        "    goog.scope(function(arg1, arg2) {\n" +
        "      goog.scope(function() {\n" +
        "      });\n" +
        "    });\n" +
        "  }\n" +
        "}\n");

    compile(inputSourceWithError, null, getDefaultOptions().with(AliasTransformation.ENABLED));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(inputSourceWithError.getProgram(), root);

    List<DiagnosticInfo> diagnostics = getDiagnostics();
    assertEquals("Expected one error, but got " + diagnostics.size(), 1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS: The call to goog.scope must take only a single parameter.  It must" +
        " be an anonymous function that itself takes no parameters.",
        diagnostics.get(0).getMessage(), diagnostics.get(0).getMessage());
  }

  @Test
  public void testProcess_ReferenceToThisInGoogleScope() {
    // Create a test case for the GOOG_SCOPE_REFERENCES_THIS error
    SourceFile inputSourceWithError = new SourceFile("TestScopedAliasesError.java",
        "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  static void testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      this.test = 'test';\n" +
        "    });\n" +
        "  }\n" +
        "}\n");

    compile(inputSourceWithError, null, getDefaultOptions().with(AliasTransformation.ENABLED));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(inputSourceWithError.getProgram(), root);

    List<DiagnosticInfo> diagnostics = getDiagnostics();
    assertEquals("Expected one error, but got " + diagnostics.size(), 1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS: The body of a goog.scope function cannot reference 'this'",
        diagnostics.get(0).getMessage(), diagnostics.get(0).getMessage());
  }

  @Test
  public void testProcess_UseReturnInGoogleScope() {
    // Create a test case for the GOOG_SCOPE_USES_RETURN error
    SourceFile inputSourceWithError = new SourceFile("TestScopedAliasesError.java",
        "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  static void testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      return 'test';\n" +
        "    });\n" +
        "  }\n" +
        "}\n");

    compile(inputSourceWithError, null, getDefaultOptions().with(AliasTransformation.ENABLED));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(inputSourceWithError.getProgram(), root);

    List<DiagnosticInfo> diagnostics = getDiagnostics();
    assertEquals("Expected one error, but got " + diagnostics.size(), 1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN: The body of a goog.scope function cannot use 'return'",
        diagnostics.get(0).getMessage(), diagnostics.get(0).getMessage());
  }

  @Test
  public void testProcess_UseThrowInGoogleScope() {
    // Create a test case for the GOOG_SCOPE_USES_THROW error
    SourceFile inputSourceWithError = new SourceFile("TestScopedAliasesError.java",
        "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  static void testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      throw new Error('test');\n" +
        "    });\n" +
        "  }\n" +
        "}\n");

    compile(inputSourceWithError, null, getDefaultOptions().with(AliasTransformation.ENABLED));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(inputSourceWithError.getProgram(), root);

    List<DiagnosticInfo> diagnostics = getDiagnostics();
    assertEquals("Expected one error, but got " + diagnostics.size(), 1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW: The body of a goog.scope function cannot use 'throw'",
        diagnostics.get(0).getMessage(), diagnostics.get(0).getMessage());
  }

  @Test
  public void testProcess_AliasRedefinition() {
    // Create a test case for the GOOG_SCOPE_ALIAS_REDEFINED error
    SourceFile inputSourceWithError = new SourceFile("TestScopedAliasesError.java",
        "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  static void testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      var a = 5;\n" +
        "      var a = 'test';\n" +
        "    });\n" +
        "  }\n" +
        "}\n");

    compile(inputSourceWithError, null, getDefaultOptions().with(AliasTransformation.ENABLED));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(inputSourceWithError.getProgram(), root);

    List<DiagnosticInfo> diagnostics = getDiagnostics();
    assertEquals("Expected one error, but got " + diagnostics.size(), 1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED: The alias 'a' is assigned a value more than once.",
        diagnostics.get(0).getMessage(), diagnostics.get(0).getMessage());
  }

  @Test
  public void testHotSwapScript() {
    // Test hot swap functionality
    Node nodeWithScopeCall = root.getChildAtIndex(1);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.hotSwapScript(root, nodeWithScopeCall);
    assertNoCompilationErrors();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        nodeWithScopeCall.getFirstChild().toString(), nodeWithScopeCall.getFirstChild().toString());
  }
}