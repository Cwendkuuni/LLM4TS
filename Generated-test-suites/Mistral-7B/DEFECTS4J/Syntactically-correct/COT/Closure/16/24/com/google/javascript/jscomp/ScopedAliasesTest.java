import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {};
  }

  @Test
  public void testScopedAliases_NoErrors() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = NodeFactory.createRoot();
    Node externs = NodeFactory.createExterns();
    scopedAliases.process(externs, root);
    assertTrue(scopedAliases.hasErrors() == false);
  }

  @Test
  public void testScopedAliases_ProperlyAppliesAliases() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = NodeFactory.createRoot();
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";
    Node externs = NodeFactory.fromString(sourceCode);
    scopedAliases.process(externs, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom.createElement", aliasDefinitionsInOrder.get(0).getString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());

    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertEquals(1, anonymousFnNode.getChildCount());
    assertEquals("function() {}", anonymousFnNode.getString());

    Node rootWithAliases = root.getChildAtIndex(0);
    Node dom = rootWithAliases.getFirstChild();
    Node domTagName = dom.getFirstChild();
    Node divElement = dom.getChildAtIndex(1);

    assertEquals("goog.dom.createElement", divElement.getParent().getLastChild().getString());
  }

  @Test
  public void testScopedAliases_ImproperUsage() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "});";
    Node externs = NodeFactory.fromString(sourceCode);

    scopedAliases.process(externs, null);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(2, scopeCalls.size());

    Node firstScopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", firstScopeCall.getFirstChild().getString());
    assertEquals(2, firstScopeCall.getChildCount());

    Node secondScopeCall = scopeCalls.get(1);
    assertEquals("goog.scope", secondScopeCall.getFirstChild().getString());
    assertEquals(2, secondScopeCall.getChildCount());

    List<DiagnosticType> errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));
  }

  @Test
  public void testScopedAliases_BadParameters() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    String sourceCode = "goog.scope(function(a, b) {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";
    Node externs = NodeFactory.fromString(sourceCode);

    scopedAliases.process(externs, null);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());

    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertEquals(2, anonymousFnNode.getChildCount());
    Node functionNameNode = anonymousFnNode.getChildAtIndex(0);
    assertEquals("function", functionNameNode.getType());
    assertEquals("(a, b)", functionNameNode.getString());

    List<DiagnosticType> errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, errors.get(0));
  }

  @Test
  public void testScopedAliases_RefersToThis() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    String sourceCode = "goog.scope(function() {" +
        "  var self = this;" +
        "  goog.dom.createElement(DIV);" +
        "});";
    Node externs = NodeFactory.fromString(sourceCode);

    scopedAliases.process(externs, null);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());

    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertEquals(1, anonymousFnNode.getChildCount());
    Node bodyNode = anonymousFnNode.getChildAtIndex(0);

    List<DiagnosticType> errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errors.get(0));
  }

  @Test
  public void testScopedAliases_UsesReturn() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    String sourceCode = "goog.scope(function() {" +
        "  function foo() {" +
        "    return goog.dom.createElement(DIV);" +
        "  }" +
        "  goog.dom.createElement(DIV);" +
        "});";
    Node externs = NodeFactory.fromString(sourceCode);

    scopedAliases.process(externs, null);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());

    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertEquals(1, anonymousFnNode.getChildCount());
    Node functionNode = anonymousFnNode.getChildAtIndex(0);
    assertEquals("function foo", functionNode.getString());

    List<DiagnosticType> errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, errors.get(0));
  }

  @Test
  public void testScopedAliases_UsesThrow() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    String sourceCode = "goog.scope(function() {" +
        "  function foo() {" +
        "    throw new Error('Something went wrong');" +
        "  }" +
        "  goog.dom.createElement(DIV);" +
        "});";
    Node externs = NodeFactory.fromString(sourceCode);

    scopedAliases.process(externs, null);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());

    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertEquals(1, anonymousFnNode.getChildCount());
    Node functionNode = anonymousFnNode.getChildAtIndex(0);
    assertEquals("function foo", functionNode.getString());

    List<DiagnosticType> errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_THROW, errors.get(0));
  }

  @Test
  public void testScopedAliases_AliasRedefined() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var div = DIV;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";
    Node externs = NodeFactory.fromString(sourceCode);

    scopedAliases.process(externs, null);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    List<DiagnosticType> errors = compiler.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0));
  }
}