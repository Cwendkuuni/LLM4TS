import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype_BothArrowType_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.STRING);
    Node parameters2 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_BothArrowType_ReturnsFalse() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.NUMBER);
    Node parameters2 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters_BothArrowType_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.STRING);
    Node parameters2 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testHasEqualParameters_BothArrowType_ReturnsFalse() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.NUMBER);
    Node parameters2 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testIsEquivalentTo_BothArrowType_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.STRING);  
    Node parameters2 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testIsEquivalentTo_BothArrowType_ReturnsFalse() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.NUMBER);
    Node parameters2 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testHashCode_BothArrowType_ReturnsSameHashcode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.STRING);
    Node parameters2 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testHashCode_BothArrowType_ReturnsDifferentHashcode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.STRING);
    Node parameters2 = new Node(Token.NUMBER);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype_ThrowsUnsupportedOperationException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    arrowType1.getLeastSupertype(arrowType1);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype_ThrowsUnsupportedOperationException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    arrowType1.getGreatestSubtype(arrowType1);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality_ThrowsUnsupportedOperationException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    arrowType1.testForEquality(arrowType1);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit_ThrowsUnsupportedOperationException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Token.STRING);
    JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    arrowType1.visit(visitor);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);

    assertEquals(BooleanLiteralSet.TRUE, arrowType1.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal_HasUnknownParamsOrReturn_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, new Node(Token.STRING), null);
    assertTrue(arrowType1.hasUnknownParamsOrReturn());
  }

  @Test
  public void testResolveInternal_HasUnknownParamsOrReturn_ReturnsFalse() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, registry.createParametersWithVarArgs(), registry.getNativeType(JSTypeNative.STRING_TYPE));
    assertFalse(arrowType1.hasUnknownParamsOrReturn());
  }
}