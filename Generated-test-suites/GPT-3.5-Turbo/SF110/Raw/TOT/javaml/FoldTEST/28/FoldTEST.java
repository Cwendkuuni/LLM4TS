import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.*;
import java.util.*;

public class FoldTest {

    // Test case for add() method
    @Test
    public void testAdd() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        Instance instance = new Instance(new double[]{1.0, 2.0}, "A");
        
        try {
            fold.add(instance);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // exception expected
        }
    }
    
    // Test case for classes() method
    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance(new double[]{1.0, 2.0}, "A"));
        parent.add(new Instance(new double[]{2.0, 3.0}, "B"));
        parent.add(new Instance(new double[]{3.0, 4.0}, "C"));
        
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        SortedSet<Object> classes = fold.classes();
        
        assertEquals(3, classes.size());
        assertTrue(classes.contains("A"));
        assertTrue(classes.contains("B"));
        assertTrue(classes.contains("C"));
    }
    
    // Test case for folds() method
    @Test
    public void testFolds() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance(new double[]{1.0, 2.0}, "A"));
        parent.add(new Instance(new double[]{2.0, 3.0}, "B"));
        parent.add(new Instance(new double[]{3.0, 4.0}, "C"));
        parent.add(new Instance(new double[]{4.0, 5.0}, "D"));
        
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        try {
            Dataset[] folds = fold.folds(2, new Random());
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // exception expected
        }
    }
}