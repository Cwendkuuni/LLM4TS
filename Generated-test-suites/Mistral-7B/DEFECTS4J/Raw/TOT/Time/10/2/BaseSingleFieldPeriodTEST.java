import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testSingleFieldPeriod() {
        // Test with valid input
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(5, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(Period.ZERO.withFieldAdded(fieldType, 5), period.toPeriod());

        // Test adding and getting the value of a field
        assertEquals(5, period.get(fieldType));
        period.setValue(10);
        assertEquals(10, period.get(fieldType));

        // Test equals method
        BaseSingleFieldPeriod samePeriod = new BaseSingleFieldPeriod(5);
        assertTrue(period.equals(samePeriod));
        BaseSingleFieldPeriod differentPeriod = new BaseSingleFieldPeriod(6);
        assertFalse(period.equals(differentPeriod));

        // Test hashcode method
        int periodHashCode = period.hashCode();
        int samePeriodHashCode = samePeriod.hashCode();
        assertEquals(periodHashCode, samePeriodHashCode);

        // Test comparison method
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriod(3);
        int comparisonResult = period.compareTo(smallerPeriod);
        assertTrue(comparisonResult > 0);

        BaseSingleFieldPeriod largerPeriod = new BaseSingleFieldPeriod(7);
        comparisonResult = period.compareTo(largerPeriod);
        assertTrue(comparisonResult < 0);

        // Test adding and subtracting periods
        BaseSingleFieldPeriod addedPeriod = period.toMutablePeriod().add(differentPeriod);
        assertEquals(10, addedPeriod.getValue());

        MutablePeriod subtractedPeriod = period.toMutablePeriod().subtract(BaseSingleFieldPeriod.of(2));
        assertEquals(3, subtractedPeriod.getValue());

        // Test with null input
        try {
            BaseSingleFieldPeriod nullPeriod = new BaseSingleFieldPeriod(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.between(null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.between(new DateTime(), null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.between(new DateTime(), new DateTime(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.between(new DateTime(), new DateTime(), new PeriodType[] {});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.between(new DateTime(), new DateTime(), new PeriodType[] {null});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}