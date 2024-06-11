package org.joda.time.base;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    private static final long SERIALIZATION_UID = 9386874258972L;
    private BaseSingleFieldPeriod instance;

    @Before
    public void setUp() {
        instance = new TestBaseSingleFieldPeriod(123);
    }

    // Test for the constructor
    @Test
    public void testConstructor() {
        assertNotNull(instance);
        assertEquals(123, instance.getValue());
    }

    // Test for the getFieldType method
    @Test
    public void testGetFieldType() {
        DurationFieldType fieldType = instance.getFieldType();
        assertNotNull(fieldType);
        assertEquals(DurationFieldType.days(), fieldType);
    }

    // Test for the getPeriodType method
    @Test
    public void testGetPeriodType() {
        PeriodType periodType = instance.getPeriodType();
        assertNotNull(periodType);
        assertEquals(PeriodType.standard(), periodType);
    }

    // Test for the size method
    @Test
    public void testSize() {
        assertEquals(1, instance.size());
    }

    // Test for the getValue method
    @Test
    public void testGetValue() {
        assertEquals(123, instance.getValue());
    }

    // Test for the getValue method with different index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        instance.getValue(2);
    }

    // Test for the getFieldType method with different index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        instance.getFieldType(2);
    }

    // Test for the equals method
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod other = new TestBaseSingleFieldPeriod(123);
        assertTrue(instance.equals(other));
        assertEquals(instance.hashCode(), other.hashCode());

        ReadablePeriod otherPeriod = new Period(1, 2, 3, 4, 5, 6);
        assertFalse(instance.equals(otherPeriod));

        BaseSingleFieldPeriod otherNull = null;
        assertFalse(instance.equals(otherNull));
    }

    // Test for the compareTo method
    @Test
    public void testCompareTo() {
        TestBaseSingleFieldPeriod other = new TestBaseSingleFieldPeriod(456);
        int comparisonResult = instance.compareTo(other);
        assertTrue(comparisonResult < 0);

        TestBaseSingleFieldPeriod otherEqual = new TestBaseSingleFieldPeriod(123);
        comparisonResult = instance.compareTo(otherEqual);
        assertEquals(0, comparisonResult);

        TestBaseSingleFieldPeriod otherLess = new TestBaseSingleFieldPeriod(122);
        comparisonResult = instance.compareTo(otherLess);
        assertTrue(comparisonResult > 0);

        TestBaseSingleFieldPeriod otherGreater = new TestBaseSingleFieldPeriod(124);
        comparisonResult = instance.compareTo(otherGreater);
        assertTrue(comparisonResult < 0);

        TestBaseSingleFieldPeriod otherNegative = new TestBaseSingleFieldPeriod(-123);
        comparisonResult = instance.compareTo(otherNegative);
        assertTrue(comparisonResult > 0);
    }

    // Test for the between method with valid arguments
    @Test
    public void testBetweenValidArguments() {
        ReadableInstant start = Chronology.getInstanceUTC().instant();
        ReadableInstant end = start.plus(Duration.standardDays(1));

        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, result);
    }

    // Test for the between method with null arguments
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullArguments() {
        BaseSingleFieldPeriod.between(null, null, DurationFieldType.days());
    }

    // Test for the standardPeriodIn method with a valid period
    @Test
    public void testStandardPeriodInValidPeriod() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6);
        try {
            BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for the standardPeriodIn method with an invalid period
    @Test
    public void testStandardPeriodInInvalidPeriod() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6).withMonths(1);
        try {
            BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for the standardPeriodIn method with a valid period
    @Test
    public void testStandardPeriodInValidPeriodValidMillisPerUnit() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 86400000);
        assertEquals(1, result);
    }

    // Test for the standardPeriodIn method with a valid period and invalid millisPerUnit
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInValidPeriodInvalidMillisPerUnit() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6);
        BaseSingleFieldPeriod.standardPeriodIn(period, 900000);
    }

    // Test for the toPeriod method
    @Test
    public void testToPeriod() {
        Period expectedPeriod = new Period(1, 0, 0, 0, 0, 0, 123);
        Period resultPeriod = instance.toPeriod();
        assertEquals(expectedPeriod, resultPeriod);
    }

    // Test for the toMutablePeriod method
    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        instance.toMutablePeriod(mutablePeriod);
        assertEquals(instance, mutablePeriod);
    }

    // Test for the toMutablePeriod method with a null MutablePeriod
    @Test(expected = NullPointerException.class)
    public void testToMutablePeriodNull() {
        instance.toMutablePeriod(null);
    }

    private static class TestBaseSingleFieldPeriod extends BaseSingleFieldPeriod {

        public TestBaseSingleFieldPeriod(int period) {
            super(period);
        }

    }

}