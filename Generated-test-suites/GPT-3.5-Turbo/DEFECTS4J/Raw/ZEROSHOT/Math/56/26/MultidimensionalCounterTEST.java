import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.util.MathUtils;

public class MultidimensionalCounterTest {
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ThrowsNotStrictlyPositiveException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-1, 2, 3);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ThrowsNotStrictlyPositiveException2() {
        MultidimensionalCounter counter = new MultidimensionalCounter(0, 2, 3);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_ThrowsDimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(1, 2);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_ThrowsOutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0, 3);
    }
    
    @Test
    public void testConstructor_GetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assert counter.getSize() == 24;
    }
    
    @Test
    public void testConstructor_GetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assert counter.getDimension() == 3;
    }
    
    @Test
    public void testConstructor_GetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] sizes = counter.getSizes();
        assert sizes.length == 3;
        assert sizes[0] == 2;
        assert sizes[1] == 4;
        assert sizes[2] == 3;
    }
    
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = counter.getCounts(12);
        assert counts.length == 3;
        assert counts[0] == 1;
        assert counts[1] == 0;
        assert counts[2] == 0;
    }
    
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        assert iterator.hasNext();
        assert iterator.next() == 0;
        
        assert iterator.hasNext();
        assert iterator.next() == 1;
        
        assert iterator.hasNext();
        assert iterator.next() == 2;
        
        assert iterator.hasNext();
        assert iterator.next() == 3;
        
        assert iterator.hasNext();
        assert iterator.next() == 4;
        
        assert iterator.hasNext();
        assert iterator.next() == 5;
        
        assert iterator.hasNext();
        assert iterator.next() == 6;
        
        assert iterator.hasNext();
        assert iterator.next() == 7;
        
        assert iterator.hasNext();
        assert iterator.next() == 8;
        
        assert iterator.hasNext();
        assert iterator.next() == 9;
        
        assert iterator.hasNext();
        assert iterator.next() == 10;
        
        assert iterator.hasNext();
        assert iterator.next() == 11;
        
        assert iterator.hasNext();
        assert iterator.next() == 12;
        
        assert iterator.hasNext();
        assert iterator.next() == 13;
        
        assert iterator.hasNext();
        assert iterator.next() == 14;
        
        assert iterator.hasNext();
        assert iterator.next() == 15;
        
        assert iterator.hasNext();
        assert iterator.next() == 16;
        
        assert iterator.hasNext();
        assert iterator.next() == 17;
        
        assert iterator.hasNext();
        assert iterator.next() == 18;
        
        assert iterator.hasNext();
        assert iterator.next() == 19;
        
        assert iterator.hasNext();
        assert iterator.next() == 20;
        
        assert iterator.hasNext();
        assert iterator.next() == 21;
        
        assert iterator.hasNext();
        assert iterator.next() == 22;
        
        assert iterator.hasNext();
        assert iterator.next() == 23;
        
        assert !iterator.hasNext();
    }
}