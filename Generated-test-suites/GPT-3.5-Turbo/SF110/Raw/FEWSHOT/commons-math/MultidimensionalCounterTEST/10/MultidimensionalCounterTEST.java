
package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        // Arrange
        int[] size = { 2, 3 };

        // Act
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        // Assert
        assertEquals(2, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
    }

    @Test
    public void testConstructor_ZeroSize_ThrowsNotStrictlyPositiveException() {
        // Arrange
        int[] size = { 0, 3 };

        // Act & Assert
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(size));
    }

    @Test
    public void testGetCounts_ValidIndex_ReturnsCorrectCounts() throws OutOfRangeException {
        // Arrange
        int[] size = { 2, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        // Act & Assert
        assertArrayEquals(new int[] { 0, 0 }, counter.getCounts(0));
        assertArrayEquals(new int[] { 0, 1 }, counter.getCounts(1));
        assertArrayEquals(new int[] { 0, 2 }, counter.getCounts(2));
        assertArrayEquals(new int[] { 1, 0 }, counter.getCounts(3));
        assertArrayEquals(new int[] { 1, 1 }, counter.getCounts(4));
        assertArrayEquals(new int[] { 1, 2 }, counter.getCounts(5));
    }

    @Test
    public void testGetCounts_InvalidIndex_ThrowsOutOfRangeException() throws OutOfRangeException {
        // Arrange
        int[] size = { 2, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        // Act & Assert
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(-1));
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(6));
    }

    @Test
    public void testGetCount_ValidCounts_ReturnsCorrectCount() throws OutOfRangeException, DimensionMismatchException {
        // Arrange
        int[] size = { 2, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        // Act & Assert
        assertEquals(0, counter.getCount(0, 0));
        assertEquals(1, counter.getCount(0, 1));
        assertEquals(2, counter.getCount(0, 2));
        assertEquals(3, counter.getCount(1, 0));
        assertEquals(4, counter.getCount(1, 1));
        assertEquals(5, counter.getCount(1, 2));
    }

    @Test
    public void testGetCount_InvalidCounts_ThrowsOutOfRangeException() throws OutOfRangeException, DimensionMismatchException {
        // Arrange
        int[] size = { 2, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        // Act & Assert
        assertThrows(DimensionMismatchException.class, () -> counter.getCount(0));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(-1, 0));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(0, 3));
    }

    @Test
    public void testGetSize_ReturnsCorrectSize() throws NotStrictlyPositiveException {
        // Arrange
        int[] size = { 2, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        // Act & Assert
        assertEquals(6, counter.getSize());
    }

    @Test
    public void testToString_ReturnsCorrectStringRepresentation() throws NotStrictlyPositiveException {
        // Arrange
        int[] size = { 2, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        // Act & Assert
        assertEquals("[0][0]", counter.toString());
    }
    
    @Test
    public void testIterator() throws NotStrictlyPositiveException {
        // Arrange
        int[] size = { 2, 3 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        // Act & Assert
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount().intValue());
        assertArrayEquals(new int[] { 0, 0 }, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(1, iterator.next().intValue());
        assertEquals(1, iterator.getCount().intValue());
        assertArrayEquals(new int[] { 0, 1 }, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(1, iterator.getCount(1));
        assertEquals(2, iterator.next().intValue());
        assertEquals(2, iterator.getCount().intValue());
        assertArrayEquals(new int[] { 0, 2 }, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(2, iterator.getCount(1));
        assertEquals(3, iterator.next().intValue());
        assertEquals(3, iterator.getCount().intValue());
        assertArrayEquals(new int[] { 1, 0 }, iterator.getCounts());
        assertEquals(1, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
    }
}
