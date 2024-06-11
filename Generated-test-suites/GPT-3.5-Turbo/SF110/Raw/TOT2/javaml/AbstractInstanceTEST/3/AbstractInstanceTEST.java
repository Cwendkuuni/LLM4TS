import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractInstanceTest {
  
  private static final double EPSILON = 0.0001;
  
  @Test
  public void testGetID() {
    AbstractInstance instance = new ConcreteInstance("test");
    assertEquals(0, instance.getID());
  }
  
  @Test
  public void testIterator() {
    AbstractInstance instance = new ConcreteInstance("test");
    Iterator<Double> iterator = instance.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(0.0, iterator.next(), EPSILON);
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testClassValue() {
    AbstractInstance instance = new ConcreteInstance("test");
    assertEquals("test", instance.classValue());
  }
  
  @Test
  public void testSetClassValue() {
    AbstractInstance instance = new ConcreteInstance("test");
    instance.setClassValue("updated");
    assertEquals("updated", instance.classValue());
  }
  
  @Test
  public void testMinus_Instance() {
    AbstractInstance instance1 = new ConcreteInstance(2.0, 3.0);
    AbstractInstance instance2 = new ConcreteInstance(1.0, 1.0);
    AbstractInstance result = instance1.minus(instance2);
    assertEquals(1.0, result.value(0), EPSILON);
    assertEquals(2.0, result.value(1), EPSILON);
  }
  
  @Test
  public void testMinus_Double() {
    AbstractInstance instance = new ConcreteInstance(2.0, 3.0);
    AbstractInstance result = instance.minus(1.0);
    assertEquals(1.0, result.value(0), EPSILON);
    assertEquals(2.0, result.value(1), EPSILON);
  }
  
  // More test cases for other methods...
  
}