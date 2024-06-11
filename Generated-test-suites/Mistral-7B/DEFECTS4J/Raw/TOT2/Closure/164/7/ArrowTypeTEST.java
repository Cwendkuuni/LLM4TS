import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.TypeError;
import org.junit.Test;

public class ArrowTypeTest {

  private static JSTypeRegistry registry;

  static {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeInstance() {
    Node node = arrayNode(nodeNumber(0), nodeString("name"), nodeBoolean(true));
    ArrowType arrowType = new ArrowType(registry, null, getNativeType(JSTypeNative.VOID), false);
    ArrowType arrowTypeWithParameters = new ArrowType(registry, node, getNativeType(JSTypeNative.NUMBER), false);

    assertEquals(arrowType, arrowType);
    assertEquals(arrowType, arrowType);

    assertNotEquals(arrowType, arrowTypeWithParameters);
    assertNotEquals(arrowTypeWithParameters, arrowType);

    ArrowType arrowTypeWithSameParameters = new ArrowType(registry, node, getNativeType(JSTypeNative.NUMBER), false);
    assertEquals(arrowTypeWithParameters, arrowTypeWithSameParameters);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(JSTypeNative.VOID), false);
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(JSTypeNative.NUMBER), false);

    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType4 = new ArrowType(registry, null, getNativeType(JSTypeNative.VOID), false);

    ArrowType arrowType5 = new ArrowType(registry, nodeNumber(0), getNativeType(JSTypeNative.VOID), false);
    ArrowType arrowType6 = new ArrowType(registry, nodeNumber(0), getNativeType(JSTypeNative.NUMBER), false);

    ArrowType arrowType7 = new ArrowType(registry, nodeNumber(0), getNativeType(JSTypeNative.VOID), true);
    ArrowType arrowType8 = new ArrowType(registry, nodeNumber(0), getNativeType(JSTypeNative.NUMBER), true);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));

    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType1.isSubtype(arrowType5));
    assertFalse(arrowType1.isSubtype(arrowType6));

    assertFalse(arrowType2.isSubtype(arrowType1));
    assertFalse(arrowType2.isSubtype(arrowType3));
    assertFalse(arrowType2.isSubtype(arrowType4));
    assertFalse(arrowType2.isSubtype(arrowType5));
    assertFalse(arrowType2.isSubtype(arrowType6));

    assertTrue(arrowType3.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType4));

    assertTrue(arrowType5.isSubtype(arrowType7));
    assertTrue(arrowType6.isSubtype(arrowType8));

    assertFalse(arrowType7.isSubtype(arrowType5));
    assertFalse(arrowType8.isSubtype(arrowType6));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node node1 = arrayNode(nodeNumber(0), nodeString("name"), nodeBoolean(true));
    Node node2 = arrayNode(nodeNumber(0), nodeString("name1"), nodeBoolean(true));
    Node node3 = arrayNode(nodeNumber(0), nodeString("name"), null);

    ArrowType arrowType1 = new ArrowType(registry, node1, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType2 = new ArrowType(registry, node2, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType3 = new ArrowType(registry, node3, getNativeType(JSTypeNative.NUMBER), false);

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    Node node1 = arrayNode(nodeNumber(0), nodeString("name"), nodeBoolean(true));
    Node node2 = arrayNode(nodeNumber(0), nodeString("name1"), nodeBoolean(true));
    Node node3 = arrayNode(nodeNumber(0), nodeString("name"), null);

    ArrowType arrowType1 = new ArrowType(registry, node1, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType2 = new ArrowType(registry, node2, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType3 = new ArrowType(registry, node3, getNativeType(JSTypeNative.NUMBER), false);

    assertTrue(arrowType1.isEquivalentTo(arrowType1));
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType1.isEquivalentTo(arrowType3));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node node1 = arrayNode(nodeNumber(0), nodeString("name"), nodeBoolean(true));
    Node node2 = arrayNode(nodeNumber(0), nodeString("name1"), nodeBoolean(true));
    Node node3 = arrayNode(nodeNumber(0), nodeString("name"), null);

    ArrowType arrowType1 = new ArrowType(registry, node1, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType2 = new ArrowType(registry, node2, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType3 = new ArrowType(registry, node3, getNativeType(JSTypeNative.NUMBER), false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();

    assertEquals(hashCode1, hashCode2);

    assertNotEquals(hashCode1, arrowType2.hashCode());
    assertNotEquals(hashCode1, arrowType3.hashCode());
  }

  @Test(expected = TypeError.class)
  public void testArrowTypeGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(registry, null, getNativeType(JSTypeNative.NUMBER), false);
    arrowType.getLeastSupertype(getNativeType(JSTypeNative.VOID));
  }

  @Test(expected = TypeError.class)
  public void testArrowTypeGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(registry, null, getNativeType(JSTypeNative.NUMBER), false);
    arrowType.getGreatestSubtype(getNativeType(JSTypeNative.VOID));
  }

  @Test
  public void testArrowTypeToString() {
    Node node = arrayNode(nodeNumber(0), nodeString("name"), nodeBoolean(true));
    ArrowType arrowType = new ArrowType(registry, node, getNativeType(JSTypeNative.NUMBER), false);

    String expected = "ArrowType[{" + node.toString() + "},Number]";
    assertEquals(expected, arrowType.toString());
  }
}