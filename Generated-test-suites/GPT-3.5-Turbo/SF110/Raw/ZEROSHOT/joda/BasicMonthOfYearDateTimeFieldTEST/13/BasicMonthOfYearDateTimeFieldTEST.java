import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        int result = field.get(n);
        assertEquals(0, result);
    }

    @Test
    public void testAddLongInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        int n2 = 0; // replace with appropriate value
        long result = field.add(n, n2);
        assertEquals(0L, result);
    }

    @Test
    public void testAddLongLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        long n2 = 0L; // replace with appropriate value
        long result = field.add(n, n2);
        assertEquals(0L, result);
    }

    @Test
    public void testAddReadablePartialIntIntArrayInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        ReadablePartial readablePartial = null; // replace with appropriate value
        int n = 0; // replace with appropriate value
        int[] array = null; // replace with appropriate value
        int n2 = 0; // replace with appropriate value
        int[] result = field.add(readablePartial, n, array, n2);
        assertArrayEquals(new int[0], result);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        int n2 = 0; // replace with appropriate value
        long result = field.addWrapField(n, n2);
        assertEquals(0L, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        long set = 0L; // replace with appropriate value
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(0L, result);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        int n2 = 0; // replace with appropriate value
        long result = field.set(n, n2);
        assertEquals(0L, result);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        DurationField result = field.getRangeDurationField();
        assertNotNull(result);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        boolean result = field.isLeap(n);
        assertFalse(result);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        int result = field.getLeapAmount(n);
        assertEquals(0, result);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        DurationField result = field.getLeapDurationField();
        assertNotNull(result);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        int result = field.getMinimumValue();
        assertEquals(1, result);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        int result = field.getMaximumValue();
        assertEquals(0, result);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        long result = field.roundFloor(n);
        assertEquals(0L, result);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0L; // replace with appropriate value
        long result = field.remainder(n);
        assertEquals(0L, result);
    }
}