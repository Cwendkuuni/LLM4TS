
package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.distance.DistanceMeasure;

public class FoldTest {

	private Dataset parent;
	private int[] indices;
	private Fold fold;

	@Before
	public void setup() {
		parent = new DefaultDataset();
		parent.add(new Instance());
		parent.add(new Instance());
		indices = new int[]{0, 1};
		fold = new Fold(parent, indices);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAdd() {
		fold.add(new Instance());
	}

	@Test
	public void testClasses() {
		assertEquals(parent.classes(), fold.classes());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testFolds() {
		fold.folds(2, null);
	}

	@Test
	public void testInstance() {
		assertEquals(parent.instance(0), fold.instance(0));
		assertEquals(parent.instance(1), fold.instance(1));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testKNearest() {
		fold.kNearest(3, new Instance(), new DistanceMeasure());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddByIndex() {
		fold.add(0, new Instance());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddAll() {
		fold.addAll(parent);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddAllByIndex() {
		fold.addAll(0, parent);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testClear() {
		fold.clear();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testContains() {
		fold.contains(new Object());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testContainsAll() {
		fold.containsAll(parent);
	}

	@Test
	public void testGet() {
		assertEquals(parent.instance(0), fold.get(0));
		assertEquals(parent.instance(1), fold.get(1));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testIndexOf() {
		fold.indexOf(new Object());
	}

	@Test
	public void testIsEmpty() {
		assertFalse(fold.isEmpty());
	}

	@Test
	public void testIterator() {
		assertTrue(fold.iterator().hasNext());
		assertEquals(parent.instance(0), fold.iterator().next());
		assertEquals(parent.instance(0), fold.iterator().next());
		assertTrue(fold.iterator().hasNext());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testLastIndexOf() {
		fold.lastIndexOf(new Object());
	}

	@Test
	public void testListIterator() {
		assertTrue(fold.listIterator().hasNext());
		assertEquals(parent.instance(0), fold.listIterator().next());
		assertEquals(parent.instance(0), fold.listIterator().next());
		assertTrue(fold.listIterator().hasNext());
	}

	@Test
	public void testListIteratorWithIndex() {
		assertTrue(fold.listIterator(0).hasNext());
		assertEquals(parent.instance(0), fold.listIterator(0).next());
		assertEquals(parent.instance(0), fold.listIterator(0).next());
		assertTrue(fold.listIterator(0).hasNext());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRemove() {
		fold.remove(new Object());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRemoveByIndex() {
		fold.remove(0);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRemoveAll() {
		fold.removeAll(parent);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRetainAll() {
		fold.retainAll(parent);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testSet() {
		fold.set(0, new Instance());
	}

	@Test
	public void testSize() {
		assertEquals(2, fold.size());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testSubList() {
		fold.subList(0, 1);
	}

	@Test
	public void testToArray() {
		Object[] expected = {parent.instance(0), parent.instance(1)};
		Object[] actual = fold.toArray();
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testToArrayWithArrayArg() {
		Instance[] expected = {parent.instance(0), parent.instance(1)};
		Instance[] actual = fold.toArray(new Instance[2]);
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testNoAttributes() {
		assertEquals(parent.noAttributes(), fold.noAttributes());
	}

	@Test
	public void testClassIndex() {
		assertEquals(parent.classIndex(new Object()), fold.classIndex(new Object()));
	}

	@Test
	public void testClassValue() {
		assertEquals(parent.classValue(0), fold.classValue(0));
	}

	@Test
	public void testCopy() {
		Dataset copy = fold.copy();
		for (Instance instance : fold) {
			assertTrue(copy.contains(instance));
		}
	}
}
