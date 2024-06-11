import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrowTypeTest {

  private ArrowType arrowType;

  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    this.arrowType = new ArrowType(registry, parameters, returnType);
  }

  @Test
  public void testIsSubtype_WhenReturnTypeIsSubtype_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    ArrowType other = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType.isSubtype(other));
  }

  @Test
  public void testIsSubtype_WhenParamTypeIsSubtype_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    parameters.addChildToFront(new Node(Token.NAME, "param1", stringType));
    ArrowType other = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.isSubtype(other));
  }

  @Test
  public void testIsSubtype_WhenVarArgs_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    parameters.addChildToFront(new Node(Token.NAME, "param1", stringType));
    parameters.setVarArgs(true);
    ArrowType other = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.isSubtype(other));
  }

  @Test
  public void testHasEqualParameters_WhenParametersAreEqual_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    parameters.addChildToFront(new Node(Token.NAME, "param1", stringType));
    ArrowType other = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType.hasEqualParameters(other));
  }

  @Test
  public void testIsEquivalentTo_WhenReturnTypeIsEquivalent_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType other = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.isEquivalentTo(other));
  }

  @Test
  public void testHashCode_WhenReturnTypeIsNull_ReturnsValidHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertNotNull(arrowType.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype_ThrowsUnsupportedOperationException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    arrowType.getLeastSupertype(arrowType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype_ThrowsUnsupportedOperationException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    arrowType.getGreatestSubtype(arrowType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality_ThrowsUnsupportedOperationException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    arrowType.testForEquality(arrowType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit_ThrowsUnsupportedOperationException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    arrowType.visit(new Visitor<>());
  }

  @Test
  public void testGetPossibleToBooleanOutcomes_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal_ReturnsResolvedArrowType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    ErrorReporter errorReporter = new ErrorReporter();

    JSType resolvedType = arrowType.resolveInternal(errorReporter, new StaticScope<>());

    assertNotNull(resolvedType);
  }

  @Test
  public void testHasUnknownParamsOrReturn_WhenReturnTypeIsUnknown_ReturnsTrue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper_ReturnsStringRepresentation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertNotNull(arrowType.toStringHelper(true));
  }

}