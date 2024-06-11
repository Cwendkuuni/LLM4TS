import org.junit.*;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class FoldTest {
    
    private Dataset parentDataset;
    private int[] indices;
    private Fold fold;
    
    @Before
    public void setUp() {
        parentDataset = new DefaultDataset();
        parentDataset.add(new DenseInstance(new double[]{1,2,3}, "A"));
        parentDataset.add(new DenseInstance(new double[]{4,5,6}, "B"));
        parentDataset.add(new DenseInstance(new double[]{7,8,9}, "C"));
        
        indices = new int[]{0, 1, 2};
        
        fold = new Fold(parentDataset, indices);
    }
    
    // Testing add() method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void add_ThrowsUnsupportedOperationException() {
        Instance instance = new DenseInstance(new double[]{1,1,1}, "D");
        fold.add(instance);
    }

    // Testing classes() method.
    
    @Test
    public void classes_ReturnsClassesOfParentDataset() {
        SortedSet<Object> expectedClasses = parentDataset.classes();
        SortedSet<Object> actualClasses = fold.classes();
        
        assertEquals(expectedClasses, actualClasses);
    }

    // Testing folds() method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void folds_ThrowsUnsupportedOperationException() {
        int numFolds = 5;
        Random rg = new Random();
        
        fold.folds(numFolds, rg);
    }

    // Testing instance() method.
    
    @Test
    public void instance_ReturnsInstanceAtGivenIndex() {
        int index = 1;
        Instance expectedInstance = parentDataset.instance(indices[index]);
        Instance actualInstance = fold.instance(index);
        
        assertEquals(expectedInstance, actualInstance);
    }

    // Testing kNearest() method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void kNearest_ThrowsUnsupportedOperationException() {
        int k = 3;
        Instance inst = new DenseInstance(new double[]{4,5,6}, "B");
        DistanceMeasure dm = new EuclideanDistance();
        
        fold.kNearest(k, inst, dm);
    }

    // Testing add(int index, Instance element) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void addAtIndex_ThrowsUnsupportedOperationException() {
        int index = 1;
        Instance instance = new DenseInstance(new double[]{10,11,12}, "E");
        
        fold.add(index, instance);
    }

    // Testing addAll(Collection<? extends Instance> c) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void addAll_Collection_ThrowsUnsupportedOperationException() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1,1,1}, "D"));
        instances.add(new DenseInstance(new double[]{2,2,2}, "E"));
        
        fold.addAll(instances);
    }

    // Testing addAll(int index, Collection<? extends Instance> c) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void addAllAtIndex_Collection_ThrowsUnsupportedOperationException() {
        int index = 1;
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{5,5,5}, "F"));
        instances.add(new DenseInstance(new double[]{6,6,6}, "G"));
        
        fold.addAll(index, instances);
    }

    // Testing clear() method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void clear_ThrowsUnsupportedOperationException() {
        fold.clear();
    }

    // Testing contains(Object o) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void contains_ThrowsUnsupportedOperationException() {
        Object obj = "A";
        
        fold.contains(obj);
    }

    // Testing containsAll(Collection<?> c) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void containsAll_Collection_ThrowsUnsupportedOperationException() {
        List<Object> objects = new ArrayList<>();
        objects.add("A");
        objects.add("B");
        objects.add("C");
        
        fold.containsAll(objects);
    }

    // Testing get(int index) method.
    
    @Test
    public void get_ReturnsInstanceAtGivenIndex() {
        int index = 2;
        Instance expectedInstance = parentDataset.instance(indices[index]);
        Instance actualInstance = fold.get(index);
        
        assertEquals(expectedInstance, actualInstance);
    }

    // Testing indexOf(Object o) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void indexOf_ThrowsUnsupportedOperationException() {
        Object obj = "A";
        
        fold.indexOf(obj);
    }

    // Testing isEmpty() method.
    
    @Test
    public void isEmpty_ReturnsFalse() {
        boolean isEmpty = fold.isEmpty();
        
        assertFalse(isEmpty);
    }

    // Testing iterator() method.
    
    @Test
    public void iterator_ReturnsFoldIterator() {
        Iterator<Instance> iterator = fold.iterator();
        
        assertTrue(iterator instanceof Fold.FoldIterator);
    }

    // Testing lastIndexOf(Object o) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void lastIndexOf_ThrowsUnsupportedOperationException() {
        Object obj = "A";
        
        fold.lastIndexOf(obj);
    }

    // Testing listIterator() method.
    
    @Test
    public void listIterator_ReturnsFoldIterator() {
        ListIterator<Instance> listIterator = fold.listIterator();
        
        assertTrue(listIterator instanceof Fold.FoldIterator);
    }

    // Testing listIterator(int index) method.
    
    @Test
    public void listIteratorAtIndex_ReturnsFoldIteratorWithGivenIndex() {
        int index = 2;
        ListIterator<Instance> listIterator = fold.listIterator(index);
        listIterator.next();
        
        assertEquals(index, listIterator.nextIndex());
    }

    // Testing remove(Object o) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void remove_ThrowsUnsupportedOperationException() {
        Object obj = new DenseInstance(new double[]{1,2,3}, "A");
        
        fold.remove(obj);
    }

    // Testing remove(int index) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void removeAtIndex_ThrowsUnsupportedOperationException() {
        int index = 1;
        
        fold.remove(index);
    }

    // Testing removeAll(Collection<?> c) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void removeAll_Collection_ThrowsUnsupportedOperationException() {
        List<Object> objects = new ArrayList<>();
        objects.add("A");
        objects.add("B");
        
        fold.removeAll(objects);
    }

    // Testing retainAll(Collection<?> c) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void retainAll_Collection_ThrowsUnsupportedOperationException() {
        List<Object> objects = new ArrayList<>();
        objects.add("A");
        objects.add("B");
        
        fold.retainAll(objects);
    }
    
    // Testing set(int index, Instance element) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void setAtIndex_ThrowsUnsupportedOperationException() {
        int index = 1;
        Instance instance = new DenseInstance(new double[]{10,11,12}, "E");
        
        fold.set(index, instance);
    }

    // Testing size() method.
    
    @Test
    public void size_ReturnsNumberOfInstancesInFold() {
        int expectedSize = indices.length;
        int actualSize = fold.size();
        
        assertEquals(expectedSize, actualSize);
    }

    // Testing subList(int fromIndex, int toIndex) method.
    
    @Test(expected = UnsupportedOperationException.class)
    public void subList_ThrowsUnsupportedOperationException() {
        int fromIndex = 0;
        int toIndex = 2;
        
        fold.subList(fromIndex, toIndex);
    }

    // Testing toArray() method.
    
    @Test
    public void toArray_ReturnsArrayOfInstancesInFold() {
        Object[] expectedArray = parentDataset.toArray();
        Object[] actualArray = fold.toArray();
        
        assertArrayEquals(expectedArray, actualArray);
    }

    // Testing toArray(T[] a) method.
    
    @Test
    public void toArray_ReturnsArrayOfInstancesInFoldWithType() {
        DenseInstance[] expectedArray = parentDataset.toArray(new DenseInstance[fold.size()]);
        DenseInstance[] actualArray = fold.toArray(new DenseInstance[fold.size()]);
        
        assertArrayEquals(expectedArray, actualArray);
    }

    // Testing noAttributes() method.
    
    @Test
    public void noAttributes_ReturnsNumberOfAttributesInParentDataset() {
        int expectedNoAttributes = parentDataset.noAttributes();
        int actualNoAttributes = fold.noAttributes();
        
        assertEquals(expectedNoAttributes, actualNoAttributes);
    }

    // Testing classIndex(Object clazz) method.
    
    @Test
    public void classIndex_ReturnsClassIndexInParentDataset() {
        Object clazz = "B";
        int expectedClassIndex = parentDataset.classIndex(clazz);
        int actualClassIndex = fold.classIndex(clazz);
        
        assertEquals(expectedClassIndex, actualClassIndex);
    }

    // Testing classValue(int index) method.
    
    @Test
    public void classValue_ReturnsClassValueAtGivenIndexInParentDataset() {
        int index = 0;
        Object expectedClassValue = parentDataset.classValue(indices[index]);
        Object actualClassValue = fold.classValue(index);
        
        assertEquals(expectedClassValue, actualClassValue);
    }

    // Testing copy() method.
    
    @Test
    public void copy_ReturnsNewDatasetWithCopiedInstances() {
        Dataset expectedCopy = new DefaultDataset();
        for (int index : indices) {
            expectedCopy.add(parentDataset.instance(index).copy());
        }
        
        Dataset actualCopy = fold.copy();
        
        assertNotSame(expectedCopy, actualCopy);
        assertEquals(expectedCopy, actualCopy);
    }
    
}