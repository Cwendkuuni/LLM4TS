package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private Std urlDeserializer = new Std(URL.class, FromStringDeserializer.Std.STD_URL);
    @InjectMocks
    private Std uriDeserializer = new Std(URI.class, FromStringDeserializer.Std.STD_URI);
    @InjectMocks
    private Std classDeserializer = new Std(String.class, FromStringDeserializer.Std.STD_CLASS);
    @InjectMocks
    private Std currencyDeserializer = new Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
    @InjectMocks
    private Std localeDeserializer = new Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
    @InjectMocks
    private Std timeZoneDeserializer = new Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);

    private ObjectMapper mapper;
    private DeserializationContext context;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper(JsonMapperFactory.builder().build());
        context = Mockito.spy(mapper.getDeserializationContext());
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void test_deserializeURL() throws IOException, JsonProcessingException {
        String testUrl = "http://www.test.com";
        URL expected = new URL(testUrl);
        JsonParser parser = mapper.getFactory().createParser(testUrl);
        T result = urlDeserializer.deserialize(parser, context);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void test_deserializeURI() throws IOException, JsonProcessingException {
        String testUri = "http://www.test.com/path";
        URI expected = new URI(testUri);
        JsonParser parser = mapper.getFactory().createParser(testUri);
        T result = uriDeserializer.deserialize(parser, context);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void test_deserializeClass() throws IOException, JsonProcessingException {
        String testClassName = "java.lang.String";
        Class<?> expectedClass = Class.forName(testClassName);
        JsonParser parser = mapper.getFactory().createParser(testClassName);
        T result = classDeserializer.deserialize(parser, context);
        assertEquals(expectedClass.getName(), result.getClass().getName());
    }

    @Test
    public void test_deserializeCurrency() throws IOException, JsonProcessingException {
        String testCurrency = "USD";
        Currency expectedCurrency = Currency.getInstance(testCurrency);
        JsonParser parser = mapper.getFactory().createParser(testCurrency);
        T result = currencyDeserializer.deserialize(parser, context);
        assertEquals(expectedCurrency.getName(), result.toString());
    }

    @Test
    public void test_deserializeLocale() throws IOException, JsonProcessingException {
        String testLocaleString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        JsonParser parser = mapper.getFactory().createParser(testLocaleString);
        T result = localeDeserializer.deserialize(parser, context);
        assertEquals(expectedLocale.toString(), result.toString());
    }

    @Test
    public void test_deserializeTimeZone() throws IOException, JsonProcessingException {
        String testTimeZoneId = "America/New_York";
        TimeZone expectedTimeZone = TimeZone.getTimeZone(testTimeZoneId);
        JsonParser parser = mapper.getFactory().createParser(testTimeZoneId);
        T result = timeZoneDeserializer.deserialize(parser, context);
        assertEquals(expectedTimeZone.getID(), result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void test_deserializeInvalidType() throws IOException, JsonProcessingException {
        String testString = "java.util.List";
        JsonParser parser = mapper.getFactory().createParser(testString);
        classDeserializer.deserialize(parser, context);
    }

    @Test
    public void test_deserializeEmptyStringURL() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("");
        URL result = (URL) urlDeserializer.deserializeFromEmptyString();
        assertNull(result);
    }

    @Test
    public void test_deserializeEmptyStringURI() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("");
        URI result = (URI) uriDeserializer.deserializeFromEmptyString();
        assertEquals(URI.class.getName(), result.getClass().getName());
        assertEquals("", result.toString());
    }

    @Test
    public void test_deserializeEmptyStringClass() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("");
        String result = (String) classDeserializer.deserializeFromEmptyString();
        assertNull(result);
    }

    @Test
    public void test_deserializeEmptyStringCurrency() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("");
        Currency result = (Currency) currencyDeserializer.deserializeFromEmptyString();
        assertNull(result);
    }

    @Test
    public void test_deserializeEmptyStringLocale() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("");
        Locale result = (Locale) localeDeserializer.deserializeFromEmptyString();
        assertEquals(Locale.ROOT, result);
    }

    @Test
    public void test_deserializeEmptyStringTimeZone() throws IOException, JsonProcessingException {
        JsonParser parser = mapper.getFactory().createParser("");
        TimeZone result = (TimeZone) timeZoneDeserializer.deserializeFromEmptyString();
        assertEquals(TimeZone.getDefault(), result);
    }
}