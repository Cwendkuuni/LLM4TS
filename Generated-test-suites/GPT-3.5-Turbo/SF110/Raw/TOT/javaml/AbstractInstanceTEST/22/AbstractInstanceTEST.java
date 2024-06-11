
package net.sf.javaml.core;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {
	
	private AbstractInstance instance;
	
	@Before
	public void setUp() {
		instance = new AbstractInstanceImpl(1.5);
	}
	
	@After
	public void tearDown() {
		instance = null;
	}
	
	@Test
	public void testGetID() {
		Assert.assertEquals(0, instance.getID()); // Test with default ID
	}
	
	@Test
	public void testIterator() {
		// Test if the iterator returns correct values
		double[] expectedValues = {1.5};
		int index = 0;
		for (double value : instance) {
			Assert.assertEquals(expectedValues[index], value, 0.001);
			index++;
		}
	}
	
	@Test
	public void testClassValue() {
		Assert.assertEquals(1.5, (double)instance.classValue(), 0.001);
	}
	
	@Test
	public void testSetClassValue() {
		instance.setClassValue(2.5);
		Assert.assertEquals(2.5, (double)instance.classValue(), 0.001);
	}
	
	@Test
	public void testMinusWithInstance() {
		Instance min = new AbstractInstanceImpl(0.5);
		Instance result = instance.minus(min);
		double[] expectedValues = {1.0};
		Assert.assertArrayEquals(expectedValues, toArray(result), 0.001);
	}
	
	@Test
	public void testMinusWithDouble() {
		Instance result = instance.minus(0.5);
		double[] expectedValues = {1.0};
		Assert.assertArrayEquals(expectedValues, toArray(result), 0.001);
	}
	
	@Test
	public void testDivide() {
		Instance result = instance.divide(2.0);
		double[] expectedValues = {0.75};
		Assert.assertArrayEquals(expectedValues, toArray(result), 0.001);
	}
	
	@Test
	public void testMultiply() {
		Instance result = instance.multiply(2.0);
		double[] expectedValues = {3.0};
		Assert.assertArrayEquals(expectedValues, toArray(result), 0.001);
	}
	
	@Test
	public void testHashCode() {
		Assert.assertEquals(0, instance.hashCode()); // Test with default ID
	}
	
	@Test
	public void testEquals() {
		AbstractInstance other = new AbstractInstanceImpl(1.5);
		Assert.assertTrue(instance.equals(other));
	}
	
	@Test
	public void testMultiplyInstance() {
		Instance value = new AbstractInstanceImpl(2.0);
		Instance result = instance.multiply(value);
		double[] expectedValues = {3.0};
		Assert.assertArrayEquals(expectedValues, toArray(result), 0.001);
	}
	
	@Test
	public void testDivideInstance() {
		Instance min = new AbstractInstanceImpl(2.0);
		Instance result = instance.divide(min);
		double[] expectedValues = {0.75};
		Assert.assertArrayEquals(expectedValues, toArray(result), 0.001);
	}
	
	@Test
	public void testAddWithDouble() {
		Instance result = instance.add(1.5);
		double[] expectedValues = {3.0};
		Assert.assertArrayEquals(expectedValues, toArray(result), 0.001);
	}
	
	@Test
	public void testAddWithInstance() {
		Instance min = new AbstractInstanceImpl(1.5);
		Instance result = instance.add(min);
		double[] expectedValues = {3.0};
		Assert.assertArrayEquals(expectedValues, toArray(result), 0.001);
	}
	
	@Test
	public void testSqrt() {
		Instance result = instance.sqrt();
		double[] expectedValues = {1.224744871391589};
		Assert.assertArrayEquals(expectedValues, toArray(result), 0.001);
	}
	
	// Helper method to convert Instance to double array
	private double[] toArray(Instance instance) {
		double[] array = new double[instance.noAttributes()];
		int index = 0;
		for (double value : instance) {
			array[index] = value;
			index++;
		}
		return array;
	}
	
	// Dummy implementation of AbstractInstance for testing
	class AbstractInstanceImpl extends AbstractInstance {
		
		public AbstractInstanceImpl(Object classValue) {
			super(classValue);
		}
		
		@Override
		public int noAttributes() {
			return 1;
		}
		
		@Override
		public Double value(int index) {
			return (Double)classValue();
		}
		
		@Override
		public void put(int index, Double value) {
			setClassValue(value);
		}
		
		@Override
		public Iterator<Double> iterator() {
			return new InstanceValueIteratorImpl();
		}
	}
	
	// Dummy implementation of InstanceValueIterator for testing
	class InstanceValueIteratorImpl implements Iterator<Double> {
		
		private boolean hasNext = true;

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public Double next() {
			hasNext = false;
			return (Double)classValue();
		}

		@Override
		public void remove() {
			// Do nothing
		}
	}
}

