import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
   @Test
   public void testSimpleTypeConstructor() {
      SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
      assertNotNull(simpleType);
      assertEquals(String.class, simpleType.getRawClass());
      assertFalse(simpleType.isStatic());
   }
}