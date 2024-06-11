import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void constructorTest() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void getCountsTest() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(9);
        assertArrayEquals(new int[]{1, 1, 1}, counts);
    }

    @Test
    public void getCountTest() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(9, counter.getCount(1, 1, 1));
    }

    @Test
    public void getSizeTest() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void getSizesTest() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertArrayEquals(new int[]{3, 4, 2}, counter.getSizes());
    }

    @Test
    public void iteratorTest() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[]{0, 0, 0}, counts);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void constructorExceptionTest() {
        int[] size = {0, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    @Test(expected = OutOfRangeException.class)
    public void getCountsExceptionTest() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(27);
    }

    @Test(expected = OutOfRangeException.class)
    public void getCountExceptionTest() {
        int[] size = {3, 4, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(1, 5, 1);
    }

}