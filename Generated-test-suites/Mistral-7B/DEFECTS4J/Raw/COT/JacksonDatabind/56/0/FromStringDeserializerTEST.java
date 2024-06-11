package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private FromStringDeserializer<URI> uriDeserializer;
    @InjectMocks
    private FromStringDeserializer<URL> urlDeserializer;
    @InjectMocks
    private FromStringDeserializer<Locale> localeDeserializer;
    @InjectMocks
    private FromStringDeserializer<TimeZone> timeZoneDeserializer;
    @InjectMocks
    private FromStringDeserializer<Currency> currencyDeserializer;

    @Test
    public void testDeserializeValidURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"http://example.com/\"");

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        URI result = uriDeserializer.deserialize(parser, ctxt);
        assertEquals("http://example.com/", result.toString());
    }

    @Test
    public void testDeserializeInvalidURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"http://example.com:8080/\""); // invalid port number

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        try {
            uriDeserializer.deserialize(parser, ctxt);
            fail("Expected InvalidFormatException");
        } catch (InvalidFormatException e) {
            assertEquals("Invalid URI: The port must be a non-negative integer.", e.getMessage());
        }
    }

    @Test
    public void testDeserializeValidURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"http://example.com/\"");

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        URL result = urlDeserializer.deserialize(parser, ctxt);
        assertEquals("http://example.com/", result.toString());
    }

    @Test
    public void testDeserializeInvalidURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"http://example.com:8080/\""); // invalid port number

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        try {
            urlDeserializer.deserialize(parser, ctxt);
            fail("Expected InvalidFormatException");
        } catch (InvalidFormatException e) {
            assertEquals("Invalid URL: Invalid port number: 8080", e.getMessage());
        }
    }

    @Test
    public void testDeserializeValidLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"en_US\"");

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Locale result = localeDeserializer.deserialize(parser, ctxt);
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testDeserializeInvalidLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"en_US_ invalid_variant\""); // invalid variant

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        try {
            localeDeserializer.deserialize(parser, ctxt);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid Locale key: 'en_US_ invalid_variant'", e.getMessage());
        }
    }

    @Test
    public void testDeserializeValidTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"America/Los_Angeles\"");

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        TimeZone result = timeZoneDeserializer.deserialize(parser, ctxt);
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeInvalidTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"America/Los_Angeles_invalid_name\""); // invalid name

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        try {
            timeZoneDeserializer.deserialize(parser, ctxt);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid TimeZone id: 'America/Los_Angeles_invalid_name'", e.getMessage());
        }
    }

    @Test
    public void testDeserializeValidCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"USD\"");

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Currency result = currencyDeserializer.deserialize(parser, ctxt);
        assertEquals("USD", result.getCurrencyCode());
    }

    @Test
    public void testDeserializeInvalidCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("\"USD_INVALID\""); // invalid code

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        try {
            currencyDeserializer.deserialize(parser, ctxt);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid Currency code: 'USD_INVALID'", e.getMessage());
        }
    }
}