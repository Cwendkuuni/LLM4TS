import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = null;
    JSType returnType1 = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    
    Node parameters2 = null;
    JSType returnType2 = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    
    assertFalse(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = null;
    JSType returnType1 = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    
    Node parameters2 = null;
    JSType returnType2 = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }
  
  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = null;
    JSType returnType1 = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    
    Node parameters2 = null;
    JSType returnType2 = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }
  
  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = null;
    JSType returnType1 = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    
    Node parameters2 = null;
    JSType returnType2 = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = null;
    JSType returnType = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    
    arrowType.getLeastSupertype(arrowType);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = null;
    JSType returnType = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    
    arrowType.getGreatestSubtype(arrowType);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = null;
    JSType returnType = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    
    arrowType.testForEquality(arrowType);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = null;
    JSType returnType = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    
    arrowType.visit(new Visitor<>());
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = null;
    JSType returnType = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }
  
  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = null;
    JSType returnType = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    
    arrowType.resolveInternal(t, scope);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = null;
    JSType returnType = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }
  
  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = null;
    JSType returnType = registry.getNativeType(UNKNOWN_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    
    arrowType.toStringHelper(true);
  }

}