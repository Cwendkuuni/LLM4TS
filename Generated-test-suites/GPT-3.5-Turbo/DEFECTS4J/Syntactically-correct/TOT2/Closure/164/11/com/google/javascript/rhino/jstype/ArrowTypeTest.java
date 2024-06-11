import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    // Test basic case
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    assertTrue(arrowType1.isSubtype(arrowType2));
    
    // Test edge case
    ArrowType arrowType3 = new ArrowType();
    ArrowType arrowType4 = new ArrowType();
    arrowType4.returnType = new JSType(JSTypeRegistry.getRegistry(), JSTypeNative.STRING_TYPE);
    assertFalse(arrowType3.isSubtype(arrowType4));
    
    // Test exception handling
    ArrowType arrowType5 = new ArrowType();
    assertFalse(arrowType5.isSubtype(null));
  }

  @Test
  public void testHasEqualParameters() {
    // Test basic case
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    
    // Test edge case
    ArrowType arrowType3 = new ArrowType();
    Node parameters = new Node();
    parameters.addChildToBack(new Node());
    parameters.getFirstChild().setJSType(new JSType(JSTypeRegistry.getRegistry(), JSTypeNative.STRING_TYPE));
    arrowType3.parameters = parameters;
    ArrowType arrowType4 = new ArrowType();
    assertFalse(arrowType3.hasEqualParameters(arrowType4));
    
    // Test exception handling
    ArrowType arrowType5 = new ArrowType();
    assertFalse(arrowType5.hasEqualParameters(null));
  }

  @Test
  public void testIsEquivalentTo() {
    // Test basic case
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    
    // Test edge case
    ArrowType arrowType3 = new ArrowType();
    arrowType3.returnType = new JSType(JSTypeRegistry.getRegistry(), JSTypeNative.NUMBER_TYPE);
    ArrowType arrowType4 = new ArrowType();
    assertFalse(arrowType3.isEquivalentTo(arrowType4));
    
    // Test exception handling
    ArrowType arrowType5 = new ArrowType();
    assertFalse(arrowType5.isEquivalentTo(null));
  }

}