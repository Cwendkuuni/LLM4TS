import static org.junit.Assert.*;
import org.junit.Test;

public class FoldTest {

    @Test
    public void testAdd() {
        // Test case for the add method
        Fold fold = new Fold(null, new int[]{1, 2, 3});
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            fold.add(null);
        });
        assertEquals("Cannot do this on a fold of a dataset", exception.getMessage());
    }

    @Test
    public void testClasses() {
        // Test case for the classes method
        Fold fold = new Fold(null, new int[]{1, 2, 3});
        assertEquals(null, fold.classes());
    }

    // Add test cases for other methods

}