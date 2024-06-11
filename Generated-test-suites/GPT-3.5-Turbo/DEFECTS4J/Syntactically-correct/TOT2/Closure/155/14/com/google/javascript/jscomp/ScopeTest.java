import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {
   
   @Test
   public void testDeclareVariable() {
      Scope scope = new Scope(null, new Node(Token.FUNCTION));
      Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
      
      assertNotNull(var);
      assertEquals("testVar", var.getName());
      assertEquals(JSType.UNKNOWN, var.getType());
   }

   @Test
   public void testUndeclareVariable() {
      Scope scope = new Scope(null, new Node(Token.FUNCTION));
      Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
      
      assertNotNull(scope.getVar("testVar"));
      scope.undeclare(var);
      assertNull(scope.getVar("testVar"));
   }

   @Test
   public void testDeclareVariableWithInput() {
      CompilerInput input = new CompilerInput("");
      Scope scope = new Scope(null, new Node(Token.FUNCTION));
      Var var = scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, input);
      
      assertNotNull(var);
      assertEquals(input, var.getInput());
   }

   @Test
   public void testIsDeclared() {
      Scope scope = new Scope(null, new Node(Token.FUNCTION));
      scope.declare("testVar", new Node(Token.NAME), JSType.UNKNOWN, null);
      
      assertTrue(scope.isDeclared("testVar", false));
      assertFalse(scope.isDeclared("nonExistentVar", false));
   }

   @Test
   public void testGetGlobalScope() {
      Scope globalScope = new Scope(new Node(Token.SCRIPT), null);
      Scope childScope = new Scope(globalScope, new Node(Token.FUNCTION));

      assertEquals(globalScope, childScope.getGlobalScope());
   }
   
   // Add more test cases for other methods in the Scope class

}