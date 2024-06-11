import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

  private SyntacticScopeCreator scopeCreator;

  @Before
  public void setUp() {
    scopeCreator = new SyntacticScopeCreator();
  }

  @Test
  public void testCreateScope_withFunctionNode_shouldCreateScope() {
    Node functionNode = new Node(Token.FUNCTION);
    Scope parentScope = new Scope();
    Scope scope = scopeCreator.createScope(functionNode, parentScope);
    assertNotNull(scope);
  }

  @Test
  public void testCreateScope_withGlobalBlockNode_shouldCreateScope() {
    Node globalBlockNode = new Node(Token.SCRIPT);
    Scope scope = scopeCreator.createScope(globalBlockNode, null);
    assertNotNull(scope);
  }

  @Test
  public void testCreateScope_withInvalidNodeType_shouldReturnNull() {
    Node invalidNode = new Node(Token.NAME);
    Scope scope = scopeCreator.createScope(invalidNode, null);
    assertNull(scope);
  }

  @Test
  public void testDeclareVar_withNewVariable_shouldAddVariableToScope() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    Node gramps = new Node(Token.SCRIPT);
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME);

    scopeCreator.declareVar("varName", node, parent, gramps, declaredType, nodeWithLineNumber);

    assertTrue(scopeCreator.getScope().isDeclared("varName", false));
  }

  @Test
  public void testDeclareVar_withRedeclaredVariableAtTopLevelScope_shouldReportError() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME);

    scopeCreator.getScope().declare("varName", node, declaredType, "sourceName");

    scopeCreator.declareVar("varName", node, parent, gramps, declaredType, nodeWithLineNumber);

    // Check that error was reported
    assertNotNull(compiler.getError());

  }

  @Test
  public void testDeclareVar_withRedeclaredVariableInCatch_shouldReportNoError() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.CATCH);
    parent.addChildToBack(new Node(Token.NAME));
    Node body = new Node(Token.BLOCK);
    Node gramps = new Node(Token.SCRIPT);
    gramps.addChildToBack(parent);
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME);

    scopeCreator.getScope().declare("varName", node, declaredType, "sourceName");

    scopeCreator.declareVar("varName", node, parent, gramps, declaredType, nodeWithLineNumber);

    // Check that no error was reported
    assertNull(compiler.getError());

  }
}