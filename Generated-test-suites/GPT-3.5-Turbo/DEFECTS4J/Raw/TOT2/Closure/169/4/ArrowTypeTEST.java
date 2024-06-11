import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructor() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);
  }

  @Test
  public void testArrowTypeSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeEquality() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.equals(arrowType2));
  }

  @Test
  public void testArrowTypeHashCode() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testArrowTypeResolution() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    arrowType.resolveInternal(t, scope);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }
}