import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ArrowType arrowType;

  @Before
  public void setUp() {
    arrowType = new ArrowType(null, null, null);
  }

  @Test
  public void testIsSubtype() {
    assertFalse(arrowType.isSubtype(null));
  }

  @Test
  public void testHasEqualParameters() {
    assertFalse(arrowType.hasEqualParameters(null, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    assertFalse(arrowType.checkArrowEquivalenceHelper(null, false));
  }

  @Test
  public void testHashCode() {
    assertEquals(0, arrowType.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    try {
      arrowType.getLeastSupertype(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Success
    }
  }

  @Test
  public void testGetGreatestSubtype() {
    try {
      arrowType.getGreatestSubtype(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Success
    }
  }

  @Test
  public void testTestForEquality() {
    try {
      arrowType.testForEquality(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Success
    }
  }

  @Test
  public void testVisit() {
    try {
      arrowType.visit(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Success
    }
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    try {
      arrowType.resolveInternal(null, null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Success
    }
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }

  @Test
  public void testHasAnyTemplateInternal() {
    assertFalse(arrowType.hasAnyTemplateInternal());
  }

}