package org.joda.time.base;

import org.joda.time.DateTimeUtils;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ReadableInstant() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = null;
        try {
            BaseSingleFieldPeriod.between(instant1, instant2, null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }

    @Test
    public void testBetween_ReadablePartial_ReadablePeriod() {
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = null;
        ReadablePeriod period = null;
        try {
            BaseSingleFieldPeriod.between(partial1, partial2, period);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }
    }

    @Test
    public void testBetween_ReadablePartial_ReadablePeriod_MismatchingFields() {
        ReadablePartial partial1 = new MyReadablePartial();
        ReadablePartial partial2 = new MyReadablePartial();
        ReadablePeriod period = null;
        try {
            BaseSingleFieldPeriod.between(partial1, partial2, period);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }

    @Test
    public void testBetween_ReadablePartial_ReadablePeriod_Contiguous() {
        ReadablePartial partial1 = new MyNonContiguousReadablePartial();
        ReadablePartial partial2 = new MyNonContiguousReadablePartial();
        ReadablePeriod period = null;
        try {
            BaseSingleFieldPeriod.between(partial1, partial2, period);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
        }
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = null;
        long n = 0;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testStandardPeriodIn_NonPreciseField() {
        ReadablePeriod period = new MyNonPrecisePeriod();
        long n = 1;
        try {
            BaseSingleFieldPeriod.standardPeriodIn(period, n);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Cannot convert period to duration as field is not precise in the period", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new MyBaseSingleFieldPeriod(0);
        int result = period.getValue();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new MyBaseSingleFieldPeriod(0);
        period.setValue(1);
        int result = period.getValue();
        Assert.assertEquals(1, result);
    }

    public class MyReadablePartial implements ReadablePartial {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public DateTimeFieldType getFieldType(int i) {
            return null;
        }

        @Override
        public int getValue(int i) {
            return 0;
        }

        @Override
        public int get(DateTimeFieldType dateTimeFieldType) {
            return 0;
        }

        @Override
        public boolean isSupported(DateTimeFieldType dateTimeFieldType) {
            return false;
        }
    }

    public class MyNonContiguousReadablePartial implements ReadablePartial {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public DateTimeFieldType getFieldType(int i) {
            return null;
        }

        @Override
        public int getValue(int i) {
            return 0;
        }

        @Override
        public int get(DateTimeFieldType dateTimeFieldType) {
            return 0;
        }

        @Override
        public boolean isSupported(DateTimeFieldType dateTimeFieldType) {
            return false;
        }
    }

    public class MyNonPrecisePeriod implements ReadablePeriod {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public DateTimeFieldType getFieldType(int i) {
            return null;
        }

        @Override
        public int getValue(int i) {
            return 0;
        }
    }

    public class MyBaseSingleFieldPeriod extends BaseSingleFieldPeriod {

        public MyBaseSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return null;
        }

        @Override
        public PeriodType getPeriodType() {
            return null;
        }

        @Override
        public Object clone() {
            return null;
        }
    }
}