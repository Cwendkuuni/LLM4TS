package com.example.tests;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StdKeyDeserializerTest {

    private DeserializationContext context;
    private ObjectMapper objectMapper;
    private StdKeyDeserializer stringDeserializer;
    private StdKeyDeserializer intDeserializer;
    private StdKeyDeserializer booleanDeserializer;
    private StdKeyDeserializer uriDeserializer;
    private StdKeyDeserializer urlDeserializer;
    private StdKeyDeserializer localeDeserializer;
    private StdKeyDeserializer calendarDeserializer;
    private StdKeyDeserializer dateDeserializer;
    private StdKeyDeserializer uuidDeserializer;
    private StdKeyDeserializer enumDeserializer;
    private StdKeyDeserializer classDeserializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        context = mock(DeserializationContext.class);

        stringDeserializer = new StdKeyDeserializer(-1, String.class);
        intDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        booleanDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        uriDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        urlDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        localeDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        calendarDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        dateDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        uuidDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        enumDeserializer = new StdKeyDeserializer(EnumResolver.class, EnumKD.class);
        classDeserializer = new StdKeyDeserializer(Class.class);
    }

    

    @Test
    public void testStringDeserializer() throws Exception {
        String key = "TestString";
        Object result = stringDeserializer.deserializeKey(key, context);
        assertEquals(key, result);
    }

    @Test
    public void testIntDeserializer() throws Exception {
        int key = 1;
        Object result = intDeserializer.deserializeKey("1", context);
        assertEquals(Integer.valueOf(key), result);
    }

    @Test
    public void testBooleanDeserializer() throws Exception {
        boolean key = true;
        Object result = booleanDeserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, result);

        result = booleanDeserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testURIDeserializer() throws Exception {
        URI key = new URI("http://example.com");
        Object result = uriDeserializer.deserializeKey(key.toString(), context);
        assertEquals(key, result);
    }

    @Test
    public void testURLDeserializer() throws Exception {
        URL key = new URL("http://example.com");
        Object result = urlDeserializer.deserializeKey(key.toString(), context);
        assertEquals(key, result);
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        Locale key = Locale.ENGLISH;
        Object result = localeDeserializer.deserializeKey(key.toString(), context);
        assertEquals(key, result);
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        Calendar key = Calendar.getInstance();
        Object result = calendarDeserializer.deserializeKey(key.toString(), context);
        assertEquals(key, result);
    }

    @Test
    public void testDateDeserializer() throws Exception {
        Date key = new Date();
        Object result = dateDeserializer.deserializeKey(key.toString(), context);
        assertEquals(key, result);
    }

    @Test
    public void testUUIDDeserializer() throws Exception {
        UUID key = UUID.randomUUID();
        Object result = uuidDeserializer.deserializeKey(key.toString(), context);
        assertEquals(key, result);
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        Enum e = Enum.VALUES[0];
        Object result = enumDeserializer.deserializeKey(e.name(), context);
        assertEquals(e, result);
    }

    @Test
    public void testClassDeserializer() throws Exception {
        Class<?> clazz = String.class;
        Object result = classDeserializer.deserializeKey(clazz.getName(), context);
        assertEquals(clazz, result);
    }

    @Test(expected = IOException.class)
    public void testInvalidDeserializer() throws Exception {
        String key = "InvalidKey";
        intDeserializer.deserializeKey(key, context);
        fail("Expected an IOException");
    }

    @Test(expected = JsonProcessingException.class)
    public void testUnexpectedTypeDeserializer() throws Exception {
        String key = "java.util.Date";
        classDeserializer.deserializeKey(key, context);
        fail("Expected a JsonProcessingException");
    }

    
}