import org.junit.*;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ReadableInstant_ValidInputs() {
        // Typical use case
        ReadableInstant instant1 = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2021, 1, 1, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();
        int expected = 366;
        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        assertEquals(expected, result);
        
        // Edge case - same instant
        instant1 = new DateTime(2022, 2, 2, 2, 2);
        instant2 = new DateTime(2022, 2, 2, 2, 2);
        fieldType = DurationFieldType.minutes();
        expected = 0;
        result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        assertEquals(expected, result);
        
        // Error scenario - null input
        instant1 = null;
        instant2 = new DateTime(2022, 2, 2, 2, 2);
        fieldType = DurationFieldType.minutes();
        try {
            BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }

    @Test
    public void testBetween_ReadablePartial_ValidInputs() {
        // Typical use case
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 1);
        ReadablePeriod readablePeriod = Period.years();
        int expected = 1;
        int result = BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
        assertEquals(expected, result);
        
        // Edge case - same partial
        partial1 = new LocalDate(2022, 2, 2);
        partial2 = new LocalDate(2022, 2, 2);
        readablePeriod = Period.years();
        expected = 0;
        result = BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
        assertEquals(expected, result);
        
        // Error scenario - null input
        partial1 = null;
        partial2 = new LocalDate(2022, 2, 2);
        readablePeriod = Period.years();
        try {
            BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }
        
        // Error scenario - partials with different size
        partial1 = new LocalDate(2022, 2, 2);
        partial2 = new LocalDateTime(2022, 2, 2, 2, 2);
        readablePeriod = Period.years();
        try {
            BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
        
        // Error scenario - non-contiguous partial
        partial1 = new LocalDate(2022, 2, 2);
        partial2 = new LocalDate(2022, 2, 3);
        readablePeriod = Period.years();
        try {
            BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
        }
    }

    @Test
    public void testStandardPeriodIn_ValidInputs() {
        // Typical use case
        ReadablePeriod readablePeriod = Period.months().withDays(15);
        long n = 30;
        int expected = 0;
        int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
        assertEquals(expected, result);

        // Edge case - null period
        readablePeriod = null;
        n = 1;
        expected = 0;
        result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
        assertEquals(expected, result);
        
        // Error scenario - non-precise field in period
        readablePeriod = Period.months().withWeeks(2);
        n = 7;
        try {
            BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert period to duration as weeks is not precise in the period P2M2W", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        int expected = 5;
        int result = period.getValue();
        assertEquals(expected, result);

        // Edge case - negative value
        period = new Years(-3);
        expected = -3;
        result = period.getValue();
        assertEquals(expected, result);
    }

    @Test
    public void testSetValue() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        int expected = 10;
        period.setValue(expected);
        int result = period.getValue();
        assertEquals(expected, result);

        // Edge case - negative value
        period.setValue(-7);
        expected = -7;
        result = period.getValue();
        assertEquals(expected, result);
    }

    @Test
    public void testGetFieldType() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        DurationFieldType expected = DurationFieldType.years();
        DurationFieldType result = period.getFieldType();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPeriodType() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        PeriodType expected = PeriodType.years();
        PeriodType result = period.getPeriodType();
        assertEquals(expected, result);
    }

    @Test
    public void testSize() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        int expected = 1;
        int result = period.size();
        assertEquals(expected, result);
    }

    @Test
    public void testGetFieldType_WithValidInput() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        int n = 0;
        DurationFieldType expected = DurationFieldType.years();
        DurationFieldType result = period.getFieldType(n);
        assertEquals(expected, result);
        
        // Error scenario - invalid index
        n = 1;
        try {
            period.getFieldType(n);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGetValue_WithValidInput() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        int n = 0;
        int expected = 5;
        int result = period.getValue(n);
        assertEquals(expected, result);
        
        // Error scenario - invalid index
        n = 1;
        try {
            period.getValue(n);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGet() {
        // Typical use case - field type matches
        BaseSingleFieldPeriod period = new Years(5);
        DurationFieldType fieldType = DurationFieldType.years();
        int expected = 5;
        int result = period.get(fieldType);
        assertEquals(expected, result);
        
        // Typical use case - field type does not match
        fieldType = DurationFieldType.months();
        expected = 0;
        result = period.get(fieldType);
        assertEquals(expected, result);
    }

    @Test
    public void testIsSupported() {
        // Typical use case - supported field type
        BaseSingleFieldPeriod period = new Years(5);
        DurationFieldType fieldType = DurationFieldType.years();
        boolean result = period.isSupported(fieldType);
        assertTrue(result);
        
        // Typical use case - unsupported field type
        fieldType = DurationFieldType.months();
        result = period.isSupported(fieldType);
        assertFalse(result);
    }

    @Test
    public void testToPeriod() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        Period expected = Period.years(5);
        Period result = period.toPeriod();
        assertEquals(expected, result);
    }

    @Test
    public void testToMutablePeriod() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        MutablePeriod expected = new MutablePeriod();
        expected.addPeriod(Period.years(5));
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        // Typical use case - same period
        BaseSingleFieldPeriod period1 = new Years(5);
        BaseSingleFieldPeriod period2 = new Years(5);
        boolean result = period1.equals(period2);
        assertTrue(result);
        
        // Typical use case - different period
        period2 = new Years(10);
        result = period1.equals(period2);
        assertFalse(result);
        
        // Typical use case - different type
        period2 = new Months(5);
        result = period1.equals(period2);
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Typical use case
        BaseSingleFieldPeriod period = new Years(5);
        int expected = 27 * (27 * 17 + 5) + DurationFieldType.years().hashCode();
        int result = period.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo() {
        // Typical use case - greater than
        BaseSingleFieldPeriod period1 = new Years(10);
        BaseSingleFieldPeriod period2 = new Years(5);
        int result = period1.compareTo(period2);
        assertEquals(1, result);
        
        // Typical use case - less than
        period1 = new Years(5);
        period2 = new Years(10);
        result = period1.compareTo(period2);
        assertEquals(-1, result);
        
        // Typical use case - equal
        period1 = new Years(5);
        period2 = new Years(5);
        result = period1.compareTo(period2);
        assertEquals(0, result);
        
        // Error scenario - different period type
        period1 = new Years(5);
        period2 = new Months(5);
        try {
            period1.compareTo(period2);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals("class Years cannot be compared to class Months", e.getMessage());
        }
    }
}