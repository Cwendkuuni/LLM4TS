import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createArrowType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createFunctionType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createNativeType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createUndefinedType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createVoidType;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeRegistry.JSTypeRegistryBuilder;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    registry = new JSTypeRegistryBuilder().build();
    nodeFactory = new NodeFactory(registry);
  }

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = nodeFactory.createList();
    JSType returnType = createNativeType( registry, "Number" );
    ArrowType arrowType = new ArrowType( registry, parameters, returnType );

    assertNotNull(arrowType);
    assertEquals("ArrowType", arrowType.getName());
    assertEquals(1, arrowType.getArity());
    assertEquals(parameters, arrowType.getParameters());
    assertEquals(returnType, arrowType.getReturnType());
    assertFalse(arrowType.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = nodeFactory.createList();
    ArrowType arrowType = new ArrowType( registry, parameters, null, true );

    assertNotNull(arrowType);
    assertEquals("ArrowType", arrowType.getName());
    assertEquals(1, arrowType.getArity());
    assertEquals(parameters, arrowType.getParameters());
    assertNull(arrowType.getReturnType());
    assertTrue(arrowType.isReturnTypeInferred());
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters1 = nodeFactory.createList();
    Node parameters2 = nodeFactory.createList();
    JSType returnType1 = createNativeType( registry, "Number" );
    JSType returnType2 = createNativeType( registry, "String" );
    ArrowType arrowType1 = new ArrowType( registry, parameters1, returnType1 );
    ArrowType arrowType2 = new ArrowType( registry, parameters2, returnType2 );

    // arrowType1 is a subtype of arrowType2 when both have the same type for return type
    assertTrue(arrowType1.isSubtype(arrowType2));

    // arrowType1 is not a subtype of arrowType2 when return types are different
    assertFalse(arrowType1.isSubtype(arrowType2));

    // arrowType1 is a subtype of arrowType2 when arrowType2 has a null return type
    ArrowType arrowType2WithNullReturn = new ArrowType( registry, parameters2, null );
    assertTrue(arrowType1.isSubtype(arrowType2WithNullReturn));

    // arrowType1 is a subtype of arrowType2 when arrowType1's return type is inferred
    ArrowType arrowType1WithInferredReturn = new ArrowType( registry, parameters1, null, true );
    assertTrue(arrowType1.isSubtype(arrowType2));

    // arrowType1 is not a subtype of arrowType2 when arrowType2 has more parameters
    Node extraParameter = nodeFactory.createIdentifier("extra");
    ArrowType arrowType2WithExtraParameter = new ArrowType( registry, nodeFactory.createList(parameters2, extraParameter), returnType2 );
    assertFalse(arrowType1.isSubtype(arrowType2WithExtraParameter));

    // arrowType1 is a subtype of arrowType2 when arrowType2 has fewer parameters
    ArrowType arrowType2WithFewerParameters = new ArrowType( registry, nodeFactory.createList(parameters2.getFirstChild()), returnType2 );
    assertTrue(arrowType1.isSubtype(arrowType2WithFewerParameters));

    // arrowType1 is not a subtype of arrowType2 when arrowType2's parameter type is not a subtype of arrowType1's parameter type
    JSType parameterType1 = parameters1.getFirstChild().getJSType();
    Node parameterNode2 = parameters2.getFirstChild();
    JSType parameterType2 = parameterNode2.getJSType();
    JSType parameterType3 = createNativeType( registry, "String" );
    parameterNode2.setJSType(parameterType3);
    assertFalse(arrowType1.isSubtype(arrowType2));

    // arrowType1 is a subtype of arrowType2 when arrowType2's parameter type is a supertype of arrowType1's parameter type (covariance)
    JSType parameterType1Super = createFunctionType( registry, nodeFactory.createList(createUndefinedType()), returnType1 );
    ArrowType arrowType1WithSuperParameter = new ArrowType( registry, parameters1, parameterType1Super );
    assertTrue(arrowType1.isSubtype(arrowType1WithSuperParameter));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters1 = nodeFactory.createList();
    Node parameters2 = nodeFactory.createList();
    JSType parameterType1 = createNativeType( registry, "Number" );
    JSType parameterType2 = createNativeType( registry, "String" );
    ArrowType arrowType1 = new ArrowType( registry, nodeFactory.createList(parameters1, nodeFactory.createIdentifier("num")), parameterType1 );
    ArrowType arrowType2 = new ArrowType( registry, nodeFactory.createList(parameters2, nodeFactory.createIdentifier("str")), parameterType2 );
    ArrowType arrowType3 = new ArrowType( registry, nodeFactory.createList(parameters2, nodeFactory.createIdentifier("num")), parameterType1 );
    ArrowType arrowType4 = new ArrowType( registry, nodeFactory.createList(parameters1, nodeFactory.createIdentifier("str")), parameterType2 );

    // arrowType1 and arrowType2 have different parameters, so they don't have equal parameters
    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));

    // arrowType1 and arrowType3 have equal parameters, because the same types can be swapped between arrow types
    assertTrue(arrowType1.hasEqualParameters(arrowType3, false));

    // arrowType1 and arrowType4 have different parameters, because the parameter names are different
    assertFalse(arrowType1.hasEqualParameters(arrowType4, false));

    // arrowType1 and arrowType3 have equal parameters when tolerating unknowns
    assertTrue(arrowType1.hasEqualParameters(arrowType3, true));

    // arrowType1 and arrowType4 have equal parameters when tolerating unknowns
    assertTrue(arrowType1.hasEqualParameters(arrowType4, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters1 = nodeFactory.createList();
    Node parameters2 = nodeFactory.createList();
    JSType returnType1 = createNativeType( registry, "Number" );
    JSType returnType2 = createNativeType( registry, "String" );
    ArrowType arrowType1 = new ArrowType( registry, parameters1, returnType1 );
    ArrowType arrowType2 = new ArrowType( registry, parameters2, returnType2 );

    // arrowType1 and arrowType2 have different return types, so they are not equivalent
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

    // arrowType1 and arrowType2 have equal return types, so they are equivalent
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

    // arrowType1 and arrowType2 have equal return types, but arrowType2 has inferred return type,
    // so they are not equivalent
    ArrowType arrowType2WithInferredReturn = new ArrowType( registry, parameters2, null, true );
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2WithInferredReturn, false));

    // arrowType1 and arrowType2 have equal return types, and arrowType2 has inferred return type,
    // but we are tolerating unknowns, so they are equivalent
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2WithInferredReturn, true));

    // arrowType1 and arrowType2 have different parameter types, so they are not equivalent
    JSType parameterType1 = parameters1.getFirstChild().getJSType();
    JSType parameterType2 = parameters2.getFirstChild().getJSType();
    JSType parameterType3 = createNativeType( registry, "String" );
    parameters2.getFirstChild().setJSType(parameterType3);
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

    // arrowType1 and arrowType2 have different parameter types, but we are tolerating unknowns,
    // so they are equivalent
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));

    // arrowType1 and arrowType3 have equal parameters, but different return types, so they are not equivalent
    ArrowType arrowType3 = new ArrowType( registry, parameters1, returnType2 );
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, false));

    // arrowType1 and arrowType3 have equal parameters and equal return types, so they are equivalent
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType3, false));

    // arrowType1 and arrowType4 have different arities, so they are not equivalent
    Node parameters4 = nodeFactory.createList(parameters1.getFirstChild(), parameters1.getFirstChild(), nodeFactory.createIdentifier("str"));
    ArrowType arrowType4 = new ArrowType( registry, parameters4, returnType1 );
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType4, false));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = nodeFactory.createList();
    JSType returnType1 = createNativeType( registry, "Number" );
    JSType returnType2 = createNativeType( registry, "String" );
    ArrowType arrowType1 = new ArrowType( registry, parameters, returnType1 );
    ArrowType arrowType2 = new ArrowType( registry, parameters, returnType2 );

    int arrowType1HashCode = arrowType1.hashCode();
    int arrowType2HashCode = arrowType2.hashCode();

    // when return types are equal, hash codes should be equal
    assertEquals(arrowType1HashCode, arrowType2.hashCode());

    // when return types are not equal, hash codes should be different
    ArrowType arrowType3 = new ArrowType( registry, parameters, createVoidType() );
    assertNotEquals(arrowType1HashCode, arrowType3.hashCode());
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = nodeFactory.createList();
    JSType returnType = createNativeType( registry, "Number" );
    ArrowType arrowType = new ArrowType( registry, parameters, returnType );

    String arrowTypeString = arrowType.toString();

    // the string representation of arrowType should include the parameters and return type
    String expectedArrowTypeString = "[ArrowType <";
    expectedArrowTypeString += parameters.toString();
    expectedArrowTypeString += ">: Number]";
    assertEquals(expectedArrowTypeString, arrowTypeString);
  }

  private JSTypeRegistry createJSTypeRegistry() {
    return createJSTypeRegistry(null);
  }

  private JSTypeRegistry createJSTypeRegistry(ErrorReporter errorReporter) {
    return new JSTypeRegistryBuilder().setErrorReporter(errorReporter).build();
  }
}