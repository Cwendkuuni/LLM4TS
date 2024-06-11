import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {
  
  private SyntacticScopeCreator scopeCreator;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope_GlobalBlock_ReturnsCorrectScope() {
    // Arrange
    Node rootNode = new Node(Token.SCRIPT);

    // Act
    Scope resultScope = scopeCreator.createScope(rootNode, null);

    // Assert
    assertNull(resultScope.getParent());
    assertEquals(rootNode, resultScope.getRootNode());
  }

  @Test
  public void testCreateScope_FunctionDeclaration_ReturnsCorrectScope() {
    // Arrange
    Node rootNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    // Act
    Scope resultScope = scopeCreator.createScope(rootNode, parent);

    // Assert
    assertNotNull(resultScope.getParent());
    assertEquals(parent, resultScope.getParent().getRootNode());
    assertEquals(rootNode, resultScope.getRootNode());
  }

  @Test
  public void testDeclareVar_UndeclaredVariable_DeclaresVariableInScope() {
    // Arrange
    Scope scope = new Scope(null, new Node(Token.SCRIPT));
    Node nameNode = new Node(Token.NAME);
    String name = "x";

    // Act
    scopeCreator.declareVar(name, nameNode, null, null, null, null);

    // Assert
    assertTrue(scope.isDeclared(name, false));
  }

  @Test
  public void testDeclareVar_DeclaredVariable_ThrowsError() {
    // Arrange
    Scope scope = new Scope(null, new Node(Token.SCRIPT));
    Node nameNode1 = new Node(Token.NAME);
    Node nameNode2 = new Node(Token.NAME);
    String name = "x";

    // Act
    scopeCreator.declareVar(name, nameNode1, null, null, null, null);

    // Assert
    assertThrows(Error.class, () -> scopeCreator.declareVar(name, nameNode2, null, null, null, null));
  }

  @Test
  public void testDeclareVar_ArgumentsVariableInLocalScope_ThrowsError() {
    // Arrange
    Scope scope = new Scope(null, new Node(Token.SCRIPT));
    Node nameNode = new Node(Token.NAME);
    String name = "arguments";

    // Act & Assert
    assertThrows(Error.class, () -> scopeCreator.declareVar(name, nameNode, null, null, null, null));
  }
}