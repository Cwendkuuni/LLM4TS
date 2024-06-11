import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {
  @Test
  public void testIsSubtype() {
    ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
    boolean result = type1.isSubtype(type2);
    assertTrue(result);
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
    boolean result = type1.hasEqualParameters(type2);
    assertTrue(result);
  }

  @Test
  public void testIsEquivalentTo() {
    ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
    boolean result = type1.isEquivalentTo(type2);
    assertTrue(result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
    type1.getLeastSupertype(type2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
    type1.getGreatestSubtype(type2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
    ArrowType type2 = new ArrowType(new JSTypeRegistry(), null, null);
    type1.testForEquality(type2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType type1 = new ArrowType(new JSTypeRegistry(), null, null);
    type1.visit(null);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
    BooleanLiteralSet result = type.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.TRUE, result);
  }

  @Test
  public void testResolveInternal() {
    ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
    JSType result = type.resolveInternal(null, null);
    assertEquals(type, result);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
    boolean result = type.hasUnknownParamsOrReturn();
    assertFalse(result);
  }

  @Test
  public void testToStringHelper() {
    ArrowType type = new ArrowType(new JSTypeRegistry(), null, null);
    String result = type.toStringHelper(false);
    assertEquals("super.toString()", result);
  }
}