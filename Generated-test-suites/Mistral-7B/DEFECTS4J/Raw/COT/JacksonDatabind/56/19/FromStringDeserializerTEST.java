package com.example.junit; // Replace this package with the actual package name of your test file

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature.Order;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.module.java8.Java8Module;

/**
 * JUnit 4 test file for FromStringDeserializer class.
 */
@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest extends TestBase {

    // Replace this with the actual ObjectMapper instance used in your application
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new Java8Module());
    }

    @Test
    public void testTypes() throws Exception {
        Class<?>[] actualTypes = FromStringDeserializer.types();
        Class<?>[] expectedTypes = { File.class, URL.class, URI.class, Class.class, JavaType.class, Currency.class, Pattern.class, Locale.class, Charset.class, TimeZone.class, InetAddress.class, InetSocketAddress.class };
        assertArrayEquals(expectedTypes, actualTypes);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        // Test for known types
        for (Class<?> type : FromStringDeserializer.types()) {
            Std expectedStd = FromStringDeserializer.findDeserializer(type);
            assertNotNull(expectedStd);
            assertEquals(type.getName(), expectedStd.getValueClass().getName());
        }

        // Test for unknown type
        assertNull(FromStringDeserializer.findDeserializer(String.class));
    }

    @Test
    public void testDeserialize() throws Exception {
        // Test for null input
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getValueAsString()).thenReturn(null);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);
        when(mockContext.getTypeDeserializer()).thenReturn(mockTypeDeserializer);
        when(mockTypeDeserializer.deserializeTypedValue(eq(mockParser), eq(mockContext), anyObject())).thenReturn(null);

        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        assertNull(deserializer.deserialize(mockParser, mockContext));

        // Test for empty string input
        when(mockParser.getValueAsString()).thenReturn("");

        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
        assertEquals(Locale.ROOT, localeDeserializer.deserialize(mockParser, mockContext));

        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
        assertEquals(URI.create(""), uriDeserializer.deserialize(mockParser, mockContext));

        // Test for valid input
        when(mockParser.getValueAsString()).thenReturn("example.txt");

        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        File expectedFile = new File("example.txt");
        assertEquals(expectedFile, fileDeserializer.deserialize(mockParser, mockContext));

        // Test for embedded object input
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_EMBEDDED_OBJECT);
        Object embeddedObject = new Object();
        when(mockParser.getEmbeddedObject()).thenReturn(embeddedObject);

        FromStringDeserializer<Object> embeddedObjectDeserializer = new FromStringDeserializer<Object>(Object.class);
        assertEquals(embeddedObject, embeddedObjectDeserializer.deserialize(mockParser, mockContext));

        // Test for exception handling
        when(mockParser.getValueAsString()).thenThrow(new IOException("Error reading string"));

        FromStringDeserializer<Object> stringDeserializer = new FromStringDeserializer<Object>(Object.class);
        assertNull(stringDeserializer.deserialize(mockParser, mockContext));

        when(mockParser.getValueAsString()).thenThrow(new InvalidFormatException("Invalid format", mockParser, null));

        FromStringDeserializer<Object> invalidFormatDeserializer = new FromStringDeserializer<Object>(Object.class);
        assertNull(invalidFormatDeserializer.deserialize(mockParser, mockContext));

        when(mockParser.getValueAsString()).thenThrow(new IllegalArgumentException("Illegal argument"));

        FromStringDeserializer<Object> illegalArgumentDeserializer = new FromStringDeserializer<Object>(Object.class);
        assertNull(illegalArgumentDeserializer.deserialize(mockParser, mockContext));

        // Test for custom deserializer implementation
        FromStringDeserializer<Locale> customLocaleDeserializer = new FromStringDeserializer<Locale>(Locale.class) {
            @Override
            protected Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
                int ix = value.indexOf('_');
                if (ix < 0) {
                    return new Locale(value);
                }
                String first = value.substring(0, ix);
                String second = value.substring(ix + 1);
                return new Locale(first, second, value.substring(ix + 2));
            }
        };

        assertEquals(new Locale("en", "US", ""), customLocaleDeserializer.deserialize(mockParser, mockContext));
        assertEquals(new Locale("en", "US", "California"), customLocaleDeserializer.deserialize(mockParser, mockContext));
    }

    @Test
    public void testDeserializeWithFeature() throws Exception {
        // Test for unwrapped single value arrays
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);

        String json = "[\"" + new File("example.txt").getAbsolutePath() + "\"]";

        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        File expectedFile = new File("example.txt");
        assertEquals(expectedFile, fileDeserializer.deserialize(MAPPER.createParser(json), MAPPER.getDeserializationContext()));

        objectMapper.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);

        String jsonUnwrapped = "\"" + new File("example.txt").getAbsolutePath() + "\"";

        File file = fileDeserializer.deserialize(MAPPER.createParser(jsonUnwrapped), MAPPER.getDeserializationContext());
        assertNull(file);

        assertEquals(expectedFile, fileDeserializer.deserialize(MAPPER.createParser(json), MAPPER.getDeserializationContext()).getValue());
    }

    @Test
    public void testDeserializeWithCustomTypeSerializer() throws Exception {
        // Test for custom type serializer
        final Class<Calendar> calendarClass = Calendar.class;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Java8Module());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.setSerializationFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOS);
        objectMapper.setSerializationFeature(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        objectMapper.setSerializationFeature(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.setSerializationFeature(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);

        JavaType javaType = MAPPER.getTypeFactory().constructType(Collections.class);
        TypeSerializer typeSerializer = new TypeSerializer() {
            @Override
            public void serializeValue(SerializerProvider p, Object value, TypeSerializerValueWriter writer, Locale l) throws IOException {
                writer.writeValue("");
            }

            @Override
            public Object deserializeValue(DeserializationContext ctxt, TypeDeserializer deserializer, TypeDeserializer.EmptyValueSupplier creatorSupplier, Object beanProperty) throws IOException {
                return Collections.emptyList();
            }

            @Override
            public void serializeValue(SerializerProvider p, Object value, JsonGenerator g, SerializerProvider.ValueWriter writer) throws IOException {
                g.writeValue(value);
            }

            @Override
            public Object deserializeValue(DeserializationContext ctxt, TypeDeserializer deserializer, Object beanProperty) throws IOException {
                return Arrays.asList((Calendar) deserializer.deserializeTypedValue(ctxt, Calendar.class));
            }
        };

        ObjectMapper objectMapperWithCustomSerializer = new ObjectMapper();
        objectMapperWithCustomSerializer.registerModule(new Java8Module());
        objectMapperWithCustomSerializer.registerSerializerInstance(javaType.getRawClass(), typeSerializer);

        String json = "{\"calendar\": \"" + calendar.getTimeInMillis() + "\"}";

        FromStringDeserializer<Calendar> calendarDeserializer = new FromStringDeserializer<Calendar>(Calendar.class);
        assertEquals(calendar, calendarDeserializer.deserialize(MAPPER.createParser(json), MAPPER.getDeserializationContext()));

        assertEquals(calendar, calendarDeserializer.deserialize(objectMapperWithCustomSerializer.createParser(json), objectMapperWithCustomSerializer.getDeserializationContext()).getValue());
    }
}