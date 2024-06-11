import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createJSType;
import static com.google.javascript.rhino.NodeFactory.createList;
import static com.google.javascript.rhino.NodeFactory.createNativeNode;
import static com.google.javascript.rhino.NodeFactory.createNumberNode;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createThisValue;
import static com.google.javascript.rhino.JSTypeFactory.createArrowType;
import static com.google.javascript.rhino.JSTypeFactory.createFunctionType;
import static com.google.javascript.rhino.JSTypeFactory.createParametersWithVarArgs;
import static com.google.javascript.rhino.JSTypeFactory.createType;
import static com.google.javascript.rhino.JSTypeFactory.createUnknownType;
import static com.google.javascript.rhino.JSTypeRegistry.getInstance;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.JSTestCase;

public class ArrowTypeTest extends JSTestCase {

  private NodeFactory nodeFactory = NodeFactory.getInitial();
  private ErrorReporter errorReporter = new ErrorReporter();
  private StaticScope<JSType> scope;

  @Override
  public void setUp() {
    super.setUp();
    scope = new StaticScope<>(getInstance());
  }

  @Override
  public void tearDown() {
    super.tearDown();
  }

  /**
   * Test constructor with valid arguments.
   */
  @Test
  public void testArrowTypeConstructionValid() {
    Node parameters = createList();
    JSType returnType = createType(createNumberNode(1));
    ArrowType arrowType = new ArrowType(getInstance(), parameters, returnType, false);

    assertEquals(arrowType.getClass(), ArrowType.class);
    assertEquals(arrowType.parameters, parameters);
    assertEquals(arrowType.returnType, returnType);
    assertEquals(arrowType.returnTypeInferred, false);
  }

  /**
   * Test constructor with null arguments.
   */
  @Test
  public void testArrowTypeConstructionNull() {
    Node parameters = null;
    JSType returnType = createType(createNumberNode(1));

    try {
      new ArrowType(getInstance(), parameters, returnType, false);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      assertEquals("parameters is null", e.getMessage());
    }

    // Test constructor with null returnType
    parameters = createList();
    try {
      new ArrowType(getInstance(), parameters, null, false);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      assertEquals("returnType is null", e.getMessage());
    }
  }

  /**
   * Test isSubtype method with equal arrow types.
   */
  @Test
  public void testIsSubtypeEqualArrowTypes() {
    Node parameters1 = createList(createNumberNode(1), createNumberNode(2));
    Node parameters2 = createList(createNumberNode(1), createNumberNode(2));
    JSType returnType1 = createType(createNumberNode(1));
    JSType returnType2 = createType(createNumberNode(1));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters1, returnType1, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters2, returnType2, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  /**
   * Test isSubtype method with different arrow types.
   */
  @Test
  public void testIsSubtypeDifferentArrowTypes() {
    Node parameters1 = createList(createNumberNode(1), createNumberNode(2));
    Node parameters2 = createList(createNumberNode(1));
    JSType returnType1 = createType(createNumberNode(1));
    JSType returnType2 = createType(createStringLiteral("hello"));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters1, returnType1, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters2, returnType2, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  /**
   * Test isSubtype method for arrow types with different parameters.
   */
  @Test
  public void testIsSubtypeDifferentParameters() {
    Node parameters1 = createList(createNumberNode(1), createNumberNode(2));
    Node parameters2 = createList(createNumberNode(1), createNumberNode(3));
    JSType returnType = createType(createNumberNode(1));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters1, returnType, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters2, returnType, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  /**
   * Test isSubtype method for arrow types with different return types.
   */
  @Test
  public void testIsSubtypeDifferentReturnTypes() {
    Node parameters = createList(createNumberNode(1));
    JSType returnType1 = createType(createNumberNode(1));
    JSType returnType2 = createType(createStringLiteral("hello"));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters, returnType1, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters, returnType2, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  /**
   * Test isSubtype method for arrow types with the same return type but vararg
   * parameters.
   */
  @Test
  public void testIsSubtypeWithVarargs() {
    Node parameters1 = createList(createNumberNode(1), createList(createNumberNode(1)));
    Node parameters2 = createList(createNumberNode(1), createList());
    JSType returnType = createType(createNumberNode(1));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters1, returnType, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters2, returnType, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  /**
   * Test hasEqualParameters method with equal arrow types.
   */
  @Test
  public void testHasEqualParametersEqualArrowTypes() {
    Node parameters1 = createList(createNumberNode(1), createNumberNode(2));
    Node parameters2 = createList(createNumberNode(1), createNumberNode(2));
    JSType returnType1 = createType(createNumberNode(1));
    JSType returnType2 = createType(createNumberNode(1));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters1, returnType1, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters2, returnType2, false);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType2.hasEqualParameters(arrowType1));
  }

  /**
   * Test hasEqualParameters method with different arrow types.
   */
  @Test
  public void testHasEqualParametersDifferentArrowTypes() {
    Node parameters1 = createList(createNumberNode(1), createNumberNode(2));
    Node parameters2 = createList(createNumberNode(1), createNumberNode(3));
    JSType returnType = createType(createNumberNode(1));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters1, returnType, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters2, returnType, false);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }

  /**
   * Test hasEqualParameters method for arrow types with different parameters.
   */
  @Test
  public void testHasEqualParametersDifferentParameters() {
    Node parameters1 = createList(createNumberNode(1), createNumberNode(2));
    Node parameters2 = createList(createNumberNode(2), createNumberNode(1));
    JSType returnType = createType(createNumberNode(1));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters1, returnType, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters2, returnType, false);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }

  /**
   * Test isEquivalentTo method with equal arrow types.
   */
  @Test
  public void testIsEquivalentToEqualArrowTypes() {
    Node parameters = createList(createNumberNode(1), createNumberNode(2));
    JSType returnType = createType(createNumberNode(1));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters, returnType, false);

    assertEquals(arrowType1, arrowType2);
  }

  /**
   * Test isEquivalentTo method with different arrow types.
   */
  @Test
  public void testIsEquivalentToDifferentArrowTypes() {
    Node parameters1 = createList(createNumberNode(1), createNumberNode(2));
    Node parameters2 = createList(createNumberNode(1), createNumberNode(3));
    JSType returnType1 = createType(createNumberNode(1));
    JSType returnType2 = createType(createStringLiteral("hello"));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters1, returnType1, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters2, returnType2, false);

    assertNotSame(arrowType1, arrowType2);
  }

  /**
   * Test hashCode method.
   */
  @Test
  public void testHashCode() {
    Node parameters = createList(createNumberNode(1), createNumberNode(2));
    JSType returnType = createType(createNumberNode(1));

    ArrowType arrowType1 = new ArrowType(getInstance(), parameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(getInstance(), parameters, returnType, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);
  }

  /**
   * Test getLeastSupertype method with null arguments.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertypeNull() {
    JSType that = null;
    ArrowType arrowType = new ArrowType(getInstance(), createList(), createUnknownType(), false);
    arrowType.getLeastSupertype(that);
  }

  /**
   * Test getGreatestSubtype method with null arguments.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtypeNull() {
    JSType that = null;
    ArrowType arrowType = new ArrowType(getInstance(), createList(), createUnknownType(), false);
    arrowType.getGreatestSubtype(that);
  }

  /**
   * Test testForEquality method with null arguments.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEqualityNull() {
    JSType that = null;
    ArrowType arrowType = new ArrowType(getInstance(), createList(), createUnknownType(), false);
    arrowType.testForEquality(that);
  }

  /**
   * Test visit method with null visitor.
   */
  @Test(expected = NullPointerException.class)
  public void testVisitNullVisitor() {
    Node node = createArrowFunction(createThisValue(), createList(), createList());
    JSType jstype = new ArrowType(getInstance(), node.getParameters(), createUnknownType(), true);
    Visitor visitor = null;

    jstype.visit(visitor);
  }

  /**
   * Test getPossibleToBooleanOutcomes method with an arrow type.
   */
  @Test
  public void testGetPossibleToBooleanOutcomesArrowType() {
    Node parameters = createList();
    JSType returnType = createUnknownType();
    ArrowType arrowType = new ArrowType(getInstance(), parameters, returnType, false);

    BooleanLiteralSet booleanLiteralSet = arrowType.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.UNDEFINED, booleanLiteralSet);
  }

  /**
   * Test resolveInternal method with null arguments.
   */
  @Test(expected = NullPointerException.class)
  public void testResolveInternalNull() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(getInstance());
    ArrowType arrowType = new ArrowType(getInstance(), createList(), createUnknownType(), false);

    arrowType.resolveInternal(errorReporter, scope);
  }

  /**
   * Test resolveInternal method with valid arguments.
   */
  @Test
  public void testResolveInternalValid() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(getInstance());
    Node parameters = createList(createNumberNode(1));
    JSType returnType = createType(createNumberNode(1));

    ArrowType arrowType = new ArrowType(getInstance(), parameters, returnType, false);
    arrowType.resolveInternal(errorReporter, scope);

    assertEquals(returnType, arrowType.returnType);
  }

  /**
   * Test hasUnknownParamsOrReturn method with an arrow type with unknown parameters
   * and unknown return type.
   */
  @Test
  public void testHasUnknownParamsOrReturnUnknown() {
    Node parameters = createList(createNumberNode(1));
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(getInstance(), parameters, returnType, false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  /**
   * Test hasUnknownParamsOrReturn method with an arrow type with unknown parameters
   * but known return type.
   */
  @Test
  public void testHasUnknownParamsOrReturnKnownReturn() {
    Node parameters = createList(createNumberNode(1));
    JSType returnType = createType(createNumberNode(1));
    ArrowType arrowType = new ArrowType(getInstance(), parameters, returnType, false);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  /**
   * Test hasUnknownParamsOrReturn method with an arrow type with known parameters
   * and unknown return type.
   */
  @Test
  public void testHasUnknownParamsOrReturnUnknownReturn() {
    Node parameters = createList(createNumberNode(1));
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(getInstance(), parameters, returnType, false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  /**
   * Test hasUnknownParamsOrReturn method with an arrow type with known parameters
   * and known return type.
   */
  @Test
  public void testHasUnknownParamsOrReturnKnown() {
    Node parameters = createList(createNumberNode(1));
    JSType returnType = createType(createNumberNode(1));
    ArrowType arrowType = new ArrowType(getInstance(), parameters, returnType, false);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }
}