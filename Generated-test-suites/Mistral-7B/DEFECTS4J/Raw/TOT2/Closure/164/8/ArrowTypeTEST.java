import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeEquivalence.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.TypeTestingUtils;

import java.util.Arrays;

public class ArrowTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setup() {
    registry = new JSTypeRegistry();
  }

  // Test for constructor with no parameters and no inferred return type
  @org.junit.Test
  public void testArrowTypeWithoutParametersAndWithoutInferredReturnType() {
    Node parameters = Node.nullNode();
    JSType returnType = registry.createBooleanType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("( => boolean )", arrowType.toString());
  }

  // Test for constructor with parameters and inferred return type
  @org.junit.Test
  public void testArrowTypeWithParametersAndInferredReturnType() {
    Node parameters = Node.newArray(
        Node.createName("number"),
        Node.createName("string")
    );
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    assertEquals("( number, string ) =>", arrowType.toString());
  }

  // Test for constructor with parameters and explicit return type
  @org.junit.Test
  public void testArrowTypeWithParametersAndExplicitReturnType() {
    Node parameters = Node.newArray(
        Node.createName("number"),
        Node.createName("string")
    );
    JSType returnType = registry.createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("( number, string ) => number", arrowType.toString());
  }

  // Test for isSubtype method
  @org.junit.Test
  public void testIsSubtype() {
    // Test with the same arrow type
    JSType type1 = new ArrowType(registry, null, null, false);
    JSType type2 = new ArrowType(registry, null, null, false);
    assertTrue(type1.isSubtype(type2));
    assertTrue(type2.isSubtype(type1));

    // Test with different arrow types
    JSType type1a = new ArrowType(registry, null, registry.createNumberType(), false);
    JSType type2a = new ArrowType(registry, null, registry.createBooleanType(), false);
    assertFalse(type1a.isSubtype(type2a));
    assertFalse(type2a.isSubtype(type1a));

    // Test with arrow types of the same functional form but different types
    JSType type1b = new ArrowType(registry, null, registry.createNumberType(), false);
    JSType type2b = new ArrowType(registry, null, registry.createNumberType(), true);
    assertTrue(type1b.isSubtype(type2b));
    assertTrue(type2b.isSubtype(type1b));

    // Test with arrow types of different functional forms
    JSType type1c = new ArrowType(registry, null, registry.createNumberType(), false);
    JSType type2c = new ArrowType(registry, null, registry.createBooleanType(), false);
    assertFalse(type1c.isSubtype(type2c));
    assertFalse(type2c.isSubtype(type1c));

    // Test with arrow types of the same functional form but different number of parameters
    JSType type1d = new ArrowType(registry, Node.newArray(Node.createName("number")), null, false);
    JSType type2d = new ArrowType(registry, null, null, false);
    assertFalse(type1d.isSubtype(type2d));
    assertFalse(type2d.isSubtype(type1d));
  }

  // Test for hasEqualParameters method
  @org.junit.Test
  public void testHasEqualParameters() {
    Node param1 = Node.createName("number");
    Node param2 = Node.createName("number");
    Node param3 = Node.createName("string");

    ArrowType type1 = new ArrowType(registry, Node.newArray(param1, param2), null, false);
    ArrowType type2 = new ArrowType(registry, Node.newArray(param1, param2), null, false);
    ArrowType type3 = new ArrowType(registry, Node.newArray(param1, param3), null, false);
    ArrowType type4 = new ArrowType(registry, Node.newArray(param2, param1), null, false);

    assertTrue(type1.hasEqualParameters(type2));
    assertTrue(type2.hasEqualParameters(type1));
    assertFalse(type1.hasEqualParameters(type3));
    assertFalse(type3.hasEqualParameters(type1));
    assertFalse(type1.hasEqualParameters(type4));
    assertFalse(type4.hasEqualParameters(type1));
  }

  // Test for isEquivalentTo method
  @org.junit.Test
  public void testIsEquivalentTo() {
    // Test with the same arrow type
    JSType type1 = new ArrowType(registry, null, null, false);
    JSType type2 = new ArrowType(registry, null, null, false);
    assertTrue(type1.isEquivalentTo(type2));
    assertTrue(type2.isEquivalentTo(type1));

    // Test with different arrow types
    JSType type1a = new ArrowType(registry, null, registry.createNumberType(), false);
    JSType type2a = new ArrowType(registry, null, registry.createBooleanType(), false);
    assertFalse(type1a.isEquivalentTo(type2a));
    assertFalse(type2a.isEquivalentTo(type1a));

    // Test with arrow types of the same functional form but different types
    JSType type1b = new ArrowType(registry, null, registry.createNumberType(), false);
    JSType type2b = new ArrowType(registry, null, registry.createNumberType(), true);
    assertTrue(type1b.isEquivalentTo(type2b));
    assertTrue(type2b.isEquivalentTo(type1b));

    // Test with arrow types of different functional forms
    JSType type1c = new ArrowType(registry, null, registry.createNumberType(), false);
    JSType type2c = new ArrowType(registry, null, registry.createBooleanType(), false);
    assertFalse(type1c.isEquivalentTo(type2c));
    assertFalse(type2c.isEquivalentTo(type1c));

    // Test with arrow types of the same functional form but different number of parameters
    JSType type1d = new ArrowType(registry, Node.newArray(Node.createName("number")), null, false);
    JSType type2d = new ArrowType(registry, null, null, false);
    assertFalse(type1d.isEquivalentTo(type2d));
    assertFalse(type2d.isEquivalentTo(type1d));
  }

  // Test for hashCode method
  @org.junit.Test
  public void testHashCode() {
    JSType type1 = new ArrowType(registry, null, null, false);
    JSType type2 = new ArrowType(registry, null, null, false);
    JSType type3 = new ArrowType(registry, null, registry.createNumberType(), false);

    assertEquals(type1.hashCode(), type1.hashCode());
    assertEquals(type1.hashCode(), type1.hashCode());
    assertEquals(type1.hashCode(), type1.hashCode());

    assertEquals(type1.hashCode(), type2.hashCode());
    assertEquals(type1.hashCode(), type2.hashCode());

    assertNotEquals(type1.hashCode(), type3.hashCode());
  }

  // Test for getLeastSupertype method
  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSType arrowType = new ArrowType(registry, null, null, false);
    arrowType.getLeastSupertype(registry.createBooleanType());
  }

  // Test for getGreatestSubtype method
  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSType arrowType = new ArrowType(registry, null, null, false);
    arrowType.getGreatestSubtype(registry.createBooleanType());
  }

  // Test for testForEquality method
  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSType arrowType = new ArrowType(registry, null, null, false);
    arrowType.testForEquality(registry.createBooleanType());
  }

  // Test for visit method
  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    JSType arrowType = new ArrowType(registry, null, null, false);
    arrowType.visit(TypeTestingUtils.createStubVisitor());
  }

  // Test for getPossibleToBooleanOutcomes method
  @org.junit.Test
  public void testGetPossibleToBooleanOutcomes() {
    JSType arrowType = new ArrowType(registry, null, null, false);
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  // Test for resolveInternal method with no unknown types
  @org.junit.Test
  public void testResolveInternalWithNoUnknownTypes() {
    JSType arrowType = new ArrowType(registry, null, null, false);
    arrowType.resolveInternal(new ErrorReporter() {}, new StaticScope<>(registry));
  }

  // Test for resolveInternal method with unknown types in parameters
  @org.junit.Test
  public void testResolveInternalWithUnknownParameters() {
    JSType arrowType = new ArrowType(registry, Node.newArray(Node.createName("number")), null, false);
    arrowType.resolveInternal(new ErrorReporter() {}, new StaticScope<>(registry));
    Node param = arrowType.parameters.getFirstChild();
    assertTrue(param.getJSType().isUnknownType());
  }

  // Test for resolveInternal method with unknown return type
  @org.junit.Test
  public void testResolveInternalWithUnknownReturnType() {
    JSType arrowType = new ArrowType(registry, null, null, false);
    arrowType.resolveInternal(new ErrorReporter() {}, new StaticScope<>(registry));
    assertTrue(arrowType.returnType.isUnknownType());
  }

  // Test for resolveInternal method with unknown types in both parameters and return
  @org.junit.Test
  public void testResolveInternalWithUnknownTypes() {
    Node parameters = Node.newArray(
        Node.createName("number"),
        Node.createName("string")
    );
    ArrowType arrowType = new ArrowType(registry, parameters, null, false);
    arrowType.resolveInternal(new ErrorReporter() {}, new StaticScope<>(registry));

    Node thisParam = arrowType.parameters.getFirstChild();
    Node thatParam = arrowType.parameters.getChild(1);
    assertEquals(Arrays.asList(thisParam, thatParam), Arrays.asList(arrowType.parameters.getFirstChild(), arrowType.parameters.getChild(1)));
    assertTrue(thisParam.getJSType().isUnknownType());
    assertTrue(thatParam.getJSType().isUnknownType());

    assertTrue(arrowType.returnType.isUnknownType());
  }
}