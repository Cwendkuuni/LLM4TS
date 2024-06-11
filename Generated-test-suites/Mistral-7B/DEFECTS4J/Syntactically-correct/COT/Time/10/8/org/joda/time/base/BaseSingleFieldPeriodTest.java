package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public class BaseSingleFieldPeriodTest {

    // Test for between method with valid arguments
    @org.junit.Test
    public void testBetween_ValidArguments() {
        ReadableInstant start = DateTimeUtils.currentTime();
        ReadableInstant end = DateTimeUtils.currentTime().plus(Duration.standardHours(1));
        DurationFieldType field = DurationFieldType.standardHours();

        int result = BaseSingleFieldPeriod.between(start, end, field);
        assertEquals(1, result);
    }

    // Test for between method with null arguments
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetween_NullArguments() {
        BaseSingleFieldPeriod.between(null, null, null);
    }

    // Test for between method with incorrect field type
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetween_IncorrectFieldType() {
        ReadableInstant start = DateTimeUtils.currentTime();
        ReadableInstant end = DateTimeUtils.currentTime().plus(Duration.standardMinutes(1));
        DurationFieldType field = DurationFieldType.standardHours();

        BaseSingleFieldPeriod.between(start, end, field);
    }

    // Test for standardPeriodIn method with valid arguments
    @org.junit.Test
    public void testStandardPeriodIn_ValidArguments() {
        ReadablePeriod period = Period.parse("PT1H30M");
        long millisPerUnit = 1000;

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(2 * 60 + 30, result);
    }

    // Test for standardPeriodIn method with null period
    @org.junit.Test
    public void testStandardPeriodIn_NullPeriod() {
        long millisPerUnit = 1000;

        int result = BaseSingleFieldPeriod.standardPeriodIn(null, millisPerUnit);
        assertEquals(0, result);
    }

    // Test for BaseSingleFieldPeriod constructor with valid argument
    @org.junit.Test
    public void testConstructor_ValidArgument() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int expected = 123;
        int actual = period.getValue();

        assertEquals(expected, actual);
    }

    // Test for BaseSingleFieldPeriod constructor with negative argument
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeArgument() {
        new BaseSingleFieldPeriod(-123);
    }

    // Test for getFieldType method
    @org.junit.Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType expected = DurationFieldType.standardHours();

        DurationFieldType actual = period.getFieldType();

        assertEquals(expected, actual);
    }

    // Test for getPeriodType method
    @org.junit.Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        PeriodType expected = PeriodType.standard();

        PeriodType actual = period.getPeriodType();

        assertEquals(expected, actual);
    }

    // Test for size method
    @org.junit.Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int expected = 1;

        int actual = period.size();

        assertEquals(expected, actual);
    }

    // Test for getFieldType method with invalid index
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_InvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        period.getFieldType(2);
    }

    // Test for getValue method with valid index
    @org.junit.Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int expected = 123;

        int actual = period.getValue(0);

        assertEquals(expected, actual);
    }

    // Test for getValue method with invalid index
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_InvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        period.getValue(1);
    }

    // Test for get method with valid field type
    @org.junit.Test
    public void testGet_ValidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType fieldType = DurationFieldType.standardHours();

        int expected = 1;
        int actual = period.get(fieldType);

        assertEquals(expected, actual);
    }

    // Test for get method with invalid field type
    @org.junit.Test
    public void testGet_InvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType fieldType = DurationFieldType.standardMinutes();

        int expected = 0;
        int actual = period.get(fieldType);

        assertEquals(expected, actual);
    }

    // Test for isSupported method with valid field type
    @org.junit.Test
    public void testIsSupported_ValidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType fieldType = DurationFieldType.standardHours();

        boolean expected = true;
        boolean actual = period.isSupported(fieldType);

        assertEquals(expected, actual);
    }

    // Test for isSupported method with invalid field type
    @org.junit.Test
    public void testIsSupported_InvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType fieldType = DurationFieldType.standardMinutes();

        boolean expected = false;
        boolean actual = period.isSupported(fieldType);

        assertEquals(expected, actual);
    }

    // Test for toPeriod method
    @org.junit.Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        Period expected = Period.parse("PT" + String.valueOf(123) + "S");

        Period actual = period.toPeriod();

        assertEquals(expected, actual);
    }

    // Test for toMutablePeriod method
    @org.junit.Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        MutablePeriod expected = new MutablePeriod();
        expected.add(period);

        MutablePeriod actual = period.toMutablePeriod();

        assertEquals(expected, actual);
    }

    // Test for equals method with null argument
    @org.junit.Test
    public void testEquals_NullArgument() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);

        boolean expected = false;
        boolean actual = period.equals(null);

        assertEquals(expected, actual);
    }

    // Test for equals method with same instance
    @org.junit.Test
    public void testEquals_SameInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);

        boolean expected = true;
        boolean actual = period.equals(period);

        assertEquals(expected, actual);
    }

    // Test for equals method with different instance but same value
    @org.junit.Test
    public void testEquals_DifferentInstanceSameValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);

        boolean expected = true;
        boolean actual = period1.equals(period2);

        assertEquals(expected, actual);
    }

    // Test for equals method with different instance and different value
    @org.junit.Test
    public void testEquals_DifferentInstancesDifferentValues() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(456);

        boolean expected = false;
        boolean actual = period1.equals(period2);

        assertEquals(expected, actual);
    }

    // Test for hashCode method
    @org.junit.Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int expected = 27 * 27 * 17 + 123 + getFieldType().hashCode();

        int actual = period.hashCode();

        assertEquals(expected, actual);
    }

    // Test for compareTo method with equal instances
    @org.junit.Test
    public void testCompareTo_EqualInstances() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);

        int expected = 0;

        int actual = period1.compareTo(period2);

        assertEquals(expected, actual);
    }

    // Test for compareTo method with greater instance
    @org.junit.Test
    public void testCompareTo_GreaterInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(111);

        int expected = 1;

        int actual = period1.compareTo(period2);

        assertEquals(expected, actual);
    }

    // Test for compareTo method with smaller instance
    @org.junit.Test
    public void testCompareTo_SmallerInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(111);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);

        int expected = -1;

        int actual = period1.compareTo(period2);

        assertEquals(expected, actual);
    }

    // Test for compareTo method with null argument
    @org.junit.Test(expected = NullPointerException.class)
    public void testCompareTo_NullArgument() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod nullPeriod = null;

        period.compareTo(nullPeriod);
    }

    // Test for compareTo method with different class argument
    @org.junit.Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentClassArgument() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        Object other = new Object();

        period.compareTo(other);
    }
}