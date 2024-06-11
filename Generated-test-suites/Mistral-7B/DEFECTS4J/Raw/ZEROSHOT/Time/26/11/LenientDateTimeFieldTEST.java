import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class LenientDateTimeFieldTest {

    private Chronology mockChronology;
    private DateTimeField mockField;

    @Test
    public void testInstance() {
        mockChronology = mock(Chronology.class);
        mockField = mock(DateTimeField.class);

        DateTimeField lenientField = LenientDateTimeField.getInstance(mockField, mockChronology);

        assertTrue(lenientField instanceof LenientDateTimeField);
        assertEquals(mockField, lenientField.getDelegate());
        assertEquals(mockChronology, lenientField.getBase());
    }

    @Test
    public void testIsLenient() {
        mockChronology = mock(Chronology.class);
        mockField = mock(DateTimeField.class);

        LenientDateTimeField lenientField = new LenientDateTimeField(mockField, mockChronology);

        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        mockChronology = mock(Chronology.class);
        mockField = mock(DateTimeField.class);
        BaseDateTime baseDateTime = new BaseDateTime(0, mockChronology);

        when(mockField.getLocalMinimumValue()).thenReturn(DateTimeFields.getLocalMinimumValue(DateTimeFields.MILLISECONDS));
        when(mockField.getLocalMaximumValue()).thenReturn(DateTimeFields.getLocalMaximumValue(DateTimeFields.MILLISECONDS));

        LenientDateTimeField lenientField = new LenientDateTimeField(mockField, mockChronology);

        long currentInstant = baseDateTime.getMillis();
        int value = FieldUtils.safeAdd(get(currentInstant, lenientField), 10);

        long result = lenientField.set(currentInstant, value);

        assertEquals(currentInstant + 10 * mockField.getIncrement(), result);
    }

    @Test
    public void testSetNegativeValue() {
        mockChronology = mock(Chronology.class);
        mockField = mock(DateTimeField.class);
        BaseDateTime baseDateTime = new BaseDateTime(0, mockChronology);

        when(mockField.getLocalMinimumValue()).thenReturn(DateTimeFields.getLocalMinimumValue(DateTimeFields.MILLISECONDS));
        when(mockField.getLocalMaximumValue()).thenReturn(DateTimeFields.getLocalMaximumValue(DateTimeFields.MILLISECONDS));

        LenientDateTimeField lenientField = new LenientDateTimeField(mockField, mockChronology);

        long currentInstant = baseDateTime.getMillis();
        int value = FieldUtils.safeSubtract(get(currentInstant, lenientField), 10);

        long result = lenientField.set(currentInstant, value);

        assertEquals(currentInstant - 10 * mockField.getIncrement(), result);
    }

    private int get(long instant, DateTimeField field) {
        LocalDateTime localDateTime = new LocalDateTime(instant, mockChronology);
        return field.get(localDateTime);
    }
}