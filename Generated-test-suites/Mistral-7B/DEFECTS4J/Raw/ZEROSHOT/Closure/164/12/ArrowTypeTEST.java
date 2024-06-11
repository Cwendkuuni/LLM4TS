import static org.junit.Assert.*;
import static com.google.javascript.rhino.node.Property.*;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TestNamer;
import com.google.javascript.rhino.testing.TypeEqualsTester;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node paramsNode = createNode("(number, number)");
    ArrowType arrowType = new ArrowType(registry, paramsNode, null, false);
    assertEquals(arrowType.getClass(), ArrowType.class);
    assertEquals(arrowType.getParameters(), paramsNode);
    assertNull(arrowType.getReturnType());
    assertFalse(arrowType.isReturnTypeInferred());

    Node varArgsParamsNode = createNode("(number, ...number)");
    ArrowType varArgsArrowType = new ArrowType(registry, varArgsParamsNode, null, false);
    assertEquals(varArgsArrowType.getClass(), ArrowType.class);
    assertEquals(varArgsArrowType.getParameters(), varArgsParamsNode);
    assertNull(varArgsArrowType.getReturnType());
    assertFalse(varArgsArrowType.isReturnTypeInferred());

    JSType returnType = getNativeType(JSTypeNative.INT);
    ArrowType arrowTypeWithReturn = new ArrowType(registry, null, returnType, false);
    assertEquals(arrowTypeWithReturn.getClass(), ArrowType.class);
    assertNull(arrowTypeWithReturn.getParameters());
    assertEquals(arrowTypeWithReturn.getReturnType(), returnType);
    assertFalse(arrowTypeWithReturn.isReturnTypeInferred());

    ArrowType arrowTypeWithInferredReturn = new ArrowType(registry, null, null, true);
    assertEquals(arrowTypeWithInferredReturn.getClass(), ArrowType.class);
    assertNull(arrowTypeWithInferredReturn.getParameters());
    assertNull(arrowTypeWithInferredReturn.getReturnType());
    assertTrue(arrowTypeWithInferredReturn.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node paramsNode1 = createNode("(number, number)");
    ArrowType arrowType1 = new ArrowType(registry, paramsNode1, null, false);

    Node paramsNode2 = createNode("(number, number)");
    ArrowType arrowType2 = new ArrowType(registry, paramsNode2, null, false);

    ArrowType arrowType3 = new ArrowType(registry, null, null, false);

    ArrowType arrowType4 = new ArrowType(registry, paramsNode1, null, false);

    ArrowType arrowType5 = new ArrowType(registry, paramsNode1, getNativeType(JSTypeNative.INT), false);

    TypeEqualsTester.testEquality(arrowType1, arrowType1, true);
    TypeEqualsTester.testEquality(arrowType1, arrowType2, true);
    TypeEqualsTester.testEquality(arrowType1, arrowType3, false);
    TypeEqualsTester.testEquality(arrowType1, arrowType4, true);
    TypeEqualsTester.testEquality(arrowType1, arrowType5, false);
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node paramsNode = createNode("(number, number)");
    ArrowType arrowType1 = new ArrowType(registry, paramsNode, null, false);
    ArrowType arrowType2 = new ArrowType(registry, paramsNode, null, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);

    JSType returnType = getNativeType(JSTypeNative.INT);
    ArrowType arrowType3 = new ArrowType(registry, null, returnType, false);
    assertNotEquals(hashCode1, arrowType3.hashCode());

    Node varArgsParamsNode = createNode("(number, ...number)");
    ArrowType arrowType4 = new ArrowType(registry, varArgsParamsNode, null, false);
    assertNotEquals(hashCode1, arrowType4.hashCode());

    ArrowType arrowType5 = new ArrowType(registry, paramsNode, null, true);
    assertNotEquals(hashCode1, arrowType5.hashCode());
  }

  @Test
  public void testArrowTypeSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node paramsNode1 = createNode("(number, number)");
    ArrowType arrowType1 = new ArrowType(registry, paramsNode1, null, false);

    Node paramsNode2 = createNode("(number)");
    ArrowType arrowType2 = new ArrowType(registry, paramsNode2, null, false);

    Node paramsNode3 = createNode("(number, number, number)");
    ArrowType arrowType3 = new ArrowType(registry, paramsNode3, null, false);

    Node paramsNode4 = createNode("(number, ...number)");
    ArrowType arrowType4 = new ArrowType(registry, paramsNode4, null, false);

    Node paramsNode5 = createNode("(number, number, number, number)");
    ArrowType arrowType5 = new ArrowType(registry, paramsNode5, null, false);

    JSType returnType1 = getNativeType(JSTypeNative.INT);
    ArrowType arrowType6 = new ArrowType(registry, null, returnType1, false);

    JSType returnType2 = getNativeType(JSTypeNative.STRING);
    ArrowType arrowType7 = new ArrowType(registry, null, returnType2, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType5));
    assertFalse(arrowType5.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType6));
    assertFalse(arrowType6.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType7));
    assertFalse(arrowType7.isSubtype(arrowType1));

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType6.isSubtype(arrowType6));
    assertTrue(arrowType7.isSubtype(arrowType7));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node paramsNode1 = createNode("(number, number)");
    ArrowType arrowType1 = new ArrowType(registry, paramsNode1, null, false);

    Node paramsNode2 = createNode("(number, number)");
    ArrowType arrowType2 = new ArrowType(registry, paramsNode2, null, false);

    Node paramsNode3 = createNode("(number, ...number)");
    ArrowType arrowType3 = new ArrowType(registry, paramsNode3, null, false);

    Node paramsNode4 = createNode("(number, number, number)");
    ArrowType arrowType4 = new ArrowType(registry, paramsNode4, null, false);

    Node paramsNode5 = createNode("(number, number, number, number)");
    ArrowType arrowType5 = new ArrowType(registry, paramsNode5, null, false);

    Node paramsNode6 = createNode("(number, string)");
    ArrowType arrowType6 = new ArrowType(registry, paramsNode6, null, false);

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType4));
    assertFalse(arrowType1.hasEqualParameters(arrowType5));
    assertFalse(arrowType1.hasEqualParameters(arrowType6));
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node paramsNode = createNode("(number, number)");
    ArrowType arrowType = new ArrowType(registry, paramsNode, null, false);

    assertEquals("ArrowType[(number, number)]", arrowType.toString());
  }

  private Node createNode(String nodeText) {
    JSTypeRegistry registry = new JSTypeRegistry();
    return registry.parseNode(nodeText, null, null, null);
  }

  private JSType getNativeType(int typeCode) {
    JSTypeRegistry registry = new JSTypeRegistry();
    return registry.getNativeType(typeCode);
  }
}