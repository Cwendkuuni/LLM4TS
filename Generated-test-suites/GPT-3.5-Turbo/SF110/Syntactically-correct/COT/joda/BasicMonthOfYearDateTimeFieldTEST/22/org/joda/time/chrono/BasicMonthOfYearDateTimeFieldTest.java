import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = DateTimeUtils.currentTimeMillis();
        int month = field.get(millis);
        assertTrue(month >= 1 && month <= 12);
    }

    @Test
    public void testAdd_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = DateTimeUtils.currentTimeMillis();
        long result = field.add(millis, 6);
        assertEquals(millis + 6 * field.getAverageMillisPerUnit(), result);
    }

    @Test
    public void testAdd_long() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = DateTimeUtils.currentTimeMillis();
        long result = field.add(millis, 6L);
        assertEquals(millis + 6L * field.getAverageMillisPerUnit(), result);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = DateTimeUtils.currentTimeMillis();
        long result = field.addWrapField(millis, 6);
        assertEquals(millis, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis1 = DateTimeUtils.currentTimeMillis();
        long millis2 = millis1 + 1000;
        long difference = field.getDifferenceAsLong(millis1, millis2);
        assertEquals(0, difference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = DateTimeUtils.currentTimeMillis();
        long result = field.set(millis, 6);
        int month = field.get(result);
        assertEquals(6, month);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 3);
        long millis = DateTimeUtils.currentTimeMillis();
        boolean isLeap = field.isLeap(millis);
        assertEquals(field.get(millis) == 3, isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 3);
        long millis = DateTimeUtils.currentTimeMillis();
        int leapAmount = field.getLeapAmount(millis);
        assertEquals(field.get(millis) == 3 ? 1 : 0, leapAmount);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = DateTimeUtils.currentTimeMillis();
        long result = field.roundFloor(millis);
        int year = new DateTime(millis).getYear();
        int month = new DateTime(result).getMonthOfYear();
        assertEquals(year + "-" + month + "-01T00:00:00.000", new DateTime(result).toString());
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long millis = DateTimeUtils.currentTimeMillis();
        long result = field.remainder(millis);
        assertEquals(0L, result);
    }

    @Test
    public void testAdd_WithPartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);

        // Test case with monthOfYear field in the partial
        Partial partial = new Partial().with(DateTimeFieldType.monthOfYear(), 3);
        int[] values = {1, 1, 1};
        int[] result = field.add(partial, 6, values, 0);
        assertEquals(3, result[0]);
        assertEquals(1, result[1]);
        assertEquals(1, result[2]);

        // Test case without monthOfYear field in the partial
        partial = new Partial().with(DateTimeFieldType.year(), 2021);
        result = field.add(partial, 6, values, 0);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(1, result[2]);
    }

}