import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructor() {
    ArrowType arrowType = new ArrowType(null, null, null);

    assertNotNull(arrowType);
    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeEquivalent() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);

    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testLeastSupertype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.getLeastSupertype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGreatestSubtype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.getGreatestSubtype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.testForEquality(null);
  }

}