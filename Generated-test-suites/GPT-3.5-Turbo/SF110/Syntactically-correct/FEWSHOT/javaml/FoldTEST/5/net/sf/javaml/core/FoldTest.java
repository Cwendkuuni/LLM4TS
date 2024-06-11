package net.sf.javaml.core;
import org.junit.Assert;
import org.junit.Test;
import java.util.SortedSet;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.Random;
import java.util.Collection;
import java.util.Set;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class FoldTest {

    @Test
    public void testAdd() {
        Fold fold = new Fold(null, null);
        Instance instance = new Instance(null);
        
        try {
            fold.add(instance);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testClasses() {
        Fold fold = new Fold(null, null);
        
        SortedSet<Object> result = fold.classes();
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testFolds() {
        Fold fold = new Fold(null, null);
        int numFolds = 0;
        Random rg = null;
        
        try {
            fold.folds(numFolds, rg);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testInstance() {
        Fold fold = new Fold(null, null);
        int index = 0;
        
        Instance result = fold.instance(index);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testKNearest() {
        Fold fold = new Fold(null, null);
        int k = 0;
        Instance inst = null;
        DistanceMeasure dm = null;
        
        try {
            fold.kNearest(k, inst, dm);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testAddAtIndex() {
        Fold fold = new Fold(null, null);
        int index = 0;
        Instance element = new Instance(null);
        
        try {
            fold.add(index, element);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testAddAll() {
        Fold fold = new Fold(null, null);
        Collection<Instance> c = new Vector<Instance>();
        
        try {
            fold.addAll(c);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testAddAllAtIndex() {
        Fold fold = new Fold(null, null);
        int index = 0;
        Collection<Instance> c = new Vector<Instance>();
        
        try {
            fold.addAll(index, c);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testClear() {
        Fold fold = new Fold(null, null);
        
        try {
            fold.clear();
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testContains() {
        Fold fold = new Fold(null, null);
        Object o = null;
        
        try {
            fold.contains(o);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAll() {
        Fold fold = new Fold(null, null);
        Collection<?> c = null;
        
        try {
            fold.containsAll(c);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testGet() {
        Fold fold = new Fold(null, null);
        int index = 0;
        
        Instance result = fold.get(index);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testIndexOf() {
        Fold fold = new Fold(null, null);
        Object o = null;
        
        try {
            fold.indexOf(o);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(null, null);
        
        boolean result = fold.isEmpty();
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIterator() {
        Fold fold = new Fold(null, null);
        
        Iterator<Instance> result = fold.iterator();
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testLastIndexOf() {
        Fold fold = new Fold(null, null);
        Object o = null;
        
        try {
            fold.lastIndexOf(o);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testListIterator() {
        Fold fold = new Fold(null, null);
        
        ListIterator<Instance> result = fold.listIterator();
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testListIteratorWithIndex() {
        Fold fold = new Fold(null, null);
        int index = 0;
        
        ListIterator<Instance> result = fold.listIterator(index);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testRemoveElement() {
        Fold fold = new Fold(null, null);
        Object o = null;
        
        try {
            fold.remove(o);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRemoveAtIndex() {
        Fold fold = new Fold(null, null);
        int index = 0;
        
        try {
            fold.remove(index);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRemoveAll() {
        Fold fold = new Fold(null, null);
        Collection<?> c = null;
        
        try {
            fold.removeAll(c);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testRetainAll() {
        Fold fold = new Fold(null, null);
        Collection<?> c = null;
        
        try {
            fold.retainAll(c);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        Fold fold = new Fold(null, null);
        int index = 0;
        Instance element = new Instance(null);
        
        try {
            fold.set(index, element);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testSize() {
        Fold fold = new Fold(null, null);
        
        int result = fold.size();
        
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testSubList() {
        Fold fold = new Fold(null, null);
        int fromIndex = 0;
        int toIndex = 0;
        
        try {
            fold.subList(fromIndex, toIndex);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("You cannot do this on a fold.", e.getMessage());
        }
    }
    
    @Test
    public void testToArray() {
        Fold fold = new Fold(null, null);
        
        Object[] result = fold.toArray();
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testToArrayWithParameter() {
        Fold fold = new Fold(null, null);
        Object[] a = new Object[0];
        
        Object[] result = fold.toArray(a);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(null, null);
        
        int result = fold.noAttributes();
        
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testClassIndex() {
        Fold fold = new Fold(null, null);
        Object clazz = null;
        
        int result = fold.classIndex(clazz);
        
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testClassValue() {
        Fold fold = new Fold(null, null);
        int index = 0;
        
        Object result = fold.classValue(index);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testCopy() {
        Fold fold = new Fold(null, null);
        
        Dataset result = fold.copy();
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFoldIterator() {
        Fold fold = new Fold(null, null);
        int index = 0;
        
        Fold.FoldIterator result = new Fold.FoldIterator(fold, index);
        
        Assert.assertNotNull(result);
    }
}