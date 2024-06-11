import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import net.sf.javaml.core.*;

public class FoldTest {
    private Dataset parent;
    private Fold fold;

    @Before
    public void setUp() {
        parent = new DefaultDataset();
        parent.add(new Instance(new double[] { 1.0, 2.0, 3.0 }, "A"));
        parent.add(new Instance(new double[] { 4.0, 5.0, 6.0 }, "B"));
        parent.add(new Instance(new double[] { 7.0, 8.0, 9.0 }, "C"));
        fold = new Fold(parent, new int[] { 1, 2 });
    }

    @Test
    public void testAdd() {
        try {
            fold.add(new Instance(new double[] { 10.0, 11.0, 12.0 }, "D"));
            fail("Add should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testClasses() {
        assertTrue(fold.classes().contains("A"));
        assertTrue(fold.classes().contains("B"));
        assertFalse(fold.classes().contains("C"));
    }

    @Test
    public void testFolds() {
        try {
            fold.folds(5, new Random());
            fail("Folds method should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    // Add test cases for other methods as needed

}