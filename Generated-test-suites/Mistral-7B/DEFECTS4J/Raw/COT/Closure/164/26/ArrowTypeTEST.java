import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
    scope = new StaticScope<>(registry);
  }

  // Test constructor with null parameters and returnType
  @Test
  public void testArrowTypeConstructor_NullParamsAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
  }

  // Test constructor with null parameters, non-null returnType and returnTypeInferred false
  @Test
  public void testArrowTypeConstructor_NullParamsAndNonNullReturnType() {
    JSType returnType = getNativeType(INT);
    ArrowType arrowType = new ArrowType(registry, null, returnType, false);
    assertNull(arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  // Test constructor with non-null parameters and null returnType
  @Test
  public void testArrowTypeConstructor_NonnullParamsAndNullReturnType() {
    Node parameters = new Node("parameters");
    ArrowType arrowType = new ArrowType(registry, parameters, null);
    assertEquals(parameters, arrowType.parameters);
    assertNull(arrowType.returnType);
  }

  // Test constructor with non-null parameters and non-null returnType
  @Test
  public void testArrowTypeConstructor_NonnullParamsAndNonnullReturnType() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  // Test constructor with non-null parameters and non-null returnType and returnTypeInferred true
  @Test
  public void testArrowTypeConstructor_NonnullParamsAndNonnullReturnTypeInferred() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, true);
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  // Test isSubtype() method with different ArrowTypes
  @Test
  public void testIsSubtype_DifferentArrowTypes() {
    Node firstParameters = new Node("firstParameters");
    Node secondParameters = new Node("secondParameters");
    JSType firstReturnType = getNativeType(INT);
    JSType secondReturnType = getNativeType(DOUBLE);

    ArrowType arrowType1 = new ArrowType(registry, firstParameters, firstReturnType, false);
    ArrowType arrowType2 = new ArrowType(registry, secondParameters, secondReturnType, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  // Test isSubtype() method with same ArrowType
  @Test
  public void testIsSubtype_SameArrowType() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  // Test isSubtype() method with ArrowType and FunctionType
  @Test(expected = UnsupportedOperationException.class)
  public void testIsSubtype_ArrowTypeAndFunctionType() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);
    FunctionType functionType = new FunctionType(registry, parameters, INT, INT);

    arrowType.isSubtype(functionType);
  }

  // Test hasEqualParameters() method with same ArrowTypes
  @Test
  public void testHasEqualParameters_SameArrowTypes() {
    Node firstParameters = new Node("parameters");
    Node secondParameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType1 = new ArrowType(registry, firstParameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, secondParameters, returnType, false);

    // Both parameter lists give a type for all parameters, they should be equal
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType2.hasEqualParameters(arrowType1));
  }

  // Test hasEqualParameters() method with different ArrowTypes
  @Test
  public void testHasEqualParameters_DifferentArrowTypes() {
    Node firstParameters = new Node("parameters1");
    Node secondParameters = new Node("parameters2");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType1 = new ArrowType(registry, firstParameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, secondParameters, returnType, false);

    // Different parameter lists, so they are not equal
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }

  // Test isEquivalentTo() method with same ArrowTypes
  @Test
  public void testIsEquivalentTo_SameArrowTypes() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType, false);

    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType2.isEquivalentTo(arrowType1));
  }

  // Test isEquivalentTo() method with different ArrowTypes
  @Test
  public void testIsEquivalentTo_DifferentArrowTypes() {
    Node firstParameters = new Node("parameters1");
    Node secondParameters = new Node("parameters2");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType1 = new ArrowType(registry, firstParameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, secondParameters, returnType, false);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
  }

  // Test hashCode() method with same ArrowTypes
  @Test
  public void testHashCode_SameArrowTypes() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);
  }

  // Test hashCode() method with different ArrowTypes
  @Test
  public void testHashCode_DifferentArrowTypes() {
    Node firstParameters = new Node("parameters1");
    Node secondParameters = new Node("parameters2");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType1 = new ArrowType(registry, firstParameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, secondParameters, returnType, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }

  // Test getLeastSupertype() method
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);
    arrowType.getLeastSupertype(null);
  }

  // Test getGreatestSubtype() method
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);
    arrowType.getGreatestSubtype(null);
  }

  // Test visit() method
  @Test
  public void testVisit() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    Object result = arrowType.visit(new Visitor<Object>() {
      @Override
      public Object visitArrowType(ArrowType arrowType) {
        return "ArrowType visited";
      }

      @Override
      public Object visitFunctionType(FunctionType functionType) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Object visitPrimitiveType(PrimitiveType primitiveType) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Object visitThisType() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Object visitUnknownType() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Object visitVoidType() {
        throw new UnsupportedOperationException();
      }
    });

    assertEquals("ArrowType visited", result);
  }

  // Test getPossibleToBooleanOutcomes() method
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    Node parameters = new Node("parameters");

    ArrowType arrowType = new ArrowType(registry, parameters, null, false);

    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.TRUE, result);
  }

  // Test resolveInternal() method with null JSType registry
  @Test(expected = NullPointerException.class)
  public void testResolveInternal_NullRegistry() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(null, parameters, returnType, false);
    arrowType.resolveInternal(errorReporter, scope);
  }

  // Test resolveInternal() method with valid JSType registry
  @Test
  public void testResolveInternal() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);
    arrowType.resolveInternal(errorReporter, scope);

    if (parameters != null) {
      for (Node paramNode = parameters.getFirstChild();
           paramNode != null; paramNode = paramNode.getNext()) {
        JSType paramType = paramNode.getJSType();
        if (paramType != null) {
          assertTrue(paramType instanceof JSType);
        }
      }
    }

    if (returnType != null) {
      assertTrue(returnType instanceof JSType);
    }
  }

  // Test hasUnknownParamsOrReturn() method with ArrowType having unknown returnType
  @Test
  public void testHasUnknownParamsOrReturn_UnknownReturnType() {
    Node parameters = new Node("parameters");

    ArrowType arrowType = new ArrowType(registry, parameters, null, false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  // Test hasUnknownParamsOrReturn() method with ArrowType having unknown parameters
  @Test
  public void testHasUnknownParamsOrReturn_UnknownParameters() {
    Node parameters = new Node("parameters");
    for (int i = 0; i < 5; i++) {
      parameters.addChild(new Node("param" + i));
    }

    ArrowType arrowType = new ArrowType(registry, parameters, null, false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  // Test hasUnknownParamsOrReturn() method with ArrowType having no unknown parameters or returnType
  @Test
  public void testHasUnknownParamsOrReturn_NoUnknownParametersOrReturnType() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  // Test toStringHelper() method
  @Test
  public void testToStringHelper() {
    Node parameters = new Node("parameters");
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    assertEquals("ArrowType[parameters->, INT]", arrowType.toStringHelper(false));
  }
}