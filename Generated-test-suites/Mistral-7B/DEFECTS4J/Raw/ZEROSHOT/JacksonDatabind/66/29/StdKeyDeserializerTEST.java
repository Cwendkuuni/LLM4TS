package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.*;

/**
 * Unit tests for {@link StdKeyDeserializer}.
 */
public class StdKeyDeserializerTest {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    @Test
    public void testBoolean() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);

        String key = "true";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Boolean.TRUE, deserialized);

        key = "false";
        deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Boolean.FALSE, deserialized);

        key = "invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testByte() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);

        String key = "123";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals((Byte)Byte.parseByte(key), deserialized);

        key = "-128";
        deserialized = serializer.deserializeKey(key, createContext());
        assertEquals((Byte)Byte.MIN_VALUE, deserialized);

        key = "256";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testShort() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);

        String key = "123";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals((Short)Short.parseShort(key), deserialized);

        key = "-128";
        deserialized = serializer.deserializeKey(key, createContext());
        assertEquals((Short)Short.MIN_VALUE, deserialized);

        key = "32768";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testChar() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);

        String key = "A";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals((Character)key.charAt(0), deserialized);

        key = "invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testInt() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        String key = "123";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Integer.parseInt(key), deserialized);

        key = "-128";
        deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Integer.MIN_VALUE, deserialized);

        key = "Integer.MAX_VALUE";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testLong() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);

        String key = "1234567890123456789";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Long.parseLong(key), deserialized);

        key = "-1234567890123456789";
        deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Long.MIN_VALUE, deserialized);

        key = "Long.MAX_VALUE";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testFloat() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);

        String key = "123.45";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Float.parseFloat(key), deserialized);

        key = "Invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testDouble() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);

        String key = "123.45";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Double.parseDouble(key), deserialized);

        key = "Invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testLocale() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);

        String key = "en_US";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Locale.forLanguageTag(key), deserialized);

        key = "Invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);

        // Test handling of deserialization failure
        key = "InvalidLocale";
        deserialized = serializer.deserializeKey(key, new ContextWrapper(createContext()) {
            @Override
            public DeserializationContext delegate() {
                return super.delegate();
            }

            @Override
            public NullValueProvider getNullValueProvider() {
                return null;
            }
        });
        assertNull(deserialized);
    }

    @Test
    public void testURL() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);

        String key = "http://example.com";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(new URL(key), deserialized);

        key = "Invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testURI() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);

        String key = "http://example.com";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(new URI(key), deserialized);

        key = "Invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testClass() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);

        String key = "java.lang.String";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(Class.forName(key), deserialized);

        key = "Invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);
    }

    @Test
    public void testEnum() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);
        EnumResolver enumResolver = new EnumResolver(MyEnum.class);

        String key = "MY_ENUM_VALUE1";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(MyEnum.MY_ENUM_VALUE1, deserialized);

        key = "Invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);

        // Test handling of deserialization failure
        MyEnum[] values = MyEnum.values();
        String invalidValue = values[0].name() + "_";
        deserialized = serializer.deserializeKey(invalidValue, createContext());
        assertNull(deserialized);

        // Test Enum with custom deserializer
        MyEnumCustomDeserializer customDeserializer = new MyEnumCustomDeserializer();
        MyEnumCustomKeyDeserializer customKeyDeserializer = new MyEnumCustomKeyDeserializer(customDeserializer);
        serializer = StdKeyDeserializer.forType(MyEnum.class);
        when(customDeserializer.deserialize(anyString(), any(DeserializationContext.class)))
                .thenReturn(MyEnum.MY_ENUM_VALUE2);
        serializer = new StdKeyDeserializer(TYPE_ENUM, MyEnum.class, customKeyDeserializer);
        key = "CustomValue";
        deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(MyEnum.MY_ENUM_VALUE2, deserialized);
    }

    @Test
    public void testEnumSet() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(EnumSet.class);
        EnumSet<MyEnum> expectedValue = EnumSet.allOf(MyEnum.class);

        String key = "ALL_VALUES";
        Object deserialized = serializer.deserializeKey(key, createContext());
        assertEquals(expectedValue, deserialized);

        key = "Invalid";
        deserialized = serializer.deserializeKey(key, createContext());
        assertNull(deserialized);

        // Test EnumSet with custom deserializer
        MyEnumCustomDeserializer customDeserializer = new MyEnumCustomDeserializer();
        MyEnumCustomKeyDeserializer customKeyDeserializer = new MyEnumCustomKeyDeserializer(customDeserializer);
        serializer = StdKeyDeserializer.forType(EnumSet.class);
        when(customDeserializer.deserialize(anyString(), any(DeserializationContext.class)))
                .thenReturn(MyEnum.MY_ENUM_VALUE2);
        serializer = new StdKeyDeserializer(TYPE_ENUMSET, EnumSet.class, customKeyDeserializer);
        key = "CustomValue";
        deserialized = serializer.deserializeKey(key, createContext());
        EnumSet<MyEnum> deserializedSet = (EnumSet<MyEnum>)deserialized;
        assertEquals(EnumSet.of(MyEnum.MY_ENUM_VALUE2), deserializedSet);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private static class MyEnum {
        public static final MyEnum MY_ENUM_VALUE1 = new MyEnum("VALUE1");
        public static final MyEnum MY_ENUM_VALUE2 = new MyEnum("VALUE2");

        private final String name;

        MyEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @JsonDeserialize(using = MyEnumDeserializer.class)
        public static MyEnum valueOf(String value) {
            for (MyEnum e : MyEnum.values()) {
                if (e.name.equals(value)) {
                    return e;
                }
            }
            throw new IllegalArgumentException("No enum constant " + MyEnum.class.getName() + "." + value);
        }

        public static EnumSet<MyEnum> allOf() {
            return EnumSet.allOf(MyEnum.class);
        }

        public static EnumSet<MyEnum> valueOf(PropertyName[] propertyNameValues) {
            EnumSet<MyEnum> result = EnumSet.noneOf(MyEnum.class);
            for (PropertyName name : propertyNameValues) {
                result.add(valueOf(name.getName()));
            }
            return result;
        }

        @JsonValue
        public String toString() {
            return name;
        }
    }

    @JsonDeserialize(using = MyEnumDeserializer.class)
    private static class MyEnumDeserializer extends StdDeserializer<MyEnum> {
        public MyEnumDeserializer() {
            this(null);
        }

        public MyEnumDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public MyEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String name = p.getValueAsString();
            return MyEnum.valueOf(name);
        }
    }

    @JsonDeserialize(using = MyEnumCustomDeserializer.class)
    private static class MyEnumCustomDeserializer extends JsonDeserializer<MyEnum> {
        @Override
        public MyEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return MyEnum.MY_ENUM_VALUE2;
        }
    }

    @JsonDeserialize(using = MyEnumCustomKeyDeserializer.class)
    private static class MyEnumCustomKeyDeserializer extends KeyDeserializer {
        private final JsonDeserializer<?> _delegate;

        public MyEnumCustomKeyDeserializer(JsonDeserializer<?> delegate) {
            _delegate = delegate;
        }

        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            try {
                return _delegate.deserializeKey(key, ctxt);
            } catch (JsonProcessingException e) {
                throw e.unwrapRootCause(IOException.class);
            }
        }
    }

    private DeserializationContext createContext() {
        return new BaseContextWrapper(mapper.getFactory().createContextHandler());
    }

    private static class BaseContextWrapper
            extends BaseHandlerDecorator<DeserializationContext> {

        public BaseContextWrapper(DeserializationContext ctx) {
            super(ctx);
        }

        @Override
        public void handleWeirdKey(Class<?> keyType, String key, String msg,
                Object valueToReturn) throws IOException {
            throw new IOException(String.format(msg, key));
        }

        @Override
        public void handleBadObjectValue(Class<?> valueType, Object value, String msg)
                throws IOException {
            throw new IOException(msg);
        }
    }

    private static interface ContextWrapper<T extends DeserializationContext> extends DeserializationContext {
        T delegate();
    }
}