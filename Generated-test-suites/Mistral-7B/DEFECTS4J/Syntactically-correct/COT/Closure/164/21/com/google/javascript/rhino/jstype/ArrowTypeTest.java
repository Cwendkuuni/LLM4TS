import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino. JaneTypes.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeRegistryImpl;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ErrorReporter t;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    t = new ErrorReporter();
    registry = new JSTypeRegistryImpl(t);
  }

  @Test
  public void testArrowTypeConstructor() {
    Node parameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    Node returnType = getNativeType(INT_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    assertEquals("ArrowType{parameters=[a, b], returnType=INT_TYPE, returnTypeInferred=false}",
        arrowType.toString());
  }

  @Test
  public void testArrowTypeConstructorWithInferredReturnType() {
    Node parameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    assertEquals("ArrowType{parameters=[a, b], returnType=UNKNOWN_TYPE, returnTypeInferred=true}",
        arrowType.toString());
  }

  @Test
  public void testArrowTypeWithNullParametersAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    assertEquals("ArrowType{parameters=null, returnType=UNKNOWN_TYPE, returnTypeInferred=false}",
        arrowType.toString());
  }

  @Test
  public void testIsSubtype_SubType() {
    Node thisParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"),
        Node.makeNamedVar("c"));
    Node thatParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));

    JSType thisReturnType = getNativeType(INT_TYPE);
    JSType thatReturnType = getNativeType(STRING_TYPE);

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType, false);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType, false);

    assertTrue(thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testIsSubtype_SuperType() {
    Node thisParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    Node thatParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("c"));

    JSType thisReturnType = getNativeType(INT_TYPE);
    JSType thatReturnType = getNativeType(STRING_TYPE);

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType, false);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType, false);

    assertFalse(thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testIsSubtype_IncompatibleReturnTypes() {
    Node thisParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));

    JSType thisReturnType = getNativeType(INT_TYPE);
    JSType thatReturnType = getNativeType(FLOAT_TYPE);

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType, false);
    ArrowType thatArrowType = new ArrowType(registry, thisParameters, thatReturnType, false);

    assertFalse(thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testHasEqualParameters_Equal() {
    Node thisParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"),
        Node.makeNamedVar("c"));
    Node thatParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"),
        Node.makeNamedVar("c"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, null, false);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, null, false);

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType));
  }

  @Test
  public void testHasEqualParameters_NotEqual() {
    Node thisParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    Node thatParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("c"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, null, false);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, null, false);

    assertFalse(thisArrowType.hasEqualParameters(thatArrowType));
  }

  @Test
  public void testIsEquivalentTo_Equal() {
    Node thisParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"),
        Node.makeNamedVar("c"));
    Node thatParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"),
        Node.makeNamedVar("c"));

    JSType thisReturnType = getNativeType(INT_TYPE);
    JSType thatReturnType = getNativeType(INT_TYPE);

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType, false);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType, false);

    assertEquals(thisArrowType, thatArrowType);
  }

  @Test
  public void testIsEquivalentTo_NotEqual() {
    Node thisParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    Node thatParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("c"));

    JSType thisReturnType = getNativeType(INT_TYPE);
    JSType thatReturnType = getNativeType(FLOAT_TYPE);

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType, false);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType, false);

    assertNotEquals(thisArrowType, thatArrowType);
  }

  @Test
  public void testHashCode_Equal() {
    Node thisParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"),
        Node.makeNamedVar("c"));
    Node thatParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"),
        Node.makeNamedVar("c"));

    JSType thisReturnType = getNativeType(INT_TYPE);
    JSType thatReturnType = getNativeType(INT_TYPE);

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType, false);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType, false);

    assertEquals(thisArrowType.hashCode(), thatArrowType.hashCode());
  }

  @Test
  public void testHashCode_Different() {
    Node thisParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    Node thatParameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("c"));

    JSType thisReturnType = getNativeType(INT_TYPE);
    JSType thatReturnType = getNativeType(FLOAT_TYPE);

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType, false);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType, false);

    assertNotEquals(thisArrowType.hashCode(), thatArrowType.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new RuntimeException("Method getLeastSupertype is not implemented.");
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new RuntimeException("Method getGreatestSubtype is not implemented.");
  }

  @Test
  public void testToString() {
    Node parameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    Node returnType = getNativeType(INT_TYPE);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    assertEquals("ArrowType{parameters=[a, b], returnType=INT_TYPE, returnTypeInferred=false}",
        arrowType.toString());
  }

  @Test
  public void testToStringForAnnotations() {
    Node parameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    Node returnType = getNativeType(INT_TYPE);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    assertEquals("ArrowType", arrowType.toStringForAnnotations());
  }

  @Test
  public void testResolveInternal_EmptyParametersAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    JSType resolvedArrowType = arrowType.resolveInternal(t, new StaticScope<JSType>(registry));

    assertTrue(resolvedArrowType instanceof ArrowType);
    assertEquals(UNKNOWN_TYPE, resolvedArrowType.getType());
  }

  @Test
  public void testResolveInternal_NullParametersAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    JSType resolvedArrowType = arrowType.resolveInternal(t, new StaticScope<JSType>(registry));

    assertTrue(resolvedArrowType instanceof ArrowType);
    assertNull(resolvedArrowType.getParameters());
    assertNull(resolvedArrowType.getReturnType());
  }

  @Test
  public void testResolveInternal_KnownParametersAndReturnType() {
    Node parameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    Node returnType = getNativeType(INT_TYPE);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    JSType resolvedArrowType = arrowType.resolveInternal(t, new StaticScope<JSType>(registry));

    assertTrue(resolvedArrowType instanceof ArrowType);
    assertEquals(parameters, resolvedArrowType.getParameters());
    assertEquals(returnType, resolvedArrowType.getReturnType());
  }

  @Test
  public void testResolveInternal_InferredReturnType() {
    Node parameters = Node.makeList(Node.makeNamedVar("a"), Node.makeNamedVar("b"));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    JSType resolvedArrowType = arrowType.resolveInternal(t, new StaticScope<JSType>(registry));

    assertTrue(resolvedArrowType instanceof ArrowType);
    assertEquals(parameters, resolvedArrowType.getParameters());
    assertNotNull(resolvedArrowType.getReturnType());
    assertTrue(resolvedArrowType.getReturnType().isUnknownType());
  }

  @Test
  public void testResolveInternal_UnknownParametersOrReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    JSType resolvedArrowType = arrowType.resolveInternal(t, new StaticScope<JSType>(registry));

    assertTrue(resolvedArrowType instanceof ArrowType);
    assertTrue(resolvedArrowType.hasUnknownParamsOrReturn());
  }
}