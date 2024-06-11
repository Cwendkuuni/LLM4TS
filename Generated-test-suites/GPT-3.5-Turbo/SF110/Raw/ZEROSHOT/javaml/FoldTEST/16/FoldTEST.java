import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.core.Instance;

public class FoldTest {

    private Dataset parent;
    private int[] indices;
    private Fold fold;
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        fold.add(new Instance());
    }
    
    @Test
    public void testClasses() {
        SortedSet<Object> classes = fold.classes();
        assertNotNull(classes);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        fold.folds(5, new Random());
    }
    
    @Test
    public void testInstance() {
        Instance instance = fold.instance(0);
        assertNotNull(instance);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        fold.kNearest(5, new Instance(), new DistanceMeasure());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddIndexElement() {
        fold.add(0, new Instance());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Collection<Instance> collection = Arrays.asList(new Instance());
        fold.addAll(collection);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndexCollection() {
        Collection<Instance> collection = Arrays.asList(new Instance());
        fold.addAll(0, collection);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        fold.contains(new Object());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Collection<Object> collection = Arrays.asList(new Object());
        fold.containsAll(collection);
    }
    
    @Test
    public void testGet() {
        Instance instance = fold.get(0);
        assertNotNull(instance);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        fold.indexOf(new Object());
    }
    
    @Test
    public void testIsEmpty() {
        boolean result = fold.isEmpty();
        assertFalse(result);
    }
    
    @Test
    public void testIterator() {
        Iterator<Instance> iterator = fold.iterator();
        assertNotNull(iterator);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testLastIndexOf() {
        fold.lastIndexOf(new Object());
    }
    
    @Test
    public void testListIterator() {
        ListIterator<Instance> iterator = fold.listIterator();
        assertNotNull(iterator);
    }
    
    @Test
    public void testListIteratorWithIndex() {
        ListIterator<Instance> iterator = fold.listIterator(0);
        assertNotNull(iterator);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        fold.remove(new Object());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        fold.remove(0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Collection<Object> collection = Arrays.asList(new Object());
        fold.removeAll(collection);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Collection<Object> collection = Arrays.asList(new Object());
        fold.retainAll(collection);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        fold.set(0, new Instance());
    }
    
    @Test
    public void testSize() {
        int size = fold.size();
        assertEquals(indices.length, size);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        fold.subList(0, 1);
    }
    
    @Test
    public void testToArray() {
        Object[] array = fold.toArray();
        assertNotNull(array);
    }
    
    @Test
    public void testToArrayWithArg() {
        Instance[] array = fold.toArray(new Instance[fold.size()]);
        assertNotNull(array);
    }
    
    @Test
    public void testNoAttributes() {
        int noAttributes = fold.noAttributes();
        assertEquals(parent.noAttributes(), noAttributes);
    }
    
    @Test
    public void testClassIndex() {
        int classIndex = fold.classIndex(new Object());
        assertEquals(parent.classIndex(new Object()), classIndex);
    }
    
    @Test
    public void testClassValue() {
        Object classValue = fold.classValue(0);
        assertEquals(parent.classValue(0), classValue);
    }
    
    @Test
    public void testCopy() {
        Dataset copy = fold.copy();
        assertNotNull(copy);
    }
    
    @Test
    public void testFoldIterator() {
        Fold.FoldIterator foldIterator = new Fold.FoldIterator(0);
        assertNotNull(foldIterator);
    }

}