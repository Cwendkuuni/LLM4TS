import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowType;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = createJSTypeRegistry();
    errorReporter = new ErrorReporter(System.err);
  }

  // Test for constructor with parameters and return type
  @Test
  public void testArrowTypeConstructor() {
    Node parameters = NodeFactory.createList(
        NodeFactory.createName("a"),
        NodeFactory.createName("b"));
    JSType returnType = NodeFactory.createBooleanType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertEquals(false, arrowType.returnTypeInferred);
  }

  // Test for constructor with only return type
  @Test
  public void testArrowTypeConstructorWithOnlyReturnType() {
    JSType returnType = NodeFactory.createNumberType();
    ArrowType arrowType = new ArrowType(registry, null, returnType);
    assertEquals(ArrowType.class, arrowType.getClass());
    assertNull(arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertEquals(false, arrowType.returnTypeInferred);
  }

  // Test for constructor with parameters, return type and inferred return type
  @Test
  public void testArrowTypeConstructorWithInferredReturnType() {
    Node parameters = NodeFactory.createList(
        NodeFactory.createName("a"),
        NodeFactory.createName("b"));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);
    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(parameters, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertEquals(true, arrowType.returnTypeInferred);
  }

  // Test for isSubtype method
  @Test
  public void testIsSubtype() {
    JSType type1 = NodeFactory.createBooleanType();
    JSType type2 = NodeFactory.createNumberType();
    ArrowType arrowType1 = new ArrowType(registry, null, type1);
    ArrowType arrowType2 = new ArrowType(registry, null, type2);

    assertFalse(arrowType1.isSubtype(arrowType2));

    JSType type3 = NodeFactory.createBooleanType();
    ArrowType arrowType3 = new ArrowType(registry, null, type3);

    assertTrue(arrowType1.isSubtype(arrowType3));

    ArrowType arrowType4 = new ArrowType(registry, null, type1);
    ArrowType arrowType5 = new ArrowType(registry, null, type1);

    assertTrue(arrowType4.isSubtype(arrowType5));
  }

  // Test for hasEqualParameters method with tolerateUnknowns = false
  @Test
  public void testHasEqualParametersWithTolerateUnknownsFalse() {
    Node parameters1 = NodeFactory.createList(
        NodeFactory.createName("a"),
        NodeFactory.createNumberType());
    Node parameters2 = NodeFactory.createList(
        NodeFactory.createName("b"),
        NodeFactory.createBooleanType());

    ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, null);

    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
  }

  // Test for hasEqualParameters method with tolerateUnknowns = true
  @Test
  public void testHasEqualParametersWithTolerateUnknownsTrue() {
    Node parameters1 = NodeFactory.createList(
        NodeFactory.createName("a"),
        NodeFactory.createNumberType());
    Node parameters2 = NodeFactory.createList(
        NodeFactory.createName("b"),
        NodeFactory.getUnknownTypeNode());

    ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
  }

  // Test for checkArrowEquivalenceHelper method
  @Test
  public void testCheckArrowEquivalenceHelper() {
    JSType returnType1 = NodeFactory.createBooleanType();
    JSType returnType2 = NodeFactory.createNumberType();
    ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType2);

    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

    JSType returnType3 = NodeFactory.createBooleanType();
    ArrowType arrowType3 = new ArrowType(registry, null, returnType3);

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType3, false));

    Node parameters1 = NodeFactory.createList(
        NodeFactory.createName("a"),
        NodeFactory.createNumberType());
    Node parameters2 = NodeFactory.createList(
        NodeFactory.createName("b"),
        NodeFactory.createBooleanType());

    ArrowType arrowType4 = new ArrowType(registry, parameters1, null);
    ArrowType arrowType5 = new ArrowType(registry, parameters2, null);

    assertFalse(arrowType4.checkArrowEquivalenceHelper(arrowType5, false));

    ArrowType arrowType6 = new ArrowType(registry, parameters1, null);
    ArrowType arrowType7 = new ArrowType(registry, parameters1, null);

    assertTrue(arrowType6.checkArrowEquivalenceHelper(arrowType7, false));
  }

  // Test for hashCode method
  @Test
  public void testHashCode() {
    JSType returnType1 = NodeFactory.createBooleanType();
    JSType returnType2 = NodeFactory.createNumberType();
    ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType2);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);

    ArrowType arrowType3 = new ArrowType(registry, null, returnType1);

    assertEquals(hashCode1, arrowType3.hashCode());
  }

  // Test for getLeastSupertype method
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSType that = NodeFactory.createStringType();
    ArrowType arrowType = new ArrowType(registry, null, null);
    arrowType.getLeastSupertype(that);
  }

  // Test for getGreatestSubtype method
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSType thisType = NodeFactory.createBooleanType();
    ArrowType arrowType = new ArrowType(registry, null, thisType);
    arrowType.getGreatestSubtype(NodeFactory.getUnknownTypeNode());
  }

  // Test for toStringHelper method
  @Test
  public void testToStringHelper() {
    JSType returnType = NodeFactory.createNumberType();
    ArrowType arrowType = new ArrowType(registry, null, returnType);

    assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }

  // Test for hasAnyTemplateInternal method
  @Test
  public void testHasAnyTemplateInternal() {
    JSType returnType = NodeFactory.createBooleanType();
    ArrowType arrowType = new ArrowType(registry, null, returnType);

    assertFalse(arrowType.hasAnyTemplateInternal());

    Node parameters = NodeFactory.createList(
        NodeFactory.createName("a"),
        NodeFactory.createTemplateExpression(NodeFactory.createIdentifier("T")));

    arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }
}