import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType.isSubtype(null));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    try {
      arrowType.getLeastSupertype(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("This operation is not supported", e.getMessage());
    }
  }

  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    try {
      arrowType.getGreatestSubtype(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("This operation is not supported", e.getMessage());
    }
  }

  @Test
  public void testTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    try {
      arrowType.testForEquality(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("This operation is not supported", e.getMessage());
    }
  }

  @Test
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    try {
      arrowType.visit(null);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("This operation is not supported", e.getMessage());
    }
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType.resolveInternal(null, null));
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasAnyTemplateInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(/*nodeType=*/0);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  private class JSTypeRegistry {
    public Node createParametersWithVarArgs(JSType jSType) {
      return new Node(0);
    }

    public JSType getNativeType(int unknownType) {
      return new JSType();
    }
  }

  private class Node {
    private int nodeType;
    private Node next;
    private JSType jSType;

    public Node(int nodeType) {
      this.nodeType = nodeType;
    }

    public Node getNext() {
      return next;
    }

    public void setNext(Node next) {
      this.next = next;
    }

    public JSType getJSType() {
      return jSType;
    }

    public void setJSType(JSType jSType) {
      this.jSType = jSType;
    }

    public boolean isOptionalArg() {
      return false;
    }
  }

  private class JSType {
    public boolean isSubtype(JSType that) {
      return false;
    }

    public int hashCode() {
      return 0;
    }

    public boolean checkEquivalenceHelper(JSType other, boolean tolerateUnknowns) {
      return false;
    }

    public JSType resolve(ErrorReporter t, StaticScope<JSType> scope) {
      return this;
    }

    public TernaryValue testForEquality(JSType that) {
      return null;
    }

    public <T> T visit(Visitor<T> visitor) {
      return null;
    }

    public BooleanLiteralSet getPossibleToBooleanOutcomes() {
      return null;
    }

    public JSType hasAnyTemplate() {
      return null;
    }
  }
}