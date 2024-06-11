import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  @InjectMocks
  private ScopedAliases scopedAliases;

  private Node rootNode;

  @Before
  public void setup() {
    rootNode = new Node();
  }

  @Test
  public void testProcess_EmptyExternsAndRoot() {
    scopedAliases.process(null, rootNode);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_NoGoogScopeCalls() {
    Node externs = new Node();
    rootNode.setString("function main() { }");
    scopedAliases.process(externs, rootNode);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_ValidGoogScopeCall() {
    Node googScopeCall = new Node("goog.scope(function() {});");
    rootNode.setString("main();" + googScopeCall.toString());
    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());

    assertNotNull(aliasDefinitionsInOrder.get(0).getParent());
    assertNotNull(scopeCalls.get(0).getParent());

    assertEquals(googScopeCall.getLastChild(),
        aliasDefinitionsInOrder.get(0));

    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_InvalidGoogScopeCall_ImproperUsage() {
    Node googScopeCall = new Node("goog.scope(function() { return 42; });");
    rootNode.setString("main();" + googScopeCall.toString());
    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType error =
        scopedAliases.getErrors().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_USES_RETURN, error);
  }

  @Test
  public void testProcess_InvalidGoogScopeCall_BadParameters() {
    Node googScopeCall = new Node("goog.scope(function() {}, 1);");
    rootNode.setString("main();" + googScopeCall.toString());
    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType error =
        scopedAliases.getErrors().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS, error);
  }

  @Test
  public void testProcess_InvalidGoogScopeCall_RefersToThis() {
    Node googScopeCall = new Node("goog.scope(function() { this.x = 42; });");
    rootNode.setString("main();" + googScopeCall.toString());
    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType error =
        scopedAliases.getErrors().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_REFERENCES_THIS, error);
  }

  @Test
  public void testProcess_InvalidGoogScopeCall_ThrowsException() {
    Node googScopeCall = new Node("goog.scope(function() { throw new Error(); });");
    rootNode.setString("main();" + googScopeCall.toString());
    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType error =
        scopedAliases.getErrors().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_USES_THROW, error);
  }

  @Test
  public void testProcess_InvalidGoogScopeCall_AliasRedefined() {
    Node externs = new Node();
    Node googScopeCall = new Node("goog.scope(function() { var x = 42; })");
    rootNode.setString("main();" + googScopeCall.toString());
    scopedAliases.process(externs, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("x", aliasDefinitionsInOrder.get(0).getString());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node aliasReference = new Node("x");

    scopedAliases.process(externs, rootNode);

    List<Node> newAliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();

    assertEquals(2, newAliasDefinitionsInOrder.size());
    String newAliasName = newAliasDefinitionsInOrder.get(1).getString();

    assertTrue(newAliasName.contains("_"));

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType error =
        scopedAliases.getErrors().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED, error);
  }

  @Test
  public void testProcess_InvalidGoogScopeCall_NonAliasLocal() {
    Node externs = new Node();
    Node googScopeCall = new Node("goog.scope(function() { var x = 42; var y = y; })");
    rootNode.setString("main();" + googScopeCall.toString());
    scopedAliases.process(externs, rootNode);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrors().size());

    DiagnosticType error =
        scopedAliases.getErrors().get(0).getDiagnosticType();
    assertEquals(ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL, error);
  }
}