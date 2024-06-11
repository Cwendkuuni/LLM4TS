package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;
    private StdKeyDeserializer stringKD;
    private StdKeyDeserializer intKD;
    private StdKeyDeserializer longKD;
    private StdKeyDeserializer booleanKD;
    private StdKeyDeserializer dateKD;
    private StdKeyDeserializer uriKD;
    private StdKeyDeserializer urlKD;
    private StdKeyDeserializer uuidKD;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        stringKD = StdKeyDeserializer.forType(String.class);
        intKD = StdKeyDeserializer.forType(int.class);
        longKD = StdKeyDeserializer.forType(long.class);
        booleanKD = StdKeyDeserializer.forType(boolean.class);
        dateKD = StdKeyDeserializer.forType(Date.class);
        uriKD = StdKeyDeserializer.forType(URI.class);
        urlKD = StdKeyDeserializer.forType(URL.class);
        uuidKD = StdKeyDeserializer.forType(UUID.class);
    }

    @Test
    public void testStringKeyDeserializer() throws IOException {
        String key = "key";
        Object deserialized = stringKD.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(key, deserialized);
    }

    @Test
    public void testIntKeyDeserializer() throws IOException {
        String key = "42";
        Object deserialized = intKD.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Integer.valueOf(42), deserialized);

        String invalidKey = "foo";
        Object result = intKD.deserializeKey(invalidKey, mapper.getDeserializationContext());
        assertEquals(mapper.handleWeirdKey(int.class, invalidKey, "not a valid representation"), result);
    }

    @Test
    public void testLongKeyDeserializer() throws IOException {
        String key = "42L";
        Object deserialized = longKD.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Long.valueOf(42L), deserialized);

        String invalidKey = "foo";
        Object result = longKD.deserializeKey(invalidKey, mapper.getDeserializationContext());
        assertEquals(mapper.handleWeirdKey(long.class, invalidKey, "not a valid representation"), result);
    }

    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        String keyTrue = "true";
        Object deserialized = booleanKD.deserializeKey(keyTrue, mapper.getDeserializationContext());
        assertTrue((Boolean) deserialized);

        String keyFalse = "false";
        Object deserializedFalse = booleanKD.deserializeKey(keyFalse, mapper.getDeserializationContext());
        assertFalse((Boolean) deserializedFalse);

        String invalidKey = "invalid";
        Object result = booleanKD.deserializeKey(invalidKey, mapper.getDeserializationContext());
        assertEquals(mapper.handleWeirdKey(boolean.class, invalidKey, "not a valid representation"), result);
    }

    @Test
    public void testDateKeyDeserializer() throws IOException {
        String key = "2022-12-31";
        Object deserialized = dateKD.deserializeKey(key, mapper.getDeserializationContext());
        assertNotNull(deserialized);
        assertEquals(new Date(2022, 11, 30), deserialized); // Note: DeserializationContext uses GregorianCalendar under the hood

        String invalidKey = "invalid";
        Object result = dateKD.deserializeKey(invalidKey, mapper.getDeserializationContext());
        assertEquals(mapper.handleWeirdKey(Date.class, invalidKey, "not a valid representation"), result);
    }

    @Test
    public void testURIKeyDeserializer() throws IOException {
        String key = "http://example.com";
        Object deserialized = uriKD.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(new URI(key), deserialized);

        String invalidKey = "invalid";
        Object result = uriKD.deserializeKey(invalidKey, mapper.getDeserializationContext());
        assertEquals(mapper.handleWeirdKey(URI.class, invalidKey, "not a valid representation"), result);
    }

    @Test
    public void testURLKeyDeserializer() throws IOException {
        String key = "http://example.com";
        Object deserialized = urlKD.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(new URL(key), deserialized);

        String invalidKey = "invalid";
        Object result = urlKD.deserializeKey(invalidKey, mapper.getDeserializationContext());
        assertEquals(mapper.handleWeirdKey(URL.class, invalidKey, "not a valid representation"), result);
    }

    @Test
    public void testUUIDKeyDeserializer() throws IOException {
        String key = "00000000-0000-0000-0000-000000000000";
        Object deserialized = uuidKD.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(UUID.fromString(key), deserialized);

        String invalidKey = "invalid";
        Object result = uuidKD.deserializeKey(invalidKey, mapper.getDeserializationContext());
        assertEquals(mapper.handleWeirdKey(UUID.class, invalidKey, "not a valid representation"), result);
    }

    @Test
    public void testEnumKeyDeserializer() throws IOException {
        AnnotatedClass ac = mapper.getTypeFactory().constructType(MyEnum.class);
        AnnotatedMethod factory = ac.findMethod("valueOf", TextNode.class);
        StdKeyDeserializer enumKD = new StdKeyDeserializer.EnumKD(new EnumResolver(MyEnum.class, factory), null);

        String key = "FOO";
        Object deserialized = enumKD.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(MyEnum.FOO, deserialized);

        String invalidKey = "invalid";
        Object result = enumKD.deserializeKey(invalidKey, mapper.getDeserializationContext());
        assertEquals(mapper.handleWeirdKey(MyEnum.class, invalidKey, "not one of values excepted for Enum class: %s", Arrays.asList("FOO", "BAR")), result);
    }

    @Test
    public void testCustomKeyDeserializer() throws IOException {
        class CustomClass {
            private final String name;

            CustomClass(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        Constructor<CustomClass> constructor = CustomClass.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        StdKeyDeserializer stringCtorKD = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);

        String key = "value";
        Object deserialized = stringCtorKD.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(new CustomClass(key), deserialized);
    }
}