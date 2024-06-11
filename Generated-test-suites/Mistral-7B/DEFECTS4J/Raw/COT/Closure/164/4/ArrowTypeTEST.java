import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createJSType;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createParametersWithVarArgs;
import static com.google.javascript.rhino.NodeFactory.createTypeName;
import static com.google.javascript.rhino.node.EmptyNode.EMPTY_NODE;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter t;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    registry = createJSTypeRegistry();
    t = ErrorReporter.NULL;
    scope = new StaticScope<>(registry);
  }

  @Test
  public void testArrowTypeConstruction() {
    Node parameters = createParametersWithVarArgs(EMPTY_NODE);
    JSType returnType = createJSType(createTypeName("int"));
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertEquals(false, arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node parameters = createParametersWithVarArgs(EMPTY_NODE);
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(JSTypeNative.UNKNOWN_TYPE, arrowType.returnType);
    assertEquals(true, arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype_ArrowTypeWithSameReturnType() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("int"));
    Node thisParameters = createParametersWithVarArgs(thisParam);
    Node thatParameters = createParametersWithVarArgs(thatParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("int"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    assertTrue(thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithDifferentReturnType() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("string"));
    Node thisParameters = createParametersWithVarArgs(thisParam);
    Node thatParameters = createParametersWithVarArgs(thisParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    assertFalse(thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithDifferentParameters() {
    Node thisParam1 = createNode(createTypeName("int"));
    Node thisParam2 = createNode(createTypeName("string"));
    Node thatParam1 = createNode(createTypeName("int"));
    Node thatParam2 = createNode(createTypeName("boolean"));
    Node thisParameters = createParametersWithVarArgs(thisParam1, thisParam2);
    Node thatParameters = createParametersWithVarArgs(thatParam1, thatParam2);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    assertFalse(thisArrowType.isSubtype(thatArrowType));
  }

  @Test
  public void testHasEqualParameters() {
    Node thisParam1 = createNode(createTypeName("int"));
    Node thatParam1 = createNode(createTypeName("int"));
    Node thisParam2 = createNode(createTypeName("string"));
    Node thatParam2 = createNode(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry,
        createParametersWithVarArgs(thisParam1, thisParam2),
        createJSType(createTypeName("int")));
    ArrowType thatArrowType = new ArrowType(registry,
        createParametersWithVarArgs(thatParam1, thatParam2),
        createJSType(createTypeName("int")));

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType));

    thisArrowType = new ArrowType(registry,
        createParametersWithVarArgs(thisParam1, thisParam2),
        createJSType(createTypeName("int")));
    thatArrowType = new ArrowType(registry,
        createParametersWithVarArgs(thisParam1, createNode(createTypeName("boolean"))),
        createJSType(createTypeName("int")));

    assertFalse(thisArrowType.hasEqualParameters(thatArrowType));
  }

  @Test
  public void testIsEquivalentTo_EqualArrowTypes() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("int"));
    Node thisParameters = createParametersWithVarArgs(thisParam);
    Node thatParameters = createParametersWithVarArgs(thatParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("int"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    assertEquals(thisArrowType, thatArrowType);
  }

  @Test
  public void testIsEquivalentTo_DifferentArrowTypes() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("string"));
    Node thisParameters = createParametersWithVarArgs(thisParam);
    Node thatParameters = createParametersWithVarArgs(thatParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    assertNotEquals(thisArrowType, thatArrowType);
  }

  @Test
  public void testHashCode() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("string"));
    Node thisParameters = createParametersWithVarArgs(thisParam);
    Node thatParameters = createParametersWithVarArgs(thatParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    int thisHashCode = thisArrowType.hashCode();
    int thatHashCode = thatArrowType.hashCode();

    // The order of returnType and returnTypeInferred should not affect the hashcode
    assertEquals(thisHashCode, thatHashCode);

    // Changing the parameters should change the hashcode
    thisArrowType.parameters = createParametersWithVarArgs(thisParam, thisParam);
    assertNotEquals(thisHashCode, thisArrowType.hashCode());

    // Changing the returnType should change the hashcode
    thisArrowType.returnType = createJSType(createTypeName("string"));
    assertNotEquals(thisHashCode, thisArrowType.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("string"));
    Node thisParameters = createParametersWithVarArgs(thisParam);
    Node thatParameters = createParametersWithVarArgs(thatParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    thisArrowType.getLeastSupertype(thatArrowType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("string"));
    Node thisParameters = createParametersWithVarArgs(thisParam);
    Node thatParameters = createParametersWithVarArgs(thatParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    thisArrowType.getGreatestSubtype(thatArrowType);
  }

  @Test
  public void testToString() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("string"));
    Node thisParameters = createParametersWithVarArgs(thisParam);
    Node thatParameters = createParametersWithVarArgs(thatParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    assertEquals("function(int) => int", thisArrowType.toString());
    assertEquals("function(string) => string", thatArrowType.toString());
  }

  @Test
  public void testResolve() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("string"));
    Node thisParameters = createParametersWithVarArgs(thisParam);
    Node thatParameters = createParametersWithVarArgs(thatParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    Node functionNode = createArrowFunction(thisParameters, thisReturnType, EMPTY_NODE);

    thisArrowType.resolve(t, scope);
    assertEquals(thisArrowType, functionNode.getJSType());

    thatArrowType.resolve(t, scope);
    assertEquals(thatArrowType, functionNode.getJSType());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    Node thisParam = createNode(createTypeName("int"));
    Node thatParam = createNode(createTypeName("string"));
    Node thisParameters = createParametersWithVarArgs(thisParam, thisParam);
    Node thatParameters = createParametersWithVarArgs(thatParam, thatParam);

    JSType thisReturnType = createJSType(createTypeName("int"));
    JSType thatReturnType = createJSType(createTypeName("string"));

    ArrowType thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);

    assertTrue(thisArrowType.hasUnknownParamsOrReturn());
    assertTrue(thatArrowType.hasUnknownParamsOrReturn());

    thisArrowType = new ArrowType(registry, thisParameters, thisReturnType);
    thisArrowType.returnType = null;
    assertFalse(thisArrowType.hasUnknownParamsOrReturn());

    thatArrowType = new ArrowType(registry, thatParameters, thatReturnType);
    thatArrowType.parameters = null;
    assertFalse(thatArrowType.hasUnknownParamsOrReturn());
  }
}