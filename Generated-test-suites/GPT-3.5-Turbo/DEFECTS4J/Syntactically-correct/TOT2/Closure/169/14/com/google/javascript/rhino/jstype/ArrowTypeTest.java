import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructor() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType);
  }

  @Test
  public void testArrowTypeSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.hashCode());
  }

  @Test
  public void testArrowTypeResolveInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    JSType resolvedType = arrowType.resolveInternal(null, null);
    assertNotNull(resolvedType);
  }

  @Test
  public void testArrowTypeUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

}