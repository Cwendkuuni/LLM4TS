import org.joda.time.LocalDate;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = new LocalDate(2022, 5, 15).toDate().getTime();
        assertEquals(5, field.get(instant));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = new LocalDate(2022, 5, 15).toDate().getTime();
        assertEquals(new LocalDate(2022, 6, 15).toDate().getTime(), field.add(instant, 1));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = new LocalDate(2022, 12, 15).toDate().getTime();
        assertEquals(new LocalDate(2023, 1, 15).toDate().getTime(), field.addWrapField(instant, 1));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant1 = new LocalDate(2022, 5, 15).toDate().getTime();
        long instant2 = new LocalDate(2023, 8, 20).toDate().getTime();
        assertEquals(15, field.getDifferenceAsLong(instant1, instant2));
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long instant = new LocalDate(2022, 5, 15).toDate().getTime();
        assertEquals(new LocalDate(2022, 11, 15).toDate().getTime(), field.set(instant, 11));
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 6);
        long instant1 = new LocalDate(2020, 6, 15).toDate().getTime();
        long instant2 = new LocalDate(2021, 7, 20).toDate().getTime();
        assertTrue(field.isLeap(instant1));
        assertFalse(field.isLeap(instant2));
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
}