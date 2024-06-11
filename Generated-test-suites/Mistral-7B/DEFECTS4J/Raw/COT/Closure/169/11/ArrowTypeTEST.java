import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

  @org.junit.Test
  public void testArrowType_WithValidArguments() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param1"),
        Node.newNode(Node.IDENT, "param2")
    });
    JSType returnType = Node.newJSType(registry, JSTypeNative.INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType));
  }

  @org.junit.Test
  public void testArrowType_WithDifferentParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param1"),
        Node.newNode(Node.INT)
    });
    Node parameters2 = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param2"),
        Node.newNode(Node.STRING)
    });
    JSType returnType = Node.newJSType(registry, JSTypeNative.INT);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @org.junit.Test
  public void testArrowType_WithDifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param")
    });
    JSType returnType1 = Node.newJSType(registry, JSTypeNative.INT);
    JSType returnType2 = Node.newJSType(registry, JSTypeNative.STRING);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param")
    });
    JSType returnType = Node.newJSType(registry, JSTypeNative.INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    arrowType.getLeastSupertype(null);
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param")
    });
    JSType returnType = Node.newJSType(registry, JSTypeNative.INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    arrowType.getGreatestSubtype(null);
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param")
    });
    JSType returnType = Node.newJSType(registry, JSTypeNative.INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    arrowType.testForEquality(null);
  }

  @org.junit.Test
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param")
    });
    JSType returnType = Node.newJSType(registry, JSTypeNative.INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowTypeTestVisitor visitor = new ArrowTypeTestVisitor();
    arrowType.visit(visitor);

    assertEquals("ArrowType", visitor.getName());
  }

  @org.junit.Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param1"),
        Node.newNode(Node.INT)
    });
    Node parameters2 = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param1"),
        Node.newNode(Node.STRING)
    });
    ArrowType arrowType1 = new ArrowType(registry, parameters1, Node.newJSType(registry, JSTypeNative.INT));
    ArrowType arrowType2 = new ArrowType(registry, parameters2, Node.newJSType(registry, JSTypeNative.INT));

    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType2, true));

    Node parameters3 = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param1"),
        Node.newNode(Node.IDENT, "param2"),
        Node.newNode(Node.INT)
    });
    Node parameters4 = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param1"),
        Node.newNode(Node.IDENT, "param2"),
        Node.newNode(Node.STRING)
    });

    ArrowType arrowType3 = new ArrowType(registry, parameters3, Node.newJSType(registry, JSTypeNative.INT));
    ArrowType arrowType4 = new ArrowType(registry, parameters4, Node.newJSType(registry, JSTypeNative.INT));

    assertFalse(arrowType3.hasEqualParameters(arrowType4, false));
    assertTrue(arrowType3.hasEqualParameters(arrowType4, true));
  }

  @org.junit.Test
  public void testCheckArrowEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param1"),
        Node.newNode(Node.INT)
    });
    Node parameters2 = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param1"),
        Node.newNode(Node.STRING)
    });
    JSType returnType1 = Node.newJSType(registry, JSTypeNative.INT);
    JSType returnType2 = Node.newJSType(registry, JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));

    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType4 = new ArrowType(registry, parameters1, returnType1);

    assertTrue(arrowType3.checkArrowEquivalenceHelper(arrowType4, false));
    assertTrue(arrowType3.checkArrowEquivalenceHelper(arrowType4, true));
  }

  @org.junit.Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType returnType = Node.newJSType(registry, JSTypeNative.INT);

    // Test with same type, different boolean values
    ArrowType arrowType1 = new ArrowType(registry, Node.newNode(Node.NEW_ARRAY), returnType);
    ArrowType arrowType2 = new ArrowType(registry, Node.newNode(Node.NEW_ARRAY), returnType, true);
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    // Test with different return types
    ArrowType arrowType3 = new ArrowType(registry, Node.newNode(Node.NEW_ARRAY), Node.newJSType(registry, JSTypeNative.STRING));
    assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());

    // Test with different parameters
    ArrowType arrowType4 = new ArrowType(registry, Node.newNode(Node.NEW_ARRAY, new Node[] { Node.newNode(Node.IDENT, "param2") }), returnType);
    assertNotEquals(arrowType1.hashCode(), arrowType4.hashCode());
  }

  @org.junit.Test
  public void testHasAnyTemplateInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newNode(Node.NEW_ARRAY, new Node[] {
        Node.newNode(Node.IDENT, "param"),
        Node.newNode(Node.IDENT, "templateParam")
    });
    JSType returnType = Node.newJSType(registry, JSTypeNative.INT);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType arrowTypeWithTemplate = new ArrowType(registry, parameters, returnType, Node.newNode(Node.IDENT, "template"));

    assertFalse(arrowType.hasAnyTemplateInternal());
    assertTrue(arrowTypeWithTemplate.hasAnyTemplateInternal());
  }

  @org.junit.Test
  public void testIsSubtype_WithNullOther() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType returnType = Node.newJSType(registry, JSTypeNative.INT);
    ArrowType arrowType = new ArrowType(registry, Node.newNode(Node.NEW_ARRAY), returnType);

    assertFalse(arrowType.isSubtype(null));
  }
}

class ArrowTypeTestVisitor implements Visitor<String> {
  private String name;

  @Override
  public String visitArrowType(ArrowType arrowType) {
    name = "ArrowType";
    return name;
  }

  public String getName() {
    return name;
  }
}