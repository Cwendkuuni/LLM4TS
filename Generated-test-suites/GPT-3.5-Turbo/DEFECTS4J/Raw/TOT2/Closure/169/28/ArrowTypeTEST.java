import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructor() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType);
  }

  @Test
  public void testArrowType_isSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowType_hasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
  }

  @Test
  public void testArrowType_checkArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
  }

  @Test
  public void testArrowType_hashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowType_resolveInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.resolveInternal(null, null);
  }

  @Test
  public void testArrowType_hasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testArrowType_toStringHelper() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.toStringHelper(false));
  }

  @Test
  public void testArrowType_hasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertFalse(arrowType.hasAnyTemplateInternal());
  }
}