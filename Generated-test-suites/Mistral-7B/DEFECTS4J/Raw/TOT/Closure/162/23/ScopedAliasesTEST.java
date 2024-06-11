import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScopedAliasesTest {

  private static Compiler compiler;
  private static CompilerOptions options;
  private static Diagnostics diagnostics;
  private static Node rootNode;

  @BeforeClass
  public static void setup() {
    compiler = new Compiler();
    options = new CompilerOptions();
    diagnostics = new Diagnostics();
    compiler.setOptionsForTest(options, diagnostics);
    rootNode = TestUtil.parseStringToRoot("testCases/ScopedAliasesTestCases.js");
  }

  @AfterClass
  public static void teardown() {
    compiler = null;
    options = null;
    diagnostics = null;
    rootNode = null;
  }

  @Test
  public void testValidScopedAliases() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    assertEquals("goog.dom.createElement", rootNode.toString());
  }

  @Test
  public void testInvalidScopedAliasesNoFunction() {
    rootNode.getChildAtIndex(0).setString("var x = goog.scope");
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    diagnostics.clear();
    scopedAliases.process(null, rootNode);
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.compile(rootNode, null, null, null);
    });
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY",
        diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesMultipleParameters() {
    rootNode.getChildAtIndex(0).setString("var x = goog.scope(1, 2)");
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    diagnostics.clear();
    scopedAliases.process(null, rootNode);
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.compile(rootNode, null, null, null);
    });
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesFunctionHasName() {
    rootNode.getChildAtIndex(0).getLastChild().setString("myFunction");
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    diagnostics.clear();
    scopedAliases.process(null, rootNode);
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.compile(rootNode, null, null, null);
    });
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesFunctionHasParameters() {
    rootNode.getChildAtIndex(0).getLastChild().setType(Token.FUNCTION);
    rootNode.getChildAtIndex(0).getLastChild().setHasParameters(true);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    diagnostics.clear();
    scopedAliases.process(null, rootNode);
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.compile(rootNode, null, null, null);
    });
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesFunctionUsesReturn() {
    rootNode.getChildAtIndex(0).getLastChild().setType(Token.FUNCTION);
    rootNode.getChildAtIndex(0).getLastChild().setHasParameters(false);
    rootNode.getChildAtIndex(0).getLastChild().getLastChild()
        .setType(Token.RETURN);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    diagnostics.clear();
    scopedAliases.process(null, rootNode);
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.compile(rootNode, null, null, null);
    });
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN",
        diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesFunctionUsesThrow() {
    rootNode.getChildAtIndex(0).getLastChild().setType(Token.FUNCTION);
    rootNode.getChildAtIndex(0).getLastChild().setHasParameters(false);
    Node throwNode = new Node(Token.THROW);
    rootNode.getChildAtIndex(0).getLastChild().getLastChild()
        .addChild(throwNode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    diagnostics.clear();
    scopedAliases.process(null, rootNode);
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.compile(rootNode, null, null, null);
    });
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW",
        diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesAliasRedefined() {
    rootNode.setChildAtIndex(0,
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "x")));
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    diagnostics.clear();
    scopedAliases.process(null, rootNode);
    assertThrows(IllegalStateException.class, () -> {
      compiler.compile(rootNode, null, null, null);
    });
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesNonAliasLocal() {
    rootNode.setChildAtIndex(0, new Node(Token.VAR, new Node(Token.NAME, "x")));
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    diagnostics.clear();
    scopedAliases.process(null, rootNode);
    assertThrows(IllegalStateException.class, () -> {
      compiler.compile(rootNode, null, null, null);
    });
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testValidScopedAliasesWithAliasTransformation() {
    options.setAliasTransformation(AliasTransformation.ALWAYS);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    Node transformedRoot = compiler.compile(rootNode, null, null, null).getTree();
    assertEquals("goog.dom.createElement", transformedRoot.toString());
  }

  @Test
  public void testValidScopedAliasesWithAliasTransformationAndHotSwap() {
    options.setAliasTransformation(AliasTransformation.ALWAYS);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    compiler.compile(rootNode, null, null, null);

    rootNode.getChildAtIndex(0).setString("goog.scope(function() { var x = goog.dom; })");
    scopedAliases.process(null, rootNode);
    compiler.reportCodeChange();

    Node transformedRoot = compiler.compile(rootNode, null, null, null).getTree();
    assertEquals("goog.dom.createElement", transformedRoot.toString());
  }
}