import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DefaultDataset;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetCollection() {
        // Test public DefaultDataset(Collection<Instance> coll) method
    }

    @Test
    public void testDefaultDataset() {
        // Test public DefaultDataset() method
    }

    @Test
    public void testCheckCollection() {
        // Test private void check(final Collection<? extends Instance> c) method
    }

    @Test
    public void testCheckInstance() {
        // Test private void check(final Instance i) method
    }

    @Test
    public void testAddAllCollection() {
        // Test synchronized boolean addAll(final Collection<? extends Instance> c) method
    }

    @Test
    public void testAddAllIntCollection() {
        // Test synchronized boolean addAll(final int index, final Collection<? extends Instance> c) method
    }

    @Test
    public void testClear() {
        // Test void clear() method
    }

    @Test
    public void testAddInstance() {
        // Test synchronized boolean add(final Instance e) method
    }

    @Test
    public void testAddIntInstance() {
        // Test void add(final int index, final Instance e) method
    }

    @Test
    public void testAddElementInstance() {
        // Test synchronized void addElement(final Instance e) method
    }

    @Test
    public void testInsertElementAtInstanceInt() {
        // Test synchronized void insertElementAt(final Instance e, final int index) method
    }

    @Test
    public void testSetElementAtInstanceInt() {
        // Test synchronized void setElementAt(final Instance e, final int index) method
    }

    @Test
    public void testInstanceInt() {
        // Test Instance instance(final int index) method
    }

    @Test
    public void testClasses() {
        // Test SortedSet<Object> classes() method
    }

    @Test
    public void testKNearest() {
        // Test Set<Instance> kNearest(final int k, final Instance inst, final DistanceMeasure dm) method
    }

    @Test
    public void testRemoveFarthest() {
        // Test private double removeFarthest(final Map<Instance, Double> vector) method
    }

    @Test
    public void testFolds() {
        // Test Dataset[] folds(final int numFolds, final Random rg) method
    }

    @Test
    public void testNoAttributes() {
        // Test int noAttributes() method
    }

    @Test
    public void testClassIndex() {
        // Test int classIndex(final Object clazz) method
    }

    @Test
    public void testClassValue() {
        // Test Object classValue(final int index) method
    }

    @Test
    public void testCopy() {
        // Test Dataset copy() method
    }

}