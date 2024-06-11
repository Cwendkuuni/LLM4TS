import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createFunctionType;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createNumber;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createUnknownType;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.StaticScope;

public class ArrowTypeTest {

  private ArrowType arrowType;
  private ArrowType arrowTypeWithReturnTypeInferred;
  private ArrowType arrowTypeWithUnknownReturnType;
  private ArrowType arrowTypeWithUnknownParamType;
  private ArrowType arrowTypeWithVarArgs;
  private ArrowType arrowTypeWithOptionalParam;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = createJSTypeRegistry();
    Node functionNode = createArrowFunction(createNode(createString("x")),
        createFunctionType(createNode(createString("y")), createUnknownType()));
    arrowType = new ArrowType(registry, functionNode.getParameters(),
        createNumber(), false);

    Node functionNodeWithInferredReturnType = createArrowFunction(
        createNode(createString("x")), createNode(createString("")));
    arrowTypeWithReturnTypeInferred = new ArrowType(registry,
        functionNodeWithInferredReturnType.getParameters(), null, true);

    Node functionNodeWithUnknownReturnType = createArrowFunction(
        createNode(createString("x")), createUnknownType());
    arrowTypeWithUnknownReturnType = new ArrowType(registry,
        functionNodeWithUnknownReturnType.getParameters(), null, false);

    Node functionNodeWithUnknownParamType = createArrowFunction(
        createNode(createUnknownType(), createString("y")),
        createFunctionType(createNode(createString("x")), createUnknownType()));
    arrowTypeWithUnknownParamType = new ArrowType(registry,
        functionNodeWithUnknownParamType.getParameters(),
        createUnknownType(), false);

    Node functionNodeWithVarArgs = createArrowFunction(
        createNode(createString("x")), createFunctionType(createNode(createString("y")), null));
    arrowTypeWithVarArgs = new ArrowType(registry, functionNodeWithVarArgs.getParameters(),
        null, false);

    Node functionNodeWithOptionalParam = createArrowFunction(
        createNode(createString("x"), createNode(createString("y")), createNode(createString("z"))),
        createFunctionType(createNode(createString("x"), createNode(createString("y")), createNode(createString("z"))),
            createUnknownType()));
    arrowTypeWithOptionalParam = new ArrowType(registry, functionNodeWithOptionalParam.getParameters(),
        null, false);
  }

  @Test
  public void testIsSubtype() {
    // arrowType should be a subtype of itself
    assertTrue(arrowType.isSubtype(arrowType));

    // arrowType should not be a subtype of an unknown type
    assertFalse(arrowType.isSubtype(createUnknownType()));

    // arrowType should not be a subtype of a function type with different return type
    assertFalse(arrowType.isSubtype(new FunctionType(createNode(createString("x")), createNumber())));

    // arrowType should be a subtype of a function type with matching return type
    assertTrue(arrowType.isSubtype(new FunctionType(createNode(createString("x")), arrowType.returnType)));

    // arrowType should not be a subtype of an arrow type with var args and different return type
    assertFalse(arrowType.isSubtype(arrowTypeWithVarArgs));

    // arrowTypeWithVarArgs should not be a subtype of arrowType, because it can accept more arguments
    assertFalse(arrowType.isSubtype(arrowTypeWithVarArgs));

    // arrowType should be a subtype of arrowTypeWithOptionalParam, because it accepts the same arguments
    assertTrue(arrowType.isSubtype(arrowTypeWithOptionalParam));

    // arrowType should be a subtype of arrowTypeWithUnknownParamType, because it accepts the same arguments
    assertTrue(arrowType.isSubtype(arrowTypeWithUnknownParamType));

    // arrowTypeWithUnknownParamType should not be a subtype of arrowType, because it expects a type for the first parameter
    assertFalse(arrowType.isSubtype(arrowTypeWithUnknownParamType));

    // arrowTypeWithReturnTypeInferred should be a subtype of arrowType, because it has the same return type
    assertTrue(arrowType.isSubtype(arrowTypeWithReturnTypeInferred));

    // arrowTypeWithReturnTypeInferred should be a subtype of arrowTypeWithUnknownReturnType, because it has a known return type
    assertTrue(arrowTypeWithUnknownReturnType.isSubtype(arrowTypeWithReturnTypeInferred));

    // arrowTypeWithUnknownReturnType should not be a subtype of arrowTypeWithNumberReturnType, because it has an unknown return type
    assertFalse(arrowTypeWithReturnTypeInferred.isSubtype(arrowTypeWithUnknownReturnType));
  }

  @Test
  public void testHasEqualParameters() {
    // arrowType should have equal parameters to arrowType
    assertTrue(arrowType.hasEqualParameters(arrowType, true));

    // arrowTypeWithVarArgs should not have equal parameters to arrowType, because it has more parameters
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithVarArgs, true));

    // arrowTypeWithOptionalParam should have equal parameters to arrowType, because it accepts the same arguments
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithOptionalParam, true));

    // arrowTypeWithOptionalParam should not have equal parameters to arrowTypeWithUnknownParamType, because it has a required parameter
    assertFalse(arrowTypeWithOptionalParam.hasEqualParameters(arrowTypeWithUnknownParamType, true));

    // arrowTypeWithUnknownParamType should have equal parameters to arrowTypeWithUnknownParamType, because they have the same unknown parameters
    assertTrue(arrowTypeWithUnknownParamType.hasEqualParameters(arrowTypeWithUnknownParamType, true));

    // arrowTypeWithUnknownParamType should have equal parameters to arrowType, when tolerating unknowns
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithUnknownParamType, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    // arrowType should be equivalent to arrowType
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowType, true));

    // arrowType should not be equivalent to an unknown type
    assertFalse(arrowType.checkArrowEquivalenceHelper(createUnknownType(), true));

    // arrowType should be equivalent to arrowTypeWithReturnTypeInferred, because they have the same return type
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowTypeWithReturnTypeInferred, true));

    // arrowTypeWithReturnTypeInferred should be equivalent to arrowType, because they have the same return type
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowTypeWithReturnTypeInferred, true));

    // arrowType should not be equivalent to arrowTypeWithUnknownReturnType, because it has a known return type and arrowTypeWithUnknownReturnType has an unknown return type
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowTypeWithUnknownReturnType, true));

    // arrowTypeWithUnknownReturnType should be equivalent to arrowTypeWithNumberReturnType, because they both have an unknown return type
    assertTrue(arrowTypeWithUnknownReturnType.checkArrowEquivalenceHelper(
        new FunctionType(createNode(createString("x")), createNumber()), true));

    // arrowTypeWithOptionalParam should be equivalent to arrowTypeWithOptionalParam, because they have the same parameters
    assertTrue(arrowTypeWithOptionalParam.checkArrowEquivalenceHelper(arrowTypeWithOptionalParam, true));

    // arrowTypeWithOptionalParam should be equivalent to arrowType, when tolerating unknowns
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowTypeWithOptionalParam, false));

    // arrowTypeWithOptionalParam should not be equivalent to arrowTypeWithUnknownParamType, because it has a required parameter and arrowTypeWithUnknownParamType has an unknown parameter
    assertFalse(arrowTypeWithOptionalParam.checkArrowEquivalenceHelper(arrowTypeWithUnknownParamType, true));

    // arrowTypeWithUnknownParamType should not be equivalent to arrowTypeWithNumberParamType, because they have different parameter types
    assertFalse(arrowTypeWithUnknownParamType.checkArrowEquivalenceHelper(
        new FunctionType(createNode(createNumber()), arrowTypeWithUnknownParamType.returnType), true));
  }

  @Test
  public void testHashCode() {
    // arrowType and arrowTypeWithReturnTypeInferred should have the same hash code, because they have the same internal state
    assertEquals(arrowType.hashCode(), arrowTypeWithReturnTypeInferred.hashCode());

    // arrowTypeWithOptionalParam and arrowType should have different hash codes, because they have a different internal state
    assertNotSame(arrowType.hashCode(), arrowTypeWithOptionalParam.hashCode());

    // arrowTypeWithOptionalParam and arrowTypeWithUnknownParamType should have the same hash code, because they have the same internal state
    assertEquals(arrowTypeWithOptionalParam.hashCode(), arrowTypeWithUnknownParamType.hashCode());
  }

  private JSTypeRegistry createJSTypeRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.addBaseType(JSTypeNative.UNKNOWN_TYPE);
    registry.addBaseType(JSTypeNative.NUMBER);
    registry.addBaseType(JSTypeNative.STRING);
    registry.addBaseType(JSTypeNative.BOOLEAN);
    registry.addBaseType(JSTypeNative.NULL);
    registry.addBaseType(JSTypeNative.OBJECT);
    registry.addBaseType(JSTypeNative.FUNCTION_TYPE);
    registry.addBaseType(JSTypeNative.ARROW_TYPE);
    registry.addType(ArrowType.class.getName(), ArrowType.class);
    return registry;
  }
}