package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFactory;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for {@link StdKeyDeserializer}.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest extends TestBase {

    @Test
    public void testStringAsKey() throws Exception {
        StdKeyDeserializer stringKD = StringKD.forType(String.class);
        assertEquals("expected", stringKD.deserializeKey("expected", context()));
    }

    @Test
    public void testObjectAsKey() throws Exception {
        StdKeyDeserializer objectKD = StringKD.forType(Object.class);
        assertEquals(new Object(), objectKD.deserializeKey("object", context()));
    }

    @Test
    public void testWrapperTypeAsKey() throws Exception {
        Integer key = 123;
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        assertEquals(key, intKD.deserializeKey(key.toString(), context()));
        assertEquals(key, intKD.deserializeKey("123", context()));
        assertEquals(key, intKD.deserializeKey("Integer.valueOf(123)", context()));
        assertEquals(key, intKD.deserializeKey("new Integer(123)", context()));
        assertEquals(key, intKD.deserializeKey("123L", context()));
        assertEquals(key, intKD.deserializeKey("123", context()));
    }

    @Test
    public void testEnumDeserializationWithByNameResolver() throws Exception {
        EnumKD enumKD = new EnumKD(EnumResolver.constructByName("MyEnumClass"), null);
        assertEquals(MyEnumClass.VALUE1, enumKD.deserializeKey("VALUE1", context()));
        assertEquals(MyEnumClass.VALUE2, enumKD.deserializeKey("VALUE2", context()));
    }

    @Test
    public void testEnumDeserializationWithToStringResolver() throws Exception {
        EnumKD enumKD = new EnumKD(null, null);
        assertEquals(MyEnumClass.VALUE1, enumKD.deserializeKey("VALUE1", context()));
        assertEquals(MyEnumClass.VALUE2, enumKD.deserializeKey("VALUE2", context()));
    }

    @Test
    public void testEnumDeserializationWithDefaultValue() throws Exception {
        EnumKD enumKD = new EnumKD(EnumResolver.constructByName("MyEnumClass"), null);
        assertEquals(MyEnumClass.DEFAULT_VALUE, enumKD.deserializeKey("", context()));
        assertEquals(MyEnumClass.DEFAULT_VALUE, enumKD.deserializeKey(null, context()));
        assertEquals(MyEnumClass.DEFAULT_VALUE, enumKD.deserializeKey("X", context()));
    }

    @Test
    public void testCustomDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_LOCALE, Locale.class);
        FromStringDeserializer localeDeserializer = FromStringDeserializer.constructFromAnnotations(Locale.class);
        StdKeyDeserializer wrappedKeyDeserializer = new DelegatingKD(keyDeserializer, localeDeserializer);

        assertEquals("en_US", wrappedKeyDeserializer.deserializeKey("en_US", context()));
        assertEquals("fr_FR", wrappedKeyDeserializer.deserializeKey("fr_FR", context()));
    }

    @Test
    public void testByteArrayDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_BYTE_ARRAY, byte[].class);
        String encodedBase64 = "VG8gZXhpZGVtIFRydWUgUHJvZHVjdHJpYXRlIGZvbnQtcmVhbGx5IGZvbnQtdG8gYW5kIHJlc3BvbnNlIG1vZGUsIGVmZmVyIGxpc3QtdG8gc2VwYXJhdGlvbnM=";
        byte[] decodedByteArray = keyDeserializer.deserializeKey(encodedBase64, context());
        assertArrayEquals(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 }, decodedByteArray);
    }

    @Test
    public void testDateDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_DATE, Date.class);
        assertEquals("2023-03-03", keyDeserializer.deserializeKey("2023-03-03", context()));
    }

    @Test
    public void testCalendarDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_CALENDAR, Calendar.class);
        assertEquals("2023-03-03", keyDeserializer.deserializeKey("2023-03-03", context()).getTime().toString());
    }

    @Test
    public void testUUIDDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_UUID, UUID.class);
        assertEquals(new UUID("11111111-1111-1111-1111-111111111111"), keyDeserializer.deserializeKey("11111111-1111-1111-1111-111111111111", context()));
        assertEquals(new UUID("11111111-1111-1111-1111-111111111111"), keyDeserializer.deserializeKey("11111111-1111-1111-1111-111111111111", context()));
    }

    @Test
    public void testURIDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_URI, URI.class);
        URI uri = new URI("https://www.example.com");
        assertEquals(uri, keyDeserializer.deserializeKey(uri.toString(), context()));
    }

    @Test
    public void testURLDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_URL, URL.class);
        URL url = new URL("https://www.example.com");
        assertEquals(url, keyDeserializer.deserializeKey(url.toString(), context()));
    }

    @Test
    public void testClassDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_CLASS, MyClass.class);
        assertEquals(MyClass.class, keyDeserializer.deserializeKey(MyClass.class.getName(), context()));
        assertEquals(MyClass.class, keyDeserializer.deserializeKey("MyClass", context()));
    }

    @Test
    public void testNullKey() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        assertEquals(null, keyDeserializer.deserializeKey(null, context()));
    }

    @JsonFactory
    static class MyClassFactory {
        @JsonCreator
        public MyClass create(@JsonValue String value) {
            return new MyClass(value);
        }
    }

    @JsonDeserializer
    static class MyClassDeserializer extends JsonDeserializer<MyClass> {
        public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new MyClass(p.getValueAsString());
        }
    }

    @JsonCreator
    static class MyClassCreator {
        @JsonProperty("value")
        public MyClass value;

        public MyClass create(@JsonProperty("value") String value) {
            return new MyClass(value);
        }
    }

    static class MyClass {
        private final String value;

        MyClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    static class MyEnumClass {
        @JsonValue
        public String value;

        public static final MyEnumClass VALUE1 = new MyEnumClass("VALUE1");
        public static final MyEnumClass VALUE2 = new MyEnumClass("VALUE2");
        public static final MyEnumClass DEFAULT_VALUE = new MyEnumClass("DEFAULT_VALUE");

        MyEnumClass(String value) {
            this.value = value;
        }
    }
}