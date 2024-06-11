package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class ScopedAliasesTest {

  private final AbstractCompiler compiler;
  private final AliasTransformationHandler transformationHandler;
  private final CompilerOptions options;

  public ScopedAliasesTest(AbstractCompiler compiler,
      AliasTransformationHandler transformationHandler,
      CompilerOptions options) {
    this.compiler = compiler;
    this.transformationHandler = transformationHandler;
    this.options = options;
  }

  @Parameterized.Parameters
  public static List<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {null, null, null},
        {new AbstractCompiler() {}, new StubAliasTransformationHandler(), null},
        {new AbstractCompiler() {}, new StubAliasTransformationHandler(),
            new CompilerOptions()}
    });
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    // Test no errors when no goog.scope blocks are present.
    Node rootNode = getTestNode("testScopedAliasesNoErrors.js");
    scopedAliases.process(null, rootNode);
    assertTrue(compiler.hasNoDiagnostics());

    // Test errors when goog.scope blocks are used improperly.
    Node improperScopeNode = getTestNode("testScopedAliasesImproperUse.js");
    scopedAliases.process(null, improperScopeNode);
    List<DiagnosticType> errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", errors.get(0).getTypeName());

    // Test errors when goog.scope blocks have bad parameters.
    Node badParametersNode = getTestNode("testScopedAliasesBadParameters.js");
    scopedAliases.process(null, badParametersNode);
    errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", errors.get(0).getTypeName());

    // Test errors when goog.scope bodies reference 'this'.
    Node referencesThisNode = getTestNode("testScopedAliasesReferencesThis.js");
    scopedAliases.process(null, referencesThisNode);
    errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", errors.get(0).getTypeName());

    // Test errors when goog.scope bodies use 'return'.
    Node usesReturnNode = getTestNode("testScopedAliasesUsesReturn.js");
    scopedAliases.process(null, usesReturnNode);
    errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", errors.get(0).getTypeName());

    // Test errors when goog.scope bodies use 'throw'.
    Node usesThrowNode = getTestNode("testScopedAliasesUsesThrow.js");
    scopedAliases.process(null, usesThrowNode);
    errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", errors.get(0).getTypeName());

    // Test alias redefinition errors.
    Node redefinedAliasNode = getTestNode("testScopedAliasesAliasRedefined.js");
    scopedAliases.process(null, redefinedAliasNode);
    errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", errors.get(0).getTypeName());

    // Test non-alias local variable errors.
    Node nonAliasLocalNode = getTestNode("testScopedAliasesNonAliasLocal.js");
    scopedAliases.process(null, nonAliasLocalNode);
    errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", errors.get(0).getTypeName());

    // Test alias substitution.
    Node aliasSubstitutionNode = getTestNode("testScopedAliasesAliasSubstitution.js");
    options.setAliasTransform(AliasTransformation.STRICT);
    scopedAliases.process(null, aliasSubstitutionNode);
    Node root = compiler.getEmitRoot();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        root.getChild(0).toString());
  }

  private Node getTestNode(String fileName) {
    return NodeUtil.parseCompilationUnit(
        getClass().getResource(fileName).getFile());
  }

  private static class StubAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public AliasTransformation logAliasTransformation(String fileName,
        SourcePosition position) {
      return new AliasTransformation();
    }
  }
}