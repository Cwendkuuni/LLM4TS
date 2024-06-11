import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> dateTimeSerializer;
    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;
    private ObjectMapper objectMapper;
    private TypeSerializer typeSerializer;

    @Before
    public void setup() {
        serializerProvider = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        objectMapper = new ObjectMapper();
        typeSerializer = objectMapper.getTypeSerializer();
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
    }

    @Test
    public void testCreateContextual() {
        // Test with default settings
        JsonSerializer<Date> result = dateTimeSerializer.createContextual(serializerProvider, null);
        Assert.assertEquals(dateTimeSerializer, result);

        // Test with custom settings
        JsonFormat.Value customFormat = new JsonFormat.Value();
        customFormat.setShape(JsonFormat.Shape.STRING);
        customFormat.setLocale(Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCodec()).thenReturn(objectMapper);
        when(jsonParser.getConfig()).thenReturn(objectMapper.getSerializationConfig());
        BeanProperty property = mock(BeanProperty.class);
        when(property.getName()).thenReturn("dateProperty");
        when(property.getType()).thenReturn(Date.class);
        when(property.hasAnnotation(JsonFormat.class)).thenReturn(true);
        when(property.getAnnotation(JsonFormat.class).value()).thenReturn(customFormat);
        result = dateTimeSerializer.createContextual(serializerProvider, property);
        Assert.assertNotNull(result);
        verify(jsonParser, times(1)).getCodec();
        verify(jsonParser, times(1)).getConfig();
        verify(jsonParser, times(1)).findFormatOverrides(serializerProvider, property, Date.class);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(dateTimeSerializer.isEmpty(serializerProvider, null));
        Assert.assertFalse(dateTimeSerializer.isEmpty(serializerProvider, new Date(0)));
    }

    @Test
    public void testGetSchema() {
        JsonNode result = dateTimeSerializer.getSchema(serializerProvider, Date.class);
        Assert.assertEquals("string", result.getClass().getName());
    }

    @Test
    public void testSerialize() throws IOException {
        Date date = new Date(1633052800000L); // Thu, 21 Oct 2100 01:00:00 UTC
        dateTimeSerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeString(anyString());

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat customFormat = (DateFormat) dateTimeSerializer._customFormat.clone();
        customFormat.setFormat(format.toPattern());
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        dateTimeSerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeString(eq("2100-10-21 01:00:00"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSerializeWithNullProvider() throws IOException {
        dateTimeSerializer.serialize(new Date(), jsonGenerator, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullValue() throws IOException {
        dateTimeSerializer.serialize(null, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerializeWithUseTimestampTrue() throws IOException {
        DateTimeSerializerBase<Date> timestampSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        Date date = new Date(1633052800000L);
        timestampSerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeNumber(eq(1633052800000L));
    }

    @Test
    public void testSerializeWithUseTimestampFalse() throws IOException {
        DateTimeSerializerBase<Date> stringSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        Date date = new Date(1633052800000L);
        stringSerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeString(anyString());
    }
}