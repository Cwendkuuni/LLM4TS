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
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
    @InjectMocks
    private FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
    @InjectMocks
    private FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
    @InjectMocks
    private FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeSuccessURI() throws IOException {
        String jsonString = "\"http://example.com/path/to/file\"";
        JsonParser jp = mapper.getFactory().createParser(jsonString);

        URI result = uriDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
        assertEquals("http://example.com/path/to/file", result.toURI().toString());
    }

    @Test
    public void testDeserializeSuccessLocale() throws IOException {
        String jsonString = "\"en_US\"";
        JsonParser jp = mapper.getFactory().createParser(jsonString);

        Locale result = localeDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeSuccessTimeZone() throws IOException {
        String jsonString = "\"America/Los_Angeles\"";
        JsonParser jp = mapper.getFactory().createParser(jsonString);

        TimeZone result = timeZoneDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeSuccessCurrency() throws IOException {
        String jsonString = "\"USD\"";
        JsonParser jp = mapper.getFactory().createParser(jsonString);

        Currency result = currencyDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeFailureURIInvalidFormat() throws IOException {
        String jsonString = "\"invalidURI\"";
        JsonParser jp = mapper.getFactory().createParser(jsonString);

        uriDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeFailureLocaleInvalidFormat() throws IOException {
        String jsonString = "\"invalidLocale\"";
        JsonParser jp = mapper.getFactory().createParser(jsonString);

        localeDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeFailureTimeZoneInvalidFormat() throws IOException {
        String jsonString = "\"invalidTimeZone\"";
        JsonParser jp = mapper.getFactory().createParser(jsonString);

        timeZoneDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeFailureCurrencyInvalidFormat() throws IOException {
        String jsonString = "\"invalidCurrency\"";
        JsonParser jp = mapper.getFactory().createParser(jsonString);

        currencyDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testEmptyString() throws IOException {
        String jsonString = "";
        JsonParser jp = mapper.getFactory().createParser(jsonString);

        URI resultURI = uriDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
        Locale resultLocale = localeDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
        TimeZone resultTimeZone = timeZoneDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));
        Currency resultCurrency = currencyDeserializer.deserialize(jp, Mockito.mock(DeserializationContext.class));

        assertNull(resultURI);
        assertNull(resultLocale);
        assertNull(resultTimeZone);
        assertNull(resultCurrency);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jp = objectMapper.getFactory().createParser("{\"class\":\"java.net.URI\",\"value\":\"http://example.com/path/to/file\"}");

        URI result = uriDeserializer.deserializeEmbedded(jp.readValueAsTree(), Mockito.mock(DeserializationContext.class));
        assertEquals("http://example.com/path/to/file", result.toURI().toString());
    }
}