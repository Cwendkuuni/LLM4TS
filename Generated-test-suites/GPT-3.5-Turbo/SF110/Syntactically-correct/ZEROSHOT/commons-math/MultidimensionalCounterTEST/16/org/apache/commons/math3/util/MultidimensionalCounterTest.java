import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        // Create new instance of MultidimensionalCounter
        // Test exceptions for size <= 0
    }

    @Test
    public void testIterator() {
        // Create new instance of MultidimensionalCounter
        // Test iterator() method
        // Test hasNext() method
        // Test next() method
        // Test getCount() method
        // Test getCounts() method
        // Test getCount(dim) method
        // Test remove() method
    }

    @Test
    public void testGetDimension() {
        // Create new instance of MultidimensionalCounter
        // Test getDimension() method
    }

    @Test
    public void testGetCounts() {
        // Create new instance of MultidimensionalCounter
        // Test getCounts(index) method with valid and out of range indices
    }

    @Test
    public void testGetCount() {
        // Create new instance of MultidimensionalCounter
        // Test getCount(c) method with valid and out of range inputs
    }

    @Test
    public void testGetSize() {
        // Create new instance of MultidimensionalCounter
        // Test getSize() method
    }

    @Test
    public void testGetSizes() {
        // Create new instance of MultidimensionalCounter
        // Test getSizes() method
    }

    @Test
    public void testToString() {
        // Create new instance of MultidimensionalCounter
        // Test toString() method
    }

    @Test
    public void testIteratorHasNext() {
        // Create new instance of MultidimensionalCounter
        // Test iterator().hasNext() method with different counter values
    }

    @Test
    public void testIteratorNext() {
        // Create new instance of MultidimensionalCounter
        // Test iterator().next() method with different counter values
    }

    @Test
    public void testIteratorGetCount() {
        // Create new instance of MultidimensionalCounter
        // Test iterator().getCount() method with different counter values
    }

    @Test
    public void testIteratorGetCounts() {
        // Create new instance of MultidimensionalCounter
        // Test iterator().getCounts() method with different counter values
    }

    @Test
    public void testIteratorGetCountDim() {
        // Create new instance of MultidimensionalCounter
        // Test iterator().getCount(dim) method with different counter values
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        // Create new instance of MultidimensionalCounter
        // Test iterator().remove() method
    }
}