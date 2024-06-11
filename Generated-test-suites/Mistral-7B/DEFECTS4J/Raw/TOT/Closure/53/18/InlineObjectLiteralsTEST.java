import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private ReferenceCollectingCallback callback;
  private AbstractCompiler abstractCompiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    abstractCompiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = mock(Supplier.class);
    compiler = new Compiler(null, null, null, null, null, null, null, null);
    compiler.setCodingConvention(mock(CodingConvention.class));
    compiler.setProcessor(mock(Processor.class));
    compiler.setOptions(new CompilerOptions());
    compiler.setCompilerPassManager(new CompilerPassManager());

    callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
  }

  @Test
  public void testInlineObjectLiteral() {
    // Given a simple object literal
    String source = "function foo() {\n" +
      "  var obj = {\n" +
      "    a: 1,\n" +
      "    b: 2,\n" +
      "    c: 3\n" +
      "  };\n" +
      "  return obj.a + obj.b;\n" +
      "}";

    Node externs = NodeUtil.parseTree(source, null, null);

    Scope rootScope = new Scope(NodeUtil.getRootNode(externs), null);

    compiler.initPasses();

    // When the InlineObjectLiterals pass is run
    compiler.getCompilerPassManager().runPasses(externs, rootScope, null);

    // Then the object literal should be inlined into separate variables
    Node rootNode = NodeUtil.getRootNode(externs);

    assertEquals(
        "function foo() {\n" +
        "  var a = JSCompiler_object_inline_a_1;\n" +
        "  var b = JSCompiler_object_inline_b_2;\n" +
        "  var c = JSCompiler_object_inline_c_3;\n" +
        "  return a + b;\n" +
        "}",
        rootNode.toString());

    // And the return statement should reference the inlined variables
    Node returnStatement = NodeUtil.getFirstChildOfType(
        rootNode.getFirstChild(), Token.EXPR_STATEMENT);
    Node expression = NodeUtil.getFirstChildOfType(
        returnStatement, Token.BINARY_EXPR);

    assertEquals(
        "JSCompiler_object_inline_a_1 + JSCompiler_object_inline_b_2",
        expression.toString());
  }

  @Test
  public void testInlineObjectLiteralWithNestedProperties() {
    // Given an object literal with nested properties
    String source = "function foo() {\n" +
      "  var obj = {\n" +
      "    a: {\n" +
      "      b: 1,\n" +
      "      c: 2\n" +
      "    },\n" +
      "    d: 3\n" +
      "  };\n" +
      "  return obj.a.b + obj.d;\n" +
      "}";

    Node externs = NodeUtil.parseTree(source, null, null);

    Scope rootScope = new Scope(NodeUtil.getRootNode(externs), null);

    compiler.initPasses();

    // When the InlineObjectLiterals pass is run
    compiler.getCompilerPassManager().runPasses(externs, rootScope, null);

    // Then the object literal should be inlined into separate variables
    Node rootNode = NodeUtil.getRootNode(externs);

    assertEquals(
        "function foo() {\n" +
        "  var a_b = JSCompiler_object_inline_a_b_1;\n" +
        "  var a_c = JSCompiler_object_inline_a_c_2;\n" +
        "  var d = JSCompiler_object_inline_d_3;\n" +
        "  return a_b + d;\n" +
        "}",
        rootNode.toString());

    // And the return statement should reference the inlined variables
    Node returnStatement = NodeUtil.getFirstChildOfType(
        rootNode.getFirstChild(), Token.EXPR_STATEMENT);
    Node expression = NodeUtil.getFirstChildOfType(
        returnStatement, Token.BINARY_EXPR);

    assertEquals(
        "JSCompiler_object_inline_a_b_1 + JSCompiler_object_inline_d_3",
        expression.toString());
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    // Given an object literal with self-reference
    String source = "function foo() {\n" +
      "  var obj = {\n" +
      "    a: 1,\n" +
      "    b: function() {\n" +
      "      return this.a;\n" +
      "    }\n" +
      "  };\n" +
      "  return obj.b();\n" +
      "}";

    Node externs = NodeUtil.parseTree(source, null, null);

    Scope rootScope = new Scope(NodeUtil.getRootNode(externs), null);

    compiler.initPasses();

    // When the InlineObjectLiterals pass is run
    try {
      compiler.getCompilerPassManager().runPasses(externs, rootScope, null);
      fail("Expected an exception");
    } catch (RuntimeException e) {
      // Then the pass should not inline self-referential object literals
      // and should throw an exception
      assertEquals(
          "Cannot inline self-referential object literals: a is a property of the object being inlined",
          e.getMessage());
    }
  }

  @Test
  public void testInlineObjectLiteralWithCycle() {
    // Given an object literal with cyclic references
    String source = "function foo() {\n" +
      "  var obj1 = {\n" +
      "    a: 1,\n" +
      "    b: obj2\n" +
      "  };\n" +
      "  var obj2 = {\n" +
      "    a: obj1\n" +
      "  };\n" +
      "  return obj1.a + obj2.a;\n" +
      "}";

    Node externs = NodeUtil.parseTree(source, null, null);

    Scope rootScope = new Scope(NodeUtil.getRootNode(externs), null);

    compiler.initPasses();

    // When the InlineObjectLiterals pass is run
    try {
      compiler.getCompilerPassManager().runPasses(externs, rootScope, null);
      fail("Expected an exception");
    } catch (RuntimeException e) {
      // Then the pass should not inline object literals with cyclic references
      // and should throw an exception
      assertEquals(
          "Cannot inline object literals with cyclic references",
          e.getMessage());
    }
  }

  @Test
  public void testInlineObjectLiteralWithNonObjectValue() {
    // Given an object literal with a non-object value
    String source = "function foo() {\n" +
      "  var obj = {\n" +
      "    a: 1,\n" +
      "    b: 'hello'\n" +
      "  };\n" +
      "  return obj.a + obj.b;\n" +
      "}";

    Node externs = NodeUtil.parseTree(source, null, null);

    Scope rootScope = new Scope(NodeUtil.getRootNode(externs), null);

    compiler.initPasses();

    // When the InlineObjectLiterals pass is run
    compiler.getCompilerPassManager().runPasses(externs, rootScope, null);

    // Then the object literal should not be inlined as it contains a non-object value
    Node rootNode = NodeUtil.getRootNode(externs);

    assertEquals(
        "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 'hello'\n" +
        "  };\n" +
        "  return obj.a + obj.b;\n" +
        "}",
        rootNode.toString());
  }

  @Test
  public void testInlineObjectLiteralWithNonLValueReference() {
    // Given an object literal with a non-lvalue reference
    String source = "function foo() {\n" +
      "  var obj = {\n" +
      "    a: 1,\n" +
      "    get b() {\n" +
      "      return this.a;\n" +
      "    }\n" +
      "  };\n" +
      "  return obj.b;\n" +
      "}";

    Node externs = NodeUtil.parseTree(source, null, null);

    Scope rootScope = new Scope(NodeUtil.getRootNode(externs), null);

    compiler.initPasses();

    // When the InlineObjectLiterals pass is run
    compiler.getCompilerPassManager().runPasses(externs, rootScope, null);

    // Then the object literal should not be inlined as getters are not lvalues
    Node rootNode = NodeUtil.getRootNode(externs);

    assertEquals(
        "function foo() {\n" +
        "  var obj = {\n" +
        "    get a() {\n" +
        "      return 1;\n" +
        "    },\n" +
        "    get b() {\n" +
        "      return this.a;\n" +
        "    }\n" +
        "  };\n" +
        "  return obj.b;\n" +
        "}",
        rootNode.toString());
  }

  @Test
  public void testInlineObjectLiteralWithExportedVariable() {
    // Given an object literal with an exported variable
    String source = "var exported = 0;\n" +
      "function foo() {\n" +
      "  var obj = {\n" +
      "    a: 1,\n" +
      "    exported: exported\n" +
      "  };\n" +
      "  return obj.a + obj.exported;\n" +
      "}";

    Node externs = NodeUtil.parseTree(source, null, null);

    Scope rootScope = new Scope(NodeUtil.getRootNode(externs), null);

    compiler.initPasses();

    // When the InlineObjectLiterals pass is run
    try {
      compiler.getCompilerPassManager().runPasses(externs, rootScope, null);
      fail("Expected an exception");
    } catch (RuntimeException e) {
      // Then the pass should not inline object literals with exported variables
      // as they are not part of the local scope
      assertEquals(
          "InlineObjectLiterals cannot inline object literals with exported variables",
          e.getMessage());
    }
  }

  private static final class InliningBehavior extends ReferenceCollectingCallback.Behavior {
    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      super.afterExitScope(t, referenceMap);

      for (Var v : t.getScope().getVars()) {
        if (InlineObjectLiterals.this.isVarInlineForbidden(v)) {
          continue;
        }

        ReferenceCollection referenceInfo = referenceMap.getReferences(v);

        if (InlineObjectLiterals.this.isInlinableObject(referenceInfo.references)) {
          // Blacklist the object itself, as well as any other values
          // it refers to, since they will have been moved around.
          staleVars.add(v);

          Reference declaration = referenceInfo.references.get(0);
          Reference init = referenceInfo.getInitializingReference();

          InlineObjectLiterals.this.splitObject(v, declaration, init, referenceInfo);
        }
      }
    }
  }
}