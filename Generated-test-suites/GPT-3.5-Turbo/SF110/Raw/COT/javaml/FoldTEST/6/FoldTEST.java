
import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {
  
  @Test
  public void testAdd() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    boolean result = fold.add(new Instance());
    assertFalse(result);
  }
  
  @Test
  public void testClasses() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    SortedSet<Object> result = fold.classes();
    assertNotNull(result);
  }
  
  @Test
  public void testFolds() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.folds(5, new Random());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("Method is not yet implemented", e.getMessage());
    }
  }
  
  @Test
  public void testInstance() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    Instance result = fold.instance(0);
    assertNotNull(result);
  }
  
  @Test
  public void testKNearest() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.kNearest(5, new Instance(), new DistanceMeasure());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("Method is not yet implemented", e.getMessage());
    }
  }
  
  @Test
  public void testAddIndexElement() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.add(0, new Instance());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
    }
  }
  
  @Test
  public void testAddAllCollection() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.addAll(new ArrayList<Instance>());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
    }
  }
  
  @Test
  public void testAddAllIndexCollection() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.addAll(0, new ArrayList<Instance>());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
    }
  }
  
  @Test
  public void testClear() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.clear();
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
    }
  }
  
  @Test
  public void testContains() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    boolean result = fold.contains(new Object());
    assertFalse(result);
  }
  
  @Test
  public void testContainsAll() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    boolean result = fold.containsAll(new ArrayList<Object>());
    assertFalse(result);
  }
  
  @Test
  public void testGet() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    Instance result = fold.get(0);
    assertNotNull(result);
  }
  
  @Test
  public void testIndexOf() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.indexOf(new Object());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("Method is not yet implemented", e.getMessage());
    }
  }
  
  @Test
  public void testIsEmpty() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    boolean result = fold.isEmpty();
    assertFalse(result);
  }
  
  @Test
  public void testIterator() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    Iterator<Instance> result = fold.iterator();
    assertNotNull(result);
  }
  
  @Test
  public void testLastIndexOf() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.lastIndexOf(new Object());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("Method is not yet implemented", e.getMessage());
    }
  }
  
  @Test
  public void testListIterator() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    ListIterator<Instance> result = fold.listIterator();
    assertNotNull(result);
  }
  
  @Test
  public void testListIteratorIndex() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    ListIterator<Instance> result = fold.listIterator(0);
    assertNotNull(result);
  }
  
  @Test
  public void testRemoveObject() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.remove(new Object());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("You cannot do this on a fold.", e.getMessage());
    }
  }
  
  @Test
  public void testRemoveIndex() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.remove(0);
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("You cannot do this on a fold.", e.getMessage());
    }
  }
  
  @Test
  public void testRemoveAll() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.removeAll(new ArrayList<Object>());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("You cannot do this on a fold.", e.getMessage());
    }
  }
  
  @Test
  public void testRetainAll() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.retainAll(new ArrayList<Object>());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("You cannot do this on a fold.", e.getMessage());
    }
  }
  
  @Test
  public void testSet() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.set(0, new Instance());
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("You cannot do this on a fold.", e.getMessage());
    }
  }
  
  @Test
  public void testSize() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    int result = fold.size();
    assertEquals(3, result);
  }
  
  @Test
  public void testSubList() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    try {
      fold.subList(0, 2);
      fail("Exception not thrown");
    } catch(UnsupportedOperationException e) {
      assertEquals("You cannot do this on a fold.", e.getMessage());
    }
  }
  
  @Test
  public void testToArray() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    Object[] result = fold.toArray();
    assertNotNull(result);
    assertEquals(3, result.length);
  }
  
  @Test
  public void testToArrayT() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    Integer[] result = fold.toArray(new Integer[3]);
    assertNotNull(result);
    assertEquals(3, result.length);
  }
  
  @Test
  public void testNoAttributes() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    int result = fold.noAttributes();
    assertEquals(0, result);
  }
  
  @Test
  public void testClassIndex() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    int result = fold.classIndex(new Object());
    assertEquals(-1, result);
  }
  
  @Test
  public void testClassValue() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    Object result = fold.classValue(0);
    assertNull(result);
  }
  
  @Test
  public void testCopy() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    Dataset result = fold.copy();
    assertNotNull(result);
  }
  
  @Test
  public void testFoldIterator() {
    Fold fold = new Fold(new Dataset(), new int[]{1, 2, 3});
    Fold.FoldIterator iterator = fold.new FoldIterator(0);
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    assertNotNull(iterator.next());
    assertFalse(iterator.hasPrevious());
    assertNotNull(iterator.previous());
    assertEquals(0, iterator.nextIndex());
    assertEquals(0, iterator.previousIndex());
    iterator.remove();
    iterator.add(new Instance());
    iterator.set(new Instance());
  }
}
