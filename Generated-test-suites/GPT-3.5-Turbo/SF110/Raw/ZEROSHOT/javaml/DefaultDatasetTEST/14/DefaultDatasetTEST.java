import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.Test;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> coll1 = new ArrayList<>();
        Instance instance1 = new Instance();
        coll1.add(instance1);
        Collection<Instance> coll2 = new ArrayList<>();
        Instance instance2 = new Instance();
        coll2.add(instance2);

        boolean result1 = dataset.addAll(coll1);
        boolean result2 = dataset.addAll(coll2);

        assert(result1 && result2);
        assert(dataset.size() == 2);
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> coll1 = new ArrayList<>();
        Instance instance1 = new Instance();
        coll1.add(instance1);
        Collection<Instance> coll2 = new ArrayList<>();
        Instance instance2 = new Instance();
        coll2.add(instance2);

        boolean result1 = dataset.addAll(0, coll1);
        boolean result2 = dataset.addAll(1, coll2);

        assert(result1 && result2);
        assert(dataset.size() == 2);
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);

        dataset.clear();

        assert(dataset.isEmpty());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();

        boolean result1 = dataset.add(instance1);
        boolean result2 = dataset.add(instance2);

        assert(result1 && result2);
        assert(dataset.size() == 2);
    }

    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();

        dataset.add(0, instance1);
        dataset.add(1, instance2);

        assert(dataset.size() == 2);
        assert(dataset.get(0) == instance1);
        assert(dataset.get(1) == instance2);
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();

        dataset.addElement(instance1);
        dataset.addElement(instance2);

        assert(dataset.size() == 2);
        assert(dataset.get(0) == instance1);
        assert(dataset.get(1) == instance2);
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();

        dataset.insertElementAt(instance1, 0);
        dataset.insertElementAt(instance2, 1);

        assert(dataset.size() == 2);
        assert(dataset.get(0) == instance1);
        assert(dataset.get(1) == instance2);
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addElement(instance1);

        dataset.setElementAt(instance2, 0);

        assert(dataset.size() == 1);
        assert(dataset.get(0) == instance2);
    }

    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.addElement(instance1);

        Instance instance = dataset.instance(0);

        assert(instance == instance1);
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        TreeSet<Object> classes = new TreeSet<>();
        classes.add("class1");
        classes.add("class2");
        dataset.classes = classes;

        SortedSet<Object> result = dataset.classes();

        assert(result.equals(classes));
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addElement(instance1);

        Set<Instance> result = dataset.kNearest(1, instance2, new DistanceMeasure());

        assert(result.size() == 0);
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        Dataset[] folds = dataset.folds(5, rg);

        assert(folds.length == 5);
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());

        int result = dataset.noAttributes();

        assert(result == 0);
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        TreeSet<Object> classes = new TreeSet<>();
        classes.add("class1");
        classes.add("class2");
        dataset.classes = classes;

        int result1 = dataset.classIndex("class1");
        int result2 = dataset.classIndex("class3");

        assert(result1 == 0);
        assert(result2 == -1);
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        TreeSet<Object> classes = new TreeSet<>();
        classes.add("class1");
        classes.add("class2");
        dataset.classes = classes;

        Object result1 = dataset.classValue(0);
        Object result2 = dataset.classValue(2);

        assert(result1.equals("class1"));
        assert(result2 == null);
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());

        Dataset result = dataset.copy();

        assert(result.size() == dataset.size());
        assert(result.get(0) != dataset.get(0));
    }
}