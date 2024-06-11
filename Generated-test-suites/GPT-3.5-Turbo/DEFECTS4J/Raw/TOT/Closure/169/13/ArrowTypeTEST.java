
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype_sameReturnTypeAndParameters_true() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Node.Type.PARAMETERS);
    JSType returnType = new JSType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_differentReturnType_false() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Node.Type.PARAMETERS);
    JSType returnType1 = new JSType(registry);
    JSType returnType2 = new JSType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
    
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_differentParameters_false() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Node.Type.PARAMETERS);
    Node parameters2 = new Node(Node.Type.PARAMETERS);
    JSType returnType = new JSType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters_sameParameters_true() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Node.Type.PARAMETERS);
    JSType returnType = new JSType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParameters_differentParameters_false() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Node.Type.PARAMETERS);
    Node parameters2 = new Node(Node.Type.PARAMETERS);
    JSType returnType = new JSType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    
    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_sameParametersAndReturnType_true() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Node.Type.PARAMETERS);
    JSType returnType = new JSType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_differentParameters_false() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(Node.Type.PARAMETERS);
    Node parameters2 = new Node(Node.Type.PARAMETERS);
    JSType returnType = new JSType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_differentReturnType_false() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Node.Type.PARAMETERS);
    JSType returnType1 = new JSType(registry);
    JSType returnType2 = new JSType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
    
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }
}
