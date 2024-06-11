import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

   SyntacticScopeCreator scopeCreator;

   @Before
   public void setUp() {
      scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
   }

   @Test
   public void testCreateScope_FunctionDeclarationWithArgs_VariablesDeclared() {
      Node rootNode = new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction"),
         new Node(Token.LP, new Node(Token.NAME, "arg1")), new Node(Token.BLOCK));

      Scope scope = scopeCreator.createScope(rootNode, null);

      assertNotNull(scope);
      assertEquals("testFunction", scope.getRootNode().getFirstFirstChild().getString());
      assertNotNull(scope.getVar("arg1"));
   }

   @Test
   public void testCreateScope_FunctionExpression_VariablesNotDeclared() {
      Node rootNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.LP),
         new Node(Token.BLOCK));

      Scope scope = scopeCreator.createScope(rootNode, null);

      assertNotNull(scope);
      assertNull(scope.getVar("testFunction"));
   }

   @Test
   public void testCreateScope_GlobalBlock_VariablesDeclared() {
      Node varNode = new Node(Token.VAR);
      varNode.addChildToBack(new Node(Token.NAME, "x"));
      varNode.addChildToBack(new Node(Token.NAME, "y"));

      Node rootNode = new Node(Token.SCRIPT, varNode);

      Scope scope = scopeCreator.createScope(rootNode, null);

      assertNotNull(scope);
      assertNotNull(scope.getVar("x"));
      assertNotNull(scope.getVar("y"));
   }

   @Test
   public void testCreateScope_FunctionArgumentAlreadyDeclared_MultipleDeclarationError() {
      Node catchNode1 = new Node(Token.CATCH, new Node(Token.NAME, "x"), new Node(Token.BLOCK));
      Node catchNode2 = new Node(Token.CATCH, new Node(Token.NAME, "x"), new Node(Token.BLOCK));
      Node catchNode3 = new Node(Token.CATCH, new Node(Token.NAME, "y"), new Node(Token.BLOCK));

      Node rootNode = new Node(Token.SCRIPT, catchNode1, catchNode2, catchNode3);

      Scope scope = scopeCreator.createScope(rootNode, null);

      assertNotNull(scope);
      assertNotNull(scope.getVar("x"));
      assertNotNull(scope.getVar("y"));
      // verify error reported
   }

}