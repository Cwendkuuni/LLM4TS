import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

   @Test
   public void testConstructor() {
      Class<?> erasedType = Object.class;
      TypeBindings bindings = new TypeBindings();
      ResolvedRecursiveType actual = new ResolvedRecursiveType(erasedType, bindings);
      ResolvedRecursiveType expected = new ResolvedRecursiveType(erasedType, bindings);
      assertEquals(expected, actual);
   }

   // Test for edge case: constructor with null erasedType or bindings
   @Test(expected = IllegalArgumentException.class)
   public void testConstructorWithNullErasedType() {
      TypeBindings bindings = new TypeBindings();
      new ResolvedRecursiveType(null, bindings);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructorWithNullBindings() {
      Class<?> erasedType = Object.class;
      new ResolvedRecursiveType(erasedType, null);
   }
}