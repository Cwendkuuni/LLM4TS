package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.testing.PolymerTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(PolymerTestRunner.class)
public class ArrowTypeTest {

  private ArrowType arrowType;
  private ArrowType arrowTypeWithUnknownReturn;
  private ArrowType arrowTypeWithVarArgs;
  private ArrowType arrowTypeWithMismatchedParams;
  private ArrowType arrowTypeWithOptionalParams;
  private ArrowType arrowTypeWithNoParams;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();

    JSTypeNative registry = new JSTypeNative();
    Node parameters = new Node();
    Node parameter1 = new Node();
    Node parameter2 = new Node();
    Node returnType = new Node();

    arrowType = new ArrowType(registry, parameters, returnType);
    arrowTypeWithUnknownReturn = new ArrowType(registry, parameters, UNKNOWN_TYPE);
    arrowTypeWithVarArgs = new ArrowType(registry, parameters, returnType, true);
    arrowTypeWithMismatchedParams = new ArrowType(registry, parameters, returnType, false);
    arrowTypeWithOptionalParams = new ArrowType(registry, new Node("number?"), returnType);
    arrowTypeWithNoParams = new ArrowType(registry, null, returnType);
  }

  @Test
  public void testIsSubtype() {
    // Test same arrow types
    assertTrue(arrowType.isSubtype(arrowType));

    // Test arrow types with same return types
    assertTrue(arrowType.isSubtype(arrowTypeWithUnknownReturn));
    assertTrue(arrowType.isSubtype(arrowTypeWithVarArgs));

    // Test arrow types with different return types
    assertFalse(arrowType.isSubtype(new ArrowType(registry, parameters, JSTypeNative.INT_TYPE)));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, parameters, JSTypeNative.STRING_TYPE)));

    // Test arrow types with different number of parameters
    assertFalse(arrowType.isSubtype(new ArrowType(registry, new Node("number"), returnType)));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, new Node("number", "number"), returnType)));

    // Test arrow types with mismatched parameter types
    assertFalse(arrowType.isSubtype(arrowTypeWithMismatchedParams));

    // Test arrow types with optional parameters
    assertTrue(arrowType.isSubtype(arrowTypeWithOptionalParams));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType3 = new ArrowType(registry, new Node("number"), returnType);
    ArrowType arrowType4 = new ArrowType(registry, new Node("number", "number"), returnType);
    ArrowType arrowType5 = new ArrowType(registry, parameters, JSTypeNative.INT_TYPE);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType4, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType5, false));

    // Tolerate unknowns
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType3, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType5, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType3 = new ArrowType(registry, new Node("number"), returnType);
    ArrowType arrowType4 = new ArrowType(registry, new Node("number", "number"), returnType);
    ArrowType arrowType5 = new ArrowType(registry, parameters, JSTypeNative.INT_TYPE);

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, false));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType4, false));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType5, false));

    // Tolerate unknowns
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType3, true));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType4, true));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType5, true));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType3 = new ArrowType(registry, new Node("number"), returnType);
    ArrowType arrowType4 = new ArrowType(registry, new Node("number", "number"), returnType);
    ArrowType arrowType5 = new ArrowType(registry, parameters, JSTypeNative.INT_TYPE);

    int hashcode1 = arrowType1.hashCode();
    int hashcode2 = arrowType1.hashCode();
    int hashcode3 = arrowType3.hashCode();
    int hashcode4 = arrowType4.hashCode();
    int hashcode5 = arrowType5.hashCode();

    assertEquals(hashcode1, hashcode2);
    assertNotEquals(hashcode1, hashcode3);
    assertNotEquals(hashcode1, hashcode4);
    assertNotEquals(hashcode1, hashcode5);
  }

  @Test
  public void testResolve() {
    JSType fixedReturnType = new IntType(registry);
    JSType fixedParameters = new ArrayType(registry, new IntType(registry));

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    ArrowType arrowTypeWithFixedReturn = new ArrowType(registry, parameters, fixedReturnType);
    ArrowType arrowTypeWithFixedParams = new ArrowType(registry, fixedParameters, returnType);

    arrowType.resolve(errorReporter, new StaticScope<>(registry));
    arrowTypeWithFixedReturn.resolve(errorReporter, new StaticScope<>(registry));
    arrowTypeWithFixedParams.resolve(errorReporter, new StaticScope<>(registry));

    assertTrue(arrowType.getJSType().checkEquivalenceHelper(arrowTypeWithFixedReturn.getJSType(), false));
    assertTrue(arrowType.getJSType().checkEquivalenceHelper(arrowTypeWithFixedParams.getJSType(), false));
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, UNKNOWN_TYPE);
    ArrowType arrowType3 = new ArrowType(registry, new Node("number?"), returnType);

    assertTrue(arrowType1.hasUnknownParamsOrReturn());
    assertTrue(arrowType2.hasUnknownParamsOrReturn());
    assertTrue(arrowType3.hasUnknownParamsOrReturn());

    Node noParamsNode = null;
    ArrowType arrowType4 = new ArrowType(registry, noParamsNode, returnType);

    assertTrue(arrowType4.hasUnknownParamsOrReturn());
  }
}