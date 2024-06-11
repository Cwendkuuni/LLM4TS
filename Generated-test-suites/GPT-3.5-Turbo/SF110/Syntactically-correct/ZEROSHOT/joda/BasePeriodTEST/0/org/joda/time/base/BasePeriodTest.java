package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testGetValue() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(1, basePeriod.getValue(0));
        assertEquals(2, basePeriod.getValue(1));
        assertEquals(3, basePeriod.getValue(2));
        assertEquals(4, basePeriod.getValue(3));
        assertEquals(5, basePeriod.getValue(4));
        assertEquals(6, basePeriod.getValue(5));
        assertEquals(7, basePeriod.getValue(6));
        assertEquals(8, basePeriod.getValue(7));
    }

    @Test
    public void testMergePeriodInto() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        int[] array = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        int[] mergedArray = basePeriod.mergePeriodInto(array, basePeriod);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, mergedArray);
    }

    @Test
    public void testAddPeriodInto() {
        BasePeriod basePeriod1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        BasePeriod basePeriod2 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        int[] array = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        int[] addedArray = basePeriod1.addPeriodInto(array, basePeriod2);
        assertArrayEquals(new int[]{2, 4, 6, 8, 10, 12, 14, 16}, addedArray);
    }

    @Test
    public void testSetValue() {
        BasePeriod basePeriod = new BasePeriod(0, 0, 0, 0, 0, 0, 0, 0);
        basePeriod.setValue(0, 1);
        basePeriod.setValue(1, 2);
        basePeriod.setValue(2, 3);
        basePeriod.setValue(3, 4);
        basePeriod.setValue(4, 5);
        basePeriod.setValue(5, 6);
        basePeriod.setValue(6, 7);
        basePeriod.setValue(7, 8);
        assertEquals(1, basePeriod.getValue(0));
        assertEquals(2, basePeriod.getValue(1));
        assertEquals(3, basePeriod.getValue(2));
        assertEquals(4, basePeriod.getValue(3));
        assertEquals(5, basePeriod.getValue(4));
        assertEquals(6, basePeriod.getValue(5));
        assertEquals(7, basePeriod.getValue(6));
        assertEquals(8, basePeriod.getValue(7));
    }
}