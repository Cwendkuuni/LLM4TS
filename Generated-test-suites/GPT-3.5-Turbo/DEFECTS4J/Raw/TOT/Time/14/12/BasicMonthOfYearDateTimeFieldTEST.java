import org.joda.time.DateTimeFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.BasicChronology;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField;

    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(chronology, 0);
    }

    @Test
    public void testGet() {
        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setMonthOfYear(5);

        int result = basicMonthOfYearDateTimeField.get(dateTime.getMillis());

        assertEquals(5, result);
    }

    @Test
    public void testAdd_PositiveMonths() {
        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setMonthOfYear(5);

        long result = basicMonthOfYearDateTimeField.add(dateTime.getMillis(), 2);

        MutableDateTime expectedDateTime = new MutableDateTime();
        expectedDateTime.setMonthOfYear(7);

        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testAdd_NegativeMonths() {
        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setMonthOfYear(5);

        long result = basicMonthOfYearDateTimeField.add(dateTime.getMillis(), -2);

        MutableDateTime expectedDateTime = new MutableDateTime();
        expectedDateTime.setMonthOfYear(3);

        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testAdd_ZeroMonths() {
        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setMonthOfYear(5);

        long result = basicMonthOfYearDateTimeField.add(dateTime.getMillis(), 0);

        MutableDateTime expectedDateTime = new MutableDateTime();
        expectedDateTime.setMonthOfYear(5);

        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testAddWrapField_PositiveMonths() {
        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setMonthOfYear(5);

        long result = basicMonthOfYearDateTimeField.addWrapField(dateTime.getMillis(), 2);

        MutableDateTime expectedDateTime = new MutableDateTime();
        expectedDateTime.setMonthOfYear(7);

        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testAddWrapField_NegativeMonths() {
        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setMonthOfYear(5);

        long result = basicMonthOfYearDateTimeField.addWrapField(dateTime.getMillis(), -2);

        MutableDateTime expectedDateTime = new MutableDateTime();
        expectedDateTime.setMonthOfYear(3);

        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testSet() {
        MutableDateTime dateTime = new MutableDateTime();

        long result = basicMonthOfYearDateTimeField.set(dateTime.getMillis(), 5);

        MutableDateTime expectedDateTime = new MutableDateTime();
        expectedDateTime.setMonthOfYear(5);

        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_InvalidMonth() {
        MutableDateTime dateTime = new MutableDateTime();

        long result = basicMonthOfYearDateTimeField.set(dateTime.getMillis(), 13);
    }

    @Test
    public void testGetLeapAmount() {
        MutableDateTime dateTime = new MutableDateTime();

        int result = basicMonthOfYearDateTimeField.getLeapAmount(dateTime.getMillis());

        assertEquals(0, result);
    }

    @Test
    public void testGetMinimumValue() {
        int result = basicMonthOfYearDateTimeField.getMinimumValue();

        assertEquals(1, result);
    }

    @Test
    public void testGetMaximumValue() {
        int result = basicMonthOfYearDateTimeField.getMaximumValue();

        assertEquals(12, result);
    }
}