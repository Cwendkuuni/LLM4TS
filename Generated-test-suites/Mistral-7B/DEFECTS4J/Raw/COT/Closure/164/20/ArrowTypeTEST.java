import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.Context;
import com.google.javascript.rhino.testing.TestBase;

public class ArrowTypeTest extends TestBase {

  private ArrowType arrowType;
  private ArrowType arrowTypeWithUnknownType;
  private ArrowType arrowTypeWithInferredReturnType;
  private ArrowType arrowTypeWithVarArgs;
  private ArrowType arrowTypeWithNullReturnType;
  private ArrowType arrowTypeWithMismatchedParameters;

  @Override
  public void setUp() {
    super.setUp();

    Context context = new Context(null);
    ErrorReporter t = context.newErrorReporter();
    StaticScope<JSType> scope = context.newStaticScope();

    arrowType = new ArrowType(getRegistry(context), null, getNativeType(INT), false);

    arrowTypeWithUnknownType = new ArrowType(getRegistry(context), null, null, false);

    arrowTypeWithInferredReturnType = new ArrowType(getRegistry(context), null, null, true);

    arrowTypeWithVarArgs = new ArrowType(getRegistry(context), null, null, false);
    arrowTypeWithVarArgs.parameters =
        new Node(getRegistry(context), new Node[] {
          new Node(getRegistry(context), new Node[] {
            new Node(getRegistry(context), getIdentifier("p1")),
            getTypeLiteral(UNDEFINED)
          }),
          getTypeLiteral(VAR_ARGS)
        });

    arrowTypeWithNullReturnType = new ArrowType(getRegistry(context), null, null, false);
    arrowTypeWithNullReturnType.returnType = null;

    arrowTypeWithMismatchedParameters = new ArrowType(getRegistry(context), null, getNativeType(INT), false);
    Node param1 = new Node(getRegistry(context), new Node[] {
      new Node(getRegistry(context), getIdentifier("p1")),
      getTypeLiteral(INT)
    });
    arrowTypeWithMismatchedParameters.parameters = new Node(getRegistry(context), new Node[] {param1, getTypeLiteral(BOOL)});
  }

  @org.junit.Test
  public void testArrowTypeIsSubtype() {
    assertTrue(arrowType.isSubtype(arrowType));
    assertTrue(arrowType.isSubtype(arrowTypeWithInferredReturnType));
    assertTrue(arrowType.isSubtype(arrowTypeWithVarArgs));
    assertTrue(arrowType.isSubtype(arrowTypeWithNullReturnType));
    assertFalse(arrowType.isSubtype(new FunctionType(getRegistry(context), new TypeParameter[0])));
    assertFalse(arrowType.isSubtype(new ArrowType(getRegistry(context), null, getNativeType(INT), false).setReturnTypeInferred(true)));
  }

  @org.junit.Test
  public void testArrowTypeHasEqualParameters() {
    assertTrue(arrowType.hasEqualParameters(arrowType));
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithInferredReturnType));
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithInferredReturnType.setReturnType(null)));
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithVarArgs));
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithNullReturnType));
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithMismatchedParameters));
  }

  @org.junit.Test
  public void testArrowTypeEquivalent() {
    assertEquals(arrowType, arrowType);
    assertEquals(arrowType, arrowTypeWithInferredReturnType);
    assertNotEquals(arrowType, arrowTypeWithVarArgs);
    assertNotEquals(arrowType, arrowTypeWithNullReturnType);
    assertNotEquals(arrowType, arrowTypeWithMismatchedParameters);
    assertNotEquals(arrowType, new FunctionType(getRegistry(context), new TypeParameter[0]));
  }

  @org.junit.Test
  public void testArrowTypeHashCode() {
    assertEquals(arrowType.hashCode(), arrowType.hashCode());
    assertEquals(arrowType.hashCode(), arrowTypeWithInferredReturnType.hashCode());
    assertNotEquals(arrowType.hashCode(), arrowTypeWithVarArgs.hashCode());
    assertNotEquals(arrowType.hashCode(), arrowTypeWithNullReturnType.hashCode());
    assertNotEquals(arrowType.hashCode(), arrowTypeWithMismatchedParameters.hashCode());
    assertNotEquals(arrowType.hashCode(), new FunctionType(getRegistry(context), new TypeParameter[0]).hashCode());
  }

  @org.junit.Test
  public void testArrowTypeGetLeastSupertype() {
    throw new UnsupportedOperationException("Method not supported.");
  }

  @org.junit.Test
  public void testArrowTypeGetGreatestSubtype() {
    throw new UnsupportedOperationException("Method not supported.");
  }

  @org.junit.Test
  public void testArrowTypeToString() {
    assertEquals("function (): ", arrowType.toString());
    assertEquals("function (): ", arrowTypeWithInferredReturnType.toString());
    assertEquals("function (...): ", arrowTypeWithVarArgs.toString());
  }

  @org.junit.Test
  public void testArrowTypeResolve() {
    arrowType.resolve(t, scope);
    arrowTypeWithInferredReturnType.resolve(t, scope);
    arrowTypeWithVarArgs.resolve(t, scope);
    arrowTypeWithNullReturnType.resolve(t, scope);
    arrowTypeWithMismatchedParameters.resolve(t, scope);

    // Check return type resolution
    assertSame(INT, arrowTypeWithInferredReturnType.returnType);
    assertNull(arrowTypeWithNullReturnType.returnType);

    // Check parameter resolution
    assertEquals(INT, arrowType.parameters.getFirstChild().getJSType().getType());
    assertEquals(INT, arrowTypeWithInferredReturnType.parameters.getFirstChild().getJSType().getType());
    assertSame(UNDEFINED, arrowTypeWithVarArgs.parameters.getFirstChild().getJSType());
  }

  @org.junit.Test
  public void testArrowTypeHasUnknownParamsOrReturn() {
    assertTrue(arrowTypeWithUnknownType.hasUnknownParamsOrReturn());
    assertFalse(arrowType.hasUnknownParamsOrReturn());
    assertTrue(arrowTypeWithNullReturnType.hasUnknownParamsOrReturn());
  }
}