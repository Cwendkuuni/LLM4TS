import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    // Test when returnType is subtype
    ArrowType arrowType1 = new ArrowType(null, null, new JSTypeRegistry());
    ArrowType arrowType2 = new ArrowType(null, null, new JSTypeRegistry());
    assertTrue(arrowType1.isSubtype(arrowType2));

    // Test when returnType is not subtype
    ArrowType arrowType3 = new ArrowType(null, null, new JSTypeRegistry());
    ArrowType arrowType4 = new ArrowType(null, null, new JSTypeRegistry());
    assertFalse(arrowType3.isSubtype(arrowType4));

    // Test when paramTypes are subtypes
    ArrowType arrowType5 = new ArrowType(null, new Node(), new JSTypeRegistry());
    ArrowType arrowType6 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertTrue(arrowType5.isSubtype(arrowType6));

    // Test when paramTypes are not subtypes
    ArrowType arrowType7 = new ArrowType(null, new Node(), new JSTypeRegistry());
    ArrowType arrowType8 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertFalse(arrowType7.isSubtype(arrowType8));

    // Test when paramTypes have different lengths
    ArrowType arrowType9 = new ArrowType(null, new Node(), new JSTypeRegistry());
    ArrowType arrowType10 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertFalse(arrowType9.isSubtype(arrowType10));
  }

  @Test
  public void testHasEqualParameters() {
    // Test when parameter specs are equal
    ArrowType arrowType1 = new ArrowType(null, new Node(), new JSTypeRegistry());
    ArrowType arrowType2 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertTrue(arrowType1.hasEqualParameters(arrowType2));

    // Test when parameter specs are not equal
    ArrowType arrowType3 = new ArrowType(null, new Node(), new JSTypeRegistry());
    ArrowType arrowType4 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertFalse(arrowType3.hasEqualParameters(arrowType4));
  }

  @Test
  public void testIsEquivalentTo() {
    // Test when return type and parameters are equivalent
    ArrowType arrowType1 = new ArrowType(null, new Node(), new JSTypeRegistry());
    ArrowType arrowType2 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertTrue(arrowType1.isEquivalentTo(arrowType2));

    // Test when return type or parameters are not equivalent
    ArrowType arrowType3 = new ArrowType(null, new Node(), new JSTypeRegistry());
    ArrowType arrowType4 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertFalse(arrowType3.isEquivalentTo(arrowType4));
  }

  @Test
  public void testHashCode() {
    // Test hash code calculation
    ArrowType arrowType1 = new ArrowType(null, new Node(), new JSTypeRegistry());
    ArrowType arrowType2 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(null, null, new JSTypeRegistry());
    arrowType.getLeastSupertype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(null, null, new JSTypeRegistry());
    arrowType.getGreatestSubtype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrowType = new ArrowType(null, null, new JSTypeRegistry());
    arrowType.testForEquality(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType arrowType = new ArrowType(null, null, new JSTypeRegistry());
    arrowType.visit(null);
  }
  
  @Test 
  public void testIsUnknownParamsAndReturn() {
    // Test when params and return type are unknown
    ArrowType arrowType1 = new ArrowType(null, null, new JSTypeRegistry());
    assertTrue(arrowType1.hasUnknownParamsOrReturn());
    
    // Test when params are not unknown but return type is unknown
    ArrowType arrowType2 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertTrue(arrowType2.hasUnknownParamsOrReturn());
    
    // Test when params are unknown but return type is not unknown
    ArrowType arrowType3 = new ArrowType(null, null, new JSTypeRegistry());
    assertTrue(arrowType3.hasUnknownParamsOrReturn());
    
    // Test when params and return type are not unknown
    ArrowType arrowType4 = new ArrowType(null, new Node(), new JSTypeRegistry());
    assertFalse(arrowType4.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    // Test forAnnotations equals false
    ArrowType arrowType1 = new ArrowType(null, null, new JSTypeRegistry());
    assertEquals("ArrowType", arrowType1.toStringHelper(false));

    // Test forAnnotations equals true
    ArrowType arrowType2 = new ArrowType(null, null, new JSTypeRegistry());
    assertEquals("ArrowType", arrowType2.toStringHelper(true));
  }

}