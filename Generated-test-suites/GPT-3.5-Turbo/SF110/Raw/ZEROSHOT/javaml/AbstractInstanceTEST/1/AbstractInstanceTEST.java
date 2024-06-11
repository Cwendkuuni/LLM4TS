import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {
  
  @Test
  public void testGetID() {
    AbstractInstance instance = new ConcreteInstance();
    assertEquals(0, instance.getID());
  }
  
  @Test
  public void testIterator() {
    AbstractInstance instance = new ConcreteInstance();
    assertNotNull(instance.iterator());
  }
  
  @Test
  public void testClassValue() {
    AbstractInstance instance = new ConcreteInstance();
    assertNull(instance.classValue());
    
    Object classValue = "class";
    instance.setClassValue(classValue);
    
    assertEquals(classValue, instance.classValue());
  }
  
  @Test
  public void testMinus_withInstance() {
    AbstractInstance instance = new ConcreteInstance();
    ConcreteInstance min = new ConcreteInstance();
    
    AbstractInstance result = instance.minus(min);
    assertNotNull(result);
    assertEquals(instance.noAttributes(), result.noAttributes());
    
    for (int i = 0; i < instance.noAttributes(); i++) {
      assertEquals(instance.get(i) - min.get(i), result.get(i), 0.00001);
    }
  }
  
  @Test
  public void testMinus_withDouble() {
    AbstractInstance instance = new ConcreteInstance();
    double min = 2.5;
    
    AbstractInstance result = instance.minus(min);
    assertNotNull(result);
    assertEquals(instance.noAttributes(), result.noAttributes());
    
    for (int i = 0; i < instance.noAttributes(); i++) {
      assertEquals(instance.get(i) - min, result.get(i), 0.00001);
    }
  }
  
  @Test
  public void testDivide() {
    AbstractInstance instance = new ConcreteInstance();
    double divisor = 2;
    
    AbstractInstance result = instance.divide(divisor);
    assertNotNull(result);
    assertEquals(instance.noAttributes(), result.noAttributes());
    
    for (int i = 0; i < instance.noAttributes(); i++) {
      assertEquals(instance.get(i) / divisor, result.get(i), 0.00001);
    }
  }
  
  @Test
  public void testMultiply() {
    AbstractInstance instance = new ConcreteInstance();
    double multiplier = 3;
    
    AbstractInstance result = instance.multiply(multiplier);
    assertNotNull(result);
    assertEquals(instance.noAttributes(), result.noAttributes());
    
    for (int i = 0; i < instance.noAttributes(); i++) {
      assertEquals(instance.get(i) * multiplier, result.get(i), 0.00001);
    }
  }
  
  @Test
  public void testHashCode() {
    AbstractInstance instance = new ConcreteInstance();
    assertEquals(instance.getID(), instance.hashCode());
  }
  
  @Test
  public void testEquals() {
    AbstractInstance instance1 = new ConcreteInstance();
    AbstractInstance instance2 = new ConcreteInstance();
    AbstractInstance instance3 = new ConcreteInstance();
    
    assertEquals(instance1, instance2);
    assertNotEquals(instance1, instance3);
  }
  
  @Test
  public void testMultiply_withInstance() {
    AbstractInstance instance = new ConcreteInstance();
    ConcreteInstance value = new ConcreteInstance();
    
    AbstractInstance result = instance.multiply(value);
    assertNotNull(result);
    assertEquals(instance.noAttributes(), result.noAttributes());
    
    for (int i = 0; i < instance.noAttributes(); i++) {
      assertEquals(instance.get(i) * value.get(i), result.get(i), 0.00001);
    }
  }
  
  @Test
  public void testDivide_withInstance() {
    AbstractInstance instance = new ConcreteInstance();
    ConcreteInstance min = new ConcreteInstance();
    
    AbstractInstance result = instance.divide(min);
    assertNotNull(result);
    assertEquals(instance.noAttributes(), result.noAttributes());
    
    for (int i = 0; i < instance.noAttributes(); i++) {
      assertEquals(instance.get(i) / min.get(i), result.get(i), 0.00001);
    }
  }
  
  @Test
  public void testAdd_withDouble() {
    AbstractInstance instance = new ConcreteInstance();
    double value = 1.5;
    
    AbstractInstance result = instance.add(value);
    assertNotNull(result);
    assertEquals(instance.noAttributes(), result.noAttributes());
    
    for (int i = 0; i < instance.noAttributes(); i++) {
      assertEquals(instance.get(i) + value, result.get(i), 0.00001);
    }
  }
  
  @Test
  public void testAdd_withInstance() {
    AbstractInstance instance = new ConcreteInstance();
    ConcreteInstance value = new ConcreteInstance();
    
    AbstractInstance result = instance.add(value);
    assertNotNull(result);
    assertEquals(instance.noAttributes(), result.noAttributes());
    
    for (int i = 0; i < instance.noAttributes(); i++) {
      assertEquals(instance.get(i) + value.get(i), result.get(i), 0.00001);
    }
  }
  
  @Test
  public void testSqrt() {
    AbstractInstance instance = new ConcreteInstance();
    
    AbstractInstance result = instance.sqrt();
    assertNotNull(result);
    assertEquals(instance.noAttributes(), result.noAttributes());
    
    for (int i = 0; i < instance.noAttributes(); i++) {
      assertEquals(Math.sqrt(instance.get(i)), result.get(i), 0.00001);
    }
  }
  
  // Helper class to test AbstractInstance
  class ConcreteInstance extends AbstractInstance {
    public ConcreteInstance() {
      super();
    }
    
    public double get(int index) {
      return ((Map<Integer, Double>) this).get(index);
    }
    
    public void put(int index, double value) {
      ((Map<Integer, Double>) this).put(index, value);
    }
    
    public int noAttributes() {
      return 10;
    }
    
    public double value(int index) {
      return ((Map<Integer, Double>) this).get(index);
    }
  }
}

