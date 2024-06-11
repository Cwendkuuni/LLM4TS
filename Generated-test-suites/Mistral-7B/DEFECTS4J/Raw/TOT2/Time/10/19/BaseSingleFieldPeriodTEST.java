import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructors() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(5, p.getValue());
        assertEquals(DurationFieldType.DAYS, p.getFieldType());

        try {
            new BaseSingleFieldPeriod(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new BaseSingleFieldPeriod("foo");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, p.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(PeriodType.standard(), p.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(1, p.size());
    }

    @Test
    public void testGetFieldTypeAndValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, p.getFieldType());
        assertEquals(5, p.getValue());

        try {
            p.setValue(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            p.setValue("foo");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        assertEquals(5, p.getValue());

        p.setValue(3);
        assertEquals(3, p.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        p.setValue(3);
        assertEquals(3, p.getValue());

        try {
            p.setValue(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            p.setValue("foo");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        Period period = p.toPeriod();
        assertEquals(5, period.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(5);
        MutablePeriod mutablePeriod = p.toMutablePeriod();
        mutablePeriod.add(new Period(3, 0, 0, 0, 0, 0, 0));
        assertEquals(8, mutablePeriod.getDays());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod p4 = new BaseSingleFieldPeriod("foo");

        assertEquals(p1, p1);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);

        p1.setValue(3);
        assertNotEquals(p1, p2);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod p4 = new BaseSingleFieldPeriod("foo");

        int hashCode1 = p1.hashCode();
        assertEquals(hashCode1, p1.hashCode());
        assertEquals(hashCode1, p2.hashCode());
        assertNotEquals(hashCode1, p3.hashCode());
        assertNotEquals(hashCode1, p4.hashCode());

        p1.setValue(3);
        assertNotEquals(hashCode1, p1.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(5);

        assertEquals(0, p1.compareTo(p1));
        assertEquals(1, p1.compareTo(p2));
        assertEquals(-1, p2.compareTo(p1));
        assertEquals(0, p1.compareTo(p3));
    }

    @Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.currentTime();
        ReadableInstant end = start.plus(new Period(5, 0, 0, 0, 0, 0, 0));

        int value = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(5, value);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(5, 0, 0, 0, 0, 0, 0);
        int value = BaseSingleFieldPeriod.standardPeriodIn(period, TimeUnit.MILLISECONDS.toMillis(24 * 60 * 60 * 1000));
        assertEquals(5, value);
    }
}