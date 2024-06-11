import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private CompilerOptions options;

  @Mock
  private AliasTransformationHandler transformationHandler;

  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testScopedAliases_Simple() {
    // Test a simple case where a goog.scope block is used correctly.
    Node rootNode = createNode("function test() { var dom = goog.scope(function() { var DIV = dom.TagName.DIV; dom.createElement(DIV); }); }");
    scopedAliases.process(null, rootNode);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliases_BadParameters() {
    // Test a case where the goog.scope call has bad parameters.
    Node rootNode = createNode("function test() { goog.scope(function() {}, function() {}); }");
    scopedAliases.process(null, rootNode);
    assertFalse(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());

    Node rootNodeWithErrors = createNode("function test() { goog.scope(function() {}, function() { return 'foo'; }); }");
    scopedAliases.process(null, rootNodeWithErrors);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliases_ThisReference() {
    // Test a case where the body of a goog.scope function references 'this'.
    Node rootNode = createNode("function test() { goog.scope(function() { this.test = 'foo'; }); }");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType error = compiler.getDiagnostics().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_REFERENCES_THIS, error);
  }

  @Test
  public void testScopedAliases_ReturnStatement() {
    // Test a case where the body of a goog.scope function uses 'return'.
    Node rootNode = createNode("function test() { goog.scope(function() { return 'foo'; }); }");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType error = compiler.getDiagnostics().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_USES_RETURN, error);
  }

  @Test
  public void testScopedAliases_ThrowStatement() {
    // Test a case where the body of a goog.scope function uses 'throw'.
    Node rootNode = createNode("function test() { goog.scope(function() { throw new Error('foo'); }); }");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType error = compiler.getDiagnostics().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_USES_THROW, error);
  }

  @Test
  public void testScopedAliases_AliasRedefined() {
    // Test a case where an alias is redefined.
    Node rootNode = createNode("function test() { goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; var DIV = 'foo'; }); }");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType error = compiler.getDiagnostics().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED, error);
  }

  @Test
  public void testScopedAliases_NonAliasLocal() {
    // Test a case where a non-alias local variable is in a goog.scope.
    Node rootNode = createNode("function test() { goog.scope(function() { var local = 'foo'; }); }");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType error = compiler.getDiagnostics().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL, error);
  }

  private Node createNode(String code) {
    SourcePosition position = new SourcePosition() {};
    Node node = new Node(null, null, null, code, null, null, position, null, null, null, null, null, null);
    Node scriptNode = new Node(null, code, null, null, null, null, null, null, null, null, null, null, null);
    node.setParent(scriptNode);
    return node;
  }
}