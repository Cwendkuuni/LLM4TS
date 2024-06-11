package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EnumSet;

import static org.junit.Assert.*;

/**
 * Test class for StdKeyDeserializer.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private DeserializationContext context;
    private StdKeyDeserializer stringKD;
    private StdKeyDeserializer booleanKD;
    private StdKeyDeserializer intKD;
    private StdKeyDeserializer longKD;
    private StdKeyDeserializer dateKD;
    private StdKeyDeserializer uriKD;
    private StdKeyDeserializer urlKD;
    private StdKeyDeserializer classKD;
    private StdKeyDeserializer enumKD;
    private FromStringDeserializer enumResolverMock;

    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(DeserializationContext.class);
        enumResolverMock = Mockito.mock(FromStringDeserializer.class);
        stringKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        booleanKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        intKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        longKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        dateKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        uriKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        urlKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        classKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
        enumKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, Enum.class, enumResolverMock);
    }

    @Test
    public void testDeserializeKeyString() throws Exception {
        Object key = stringKD.deserializeKey("key", context);
        assertEquals("key", key);
    }

    @Test
    public void testDeserializeKeyBoolean() throws Exception {
        Object key = booleanKD.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, key);

        Object key2 = booleanKD.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, key2);

        Object key3 = booleanKD.deserializeKey("invalid", context);
        assertNull(key3);
    }

    @Test
    public void testDeserializeKeyInt() throws Exception {
        Object key = intKD.deserializeKey("1", context);
        assertEquals(Integer.valueOf(1), key);

        Object key2 = intKD.deserializeKey("invalid", context);
        assertNull(key2);
    }

    @Test
    public void testDeserializeKeyLong() throws Exception {
        Object key = longKD.deserializeKey("100", context);
        assertEquals(Long.valueOf(100), key);

        Object key2 = longKD.deserializeKey("invalid", context);
        assertNull(key2);
    }

    @Test
    public void testDeserializeKeyDate() throws Exception {
        Object key = dateKD.deserializeKey("2022-01-01", context);
        assertEquals(new Date(1641376000000L), key);

        Object key2 = dateKD.deserializeKey("invalid", context);
        assertNull(key2);
    }

    @Test
    public void testDeserializeKeyURI() throws Exception {
        Object key = uriKD.deserializeKey("http://example.com", context);
        assertEquals(new URI("http://example.com"), key);

        Object key2 = uriKD.deserializeKey("invalid", context);
        assertNull(key2);
    }

    @Test
    public void testDeserializeKeyURL() throws Exception {
        Object key = urlKD.deserializeKey("http://example.com", context);
        assertEquals(new URL("http://example.com"), key);

        Object key2 = urlKD.deserializeKey("invalid", context);
        assertNull(key2);
    }

    @Test
    public void testDeserializeKeyClass() throws Exception {
        Object key = classKD.deserializeKey("java.lang.String", context);
        assertEquals(String.class, key);

        Object key2 = classKD.deserializeKey("java.util.HashMap", context);
        assertEquals(HashMap.class, key2);

        Object key3 = classKD.deserializeKey("invalid", context);
        assertNull(key3);
    }

    @Test
    public void testDeserializeKeyEnum() throws Exception {
        EnumSet<TestEnum> expectedEnumValues = EnumSet.allOf(TestEnum.class);
        Mockito.when(enumResolverMock.deserialize(ArgumentMatchers.anyString(), ArgumentMatchers.any(DeserializationContext.class)))
                .thenReturn(expectedEnumValues.stream().findFirst().get());

        Object key = enumKD.deserializeKey("TestEnum.VALUE_1", context);
        assertEquals(TestEnum.VALUE_1, key);

        Object key2 = enumKD.deserializeKey("TestEnum.VALUE_2", context);
        assertEquals(TestEnum.VALUE_2, key2);

        Object key3 = enumKD.deserializeKey("invalid", context);
        assertNull(key3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKeyInvalidType() throws Exception {
        Object key = new StdKeyDeserializer(-1, Object.class).deserializeKey("invalid", context);
        fail("Expected IllegalArgumentException");
    }
}