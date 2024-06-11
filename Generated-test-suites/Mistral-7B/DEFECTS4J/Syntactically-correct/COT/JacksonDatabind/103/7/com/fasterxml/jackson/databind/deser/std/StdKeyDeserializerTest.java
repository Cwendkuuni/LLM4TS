package com.example.test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;
    private StdKeyDeserializer stringKd;
    private StdKeyDeserializer booleanKd;
    private StdKeyDeserializer charKd;
    private StdKeyDeserializer intKd;
    private StdKeyDeserializer longKd;
    private StdKeyDeserializer floatKd;
    private StdKeyDeserializer doubleKd;
    private StdKeyDeserializer uriKd;
    private StdKeyDeserializer urlKd;
    private StdKeyDeserializer classKd;
    private StdKeyDeserializer dateKd;
    private StdKeyDeserializer calendarKd;
    private StdKeyDeserializer uuidKd;
    private StdKeyDeserializer localeKd;
    private StdKeyDeserializer currencyKd;
    private StdKeyDeserializer byteArrayKd;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        stringKd = StdKeyDeserializer.forType(String.class);
        booleanKd = StdKeyDeserializer.forType(Boolean.class);
        charKd = StdKeyDeserializer.forType(Character.class);
        intKd = StdKeyDeserializer.forType(Integer.class);
        longKd = StdKeyDeserializer.forType(Long.class);
        floatKd = StdKeyDeserializer.forType(Float.class);
        doubleKd = StdKeyDeserializer.forType(Double.class);
        uriKd = StdKeyDeserializer.forType(URI.class);
        urlKd = StdKeyDeserializer.forType(URL.class);
        classKd = StdKeyDeserializer.forType(Class.class);
        dateKd = StdKeyDeserializer.forType(java.util.Date.class);
        calendarKd = StdKeyDeserializer.forType(java.util.Calendar.class);
        uuidKd = StdKeyDeserializer.forType(java.util.UUID.class);
        localeKd = StdKeyDeserializer.forType(java.util.Locale.class);
        currencyKd = StdKeyDeserializer.forType(java.util.Currency.class);
        byteArrayKd = StdKeyDeserializer.forType(byte[].class);
    }

    @Test
    public void testDeserializeKeyString() throws IOException {
        Assert.assertEquals("test", stringKd.deserializeKey("test", new DeserializationContext(mapper, null)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyNull() throws IOException {
        stringKd.deserializeKey(null, new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyBooleanTrue() throws IOException {
        Assert.assertEquals(Boolean.TRUE, booleanKd.deserializeKey("true", new DeserializationContext(mapper, null)));
    }

    @Test
    public void testDeserializeKeyBooleanFalse() throws IOException {
        Assert.assertEquals(Boolean.FALSE, booleanKd.deserializeKey("false", new DeserializationContext(mapper, null)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyInvalidBoolean() throws IOException {
        booleanKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyChar() throws IOException {
        Assert.assertEquals('a', charKd.deserializeKey("a", new DeserializationContext(mapper, null)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyInvalidCharLength() throws IOException {
        charKd.deserializeKey("ab", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyInt() throws IOException {
        Assert.assertEquals(1, intKd.deserializeKey("1", new DeserializationContext(mapper, null)));
    }

    @Test
    public void testDeserializeKeyLong() throws IOException {
        Assert.assertEquals(1L, longKd.deserializeKey("1", new DeserializationContext(mapper, null)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyInvalidLong() throws IOException {
        longKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyFloat() throws IOException {
        Assert.assertEquals(1.0f, floatKd.deserializeKey("1", new DeserializationContext(mapper, null)), 0.0f);
    }

    @Test
    public void testDeserializeKeyDouble() throws IOException {
        Assert.assertEquals(1.0, doubleKd.deserializeKey("1.0", new DeserializationContext(mapper, null)), 0.0);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyInvalidFloat() throws IOException {
        doubleKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyUri() throws IOException {
        URI uri = new URI("http://example.com");
        Assert.assertEquals(uri, uriKd.deserializeKey("http://example.com", new DeserializationContext(mapper, null)));
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeKeyInvalidUri() throws IOException {
        uriKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyUrl() throws IOException {
        URL url = new URL("http://example.com");
        Assert.assertEquals(url, urlKd.deserializeKey("http://example.com", new DeserializationContext(mapper, null)));
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeKeyInvalidUrl() throws IOException {
        urlKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyClass() throws IOException, ClassNotFoundException {
        Assert.assertEquals(String.class, classKd.deserializeKey(String.class.getName(), new DeserializationContext(mapper, null)));
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeKeyInvalidClass() throws IOException {
        classKd.deserializeKey("InvalidClass", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyDate() throws IOException {
        Assert.assertEquals(new Date(), dateKd.deserializeKey("2022-01-01", new DeserializationContext(mapper, null)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyInvalidDateFormat() throws IOException {
        dateKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyCalendar() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        Assert.assertEquals(calendar, calendarKd.deserializeKey("2022-01-01", new DeserializationContext(mapper, null)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyInvalidCalendarFormat() throws IOException {
        calendarKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyUuid() throws IOException {
        UUID uuid = UUID.randomUUID();
        Assert.assertEquals(uuid, uuidKd.deserializeKey(uuid.toString(), new DeserializationContext(mapper, null)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyInvalidUuidFormat() throws IOException {
        uuidKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyLocale() throws IOException {
        Locale locale = new Locale("en", "US");
        Assert.assertEquals(locale, localeKd.deserializeKey("en_US", new DeserializationContext(mapper, null)));
    }

    @Test(expected = IOException.class)
    public void testDeserializeKeyInvalidLocaleFormat() throws IOException {
        localeKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyCurrency() throws IOException {
        Currency currency = Currency.getInstance("USD");
        Assert.assertEquals(currency, currencyKd.deserializeKey("USD", new DeserializationContext(mapper, null)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyInvalidCurrencyFormat() throws IOException {
        currencyKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyByteArrayBase64() throws IOException {
        byte[] byteArray = {1, 2, 3};
        Assert.assertArrayEquals(byteArray, byteArrayKd.deserializeKey("SGVsbG8sIFdvcmxkIQ==", new DeserializationContext(mapper, null)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyInvalidByteArrayBase64Format() throws IOException {
        byteArrayKd.deserializeKey("invalid", new DeserializationContext(mapper, null));
    }

    @Test
    public void testDeserializeKeyWithNullValueProvider() throws IOException {
        NullValueProvider nvp = new JsonDeserializer.NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException, IOException {
                return null;
            }
        };
        Properties properties = new Properties();
        properties.put("nullValueProvider", nvp);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setDefaultPropertyValueHandler(PropertyName.construct("nullValueProvider", String.class, null));
        StdKeyDeserializer stringKdWithNullValueProvider = new StdKeyDeserializer(-1, String.class);
        stringKdWithNullValueProvider.setNullValueProvider(nvp);
        Object result = stringKdWithNullValueProvider.deserializeKey("null", new DeserializationContext(mapper, properties));
        Assert.assertNull(result);
    }
}