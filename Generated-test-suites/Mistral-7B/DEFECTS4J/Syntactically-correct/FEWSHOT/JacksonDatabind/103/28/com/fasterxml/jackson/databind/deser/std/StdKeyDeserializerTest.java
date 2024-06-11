package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFactory;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Tests for {@link StdKeyDeserializer} class.
 */
public class StdKeyDeserializerTest {

    @Test
    public void testIntegerKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(1, deserializer.deserializeKey("1", mapper.getDeserializationContext()).intValue());
    }

    @Test
    public void testStringKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        assertEquals("foo", deserializer.deserializeKey("foo", mapper.getDeserializationContext()));
    }

    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", mapper.getDeserializationContext()));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", mapper.getDeserializationContext()));
    }

    @Test
    public void testEnumKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        enum TestEnum {
            FOO, BAR, BAZ
        }
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        TestEnum expected = TestEnum.FOO;
        AnnotatedClass ac = mapper.getAnnotationIntrospector().findAnnotation(TestEnum.class.getName());
        Constructor<TestEnum> constructor = ac.getAnnotatedConstructors().next();
        assertEquals(expected, deserializer.deserializeKey("FOO", mapper.getDeserializationContext()));
        assertEquals(expected, deserializer.deserializeKey(constructor.getName(), mapper.getDeserializationContext()));
    }

    @Test
    public void testDateKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Date expected = new Date();
        assertEquals(expected, deserializer.deserializeKey(expected.toString(), mapper.getDeserializationContext()));
    }

    @Test
    public void testLocaleKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Locale expected = Locale.US;
        assertEquals(expected, deserializer.deserializeKey(expected.toString(), mapper.getDeserializationContext()));
    }

    @Test
    public void testURIKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        URI expected = new URI("https://www.example.com");
        assertEquals(expected, deserializer.deserializeKey(expected.toString(), mapper.getDeserializationContext()));
    }

    @Test
    public void testURLKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        URL expected = new URL("https://www.example.com");
        assertEquals(expected, deserializer.deserializeKey(expected.toString(), mapper.getDeserializationContext()));
    }

    @Test
    public void testClassKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
        assertEquals(String[].class, deserializer.deserializeKey("[Ljava/lang/String;", mapper.getDeserializationContext()));
    }

    @Test
    public void testEnumFactoryKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        enum TestEnum {
            FOO, BAR, BAZ;

            @JsonCreator
            public static TestEnum fromString(String value) {
                return TestEnum.valueOf(value);
            }
        }
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        TestEnum expected = TestEnum.FOO;
        assertEquals(expected, deserializer.deserializeKey("FOO", mapper.getDeserializationContext()));
    }

    @Test
    public void testEnumCreatorKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        enum TestEnum {
            FOO, BAR, BAZ;

            @JsonFactory
            public static TestEnum create(Map<String, Object> values) {
                return TestEnum.valueOf(values.get("name").toString());
            }
        }
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        TestEnum expected = TestEnum.FOO;
        JSONPObject jsonpObject = new JSONPObject("(function() { return {" +
                "\"name\": \"FOO\" }; })()");
        assertEquals(expected, deserializer.deserializeKey(jsonpObject.toString(), mapper.getDeserializationContext()));
    }

    @Test
    public void testCustomConverterKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        enum TestEnum {
            FOO, BAR, BAZ;

            @JsonValue
            public int toInt() {
                return ordinal();
            }
        }
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        TestEnum expected = TestEnum.FOO;
        StdConverter<TestEnum, Integer> converter = new StdConverter<TestEnum, Integer>() {
            @Override
            public TestEnum fromInt(int value) {
                return TestEnum.values()[value];
            }
        };
        AnnotatedClass ac = mapper.getAnnotationIntrospector().findAnnotation(TestEnum.class.getName());
        AnnotatedMethod am = ac.findAnnotatedMembers(JsonValue.class).next();
        AnnotatedMember amm = am.getAnnotatedMember();
        Converter<TestEnum, Integer> registeredConverter = mapper.getConfig().findConverter(amm.getType(), TestEnum.class);
        if (registeredConverter != null) {
            mapper.getConfig().removeAnnotation(amm.getMemberName(), JsonValue.class);
            mapper.getConfig().removeAnnotation(TestEnum.class.getName(), JsonValue.class);
        }
        mapper.getConfig().addValueHandler(TestEnum.class, new StdValueProvider.StdValueProviderMethod() {
            @Override
            public Object getStdValueProvider(DeserializationContext ctxt) throws IOException {
                return converter;
            }
        });
        assertEquals(expected, deserializer.deserializeKey("0", mapper.getDeserializationContext()));
    }

    @Test
    public void testCustomDeserializationKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        enum TestEnum {
            FOO, BAR, BAZ;

            @JsonValue
            public int toInt() {
                return ordinal();
            }
        }
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        TestEnum expected = TestEnum.FOO;
        class TestEnumDeserializer extends JsonDeserializer<TestEnum> {
            @Override
            public TestEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return TestEnum.values()[p.getValueAsInt()];
            }
        }
        AnnotatedClass ac = mapper.getAnnotationIntrospector().findAnnotation(TestEnum.class.getName());
        AnnotatedMethod am = ac.findAnnotatedMembers(JsonValue.class).next();
        AnnotatedMember amm = am.getAnnotatedMember();
        JsonDeserializer<?> registeredDeserializer = mapper.getConfig().findValueDeserializer(amm.getType(), TestEnum.class);
        if (registeredDeserializer != null) {
            mapper.getConfig().removeAnnotation(amm.getMemberName(), JsonValue.class);
            mapper.getConfig().removeAnnotation(TestEnum.class.getName(), JsonValue.class);
        }
        mapper.getConfig().addDeserializer(TestEnum.class, new TestEnumDeserializer());
        assertEquals(expected, deserializer.deserializeKey("0", mapper.getDeserializationContext()));
    }

    @Test
    public void testUnknownTypeKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Enum[].class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("FOO", mapper.getDeserializationContext()));
    }
}