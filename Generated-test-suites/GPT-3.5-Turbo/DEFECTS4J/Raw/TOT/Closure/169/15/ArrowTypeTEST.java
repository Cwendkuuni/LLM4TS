import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.SyntaxError;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.StaticScope;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    
    // Create instances of ArrowType
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    
    // Test for subtype
    Assert.assertTrue(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    
    // Create instances of ArrowType
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    
    // Test for equals
    Assert.assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
  }
  
  @Test
  public void testCheckArrowEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    
    // Create instances of ArrowType
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    
    // Test for equivalence
    Assert.assertTrue(
      arrowType1.checkArrowEquivalenceHelper(arrowType2, true)
    );
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    
    // Test getLeastSupertype method
    arrowType.getLeastSupertype(null);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    
    // Test getGreatestSubtype method
    arrowType.getGreatestSubtype(null);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    
    // Test testForEquality method
    arrowType.testForEquality(null);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    
    // Test visit method
    arrowType.visit(null);
  }
  
  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    StaticScope<JSType> scope = new StaticScope<>();
    
    // Test resolveInternal method
    JSType resolvedType = arrowType.resolveInternal(null, scope);
    Assert.assertNotNull(resolvedType);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    
    // Test hasUnknownParamsOrReturn method
    boolean hasUnknown = arrowType.hasUnknownParamsOrReturn();
    Assert.assertFalse(hasUnknown);
  }
  
  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    
    // Test toStringHelper method
    String str = arrowType.toStringHelper(false);
    Assert.assertNotNull(str);
  }
  
  @Test
  public void testHasAnyTemplateInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    
    // Test hasAnyTemplateInternal method
    boolean hasTemplate = arrowType.hasAnyTemplateInternal();
    Assert.assertFalse(hasTemplate);
  }
}