package com.example.junit; // Replace with your test package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringKD;
import com.fasterxml.jackson.databind.node.TextNode;

import junit.framework.Assert;

@RunWith(org.mockito.MockitoJUnitRunner.class)
public class StdKeyDeserializerTest {

    @Test
    public void testStdKeyDeserializer_ForType() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Assert.assertEquals(StringKD.class, keyDeserializer.getClass());
    }

    @Test
    public void testStdKeyDeserializer_DeserializeKey() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        int expectedInt = 1;
        String key = Integer.toString(expectedInt);
        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(expectedInt, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdKeyDeserializer_DeserializeKey_InvalidBooleanKey() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        String key = "not_a_boolean";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testStdKeyDeserializer_DeserializeKey_EnumClass() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(Locale.class);

        Locale expectedLocale = Locale.US;
        String key = Locale.US.toString();
        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(expectedLocale, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdKeyDeserializer_DeserializeKey_InvalidEnumKey() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(Locale.class);

        String key = "invalid_locale";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testStdKeyDeserializer_DeserializeKey_NullValueProvider() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);

        context.putNullValueProvider(nullValueProvider);
        String key = null;
        Object result = keyDeserializer.deserializeKey(key, context);

        Mockito.verify(nullValueProvider).nullValue(context, Date.class);
        Assert.assertNull(result);
    }

    @Test
    public void testStdKeyDeserializer_DeserializeKey_Date() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);

        Date expectedDate = new Date();
        String key = TextNode.valueAsString(expectedDate);
        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(expectedDate, result);
    }

    @Test(expected = IOException.class)
    public void testStdKeyDeserializer_DeserializeKey_InvalidDateKey() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);

        String key = "invalid_date";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testStdKeyDeserializer_DeserializeKey_Calendar() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);

        Calendar expectedCalendar = Calendar.getInstance();
        String key = TextNode.valueAsString(expectedCalendar.getTime());
        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(expectedCalendar, result);
    }

    @Test(expected = IOException.class)
    public void testStdKeyDeserializer_DeserializeKey_InvalidCalendarKey() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);

        String key = "invalid_calendar";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testStdKeyDeserializer_DeserializeKey_UUID() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);

        UUID expectedUuid = UUID.randomUUID();
        String key = expectedUuid.toString();
        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(expectedUuid, result);
    }

    @Test(expected = IOException.class)
    public void testStdKeyDeserializer_DeserializeKey_InvalidUUIDKey() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);

        String key = "invalid_uuid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testStdKeyDeserializer_DeserializeKey_URI() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);

        URI expectedUri = new URI("http://example.com");
        String key = expectedUri.toString();
        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(expectedUri, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testStdKeyDeserializer_DeserializeKey_InvalidURICodeKey() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);

        String key = "invalid_uri_code";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testStdKeyDeserializer_DeserializeKey_URL() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);

        URL expectedUrl = new URL("http://example.com");
        String key = expectedUrl.toString();
        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(expectedUrl, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testStdKeyDeserializer_DeserializeKey_InvalidURLKey() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);

        String key = "invalid_url_code";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testStdKeyDeserializer_DeserializeKey_Class() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);

        String className = String.class.getName();
        Object result = keyDeserializer.deserializeKey(className, context);

        Assert.assertEquals(String.class, result);
    }

    @Test(expected = IOException.class)
    public void testStdKeyDeserializer_DeserializeKey_InvalidClassKey() throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);

        String key = "invalid_class_name";
        keyDeserializer.deserializeKey(key, context);
    }
}