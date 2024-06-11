import static com.google.javascript.rhino.NodeFactory.createArrowType;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.testing.Assertions.assertTrue;
import static org.junit.Assert.assertEquals;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.BaseTest;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;

import java.util.Arrays;
import java.util.Collections;

public class ArrowTypeTest extends BaseTest {

  public void testArrowType() {
    JSTypeRegistry registry = createJSTypeRegistry();

    // Test empty arrow type
    ArrowType arrowType1 = (ArrowType) createArrowType(registry, null, null);
    assertTrue(arrowType1.hasUnknownParamsOrReturn());
    assertEquals(UNKNOWN_TYPE, arrowType1.returnType);
    assertEquals(UNKNOWN_TYPE, arrowType1.parameters);

    // Test arrow type with no parameters and a known return type
    ArrowType arrowType2 = (ArrowType) createArrowType(registry, null, getBooleanType());
    assertFalse(arrowType2.hasUnknownParamsOrReturn());
    assertEquals(getBooleanType(), arrowType2.returnType);
    assertEquals(JSType.VOID, arrowType2.parameters);

    // Test arrow type with one parameter and a known return type
    ArrowType arrowType3 = (ArrowType) createArrowType(
        registry, createNode(createNumberLiteral(1)), getBooleanType());
    assertFalse(arrowType3.hasUnknownParamsOrReturn());
    assertEquals(getBooleanType(), arrowType3.returnType);
    assertEquals(getNumberType(1), arrowType3.parameters);

    // Test arrow type with multiple parameters and a known return type
    ArrowType arrowType4 = (ArrowType) createArrowType(
        registry, createNode(createNumberLiteral(1), createStringLiteral("str")),
        getBooleanType());
    assertFalse(arrowType4.hasUnknownParamsOrReturn());
    assertEquals(getBooleanType(), arrowType4.returnType);
    assertEquals(createArrayType(getNumberType(1), getStringType()), arrowType4.parameters);

    // Test arrow type with a known return type and an inferred parameter type
    ArrowType arrowType5 = (ArrowType) createArrowType(registry, null, null, true, getNumberType(1));
    assertFalse(arrowType5.hasUnknownParamsOrReturn());
    assertEquals(UNKNOWN_TYPE, arrowType5.returnType);
    assertEquals(getNumberType(1), arrowType5.parameters);

    // Test arrow type with a known return type and optional parameter
    ArrowType arrowType6 = (ArrowType) createArrowType(registry,
        createNode(createNode(createStringLiteral("str"), isOptionalNode(true))),
        getBooleanType());
    assertFalse(arrowType6.hasUnknownParamsOrReturn());
    assertEquals(getBooleanType(), arrowType6.returnType);
    assertEquals(createNamedType("String", registry), arrowType6.parameters);

    // Test arrow type with a known return type and var-args parameter
    ArrowType arrowType7 = (ArrowType) createArrowType(registry,
        createNode(createNode(createNameNode("arg", true), isVarargsNode(true))),
        getBooleanType());
    assertFalse(arrowType7.hasUnknownParamsOrReturn());
    assertEquals(getBooleanType(), arrowType7.returnType);
    assertEquals(getFunctionType(getVoidType(), getArrayType(getUnknownType(), 1)), arrowType7.parameters);

    // Test arrow type with unknown return type and unknown parameter types
    ArrowType arrowType8 = (ArrowType) createArrowType(registry, null, null, false, null, null);
    assertTrue(arrowType8.hasUnknownParamsOrReturn());
    assertEquals(UNKNOWN_TYPE, arrowType8.returnType);
    assertEquals(UNKNOWN_TYPE, arrowType8.parameters);

    // Test arrow type with unknown return type and known parameter types
    ArrowType arrowType9 = (ArrowType) createArrowType(registry,
        createNode(createNumberLiteral(1), createStringLiteral("str")), null, false);
    assertTrue(arrowType9.hasUnknownParamsOrReturn());
    assertEquals(UNKNOWN_TYPE, arrowType9.returnType);
    assertEquals(createArrayType(getNumberType(1), getStringType()), arrowType9.parameters);

    // Test arrow type with known return type and unknown parameter types
    ArrowType arrowType10 = (ArrowType) createArrowType(registry, null, getBooleanType(), false, null);
    assertTrue(arrowType10.hasUnknownParamsOrReturn());
    assertEquals(getBooleanType(), arrowType10.returnType);
    assertEquals(JSType.VOID, arrowType10.parameters);

    // Test arrow type equivalence
    ArrowType arrowType11 = (ArrowType) createArrowType(registry, null, getBooleanType());
    ArrowType arrowType12 = (ArrowType) createArrowType(registry, null, getBooleanType());
    ArrowType arrowType13 = (ArrowType) createArrowType(registry, createNode(createNumberLiteral(1)), getBooleanType());
    ArrowType arrowType14 = (ArrowType) createArrowType(registry, createNode(createNumberLiteral(2)), getBooleanType());

    assertTrue(arrowType11.isSubtype(arrowType12));
    assertTrue(arrowType12.isSubtype(arrowType11));
    assertTrue(arrowType11.isSubtype(arrowType13));
    assertFalse(arrowType13.isSubtype(arrowType11));
    assertFalse(arrowType11.isSubtype(arrowType14));
    assertFalse(arrowType14.isSubtype(arrowType11));

    TypeEquivalenceTester tester = new TypeEquivalenceTester();
    assertTrue(tester.checkEquivalence(arrowType11, arrowType12, true));
    assertTrue(tester.checkEquivalence(arrowType11, arrowType13, false));
    assertFalse(tester.checkEquivalence(arrowType11, arrowType14, false));

    ArrowType arrowType15 = (ArrowType) createArrowType(registry, createNode(createNameNode("arg"), isVarargsNode(true)), null);
    ArrowType arrowType16 = (ArrowType) createArrowType(registry, createNode(createNameNode("arg"), isVarargsNode(true)), null);
    ArrowType arrowType17 = (ArrowType) createArrowType(registry, createNode(createNameNode("arg1"), isOptionalNode(true)), null);
    ArrowType arrowType18 = (ArrowType) createArrowType(registry, createNode(createNameNode("arg1"), isOptionalNode(true)), null);

    // Test parameter type equivalence
    ArrowType arrowType19 = (ArrowType) createArrowType(registry, null, getNumberType(1));
    ArrowType arrowType20 = (ArrowType) createArrowType(registry, null, getNumberType(2));
    ArrowType arrowType21 = (ArrowType) createArrowType(registry, null, getNumberType(1));

    assertTrue(arrowType19.hasEqualParameters(arrowType21, true));
    assertFalse(arrowType19.hasEqualParameters(arrowType20, true));

    // Test parameter type with optional argument
    assertTrue(arrowType17.hasEqualParameters(arrowType18, true));

    // Test parameter type with var-args
    assertTrue(arrowType15.hasEqualParameters(arrowType16, true));

    // Test arrow type equality
    assertTrue(arrowType15.checkArrowEquivalenceHelper(arrowType16, true));
    assertFalse(arrowType15.checkArrowEquivalenceHelper(arrowType17, true));

    // Test error scenarios for unknown return type
    try {
      ArrowType arrowType22 = (ArrowType) createArrowType(registry, null, null, false, null, createErrorNode());
      fail("Expected error in creating ArrowType with error node as return type.");
    } catch (RuntimeException e) {}

    try {
      ArrowType arrowType23 = (ArrowType) createArrowType(registry, null, null, false, null, createNode(createNameNode("Function"), createNameNode("Function")));
      fail("Expected error in creating ArrowType with Function type as return type.");
    } catch (RuntimeException e) {}

    try {
      ArrowType arrowType24 = (ArrowType) createArrowType(registry, null, null, false, null, createNode(createNameNode("Array"), createUnknownTypeNode()));
      fail("Expected error in creating ArrowType with Array type with unknown type as return type.");
    } catch (RuntimeException e) {}

    // Test error scenarios for unknown parameter types
    try {
      ArrowType arrowType25 = (ArrowType) createArrowType(registry, null, null, false, createErrorNode(), null);
      fail("Expected error in creating ArrowType with error node as parameter type.");
    } catch (RuntimeException e) {}

    try {
      ArrowType arrowType26 = (ArrowType) createArrowType(registry, null, null, false, createNode(createNameNode("Function"), createNameNode("Function")), null);
      fail("Expected error in creating ArrowType with Function type as parameter type.");
    } catch (RuntimeException e) {}

    try {
      ArrowType arrowType27 = (ArrowType) createArrowType(registry, null, null, false, null, createNode(createNameNode("Array"), getUnknownType()));
      fail("Expected error in creating ArrowType with Array type with unknown type as parameter.");
    } catch (RuntimeException e) {}
  }

  private Node isOptionalNode(boolean isOptional) {
    Node name = createNameNode("arg");
    Node optionalNode = createNode(createNameNode("?"), name);
    optionalNode.setOptional(isOptional);
    return optionalNode;
  }

  private Node isVarargsNode(boolean isVarargs) {
    Node name = createNameNode("arg");
    Node varargsNode = createNode(createNameNode("..."), name);
    varargsNode.setVarargs(isVarargs);
    return varargsNode;
  }

  private Node createErrorNode() {
    return createNode(createNameNode("Error"));
  }

  private Node createUnknownTypeNode() {
    return createNode(createNameNode("UnknownType"));
  }

  private Node createNameNode(String name) {
    return createNode(createNameNode("Name"), createStringLiteral(name));
  }

  private JSType getFunctionType(JSType returnType, JSType... parameters) {
    return new FunctionType(createJSTypeRegistry(), null, null, null, returnType, Arrays.asList(parameters));
  }

  private JSType getNativeType(int typeCode) {
    return new JSTypeNative(createJSTypeRegistry(), typeCode);
  }

  private JSType getBooleanType() {
    return registry.getBooleanType();
  }

  private JSType getNumberType(int numberFlags) {
    return registry.getNumberType(numberFlags);
  }

  private JSType getStringType() {
    return registry.getStringType();
  }

  private JSType getArrayType(JSType componentType, int length) {
    return new ArrayType(createJSTypeRegistry(), null, null, null, componentType, length);
  }

  private JSType getVoidType() {
    return registry.getVoidType();
  }

  private JSType getUnknownType() {
    return registry.getUnknownType();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  @Override
  protected void runTest() throws Exception {
    testArrowType();
  }

  @Override
  public String testName() {
    return "ArrowTypeTest";
  }
}