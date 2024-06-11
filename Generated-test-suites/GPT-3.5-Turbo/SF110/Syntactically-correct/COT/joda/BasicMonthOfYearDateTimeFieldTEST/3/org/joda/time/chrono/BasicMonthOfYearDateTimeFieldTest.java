import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        assertFalse(field.isLenient);
    }

    @Test
    public void testGet() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        int expected = 1; // example value
        assertEquals(expected, field.get(n));
    }

    @Test
    public void testAddInt1() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        int n2 = 1; // example value
        long expected = 1; // example value
        assertEquals(expected, field.add(n, n2));
    }

    @Test
    public void testAddInt2() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        int n2 = -1; // example value
        long expected = -1; // example value
        assertEquals(expected, field.add(n, n2));
    }

    @Test
    public void testAddLong1() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        long n2 = 1L; // example value
        long expected = 1L; // example value
        assertEquals(expected, field.add(n, n2));
    }

    @Test
    public void testAddLong2() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        long n2 = -1L; // example value
        long expected = -1L; // example value
        assertEquals(expected, field.add(n, n2));
    }

    @Test
    public void testAddWrapField1() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        int n2 = 1; // example value
        long expected = 1; // example value
        assertEquals(expected, field.addWrapField(n, n2));
    }

    @Test
    public void testAddWrapField2() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        int n2 = -1; // example value
        long expected = -1; // example value
        assertEquals(expected, field.addWrapField(n, n2));
    }

    @Test
    public void testGetDifferenceAsLong1() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        long set = 1L; // example value
        long expected = -1L; // example value
        assertEquals(expected, field.getDifferenceAsLong(n, set));
    }

    @Test
    public void testGetDifferenceAsLong2() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        long set = -1L; // example value
        long expected = 1L; // example value
        assertEquals(expected, field.getDifferenceAsLong(n, set));
    }

    @Test
    public void testSet() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        int n2 = 1; // example value
        long expected = 1L; // example value
        assertEquals(expected, field.set(n, n2));
    }

    @Test
    public void testIsLeap() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        assertTrue(field.isLeap(n));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        int expected = 1; // example value
        assertEquals(expected, field.getLeapAmount(n));
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        int expected = 1; // example value
        assertEquals(expected, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        int expected = field.iMax;
        assertEquals(expected, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        long expected = 0L; // example value
        assertEquals(expected, field.roundFloor(n));
    }
  
  @Test
    public void testRemainder() {
        BasicChronology bc = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(bc, 1);
        long n = 0L; // example value
        long expected = 0L; // example value
        assertEquals(expected, field.remainder(n));
    }

}