import static org.junit.Assert.*;
import org.junit.*;

public class AbstractInstanceTest {

  @Test
  public void testGetID() {
    // Typical use case
    AbstractInstance instance1 = new AbstractInstance();
    int id = instance1.getID();
    assertEquals(0, id);
    
    // Edge case
    AbstractInstance instance2 = new AbstractInstance();
    id = instance2.getID();
    assertEquals(1, id);
  }
  
  @Test
  public void testIterator() {
    // Typical use case
    AbstractInstance instance = new AbstractInstance();
    int count = 0;
    for (double value : instance) {
      count++;
    }
    assertEquals(instance.noAttributes(), count);
    
    // Edge case
    AbstractInstance emptyInstance = new AbstractInstance();
    count = 0;
    for (double value : emptyInstance) {
      count++;
    }
    assertEquals(0, count);
  }
  
  @Test
  public void testClassValue() {
    // Typical use case
    String classValue = "Class A";
    AbstractInstance instance = new AbstractInstance(classValue);
    assertEquals(classValue, instance.classValue());
    
    // Edge case
    AbstractInstance instanceNoClassValue = new AbstractInstance();
    assertNull(instanceNoClassValue.classValue());
  }
  
  @Test
  public void testSetClassValue() {
    // Typical use case
    String classValue = "Class B";
    AbstractInstance instance = new AbstractInstance();
    instance.setClassValue(classValue);
    assertEquals(classValue, instance.classValue());
    
    // Edge case
    AbstractInstance instanceNoClassValue = new AbstractInstance();
    instanceNoClassValue.setClassValue(null);
    assertNull(instanceNoClassValue.classValue());
  }
  
  @Test
  public void testMinus_Instance() {
    // Typical use case
    AbstractInstance instance1 = new AbstractInstance();
    AbstractInstance instance2 = new AbstractInstance();
    AbstractInstance result = instance1.minus(instance2);
    // Check that all attributes are 0
    for (double value : result) {
      assertEquals(0, value, 0.001);
    }
    
    // Edge case
    AbstractInstance instance3 = new AbstractInstance();
    AbstractInstance instance4 = new AbstractInstance();
    instance4.put(0, 1.5);
    AbstractInstance result2 = instance3.minus(instance4);
    // Check that the non-zero attribute is negative
    assertEquals(-1.5, result2.getValue(0), 0.001);
  }
  
  @Test
  public void testMinus_double() {
    // Typical use case
    AbstractInstance instance = new AbstractInstance();
    AbstractInstance result = instance.minus(1.0);
    // Check that all attributes are -1.0
    for (double value : result) {
      assertEquals(-1.0, value, 0.001);
    }
    
    // Edge case
    AbstractInstance emptyInstance = new AbstractInstance();
    AbstractInstance result2 = emptyInstance.minus(0.0);
    // Check that all attributes are 0.0
    for (double value : result2) {
      assertEquals(0.0, value, 0.001);
    }
  }
  
  @Test
  public void testDivide_double() {
    // Typical use case
    AbstractInstance instance = new AbstractInstance();
    AbstractInstance result = instance.divide(2.0);
    // Check that all attributes are 0.5
    for (double value : result) {
      assertEquals(0.5, value, 0.001);
    }
    
    // Edge case
    AbstractInstance instanceWithZero = new AbstractInstance();
    AbstractInstance result2 = instanceWithZero.divide(0.0);
    // Check that all attributes are NaN
    for (double value : result2) {
      assertTrue(Double.isNaN(value));
    }
  }
  
  @Test
  public void testMultiply_double() {
    // Typical use case
    AbstractInstance instance = new AbstractInstance();
    AbstractInstance result = instance.multiply(2.0);
    // Check that all attributes are 0.0
    for (double value : result) {
      assertEquals(0.0, value, 0.001);
    }
    
    // Edge case
    AbstractInstance emptyInstance = new AbstractInstance();
    AbstractInstance result2 = emptyInstance.multiply(0.0);
    // Check that all attributes are 0.0
    for (double value : result2) {
      assertEquals(0.0, value, 0.001);
    }
  }
  
  @Test
  public void testHashCode() {
    // Typical use case
    AbstractInstance instance1 = new AbstractInstance();
    AbstractInstance instance2 = new AbstractInstance();
    int hashCode1 = instance1.hashCode();
    int hashCode2 = instance2.hashCode();
    assertNotEquals(hashCode1, hashCode2);
    
    // Edge case
    AbstractInstance emptyInstance = new AbstractInstance();
    int emptyInstanceHashCode = emptyInstance.hashCode();
    assertEquals(0, emptyInstanceHashCode);
  }
  
  @Test
  public void testEquals() {
    // Typical use case
    AbstractInstance instance1 = new AbstractInstance();
    AbstractInstance instance2 = new AbstractInstance();
    boolean equals = instance1.equals(instance2);
    assertTrue(equals);
    
    // Edge case
    AbstractInstance emptyInstance = new AbstractInstance();
    boolean emptyInstanceEquals = emptyInstance.equals(null);
    assertFalse(emptyInstanceEquals);
  }
  
  @Test
  public void testMultiply_Instance() {
    // Typical use case
    AbstractInstance instance1 = new AbstractInstance();
    AbstractInstance instance2 = new AbstractInstance();
    AbstractInstance result = instance1.multiply(instance2);
    // Check that all attributes are 0.0
    for (double value : result) {
      assertEquals(0.0, value, 0.001);
    }
    
    // Edge case
    AbstractInstance instance3 = new AbstractInstance();
    AbstractInstance instance4 = new AbstractInstance();
    instance4.put(0, 2.0);
    AbstractInstance result2 = instance3.multiply(instance4);
    // Check that the non-zero attribute is 0.0
    assertEquals(0.0, result2.getValue(0), 0.001);
  }
  
  @Test
  public void testDivide_Instance() {
    // Typical use case
    AbstractInstance instance1 = new AbstractInstance();
    AbstractInstance instance2 = new AbstractInstance();
    AbstractInstance result = instance1.divide(instance2);
    // Check that all attributes are NaN
    for (double value : result) {
      assertTrue(Double.isNaN(value));
    }
    
    // Edge case
    AbstractInstance instance3 = new AbstractInstance();
    AbstractInstance instance4 = new AbstractInstance();
    instance4.put(0, 2.0);
    AbstractInstance result2 = instance3.divide(instance4);
    // Check that the non-zero attribute is NaN
    assertTrue(Double.isNaN(result2.getValue(0)));
  }
  
  @Test
  public void testAdd_double() {
    // Typical use case
    AbstractInstance instance = new AbstractInstance();
    AbstractInstance result = instance.add(1.0);
    // Check that all attributes are 1.0
    for (double value : result) {
      assertEquals(1.0, value, 0.001);
    }
    
    // Edge case
    AbstractInstance emptyInstance = new AbstractInstance();
    AbstractInstance result2 = emptyInstance.add(0.0);
    // Check that all attributes are 0.0
    for (double value : result2) {
      assertEquals(0.0, value, 0.001);
    }
  }
  
  @Test
  public void testAdd_Instance() {
    // Typical use case
    AbstractInstance instance1 = new AbstractInstance();
    AbstractInstance instance2 = new AbstractInstance();
    AbstractInstance result = instance1.add(instance2);
    // Check that all attributes are 0.0
    for (double value : result) {
      assertEquals(0.0, value, 0.001);
    }
    
    // Edge case
    AbstractInstance instance3 = new AbstractInstance();
    AbstractInstance instance4 = new AbstractInstance();
    instance4.put(0, 2.0);
    AbstractInstance result2 = instance3.add(instance4);
    // Check that the non-zero attribute is 2.0
    assertEquals(2.0, result2.getValue(0), 0.001);
  }
  
  @Test
  public void testSqrt() {
    // Typical use case
    AbstractInstance instance = new AbstractInstance();
    AbstractInstance result = instance.sqrt();
    // Check that all attributes are 0.0
    for (double value : result) {
      assertEquals(0.0, value, 0.001);
    }
    
    // Edge case
    AbstractInstance emptyInstance = new AbstractInstance();
    AbstractInstance result2 = emptyInstance.sqrt();
    // Check that all attributes are 0.0
    for (double value : result2) {
      assertEquals(0.0, value, 0.001);
    }
  }

}