import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createParameters;
import static com.google.javascript.rhino.NodeFactory.createTypeNode;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeNative;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private ErrorReporter errorReporter;
  private JSTypeFactory typeFactory;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    typeFactory = new JSTypeFactory();
    typeRegistry = new JSTypeRegistry(typeFactory);
  }

  @Test
  public void testConstructor_ValidArgs() {
    Node parameters = createParameters(
        createTypeNode(INT),
        createTypeNode(STRING)
    );
    JSType returnType = typeRegistry.createPrimitiveType(INT);
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, returnType);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertEquals(false, arrowType.returnTypeInferred);
  }

  @Test
  public void testConstructor_NullParams() {
    ArrowType arrowType = new ArrowType(typeRegistry, null, null);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testConstructor_NullReturnType() {
    Node parameters = createParameters(createTypeNode(INT));
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, null);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(parameters, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype_ValidCases() {
    JSType arrowType1 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);
    JSType arrowType2 = typeRegistry.createArrowType(createParameters(createTypeNode(INT), createTypeNode(INT)), INT);
    JSType arrowType3 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), STRING);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType2.isSubtype(arrowType3));
  }

  @Test
  public void testIsSubtype_InvalidCases() {
    JSType arrowType1 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);
    JSType arrowType2 = typeRegistry.createArrowType(createParameters(createTypeNode(INT), createTypeNode(INT)), INT);
    JSType arrowType3 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), STRING);
    JSType arrowType4 = typeRegistry.createArrowType(createParameters(createTypeNode(INT), createTypeNode(INT)), BOOL);

    assertFalse(arrowType2.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType3.isSubtype(arrowType1));
    assertFalse(arrowType3.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType1));
    assertFalse(arrowType4.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType4));
  }

  @Test
  public void testHasEqualParameters() {
    Node param1_1 = createParameters(createTypeNode(INT));
    Node param1_2 = createParameters(createTypeNode(INT), createTypeNode(INT));
    Node param2_1 = createParameters(createTypeNode(INT), createTypeNode(INT));
    Node param2_2 = createParameters(createTypeNode(INT));

    ArrowType arrowType1 = new ArrowType(typeRegistry, param1_1, INT);
    ArrowType arrowType2 = new ArrowType(typeRegistry, param1_2, INT);
    ArrowType arrowType3 = new ArrowType(typeRegistry, param2_1, INT);
    ArrowType arrowType4 = new ArrowType(typeRegistry, param2_2, INT);

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType4));
    assertTrue(arrowType2.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType3));
    assertFalse(arrowType2.hasEqualParameters(arrowType4));
    assertTrue(arrowType3.hasEqualParameters(arrowType3));
    assertTrue(arrowType3.hasEqualParameters(arrowType4));
  }

  @Test
  public void testIsEquivalentTo_EqualArgs() {
    JSType arrowType1 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);
    JSType arrowType2 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);

    assertTrue(arrowType1.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType2.isEquivalentTo(arrowType1));
    assertTrue(arrowType2.isEquivalentTo(arrowType2));
  }

  @Test
  public void testIsEquivalentTo_DifferentArgs() {
    JSType arrowType1 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);
    JSType arrowType2 = typeRegistry.createArrowType(createParameters(createTypeNode(INT), createTypeNode(STRING)), INT);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
  }

  @Test
  public void testIsEquivalentTo_Null() {
    JSType arrowType = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);

    assertFalse(arrowType.isEquivalentTo(null));
    assertFalse(null.isEquivalentTo(arrowType));
  }

  @Test
  public void testHashCode_EqualArgs() {
    JSType arrowType1 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);
    JSType arrowType2 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testHashCode_DifferentArgs() {
    JSType arrowType1 = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);
    JSType arrowType2 = typeRegistry.createArrowType(createParameters(createTypeNode(INT), createTypeNode(STRING)), INT);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testGetLeastSupertype_UnsupportedOperationException() {
    JSType arrowType = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);

    try {
      arrowType.getLeastSupertype(createNullType());
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testGetGreatestSubtype_UnsupportedOperationException() {
    JSType arrowType = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);

    try {
      arrowType.getGreatestSubtype(createNullType());
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testToString_ValidArgs() {
    Node parameters = createParameters(createTypeNode(INT), createTypeNode(STRING));
    JSType returnType = INT;
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, returnType);
    String expected = "(function(int, string)) -> int";

    String actual = arrowType.toString();

    assertEquals(expected, actual);
  }

  @Test
  public void testToString_NullParams() {
    ArrowType arrowType = new ArrowType(typeRegistry, null, null);
    String expected = "ArrowType";

    String actual = arrowType.toString();

    assertEquals(expected, actual);
  }

  @Test
  public void testToString_NullReturnType() {
    Node parameters = createParameters(createTypeNode(INT));
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, null);
    String expected = "(function())";

    String actual = arrowType.toString();

    assertEquals(expected, actual);
  }

  @Test
  public void testToString_NoReturnTypeInferred() {
    Node parameters = createParameters(createTypeNode(INT));
    JSType returnType = INT;
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, returnType, false);
    String expected = "(function(" + parameters.toString() + ")) -> int";

    String actual = arrowType.toString();

    assertEquals(expected, actual);
  }

  @Test
  public void testToString_ReturnTypeInferred() {
    Node parameters = createParameters(createTypeNode(INT));
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, null, true);
    String expected = "(function(" + parameters.toString() + "))";

    String actual = arrowType.toString();

    assertEquals(expected, actual);
  }

  @Test
  public void testVisit_VisitorIsNull() {
    ArrowType arrowType = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);

    try {
      arrowType.visit(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testVisit_VisitorIsNotNull() {
    ArrowType arrowType = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);
    Visitor visitor = mock(Visitor.class);

    arrowType.visit(visitor);

    verify(visitor).visitArrowType(arrowType);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes_AlwaysReturnsTrue() {
    JSType arrowType = typeRegistry.createArrowType(createParameters(createTypeNode(INT)), INT);

    BooleanLiteralSet actual = arrowType.getPossibleToBooleanOutcomes();

    assertTrue(actual.contains(BooleanLiteralSet.TRUE));
  }

  @Test
  public void testResolveInternal_ValidArgs() {
    Node parameters = createParameters(createTypeNode(INT), createTypeNode(STRING));
    JSType returnType = createNullType();
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, returnType);

    JSType resolvedArrowType = arrowType.resolveInternal(errorReporter, new StaticScope<>(typeRegistry));

    assertEquals(resolvedArrowType, arrowType);
  }

  @Test
  public void testHasUnknownParamsOrReturn_ValidCase() {
    Node parameters = createParameters(createTypeNode(INT), createTypeNode(INT));
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, INT);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_NullParams() {
    ArrowType arrowType = new ArrowType(typeRegistry, null, null);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_NullReturnType() {
    Node parameters = createParameters(createTypeNode(INT));
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, null);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturn_MixedTypes() {
    Node parameters = createParameters(createTypeNode(INT), createTypeNode(INT), createTypeNode(STRING));
    ArrowType arrowType = new ArrowType(typeRegistry, parameters, INT);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }
}