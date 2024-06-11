package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;
import com.fasterxml.jackson.databind.type.TypeFactoryImpl;

/**
 * Test class for FromStringDeserializer
 */
@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Class<?>[] types = FromStringDeserializer.types();
        for (Class<?> type : types) {
            FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(type);
            assertNotNull(deserializer);
        }
    }

    /**
     * Test deserialization of supported types from string.
     *
     * @throws Exception if deserialization fails
     */
    @Test
    public void testDeserializeSupportedTypes() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test serialization and deserialization of supported types
        for (Class<?> type : FromStringDeserializer.types()) {
            FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(type);
            String jsonString = mapper.writeValueAsString(deserializer.getValueClass().newInstance());
            Object deserializedObject = mapper.readValue(jsonString, Object.class);
            assertEquals(deserializer.getValueClass(), deserializedObject.getClass());
        }
    }

    /**
     * Test deserialization of empty string.
     *
     * @throws Exception if deserialization fails
     */
    @Test
    public void testDeserializeEmptyString() throws Exception {
        FromStringDeserializer.Std fileStd = new FromStringDeserializer.Std(File.class, Std.STD_FILE);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new TypeFactoryImpl());
        JsonParser p = ctxt.getFactory().createParser("\"\"");
        assertNull(fileStd.deserialize(p, ctxt));

        FromStringDeserializer.Std uriStd = new FromStringDeserializer.Std(URI.class, Std.STD_URI);
        assertInstanceOf(URI.class, uriStd._deserializeFromEmptyString());

        FromStringDeserializer.Std localeStd = new FromStringDeserializer.Std(Locale.class, Std.STD_LOCALE);
        assertSame(Locale.ROOT, localeStd._deserializeFromEmptyString());

        FromStringDeserializer.Std timeZoneStd = new FromStringDeserializer.Std(TimeZone.class, Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getDefault(), timeZoneStd._deserializeFromEmptyString());
    }

    /**
     * Test deserialization of null string.
     *
     * @throws Exception if deserialization fails
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullString() throws Exception {
        FromStringDeserializer.Std urlStd = new FromStringDeserializer.Std(URL.class, Std.STD_URL);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new TypeFactoryImpl());
        JsonParser p = ctxt.getFactory().createParser(null);
        urlStd.deserialize(p, ctxt);
    }

    /**
     * Test deserialization of invalid string.
     *
     * @throws Exception if deserialization fails
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws Exception {
        FromStringDeserializer.Std patternStd = new FromStringDeserializer.Std(Pattern.class, Std.STD_PATTERN);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new TypeFactoryImpl());
        JsonParser p = ctxt.getFactory().createParser("invalid_pattern");
        patternStd.deserialize(p, ctxt);
    }
}