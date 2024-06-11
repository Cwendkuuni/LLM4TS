import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
@Category(junit.categories.JodaTimeTest.class)
public class LenientDateTimeFieldTest {

    private final DateTimeField field;
    private final Chronology base;

    public LenientDateTimeFieldTest(DateTimeField field, Chronology base) {
        this.field = field;
        this.base = base;
    }

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> data = Arrays.asList(new Object[][]{
                {new LocalDateField(DateTimeFieldType.dayOfMonth(), ISOChronology.getInstance()), ISOChronology.getInstance()},
                {new LocalDateTimeField(DateTimeFieldType.hourOfDay(), ISOChronology.getInstance()), ISOChronology.getInstance()},
                {new LocalDateTimeField(DateTimeFieldType.minuteOfHour(), ISOChronology.getInstance()), ISOChronology.getInstance()},
                {new LocalDateTimeField(DateTimeFieldType.secondOfMinute(), ISOChronology.getInstance()), ISOChronology.getInstance()},
                {new LocalDateTimeField(DateTimeFieldType.millisOfSecond(), ISOChronology.getInstance()), ISOChronology.getInstance()},
                {new LocalDateTimeField(new DurationFieldType() {
                    {
                        setName("millisOfDay");
                        setGet(new DateTimeField() {
                            public int get(long instant) {
                                return (int) (Type.getField(base).get(instant) / 1000L);
                            }
                        });
                        setSet(new DateTimeField() {
                            public long set(long instant, int value) {
                                return Type.getField(base).set(instant, value * 1000);
                            }
                        });
                    }
                }, ISOChronology.getInstance()), ISOChronology.getInstance()},
                {new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.minuteOfHour(), ISOChronology.getInstance()), ISOChronology.getInstance()), ISOChronology.getInstance()}
        });
        return data;
    }

    @Test
    public void testGetInstance() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertNotNull(lenientField);
        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.getBase());
        assertTrue(lenientField instanceof LenientDateTimeField);
    }

    @Test
    public void testIsLenient() {
        assertTrue(field.isLenient());
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSetOutOfBounds() {
        MutableDateTime dateTime = new MutableDateTime(BaseDateTime.UNIX_UTC);
        long current = dateTime.getMillis();

        long newValue = current + (getFieldType().getPrecision() * 2);
        long lenientValue = lenientField.get(current) + (getFieldType().getPrecision() * 2);

        long setValue = lenientField.set(current, newValue);

        assertEquals(setValue, current + getFieldType().getPrecision() * 2);
        assertEquals(getFieldType().get(setValue), lenientValue);
    }

    @Test
    public void testSetInBounds() {
        MutableDateTime dateTime = new MutableDateTime(BaseDateTime.UNIX_UTC);
        long current = dateTime.getMillis();

        long newValue = getFieldType().get(current);

        long setValue = lenientField.set(current, newValue);

        assertEquals(setValue, current);
        assertEquals(getFieldType().get(setValue), newValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNull() {
        lenientField.set(0, null);
    }

    @Test
    public void testSetNegative() {
        MutableDateTime dateTime = new MutableDateTime(BaseDateTime.UNIX_UTC);
        long current = dateTime.getMillis();

        long newValue = current - (getFieldType().getPrecision() * 2);

        long setValue = lenientField.set(current, newValue);

        assertEquals(setValue, current - getFieldType().getPrecision() * 2);
        assertEquals(getFieldType().get(setValue), getFieldType().get(current) - getFieldType().getPrecision() * 2);
    }

    @Test
    public void testSetOutOfBoundsNegative() {
        MutableDateTime dateTime = new MutableDateTime(BaseDateTime.UNIX_UTC);
        long current = dateTime.getMillis();

        long newValue = current - (getFieldType().getPrecision() * 3);
        long lenientValue = getFieldType().get(current) - getFieldType().getPrecision() * 3;

        long setValue = lenientField.set(current, newValue);

        assertEquals(setValue, current - getFieldType().getPrecision() * 2);
        assertEquals(getFieldType().get(setValue), lenientValue);
    }

    private DateTimeFieldType getFieldType() {
        return field.getFieldType();
    }
}