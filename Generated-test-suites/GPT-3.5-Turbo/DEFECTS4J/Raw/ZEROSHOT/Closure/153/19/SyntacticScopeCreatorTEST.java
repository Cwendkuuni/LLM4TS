import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private SyntacticScopeCreator syntacticScopeCreator;

  @Before
  public void setUp() {
    syntacticScopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
  }

  @Test
  public void testCreateScope_function() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.putProp(Node.SOURCENAME_PROP, "test.js");
    Scope parent = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
    Scope scope = syntacticScopeCreator.createScope(functionNode, parent);
    assertEquals("test.js", scope.getSourceName());
  }

  @Test
  public void testCreateScope_globalBlock() {
    Node globalBlockNode = new Node(Token.SCRIPT);
    Scope scope = syntacticScopeCreator.createScope(globalBlockNode, null);
    assertNull(scope.getParent());
  }

  @Test
  public void testScanVars_varDeclaration() {
    Node varNode = new Node(Token.VAR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.NAME);
    child1.setString("x");
    child2.setString("y");
    varNode.addChildToBack(child1);
    varNode.addChildToBack(child2);
    syntacticScopeCreator.scanVars(varNode, null);
    assertTrue(varNode.hasChildren());
  }

  @Test
  public void testScanVars_functionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node child = new Node(Token.NAME);
    child.setString("foo");
    functionNode.addChildToFront(child);
    syntacticScopeCreator.scanVars(functionNode, null);
    assertFalse(functionNode.hasChildren());
  }

  @Test
  public void testScanVars_catchBlock() {
    Node catchNode = new Node(Token.CATCH);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.BLOCK);
    child1.setString("error");
    catchNode.addChildToFront(child1);
    catchNode.addChildToBack(child2);
    syntacticScopeCreator.scanVars(catchNode, null);
    assertFalse(catchNode.hasChildren());
  }
}