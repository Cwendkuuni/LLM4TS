package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private FromStringDeserializer<URI> uriDeserializer;
    @InjectMocks
    private FromStringDeserializer<Locale> localeDeserializer;
    @InjectMocks
    private FromStringDeserializer<TimeZone> timeZoneDeserializer;
    @InjectMocks
    private FromStringDeserializer<Currency> currencyDeserializer;

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        String json = "\"http://example.com\"";
        JsonParser parser = mapper.getFactory().createParser(json);

        URI result = uriDeserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        assertEquals("http://example.com", result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeURIInvalid() throws IOException {
        String json = "\"http://example.com:8080\""; // port should not be deserialized
        JsonParser parser = mapper.getFactory().createParser(json);

        uriDeserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "\"fr_FR\"";
        JsonParser parser = mapper.getFactory().createParser(json);

        Locale result = localeDeserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        assertEquals("fr_FR", result.toString());
    }

    @Test
    public void testDeserializeLocaleInvalid() throws IOException {
        String json = "\"en_US_FOO\""; // unused third argument should not be deserialized
        JsonParser parser = mapper.getFactory().createParser(json);

        localeDeserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "\"Europe/Paris\"";
        JsonParser parser = mapper.getFactory().createParser(json);

        TimeZone result = timeZoneDeserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        assertEquals("Europe/Paris", result.getID());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTimeZoneInvalid() throws IOException {
        String json = "\"America/New_York:10:00\""; // time offset should not be deserialized
        JsonParser parser = mapper.getFactory().createParser(json);

        timeZoneDeserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "\"USD\"";
        JsonParser parser = mapper.getFactory().createParser(json);

        Currency result = currencyDeserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        assertEquals("USD", result.getCurrencyCode());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeCurrencyInvalid() throws IOException {
        String json = "\"EUR_123.45\""; // no ISO 4217 currency code
        JsonParser parser = mapper.getFactory().createParser(json);

        currencyDeserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
    }

}