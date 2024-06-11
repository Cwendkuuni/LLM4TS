import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_REFERENCES_THIS;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USES_RETURN;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USES_THROW;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ScopedAliasesTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_NoScopeCalls() {
    Node root = Node.newText("function foo() {}");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testProcess_OneScopeCall_Valid() {
    Node root = Node.newText("(function() {\n" +
        "  var x = goog.x;\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testProcess_OneScopeCall_Invalid_ImproperlyUsed() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var x = 0;\n" +
        "  });\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0).getType());
  }

  @Test
  public void testProcess_OneScopeCall_Invalid_BadParameters() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    return function() {};\n" +
        "  });\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(0).getType());
  }

  @Test
  public void testProcess_OneScopeCall_Invalid_RefersToThis() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var self = this;\n" +
        "  });\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(0).getType());
  }

  @Test
  public void testProcess_OneScopeCall_Invalid_UsesReturn() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    return 'foo';\n" +
        "  });\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(0).getType());
  }

  @Test
  public void testProcess_OneScopeCall_Invalid_UsesThrow() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    throw new Error('foo');\n" +
        "  });\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostics.get(0).getType());
  }

  @Test
  public void testProcess_MultipleScopeCalls() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var x = goog.x;\n" +
        "  });\n" +
        "  goog.scope(function() {\n" +
        "    var y = goog.y;\n" +
        "  });\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testProcess_AliasRedefined() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var x = goog.x;\n" +
        "    var x = 0;\n" +
        "  });\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(0).getType());
  }

  @Test
  public void testProcess_NonAliasLocal() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var x = 0;\n" +
        "  });\n" +
        "  var x = goog.x;\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    scopedAliases.process(null, root);

    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, diagnostics.get(0).getType());
  }

  @Test
  public void testHotSwapScript() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var x = goog.x;\n" +
        "  });\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.hotSwapScript(root, null);

    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testHotSwapScript_WithErrors() {
    Node root = Node.newText("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    return function() {};\n" +
        "  });\n" +
        "})()");

    Diagnostics diagnostics = new Diagnostics();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.hotSwapScript(root, null);

    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(0).getType());
  }
}