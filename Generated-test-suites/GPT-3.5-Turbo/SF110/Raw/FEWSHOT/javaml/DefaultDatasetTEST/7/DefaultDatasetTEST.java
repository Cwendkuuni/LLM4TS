package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultDatasetTest {
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        assertTrue(dataset.add(instance1));
        assertTrue(dataset.add(instance2));
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2)));
        assertEquals(2, dataset.size());
        
        assertTrue(dataset.addAll(1, Arrays.asList(instance3)));
        assertEquals(3, dataset.size());
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addAll(Arrays.asList(instance1, instance2));
        assertEquals(2, dataset.size());
        
        dataset.clear();
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addElement(instance1);
        dataset.insertElementAt(instance2, 1);
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addElement(instance1);
        dataset.setElementAt(instance2, 0);
        
        assertEquals(1, dataset.size());
        assertEquals(instance2, dataset.get(0));
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertTrue(dataset.classes().contains("class1"));
        assertTrue(dataset.classes().contains("class2"));
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setAttribute("attr1", 1);
        
        Instance instance2 = new Instance();
        instance2.setAttribute("attr1", 2);
        
        Instance instance3 = new Instance();
        instance3.setAttribute("attr1", 3);
        
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        
        Set<Instance> nearest = dataset.kNearest(2, instance1, new EuclideanDistance());
        assertEquals(2, nearest.size());
        assertTrue(nearest.contains(instance2));
        assertTrue(nearest.contains(instance3));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        Instance instance4 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        dataset.add(instance4);
        
        Dataset[] folds = dataset.folds(2, new Random());
        assertEquals(2, folds.length);
        
        assertEquals(2, folds[0].size());
        assertEquals(2, folds[1].size());
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setAttribute("attr1", 1);
        instance1.setAttribute("attr2", 2);
        
        Instance instance2 = new Instance();
        instance2.setAttribute("attr1", 3);
        instance2.setAttribute("attr2", 4);
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals(2, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
        assertEquals(-1, dataset.classIndex("class3"));
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
        assertNull(dataset.classValue(2));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setAttribute("attr1", 1);
        instance1.setAttribute("attr2", 2);
        
        Instance instance2 = new Instance();
        instance2.setAttribute("attr1", 3);
        instance2.setAttribute("attr2", 4);
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        
        for (int i = 0; i < dataset.size(); i++) {
            Instance original = dataset.get(i);
            Instance copied = copy.get(i);
            
            assertNotSame(original, copied);
            assertEquals(original.getAttribute("attr1"), copied.getAttribute("attr1"));
            assertEquals(original.getAttribute("attr2"), copied.getAttribute("attr2"));
        }
    }
}