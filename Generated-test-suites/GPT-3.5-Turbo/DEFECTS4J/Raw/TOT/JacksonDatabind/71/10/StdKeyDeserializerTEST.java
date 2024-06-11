import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stringDeserializer;
    private StdKeyDeserializer integerDeserializer;
    private StdKeyDeserializer dateDeserializer;

    @Before
    public void setUp() {
        stringDeserializer = StdKeyDeserializer.forType(String.class);
        integerDeserializer = StdKeyDeserializer.forType(Integer.class);
        dateDeserializer = StdKeyDeserializer.forType(Date.class);
    }

    @Test
    public void testStringDeserializer_typical() throws Exception {
        DeserializationContext context = null;
        String key = "key";
        Object result = stringDeserializer.deserializeKey(key, context);
        assertEquals(key, result);
    }

    @Test
    public void testStringDeserializer_edgeCase() throws Exception {
        DeserializationContext context = null;
        String key = "";
        Object result = stringDeserializer.deserializeKey(key, context);
        assertEquals(key, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testStringDeserializer_error() throws Exception {
        DeserializationContext context = null;
        String key = null;
        stringDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testIntegerDeserializer_typical() throws Exception {
        DeserializationContext context = null;
        String key = "123";
        Object result = integerDeserializer.deserializeKey(key, context);
        assertEquals(123, result);
    }

    @Test
    public void testIntegerDeserializer_edgeCase() throws Exception {
        DeserializationContext context = null;
        String key = "-1";
        Object result = integerDeserializer.deserializeKey(key, context);
        assertEquals(-1, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testIntegerDeserializer_error() throws Exception {
        DeserializationContext context = null;
        String key = "ABC";
        integerDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testDateDeserializer_typical() throws Exception {
        DeserializationContext context = null;
        String key = "2022-01-01";
        Object result = dateDeserializer.deserializeKey(key, context);
        assertEquals(new Date(122, 0, 1), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateDeserializer_error() throws Exception {
        DeserializationContext context = null;
        String key = "2022/01/01";
        dateDeserializer.deserializeKey(key, context);
    }

}