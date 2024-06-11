import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        // Typical use case
        assertEquals(5, BaseSingleFieldPeriod.between(new Instant(0), new Instant(5000), DurationFieldType.seconds()));

        // Edge case - negative duration
        assertEquals(-5, BaseSingleFieldPeriod.between(new Instant(5000), new Instant(0), DurationFieldType.seconds()));

        // Error scenario - null start instant
        assertThrows(IllegalArgumentException.class, () -> {
            BaseSingleFieldPeriod.between(null, new Instant(5000), DurationFieldType.seconds());
        });

        // Error scenario - null end instant
        assertThrows(IllegalArgumentException.class, () -> {
            BaseSingleFieldPeriod.between(new Instant(0), null, DurationFieldType.seconds());
        });
    }

    @Test
    public void testBetweenPartial() {
        // Typical use case
        assertEquals(2, BaseSingleFieldPeriod.between(new LocalDate(2000, 1, 1), new LocalDate(2000, 1, 3), new Period(1, 0, 0, 0)));

        // Edge case - zero instance constant
        assertThrows(IllegalArgumentException.class, () -> {
            BaseSingleFieldPeriod.between(new LocalDate(2000, 1, 1), new LocalDate(2000, 1, 3), null);
        });

        // Error scenario - partials with different field sets
        assertThrows(IllegalArgumentException.class, () -> {
            BaseSingleFieldPeriod.between(new LocalDate(2000, 1, 1), new LocalDate(2000, 1, 3), new Period(1, 1, 0, 0));
        });

        // Error scenario - not contiguous partials
        assertThrows(IllegalArgumentException.class, () -> {
            BaseSingleFieldPeriod.between(new LocalDate(2000, 1, 1).withFieldAdded(DurationFieldType.days(), 2), new LocalDate(2000, 1, 3), new Period(1, 0, 0, 0));
        });
    }

    @Test
    public void testStandardPeriodIn() {
        // Typical use case
        assertEquals(48, BaseSingleFieldPeriod.standardPeriodIn(new Period(2, 0, 0, 0), DurationFieldType.hours().getUnitMillis()));

        // Edge case - empty period
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, DurationFieldType.hours().getUnitMillis()));

        // Error scenario - imprecise duration values
        assertThrows(IllegalArgumentException.class, () -> {
            BaseSingleFieldPeriod.standardPeriodIn(new Period(0, 1, 0, 0), DurationFieldType.hours().getUnitMillis());
        });
    }

}