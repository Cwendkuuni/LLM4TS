package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

	private DefaultDataset dataset;
	private Instance instance1;
	private Instance instance2;

	@Before
	public void setup() {
		dataset = new DefaultDataset();
		instance1 = new Instance();
		instance2 = new Instance();
	}

	@Test
	public void testAddInstance() {
		dataset.add(instance1);
		assertTrue(dataset.contains(instance1));
		assertEquals(1, dataset.size());
	}

	@Test
	public void testAddAllInstances() {
		dataset.addAll(Arrays.asList(instance1, instance2));
		assertTrue(dataset.containsAll(Arrays.asList(instance1, instance2)));
		assertEquals(2, dataset.size());
	}

	@Test
	public void testAddInstanceAtIndex() {
		dataset.addAll(Arrays.asList(instance1, instance2));
		Instance instance3 = new Instance();
		dataset.add(1, instance3);
		assertEquals(instance3, dataset.get(1));
		assertEquals(3, dataset.size());
	}

	@Test
	public void testClearDataset() {
		dataset.addAll(Arrays.asList(instance1, instance2));
		dataset.clear();
		assertTrue(dataset.isEmpty());
		assertEquals(0, dataset.size());
	}

	@Test
	public void testKNearestInstances() {
		dataset.addAll(Arrays.asList(instance1, instance2));
		DistanceMeasure distanceMeasure = new EuclideanDistance();
		Set<Instance> nearest = dataset.kNearest(1, instance1, distanceMeasure);
		assertEquals(1, nearest.size());
		assertTrue(nearest.contains(instance2));
	}

	@Test
	public void testFoldsCreation() {
		dataset.addAll(Arrays.asList(instance1, instance2));
		Random random = new Random();
		Dataset[] folds = dataset.folds(2, random);
		assertEquals(2, folds.length);
		assertEquals(1, folds[0].size());
		assertEquals(1, folds[1].size());
	}

	@Test
	public void testNoAttributes() {
		dataset.add(instance1);
		assertEquals(0, dataset.noAttributes());
	}

	@Test
	public void testClassIndex() {
		dataset.addAll(Arrays.asList(instance1, instance2));
		assertEquals(0, dataset.classIndex(null));
		assertEquals(-1, dataset.classIndex(new Object()));
	}

	@Test
	public void testClassValue() {
		dataset.addAll(Arrays.asList(instance1, instance2));
		assertEquals(instance1.classValue(), dataset.classValue(0));
		assertEquals(instance2.classValue(), dataset.classValue(1));
	}

	@Test
	public void testDatasetCopy() {
		dataset.add(instance1);
		Dataset copy = dataset.copy();
		assertTrue(copy.contains(instance1));
		assertEquals(1, copy.size());
	}
}