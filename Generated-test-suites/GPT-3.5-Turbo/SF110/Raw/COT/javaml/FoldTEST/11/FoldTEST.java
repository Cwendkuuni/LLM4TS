import static org.junit.Assert.*;
import org.junit.Test;

public class FoldTest {

    @Test
    public void testAdd() {
        Fold fold = new Fold(null, null);
        Instance instance = new Instance();
        
        try {
            fold.add(instance);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    @Test
    public void testClasses() {
        Fold fold = new Fold(null, null);
        SortedSet<Object> classes = fold.classes();
        
        // Assert that classes() returns the same instance as parent.classes()
        assertSame(fold.parent.classes(), classes);
    }
    
    @Test
    public void testFolds() {
        Fold fold = new Fold(null, null);
        
        try {
            fold.folds(2, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testInstance() {
        Fold fold = new Fold(null, new int[]{0, 1, 2});
        
        // Test valid index
        Instance instance = fold.instance(1);
        assertEquals(1, instance.getIndex());
        
        // Test out of bounds index
        try {
            fold.instance(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 3, Size: 3", e.getMessage());
        }
    }
    
    @Test
    public void testKNearest() {
        Fold fold = new Fold(null, null);
        Instance instance = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        
        try {
            fold.kNearest(3, instance, dm);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Method is not yet implemented", e.getMessage());
        }
    }
    
    @Test
    public void testAddWithIndex() {
        Fold fold = new Fold(null, null);
        Instance instance = new Instance();
        
        try {
            fold.add(0, instance);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot do this on a fold of a dataset", e.getMessage());
        }
    }
    
    // Continue adding test cases for the remaining public methods in the class

}