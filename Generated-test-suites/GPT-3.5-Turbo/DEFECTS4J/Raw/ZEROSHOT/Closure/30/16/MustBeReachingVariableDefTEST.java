import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

   MustBeReachingVariableDef mustBeReachingVariableDef;

   @Before
   public void setUp() {
      mustBeReachingVariableDef = new MustBeReachingVariableDef();
   }

   @Test
   public void testGetDef() {
      Node node = mustBeReachingVariableDef.getDef("name", new Node());
      assertEquals(null, node);
   }

   @Test
   public void testDependsOnOuterScopeVars() {
      boolean result = mustBeReachingVariableDef.dependsOnOuterScopeVars("name", new Node());
      assertEquals(false, result);
   }
}