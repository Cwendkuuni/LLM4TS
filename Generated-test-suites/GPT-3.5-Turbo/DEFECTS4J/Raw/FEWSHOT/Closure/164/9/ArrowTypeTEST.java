import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.NAME);
    JSType returnType = new JSType(registry);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.isSubType(arrowType));
    assertTrue(arrowType.isSubType(returnType));
    assertTrue(arrowType.isSubType(parameters));

    ArrowType arrowType2 = new ArrowType(null, null, null, true);
    assertFalse(arrowType.isSubType(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, new Node(Token.EXPR_RESULT), returnType);
    assertFalse(arrowType.isSubType(arrowType3));

    ArrowType arrowType4 = new ArrowType(registry, parameters, new JSType(registry), true);
    assertFalse(arrowType.isSubType(arrowType4));
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.NAME);
    JSType returnType = new JSType(registry);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.isEquivalentTo(arrowType));
    assertFalse(arrowType.isEquivalentTo(returnType));
    assertFalse(arrowType.isEquivalentTo(parameters));

    ArrowType arrowType2 = new ArrowType(null, null, null, true);
    assertFalse(arrowType.isEquivalentTo(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, new Node(Token.EXPR_RESULT), returnType);
    assertFalse(arrowType.isEquivalentTo(arrowType3));

    ArrowType arrowType4 = new ArrowType(registry, parameters, new JSType(registry), true);
    assertFalse(arrowType.isEquivalentTo(arrowType4));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.NAME);
    JSType returnType = new JSType(registry);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType arrowType2 = new ArrowType(registry, null, returnType, false);
    assertFalse(arrowType.hashCode() == arrowType2.hashCode());

    ArrowType arrowType3 = new ArrowType(registry, parameters, null, false);
    assertFalse(arrowType.hashCode() == arrowType3.hashCode());

    ArrowType arrowType4 = new ArrowType(null, null, null, true);
    assertFalse(arrowType.hashCode() == arrowType4.hashCode());

    ArrowType arrowType5 = new ArrowType(null, null, null, false);
    assertTrue(arrowType4.hashCode() == arrowType5.hashCode());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.NAME);
    JSType returnType = new JSType(registry);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertFalse(arrowType.hasUnknownParamsOrReturn());

    ArrowType arrowType2 = new ArrowType(registry, new Node(Token.EXPR_RESULT), returnType);
    assertTrue(arrowType2.hasUnknownParamsOrReturn());

    ArrowType arrowType3 = new ArrowType(null, null, null, true);
    assertTrue(arrowType3.hasUnknownParamsOrReturn());
  }
}