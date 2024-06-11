import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    // Test case 1
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    boolean result = arrowType1.isSubtype(arrowType2);
    assertTrue(result);

    // Test case 2
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    result = arrowType1.isSubtype(arrowType2);
    assertFalse(result);

    // Test case 3
    arrowType1.returnType = new JSType(registry);
    arrowType2.returnType = new JSType(registry);
    result = arrowType1.isSubtype(arrowType2);
    assertTrue(result);
  }

  @Test
  public void testHasEqualParameters() {
    // Test case 1
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    boolean result = arrowType1.hasEqualParameters(arrowType2, false);
    assertTrue(result);

    // Test case 2
    Node node1 = new Node();
    Node node2 = new Node();
    arrowType1.parameters = node1;
    arrowType2.parameters = node2;
    result = arrowType1.hasEqualParameters(arrowType2, false);
    assertFalse(result);

    // Test case 3
    arrowType1.parameters = node1;
    arrowType2.parameters = node1;
    result = arrowType1.hasEqualParameters(arrowType2, false);
    assertTrue(result);
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    // Test case 1
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    boolean result = arrowType1.checkArrowEquivalenceHelper(arrowType2, false);
    assertTrue(result);

    // Test case 2
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    result = arrowType1.checkArrowEquivalenceHelper(arrowType2, false);
    assertFalse(result);
  }

  @Test
  public void testHashCode() {
    // Test case 1: check not null type
    JSTypeRegistry registry1 = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry1, null, null);
    int hashcode = arrowType1.hashCode();
    assertEquals(0, hashcode);

    // Test case 2: check return type is null
    JSTypeRegistry registry2 = new JSTypeRegistry();
    ArrowType arrowType2 = new ArrowType(registry2, null, null);
    arrowType2.returnType = new JSType();
    hashcode = arrowType2.hashCode();
    assertEquals(31, hashcode);

    // Test case 3: check parameter node null
    Node node1 = new Node();
    JSTypeRegistry registry3 = new JSTypeRegistry();
    ArrowType arrowType3 = new ArrowType(registry3, node1, new JSType());
    hashcode = arrowType3.hashCode();
    assertEquals(31, hashcode);

    // Test case 4 : Check if node does not have type
    Node node2 = new Node();
    node1.setJSType(node2.getJSType());
    JSTypeRegistry registry4 = new JSTypeRegistry();
    ArrowType arrowType4 = new ArrowType(registry4, node1, new JSType());
    hashcode = arrowType4.hashCode();
    assertEquals(31, hashcode);
  }
}