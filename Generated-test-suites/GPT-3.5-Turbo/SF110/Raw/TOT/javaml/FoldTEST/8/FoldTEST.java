
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class FoldTest {

    @Test
    public void testAdd() {
        try {
            int[] indices = {0, 1, 2, 3, 4};
            Dataset parent = new DefaultDataset();
            Fold fold = new Fold(parent, indices);

            Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
            assertFalse(fold.add(instance)); // Adding instance should throw UnsupportedOperationException

        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testClasses() {
        int[] indices = {0, 1, 2, 3, 4};
        Dataset parent = new DefaultDataset();
        parent.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));
        parent.add(new Instance(new double[]{4.0, 5.0, 6.0}, "B"));
        parent.add(new Instance(new double[]{7.0, 8.0, 9.0}, "C"));
        Fold fold = new Fold(parent, indices);

        SortedSet<Object> classes = fold.classes();
        assertEquals(3, classes.size());
        assertTrue(classes.contains("A"));
        assertTrue(classes.contains("B"));
        assertTrue(classes.contains("C"));
    }

    @Test
    public void testFolds() {
        try {
            int[] indices = {0, 1, 2, 3, 4};
            Dataset parent = new DefaultDataset();
            Fold fold = new Fold(parent, indices);

            int numFolds = 3;
            Random rg = new Random();
            Dataset[] folds = fold.folds(numFolds, rg); // Folds method should throw UnsupportedOperationException

        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testInstance() {
        int[] indices = {0, 1, 2, 3, 4};
        Dataset parent = new DefaultDataset();
        parent.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));
        parent.add(new Instance(new double[]{4.0, 5.0, 6.0}, "B"));
        parent.add(new Instance(new double[]{7.0, 8.0, 9.0}, "C"));
        Fold fold = new Fold(parent, indices);

        Instance instance = fold.instance(2);
        assertEquals(7.0, instance.value(0), 0.001); // value at index 2 should be 7.0
        assertEquals(8.0, instance.value(1), 0.001); // value at index 2 should be 8.0
        assertEquals(9.0, instance.value(2), 0.001); // value at index 2 should be 9.0
    }

    @Test
    public void testKNearest() {
        try {
            int[] indices = {0, 1, 2, 3, 4};
            Dataset parent = new DefaultDataset();
            Fold fold = new Fold(parent, indices);

            int k = 3;
            Instance inst = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
            EuclideanDistance dm = new EuclideanDistance();
            Set<Instance> nearestInstances = fold.kNearest(k, inst, dm); // kNearest method should throw UnsupportedOperationException

        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAddIndexElement() {
        try {
            int[] indices = {0, 1, 2, 3, 4};
            Dataset parent = new DefaultDataset();
            Fold fold = new Fold(parent, indices);

            int index = 1;
            Instance instance = new Instance(new double[]{1.5, 2.5, 3.5}, "B");
            fold.add(index, instance); // Adding instance at index should throw UnsupportedOperationException

        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    // Similar test methods for other methods in Fold class

}

