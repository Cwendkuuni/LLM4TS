import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {
   @Test
   public void testConstructor() {
      Class<?> erasedType = Object.class;
      TypeBindings bindings = new TypeBindings();
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
      assertNotNull(resolvedRecursiveType);
      assertEquals(erasedType, resolvedRecursiveType.getErasedType());
      assertEquals(bindings, resolvedRecursiveType.getTypeBindings());
      assertNull(resolvedRecursiveType.getSelfReferencedType());
   }
}