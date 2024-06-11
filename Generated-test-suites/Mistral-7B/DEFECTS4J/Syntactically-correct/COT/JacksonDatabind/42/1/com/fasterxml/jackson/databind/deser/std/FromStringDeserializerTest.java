package com.example.test; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;
import com.fasterxml.jackson.databind.deser.std.StdClassDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdJavaTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdLocaleDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdPatternDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdTimeZoneDeserializer;

@RunWith(JUnit4.class)
public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    public FromStringDeserializerTest() throws IOException {
        mapper = JsonMapperFactory.builder()
                .withSerializers(new StdSerializers.Base())
                .withFeatures(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
    }

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"path/to/file.txt\"");
        Std.StdFile deserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com\"");
        Std.StdURL deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com/path?query#fragment\"");
        Std.StdURI deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("http://example.com/path?query#fragment", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String.class\"");
        StdClassDeserializer<String> deserializer = (StdClassDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"com.example.CustomType.class\"");
        JavaType javaType = mapper.getTypeFactory().constructType(parser.getValueAsString());
        StdJavaTypeDeserializer deserializer = (StdJavaTypeDeserializer) FromStringDeserializer.findDeserializer(javaType.getRawClass());
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(javaType, mapper.getTypeFactory().constructType(result.getClass()));
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"USD\"");
        Std.StdCurrency deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"[a-zA-Z]+\\.\\w+\"");
        StdPatternDeserializer deserializer = (StdPatternDeserializer) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("[a-zA-Z]+\\.\\w+", pattern.pattern());
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        StdLocaleDeserializer deserializer = (StdLocaleDeserializer) FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("en_US", locale.toLanguageTag());

        parser = mapper.getFactory().createParser("\"en_US_POSIX\"");
        locale = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("en_US_POSIX", locale.toLanguageTag());

        parser = mapper.getFactory().createParser("\"en_US_POSIX.UTF-8\"");
        locale = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("en_US_POSIX.UTF-8", locale.toLanguageTag());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"Europe/Paris\"");
        StdTimeZoneDeserializer deserializer = (StdTimeZoneDeserializer) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("Europe/Paris", timeZone.getID());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        Std.StdFile fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        assertThrows(JsonMappingException.class, () -> fileDeserializer.deserialize(parser, mapper.getDeserializationContext()));

        Std.StdURI uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = uriDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNull(uri);
    }

    @Test
    public void testDeserializeInvalidClass() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("\"java.util.ArrayList.class\"");
        assertThrows(JsonMappingException.class, () -> FromStringDeserializer.findDeserializer(parser.getValueAsString()));
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = mapper.copy();
        objectMapper.registerModule(new SimpleModule() {
            @Override
            public StdSerializer<EmbeddedObject> _findEmptyValueSerializer(Class<?> type, JsonSerializerProvider serializers) {
                return new StdSerializer<EmbeddedObject>(EmbeddedObject.class) {
                    @Override
                    public EmbeddedObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return new EmbeddedObject();
                    }
                };
            }
        });

        JsonParser parser = mapper.getFactory().createParser("{" +
                "\"class\": \"com.example.EmbeddedObject\"," +
                "\"property\": \"Test value\"" +
                "}");
        EmbeddedObject embeddedObject = parser.readValueAs(EmbeddedObject.class);
        assertEquals("Test value", embeddedObject.getProperty());
    }

    @Test
    public void testDeserializeInvalidEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = mapper.copy();
        objectMapper.registerModule(new SimpleModule() {
            @Override
            public StdSerializer<EmbeddedObject> _findEmptyValueSerializer(Class<?> type, JsonSerializerProvider serializers) {
                return new StdSerializer<EmbeddedObject>(EmbeddedObject.class) {
                    @Override
                    public EmbeddedObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return new EmbeddedObject();
                    }
                };
            }
        });

        JsonParser parser = mapper.getFactory().createParser("{" +
                "\"class\": \"com.example.InvalidEmbeddedObject\"," +
                "\"property\": \"Test value\"" +
                "}");
        assertThrows(JsonMappingException.class, () -> parser.readValueAs(EmbeddedObject.class));
    }

    private static class EmbeddedObject {
        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

    private static class InvalidEmbeddedObject {
        // an invalid embedded object class for testing
    }
}