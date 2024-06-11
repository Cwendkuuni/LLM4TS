
package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField field;

    private DateTimeField wrappedField;
    private Chronology baseChronology;

    @Before
    public void setUp() {
        wrappedField = mock(DateTimeField.class);
        baseChronology = mock(Chronology.class);
        field = new LenientDateTimeField(wrappedField, baseChronology);
    }

    @Test
    public void testIsLenient() {
        when(wrappedField.isLenient()).thenReturn(false);
        assertEquals(true, field.isLenient());

        when(wrappedField.isLenient()).thenReturn(true);
        assertEquals(true, field.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeZone.setDefault(DateTimeZone.UTC);
        DateTimeUtils.setCurrentMillisFixed(123456789L);

        long instant = 1000L;
        int value = 5;
        int currentValue = 10;

        when(wrappedField.get(instant)).thenReturn(currentValue);
        when(baseChronology.withUTC()).thenReturn(ISOChronology.getInstanceUTC());
        when(wrappedField.add(instant, value - currentValue)).thenReturn(2000L);
        when(baseChronology.getZone()).thenReturn(DateTimeZone.UTC);
        assertEquals(2000L, field.set(instant, value));
    }
}
