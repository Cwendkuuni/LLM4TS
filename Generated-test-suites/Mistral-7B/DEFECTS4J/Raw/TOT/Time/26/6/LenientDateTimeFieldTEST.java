package org.joda.time.field.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientField;
    private DateTimeFieldType type;
    private Chronology chronology;

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{DateTimeFieldType.instance(DateTimeFieldType.YEAR), Chronology.UTC});
        data.add(new Object[]{DateTimeFieldType.instance(DateTimeFieldType.MONTH_OF_YEAR), Chronology.JULIAN_CALENDAR});
        data.add(new Object[]{DateTimeFieldType.instance(DateTimeFieldType.DAY_OF_MONTH), Chronology.PROLEPTIC_GREGORIAN});
        data.add(new Object[]{DateTimeFieldType.instance(DateTimeFieldType.HOUR_OF_DAY), Chronology.ISO_DATE_TIME});
        data.add(new Object[]{DateTimeFieldType.instance(DateTimeFieldType.MINUTE_OF_HOUR), Chronology.ISO_DATE_TIME});
        data.add(new Object[]{DateTimeFieldType.instance(DateTimeFieldType.SECOND_OF_MINUTE), Chronology.ISO_DATE_TIME});
        data.add(new Object[]{DateTimeFieldType.instance(DateTimeFieldType.MILLISECOND_OF_SECOND), Chronology.ISO_DATE_TIME});
        return data;
    }

    @Before
    public void setUp(DateTimeFieldType type, Chronology chronology) {
        this.type = type;
        this.chronology = chronology;
        lenientField = LenientDateTimeField.getInstance(type, chronology);
    }

    @Test
    public void testIsLenient() {
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSet_ValidValue() {
        BaseDateTime baseDateTime = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime = new MutableDateTime(baseDateTime);

        int value = get(mutableDateTime.getLong());
        long newValue = value + 1;
        long result = lenientField.set(mutableDateTime.getLong(), newValue);

        assertEquals(result, mutableDateTime.getLong());
        assertEquals(newValue, get(result));
    }

    @Test
    public void testSet_OutOfBounds_Positive() {
        BaseDateTime baseDateTime = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime = new MutableDateTime(baseDateTime);

        int outOfBoundsValue = get(mutableDateTime.getLong()) + get(type.getRange());
        long result = lenientField.set(mutableDateTime.getLong(), outOfBoundsValue);

        long expectedValue = mutableDateTime.getLong() + getType().getRange();
        assertEquals(expectedValue, result);
        assertEquals(expectedValue, get(result));
    }

    @Test
    public void testSet_OutOfBounds_Negative() {
        BaseDateTime baseDateTime = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime = new MutableDateTime(baseDateTime);

        int outOfBoundsValue = get(mutableDateTime.getLong()) - get(type.getRange());
        long result = lenientField.set(mutableDateTime.getLong(), outOfBoundsValue);

        long expectedValue = mutableDateTime.getLong() - getType().getRange();
        assertEquals(expectedValue, result);
        assertEquals(expectedValue, get(result));
    }

    @Test
    public void testSet_InvalidValue() {
        BaseDateTime baseDateTime = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime = new MutableDateTime(baseDateTime);

        int invalidValue = Integer.MIN_VALUE;
        long result = lenientField.set(mutableDateTime.getLong(), invalidValue);

        assertEquals(mutableDateTime.getLong(), result);
        assertEquals(get(mutableDateTime.getLong()), get(result));
    }

    @Test
    public void testSet_NullDateTime() {
        assertNull(lenientField.set(null, 0));
    }

    @Test
    public void testSet_NullValue() {
        BaseDateTime baseDateTime = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime = new MutableDateTime(baseDateTime);

        assertNull(lenientField.set(mutableDateTime.getLong(), null));
        assertEquals(mutableDateTime.getLong(), get(mutableDateTime.getLong()));
    }

    @Test
    public void testSet_SameDateTime() {
        BaseDateTime baseDateTime = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime = new MutableDateTime(baseDateTime);

        long result = lenientField.set(mutableDateTime.getLong(), mutableDateTime.getLong());
        assertEquals(mutableDateTime.getLong(), result);
        assertEquals(get(mutableDateTime.getLong()), get(result));
    }

    @Test
    public void testSet_SameValue() {
        BaseDateTime baseDateTime = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime = new MutableDateTime(baseDateTime);

        int value = get(mutableDateTime.getLong());
        long result = lenientField.set(mutableDateTime.getLong(), value);

        assertEquals(mutableDateTime.getLong(), result);
        assertEquals(value, get(result));
    }

    @Test
    public void testSet_DateAndTime() {
        Chronology chronology = Chronology.ISO_DATE_TIME;
        DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.INSTANCE, chronology);

        Instant instant = new Instant(1412315200000L);
        LocalDateTime localDateTime = new LocalDateTime(instant, chronology);
        MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime(localDateTime);

        int value = get(mutableLocalDateTime.getLong());
        int newValue = value + 1;
        long result = field.set(mutableLocalDateTime.getLong(), newValue);

        assertEquals(result, mutableLocalDateTime.getLong());
        assertEquals(newValue, get(result));

        LocalTime localTime = new LocalTime(mutableLocalDateTime.toLocalDateTime());
        Instant newInstant = new Instant(result);
        assertEquals(newInstant.getMillis(), newInstant.getMillis() + (1000 * 60 * 60 * 24 * 30), 1); // 30 days

        assertEquals(localTime.getHourOfDay(), newValue % 24);
        assertEquals(localTime.getMinuteOfHour(), (newValue % 60) % 60);
        assertEquals(localTime.getSecondOfMinute(), (newValue % 60) % 60);
    }

    @Test
    public void testSet_SameDateTimeAndSameValue() {
        BaseDateTime baseDateTime = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime = new MutableDateTime(baseDateTime);

        long result = lenientField.set(mutableDateTime.getLong(), mutableDateTime.getLong());
        assertEquals(mutableDateTime.getLong(), result);
        assertEquals(get(mutableDateTime.getLong()), get(result));
    }

    @Test
    public void testSet_SameDateTimeAndDifferentValue() {
        BaseDateTime baseDateTime = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime = new MutableDateTime(baseDateTime);

        int value = get(mutableDateTime.getLong());
        int newValue = value + 1;
        long result = lenientField.set(mutableDateTime.getLong(), newValue);

        assertEquals(mutableDateTime.getLong(), result);
        assertEquals(newValue, get(result));

        MutableDateTime newMutableDateTime = new MutableDateTime(result);
        assertEquals(mutableDateTime, newMutableDateTime);
        assertEquals(mutableDateTime.getLocalDate(), newMutableDateTime.getLocalDate());
        assertEquals(mutableDateTime.getLocalTime(), newMutableDateTime.getLocalTime());
        assertEquals(mutableDateTime.getChronology(), newMutableDateTime.getChronology());
        assertEquals(mutableDateTime.getZone(), newMutableDateTime.getZone());
    }

    @Test
    public void testSet_DiffrentDateTime() {
        BaseDateTime baseDateTime1 = new BaseDateTime(0, chronology);
        MutableDateTime mutableDateTime1 = new MutableDateTime(baseDateTime1);

        BaseDateTime baseDateTime2 = new BaseDateTime(1412315200000L, chronology);
        MutableDateTime mutableDateTime2 = new MutableDateTime(baseDateTime2);

        int value1 = get(mutableDateTime1.getLong());
        int value2 = get(mutableDateTime2.getLong());
        int newValue = value1 + 1;
        long result = lenientField.set(mutableDateTime2.getLong(), newValue);

        assertEquals(mutableDateTime2.getLong(), result);
        assertEquals(newValue, get(result));

        MutableDateTime newMutableDateTime = new MutableDateTime(result);
        assertNotSame(mutableDateTime1, newMutableDateTime);
        assertEquals(mutableDateTime1.getLocalDate(), newMutableDateTime.getLocalDate());
        assertEquals(mutableDateTime1.getLocalTime(), newMutableDateTime.getLocalTime());
        assertEquals(mutableDateTime1.getChronology(), newMutableDateTime.getChronology());
        assertNotSame(mutableDateTime1.getZone(), newMutableDateTime.getZone());

        assertEquals(mutableDateTime1.getZone(), mutableDateTime2.getZone());
        assertEquals(mutableDateTime1.getLocalDate(), mutableDateTime2.getLocalDate());
        assertEquals(mutableDateTime1.getLocalTime(), mutableDateTime2.getLocalTime());
    }

    @Test
    public void testSet_SameDateTimeAndSameValueWithOffset() {
        Chronology chronology = ZoneUtils.UTC;
        DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.INSTANCE, chronology);

        Instant instant = Instant.now();
        MutableDateTime mutableDateTime = new MutableDateTime(instant, chronology);

        int value = get(mutableDateTime.getLong());
        int newValue = value + 1;
        long result = field.set(mutableDateTime.getLong(), newValue);

        assertEquals(mutableDateTime.getLong(), result);
        assertEquals(newValue, get(result));

        Instant newInstant = new Instant(result);
        assertEquals(newInstant.getMillis(), newInstant.getMillis() + (1000 * 60 * 60 * 24 * 30), 1); // 30 days
    }

    @Test
    public void testSet_SameDateTimeAndSameValueWithOffsetAndDst() {
        Chronology chronology = Chronology.forZone(ZoneId.of("Europe/London"));
        DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.INSTANCE, chronology);

        Instant instant = Instant.now();
        MutableDateTime mutableDateTime = new MutableDateTime(instant, chronology);

        int value = get(mutableDateTime.getLong());
        int newValue = value + 1;
        long result = field.set(mutableDateTime.getLong(), newValue);

        assertEquals(mutableDateTime.getLong(), result);
        assertEquals(newValue, get(result));

        Instant newInstant = new Instant(result);
        assertEquals(newInstant.getMillis(), newInstant.getMillis() + (1000 * 60 * 60 * 24 * 30), 1); // 30 days

        assertEquals(mutableDateTime.getZone().getIDs().get(0), mutableDateTime.getZone().getIDs().get(1));
        assertEquals(mutableDateTime.getZone().getIDs().get(1), "Europe/London");
    }

    private int get(long instant) {
        return (int) lenientField.get(instant);
    }
}