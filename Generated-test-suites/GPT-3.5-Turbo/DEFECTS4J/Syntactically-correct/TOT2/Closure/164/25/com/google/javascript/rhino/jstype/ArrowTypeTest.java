import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructor() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testArrowTypeHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    assertNotNull(arrowType.hashCode());
  }

  @Test
  public void testArrowTypeGetLeastSupertype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    try {
      arrowType1.getLeastSupertype(arrowType2);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testArrowTypeGetGreatestSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    try {
      arrowType1.getGreatestSubtype(arrowType2);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testArrowTypeTestForEquality() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    
    try {
      arrowType1.testForEquality(arrowType2);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

}