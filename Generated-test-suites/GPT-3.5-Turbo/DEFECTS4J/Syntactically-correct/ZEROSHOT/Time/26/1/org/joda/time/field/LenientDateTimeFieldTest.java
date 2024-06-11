
package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    private DateTimeField field;
    private Chronology base;

    @Before
    public void setup() {
        field = new LenientDateTimeField(new BaseDateTimeField() {
            @Override
            public int get(long instant) {
                return 0;
            }

            @Override
            public Chronology getChronology() {
                return null;
            }

            @Override
            public String getAsText(long instant, Locale locale) {
                return null;
            }

            @Override
            public String getAsShortText(long instant, Locale locale) {
                return null;
            }

            @Override
            public long add(long instant, int value) {
                return 0;
            }

            @Override
            public long add(long instant, long value) {
                return 0;
            }

            @Override
            public long addWrapField(long instant, int value) {
                return 0;
            }

            @Override
            public long set(long instant, int value) {
                return 0;
            }

            @Override
            public long set(long instant, String text, Locale locale) {
                return 0;
            }

            @Override
            public long roundFloor(long instant) {
                return 0;
            }

            @Override
            public long roundCeiling(long instant) {
                return 0;
            }

            @Override
            public long roundHalfFloor(long instant) {
                return 0;
            }

            @Override
            public long roundHalfCeiling(long instant) {
                return 0;
            }

            @Override
            public long roundHalfEven(long instant) {
                return 0;
            }

            @Override
            public long remainder(long instant) {
                return 0;
            }

            @Override
            public int getMinimumValue() {
                return 0;
            }

            @Override
            public int getMaximumValue() {
                return 0;
            }

            @Override
            public int getMaximumTextLength(Locale locale) {
                return 0;
            }

            @Override
            public int getMaximumShortTextLength(Locale locale) {
                return 0;
            }
        }, base);
    }

    @Test
    public void testIsLenient() {
        assertEquals(true, field.isLenient());
    }

    @Test
    public void testSet() {
        long instant = DateTimeUtils.currentTimeMillis();
        int value = 1;
        long result = field.set(instant, value);
        assertEquals(instant, result);
    }

}
