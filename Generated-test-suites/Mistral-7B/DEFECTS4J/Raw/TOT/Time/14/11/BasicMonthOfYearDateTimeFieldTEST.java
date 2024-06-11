package org.joda.time.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.chrono.BasicGregorianChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    /** The chronology to use. */
    private static final BasicGregorianChronology CHRONOLOGY = new BasicGregorianChronology();

    /**
     * Tests basic functionality.
     */
    @Test
    public void testBasic() {
        BasicMonthOfYearDateTimeField field = CHRONOLOGY.monthOfYear();

        // Basic get and set.
        long instant = BaseDateTime.UTC.getMillis();
        int month = field.get(instant);
        assertEquals(12, month);
        long newInstant = field.set(instant, 2).getMillis();
        month = field.get(newInstant);
        assertEquals(2, month);

        // Get and set month out of range.
        long minInstant = CHRONOLOGY.getMinimumValue().getMillis();
        long maxInstant = CHRONOLOGY.getMaximumValue().getMillis();
        int minMonth = field.get(minInstant);
        int maxMonth = field.get(maxInstant);
        assertEquals(1, minMonth);
        assertEquals(12, maxMonth);
        long newMinInstant = field.set(minInstant, 0).getMillis();
        newMinInstant = field.set(newMinInstant, 13).getMillis();
        newMinInstant = field.set(newMinInstant, -2).getMillis();
        long newMaxInstant = field.set(maxInstant, 13).getMillis();
        newMaxInstant = field.set(newMaxInstant, 0).getMillis();
        newMaxInstant = field.set(newMaxInstant, -1).getMillis();
        assertEquals(1, field.get(newMinInstant));
        assertEquals(12, field.get(newMaxInstant));

        // Add and wrap.
        assertEquals(1, field.addWrapField(instant, -11).getMonthOfYear());
        assertEquals(2, field.addWrapField(instant, 13).getMonthOfYear());
        assertEquals(2, field.addWrapField(instant, -13).getMonthOfYear());
        assertEquals(12, field.addWrapField(instant, 13).getMonthOfYear());

        // Add without wrapping.
        long newInstant2 = field.add(instant, -11);
        assertEquals(11, field.get(newInstant2).getMonthOfYear());
        newInstant2 = field.add(instant, 13);
        assertEquals(24, field.get(newInstant2).getMonthOfYear());

        // Add with invalid input.
        try {
            field.add(instant, Long.MAX_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Add with invalid input, negative.
        try {
            field.add(instant, Long.MIN_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Add with invalid input, non-long.
        try {
            field.add(instant, new Object());
            fail();
        } catch (ClassCastException e) {
            // expected
        }

        // Add with invalid input, null.
        try {
            field.add(instant, null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        // Get minimum and maximum values.
        assertEquals(1, field.getMinimumValue().getMonthOfYear());
        assertEquals(12, field.getMaximumValue().getMonthOfYear());

        // Tests for add with long input.
        long newInstant3 = field.add(instant, 12345);
        assertEquals(12345, field.get(newInstant3).getMonthOfYear());

        // Tests for add with int input.
        long newInstant4 = field.add(instant, 12345);
        assertEquals(12345, field.get(newInstant4).getMonthOfYear());
        newInstant4 = field.add(instant, 12345);
        assertEquals(24789, field.get(newInstant4).getMonthOfYear());

        // Tests for add with negative input.
        long newInstant5 = field.add(instant, -12345);
        assertEquals(-12345, field.get(newInstant5).getMonthOfYear());

        // Tests for add with negative input, negative.
        long newInstant6 = field.add(instant, -12345);
        assertEquals(-24789, field.get(newInstant6).getMonthOfYear());

        // Tests for add with null input.
        try {
            field.add(instant, null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        // Tests for add with invalid input, negative.
        try {
            field.add(instant, Long.MIN_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Tests for add with invalid input, non-long.
        try {
            field.add(instant, new Object());
            fail();
        } catch (ClassCastException e) {
            // expected
        }

        // Tests for add with invalid input, array.
        try {
            field.add(instant, new int[] {1, 2, 3});
            fail();
        } catch (ClassCastException e) {
            // expected
        }

        // Tests for add with invalid input, long array.
        long[] longArray = new long[] {1, 2, 3};
        try {
            field.add(instant, longArray);
            fail();
        } catch (ClassCastException e) {
            // expected
        }

        // Tests for add with invalid input, long[] with invalid value.
        long[] longArray2 = new long[] {1, 2, Long.MAX_VALUE};
        try {
            field.add(instant, longArray2);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Tests for add with invalid input, long[] with negative value.
        long[] longArray3 = new long[] {1, 2, Long.MIN_VALUE};
        try {
            field.add(instant, longArray3);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}