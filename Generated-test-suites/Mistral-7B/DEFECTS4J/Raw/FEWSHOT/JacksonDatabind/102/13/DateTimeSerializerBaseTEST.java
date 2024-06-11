package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        // Test empty constructor
        DateTimeSerializerBase<?> emptySerializer = new DateTimeSerializerBase<>(null, null, null);
        assertNotNull(emptySerializer);

        // Test constructor with useTimestamp and null customFormat
        DateTimeSerializerBase<Date> useTimestampSerializer = new DateTimeSerializerBase<>(Date.class, true, null);
        assertNotNull(useTimestampSerializer);

        // Test constructor with useTimestamp and customFormat
        DateTimeSerializerBase<Date> customFormatSerializer = new DateTimeSerializerBase<>(Date.class, true, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        assertNotNull(customFormatSerializer);

        // Test constructor with null useTimestamp and null customFormat
        DateTimeSerializerBase<Date> nullUseNullCustomSerializer = new DateTimeSerializerBase<>(Date.class, null, null);
        assertNotNull(nullUseNullCustomSerializer);

        // Test constructor with null useTimestamp and customFormat
        DateTimeSerializerBase<Date> nullUseCustomSerializer = new DateTimeSerializerBase<>(Date.class, null, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        assertNotNull(nullUseCustomSerializer);

        // Test constructor with useTimestamp and null customFormat
        DateTimeSerializerBase<Date> useTimestampNullCustomSerializer = new DateTimeSerializerBase<>(Date.class, true, null);
        assertNotNull(useTimestampNullCustomSerializer);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> baseSerializer = new DateTimeSerializerBase<>(Date.class, true, null);

        DateTimeSerializerBase<Date> withFormatSerializer1 = baseSerializer.withFormat(true, null);
        assertTrue(withFormatSerializer1 instanceof DateTimeSerializerBase);
        assertEquals(baseSerializer.getClass(), withFormatSerializer1.getClass());
        assertNull(withFormatSerializer1._customFormat);
       assertTrue(withFormatSerializer1._useTimestamp);

        DateTimeSerializerBase<Date> withFormatSerializer2 = baseSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        assertTrue(withFormatSerializer2 instanceof DateTimeSerializerBase);
        assertEquals(baseSerializer.getClass(), withFormatSerializer2.getClass());
        assertNotNull(withFormatSerializer2._customFormat);
        assertFalse(withFormatSerializer2._useTimestamp);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer.Provider serializers = new SerializerProvider();
        SerializerFactory serializerFactory = new SerializerFactory();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<Date> baseSerializer = new DateTimeSerializerBase<>(Date.class, true, null);

        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setPattern("dd-MM-yyyy HH:mm:ss");

        JsonSerializer<Date> serializedDate = baseSerializer.createContextual(serializers, null);
        assertTrue(serializedDate instanceof DateTimeSerializerBase);
        assertEquals(baseSerializer.getClass(), serializedDate.getClass());
        assertNotNull(serializedDate._customFormat);
        assertTrue(serializedDate._useTimestamp);

        JsonSerializer<Date> serializedDate2 = baseSerializer.createContextual(serializers, null);
        assertEquals(serializedDate, serializedDate2);

        JsonSerializer<Date> serializedFormat = baseSerializer.createContextual(serializers, null);
        JsonSerializer<Date> serializedFormatCloned = serializedFormat.clone();
        assertNotEquals(serializedFormat, serializedFormatCloned);

        JsonSerializer<Date> serializedCustomFormat = baseSerializer.createContextual(serializers, new JsonProperty("test"));
        assertEquals(serializedFormat, serializedCustomFormat);

        JsonSerializer<Date> serializedCustomFormat2 = baseSerializer.createContextual(serializers, new JsonProperty("test2"));
        assertNotEquals(serializedFormat, serializedCustomFormat2);

        JerseyAnnotationIntrospector introspector = new JerseyAnnotationIntrospector();
        SerializationConfig config = new SerializationConfig();
        config.setAnnotationIntrospector(introspector);
        config.setDefaultFormatSerializerProvider(serializerFactory.findValueSerializer(config, Date.class));
        JsonSerializer<Date> serializedCustomFormat3 = baseSerializer.createContextual(config.getDefaultSerializerProvider(), new JsonProperty("test3"));
        assertNotEquals(serializedFormat, serializedCustomFormat3);

        JsonSerializer<Date> serializedNull = baseSerializer.createContextual(serializers, null);
        assertTrue(serializedNull instanceof DateTimeSerializerBase);
        assertFalse(serializedNull._useTimestamp);
        assertNull(serializedNull._customFormat);

        JsonSerializer<Date> serializedTimestamp = baseSerializer.createContextual(serializers, null);
        assertTrue(serializedTimestamp instanceof DateTimeSerializerBase);
        assertTrue(serializedTimestamp._useTimestamp);
        assertNull(serializedTimestamp._customFormat);

        JsonSerializer<Date> serializedNullCustomFormat = baseSerializer.createContextual(serializers, new JsonProperty("test"));
        assertTrue(serializedNullCustomFormat instanceof DateTimeSerializerBase);
        assertFalse(serializedNullCustomFormat._useTimestamp);
        assertNotNull(serializedNullCustomFormat._customFormat);

        JsonSerializer<Date> serializedCustomFormat4 = baseSerializer.createContextual(serializers, new JsonProperty("test2"));
        assertTrue(serializedCustomFormat4 instanceof DateTimeSerializerBase);
        assertFalse(serializedCustomFormat4._useTimestamp);
        assertNotNull(serializedCustomFormat4._customFormat);

        JsonSerializer<Date> serializedCustomFormat5 = baseSerializer.createContextual(serializers, new JsonProperty("test3"));
        assertTrue(serializedCustomFormat5 instanceof DateTimeSerializerBase);
        assertFalse(serializedCustomFormat5._useTimestamp);
        assertNotNull(serializedCustomFormat5._customFormat);

        JsonSerializer<Date> serializedCustomFormat6 = baseSerializer.createContextual(serializers, new JsonProperty("test4"));
        assertTrue(serializedCustomFormat6 instanceof DateTimeSerializerBase);
        assertFalse(serializedCustomFormat6._useTimestamp);
        assertNotNull(serializedCustomFormat6._customFormat);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        DateTimeSerializerBase<Date> baseSerializer = new DateTimeSerializerBase<>(Date.class, true, null);

        Date date1 = new Date();
        baseSerializer.serialize(date1, gen, new SerializerProvider());
        gen.flush();
        gen.close();

        DateTimeSerializerBase<Date> customFormatSerializer = new DateTimeSerializerBase<>(Date.class, true, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        Date date2 = new Date();
        customFormatSerializer.serialize(date2, gen, new SerializerProvider());
        gen.flush();
        gen.close();

        String output1 = System.in.readLine();
        String output2 = System.in.readLine();

        SimpleDateFormat expectedOutputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date expectedOutput1 = new Date();
        String expectedOutput1String = expectedOutputFormat.format(expectedOutput1);
        String expectedOutput2String = expectedOutputFormat.format(expectedOutput1);

        assertEquals(expectedOutput1String, output1);
        assertEquals(expectedOutput2String, output2);
    }

    @Test
    public void testSerializeCustomFormat() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        DateTimeSerializerBase<Date> baseSerializer = new DateTimeSerializerBase<>(Date.class, true, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        Date date = new Date();
        baseSerializer.serialize(date, gen, new SerializerProvider());
        gen.flush();
        gen.close();

        String output = System.in.readLine();
        SimpleDateFormat expectedOutputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date expectedOutput = new Date();
        String expectedOutputString = expectedOutputFormat.format(expectedOutput);

        assertEquals(expectedOutputString, output);
    }
}