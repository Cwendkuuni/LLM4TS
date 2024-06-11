import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    errorReporter = mock(ErrorReporter.class);
  }

  @Test
  public void testArrowTypeConstructor_NullParameters() {
    // Test case: ArrowType(registry, null, returnType)
    ArrowType arrowType = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    assertEquals(JSType.FUNCTION_TYPE, arrowType.getTypeClass());
    assertNull(arrowType.parameters);
    assertEquals(new JSTypeNative(JSType.INT), arrowType.returnType);
  }

  @Test
  public void testArrowTypeConstructor_ValidParameters() {
    // Test case: ArrowType(registry, parameters, returnType)
    Node parametersNode = new Node("(number, number)");
    ArrowType arrowType = new ArrowType(registry, parametersNode, new JSTypeNative(JSType.INT), false);
    assertEquals(JSType.FUNCTION_TYPE, arrowType.getTypeClass());
    assertEquals(parametersNode, arrowType.parameters);
    assertEquals(new JSTypeNative(JSType.INT), arrowType.returnType);
  }

  @Test
  public void testArrowTypeConstructor_ValidParametersWithInferredReturnType() {
    // Test case: ArrowType(registry, parameters, null)
    Node parametersNode = new Node("(number, number)");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);
    assertEquals(JSType.FUNCTION_TYPE, arrowType.getTypeClass());
    assertEquals(parametersNode, arrowType.parameters);
    assertNull(arrowType.returnType);
  }

  @Test
  public void testArrowTypeConstructor_InvalidParameters() {
    // Test case: ArrowType(registry, null, returnType)
    ArrowType arrowType = assertThrows(
        IllegalArgumentException.class,
        () -> new ArrowType(registry, null, new JSTypeNative(JSType.INT), false));
    assertEquals("parameters cannot be null", arrowType.getMessage());

    // Test case: ArrowType(registry, parameters, null)
    Node parametersNode = new Node("(number, number)");
    ArrowType arrowType = assertThrows(
        IllegalArgumentException.class,
        () -> new ArrowType(registry, parametersNode, null, true));
    assertEquals("returnType cannot be null", arrowType.getMessage());
  }

  @Test
  public void testArrowTypeIsSubtype_SameType() {
    // Test case: this.returnType <: that.returnType
    ArrowType arrowType1 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType2 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtype_DifferentReturnTypes() {
    // Test case: this.returnType <: that.returnType (covariant)
    ArrowType arrowType1 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType2 = new ArrowType(registry, null, new JSTypeNative(JSType.STRING), false);
    assertFalse(arrowType1.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, null, new JSTypeNative(JSType.STRING), false);
    assertTrue(arrowType2.isSubtype(arrowType3));
  }

  @Test
  public void testArrowTypeIsSubtype_DifferentParameterTypes() {
    // Test case: that.paramType[i] <: this.paramType[i] (contravariant)
    ArrowType arrowType1 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType2 = new ArrowType(registry, null, new JSTypeNative(JSType.STRING), false);
    assertFalse(arrowType1.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, null, new JSTypeNative(JSType.NUMBER), false);
    ArrowType arrowType4 = new ArrowType(registry, null, new JSTypeNative(JSType.NUMBER), false);
    assertTrue(arrowType3.isSubtype(arrowType4));

    // Test case: "that" can't be a supertype, because it's missing a required argument.
    ArrowType arrowType5 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType6 = new ArrowType(registry, null, null, false);
    assertFalse(arrowType5.isSubtype(arrowType6));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    // Test case: hasEqualParameters(that, tolerateUnknowns = false)
    ArrowType arrowType1 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType2 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));

    // Test case: hasEqualParameters(that, tolerateUnknowns = true)
    ArrowType arrowType3 = new ArrowType(registry, null, new JSTypeNative(JSType.UNKNOWN_TYPE), false);
    ArrowType arrowType4 = new ArrowType(registry, null, new JSTypeNative(JSType.UNKNOWN_TYPE), false);
    assertTrue(arrowType3.hasEqualParameters(arrowType4, true));

    ArrowType arrowType5 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType6 = new ArrowType(registry, null, new JSTypeNative(JSType.STRING), false);
    assertFalse(arrowType5.hasEqualParameters(arrowType6, false));

    ArrowType arrowType7 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType8 = new ArrowType(registry, null, null, false);
    assertFalse(arrowType7.hasEqualParameters(arrowType8, false));

    ArrowType arrowType9 = new ArrowType(registry, null, null, false);
    ArrowType arrowType10 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    assertFalse(arrowType9.hasEqualParameters(arrowType10, false));
  }

  @Test
  public void testArrowTypeCheckEquivalenceHelper() {
    // Test case: checkArrowEquivalenceHelper(that, tolerateUnknowns = false)
    ArrowType arrowType1 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType2 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

    ArrowType arrowType3 = new ArrowType(registry, null, new JSTypeNative(JSType.STRING), false);
    ArrowType arrowType4 = new ArrowType(registry, null, new JSTypeNative(JSType.STRING), false);
    assertTrue(arrowType3.checkArrowEquivalenceHelper(arrowType4, false));

    ArrowType arrowType5 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType6 = new ArrowType(registry, null, new JSTypeNative(JSType.STRING), false);
    assertFalse(arrowType5.checkArrowEquivalenceHelper(arrowType6, false));

    ArrowType arrowType7 = new ArrowType(registry, null, null, false);
    ArrowType arrowType8 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    assertFalse(arrowType7.checkArrowEquivalenceHelper(arrowType8, false));

    // Test case: checkArrowEquivalenceHelper(that, tolerateUnknowns = true)
    ArrowType arrowType9 = new ArrowType(registry, null, new JSTypeNative(JSType.UNKNOWN_TYPE), false);
    ArrowType arrowType10 = new ArrowType(registry, null, new JSTypeNative(JSType.UNKNOWN_TYPE), false);
    assertTrue(arrowType9.checkArrowEquivalenceHelper(arrowType10, true));

    ArrowType arrowType11 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType12 = new ArrowType(registry, null, new JSTypeNative(JSType.UNKNOWN_TYPE), false);
    assertFalse(arrowType11.checkArrowEquivalenceHelper(arrowType12, true));

    ArrowType arrowType13 = new ArrowType(registry, null, new JSTypeNative(JSType.UNKNOWN_TYPE), false);
    ArrowType arrowType14 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    assertFalse(arrowType13.checkArrowEquivalenceHelper(arrowType14, true));
  }

  @Test
  public void testArrowTypeHashCode() {
    ArrowType arrowType1 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType2 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    ArrowType arrowType3 = new ArrowType(registry, null, new JSTypeNative(JSType.STRING), false);
    ArrowType arrowType4 = new ArrowType(registry, null, null, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();
    assertEquals(hashCode1, hashCode2);

    int hashCode5 = arrowType1.hashCode();
    int hashCode3 = arrowType2.hashCode();
    assertNotEquals(hashCode1, hashCode3);

    int hashCode6 = arrowType1.hashCode();
    int hashCode4 = arrowType3.hashCode();
    assertNotEquals(hashCode1, hashCode4);

    int hashCode7 = arrowType1.hashCode();
    int hashCode5 = arrowType4.hashCode();
    assertNotEquals(hashCode1, hashCode5);
  }

  @Test
  public void testArrowTypeGetLeastSupertype() {
    // UnsupportedOperationException is thrown
  }

  @Test
  public void testArrowTypeGetGreatestSubtype() {
    // UnsupportedOperationException is thrown
  }

  @Test
  public void testArrowTypeTestForEquality() {
    // UnsupportedOperationException is thrown
  }

  @Test
  public void testArrowTypeVisit() {
    // UnsupportedOperationException is thrown
  }

  @Test
  public void testArrowTypeGetPossibleToBooleanOutcomes() {
    // The method returns BooleanLiteralSet.TRUE
  }

  @Test
  public void testArrowTypeResolveInternal() {
    when(registry.createParametersWithVarArgs(JSType.INT)).thenReturn(new Node("(number, ...)"));
    when(registry.createParametersWithVarArgs(JSType.STRING)).thenReturn(new Node("(string, ...)"));

    ArrowType arrowType1 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    arrowType1.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertSame(new Node("(number, ...)"), arrowType1.parameters);

    ArrowType arrowType2 = new ArrowType(registry, null, new JSTypeNative(JSType.STRING), false);
    arrowType2.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertSame(new Node("(string, ...)"), arrowType2.parameters);

    ArrowType arrowType3 = new ArrowType(registry, null, null, true);
    arrowType3.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertNull(arrowType3.parameters);

    ArrowType arrowType4 = new ArrowType(registry, null, null, false);
    arrowType4.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertNull(arrowType4.parameters);
  }

  @Test
  public void testArrowTypeHasAnyTemplateInternal() {
    ArrowType arrowType1 = new ArrowType(registry, null, new JSTypeNative(JSType.INT), false);
    assertFalse(arrowType1.hasAnyTemplateInternal());

    ArrowType arrowType2 = new ArrowType(registry, null, new JSTypeNative(JSType.TUPLE), false);
    assertTrue(arrowType2.hasAnyTemplateInternal());
  }
}