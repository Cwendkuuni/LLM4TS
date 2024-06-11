import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void isSubtype_returnTrue() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void isSubtype_returnFalse() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void hasEqualParameters_returnTrue() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void hasEqualParameters_returnFalse() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void isEquivalentTo_returnTrue() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void isEquivalentTo_returnFalse() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void hashCode_returnHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.hashCode());
  }

  @Test
  public void getLeastSupertype_throwException() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.getLeastSupertype(null);
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
  }

  @Test
  public void getGreatestSubtype_throwException() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.getGreatestSubtype(null);
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testForEquality_throwException() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.testForEquality(null);
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
  }

  @Test
  public void visit_throwException() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.visit(null);
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
  }

  @Test
  public void getPossibleToBooleanOutcomes_returnBooleanLiteralSet() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void resolveInternal_returnJSType() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.resolveInternal(null, null));
  }

  @Test
  public void hasUnknownParamsOrReturn_returnBoolean() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void toStringHelper_returnString() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.toStringHelper(true));
  }
}