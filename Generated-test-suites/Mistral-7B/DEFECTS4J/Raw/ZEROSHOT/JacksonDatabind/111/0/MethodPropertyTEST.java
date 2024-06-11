package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.StandardSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;

/**
 * Unit tests for the {@code MethodProperty} class.
 */
public class MethodPropertyTest {

    static class TestClass {

        @JsonProperty(value = "name")
        private String _name;

        @JsonProperty(value = "age")
        private int _age;

        public String getName() {
            return _name;
        }

        @JsonSetter(value = "name")
        public void setName(String value) {
            _name = value;
        }

        public int getAge() {
            return _age;
        }

        @JsonSetter(value = "age")
        public void setAge(int value) {
            _age = value;
        }

        @JsonProperty(value = "value")
        private Object _value;

        @JsonSetter(value = "value")
        public void setValue(Object value) {
            _value = value;
        }
    }

    @Test
    public void testMethodProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanPropertyDefinition propDef = PropertyModifier.makeIgnore();
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeDeserializer typeDeser = new stdClassSerializer(TestClass.class);
        Annotations contextAnnotations = Annotations.forSerialization(TestClass.class);
        AnnotatedMethod methodName = Annotations.findMethod(TestClass.class, "setName", String.class);
        MethodProperty methodPropertyName = new MethodProperty(propDef, javaType, typeDeser, contextAnnotations, methodName);

        TestClass testInstance = new TestClass();
        JsonParser jsonParser = mapper.getFactory().createParser("{\"name\":\"John Doe\"}");
        jsonParser.nextToken();

        methodPropertyName.deserializeAndSet(jsonParser, null, testInstance);
        assertEquals("John Doe", testInstance.getName());

        // Test null value
        jsonParser.nextToken();
        jsonParser.nextToken(); // consume "null" token
        methodPropertyName.deserializeAndSet(jsonParser, null, testInstance);
        assertNull(testInstance.getName());

        // Test non-null value
        jsonParser.nextToken();
        jsonParser.nextValue("Jane Doe");
        methodPropertyName.deserializeAndSet(jsonParser, null, testInstance);
        assertEquals("Jane Doe", testInstance.getName());

        // Test deserialize and set with value deserializer
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        methodPropertyName = new MethodProperty(methodPropertyName, stringDeserializer, null);
        jsonParser.nextToken();
        jsonParser.nextValue("Jack Doe");
        methodPropertyName.deserializeAndSet(jsonParser, null, testInstance);
        assertEquals("Jack Doe", testInstance.getName());

        // Test deserialize and set with null value provider
        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext context) throws IOException {
                return "default";
            }
        };
        methodPropertyName = new MethodProperty(methodPropertyName, null, nullValueProvider);
        jsonParser.nextToken();
        jsonParser.nextToken(); // consume "null" token
        methodPropertyName.deserializeAndSet(jsonParser, null, testInstance);
        assertEquals("default", testInstance.getName());

        // Test setter method with empty constructor
        TestClass emptyConstructorTestInstance = new TestClass();
        methodPropertyName.set(emptyConstructorTestInstance, "Test");
        assertEquals("Test", emptyConstructorTestInstance.getName());

        // Test setter method with constructor having arguments
        TestClass argConstructorTestInstance = new TestClass();
        Class<TestClass> testClass = TestClass.class;
        Constructor<TestClass> constructor = testClass.getConstructor(String.class, int.class);
        Object[] constructorArgs = new Object[]{"Test2", 20};
        methodPropertyName.set(argConstructorTestInstance, constructorArgs);
        assertEquals("Test2", argConstructorTestInstance.getName());
        assertEquals(20, argConstructorTestInstance.getAge());

        // Test deserialize and return
        JsonDeserializer<TestClass> testClassDeserializer = mapper.findValueDeserializer(TestClass.class);
        methodPropertyName = new MethodProperty(methodPropertyName, testClassDeserializer, null);
        TestClass deserializedInstance = (TestClass) methodPropertyName.deserializeSetAndReturn(jsonParser, null, null);
        assertEquals("Jack Doe", deserializedInstance.getName());

        // Test set and return
        TestClass setAndReturnInstance = new TestClass();
        Object setAndReturnValue = methodPropertyName.setAndReturn(setAndReturnInstance, "Test3");
        assertEquals("Test3", setAndReturnInstance.getName());
        assertEquals(setAndReturnInstance, setAndReturnValue);
    }

    @Test
    public void testMethodPropertyWithValueDeserializerAndTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanPropertyDefinition propDef = PropertyModifier.makeIgnore();
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeDeserializer typeDeser = new stdClassSerializer(TestClass.class);
        Annotations contextAnnotations = Annotations.forSerialization(TestClass.class);
        AnnotatedMethod methodValue = Annotations.findMethod(TestClass.class, "setValue", Object.class);
        MethodProperty methodPropertyValue = new MethodProperty(propDef, javaType, typeDeser, contextAnnotations, methodValue);
        TypeDeserializer stringTypeDeser = new StringDeserializer();
        methodPropertyValue = new MethodProperty(methodPropertyValue, null, stringTypeDeser);

        TestClass testInstance = new TestClass();
        JsonParser jsonParser = mapper.getFactory().createParser("{\"value\":\"TestString\"}");
        jsonParser.nextToken();

        Object deserializedValue = methodPropertyValue.deserializeSetAndReturn(jsonParser, null, testInstance);
        assertEquals("TestString", deserializedValue);
        assertEquals("TestString", testInstance.getValue());
    }

    static class StringDeserializer extends StdScalarDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    static class stdClassSerializer<T> extends StdSerializer<T> {
        private final Class<T> _class;

        stdClassSerializer(Class<T> clazz) {
            this(null, clazz);
        }

        stdClassSerializer(SerializerProvider prov, Class<T> clazz) {
            super(prov, clazz);
            _class = clazz;
        }

        @Override
        public void serializeValues(Object values, JsonGenerator g, SerializerProvider serializers)
                throws IOException {
            Object value = values;
            if (values.getClass().isArray()) {
                value = BeanUtil.convertValue(values, _class);
            }
            g.writeObject(value);
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator g, SerializerProvider serializers, TypeSerializer typeSer)
                throws IOException {
            g.writeObject(value);
            typeSer.writeTypePrefixForValue(value, g);
            typeSer.writeTypeSuffixForValue(value, g);
        }
    }
}