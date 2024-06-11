import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenWithInstantAndDurationFieldType() {
        // Test typical case
        int result = BaseSingleFieldPeriod.between(new Instant(0), new Instant(1000), DurationFieldType.seconds());
        assertEquals(1, result);

        // Test edge case - start and end instants are the same
        result = BaseSingleFieldPeriod.between(new Instant(0), new Instant(0), DurationFieldType.seconds());
        assertEquals(0, result);

        // Test error scenario - null start instant
        try {
            result = BaseSingleFieldPeriod.between(null, new Instant(1000), DurationFieldType.seconds());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBetweenWithPartialAndZeroInstance() {
        // Test typical case
        BaseSingleFieldPeriod zeroInstance = new BaseSingleFieldPeriod(0);
        int result = BaseSingleFieldPeriod.between(new LocalDate(), new LocalDate().plusDays(1), zeroInstance);
        assertEquals(1, result);

        // Test edge case - partials with different sizes
        try {
            result = BaseSingleFieldPeriod.between(new LocalDate(), new LocalDate().plusMonths(1), zeroInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test error scenario - null start partial
        try {
            result = BaseSingleFieldPeriod.between(null, new LocalDate().plusDays(1), zeroInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodIn() {
        // Test typical case
        int result = BaseSingleFieldPeriod.standardPeriodIn(new Period(1, 2, 3, 4, 5, 6, 7), 3600000L);
        assertEquals(248, result);

        // Test edge case - period is null
        result = BaseSingleFieldPeriod.standardPeriodIn(null, 3600000L);
        assertEquals(0, result);

        // Test error scenario - period contains imprecise duration values
        try {
            result = BaseSingleFieldPeriod.standardPeriodIn(new Period(1, 2, 3, 4, 5, 6, 7), 60000L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}