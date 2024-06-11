import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createParameter;
import static com.google.javascript.rhino.NodeFactory.createTypeLiteral;
import static com.google.javascript.rhino.NodeFactory.createUnknownType;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setup() {
    registry = createJSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testArrowTypeConstructorWithParametersAndReturnType() {
    Node parametersNode = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );
    Node returnTypeNode = createTypeLiteral(String.class);

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnTypeNode);

    assertEquals("ArrowType", arrowType.getClass().getName());
    assertEquals(parametersNode, arrowType.parameters);
    assertEquals(returnTypeNode, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithNoParametersAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    assertEquals("ArrowType", arrowType.getClass().getName());
    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithParametersAndNoReturnType() {
    Node parametersNode = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    ArrowType arrowType = new ArrowType(registry, parametersNode, null);

    assertEquals("ArrowType", arrowType.getClass().getName());
    assertEquals(parametersNode, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtypeSimple() {
    JSType type1 = new ArrowType(registry, null, null);
    JSType type2 = new ArrowType(registry, null, null);

    assertTrue(type1.isSubtype(type2));
    assertTrue(type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtypeDifferentReturnTypes() {
    JSType type1 = new ArrowType(registry, null, createTypeLiteral(int.class));
    JSType type2 = new ArrowType(registry, null, createTypeLiteral(String.class));

    assertFalse(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testIsSubtypeComplex() {
    Node parametersNode1 = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    Node parametersNode2 = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(double.class)),
        NodeFactory.createParameter("b", createTypeLiteral(int.class))
    );

    JSType type1 = new ArrowType(registry, parametersNode1, createTypeLiteral(int.class));
    JSType type2 = new ArrowType(registry, parametersNode2, createTypeLiteral(int.class));

    assertFalse(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testHasEqualParametersSimple() {
    Node parametersNode1 = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    Node parametersNode2 = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testHasEqualParametersDifferentParameters() {
    Node parametersNode1 = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    Node parametersNode2 = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(double.class)),
        NodeFactory.createParameter("b", createTypeLiteral(int.class))
    );

    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, null);
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, null);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testIsEquivalentToSimple() {
    JSType type1 = new ArrowType(registry, null, null);
    JSType type2 = new ArrowType(registry, null, null);

    assertTrue(type1.isEquivalentTo(type2));
    assertTrue(type2.isEquivalentTo(type1));
  }

  @Test
  public void testIsEquivalentToDifferentReturnTypes() {
    JSType type1 = new ArrowType(registry, null, createTypeLiteral(int.class));
    JSType type2 = new ArrowType(registry, null, createTypeLiteral(String.class));

    assertFalse(type1.isEquivalentTo(type2));
    assertFalse(type2.isEquivalentTo(type1));
  }

  @Test
  public void testIsEquivalentToComplex() {
    Node parametersNode1 = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    Node parametersNode2 = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(double.class)),
        NodeFactory.createParameter("b", createTypeLiteral(int.class))
    );

    JSType type1 = new ArrowType(registry, parametersNode1, createTypeLiteral(int.class));
    JSType type2 = new ArrowType(registry, parametersNode2, createTypeLiteral(int.class));

    assertFalse(type1.isEquivalentTo(type2));
    assertFalse(type2.isEquivalentTo(type1));
  }

  @Test
  public void testHashCodeSimple() {
    JSType type1 = new ArrowType(registry, null, null);
    JSType type2 = new ArrowType(registry, null, null);

    assertEquals(type1.hashCode(), type2.hashCode());
  }

  @Test
  public void testHashCodeWithParameters() {
    Node parametersNode = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    ArrowType type1 = new ArrowType(registry, parametersNode, null);
    ArrowType type2 = new ArrowType(registry, parametersNode, null);

    assertEquals(type1.hashCode(), type2.hashCode());
  }

  @Test
  public void testHashCodeWithDifferentReturnTypes() {
    Node parametersNode = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    JSType type1 = new ArrowType(registry, parametersNode, createTypeLiteral(int.class));
    JSType type2 = new ArrowType(registry, parametersNode, createTypeLiteral(String.class));

    assertNotEquals(type1.hashCode(), type2.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSType type1 = new ArrowType(registry, null, null);
    JSType type2 = new ArrowType(registry, null, null);

    type1.getLeastSupertype(type2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSType type1 = new ArrowType(registry, null, null);
    JSType type2 = new ArrowType(registry, null, null);

    type1.getGreatestSubtype(type2);
  }

  @Test
  public void testToString() {
    Node parametersNode = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    ArrowType arrowType = new ArrowType(registry, parametersNode, createTypeLiteral(int.class));

    assertEquals("ArrowType(\n  parameters: ArrowFunctionParameters(function (\n    a: number,\n    b: number\n  ) {\n  }),\n  returnType: int\n)", arrowType.toString());
  }

  @Test
  public void testResolveInternalWithUnknownTypes() {
    JSTypeRegistry registry = createJSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    ArrowType arrowType = new ArrowType(registry, null, null);

    arrowType.resolveInternal(errorReporter, null);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testResolveInternalWithNoUnknownTypes() {
    JSTypeRegistry registry = createJSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    ArrowType arrowType = new ArrowType(registry, null, createTypeLiteral(int.class));

    arrowType.resolveInternal(errorReporter, null);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testIsSubtypeWithUnknownTypes() {
    JSType type1 = new ArrowType(registry, null, null);
    JSType type2 = new ArrowType(registry, null, createTypeLiteral(int.class));

    assertFalse(type1.isSubtype(type2));
  }

  @Test
  public void testIsSubtypeWithKnownTypes() {
    JSType type1 = new ArrowType(registry, null, createTypeLiteral(int.class));
    JSType type2 = new ArrowType(registry, null, createTypeLiteral(int.class));

    assertTrue(type1.isSubtype(type2));
    assertTrue(type2.isSubtype(type1));
  }

  @Test
  public void testToStringHelper() {
    Node parametersNode = NodeFactory.createList(
        NodeFactory.createParameter("a", createTypeLiteral(int.class)),
        NodeFactory.createParameter("b", createTypeLiteral(double.class))
    );

    ArrowType arrowType = new ArrowType(registry, parametersNode, createTypeLiteral(int.class));

    assertEquals("ArrowType", arrowType.toStringHelper(false).getClass().getName());
  }
}