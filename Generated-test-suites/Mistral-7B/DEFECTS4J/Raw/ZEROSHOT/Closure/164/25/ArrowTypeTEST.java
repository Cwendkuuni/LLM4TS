import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private ArrowType arrowType;
  private ArrowType arrowTypeWithSameParamsAndReturn;
  private ArrowType arrowTypeWithDifferentReturn;
  private ArrowType arrowTypeWithDifferentParams;
  private ArrowType arrowTypeWithVarArgs;
  private ArrowType arrowTypeWithNullParams;
  private ArrowType arrowTypeWithNullReturn;
  private Node node;
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    errorReporter = mock(ErrorReporter.class);
    scope = mock(StaticScope.class);

    node = new Node();

    arrowType = new ArrowType(registry, node, null, false);

    arrowTypeWithSameParamsAndReturn = new ArrowType(registry, node,
        new IntType(registry), false);

    arrowTypeWithDifferentReturn = new ArrowType(registry, node,
        new StringType(registry), false);

    arrowTypeWithDifferentParams = new ArrowType(registry, node, null, false);
    arrowTypeWithDifferentParams.parameters = node.createList(
        node.createIdentifier("a"),
        node.createIdentifier("b"),
        node.createIdentifier("c"));

    arrowTypeWithVarArgs = new ArrowType(registry, node, null, false);
    arrowTypeWithVarArgs.parameters = node.createList(
        node.createIdentifier("a"),
        node.createList(node.createIdentifier("arg"), JSTypeNative.ANY));

    arrowTypeWithNullParams = new ArrowType(registry, null, null, false);

    arrowTypeWithNullReturn = new ArrowType(registry, node, null, true);
  }

  @Test
  public void testIsSubtype_sameType_shouldReturnTrue() {
    assertEquals(arrowType, arrowType);
  }

  @Test
  public void testIsSubtype_sameTypeWithSameParamsAndReturn_shouldReturnTrue() {
    assertEquals(arrowType, arrowTypeWithSameParamsAndReturn);
  }

  @Test
  public void testIsSubtype_sameTypeWithDifferentParams_shouldReturnFalse() {
    assertFalse(arrowType.isSubtype(arrowTypeWithDifferentParams));
  }

  @Test
  public void testIsSubtype_sameTypeWithSameParamsAndDifferentReturn_shouldReturnFalse() {
    assertFalse(arrowTypeWithSameParamsAndReturn.isSubtype(arrowTypeWithDifferentReturn));
  }

  @Test
  public void testIsSubtype_differentTypes_shouldReturnFalse() {
    assertFalse(arrowType.isSubtype(new IntType(registry)));
  }

  @Test
  public void testIsSubtype_differentTypesWithSameReturn_shouldReturnTrue() {
    assertTrue(arrowTypeWithDifferentParams.isSubtype(arrowTypeWithSameParamsAndReturn));
  }

  @Test
  public void testIsSubtype_differentTypesWithSameParamsAndDifferentReturn_shouldReturnFalse() {
    assertFalse(arrowTypeWithSameParamsAndReturn.isSubtype(arrowTypeWithDifferentReturn));
  }

  @Test
  public void testIsSubtype_differentTypesWithNullParams_shouldReturnFalse() {
    assertFalse(arrowType.isSubtype(arrowTypeWithNullParams));
  }

  @Test
  public void testIsSubtype_differentTypesWithNullReturn_shouldReturnFalse() {
    assertFalse(arrowTypeWithNullReturn.isSubtype(arrowType));
  }

  @Test
  public void testIsSubtype_nullTypes_shouldReturnFalse() {
    assertFalse(arrowType.isSubtype(null));
  }

  @Test
  public void testHasEqualParameters_sameTypes_shouldReturnTrue() {
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithSameParamsAndReturn));
  }

  @Test
  public void testHasEqualParameters_differentTypes_shouldReturnFalse() {
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithDifferentParams));
  }

  @Test
  public void testIsEquivalentTo_sameTypes_shouldReturnTrue() {
    assertEquals(arrowType, arrowType);
  }

  @Test
  public void testIsEquivalentTo_sameTypesWithSameParamsAndReturn_shouldReturnTrue() {
    assertEquals(arrowType, arrowTypeWithSameParamsAndReturn);
  }

  @Test
  public void testIsEquivalentTo_sameTypesWithDifferentParams_shouldReturnFalse() {
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentParams));
  }

  @Test
  public void testIsEquivalentTo_differentTypes_shouldReturnFalse() {
    assertFalse(arrowType.isEquivalentTo(new IntType(registry)));
  }

  @Test
  public void testHashCode_sameTypes_shouldReturnEqualHashCode() {
    int hashCode1 = arrowType.hashCode();
    int hashCode2 = arrowTypeWithSameParamsAndReturn.hashCode();
    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testHashCode_differentTypes_shouldReturnDifferentHashCode() {
    int hashCode1 = arrowType.hashCode();
    int hashCode2 = arrowTypeWithDifferentParams.hashCode();
    assertNotSame(hashCode1, hashCode2);
  }

  @Test
  public void testGetLeastSupertype_shouldThrowUnsupportedOperationException() {
    try {
      arrowType.getLeastSupertype(new IntType(registry));
      fail("Unexpected result");
    } catch (UnsupportedOperationException e) {
      // Expected result
    }
  }

  @Test
  public void testGetGreatestSubtype_shouldThrowUnsupportedOperationException() {
    try {
      arrowType.getGreatestSubtype(new IntType(registry));
      fail("Unexpected result");
    } catch (UnsupportedOperationException e) {
      // Expected result
    }
  }

  @Test
  public void testTestForEquality_shouldThrowUnsupportedOperationException() {
    try {
      arrowType.testForEquality(new IntType(registry));
      fail("Unexpected result");
    } catch (UnsupportedOperationException e) {
      // Expected result
    }
  }

  @Test
  public void testVisit_shouldThrowUnsupportedOperationException() {
    try {
      arrowType.visit(mock(Visitor.class));
      fail("Unexpected result");
    } catch (UnsupportedOperationException e) {
      // Expected result
    }
  }

  @Test
  public void testGetPossibleToBooleanOutcomes_shouldReturnTrueBooleanLiteralSet() {
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal_shouldResoveReturnType() {
    when(scope.getType(node.createIdentifier("returnType")))
        .thenReturn(new StringType(registry));

    arrowType.resolveInternal(errorReporter, scope);

    assertEquals(new StringType(registry), arrowType.returnType);
  }

  @Test
  public void testResolveInternal_shouldResoveParameters() {
    when(scope.getType(node.createIdentifier("param1")))
        .thenReturn(new IntType(registry));

    arrowTypeWithVarArgs.parameters = node.createList(
        node.createIdentifier("param1"),
        node.createList(node.createIdentifier("arg"), JSTypeNative.ANY));

    arrowTypeWithVarArgs.resolveInternal(errorReporter, scope);

    assertEquals(new IntType(registry), arrowTypeWithVarArgs.parameters.getFirstChild().getJSType());
  }

  @Test
  public void testHasUnknownParamsOrReturn_shouldReturnFalse_whenAllParamsAndReturnAreKnown() {
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_shouldReturnFalse_whenOnlyReturnIsUnknown() {
    arrowTypeWithNullReturn.returnType = null;

    assertFalse(arrowTypeWithNullReturn.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_shouldReturnFalse_whenOnlyParamsAreUnknown() {
    arrowTypeWithNullParams.parameters = null;

    assertFalse(arrowTypeWithNullParams.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_shouldReturnTrue_whenBothParamsAndReturnAreUnknown() {
    arrowTypeWithNullParams.parameters = null;
    arrowTypeWithNullReturn.returnType = null;

    assertTrue(arrowTypeWithNullParams.hasUnknownParamsOrReturn());
  }
}