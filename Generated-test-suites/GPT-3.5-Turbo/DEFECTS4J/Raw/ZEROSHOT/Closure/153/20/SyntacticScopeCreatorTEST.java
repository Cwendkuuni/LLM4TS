import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private SyntacticScopeCreator scopeCreator;

  @Before
  public void setUp() {
    scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
  }

  @Test
  public void testCreateScope_functionDeclaration_scopeCreated() {
    Node n = new Node(Token.FUNCTION);
    Scope parent = null;
    Scope scope = scopeCreator.createScope(n, parent);

    assertNotNull(scope);
  }

  @Test
  public void testCreateScope_globalBlock_scopeCreated() {
    Node n = new Node(Token.SCRIPT);
    Scope parent = null;
    Scope scope = scopeCreator.createScope(n, parent);

    assertNotNull(scope);
  }

  @Test
  public void testScanVars_varDeclaration_variableDeclared() {
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;

    scopeCreator.scanVars(n, parent);

    assertNotNull(scopeCreator.getScope().getVar("x"));
  }

  @Test
  public void testScanVars_functionDeclaration_variableDeclared() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;

    scopeCreator.scanVars(n, parent);

    assertNotNull(scopeCreator.getScope().getVar("fnName"));
  }

  @Test
  public void testScanVars_catchBlock_variableDeclared() {
    Node n = new Node(Token.CATCH);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;

    scopeCreator.scanVars(n, parent);

    assertNotNull(scopeCreator.getScope().getVar("var"));
  }

  @Test
  public void testScanVars_controlStructure_variableDeclared() {
    Node n = new Node(Token.IF);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;

    scopeCreator.scanVars(n, parent);

    assertNotNull(scopeCreator.getScope().getVar("x"));
  }

  @Test
  public void testScanVars_statementBlock_variableDeclared() {
    Node n = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;

    scopeCreator.scanVars(n, parent);

    assertNotNull(scopeCreator.getScope().getVar("x"));
  }

  @Test
  public void testDeclareVar_variableNotDeclaredNameDeclared() {
    String name = "var";
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME);

    scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

    assertNotNull(scopeCreator.getScope().getVar(name));
  }

  @Test
  public void testDeclareVar_variableDeclaredMultipleTimes_errorReported() {
    String name = "x";
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME);
    scopeCreator.getScope().declare(name, n, declaredType, new Input());

    scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

    assertTrue(scopeCreator.getCompiler().hasErrors());
  }

  @Test
  public void testDeclareVar_variableDeclaredWithArguments_errorReported() {
    String name = "arguments";
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    Node gramps = null;
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME);

    scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

    assertTrue(scopeCreator.getCompiler().hasErrors());
  }
}