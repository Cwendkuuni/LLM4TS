import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private DateTimeField field;
    private Chronology base;

    @Before
    public void setUp() {
        base = ISOChronology.getInstance(DateTimeZone.UTC);
        field = new LenientDateTimeField(base.year(), base);
    }

    @Test
    public void testGet() {
        assertEquals(2021, field.get(1610668800000L));
        assertEquals(2000, field.get(-631152000000L));
        assertEquals(3999, field.get(6406051199999L));
    }

    @Test
    public void testSet() {
        assertEquals(1610668800000L, field.set(1610668800000L, 2021));
        assertEquals(-631152000000L, field.set(-631152000000L, 2000));
        assertEquals(6406051199999L, field.set(6406051199999L, 3999));
    }

    @Test
    public void testSet_OutOfBounds() {
        assertEquals(1610668799999L, field.set(1610668800000L, 10000)); // Upper bound
        assertEquals(-631152000001L, field.set(-631152000000L, -10000)); // Lower bound
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_InvalidValue() {
        field.set(1610668800000L, -1);
    }

    @Test
    public void testRoundFloor() {
        assertEquals(1610668800000L, field.roundFloor(1610668805000L));
    }

    @Test
    public void testRoundCeiling() {
        assertEquals(1610668805000L, field.roundCeiling(1610668800000L));
    }

    @Test
    public void testRoundHalfFloor() {
        assertEquals(1610668800000L, field.roundHalfFloor(1610668802500L));
    }

    @Test
    public void testRoundHalfCeiling() {
        assertEquals(1610668805000L, field.roundHalfCeiling(1610668802500L));
    }

    @Test
    public void testRoundHalfEven() {
        assertEquals(1610668805000L, field.roundHalfEven(1610668802500L));
    }

    @Test
    public void testGetLeapAmount() {
        assertEquals(86400000, field.getLeapAmount(1610668800000L, 1619683200000L));
    }

    @Test
    public void testGetDifference() {
        assertEquals(86400000, field.getDifference(1619683200000L, 1610668800000L));
    }

    @Test
    public void testGetDurationField() {
        assertEquals(base.days(), field.getDurationField());
    }

    @Test
    public void testGetRangeDurationField() {
        assertEquals(base.years(), field.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        assertFalse(field.isLeap(1610668800000L));
    }

    @Test
    public void testGetLeapDurationField() {
        assertNull(field.getLeapDurationField());
    }
}