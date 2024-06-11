import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createArrowType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createFunctionType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;

public class ArrowTypeTest {

  @org.junit.Test
  public void testArrowTypeWithParametersAndReturnType() {
    JSTypeRegistry registry = createJSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    Node parameters = Node.newList();
    JSType paramType1 = createJSType(registry, JSTypeNative.INT);
    JSType paramType2 = createJSType(registry, JSTypeNative.STRING);
    parameters.addChild(Node.newNamedNode("param1", paramType1));
    parameters.addChild(Node.newNamedNode("param2", paramType2));

    JSType returnType = createJSType(registry, JSTypeNative.BOOLEAN);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Basic functionality test
    JSType result = arrowType.resolveInternal(t, scope);
    assertTrue(result instanceof ArrowType);

    ArrowType checkedArrowType = (ArrowType) result;
    assertEquals(paramType1, checkedArrowType.getParameters().getFirstChild()
        .getJSType());
    assertEquals(paramType2, checkedArrowType.getParameters().getChild(1)
        .getJSType());
    assertEquals(returnType, checkedArrowType.getReturnType());

    // Test for equal parameters
    JSType that = createArrowType(registry, parameters, returnType);
    assertTrue(arrowType.isEquivalentTo(that));

    // Test for unequal parameters
    Node newParamType1 = Node.newNamedNode("newParamType1", JSTypeNative.INT);
    Node newParameters = Node.newList();
    newParameters.addChild(parameters.getFirstChild());
    newParameters.addChild(newParamType1);
    JSType that2 = new ArrowType(registry, newParameters, returnType);
    assertFalse(arrowType.isEquivalentTo(that2));

    // Test for null parameters
    ArrowType that3 = new ArrowType(registry, null, returnType);
    assertFalse(arrowType.isEquivalentTo(that3));

    // Test for null return type
    ArrowType that4 = new ArrowType(registry, parameters, null);
    assertFalse(arrowType.isEquivalentTo(that4));

    // Test for unknown return type
    ArrowType that5 = new ArrowType(registry, parameters, UNKNOWN_TYPE);
    assertFalse(arrowType.isEquivalentTo(that5));

    // Test for unknown parameters
    ArrowType that6 = new ArrowType(registry, Node.newList(), returnType);
    assertFalse(arrowType.isEquivalentTo(that6));

    // Test for unknown parameters and return type
    ArrowType that7 = new ArrowType(registry, null, UNKNOWN_TYPE);
    assertFalse(arrowType.isEquivalentTo(that7));
  }

  @org.junit.Test
  public void testArrowTypeWithUnknownParametersOrReturn() {
    JSTypeRegistry registry = createJSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    ArrowType arrowType = new ArrowType(registry, null, null);

    // Basic functionality test
    JSType result = arrowType.resolveInternal(t, scope);
    assertTrue(result instanceof ArrowType);
    ArrowType checkedArrowType = (ArrowType) result;
    assertTrue(checkedArrowType.hasUnknownParamsOrReturn());

    // Test for subtype with known parameters and return type
    JSType subType = createArrowType(registry, Node.newList(), JSTypeNative.BOOLEAN);
    assertTrue(arrowType.isSubtype(subType));

    // Test for subtype with unknown return type
    ArrowType subType2 = new ArrowType(registry, Node.newList(), UNKNOWN_TYPE);
    assertTrue(arrowType.isSubtype(subType2));

    // Test for subtype with unknown parameters
    ArrowType subType3 = new ArrowType(registry, Node.newList(), null);
    assertTrue(arrowType.isSubtype(subType3));

    // Test for subtype with unknown parameters and return type
    ArrowType subType4 = new ArrowType(registry, null, null);
    assertTrue(arrowType.isSubtype(subType4));
  }
}