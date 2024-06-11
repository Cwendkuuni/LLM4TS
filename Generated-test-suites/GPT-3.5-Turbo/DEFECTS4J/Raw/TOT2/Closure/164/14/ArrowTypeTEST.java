import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {
  
  @Test
  public void testConstructor() {
    // Test ArrowType constructor with parameters and returnType
    ArrowType arrowType = new ArrowType(registry, new Node(), returnType);
    assertNotNull(arrowType);
  }

  @Test
  public void testIsSubtype() {
    // Test isSubtype method with valid input
    ArrowType arrowType1 = new ArrowType(registry, new Node(), returnType);
    ArrowType arrowType2 = new ArrowType(registry, new Node(), returnType);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeDifferentReturnTypes() {
    // Test isSubtype method with different return types
    ArrowType arrowType1 = new ArrowType(registry, new Node(), returnType);
    JSType differentReturnType = new JSType(registry);
    ArrowType arrowType2 = new ArrowType(registry, new Node(), differentReturnType);
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtypeDifferentParameters() {
    // Test isSubtype method with different parameters
    Node parameters1 = new Node();
    Node parameters2 = new Node();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHashCode() {
    // Test hashCode method for ArrowType
    ArrowType arrowType = new ArrowType(registry, new Node(), returnType);
    assertNotNull(arrowType.hashCode());
  }

  @Test
  public void testResolveInternal() {
    // Test resolveInternal method for ArrowType
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope scope = new StaticScope<>();
    ArrowType arrowType = new ArrowType(registry, new Node(), returnType);
    assertNotNull(arrowType.resolveInternal(errorReporter, scope));
  }
}