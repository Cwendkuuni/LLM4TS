package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SuppressWarnings("static-access")
@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private FromStringDeserializer<Object> deserializer;
    private ObjectMapper objectMapper;

    public FromStringDeserializerTest() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize_StringToObject() throws IOException {
        String testString = "testString";
        String testMethodName = "testDeserialize_StringToObject";
        Object expectedResult = new Object();

        ObjectNode node = Mockito.mock(ObjectNode.class);
        Mockito.when(node.getValueAsText()).thenReturn(testString);
        JsonParser jsonParser = objectMapper.treeToValues(node, ObjectNode[].class)[0];

        Object result = deserializer.deserialize(jsonParser, Mockito.mock(DeserializationContext.class));

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserialize_EmptyStringToObject() throws IOException {
        String testMethodName = "testDeserialize_EmptyStringToObject";

        String emptyString = "";
        Object expectedResult = null;

        JsonParser jsonParser = objectMapper.getFactory().createParser(emptyString);

        Object result = deserializer.deserialize(jsonParser, Mockito.mock(DeserializationContext.class));

        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_InvalidJson() throws IOException {
        String testMethodName = "testDeserialize_InvalidJson";
        String invalidJson = "{\"invalidJson\":\"invalid\"}";

        JsonParser jsonParser = objectMapper.getFactory().createParser(invalidJson);

        deserializer.deserialize(jsonParser, Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        String testString = "https://www.example.com";
        String testMethodName = "testDeserialize_URL";
        URL expectedResult = new URL(testString);

        ObjectNode node = Mockito.mock(ObjectNode.class);
        Mockito.when(node.getValueAsText()).thenReturn(testString);
        JsonParser jsonParser = objectMapper.treeToValues(node, ObjectNode[].class)[0];

        URL result = (URL) deserializer.findDeserializer(URL.class).deserialize(jsonParser, Mockito.mock(DeserializationContext.class));

        assertEquals(expectedResult, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidURL() throws IOException {
        String testMethodName = "testDeserialize_InvalidURL";
        String invalidUrl = "invalidUrl";

        ObjectNode node = Mockito.mock(ObjectNode.class);
        Mockito.when(node.getValueAsText()).thenReturn(invalidUrl);
        JsonParser jsonParser = objectMapper.treeToValues(node, ObjectNode[].class)[0];

        deserializer.findDeserializer(URL.class).deserialize(jsonParser, Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        String testString = "USD";
        String testMethodName = "testDeserialize_Currency";
        Currency expectedResult = Currency.getInstance("USD");

        JsonParser jsonParser = objectMapper.getFactory().createParser(testString);

        Currency result = deserializer.findDeserializer(Currency.class).deserialize(jsonParser, Mockito.mock(DeserializationContext.class));

        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidCurrency() throws IOException {
        String testMethodName = "testDeserialize_InvalidCurrency";
        String invalidCurrency = "XCD";

        JsonParser jsonParser = objectMapper.getFactory().createParser(invalidCurrency);

        deserializer.findDeserializer(Currency.class).deserialize(jsonParser, Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        String testString = "en_US";
        String testMethodName = "testDeserialize_Locale";
        Locale expectedResult = new Locale("en", "US");

        JsonParser jsonParser = objectMapper.getFactory().createParser(testString);

        Locale result = deserializer.findDeserializer(Locale.class).deserialize(jsonParser, Mockito.mock(DeserializationContext.class));

        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidLocale() throws IOException {
        String testMethodName = "testDeserialize_InvalidLocale";
        String invalidLocale = "es_AR";

        JsonParser jsonParser = objectMapper.getFactory().createParser(invalidLocale);

        deserializer.findDeserializer(Locale.class).deserialize(jsonParser, Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        String testString = "America/New_York";
        String testMethodName = "testDeserialize_TimeZone";
        TimeZone expectedResult = TimeZone.getTimeZone("America/New_York");

        JsonParser jsonParser = objectMapper.getFactory().createParser(testString);

        TimeZone result = deserializer.findDeserializer(TimeZone.class).deserialize(jsonParser, Mockito.mock(DeserializationContext.class));

        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidTimeZone() throws IOException {
        String testMethodName = "testDeserialize_InvalidTimeZone";
        String invalidTimeZone = "Asia/Other";

        JsonParser jsonParser = objectMapper.getFactory().createParser(invalidTimeZone);

        deserializer.findDeserializer(TimeZone.class).deserialize(jsonParser, Mockito.mock(DeserializationContext.class));
    }
}