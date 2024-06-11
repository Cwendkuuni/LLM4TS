import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType type1 = new ArrowType(null, null, null);
    ArrowType type2 = new ArrowType(null, null, null);
    assertTrue(type1.isSubtype(type2));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType type1 = new ArrowType(null, null, null);
    ArrowType type2 = new ArrowType(null, null, null);
    assertTrue(type1.hasEqualParameters(type2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType type1 = new ArrowType(null, null, null);
    ArrowType type2 = new ArrowType(null, null, null);
    assertTrue(type1.checkArrowEquivalenceHelper(type2, false));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.getLeastSupertype(null);
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.getGreatestSubtype(null);
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testTestForEquality() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.testForEquality(null);
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testVisit() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.visit(null);
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.resolveInternal(null, null);
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.toStringHelper(false));
  }

  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

}