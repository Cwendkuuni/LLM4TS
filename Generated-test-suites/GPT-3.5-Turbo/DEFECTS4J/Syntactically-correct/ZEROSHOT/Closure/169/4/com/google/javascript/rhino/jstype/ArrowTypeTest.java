import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {
  
  @Test
  public void testIsSubtype() {
    // Create instances
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node();
    JSType returnType1 = new JSType(registry);
    boolean returnTypeInferred1 = true;
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1, returnTypeInferred1);
    Node parameters2 = new Node();
    JSType returnType2 = new JSType(registry);
    boolean returnTypeInferred2 = true;
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2, returnTypeInferred2);

    // Test isSubtype()
    boolean result1 = arrowType1.isSubtype(arrowType2);
    boolean result2 = arrowType2.isSubtype(arrowType1);

    // Assertion
    assertTrue(result1);
    assertTrue(result2);
  }
  
  @Test
  public void testHasEqualParameters() {
    // Create instances
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node();
    JSType returnType1 = new JSType(registry);
    boolean returnTypeInferred1 = true;
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1, returnTypeInferred1);
    Node parameters2 = new Node();
    JSType returnType2 = new JSType(registry);
    boolean returnTypeInferred2 = true;
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2, returnTypeInferred2);

    // Test hasEqualParameters()
    boolean result = arrowType1.hasEqualParameters(arrowType2, true);

    // Assertion
    assertTrue(result);
  }
  
  @Test
  public void testCheckArrowEquivalenceHelper() {
    // Create instances
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node();
    JSType returnType1 = new JSType(registry);
    boolean returnTypeInferred1 = true;
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1, returnTypeInferred1);
    Node parameters2 = new Node();
    JSType returnType2 = new JSType(registry);
    boolean returnTypeInferred2 = true;
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2, returnTypeInferred2);

    // Test checkArrowEquivalenceHelper()
    boolean result1 = arrowType1.checkArrowEquivalenceHelper(arrowType2, true);
    boolean result2 = arrowType2.checkArrowEquivalenceHelper(arrowType1, true);

    // Assertion
    assertTrue(result1);
    assertTrue(result2);
  }
  
  @Test
  public void testHashCode() {
    // Create instances
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node();
    JSType returnType1 = new JSType(registry);
    boolean returnTypeInferred1 = true;
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1, returnTypeInferred1);
    Node parameters2 = new Node();
    JSType returnType2 = new JSType(registry);
    boolean returnTypeInferred2 = true;
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2, returnTypeInferred2);

    // Test hashCode()
    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    // Assertion
    assertEquals(hashCode1, hashCode2);
  }
}