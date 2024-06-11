import static com.google.javascript.jscomp.DiagnosticType.*;
import static com.google.javascript.jscomp.JSCodingConventions.GOOG_SCOPE_USED_IMPROPERLY;
import static com.google.javascript.jscomp.JSCodingConventions.GOOG_SCOPE_HAS_BAD_PARAMETERS;
import static com.google.javascript.jscomp.JSCodingConventions.GOOG_SCOPE_ALIAS_REDEFINED;
import static com.google.javascript.jscomp.JSCodingConventions.GOOG_SCOPE_NON_ALIAS_LOCAL;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler(compiler, preprocessorSymbolTable);
  }

  @Test
  public void testSimpleAlias() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    Node rootNode = NodePrinter.parseCompilationUnit(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that the alias has been applied correctly
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getString());

    // Check the alias is used correctly
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      Node aliasReference = ((AliasedNode) aliasUsage).aliasReference;
      assertEquals("DOM", aliasReference.getString());
      assertEquals("goog.dom", aliasDefinition.getString());
    }

    // Check no errors have been reported
    assertTrue(compiler.getNumDiagnostics() == 0);
  }

  @Test
  public void testImproperUsage() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    dom.createElement(DIV);" +
        "  });" +
        "});";

    Node rootNode = NodePrinter.parseCompilationUnit(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that an error has been reported for improper usage
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    // Check the error message
    DiagnosticType errorType = scopedAliases.getErrorForNode(scopeCall);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errorType);

    // Check an error has been reported
    assertTrue(compiler.getNumDiagnostics() > 0);
  }

  @Test
  public void testMultipleAliases() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV_ONE = dom.TagName.DIV;" +
        "  var DIV_TWO = dom.TagName.DIV;" +
        "  dom.createElement(DIV_ONE);" +
        "  dom.createElement(DIV_TWO);" +
        "});";

    Node rootNode = NodePrinter.parseCompilationUnit(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that the aliases have been applied correctly
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertArrayEquals(
        new String[]{"goog.dom", "goog.dom"},
        new String[2][],
        new String[]{"aliasDefinition.getString()", "aliasDefinition.getString()"}
    );

    // Check the aliases are used correctly
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    AliasUsage aliasUsageOne = aliasUsages.get(0);
    AliasUsage aliasUsageTwo = aliasUsages.get(1);
    if (aliasUsageOne instanceof AliasedNode) {
      Node aliasReferenceOne = ((AliasedNode) aliasUsageOne).aliasReference;
      assertEquals("DIV_ONE", aliasReferenceOne.getString());
      assertEquals("goog.dom", aliasDefinitionInOrder.get(0).getString());
    }

    if (aliasUsageTwo instanceof AliasedNode) {
      Node aliasReferenceTwo = ((AliasedNode) aliasUsageTwo).aliasReference;
      assertEquals("DIV_TWO", aliasReferenceTwo.getString());
      assertEquals("goog.dom", aliasDefinitionInOrder.get(0).getString());
    }

    // Check no errors have been reported
    assertTrue(compiler.getNumDiagnostics() == 0);
  }

  @Test
  public void testBadParameters() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var foo = function() {" +
        "  };" +
        "  goog.scope(function() {" +
        "    foo();" +
        "  });" +
        "});";

    Node rootNode = NodePrinter.parseCompilationUnit(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that an error has been reported for bad parameters
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    // Check the error message
    DiagnosticType errorType = scopedAliases.getErrorForNode(scopeCall);
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, errorType);

    // Check an error has been reported
    assertTrue(compiler.getNumDiagnostics() > 0);
  }

  @Test
  public void testRefersThis() {
    String sourceCode =
        "goog.scope(function() {" +
        "  this.foo = 1;" +
        "});";

    Node rootNode = NodePrinter.parseCompilationUnit(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that an error has been reported for referring to 'this'
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    // Check the error message
    DiagnosticType errorType = scopedAliases.getErrorForNode(scopeCall);
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errorType);

    // Check an error has been reported
    assertTrue(compiler.getNumDiagnostics() > 0);
  }

  @Test
  public void testUsesReturn() {
    String sourceCode =
        "goog.scope(function() {" +
        "  return goog.dom.createElement('div');" +
        "});";

    Node rootNode = NodePrinter.parseCompilationUnit(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that an error has been reported for using 'return'
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    // Check the error message
    DiagnosticType errorType = scopedAliases.getErrorForNode(scopeCall);
    assertEquals(GOOG_SCOPE_USES_RETURN, errorType);

    // Check an error has been reported
    assertTrue(compiler.getNumDiagnostics() > 0);
  }

  @Test
  public void testUsesThrow() {
    String sourceCode =
        "goog.scope(function() {" +
        "  throw new Error('Test error');" +
        "});";

    Node rootNode = NodePrinter.parseCompilationUnit(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that an error has been reported for using 'throw'
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    // Check the error message
    DiagnosticType errorType = scopedAliases.getErrorForNode(scopeCall);
    assertEquals(GOOG_SCOPE_USES_THROW, errorType);

    // Check an error has been reported
    assertTrue(compiler.getNumDiagnostics() > 0);
  }

  @Test
  public void testRedefinedAlias() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var goog = {" +
        "    dom: {" +
        "      TagName: {" +
        "        DIV: {'value': 'DIV'}" +
        "      }" +
        "    }" +
        "  }" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var DIV = 1;" +
        "});";

    Node rootNode = NodePrinter.parseCompilationUnit(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that an error has been reported for redefining an alias
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getString());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasUsages.size());

    AliasUsage aliasUsageOne = aliasUsages.get(0);
    if (aliasUsageOne instanceof AliasedNode) {
      Node aliasReferenceOne = ((AliasedNode) aliasUsageOne).aliasReference;
      assertEquals("DIV", aliasReferenceOne.getString());
      assertEquals("goog.dom", aliasDefinition.getString());
    }

    AliasUsage aliasUsageTwo = aliasUsages.get(1);
    if (aliasUsageTwo instanceof AliasedNode) {
      Node aliasReferenceTwo = ((AliasedNode) aliasUsageTwo).aliasReference;
      assertNull(aliasReferenceTwo);
      assertNotNull(aliasDefinition);
      assertEquals("DIV", aliasDefinition.getString());

      // Check the error message
      DiagnosticType errorType = scopedAliases.getErrorForNode(aliasDefinition);
      assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errorType);
    }

    // Check an error has been reported
    assertTrue(compiler.getNumDiagnostics() > 0);
  }

  @Test
  public void testNonAliasLocal() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var foo = 1;" +
        "});";

    Node rootNode = NodePrinter.parseCompilationUnit(sourceCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that an error has been reported for a non-alias local
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    // Check the error message
    DiagnosticType errorType = scopedAliases.getErrorForNode(scopeCall);
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, errorType);

    // Check an error has been reported
    assertTrue(compiler.getNumDiagnostics() > 0);
  }

  private DiagnosticType getErrorForNode(ScopedAliases scopedAliases, Node node) {
    return scopedAliases.getErrorForNode(node);
  }
}