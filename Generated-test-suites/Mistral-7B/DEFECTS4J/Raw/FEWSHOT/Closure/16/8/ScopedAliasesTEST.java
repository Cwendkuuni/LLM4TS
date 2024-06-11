package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Util;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.scope(function() {" +
        "    var y = goog.y;" +
        "  });" +
        "  var z = goog.z;" +
        "});";

    Node root = Util.parseJs(sourceCode, null);

    scopedAliases.process(null, root);

    Node scopeCall = Util.findSingleChildOfType(root, Node.CALL);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node anonymousFunction = scopeCall.getChildAtIndex(1);
    assertEquals(1, anonymousFunction.getChildCount());

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(3, aliasUsages.size());
    assertEquals("goog.x", aliasUsages.get(0).getParent().getString());
    assertEquals("goog.y", aliasUsages.get(1).getParent().getString());
    assertEquals("goog.z", aliasUsages.get(2).getParent().getString());

    List<Node> aliasDefinitions = Util.getDescendantsOfType(root, Node.VAR);
    assertEquals(3, aliasDefinitions.size());

    Var x = aliasDefinitions.get(0).getParent();
    assertEquals("x", x.getName());
    assertFalse(x.isBleedingFunction());
    assertNull(x.getInitialValue());

    Var y = aliasDefinitions.get(1).getParent();
    assertEquals("y", y.getName());
    assertFalse(y.isBleedingFunction());
    assertNull(y.getInitialValue());

    Var z = aliasDefinitions.get(2).getParent();
    assertEquals("z", z.getName());
    assertFalse(z.isBleedingFunction());
    assertNull(z.getInitialValue());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(scopeCall, scopeCalls.get(0));

    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticCategory.WARNING, diagnostic.getCategory());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  var y = goog.y;" +
        "  var z = goog.z;" +
        "  goog.scope(function() {" +
        "    var x = 1;" +
        "  });" +
        "});";

    Node root = Util.parseJs(sourceCode, null);

    scopedAliases.process(null, root);

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(3, aliasUsages.size());
    assertEquals("goog.x", aliasUsages.get(0).getParent().getString());
    assertEquals("goog.y", aliasUsages.get(1).getParent().getString());
    assertEquals("goog.z", aliasUsages.get(2).getParent().getString());

    List<Node> aliasDefinitions = Util.getDescendantsOfType(root, Node.VAR);
    assertEquals(3, aliasDefinitions.size());

    Var x = aliasDefinitions.get(0).getParent();
    assertEquals("x", x.getName());
    assertFalse(x.isBleedingFunction());
    assertNull(x.getInitialValue());

    Var y = aliasDefinitions.get(1).getParent();
    assertEquals("y", y.getName());
    assertFalse(y.isBleedingFunction());
    assertNull(y.getInitialValue());

    Var z = aliasDefinitions.get(2).getParent();
    assertEquals("z", z.getName());
    assertFalse(z.isBleedingFunction());
    assertNull(z.getInitialValue());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root, scopeCalls.get(0));

    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(3, diagnostics.size());
    DiagnosticType diagnostic1 = diagnostics.get(0);
    DiagnosticType diagnostic2 = diagnostics.get(1);
    DiagnosticType diagnostic3 = diagnostics.get(2);
    assertEquals(DiagnosticCategory.ERROR, diagnostic1.getCategory());
    assertEquals(DiagnosticCategory.ERROR, diagnostic2.getCategory());
    assertEquals(DiagnosticCategory.ERROR, diagnostic3.getCategory());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostic1.getType());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, diagnostic2.getType());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostic3.getType());
  }
}