import static org.junit.Assert.*;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.jstype.*;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructorWithParametersAndReturnType() {
    Node parametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    JSType returnType = context.mkType(int.class);
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, returnType);
    assertEquals("ArrowType(parameters=[x, y], returnType=int)", arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructorWithOnlyReturnType() {
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), null, getNativeType(int.class));
    assertEquals("ArrowType(parameters=[], returnType=int)", arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructorWithOnlyParameters() {
    Node parametersNode = context.mkList(context.mkName("x"), context.mkList(context.mkName("y"), context.mkName("z")));
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, null);
    assertEquals("ArrowType(parameters=[x, y, z], returnType=unknown)", arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructorWithReturnTypeInferred() {
    Node parametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, null, true);
    assertEquals("ArrowType(parameters=[x, y], returnType=inferred)", arrowType.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIsSubtypeWithDifferentNumberOfParameters() {
    Node thisParametersNode = context.mkList(context.mkName("x"));
    Node thatParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thatParametersNode, getNativeType(int.class));
    thisType.isSubtype(thatType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIsSubtypeWithDifferentReturnTypes() {
    Node thisParametersNode = context.mkList(context.mkName("x"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(double.class));
    thisType.isSubtype(thatType);
  }

  @Test
  public void testIsSubtypeWithSameParameterTypes() {
    Node thisParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    Node thatParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thatParametersNode, getNativeType(int.class));
    assertTrue(thisType.isSubtype(thatType));
  }

  @Test
  public void testHasEqualParameters() {
    Node thisParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    Node thatParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thatParametersNode, getNativeType(int.class));
    assertTrue(thisType.hasEqualParameters(thatType));
  }

  @Test
  public void testHasEqualParametersDifferentOrder() {
    Node thisParametersNode = context.mkList(context.mkName("y"), context.mkName("x"));
    Node thatParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thatParametersNode, getNativeType(int.class));
    assertFalse(thisType.hasEqualParameters(thatType));
  }

  @Test
  public void testHasEqualParametersWithVarArgs() {
    Node thisParametersNode = context.mkList(context.mkName("x"), context.mkVarArgs("args"));
    Node thatParametersNode = context.mkList(context.mkName("x"), context.mkList(context.mkName("y"), context.mkName("z")), context.mkVarArgs("args"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thatParametersNode, getNativeType(int.class));
    assertTrue(thisType.hasEqualParameters(thatType));
  }

  @Test
  public void testHasEqualParametersWithMissingParameter() {
    Node thisParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    Node thatParametersNode = context.mkList(context.mkName("x"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thatParametersNode, getNativeType(int.class));
    assertFalse(thisType.hasEqualParameters(thatType));
  }

  @Test
  public void testIsEquivalentToWithDifferentNumberOfParameters() {
    Node thisParametersNode = context.mkList(context.mkName("x"));
    Node thatParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thatParametersNode, getNativeType(int.class));
    assertFalse(thisType.isEquivalentTo(thatType));
  }

  @Test
  public void testIsEquivalentToWithDifferentReturnTypes() {
    Node thisParametersNode = context.mkList(context.mkName("x"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(double.class));
    assertFalse(thisType.isEquivalentTo(thatType));
  }

  @Test
  public void testIsEquivalentToWithSameParameterTypes() {
    Node thisParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    Node thatParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thatParametersNode, getNativeType(int.class));
    assertTrue(thisType.isEquivalentTo(thatType));
  }

  @Test
  public void testHashCode() {
    Node thisParametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    Node thatParametersNode = context.mkList(context.mkName("x"), context.mkName("z"));
    ArrowType thisType = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    ArrowType thatType = new ArrowType(new JSTypeRegistry(context), thatParametersNode, getNativeType(int.class));
    assertNotEquals(thisType.hashCode(), thatType.hashCode());

    ArrowType thisTypeWithSame = new ArrowType(new JSTypeRegistry(context), thisParametersNode, getNativeType(int.class));
    assertEquals(thisType.hashCode(), thisTypeWithSame.hashCode());
  }

  @Test
  public void testResolveInternal() {
    Node parametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, getNativeType(int.class));
    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void reportError(int line, int offset, String message, Throwable e) {
        fail("Unexpected error: " + message);
      }

      @Override
      public void reportWarning(int line, int offset, String message) {
        fail("Unexpected warning: " + message);
      }
    };
    StaticScope<JSType> scope = new StaticScope<>(new JSTypeRegistry(context), null);
    arrowType.resolveInternal(errorReporter, scope);
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    JSType xType = context.mkType(int.class);
    JSType yType = context.mkType(String.class);
    scope.put(context.mkName("x"), xType);
    scope.put(context.mkName("y"), yType);
    arrowType.resolveInternal(errorReporter, scope);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturnWithOnlyReturnTypeUnknown() {
    Node parametersNode = null;
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, null);
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturnWithOnlyParametersUnknown() {
    Node parametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, null);
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturnWithBothUnknown() {
    Node parametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, null);
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturnWithOnlyReturnTypeKnown() {
    Node parametersNode = null;
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, getNativeType(int.class));
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturnWithOnlyParametersKnown() {
    Node parametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, getNativeType(int.class));
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturnWithBothKnown() {
    Node parametersNode = context.mkList(context.mkName("x"), context.mkName("y"));
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(context), parametersNode, getNativeType(int.class));
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }
}