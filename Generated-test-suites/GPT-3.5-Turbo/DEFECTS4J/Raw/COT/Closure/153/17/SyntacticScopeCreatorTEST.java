import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

  private SyntacticScopeCreator scopeCreator;

  @Before
  public void setUp() {
    scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
  }

  @Test
  public void createScope_withNullNode_returnsNull() {
    Scope scope = scopeCreator.createScope(null, null);
    assertNull(scope);
  }

  @Test
  public void createScope_withValidNode_createsScope() {
    Node node = new Node(Token.ROOT);
    Scope scope = scopeCreator.createScope(node, null);
    assertNotNull(scope);
  }

  @Test
  public void createScope_withValidParent_createsNestedScope() {
    Node parent = new Node(Token.ROOT);
    Node child = new Node(Token.FUNCTION);

    parent.addChildToFront(child);

    Scope parentScope = scopeCreator.createScope(parent, null);
    Scope childScope = scopeCreator.createScope(child, parentScope);

    assertNotNull(childScope);
    assertSame(parentScope, childScope.getParent());
  }

  @Test
  public void createScope_withFunctionNode_addsFunctionNameToScope() {
    Node node = new Node(Token.FUNCTION);
    Node functionNameNode = new Node(Token.NAME, "myFunction");

    node.addChildToFront(functionNameNode);

    Scope scope = scopeCreator.createScope(node, null);
    assertNotNull(scope.getVar("myFunction"));
  }

  @Test
  public void createScope_withArgumentsVariable_addsArgumentsToScope() {
    Node node = new Node(Token.FUNCTION);
    Node functionNameNode = new Node(Token.NAME, "");
    Node argumentsNode = new Node(Token.LP);
    Node bodyNode = new Node(Token.BLOCK);

    functionNameNode.setString("myFunction");
    node.addChildToFront(functionNameNode);
    functionNameNode.addChildToBack(argumentsNode);
    functionNameNode.addChildToBack(bodyNode);

    Scope scope = scopeCreator.createScope(node, null);
    assertNotNull(scope.getVar("arguments"));
  }

  @Test
  public void createScope_withVarDeclaration_addsVariablesToScope() {
    Node node = new Node(Token.VAR);
    Node variable1 = new Node(Token.NAME, "x");
    Node variable2 = new Node(Token.NAME, "y");

    node.addChildToBack(variable1);
    node.addChildToBack(variable2);

    Scope scope = scopeCreator.createScope(node, null);
    assertNotNull(scope.getVar("x"));
    assertNotNull(scope.getVar("y"));
  }

  @Test
  public void createScope_withCatchBlock_addsCatchVariableToScope() {
    Node node = new Node(Token.CATCH);
    Node catchVar = new Node(Token.NAME, "err");
    Node catchBody = new Node(Token.BLOCK);

    node.addChildToFront(catchVar);
    node.addChildToFront(catchBody);

    Scope scope = scopeCreator.createScope(node, null);
    assertNotNull(scope.getVar("err"));
  }

  @Test
  public void createScope_withScriptNode_setsSourceName() {
    Node node = new Node(Token.SCRIPT);
    node.putProp(Node.SOURCENAME_PROP, "source.js");

    Scope scope = scopeCreator.createScope(node, null);
    assertEquals("source.js", scope.getScopeNode().getStringProp(Node.SOURCENAME_PROP));
  }
}