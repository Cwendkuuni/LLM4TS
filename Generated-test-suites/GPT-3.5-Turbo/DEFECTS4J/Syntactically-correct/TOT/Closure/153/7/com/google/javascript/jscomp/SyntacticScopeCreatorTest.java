import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

  @Test
  public void createScope_NullNode_ReturnsNullScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(null);

    Scope scope = creator.createScope(null, null);

    assertNull(scope);
  }

  @Test
  public void createScope_GlobalBlock_ReturnsGlobalScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(null);

    Scope scope = creator.createScope(new Node(Token.SCRIPT), null);

    assertNull(scope.getParent());
  }

  @Test
  public void createScope_FunctionDeclaration_ReturnsFunctionScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(null);

    Node fnNameNode = new Node(Token.NAME, "myFunction");
    Node argsNode = new Node(Token.LP);
    Node bodyNode = new Node(Token.BLOCK);

    Node functionNode = new Node(Token.FUNCTION, fnNameNode, argsNode, bodyNode);

    Scope scope = creator.createScope(functionNode, null);

    assertNotNull(scope);
    assertNotNull(scope.getParent());
    assertEquals(scope.getParent().getRootNode(), functionNode);
  }

  @Test
  public void declareVar_NewVariable_DeclaresVariable() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(null);
    Scope scope = new Scope(null, null);

    Node nameNode = new Node(Token.NAME, "myVariable");

    creator.declareVar("myVariable", nameNode, null, null, null, null);

    assertTrue(scope.isDeclared("myVariable", false));
  }

  @Test
  public void declareVar_MultipleDeclaration_ErrorReported() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(null);
    Scope scope = new Scope(null, null);

    Node nameNode1 = new Node(Token.NAME, "myVariable");
    Node nameNode2 = new Node(Token.NAME, "myVariable");

    creator.declareVar("myVariable", nameNode1, null, null, null, null);
    creator.declareVar("myVariable", nameNode2, null, null, null, null);

    // TODO: assert error message
  }

  @Test
  public void declareVar_ArgumentsVariableAndNotVarDeclaration_ErrorReported() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(null);
    Scope scope = new Scope(null, null);

    Node nameNode = new Node(Token.NAME, "arguments");

    creator.declareVar("arguments", nameNode, null, null, null, null);

    // TODO: assert error message
  }

}