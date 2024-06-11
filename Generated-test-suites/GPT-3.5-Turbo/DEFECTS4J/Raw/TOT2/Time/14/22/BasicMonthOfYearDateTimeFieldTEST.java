import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0); // Leap month is January
    }

    @Test
    public void testGet() {
        assertEquals(1, field.get(0)); // January
        assertEquals(12, field.get(-1000000000)); // December
    }

    @Test
    public void testAdd() {
        assertEquals(31449600000L, field.add(0, 1)); // Adding 1 month from January 1, 1970 gives February 1, 1970
        assertEquals(-2678400000L, field.add(0, -1)); // Subtracting 1 month from January 1, 1970 gives December 1, 1969
    }

    @Test
    public void testAddWrapField() {
        assertEquals(2678400000L, field.addWrapField(0, 12)); // Adding 12 months wraps around January to January of next year
        assertEquals(-2678400000L, field.addWrapField(0, -1)); // Subtracting 1 month wraps around January to December of previous year
    }

    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(12, field.getDifferenceAsLong(0, -2678400000L)); // Difference between January 1, 1970 and December 1, 1969 is 12 months
    }

    @Test
    public void testSet() {
        assertEquals(2678400000L, field.set(0, 12)); // Setting month to 12 (December) from January 1, 1970 gives December 1, 1970
    }

    @Test
    public void testIsLeap() {
        assertTrue(field.isLeap(0)); // January is the leap month
        assertFalse(field.isLeap(-2678400000L)); // December is not the leap month
    }

    @Test
    public void testGetLeapAmount() {
        assertEquals(1, field.getLeapAmount(0)); // January is the leap month
        assertEquals(0, field.getLeapAmount(-2678400000L)); // December is not the leap month
    }
}