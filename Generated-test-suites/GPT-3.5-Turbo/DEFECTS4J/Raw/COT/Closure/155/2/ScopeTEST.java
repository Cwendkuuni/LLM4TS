
import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeTest {
   @Test
   public void testDeclare() {
      Scope scope = new Scope(null, null);
      Var var = scope.declare("var", null, null, null);
      assertNotNull(var);
   }

   @Test
   public void testUndeclare() {
      Scope scope = new Scope(null, null);
      Var var = new Var(true, "var", null, null, null, 0, null, false, null);
      scope.undeclare(var);
      assertNull(scope.getVar("var"));
   }

   @Test
   public void testGetVar() {
      Scope scope = new Scope(null, null);
      Var var = new Var(true, "var", null, null, null, 0, null, false, null);
      scope.declare("var", null, null, null);
      assertTrue(var.equals(scope.getVar("var")));
   }

   @Test
   public void testIsDeclared() {
      Scope scope = new Scope(null, null);
      scope.declare("var", null, null, null);
      assertTrue(scope.isDeclared("var", false));
   }

   @Test
   public void testGetVars() {
      Scope scope = new Scope(null, null);
      scope.declare("var1", null, null, null);
      scope.declare("var2", null, null, null);
      scope.declare("var3", null, null, null);
      int count = 0;
      Iterator<Var> iter = scope.getVars();
      while (iter.hasNext()) {
         iter.next();
         count++;
      }
      assertEquals(3, count);
   }

   @Test
   public void testGetVarCount() {
      Scope scope = new Scope(null, null);
      scope.declare("var1", null, null, null);
      scope.declare("var2", null, null, null);
      scope.declare("var3", null, null, null);
      assertEquals(3, scope.getVarCount());
   }
}
