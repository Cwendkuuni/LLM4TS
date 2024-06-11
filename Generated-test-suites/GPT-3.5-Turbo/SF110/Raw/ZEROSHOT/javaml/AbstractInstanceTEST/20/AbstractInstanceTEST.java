import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import net.sf.javaml.core.*;

public class AbstractInstanceTest {

   private AbstractInstance instance;
   
   @Before
   public void setUp() {
      instance = new AbstractInstance() {};
   }
   
   @Test
   public void testGetID() {
      assertNotNull(instance.getID());
   }
   
   @Test
   public void testIterator() {
      assertNotNull(instance.iterator());
   }
   
   @Test
   public void testClassValue() {
      assertNull(instance.classValue());
   }
   
   @Test
   public void testSetClassValue() {
      instance.setClassValue(new Object());
      assertNotNull(instance.classValue());
   }
   
   @Test
   public void testMinus() {
      Instance min = new DenseInstance(new double[instance.noAttributes()]);
      assertNotNull(instance.minus(min));
   }
   
   @Test
   public void testMinusWithDouble() {
      double min = 0.0;
      assertNotNull(instance.minus(min));
   }
   
   @Test
   public void testDivideWithDouble() {
      double min = 0.0;
      assertNotNull(instance.divide(min));
   }
   
   @Test
   public void testMultiply() {
      double value = 0.0;
      assertNotNull(instance.multiply(value));
   }
   
   @Test
   public void testHashCode() {
      assertNotNull(instance.hashCode());
   }
   
   @Test
   public void testEquals() {
      Object obj = new Object();
      assertFalse(instance.equals(obj));
   }
   
   @Test
   public void testMultiplyWithInstance() {
      Instance value = new DenseInstance(new double[instance.noAttributes()]);
      assertNotNull(instance.multiply(value));
   }
   
   @Test
   public void testDivideWithInstance() {
      Instance min = new DenseInstance(new double[instance.noAttributes()]);
      assertNotNull(instance.divide(min));
   }
   
   @Test
   public void testAddWithDouble() {
      double min = 0.0;
      assertNotNull(instance.add(min));
   }
   
   @Test
   public void testAddWithInstance() {
      Instance min = new DenseInstance(new double[instance.noAttributes()]);
      assertNotNull(instance.add(min));
   }
   
   @Test
   public void testSqrt() {
      assertNotNull(instance.sqrt());
   }
   
   @Test
   public void testInstanceValueIterator() {
      Iterator<Double> iterator = instance.new InstanceValueIterator();
      assertNotNull(iterator);
   }
   
   @Test
   public void testHasNext() {
      Iterator<Double> iterator = instance.new InstanceValueIterator();
      assertTrue(iterator.hasNext());
   }
   
   @Test
   public void testNext() {
      Iterator<Double> iterator = instance.new InstanceValueIterator();
      assertNotNull(iterator.next());
   }
   
   @Test(expected = UnsupportedOperationException.class)
   public void testRemove() {
      Iterator<Double> iterator = instance.new InstanceValueIterator();
      iterator.remove();
   }
}