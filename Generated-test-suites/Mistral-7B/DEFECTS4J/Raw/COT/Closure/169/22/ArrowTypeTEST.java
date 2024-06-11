import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

public class ArrowTypeTest {

  private ErrorReporter t = ErrorReporter.NULL;
  private JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testArrowTypeConstructorWithParametersAndReturnType() {
    Node parameters = arrayLiteral(
        identifier("param1"),
        identifier("param2")
    );
    JSType returnType = intType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);
    assertEquals(registry.createParametersWithVarArgs(intType()), arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
  }

  @Test
  public void testArrowTypeConstructorWithOnlyReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, intType());
    assertNotNull(arrowType);
    assertNull(arrowType.parameters);
    assertEquals(intType(), arrowType.returnType);
  }

  @Test
  public void testArrowTypeConstructorWithOnlyReturnTypeInferred() {
    ArrowType arrowType = new ArrowType(registry, null, null, true);
    assertNotNull(arrowType);
    assertNull(arrowType.parameters);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeConstructorWithNullParameters() {
    new ArrowType(registry, null, intType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeConstructorWithNullReturnType() {
    new ArrowType(registry, arrayLiteral(), null);
  }

  @Test
  public void testIsSubtypeBasicScenario() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(), intType());

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeReturnTypes() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(), stringType());

    assertFalse(arrowType1.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, arrayLiteral(), objectType());
    assertTrue(arrowType1.isSubtype(arrowType3));
  }

  @Test
  public void testIsSubtypeParameters() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("Number"), identifier("String")), intType());

    assertFalse(arrowType1.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, arrayLiteral(identifier("Number")), intType());
    assertTrue(arrowType1.isSubtype(arrowType3));
  }

  @Test
  public void testIsSubtypeVarArgs() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("Number"), identifier("Number")), intType());

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParametersBasicScenario() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParametersDifferentNumberOfParameters() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("String")), intType());

    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParametersSameParameterTypeButDifferentNames() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("param2"), identifier("param1")), intType());

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParametersDifferentTypes() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("Number"), identifier("String")), intType());

    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParametersWithOptionalAndVarArgs() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2?"), arrayLiteral()), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("param1?"), identifier("param2"), arrayLiteral()), intType());

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParametersWithVarArgsAtEnd() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2"), arrayLiteral()), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2")), arrayLiteral());

    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));

    ArrowType arrowType3 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2")), arrayLiteral());
    ArrowType arrowType4 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2"), arrayLiteral()), intType());

    assertTrue(arrowType3.hasEqualParameters(arrowType4, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperBasicScenario() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperDifferentNumberOfParameters() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("String")), intType());

    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperSameParameterTypeButDifferentNames() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("param2"), identifier("param1")), intType());

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperDifferentTypes() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("Number"), identifier("String")), intType());

    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperWithOptionalAndVarArgs() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2?"), arrayLiteral()), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("param1?"), identifier("param2"), arrayLiteral()), intType());

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelperWithVarArgsAtEnd() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2"), arrayLiteral()), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2")), arrayLiteral());

    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

    ArrowType arrowType3 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2")), arrayLiteral());
    ArrowType arrowType4 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2"), arrayLiteral()), intType());

    assertTrue(arrowType3.checkArrowEquivalenceHelper(arrowType4, false));
  }

  @Test
  public void testHashCodeBasicScenario() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testHashCodeDifferentParameters() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("String"), identifier("Number")), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("Number"), identifier("String")), intType());

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testHashCodeWithOptionalAndVarArgs() {
    ArrowType arrowType1 = new ArrowType(registry, arrayLiteral(identifier("param1"), identifier("param2?"), arrayLiteral()), intType());
    ArrowType arrowType2 = new ArrowType(registry, arrayLiteral(identifier("param1?"), identifier("param2"), arrayLiteral()), intType());

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testGetLeastSupertypeUnsupportedOperationException() {
    ArrowType arrowType = new ArrowType(registry, arrayLiteral(), intType());

    try {
      arrowType.getLeastSupertype(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testGetGreatestSubtypeUnsupportedOperationException() {
    ArrowType arrowType = new ArrowType(registry, arrayLiteral(), intType());

    try {
      arrowType.getGreatestSubtype(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testTestForEqualityUnsupportedOperationException() {
    ArrowType arrowType = new ArrowType(registry, arrayLiteral(), intType());

    try {
      arrowType.testForEquality(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testVisitUnsupportedOperationException() {
    ArrowType arrowType = new ArrowType(registry, arrayLiteral(), intType());

    try {
      arrowType.visit(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(registry, arrayLiteral(), intType());

    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.TRUE, result);
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType(registry, arrayLiteral(), intType());

    arrowType.resolveInternal(t, registry.getGlobalScope());

    JSType resolvedReturnType = arrowType.returnType;
    assertEquals(intType(), resolvedReturnType);
  }

  @Test
  public void testHasAnyTemplateInternalFalse() {
    ArrowType arrowType = new ArrowType(registry, arrayLiteral(), intType());

    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternalTrue() {
    JSType parameterType = new ArrayType(registry, arrayLiteral(intType()), intType());
    ArrowType arrowType = new ArrowType(registry, arrayLiteral(identifier("T")), parameterType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasAnyTemplateInternalWithParams() {
    JSType parameterType = new ArrayType(registry, arrayLiteral(intType()), intType());
    ArrowType arrowType = new ArrowType(registry, arrayLiteral(identifier("T"), identifier("S")), parameterType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }
}