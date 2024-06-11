package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.LocatedInstant;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.TestDurationField.TestDateTimeField;
import org.joda.time.field.TestDurationField.TestDurationType;
import org.joda.time.field.TestDurationField.TestMillisDurationField;
import org.joda.time.field.TestDurationField.TestWrappedDurationField;
import org.junit.Test;

public class LenientDateTimeFieldTest {
	private static final DateTimeZone DEFAULT_ZONE = DateTimeZone.getDefault();
    private static final DateTimeFieldType DUMMY_TYPE = new DateTimeFieldType("dummy");
    private static final Chronology LENIENT_CHRONOLOGY = ISOChronology.getInstance(DateTimeZone.getDefault()).withUTC().withAllFields().withLenient();
    private static final TestDateTimeField TEST_FIELD = new TestDateTimeField();

    static {
        DateTimeUtils.setCurrentMillisFixed(0L);
    }

    protected void setUp() throws Exception {
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(DEFAULT_ZONE);
    }

    // -----------------------------------------------------------------------
    public void testIsLenient() {
        assertSame(TEST_FIELD, LENIENT_CHRONOLOGY.dayOfWeek().getField().isLenient(true));
    }

    public void testSet_long_int() {
        DateTimeField fld = LENIENT_CHRONOLOGY.dayOfWeek().getField().withMinimumValue();
        long instant = fld.set(0L, 3);
        assertEquals(3, fld.get(instant));

        instant = fld.set(0L, 2);
        assertEquals(2, fld.get(instant));

        instant = fld.set(0L, 8);
        assertEquals(8, fld.get(instant));

        fld = LENIENT_CHRONOLOGY.dayOfWeek().getField();
        instant = fld.set(0L, 3);
        assertEquals(3, fld.get(instant));

        instant = fld.set(0L, 2);
        assertEquals(2, fld.get(instant));

        instant = fld.set(0L, 8);  // lenient
        assertEquals(1, fld.get(instant));
        instant = LENIENT_CHRONOLOGY.dayOfWeek().set(instant, 7);  // test cross years
        assertEquals(7, fld.get(instant));
    }


}