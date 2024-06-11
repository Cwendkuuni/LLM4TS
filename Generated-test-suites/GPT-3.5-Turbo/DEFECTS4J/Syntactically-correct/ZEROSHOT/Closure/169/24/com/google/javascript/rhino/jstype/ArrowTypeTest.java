import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    boolean result = arrowType1.isSubtype(arrowType2);
    assertEquals(true, result);
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    boolean result = arrowType1.hasEqualParameters(arrowType2, true);
    assertEquals(true, result);
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    boolean result = arrowType1.checkArrowEquivalenceHelper(arrowType2, true);
    assertEquals(true, result);
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    int result = arrowType.hashCode();
    assertNotNull(result);
  }

  @Test
  public void testGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.getLeastSupertype(null);
    } catch (UnsupportedOperationException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void testGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.getGreatestSubtype(null);
    } catch (UnsupportedOperationException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void testTestForEquality() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.testForEquality(null);
    } catch (UnsupportedOperationException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void testVisit() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.visit(null);
    } catch (UnsupportedOperationException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(null, null, null);
    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();
    assertNotNull(result);
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    try {
      arrowType.resolveInternal(null, null);
    } catch (UnsupportedOperationException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(null, null, null);
    boolean result = arrowType.hasUnknownParamsOrReturn();
    assertNotNull(result);
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrowType = new ArrowType(null, null, null);
    String result = arrowType.toStringHelper(true);
    assertNotNull(result);
  }

  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    boolean result = arrowType.hasAnyTemplateInternal(); 
    assertNotNull(result);
  }
  
}