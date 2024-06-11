import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {
  
  private ArrowType arrowType;
  private JSTypeRegistry registry;
  private Node parameters;
  private JSType returnType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    parameters = new Node(Node.Type.PARAMETERS);
    returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    arrowType = new ArrowType(registry, parameters, returnType);
  }

  @Test
  public void testConstructor_withNullParametersAndReturnType() {
    arrowType = new ArrowType(registry, null, null);
    assertNull(arrowType.parameters);
    assertNotNull(arrowType.returnType);
    assertTrue(arrowType.returnType.isUnknownType());
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testConstructor_withNonNullParametersAndReturnType() {
    assertNotNull(arrowType.parameters);
    assertNotNull(arrowType.returnType);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testConstructor_withNonNullParametersNullReturnType() {
    arrowType = new ArrowType(registry, parameters, null);
    assertNotNull(arrowType.parameters);
    assertNotNull(arrowType.returnType);
    assertTrue(arrowType.returnType.isUnknownType());
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testConstructor_withNonNullParametersAndNonNullReturnTypeAndReturnTypeInferred() {
    arrowType = new ArrowType(registry, parameters, returnType, true);
    assertNotNull(arrowType.parameters);
    assertNotNull(arrowType.returnType);
    assertEquals(returnType, arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype_withDifferentTypes() {
    JSType otherType = new JSType(registry);
    assertFalse(arrowType.isSubtype(otherType));
  }

  @Test
  public void testIsSubtype_withSameType() {
    ArrowType sameType = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType.isSubtype(sameType));
  }

  @Test
  public void testIsSubtype_withSubtype() {
    Node subParameters = new Node(Node.Type.PARAMETERS);
    JSType subReturnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    ArrowType subType = new ArrowType(registry, subParameters, subReturnType);
    assertTrue(arrowType.isSubtype(subType));
  }

  @Test
  public void testIsEquivalentTo_withDifferentTypes() {
    JSType otherType = new JSType(registry);
    assertFalse(arrowType.isEquivalentTo(otherType));
  }

  @Test
  public void testIsEquivalentTo_withSameType() {
    ArrowType sameType = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType.isEquivalentTo(sameType));
  }

  @Test
  public void testIsEquivalentTo_withEquivalentType() {
    ArrowType equivalentType = new ArrowType(registry, parameters, returnType);
    equivalentType.returnTypeInferred = true;
    assertTrue(arrowType.isEquivalentTo(equivalentType));
  }

  @Test
  public void testGetGreatestSubtype() {
    try {
      arrowType.getGreatestSubtype(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testGetLeastSupertype() {
    try {
      arrowType.getLeastSupertype(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testTestForEquality() {
    try {
      arrowType.testForEquality(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      // Pass
    }
  }

  @Test
  public void testToStringHelper() {
    String expectedString = arrowType.toString();
    assertEquals(expectedString, arrowType.toStringHelper(true));
  }

  @Test
  public void testResolveInternal_withNonNullReturnTypeAndNonNullParameters() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    arrowType.resolveInternal(errorReporter, scope);
    assertEquals(returnType, arrowType.returnType);

    Node paramNode = parameters.getFirstChild();
    while (paramNode != null) {
      JSType paramType = paramNode.getJSType();
      assertNotNull(paramType);
      assertFalse(paramType.isUnknownType());
      paramNode = paramNode.getNext();
    }
  }

  @Test
  public void testHasUnknownParamsOrReturn_withUnknownReturnType() {
    arrowType = new ArrowType(registry, parameters, registry.getNativeType(JSTypeNative.NUMBER_TYPE), true);
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_withUnknownParameterType() {
    parameters.addChildToFront(new Node(Node.Type.VAR, new Node(Node.Type.NAME, "x")));
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_withUnknownParameterTypeAndReturnType() {
    parameters.addChildToFront(new Node(Node.Type.VAR, new Node(Node.Type.NAME, "x")));
    arrowType.returnType = null;
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

}