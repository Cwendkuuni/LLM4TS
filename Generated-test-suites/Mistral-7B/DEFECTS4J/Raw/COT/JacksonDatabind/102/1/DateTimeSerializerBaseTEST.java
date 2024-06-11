import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultConstructor() {
        DateTimeSerializerBase<?> instance = new DateTimeSerializerBase<Date>(Date.class, false, null);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testWithFormatConstructor() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        Assert.assertNotNull(instance);
        Assert.assertEquals(customFormat, instance._customFormat);
    }

    @Test
    public void testWithTimestampTrue() {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, true, null);
        Assert.assertTrue(instance._useTimestamp);
    }

    @Test
    public void testWithTimestampFalseAndCustomFormat() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        Assert.assertFalse(instance._useTimestamp);
        Assert.assertEquals(customFormat, instance._customFormat);
    }

    @Test
    public void testIsEmptyReturnsFalse() {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, null);
        Assert.assertFalse(instance.isEmpty(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, null);
        instance.serialize(null, null, null);
    }

    @Test
    public void testSerializeWithDefaultFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, null);
        SerializerProvider serializerProvider = new SerializerProvider() {
            @Override
            public JsonGenerator getFactory() {
                return null;
            }

            @Override
            public void defaultSerializeDateValue(Date value, JsonGenerator gen) throws IOException {
                gen.writeString(new SimpleDateFormat("yyyy-MM-dd").format(value));
            }
        };
        Date date = new Date();
        TextNode result = (TextNode) instance.serialize(date, new JsonGenerator(){}, serializerProvider);
        Assert.assertEquals("2023-03-31", result.getTextValue());
    }

    @Test
    public void testSerializeWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        SerializerProvider serializerProvider = new SerializerProvider() {
            @Override
            public JsonGenerator getFactory() {
                return null;
            }
        };
        Date date = new Date();
        TextNode result = (TextNode) instance.serialize(date, new JsonGenerator(){}, serializerProvider);
        Assert.assertEquals("2023-03-31 00:00:00", result.getTextValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithLocale() throws JsonMappingException, IOException {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        SerializerProvider serializerProvider = new SerializerProvider() {
            @Override
            public Locale getLocale() {
                return Locale.CHINESE;
            }
        };
        Date date = new Date();
        instance.serialize(date, new JsonGenerator(){}, serializerProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithTimeZone() throws JsonMappingException, IOException {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        SerializerProvider serializerProvider = new SerializerProvider() {
            @Override
            public TimeZone getTimeZone() {
                return TimeZone.getTimeZone("UTC+1");
            }
        };
        Date date = new Date();
        instance.serialize(date, new JsonGenerator(){}, serializerProvider);
    }

    @Test
    public void testGetSchemaReturnsStringType() throws JsonMappingException {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, true, null);
        JsonNode schema = instance.getSchema(null, null);
        Assert.assertEquals("string", schema.getClass().getSimpleName());
    }

    @Test
    public void testGetSchemaReturnsNumberTypeWithUseTimestampTrue() throws JsonMappingException {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, true, null);
        JsonNode schema = instance.getSchema(null, null);
        Assert.assertEquals("number", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        instance.acceptJsonFormatVisitor(visitor, null);
        Assert.assertTrue(visitor.visitIntFormat(null, null, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void testSerializeWithCustomFormatAndThreadSafeReuse() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        DateTimeSerializerBase<Date> instance = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        SerializerProvider serializerProvider = new SerializerProvider() {
            @Override
            public JsonGenerator getFactory() {
                return null;
            }
        };
        SimpleDateFormat customFormat = (SimpleDateFormat) instance._customFormat.clone();
        Date date1 = new Date();
        Date date2 = new Date();
        instance.serialize(date1, new JsonGenerator(){}, serializerProvider);
        instance.serialize(date2, new JsonGenerator(){}, serializerProvider);
        Assert.assertEquals("2023-03-31 00:00:00", customFormat.format(date1));
        Assert.assertEquals("2023-03-31 00:00:01", customFormat.format(date2));
    }
}