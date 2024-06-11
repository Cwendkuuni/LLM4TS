package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> T createTestInstance(Class<T> type) {
        return (T) new FromStringDeserializer<T>(type);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setTypeResolverBuilder(new StandardTypeResolverBuilder());
        TypeDeserializer urlDeserializer = createTestInstance(URL.class);

        String testString = "http://www.example.com";
        JsonParser parser = new JsonFactory().createParser(testString);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setCurrentRootValueContainer(new Object());

        URI result = urlDeserializer.deserialize(parser, ctxt);

        URI expected = new URI("http", "www.example.com", null);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setTypeResolverBuilder(new StandardTypeResolverBuilder());
        TypeDeserializer uriDeserializer = createTestInstance(URI.class);

        String testString = "http://www.example.com/path/to/file.txt";
        JsonParser parser = new JsonFactory().createParser(testString);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setCurrentRootValueContainer(new Object());

        URI result = uriDeserializer.deserialize(parser, ctxt);

        URI expected = new URI("http", "www.example.com", "/path/to/file.txt");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setTypeResolverBuilder(new StandardTypeResolverBuilder());
        TypeDeserializer localeDeserializer = createTestInstance(Locale.class);

        String testString = "en_US";
        JsonParser parser = new JsonFactory().createParser(testString);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setCurrentRootValueContainer(new Object());

        Locale result = localeDeserializer.deserialize(parser, ctxt);

        Locale expected = Locale.forLanguageTag("en-US");
        assertEquals(expected, result);

        testString = "fr_FR@Europe";
        parser = new JsonFactory().createParser(testString);

        result = localeDeserializer.deserialize(parser, ctxt);

        expected = new Locale("fr", "FR", "Europe");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setTypeResolverBuilder(new StandardTypeResolverBuilder());
        TypeDeserializer currencyDeserializer = createTestInstance(Currency.class);

        String testString = "USD";
        JsonParser parser = new JsonFactory().createParser(testString);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setCurrentRootValueContainer(new Object());

        Currency result = currencyDeserializer.deserialize(parser, ctxt);

        Currency expected = Currency.getInstance("USD");
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setTypeResolverBuilder(new StandardTypeResolverBuilder());
        TypeDeserializer invalidDeserializer = createTestInstance(String.class);

        String testString = "invalid";
        JsonParser parser = new JsonFactory().createParser(testString);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setCurrentRootValueContainer(new Object());

        invalidDeserializer.deserialize(parser, ctxt);

        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setTypeResolverBuilder(new StandardTypeResolverBuilder());
        TypeDeserializer nullDeserializer = createTestInstance(String.class);

        String testString = null;
        JsonParser parser = new JsonFactory().createParser(testString);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setCurrentRootValueContainer(new Object());

        String result = (String) nullDeserializer.deserialize(parser, ctxt);

        assertEquals(null, result);
    }

}