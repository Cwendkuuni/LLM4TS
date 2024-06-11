import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(6, field.get(1589638400000L)); // June, 2020
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(1593561600000L, field.add(1589638400000L, 1)); // July, 2020
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(1604208000000L, field.addWrapField(1589638400000L, 17)); // November, 2020
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(1593561600000L, field.set(1589638400000L, 7)); // July, 2020
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertNotNull(field.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertFalse(field.isLeap(1589638400000L)); // June, 2020
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(0, field.getLeapAmount(1589638400000L)); // June, 2020
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertNotNull(field.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(1577836800000L, field.roundFloor(1589638400000L)); // January, 2020
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(76800000L, field.remainder(1589638400000L)); // 21:00:00
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(8, field.getDifferenceAsLong(1593561600000L, 1589638400000L)); // July - June = 1 month
    }

    @Test
    public void testAddLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertEquals(1593561600000L, field.add(1589638400000L, 1)); // July, 2020
    }

    @Test
    public void testAddPartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        int[] values = {2020, 6, 24}; // June 24, 2020
        assertArrayEquals(new int[] {2020, 8, 24}, field.add(null, 1, values, 2)); // August 24, 2020
    }

}