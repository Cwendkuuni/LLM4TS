import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class AbstractInstanceTest {
  
  private AbstractInstance instance;
  private AbstractInstance equalInstance;
  private AbstractInstance differentInstance;
  
  @Before
  public void setUp() {
    instance = new AbstractInstance();
    equalInstance = new AbstractInstance();
    differentInstance = new AbstractInstance();
  }
  
  @Test
  public void testGetID() {
    int expectedID = 0;
    int actualID = instance.getID();
    assertEquals(expectedID, actualID);
  }
  
  @Test
  public void testIterator() {
    Iterator<Double> iterator = instance.iterator();
    assertNotNull(iterator);
  }
  
  @Test
  public void testClassValue() {
    Object expectedClassValue = null;
    Object actualClassValue = instance.classValue();
    assertEquals(expectedClassValue, actualClassValue);
  }
  
  @Test
  public void testSetClassValue() {
    Object classValue = new Object();
    instance.setClassValue(classValue);
    Object expectedClassValue = classValue;
    Object actualClassValue = instance.classValue();
    assertEquals(expectedClassValue, actualClassValue);
  }
  
  @Test
  public void testMinusWithInstance() {
    Instance min = new DenseInstance(new double[instance.noAttributes()]);
    Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
    Instance actualResult = instance.minus(min);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testMinusWithDouble() {
    double min = 0.0;
    Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
    Instance actualResult = instance.minus(min);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testDivide() {
    double min = 1.0;
    Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
    Instance actualResult = instance.divide(min);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testMultiply() {
    double value = 1.0;
    Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
    Instance actualResult = instance.multiply(value);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testHashCode() {
    int expectedHashCode = instance.getID();
    int actualHashCode = instance.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }
  
  @Test
  public void testEqualsWithSameInstance() {
    assertEquals(instance, instance);
  }
  
  @Test
  public void testEqualsWithEqualInstance() {
    assertEquals(instance, equalInstance);
  }
  
  @Test
  public void testEqualsWithDifferentInstance() {
    assertNotEquals(instance, differentInstance);
  }
  
  @Test
  public void testMultiplyWithInstance() {
    Instance value = new DenseInstance(new double[instance.noAttributes()]);
    Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
    Instance actualResult = instance.multiply(value);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testDivideWithInstance() {
    Instance min = new DenseInstance(new double[instance.noAttributes()]);
    Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
    Instance actualResult = instance.divide(min);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testAddWithDouble() {
    double min = 0.0;
    Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
    Instance actualResult = instance.add(min);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testAddWithInstance() {
    Instance min = new DenseInstance(new double[instance.noAttributes()]);
    Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
    Instance actualResult = instance.add(min);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testSqrt() {
    Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
    Instance actualResult = instance.sqrt();
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testInstanceValueIteratorHasNext() {
    instance.put(0, 1.0);
    Iterator<Double> iterator = instance.iterator();
    boolean hasNext = iterator.hasNext();
    assertTrue(hasNext);
  }
  
  @Test
  public void testInstanceValueIteratorNext() {
    double expectedValue = 1.0;
    instance.put(0, expectedValue);
    Iterator<Double> iterator = instance.iterator();
    double actualValue = iterator.next();
    assertEquals(expectedValue, actualValue, 0.0);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testInstanceValueIteratorRemove() {
    Iterator<Double> iterator = instance.iterator();
    iterator.remove();
  }
}