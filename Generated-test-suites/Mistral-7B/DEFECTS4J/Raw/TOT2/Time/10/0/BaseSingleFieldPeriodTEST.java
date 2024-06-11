package org.joda.time.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

import static org.joda.time.base.BaseSingleFieldPeriod.*;
import static org.joda.time.base.DurationFieldType.*;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(5, p.getValue());
        assertEquals(DAYS, p.getFieldType());
        assertEquals(Size.ONE, p.size());
    }

    /**
     * Test getFieldType.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(DAYS, p.getFieldType());
    }

    /**
     * Test getPeriodType.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(Standard.DAYS, p.getPeriodType());
    }

    /**
     * Test size.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(Size.ONE, p.size());
    }

    /**
     * Test getValue.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(5, p.getValue());
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5);
        assertEquals(p1, p2);

        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(10);
        assertNotEquals(p1, p3);

        BaseSingleFieldPeriod p4 = null;
        assertNotEquals(p1, p4);
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        int hashCode1 = p.hashCode();
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5);
        int hashCode2 = p2.hashCode();

        assertEquals(hashCode1, hashCode2);

        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(10);
        int hashCode3 = p3.hashCode();

        assertNotEquals(hashCode1, hashCode3);
    }

    /**
     * Test compareTo.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(5);

        int result1 = p1.compareTo(p2);
        int result2 = p2.compareTo(p1);
        int result3 = p1.compareTo(p3);
        int result4 = p3.compareTo(p1);

        assertTrue(result1 > 0);
        assertTrue(result2 < 0);
        assertTrue(result3 == 0);
        assertTrue(result4 == 0);
    }

    /**
     * Test add.
     */
    @Test
    public void testAdd() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1);

        BaseSingleFieldPeriod result = p1.plus(p2);

        assertEquals(4, result.getValue());
    }

    /**
     * Test add with negative period.
     */
    @Test
    public void testAddNegativePeriod() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(-1);

        BaseSingleFieldPeriod result = p1.plus(p2);

        assertEquals(2, result.getValue());
    }

    /**
     * Test add with null period.
     */
    @Test(expected = NullPointerException.class)
    public void testAddNullPeriod() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(3);
        p1.plus(null);
    }

    /**
     * Test subtract.
     */
    @Test
    public void testSubtract() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(2);

        BaseSingleFieldPeriod result = p1.minus(p2);

        assertEquals(3, result.getValue());
    }

    /**
     * Test subtract with negative period.
     */
    @Test
    public void testSubtractNegativePeriod() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(-2);

        BaseSingleFieldPeriod result = p1.minus(p2);

        assertEquals(7, result.getValue());
    }

    /**
     * Test subtract with null period.
     */
    @Test(expected = NullPointerException.class)
    public void testSubtractNullPeriod() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        p1.minus(null);
    }

    /**
     * Test get.
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(2);

        assertEquals(2, p.get(DAYS));
        assertEquals(0, p.get(HOURS));
    }

    /**
     * Test isSupported.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(2);

        assertTrue(p.isSupported(DAYS));
        assertTrue(p.isSupported(HOURS));
        assertFalse(p.isSupported(WEEKS));
    }

    /**
     * Test add with excessively large period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddExcessivelyLargePeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(Long.MAX_VALUE);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(Long.MAX_VALUE);

        p.plus(p2);
    }

    /**
     * Test getPeriodType.
     */
    @Test
    public void testGetPeriodTypeFromConstructor() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5, PeriodType.weeks());
        assertEquals(WEEKS, p.getFieldType());
        assertEquals(PeriodType.weeks(), p.getPeriodType());
    }

    /**
     * Test add with different period type.
     */
    @Test
    public void testAddDifferentPeriodType() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5, PERIODTYPE_DAYS);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(3, PERIODTYPE_WEEKS);

        BaseSingleFieldPeriod p3 = p1.plus(p2);

        assertEquals(5 + 3 * 7, p3.getValue());
    }

    /**
     * Test add edge case with minimum and maximum long value.
     */
    @Test
    public void testAddEdgeCases() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(Long.MIN_VALUE, DAYS);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(Long.MAX_VALUE, DAYS);

        BaseSingleFieldPeriod p3 = p1.plus(p2);

        long expected = Long.MAX_VALUE - Math.abs(Long.MIN_VALUE);

        assertEquals(expected, p3.getValue());
    }

    /**
     * Test add with negative period and edge case.
     */
    @Test
    public void testAddNegativePeriodEdgeCase() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(Long.MIN_VALUE, DAYS);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(-1, DAYS);

        BaseSingleFieldPeriod p3 = p1.plus(p2);

        long expected = Long.MIN_VALUE + 1;

        assertEquals(expected, p3.getValue());
    }

    /**
     * Test add with null period and edge case.
     */
    @Test(expected = NullPointerException.class)
    public void testAddNullPeriodEdgeCase() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(Long.MAX_VALUE, DAYS);
        p.plus(null);
    }

    /**
     * Test subtract with excessively large period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractExcessivelyLargePeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(Long.MIN_VALUE, DAYS);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(Long.MAX_VALUE, DAYS);

        p.minus(p2);
    }

    /**
     * Test subtract with negative period and edge case.
     */
    @Test
    public void testSubtractNegativePeriodEdgeCase() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(Long.MIN_VALUE, DAYS);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(-1, DAYS);

        BaseSingleFieldPeriod p3 = p1.minus(p2);

        long expected = Long.MIN_VALUE + 1;

        assertEquals(expected, p3.getValue());
    }

    /**
     * Test subtract with null period and edge case.
     */
    @Test(expected = NullPointerException.class)
    public void testSubtractNullPeriodEdgeCase() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(Long.MIN_VALUE, DAYS);
        p.minus(null);
    }

    /**
     * Test withThreadLocalRandom.
     */
    @Test
    public void testWithThreadLocalRandom() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;

        for (int i = 0; i < 100; i++) {
            int value = random.nextInt(max - min + 1) + min;
            BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(value, DAYS);
            assertEquals(value, p.getValue());
        }
    }
}