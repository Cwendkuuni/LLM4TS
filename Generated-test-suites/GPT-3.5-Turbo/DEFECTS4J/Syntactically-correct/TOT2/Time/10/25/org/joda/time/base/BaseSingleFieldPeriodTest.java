import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenWithReadableInstant() {
        // Typical case
        assertEquals(10, BaseSingleFieldPeriod.between(new Instant(0), new Instant(10000), DurationFieldType.millis()));

        // Edge case
        assertEquals(0, BaseSingleFieldPeriod.between(new Instant(0), new Instant(0), DurationFieldType.millis()));

        // Error scenario
        try {
            BaseSingleFieldPeriod.between(null, new Instant(10000), DurationFieldType.seconds());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBetweenWithReadablePartial() {
        // Typical case
        assertEquals(24, BaseSingleFieldPeriod.between(new LocalDate(2022, 1, 1), new LocalDate(2022, 1, 2), new Period(0, 1, 0, 0)));

        // Edge case
        assertEquals(0, BaseSingleFieldPeriod.between(new LocalDate(2022, 1, 1), new LocalDate(2022, 1, 1), new Period(0, 1, 0, 0)));

        // Error scenario
        try {
            BaseSingleFieldPeriod.between(null, new LocalDate(2022, 1, 2), new Period(0, 1, 0, 0));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodIn() {
        // Typical case
        assertEquals(25, BaseSingleFieldPeriod.standardPeriodIn(new Period(1, 0, 0, 0), 3600000));

        // Edge case
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 3600000));

        // Error scenario
        try {
            BaseSingleFieldPeriod.standardPeriodIn(new Period(0, 1, 0, 0), 3600000);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);

        // Typical case
        assertTrue(period1.equals(period2));

        // Edge case
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);

        // Typical case
        assertEquals(1017, period.hashCode());

        // Edge case
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(0);
        assertEquals(910, period2.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);

        // Typical case
        assertEquals(1, period1.compareTo(period2));

        // Edge case
        assertEquals(0, period1.compareTo(new BaseSingleFieldPeriod(10)));

        // Error scenario
        try {
            period1.compareTo(new BaseSingleFieldPeriod(0, 1));
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

}