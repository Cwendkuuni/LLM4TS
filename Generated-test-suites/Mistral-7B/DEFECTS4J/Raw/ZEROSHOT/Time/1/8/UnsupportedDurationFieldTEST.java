package org.joda.time.field.test;

import org.joda.time.BaseTest;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UnsupportedDurationFieldTest extends BaseTest {

    @Test
    public void testUnsupportedDurationField() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);

        // test name
        assertEquals("seconds", unsupported.getName());

        // test isSupported
        assertFalse(unsupported.isSupported());

        // test isPrecise
        assertTrue(unsupported.isPrecise());

        // test getValue
        try {
            unsupported.getValue(1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test getValueAsLong
        try {
            unsupported.getValueAsLong(1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test getValue and getValueAsLong with instant
        try {
            unsupported.getValue(1, 1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            unsupported.getValueAsLong(1, 1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test getMillis
        try {
            unsupported.getMillis(1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            unsupported.getMillis(1L);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test getMillis with instant
        try {
            unsupported.getMillis(1, 1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            unsupported.getMillis(1L, 1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test add
        try {
            unsupported.add(1, 1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            unsupported.add(1L, 1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test getDifference
        try {
            unsupported.getDifference(1, 1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            unsupported.getDifferenceAsLong(1, 1);
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test getUnitMillis
        assertEquals(0, unsupported.getUnitMillis());

        // test compareTo
        DurationField other = new UnsupportedDurationField(type);
        assertEquals(1, unsupported.compareTo(other));

        // test equals
        UnsupportedDurationField equal = UnsupportedDurationField.getInstance(type);
        assertEquals(equal.hashCode(), unsupported.hashCode());
        assertTrue(unsupported.equals(equal));

        // test null
        assertNull(unsupported.equals(null));

        // test toString
        assertEquals("UnsupportedDurationField[seconds]", unsupported.toString());

        // test readResolve
        UnsupportedDurationField clone = (UnsupportedDurationField) unsupported.clone();
        assertEquals(unsupported.hashCode(), clone.hashCode());
        assertTrue(unsupported.equals(clone));
    }
}