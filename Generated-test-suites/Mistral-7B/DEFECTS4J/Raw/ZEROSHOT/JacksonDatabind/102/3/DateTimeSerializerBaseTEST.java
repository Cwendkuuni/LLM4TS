import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> dateTimeSerializer;
    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;
    private JsonParser jsonParser;

    @Before
    public void setUp() {
        serializerProvider = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        jsonParser = mock(JsonParser.class);

        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                _serializeAsString(value, gen, serializers);
            }
        };
    }

    @Test
    public void testCreateContextualWithNullProperty() {
        when(serializerProvider.getLocale()).thenReturn(Locale.US);
        when(serializerProvider.getTimeZone()).thenReturn(TimeZone.getDefault());

        assertEquals(dateTimeSerializer, dateTimeSerializer.createContextual(serializerProvider, null));
        verifyNoMoreInteractions(serializerProvider);
    }

    @Test
    public void testCreateContextualWithProperty() {
        BeanProperty property = mock(BeanProperty.class);
        when(property.getName()).thenReturn("dateProperty");

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("dd-MM-yyyy HH:mm:ss z");

        when(serializerProvider.findValueSerializer(Date.class)).thenReturn(dateTimeSerializer);
        when(serializerProvider.findPropertyValueSerializer(property, Date.class)).thenReturn(null);

        assertEquals(dateTimeSerializer, dateTimeSerializer.createContextual(serializerProvider, property));

        verify(serializerProvider, times(1)).findValueSerializer(Date.class);
        verify(serializerProvider, times(1)).findPropertyValueSerializer(property, Date.class);
        verify(serializerProvider, times(1)).getLocale();
        verify(serializerProvider, times(1)).getTimeZone();
        verifyNoMoreInteractions(serializerProvider);
    }

    @Test
    public void testCreateContextualWithFormatOverrides() {
        BeanProperty property = mock(BeanProperty.class);
        when(property.getName()).thenReturn("dateProperty");

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("dd-MM-yyyy HH:mm:ss z");

        when(serializerProvider.findValueSerializer(Date.class)).thenReturn(dateTimeSerializer);
        when(serializerProvider.findPropertyValueSerializer(property, Date.class)).thenReturn(null);
        when(serializerProvider.findPropertyValueSerializer(property, JsonFormat.Value.class)).thenReturn(format);

        DateTimeSerializerBase<Date> serializedSerializer = dateTimeSerializer.withFormat(false, format);

        assertEquals(serializedSerializer, dateTimeSerializer.createContextual(serializerProvider, property));

        verify(serializerProvider, times(1)).findValueSerializer(Date.class);
        verify(serializerProvider, times(1)).findPropertyValueSerializer(property, Date.class);
        verify(serializerProvider, times(1)).findPropertyValueSerializer(property, JsonFormat.Value.class);
        verifyNoMoreInteractions(serializerProvider);
    }

    @Test
    public void testSerializeAsString() throws IOException {
        Date date = new Date();

        when(jsonGenerator.getOutputFactory()).thenReturn(new JsonFactory().getGeneratorFactory());
        when(jsonGenerator.writeString(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        dateTimeSerializer.serialize(date, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeString(anyString());
        verifyNoMoreInteractions(jsonGenerator);

        String serializedDate = verify(jsonGenerator, times(1)).getOutput();
        assertEquals("27-01-2023 14:15:19 UTC", serializedDate);
    }
}