package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.Named;
import com.fasterxml.jackson.databind.introspect.ValuePropertyAnnotation;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test class for StdValueInstantiator
 */
public class StdValueInstantiatorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testStdValueInstantiator() throws JsonMappingException, IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(StdValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);

        // Test default constructor
        assertNotNull(instantiator.createUsingDefault(mapper.getDeserializationContext()));

        // Test constructor with args
        AnnotatedWithParams withArgsCreator = mapper.findValueDeserializerAnnotation(
                StdValueInstantiator.class.getDeclaredMethod("configureFromObjectSettings", AnnotatedWithParams.class,
                        AnnotatedWithParams.class, JavaType.class, SettableBeanProperty[].class, AnnotatedWithParams.class,
                        SettableBeanProperty[].class), 0);
        instantiator.configureFromObjectSettings(withArgsCreator, null, null, null, null, null);
        assertNotNull(instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[0]));

        // Test constructor with delegate
        JavaType delegateType = mapper.getTypeFactory().constructType(ValueDeserializer.class);
        AnnotatedWithParams delegateCreator = mapper.findValueDeserializerAnnotation(ValueDeserializer.class.getDeclaredMethod("noArgs"), 0);
        instantiator.configureUsingDelegate(delegateType, delegateCreator);
        assertNotNull(instantiator.createUsingDelegate(mapper.getDeserializationContext(), mapper.findValueDeserializer(delegateType).createUsingDefault(mapper.getDeserializationContext())));

        // Test constructor with array delegate
        JavaType arrayDelegateType = mapper.getTypeFactory().constructType(ValueDeserializer[].class);
        AnnotatedWithParams arrayDelegateCreator = mapper.findValueDeserializerAnnotation(ValueDeserializer[].class.getDeclaredMethod("noArgs"), 0);
        instantiator.configureFromArraySettings(arrayDelegateCreator, arrayDelegateType, new SettableBeanProperty[0]);
        assertNotNull(instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), mapper.findValueDeserializer(arrayDelegateType).createUsingDefault(mapper.getDeserializationContext())));

        // Test constructor with fromString
        AnnotatedWithParams fromStringCreator = mapper.findValueDeserializerAnnotation(StdValueInstantiator.class.getDeclaredMethod("configureFromStringCreator", AnnotatedWithParams.class), 0);
        instantiator.configureFromStringCreator(fromStringCreator);
        String json = "\"Hello World\"";
        assertNotNull(instantiator.createFromString(mapper.getDeserializationContext(), json));

        // Test constructor with fromInt
        AnnotatedWithParams fromIntCreator = mapper.findValueDeserializerAnnotation(StdValueInstantiator.class.getDeclaredMethod("configureFromIntCreator", AnnotatedWithParams.class), 0);
        instantiator.configureFromIntCreator(fromIntCreator);
        assertNotNull(instantiator.createFromInt(mapper.getDeserializationContext(), 42));

        // Test constructor with fromLong
        AnnotatedWithParams fromLongCreator = mapper.findValueDeserializerAnnotation(StdValueInstantiator.class.getDeclaredMethod("configureFromLongCreator", AnnotatedWithParams.class), 0);
        instantiator.configureFromLongCreator(fromLongCreator);
        assertNotNull(instantiator.createFromLong(mapper.getDeserializationContext(), 42L));

        // Test constructor with fromDouble
        AnnotatedWithParams fromDoubleCreator = mapper.findValueDeserializerAnnotation(StdValueInstantiator.class.getDeclaredMethod("configureFromDoubleCreator", AnnotatedWithParams.class), 0);
        instantiator.configureFromDoubleCreator(fromDoubleCreator);
        assertNotNull(instantiator.createFromDouble(mapper.getDeserializationContext(), 42.0));

        // Test constructor with fromBoolean
        AnnotatedWithParams fromBooleanCreator = mapper.findValueDeserializerAnnotation(StdValueInstantiator.class.getDeclaredMethod("configureFromBooleanCreator", AnnotatedWithParams.class), 0);
        instantiator.configureFromBooleanCreator(fromBooleanCreator);
        assertNotNull(instantiator.createFromBoolean(mapper.getDeserializationContext(), true));

        // Test null value
        assertNull(instantiator.createUsingDefault(mapper.getDeserializationContext()));

        // Test invalid constructor
        AnnotatedClass clazz = mapper.getTypeFactory().constructType(InvalidClass.class);
        instantiator.configureUsingDelegate(clazz, null);
        try {
            instantiator.createUsingDelegate(mapper.getDeserializationContext(), mapper.findValueDeserializer(clazz).createUsingDefault(mapper.getDeserializationContext()));
            assert(false); // should not reach here
        } catch (JsonMappingException e) {
            // expected
        }

        // Test incomplete constructor
        AnnotatedParameter incompleteParameter = mapper.findValueDeserializerAnnotation(StdValueInstantiator.class.getDeclaredMethod("configureIncompleteParameter", AnnotatedParameter.class), 0).getParameters()[0];
        instantiator.configureIncompleteParameter(incompleteParameter);
        assertNull(instantiator.createUsingDefault(mapper.getDeserializationContext()));

        // Test missing constructor
        JavaType missingType = mapper.getTypeFactory().constructType(MissingClass.class);
        instantiator = mapper.findValueInstantiator(missingType);
        assertNull(instantiator);
    }

    static class InvalidClass {

        @JsonCreator
        public InvalidClass(int i) {}

        @JsonProperty
        public int value;
    }

    static class MissingClass {

        @JsonProperty
        public int value;
    }

    static class ValueProperty {

        @JsonProperty
        public int value;

        @ValuePropertyAnnotation
        public int annotation;

        public ValueProperty() {}

        @JsonCreator
        public ValueProperty(@JsonProperty int value, @ValuePropertyAnnotation int annotation) {
            this.value = value;
            this.annotation = annotation;
        }
    }

    static class TestClass {

        @JsonProperty
        public int value;

        @JsonProperty
        public TestClass(int value) {
            this.value = value;
        }

        @JsonCreator
        public TestClass(@JsonProperty int value) {
            this.value = value;
        }

        @JsonProperty
        public TestClass(@JsonProperty int value1, @JsonProperty int value2) {
            this.value = value1 + value2;
        }

        @JsonCreator
        public TestClass(@JsonProperty int value1, @JsonProperty int value2, @JsonProperty int value3) {
            this.value = value1 + value2 + value3;
        }

        @JsonProperty
        public TestClass(@JsonProperty BeanPropertyDefinition[] constructorArguments) {
            this(constructorArguments[0].getName(), constructorArguments[1].getName(), constructorArguments[2].getName());
        }

        @JsonProperty
        public TestClass(@JsonProperty AnnotatedMethod constructorMethod, @JsonProperty SettableBeanProperty[] constructorArguments) {
            this(constructorMethod, constructorArguments[0].getName(), constructorArguments[1].getName());
        }

        @JsonProperty
        public TestClass(@JsonProperty AnnotatedField field, @JsonProperty SettableBeanProperty[] constructorArguments) {
            this(field.getAnnotation(JsonProperty.class).value(), constructorArguments[0].getName(), constructorArguments[1].getName());
        }
    }

    @Test
    public void testValueInstantiatorWithCustomDeserializers() throws JsonMappingException, IOException {
        // Test class with custom deserializers for properties
        JavaType javaType = mapper.getTypeFactory().constructType(TestClassWithCustomDeserializers.class);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);

        String json = "{\"a\": 1, \"b\": \"two\", \"c\": 3.5, \"d\": false, \"e\": null}";
        TestClassWithCustomDeserializers obj = instantiator.createFromString(mapper.getDeserializationContext(), json);
        assertNotNull(obj);
        assertNotNull(obj.a);
        assertNotNull(obj.b);
        assertEquals(3.5, obj.c, 0.0);
        assertFalse(obj.d);
        assertNull(obj.e);

        // Test class with custom deserializers for constructors
        JavaType javaType2 = mapper.getTypeFactory().constructType(TestClassWithCustomDeserializers2.class);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ValueInstantiator instantiator2 = mapper.findValueInstantiator(javaType2);

        String json2 = "{ \"a\": 1, \"b\": \"two\", \"c\": [1, 2], \"d\": 3.5 }";
        TestClassWithCustomDeserializers2 obj2 = instantiator2.createFromString(mapper.getDeserializationContext(), json2);
        assertNotNull(obj2);
        assertNotNull(obj2.a);
        assertEquals(2, obj2.b.size());
        assertTrue(obj2.b.contains(1));
        assertTrue(obj2.b.contains(2));
        assertEquals(3.5, obj2.c, 0.0);
    }

    static class TestClassWithCustomDeserializers {

        private int a;
        private String b;
        private float c;
        private boolean d;
        private Object e;

        @JsonProperty
        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        @JsonDeserialize(using = StringDeserializer.class)
        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        @JsonDeserialize(using = FloatDeserializer.class)
        public float getC() {
            return c;
        }

        public void setC(float c) {
            this.c = c;
        }

        @JsonProperty
        public boolean isD() {
            return d;
        }

        public void setD(boolean d) {
            this.d = d;
        }

        @JsonProperty
        public Object getE() {
            return e;
        }

        public void setE(Object e) {
            this.e = e;
        }

        @JsonCreator
        public TestClassWithCustomDeserializers(@JsonProperty int a, @JsonProperty String b, @JsonProperty float c,
                @JsonProperty boolean d, @JsonProperty Object e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
        }
    }

    static class StringDeserializer extends StdDeserializer<String> {

        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }

    }

    static class FloatDeserializer extends StdDeserializer<Float> {

        public FloatDeserializer() {
            this(null);
        }

        public FloatDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Float deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsFloat();
        }

    }

    static class TestClassWithCustomDeserializers2 {

        private int a;
        private List<Integer> b;
        private float c;

        @JsonProperty
        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        @JsonProperty
        public List<Integer> getB() {
            return b;
        }

        public void setB(List<Integer> b) {
            this.b = b;
        }

        @JsonDeserialize(using = CustomListDeserializer.class)
        public float getC() {
            return c;
        }

        public void setC(float c) {
            this.c = c;
        }

        @JsonCreator
        public TestClassWithCustomDeserializers2(@JsonProperty int a, @JsonProperty List<Integer> b, @JsonProperty float c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    static class CustomListDeserializer extends StdDeserializer<List<Integer>> {

        public CustomListDeserializer() {
            this(null);
        }

        public CustomListDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public List<Integer> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            int size = jp.getReader().readValueAsInt();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(jp.nextValueAsInt());
            }
            return list;
        }

    }
}