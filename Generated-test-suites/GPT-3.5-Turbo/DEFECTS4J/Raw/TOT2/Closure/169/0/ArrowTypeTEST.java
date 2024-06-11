import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructor() {
      // Create ArrowType object using constructor with known parameters
      ArrowType arrowType = new ArrowType(registry, parameters, returnType);
      
      // Validate that the object is created successfully
      assertNotNull(arrowType);
      assertEquals(registry, arrowType.registry);
      assertEquals(parameters, arrowType.parameters);
      assertEquals(returnType, arrowType.returnType);
      assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeSubtypeCheck() {
      ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
      ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

      // Check if arrowType1 is a subtype of arrowType2
      assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeEquivalenceCheck() {
      ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
      ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

      // Check if arrowType1 is equivalent to arrowType2
      assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
  }

  @Test
  public void testArrowTypeHashCode() {
      ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
      ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

      // Check if hash codes of arrowType1 and arrowType2 are equal
      assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testArrowTypeUnknownParamsOrReturn() {
      ArrowType arrowType = new ArrowType(registry, parameters, returnType);

      // Check if the ArrowType object has any unknown parameters or return type
      assertFalse(arrowType.hasUnknownParamsOrReturn());
  }
}