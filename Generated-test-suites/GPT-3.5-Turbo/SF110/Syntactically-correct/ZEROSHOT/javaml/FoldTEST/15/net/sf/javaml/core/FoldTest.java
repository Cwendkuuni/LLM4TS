import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoldTest {
  
  @Before
  public void setUp() {
    // TODO: Setup test data here
  }
  
  @Test
  public void testAdd() {
    Fold fold = new Fold(null, null);
    try {
      fold.add(null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testClasses() {
    Fold fold = new Fold(null, null);
    SortedSet<Object> result = fold.classes();
    // TODO: Add assertion here
  }
  
  @Test
  public void testFolds() {
    Fold fold = new Fold(null, null);
    try {
      fold.folds(0, null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testInstance() {
    Fold fold = new Fold(null, null);
    Instance result = fold.instance(0);
    // TODO: Add assertion here
  }
  
  @Test
  public void testKNearest() {
    Fold fold = new Fold(null, null);
    try {
      fold.kNearest(0, null, null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testAddIndexElement() {
    Fold fold = new Fold(null, null);
    try {
      fold.add(0, null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testAddAllC() {
    Fold fold = new Fold(null, null);
    try {
      fold.addAll(null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testAddAllIndexC() {
    Fold fold = new Fold(null, null);
    try {
      fold.addAll(0, null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testClear() {
    Fold fold = new Fold(null, null);
    try {
      fold.clear();
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testContains() {
    Fold fold = new Fold(null, null);
    boolean result = fold.contains(null);
    // TODO: Add assertion here
  }
  
  @Test
  public void testContainsAll() {
    Fold fold = new Fold(null, null);
    boolean result = fold.containsAll(null);
    // TODO: Add assertion here
  }
  
  @Test
  public void testGet() {
    Fold fold = new Fold(null, null);
    Instance result = fold.get(0);
    // TODO: Add assertion here
  }
  
  @Test
  public void testIndexOf() {
    Fold fold = new Fold(null, null);
    int result = fold.indexOf(null);
    // TODO: Add assertion here
  }
  
  @Test
  public void testIsEmpty() {
    Fold fold = new Fold(null, null);
    boolean result = fold.isEmpty();
    // TODO: Add assertion here
  }
  
  @Test
  public void testIterator() {
    Fold fold = new Fold(null, null);
    Iterator<Instance> result = fold.iterator();
    // TODO: Add assertion here
  }
  
  @Test
  public void testLastIndexOf() {
    Fold fold = new Fold(null, null);
    int result = fold.lastIndexOf(null);
    // TODO: Add assertion here
  }
  
  @Test
  public void testListIterator() {
    Fold fold = new Fold(null, null);
    ListIterator<Instance> result = fold.listIterator();
    // TODO: Add assertion here
  }
  
  @Test
  public void testListIteratorIndex() {
    Fold fold = new Fold(null, null);
    ListIterator<Instance> result = fold.listIterator(0);
    // TODO: Add assertion here
  }
  
  @Test
  public void testRemove() {
    Fold fold = new Fold(null, null);
    try {
      fold.remove(null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testRemoveIndex() {
    Fold fold = new Fold(null, null);
    try {
      fold.remove(0);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testRemoveAll() {
    Fold fold = new Fold(null, null);
    try {
      fold.removeAll(null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testRetainAll() {
    Fold fold = new Fold(null, null);
    try {
      fold.retainAll(null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testSet() {
    Fold fold = new Fold(null, null);
    try {
      fold.set(0, null);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testSize() {
    Fold fold = new Fold(null, null);
    int result = fold.size();
    // TODO: Add assertion here
  }
  
  @Test
  public void testSubList() {
    Fold fold = new Fold(null, null);
    try {
      fold.subList(0, 0);
      fail("UnsupportedOperationException was expected");
    } catch (UnsupportedOperationException e) {}
  }
  
  @Test
  public void testToArray() {
    Fold fold = new Fold(null, null);
    Object[] result = fold.toArray();
    // TODO: Add assertion here
  }
  
  @Test
  public void testToArrayT() {
    Fold fold = new Fold(null, null);
    Object[] result = fold.toArray(new Object[0]);
    // TODO: Add assertion here
  }
  
  @Test
  public void testNoAttributes() {
    Fold fold = new Fold(null, null);
    int result = fold.noAttributes();
    // TODO: Add assertion here
  }
  
  @Test
  public void testClassIndex() {
    Fold fold = new Fold(null, null);
    int result = fold.classIndex(null);
    // TODO: Add assertion here
  }
  
  @Test
  public void testClassValue() {
    Fold fold = new Fold(null, null);
    Object result = fold.classValue(0);
    // TODO: Add assertion here
  }
  
  @Test
  public void testCopy() {
    Fold fold = new Fold(null, null);
    Dataset result = fold.copy();
    // TODO: Add assertion here
  }
  
  @Test
  public void testFoldIterator() {
    Fold.FoldIterator foldIterator = fold.new FoldIterator(0);
    // TODO: Add assertion here
  }
}