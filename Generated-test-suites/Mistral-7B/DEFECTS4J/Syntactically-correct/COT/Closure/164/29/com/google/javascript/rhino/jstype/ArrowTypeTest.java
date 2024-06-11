import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.jstype.JSType.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    JSType returnType = getNativeType(INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);
    assertEquals("Expected type to correctly instantiate ArrowType",
        ArrowType.class, arrowType.getClass());
  }

  @Test
  public void testArrowTypeCreationWithNullParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    assertEquals("Expected type to correctly instantiate ArrowType with null parameters and return type",
        ArrowType.class, arrowType.getClass());
  }

  @Test
  public void testArrowTypeCreationWithVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList(VAR_ARGS);
    JSType returnType = getNativeType(INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);
    assertEquals("Expected type to correctly instantiate ArrowType with var args",
        ArrowType.class, arrowType.getClass());
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    JSType returnType = UNKNOWN_TYPE;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, true);
    assertEquals("Expected type to correctly instantiate ArrowType with inferred return type",
        ArrowType.class, arrowType.getClass());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeRegistry otherRegistry = new JSTypeRegistry();
    Node thisParam = Node.newIdentifier("p1");
    Node thatParam = Node.newIdentifier("p2");
    Node thisReturn = Node.newIdentifier("r");
    Node thatReturn = Node.newIdentifier("r");

    JSType thisType = new ArrowType(registry, Node.newList(thisParam),
        thisReturn, false);
    JSType thatType = new ArrowType(otherRegistry, Node.newList(thatParam),
        thatReturn, false);

    assertTrue("Expected this type to be a subtype of itself",
        thisType.isSubtype(thisType));

    // Function types should be contravariant in their parameters
    // and covariant in their return types.

    // This should be a subtype of that, because the return type is a
    // supertype.
    assertTrue("Expected this to be a subtype of that with the same return type",
        thisType.isSubtype(thatType));

    // This should *not* be a subtype of that, because the parameter types
    // are not a supertype.
    thisParam.setJSType(INT);
    thatParam.setJSType(STRING);
    assertFalse("Expected this to not be a subtype of that with different parameter types",
        thisType.isSubtype(thatType));

    // This should be a subtype of that, because it has an additional, optional
    // parameter.
    Node thisParamWithVarArgs = Node.newList(thisParam, VAR_ARGS);
    Node thatParamWithVarArgs = Node.newList(thatParam, VAR_ARGS);
    ArrowType thisTypeWithVarArgs = new ArrowType(registry,
        thisParamWithVarArgs, thisReturn, false);
    ArrowType thatTypeWithVarArgs = new ArrowType(otherRegistry,
        thatParamWithVarArgs, thatReturn, false);
    assertTrue("Expected this with varargs to be a subtype of that with varargs",
        thisTypeWithVarArgs.isSubtype(thatTypeWithVarArgs));

    // This should *not* be a subtype of that, because it has a required
    // parameter that is not present in that.
    assertFalse("Expected this to not be a subtype of that with missing required parameter",
        thisType.isSubtype(thatTypeWithVarArgs));

    // This should be a subtype of that, because all of its parameters are
    // subtypes of the corresponding parameters in that.
    JSType thisParamType = INT;
    JSType thatParamType = STRING;
    JSType thisParamSubType = getNativeType(INT);
    JSType thatParamSubType = getNativeType(STRING);
    thisParam.setJSType(thisParamType);
    thatParam.setJSType(thatParamType);
    thisParamType.setSubtype(thisParamSubType);
    thatParamType.setSubtype(thatParamSubType);
    assertTrue("Expected this to be a subtype of that with subtype parameters",
        thisType.isSubtype(thatTypeWithVarArgs));

    // This should not be a subtype of that, because it has a required
    // parameter that is not present in that.
    thisParamType = getNativeType(INT);
    thisParam.setJSType(thisParamType);
    thisParamType.setSubtype(Node.NULL_TYPE);
    assertFalse("Expected this to not be a subtype of that with missing required parameter",
        thisType.isSubtype(thatTypeWithVarArgs));

    // This should be a subtype of that, because all of its parameters are
    // subtypes of the corresponding parameters in that, and its return type is
    // a supertype.
    JSType thisReturnType = STRING;
    thisReturn.setJSType(thisReturnType);
    thisReturnType.setSubtype(thatReturnType);
    assertTrue("Expected this to be a subtype of that with subtype return type",
        thisTypeWithVarArgs.isSubtype(thatType));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node thisParam1 = Node.newIdentifier("p1");
    Node thatParam1 = Node.newIdentifier("p1");
    Node thisParam2 = Node.newIdentifier("p2");
    Node thatParam2 = Node.newIdentifier("p2");
    Node thisParam3 = Node.newIdentifier("p3");
    Node thatParam3 = Node.newIdentifier("p3");
    JSType thisType = new ArrowType(registry, Node.newList(thisParam1, thisParam2, thisParam3),
        getNativeType(INT), false);
    JSType thatType = new ArrowType(registry, Node.newList(thatParam1, thatParam2, thatParam3),
        getNativeType(INT), false);
    assertTrue("Expected this to have equal parameters to that",
        thisType.hasEqualParameters(thatType));
  }

  @Test
  public void testArrowTypeHasUnequalParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node thisParam1 = Node.newIdentifier("p1");
    Node thatParam1 = Node.newIdentifier("p2");
    Node thisParam2 = Node.newIdentifier("p2");
    Node thatParam2 = Node.newIdentifier("p3");
    JSType thisType = new ArrowType(registry, Node.newList(thisParam1, thisParam2),
        getNativeType(INT), false);
    JSType thatType = new ArrowType(registry, Node.newList(thatParam1, thatParam2),
        getNativeType(INT), false);
    assertFalse("Expected this to not have equal parameters to that",
        thisType.hasEqualParameters(thatType));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node thisParam1 = Node.newIdentifier("p1");
    Node thisParam2 = Node.newIdentifier("p2");
    Node thisReturn = Node.newIdentifier("r");
    Node thatParam1 = Node.newIdentifier("p1");
    Node thatParam2 = Node.newIdentifier("p3");
    Node thatReturn = Node.newIdentifier("r");
    JSType thisType = new ArrowType(registry, Node.newList(thisParam1, thisParam2),
        thisReturn, false);
    JSType thatType = new ArrowType(registry, Node.newList(thatParam1, thatParam2),
        thatReturn, false);

    int thisHashCode = thisType.hashCode();
    int thatHashCode = thatType.hashCode();

    // Changing the return type should change the hash code.
    thisReturn.setJSType(STRING);
    int thisHashCode2 = thisType.hashCode();
    assertNotEquals("Expected changed return type to have different hash codes",
        thisHashCode, thisHashCode2);
    assertNotEquals("Expected changed return type to have different hash codes",
        thisHashCode, thatHashCode);

    // Changing the parameter types should change the hash code.
    thisParam1.setJSType(INT);
    int thisHashCode3 = thisType.hashCode();
    assertNotEquals("Expected changed parameter type to have different hash codes",
        thisHashCode, thisHashCode3);
    assertNotEquals("Expected changed parameter type to have different hash codes",
        thisHashCode, thisHashCode2);
    assertNotEquals("Expected changed parameter type to have different hash codes",
        thisHashCode, thatHashCode);

    // Changing both the return type and a parameter type should change the hash code.
    thisReturn.setJSType(STRING);
    thisParam1.setJSType(LONG);
    int thisHashCode4 = thisType.hashCode();
    assertNotEquals("Expected changed return and parameter types to have different hash codes",
        thisHashCode, thisHashCode4);
    assertNotEquals("Expected changed return and parameter types to have different hash codes",
        thisHashCode, thisHashCode3);
    assertNotEquals("Expected changed return and parameter types to have different hash codes",
        thisHashCode, thisHashCode2);
    assertNotEquals("Expected changed return and parameter types to have different hash codes",
        thisHashCode, thatHashCode);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType thisType = new ArrowType(registry, Node.newList(), getNativeType(INT), false);
    JSType thatType = getNativeType(STRING);
    thisType.getLeastSupertype(thatType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType thisType = new ArrowType(registry, Node.newList(), getNativeType(INT), false);
    JSType thatType = getNativeType(STRING);
    thisType.getGreatestSubtype(thatType);
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node thisParam1 = Node.newIdentifier("p1");
    Node thisParam2 = Node.newIdentifier("p2");
    Node thisReturn = Node.newIdentifier("r");
    JSType thisType = new ArrowType(registry, Node.newList(thisParam1, thisParam2),
        thisReturn, false);
    assertEquals("ExpectedtoString to produce correct representation of ArrowType",
        "ArrowType[p1: Any, p2: Any] -> Any", thisType.toString());
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    ArrowType arrowType = new ArrowType(registry, parameters, null, false);
    assertTrue("Expected arrow type to have unknown parameters or return type",
        arrowType.hasUnknownParamsOrReturn());

    parameters.setJSType(Node.newList(Node.newIdentifier("p1")));
    arrowType = new ArrowType(registry, parameters, null, false);
    assertTrue("Expected arrow type to have unknown parameters or return type",
        arrowType.hasUnknownParamsOrReturn());

    parameters.setJSType(Node.NULL_TYPE);
    arrowType = new ArrowType(registry, parameters, null, false);
    assertTrue("Expected arrow type to have unknown parameters or return type",
        arrowType.hasUnknownParamsOrReturn());

    arrowType = new ArrowType(registry, parameters, getNativeType(INT), false);
    assertFalse("Expected arrow type to no longer have unknown parameters or return type",
        arrowType.hasUnknownParamsOrReturn());

    arrowType = new ArrowType(registry, parameters, null, true);
    assertTrue("Expected arrow type to have unknown parameters or return type",
        arrowType.hasUnknownParamsOrReturn());
  }

  private JSType getNativeType(JSType type) {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setNativeType(type.getName(), JSTypeNative.getNativeType(type.getName()));
    return registry.getNativeType(type.getName());
  }

  private ErrorReporter createErrorReporter() {
    return new ErrorReporter() {
      @Override
      public void error(String message, int line, int column) {
        fail("Unexpected error: " + message);
      }

      @Override
      public void warning(String message, int line, int column) {
        // Ignore
      }
    };
  }

  private StaticScope<JSType> createScope() {
    return new StaticScope<JSType>() {
      @Override
      public JSType getType(String name, int line) {
        return getNativeType(JSType.createUnknownType(name, line));
      }

      @Override
      public void define(String name, JSType type, int line) {
        // Ignore
      }
    };
  }
}