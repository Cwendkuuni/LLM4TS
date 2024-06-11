import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> dateTimeSerializer;
    private SerializerProvider serializers;

    @Before
    public void setup() {
        serializers = Mockito.mock(SerializerProvider.class);
        dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.class, Boolean.FALSE, null) {
            @Override
            protected long _timestamp(Long value) {
                return value.longValue();
            }
        };
    }

    @Test
    public void testIsEmpty() {
        assertFalse(dateTimeSerializer.isEmpty(null));
        assertFalse(dateTimeSerializer.isEmpty(serializers, null));
        assertTrue(dateTimeSerializer.isEmpty(serializers, 0L));
        assertTrue(dateTimeSerializer.isEmpty(serializers, new Long(0)));
    }

    @Test
    public void testWithFormat() {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeSerializerBase<Long> customFormatSerializer = dateTimeSerializer.withFormat(Boolean.FALSE, customFormat);
        assertNotEquals(dateTimeSerializer, customFormatSerializer);
        assertEquals(customFormat, customFormatSerializer._customFormat);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateContextualWithNullProperty() {
        dateTimeSerializer.createContextual(serializers, null);
    }

    @Test
    public void testCreateContextualWithDefaultFormat() {
        JsonFormat.Value format = Mockito.mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.DEFAULT);
        when(serializers.findFormatOverrides(any(SerializerProvider.class), any(BeanProperty.class), any(Type.class))).thenReturn(format);

        JsonSerializer<?> serializer = dateTimeSerializer.createContextual(serializers, null);
        assertSame(dateTimeSerializer, serializer);
    }

    @Test
    public void testCreateContextualWithStringFormat() {
        JsonFormat.Value format = Mockito.mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.getPattern()).thenReturn("dd-MM-yyyy");
        when(format.getLocale()).thenReturn(Locale.ENGLISH);
        when(format.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(serializers.findFormatOverrides(any(SerializerProvider.class), any(BeanProperty.class), any(Type.class))).thenReturn(format);

        SimpleDateFormat expectedDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        expectedDateFormat.setLocale(Locale.ENGLISH);
        expectedDateFormat.setTimeZone(TimeZone.getDefault());
        DateTimeSerializerBase<Long> dateFormatSerializer = dateTimeSerializer.createContextual(serializers, null);
        assertEquals(expectedDateFormat, dateFormatSerializer._customFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullProvider() {
        dateTimeSerializer.createContextual(null, null);
    }

    @Test
    public void testSerialize() throws IOException {
        Long value = 1653323200000L; // 2022-01-01 00:00:00.000
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        when(gen.writeNumber(anyLong())).thenAnswer(i -> gen.writeString(String.valueOf(i)));

        dateTimeSerializer.serialize(value, gen, serializers);

        verify(gen, times(1)).writeString("1653323200000");
    }
}