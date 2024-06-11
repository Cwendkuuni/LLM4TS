import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private ArrowType arrowType;

  @Before
  public void setUp() {
    arrowType = new ArrowType();
  }

  @Test
  public void testIsSubtype_WhenOtherTypeIsArrowType_ReturnsTrue() {
    ArrowType otherType = new ArrowType();
    boolean result = arrowType.isSubtype(otherType);
    assertTrue(result);
  }

  @Test
  public void testIsSubtype_WhenOtherTypeIsNotArrowType_ReturnsFalse() {
    JSType otherType = new JSType();
    boolean result = arrowType.isSubtype(otherType);
    assertFalse(result);
  }

  @Test
  public void testHasEqualParameters_WhenOtherTypeHasEqualParameters_ReturnsTrue() {
    ArrowType otherType = new ArrowType();
    otherType.setParameters("x", "y", "z");
    boolean result = arrowType.hasEqualParameters(otherType);
    assertTrue(result);
  }

  @Test
  public void testHasEqualParameters_WhenOtherTypeDoesNotHaveEqualParameters_ReturnsFalse() {
    ArrowType otherType = new ArrowType();
    otherType.setParameters("x", "y");
    boolean result = arrowType.hasEqualParameters(otherType);
    assertFalse(result);
  }

  @Test
  public void testCheckArrowEquivalenceHelper_WhenOtherTypeIsEquivalent_ReturnsTrue() {
    ArrowType otherType = new ArrowType();
    otherType.setParameters("x", "y");
    boolean result = arrowType.checkArrowEquivalenceHelper(otherType);
    assertTrue(result);
  }

  @Test
  public void testCheckArrowEquivalenceHelper_WhenOtherTypeIsNotEquivalent_ReturnsFalse() {
    ArrowType otherType = new ArrowType();
    otherType.setParameters("x", "y", "z");
    boolean result = arrowType.checkArrowEquivalenceHelper(otherType);
    assertFalse(result);
  }

  @Test
  public void testHashCode_WhenReturnTypeIsNull_ReturnsZero() {
    arrowType.setReturnType(null);
    int result = arrowType.hashCode();
    assertEquals(0, result);
  }
  
  @Test
  public void testHashCode_WhenReturnTypeIsNotNull_ReturnsReturnValueHashCode() {
    JSType returnType = new JSType();
    returnType.setValue("int");
    arrowType.setReturnType(returnType);
    int result = arrowType.hashCode();
    assertEquals(returnType.hashCode(), result);
  }

  @Test
  public void testResolveInternal_WhenReturnTypeIsNull_ReturnsSameInstance() {
    arrowType.setReturnType(null);
    ArrowType result = arrowType.resolveInternal();
    assertSame(arrowType, result);
  }

  @Test
  public void testToStringHelper_WhenForAnnotationsIsTrue_ReturnsAnnotationsStringRepresentation() {
    String expected = "[ArrowType]";
    String result = arrowType.toStringHelper(true);
    assertEquals(expected, result);
  }

  @Test
  public void testToStringHelper_WhenForAnnotationsIsFalse_ReturnsStringRepresentation() {
    String expected = "[ArrowType]";
    String result = arrowType.toStringHelper(false);
    assertEquals(expected, result);
  }
}