import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 0);
    }
    
    @Test
    public void testConstructorWithCollection() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(instance1);
        collection.add(instance2);
        
        DefaultDataset dataset = new DefaultDataset(collection);
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 2);
        assert(dataset.contains(instance1) && dataset.contains(instance2));
    }
    
    @Test
    public void testAddAll() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(instance1);
        collection.add(instance2);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(collection);
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 2);
        assert(dataset.contains(instance1) && dataset.contains(instance2));
    }
    
    @Test
    public void testAddAllAtIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(instance1);
        collection.add(instance2);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(0, collection);
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 2);
        assert(dataset.get(0) == instance1 && dataset.get(1) == instance2);
    }
    
    @Test
    public void testClear() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(instance1);
        collection.add(instance2);
        
        DefaultDataset dataset = new DefaultDataset(collection);
        dataset.clear();
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 0);
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 1);
        assert(dataset.contains(instance));
    }
    
    @Test
    public void testAddAtIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(0, instance1);
        dataset.add(1, instance2);
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 2);
        assert(dataset.get(0) == instance1 && dataset.get(1) == instance2);
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instance);
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 1);
        assert(dataset.contains(instance));
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.insertElementAt(instance1, 0);
        dataset.insertElementAt(instance2, 1);
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 2);
        assert(dataset.get(0) == instance1 && dataset.get(1) == instance2);
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instance1);
        dataset.setElementAt(instance1, 0);
        dataset.setElementAt(instance2, 0);
        
        assert(dataset.classes().isEmpty());
        assert(dataset.noAttributes() == 0);
        assert(dataset.size() == 1);
        assert(dataset.get(0) == instance2);
    }
    
    @Test
    public void testInstance() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        
        assert(dataset.instance(0) == instance1);
        assert(dataset.instance(1) == instance2);
    }
    
    @Test
    public void testClasses() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        
        assert(dataset.classes().isEmpty());
        
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        
        assert(dataset.classes().size() == 2);
        assert(dataset.classes().contains("class1") && dataset.classes().contains("class2"));
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        
        DistanceMeasure dm = new EuclideanDistance();
        
        Set<Instance> nearest = dataset.kNearest(2, instance1, dm);
        assert(nearest.size() == 2);
        assert(nearest.contains(instance2) && nearest.contains(instance3));
    }
    
    @Test
    public void testRemoveFarthest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        vector.put(instance1, 1.0);
        vector.put(instance2, 2.0);
        vector.put(instance3, 3.0);
        
        DefaultDataset dataset = new DefaultDataset();
        
        double max = dataset.removeFarthest(vector);
        assert(max == 3.0);
        assert(!vector.containsKey(instance3));
    }
    
    @Test
    public void testFolds() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        Instance instance4 = new Instance();
        Instance instance5 = new Instance();
        Instance instance6 = new Instance();
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(instance1);
        collection.add(instance2);
        collection.add(instance3);
        collection.add(instance4);
        collection.add(instance5);
        collection.add(instance6);
        
        DefaultDataset dataset = new DefaultDataset(collection);
        Random rg = new Random();
        
        Dataset[] folds = dataset.folds(3, rg);
        assert(folds.length == 3);
        
        int totalSize = 0;
        for (int i = 0; i < 3; i++) {
            totalSize += folds[i].size();
        }
        
        assert(totalSize == collection.size());
    }
    
    @Test
    public void testNoAttributes() {
        Instance instance = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        
        assert(dataset.noAttributes() == instance.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        
        assert(dataset.classIndex(null) == -1);
        
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        dataset.add(instance1);
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance2);
        
        assert(dataset.classIndex("class1") == 0);
        assert(dataset.classIndex("class2") == 1);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        
        assert(dataset.classValue(0) == null);
        
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        dataset.add(instance1);
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance2);
        
        assert(dataset.classValue(0).equals("class1"));
        assert(dataset.classValue(1).equals("class2"));
    }
    
    @Test
    public void testCopy() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        
        Dataset copy = dataset.copy();
        
        assert(copy instanceof DefaultDataset);
        assert(copy.size() == dataset.size());
        assert(copy.contains(instance1) && copy.contains(instance2));
    }
}