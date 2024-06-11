import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.SourceFile;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ScopedAliasesTest {
  @org.junit.Test
  public void testScopedAliases() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    // Create test compiler.
    Compiler compiler = new Compiler();
    SourceFile source = new SourceFile("test.js", sourceCode);

    // Perform initial compilation.
    compiler.compile(source.getCode(), source.getSourceFileName());
    List<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.size());

    // Create test ScopedAliases pass.
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Perform test compilation.
    compiler.process(Collections.emptyList(), source.getCode());
    diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.size());

    // Check results.
    List<Node> rootNodes = compiler.getAllRoots();
    assertEquals(1, rootNodes.size());
    Node rootNode = rootNodes.get(0);
    assertEquals(Node.BLOCK, rootNode.getType());

    // Check for correct aliasing.
    List<Node> aliasDefinitions = Lists.newArrayList();
    for (Node n = rootNode; n != null; n = n.getPrevious()) {
      if (n.getType() == Token.NAME && n.getString().equals("dom")) {
        Node aliasDefinition = n.getParent();
        assertEquals(Node.ASSIGN, aliasDefinition.getType());
        assertEquals(Token.VAR, aliasDefinition.getTypeParent());
        aliasDefinitions.add(aliasDefinition);
        break;
      }
    }

    Node aliasedNode = null;
    for (Node n = rootNode; n != null; n = n.getNext()) {
      if (n.getType() == Token.CALL &&
          n.getFirstChild().isQualifiedName() &&
          n.getFirstChild().getString().equals("goog.dom.createElement")) {
        aliasedNode = n.getLastChild();
        break;
      }
    }

    assertEquals(Node.NAME, aliasedNode.getType());
    assertEquals("DIV", aliasedNode.getString());

    // Check for correct removal of alias definition.
    for (Node aliasDefinition : aliasDefinitions) {
      assertEquals(Node.VAR, aliasDefinition.getTypeParent());
      assertEquals(Token.VAR, aliasDefinition.getType());
      assertEquals(1, aliasDefinition.getChildCount());
      Node aliasName = aliasDefinition.getOnlyChild();
      assertEquals(Node.NAME, aliasName.getType());
      assertEquals("dom", aliasName.getString());
      assertTrue(aliasDefinition.getParent().getChildren().remove(aliasDefinition));
    }

    // Check for correct merging of scopes.
    Node scopeCall = null;
    for (Node n = rootNode; n != null; n = n.getNext()) {
      if (n.getType() == Token.CALL &&
          n.getFirstChild().isQualifiedName() &&
          n.getFirstChild().getString().equals("goog.scope")) {
        scopeCall = n;
        break;
      }
    }

    Node scopeBlock = scopeCall.getLastChild();
    assertEquals(Node.BLOCK, scopeBlock.getType());
    assertEquals(0, scopeBlock.getChildCount());

    assertTrue(NodeUtil.tryMergeBlock(scopeBlock));
    assertEquals(1, scopeBlock.getChildCount());
  }

  @org.junit.Test
  public void testScopedAliasesInvalidCall() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "});" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "});";

    // Create test compiler.
    Compiler compiler = new Compiler();
    SourceFile source = new SourceFile("test.js", sourceCode);

    // Perform initial compilation.
    compiler.compile(source.getCode(), source.getSourceFileName());
    List<Diagnostic> diagnostics = compiler.getDiagnostics();

    // Check for incorrect number of calls to goog.scope.
    assertEquals(1, diagnostics.size());
    Diagnostic diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.ERROR, diagnostic.getSeverity());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType());

    // Create test ScopedAliases pass.
    // (This will not be executed since we have a diagnostic.)
  }

  @org.junit.Test
  public void testScopedAliasesInvalidFunction() {
    String sourceCode =
        "goog.scope(function a() {" +
        "  var dom = goog.dom;" +
        "});" +
        "goog.scope(function b() {" +
        "  var dom = goog.dom;" +
        "});";

    // Create test compiler.
    Compiler compiler = new Compiler();
    SourceFile source = new SourceFile("test.js", sourceCode);

    // Perform initial compilation.
    compiler.compile(source.getCode(), source.getSourceFileName());
    List<Diagnostic> diagnostics = compiler.getDiagnostics();

    // Check for incorrect function syntax in goog.scope.
    assertEquals(1, diagnostics.size());
    Diagnostic diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.ERROR, diagnostic.getSeverity());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", diagnostic.getType());

    // Create test ScopedAliases pass.
    // (This will not be executed since we have a diagnostic.)
  }

  @org.junit.Test
  public void testScopedAliasesThis() {
    String sourceCode =
        "goog.scope(function() {" +
        "  this.x = 1;" +
        "});" +
        "goog.scope(function() {" +
        "  return this.x;" +
        "});";

    // Create test compiler.
    Compiler compiler = new Compiler();
    SourceFile source = new SourceFile("test.js", sourceCode);

    // Perform initial compilation.
    compiler.compile(source.getCode(), source.getSourceFileName());
    List<Diagnostic> diagnostics = compiler.getDiagnostics();

    // Check for "this" reference in goog.scope function.
    assertEquals(1, diagnostics.size());
    Diagnostic diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.ERROR, diagnostic.getSeverity());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", diagnostic.getType());

    // Create test ScopedAliases pass.
    // (This will not be executed since we have a diagnostic.)
  }

  @org.junit.Test
  public void testScopedAliasesReturn() {
    String sourceCode =
        "goog.scope(function() {" +
        "  return 1;" +
        "});";

    // Create test compiler.
    Compiler compiler = new Compiler();
    SourceFile source = new SourceFile("test.js", sourceCode);

    // Perform initial compilation.
    compiler.compile(source.getCode(), source.getSourceFileName());
    List<Diagnostic> diagnostics = compiler.getDiagnostics();

    // Check for return statement in goog.scope function.
    assertEquals(1, diagnostics.size());
    Diagnostic diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.ERROR, diagnostic.getSeverity());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostic.getType());

    // Create test ScopedAliases pass.
    // (This will not be executed since we have a diagnostic.)
  }

  @org.junit.Test
  public void testScopedAliasesThrow() {
    String sourceCode =
        "goog.scope(function() {" +
        "  throw new Error();" +
        "});";

    // Create test compiler.
    Compiler compiler = new Compiler();
    SourceFile source = new SourceFile("test.js", sourceCode);

    // Perform initial compilation.
    compiler.compile(source.getCode(), source.getSourceFileName());
    List<Diagnostic> diagnostics = compiler.getDiagnostics();

    // Check for throw statement in goog.scope function.
    assertEquals(1, diagnostics.size());
    Diagnostic diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.ERROR, diagnostic.getSeverity());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostic.getType());

    // Create test ScopedAliases pass.
    // (This will not be executed since we have a diagnostic.)
  }

  @org.junit.Test
  public void testScopedAliasesAliasRedefined() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var foo = 1;" +
        "  var foo = 2;" +
        "});";

    // Create test compiler.
    Compiler compiler = new Compiler();
    SourceFile source = new SourceFile("test.js", sourceCode);

    // Perform initial compilation.
    compiler.compile(source.getCode(), source.getSourceFileName());
    List<Diagnostic> diagnostics = compiler.getDiagnostics();

    // Check for redefined alias.
    assertEquals(1, diagnostics.size());
    Diagnostic diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.ERROR, diagnostic.getSeverity());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostic.getType());

    // Create test ScopedAliases pass.
    // (This will not be executed since we have a diagnostic.)
  }

  @org.junit.Test
  public void testScopedAliasesNonAliasLocal() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var x = 1;" +
        "});";

    // Create test compiler.
    Compiler compiler = new Compiler();
    SourceFile source = new SourceFile("test.js", sourceCode);

    // Perform initial compilation.
    compiler.compile(source.getCode(), source.getSourceFileName());
    List<Diagnostic> diagnostics = compiler.getDiagnostics();

    // Check for non-alias local variable.
    assertEquals(1, diagnostics.size());
    Diagnostic diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.ERROR, diagnostic.getSeverity());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnostic.getType());

    // Create test ScopedAliases pass.
    // (This will not be executed since we have a diagnostic.)
  }
}