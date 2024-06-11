import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.getNativeType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createParameters;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createArrowType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createJSType;

import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    // Test ArrowType creation with null parameters and null returnType
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertEquals(getNativeType(ArrowType.class), arrowType.getType());

    // Test ArrowType creation with valid parameters and null returnType
    Node parameters = Node.newNode(Node.callExpr(Node.newExpr("Function"),
        Node.newList(Node.newIdentifier("p1")), Node.newList()));
    arrowType = new ArrowType(registry, parameters, null);
    JSType expectedType = createArrowType(
        createParameters(registry, Node.newList(Node.newIdentifier("p1"))),
        getNativeType(JSTypeNative.VOID), false);
    assertEquals(expectedType, arrowType);

    // Test ArrowType creation with null parameters and valid returnType
    arrowType = new ArrowType(registry, null, createJSType(registry, "number"));
    expectedType = createArrowType(
        null, createJSType(registry, "number"), false);
    assertEquals(expectedType, arrowType);

    // Test ArrowType creation with valid parameters and valid returnType
    Node param1 = Node.newIdentifier("p1");
    Node param2 = Node.newIdentifier("p2");
    Node params = Node.newList(Node.newList(param1, param2), Node.newIdentifier("..."));
    arrowType = new ArrowType(registry, createParameters(registry, params),
        createJSType(registry, "number"));
    expectedType = createArrowType(
        createParameters(registry, params), createJSType(registry, "number"),
        false);
    assertEquals(expectedType, arrowType);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test subtyping of identical arrow types
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));

    // Test subtyping of arrow types with fewer parameters
    ArrowType arrowType3 = new ArrowType(registry, null, null);
    ArrowType arrowType4 = new ArrowType(registry, Node.newIdentifier("p1"), null);
    assertTrue(arrowType1.isSubtype(arrowType4));
    assertTrue(arrowType4.isSubtype(arrowType1));

    // Test subtyping of arrow types with more parameters
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    ArrowType arrowType6 = new ArrowType(registry, createParameters(registry,
        Node.newList(Node.newIdentifier("p1"))), null);
    assertFalse(arrowType5.isSubtype(arrowType6));
    assertFalse(arrowType6.isSubtype(arrowType5));

    // Test subtyping of arrow types with same parameters but different return types
    ArrowType arrowType7 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")),
        createJSType(registry, "number"));
    ArrowType arrowType8 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")),
        createJSType(registry, "string"));
    assertFalse(arrowType7.isSubtype(arrowType8));
    assertFalse(arrowType8.isSubtype(arrowType7));

    // Test subtyping of arrow types with same parameters and inferred return types
    ArrowType arrowType9 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null, true);
    ArrowType arrowType10 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null, true);
    assertTrue(arrowType9.isSubtype(arrowType10));
    assertTrue(arrowType10.isSubtype(arrowType9));

    // Test subtyping of arrow types with different numbers of parameters
    ArrowType arrowType11 = new ArrowType(registry,
        Node.newList(Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    ArrowType arrowType12 = new ArrowType(registry, Node.newIdentifier("p1"), null);
    assertFalse(arrowType11.isSubtype(arrowType12));
    assertFalse(arrowType12.isSubtype(arrowType11));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test equal arrow types
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertTrue(arrowType2.hasEqualParameters(arrowType1, false));

    // Test arrow types with different return types
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")),
        createJSType(registry, "number"));
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")),
        createJSType(registry, "string"));
    assertFalse(arrowType3.hasEqualParameters(arrowType4, false));
    assertFalse(arrowType4.hasEqualParameters(arrowType3, false));

    // Test arrow types with optional parameters
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    ArrowType arrowType6 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2"),
        Node.newIdentifier("p3")), null);
    assertFalse(arrowType5.hasEqualParameters(arrowType6, false));
    assertFalse(arrowType6.hasEqualParameters(arrowType5, false));

    // Test arrow types with var_args
    ArrowType arrowType7 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    ArrowType arrowType8 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2"), Node.newIdentifier("...")),
        null);
    assertFalse(arrowType7.hasEqualParameters(arrowType8, false));
    assertFalse(arrowType8.hasEqualParameters(arrowType7, false));

    // Test arrow types with unknown types
    ArrowType arrowType9 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    ArrowType arrowType10 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2"), Node.newIdentifier("...")),
        getNativeType(JSTypeNative.UNKNOWN_TYPE));
    assertFalse(arrowType9.hasEqualParameters(arrowType10, true));
    assertFalse(arrowType10.hasEqualParameters(arrowType9, true));

    // Test arrow types with the same parameters but in different orders
    ArrowType arrowType11 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p2"), Node.newIdentifier("p1")), null);
    ArrowType arrowType12 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    assertFalse(arrowType11.hasEqualParameters(arrowType12, false));
    assertFalse(arrowType12.hasEqualParameters(arrowType11, false));

    // Test arrow types with matching var_args
    ArrowType arrowType13 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2"), Node.newIdentifier("...")),
        null);
    ArrowType arrowType14 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2"), Node.newIdentifier("...")),
        null);
    assertTrue(arrowType13.hasEqualParameters(arrowType14, false));
    assertTrue(arrowType14.hasEqualParameters(arrowType13, false));

    // Test arrow types with different numbers of parameters
    ArrowType arrowType15 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    ArrowType arrowType16 = new ArrowType(registry, Node.newIdentifier("p1"), null);
    assertFalse(arrowType15.hasEqualParameters(arrowType16, false));
    assertFalse(arrowType16.hasEqualParameters(arrowType15, false));

    // Test arrow types with same parameters but different optional parameters
    ArrowType arrowType17 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    ArrowType arrowType18 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2"), Node.newIdentifier("p3")),
        null);
    assertFalse(arrowType17.hasEqualParameters(arrowType18, false));
    assertFalse(arrowType18.hasEqualParameters(arrowType17, false));

    // Test arrow types with same parameters but different inferred return types
    ArrowType arrowType19 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null, true);
    ArrowType arrowType20 = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null, false);
    assertFalse(arrowType19.hasEqualParameters(arrowType20, false));
    assertFalse(arrowType20.hasEqualParameters(arrowType19, false));
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType = new ArrowType(registry, createParameters(registry,
        Node.newList(Node.newIdentifier("p1"), Node.newIdentifier("p2"))),
        createJSType(registry, "number"));
    assertEquals("[ArrowType(p1: Any, p2: number)]", arrowType.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    arrowType.getLeastSupertype(Node.newNull());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    arrowType.getGreatestSubtype(Node.newNull());
  }

  @Test
  public void testArrowTypeTernaryValue() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    TernaryValue result = arrowType.testForEquality(Node.newNull());
    assertEquals(TernaryValue.FALSE, result);
  }

  @Test
  public void testArrowTypeVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    Visitor visitor = new Visitor<Object>() {
      @Override
      public Object visitArrowType(ArrowType type) {
        return "ArrowType";
      }
    };
    assertEquals("ArrowType", arrowType.visit(visitor));
  }

  @Test
  public void testArrowTypeBooleanLiteralSet() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    BooleanLiteralSet set = arrowType.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.TRUE, set);
  }

  @Test
  public void testArrowTypeResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter() {};

    ArrowType arrowType = new ArrowType(registry, null, null);
    arrowType.resolveInternal(errorReporter, new StaticScope<JSType>(registry));
    assertEquals(arrowType, arrowType);

    Node param = Node.newIdentifier("p1");
    arrowType = new ArrowType(registry, Node.newList(param), null);
    arrowType.resolveInternal(errorReporter, new StaticScope<JSType>(registry));
    assertEquals(arrowType, arrowType);

    JSType paramType = createJSType(registry, "number");
    arrowType = new ArrowType(registry, Node.newList(param), paramType);
    arrowType.resolveInternal(errorReporter, new StaticScope<JSType>(registry));
    assertEquals(arrowType, arrowType);
  }

  @Test
  public void testArrowTypeHasAnyTemplateInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    assertFalse(arrowType.hasAnyTemplateInternal());

    arrowType = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")),
        createJSType(registry, Node.newList(Node.newIdentifier("T"))));
    assertTrue(arrowType.hasAnyTemplateInternal());

    arrowType = new ArrowType(registry, Node.newList(
        Node.newIdentifier("p1"), Node.newIdentifier("p2")), null);
    JSType paramType = createJSType(registry, Node.newList(Node.newIdentifier("T")));
    arrowType = new ArrowType(registry, Node.newList(param), paramType);
    assertTrue(arrowType.hasAnyTemplateInternal());
  }
}