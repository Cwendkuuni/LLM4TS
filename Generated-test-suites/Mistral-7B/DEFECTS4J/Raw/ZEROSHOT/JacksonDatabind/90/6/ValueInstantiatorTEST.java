package com.example.junit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ValueInstantiatorTest {

    @Test
    public void testBasicValueInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        ValueInstantiator valueInstantiator = new ValueInstantiatorBase("com.example.MyClass");

        Assert.assertEquals("java.lang.Object", valueInstantiator.getValueTypeDesc());
        Assert.assertEquals("java.lang.Object", valueInstantiator.getValueClass().getName());
        Assert.assertFalse(valueInstantiator.canCreateUsingDefault());
        Assert.assertFalse(valueInstantiator.canCreateUsingDelegate());
        Assert.assertFalse(valueInstantiator.canCreateFromObjectWith());
        Assert.assertFalse(valueInstantiator.canCreateFromString());
        Assert.assertFalse(valueInstantiator.canCreateFromInt());
        Assert.assertFalse(valueInstantiator.canCreateFromLong());
        Assert.assertFalse(valueInstantiator.canCreateFromDouble());
        Assert.assertFalse(valueInstantiator.canCreateFromBoolean());

        // Test createUsingDefault
        Assert.assertNull(valueInstantiator.createUsingDefault(mapper.getDeserializationContext()));

        // Test createFromObjectWith
        MyClass obj = new MyClass("Test");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = mapper.writerWithView("simple").writeValueAsString(obj);
        MyClass deserialized = mapper.readValue(json, MyClass.class);

        Object result = valueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), deserialized);
        Assert.assertSame(obj, result);

        // Test createFromString
        Class<?> stringClass = String.class;
        ValueInstantiator stringValueInstantiator = new StringValueInstantiator();
        Assert.assertNotNull(stringValueInstantiator.createFromString(mapper.getDeserializationContext(), "Hello World"));

        // Test createFromInt
        Class<?> intClass = Integer.class;
        ValueInstantiator intValueInstantiator = new IntValueInstantiator();
        Assert.assertNotNull(intValueInstantiator.createFromInt(mapper.getDeserializationContext(), 42));

        // Test createFromLong
        Class<?> longClass = Long.class;
        ValueInstantiator longValueInstantiator = new LongValueInstantiator();
        Assert.assertNotNull(longValueInstantiator.createFromLong(mapper.getDeserializationContext(), 42L));

        // Test createFromDouble
        Class<?> doubleClass = Double.class;
        ValueInstantiator doubleValueInstantiator = new DoubleValueInstantiator();
        Assert.assertNotNull(doubleValueInstantiator.createFromDouble(mapper.getDeserializationContext(), 42.0));

        // Test createFromBoolean
        Class<?> booleanClass = Boolean.class;
        ValueInstantiator booleanValueInstantiator = new BooleanValueInstantiator();
        Assert.assertNotNull(booleanValueInstantiator.createFromBoolean(mapper.getDeserializationContext(), true));

        // Test createUsingDelegate
        MyClassWithDelegate myClassWithDelegate = new MyClassWithDelegate();
        AnnotatedMethod delegateMethod = AnnotationIntrospector.findMethod("createFromDelegate", MyClassWithDelegate.class);
        ValueInstantiator delegateValueInstantiator = new DelegateValueInstantiator();
        Assert.assertNotNull(delegateValueInstantiator.createUsingDelegate(mapper.getDeserializationContext(), myClassWithDelegate));

        // Test createUsingArrayDelegate
        MyClassArray myClassArray = new MyClassArray();
        AnnotatedMethod delegateArrayMethod = AnnotationIntrospector.findMethod("createFromArrayDelegate", MyClassArray.class);
        ValueInstantiator arrayDelegateValueInstantiator = new ArrayDelegateValueInstantiator();
        Assert.assertNotNull(arrayDelegateValueInstantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), myClassArray));
    }

    static class MyClass {
        private final String name;

        @JsonCreator
        public MyClass(@JsonProperty("name") String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class MyClassWithDelegate {
        private final String name;

        @JsonCreator
        public MyClassWithDelegate(@JsonProperty("name") String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Object createFromDelegate(DeserializationContext ctx) throws IOException {
            return new MyClassWithDelegate("Delegate Created");
        }
    }

    static class MyClassArray {
        private final MyClass[] elements;

        @JsonCreator
        public MyClassArray(@JsonProperty("elements") MyClass[] elements) {
            this.elements = elements;
        }

        public MyClass[] getElements() {
            return elements;
        }

        public Object createFromArrayDelegate(DeserializationContext ctx) throws IOException {
            return new ObjectMapper().readValue(ctx.getText(), MyClass[].class);
        }
    }

    static class ValueInstantiatorBase extends ValueInstantiator {
        private final Class<?> _valueType;

        public ValueInstantiatorBase(String className) {
            this(ClassUtil.loadClassMandatory(className));
        }

        public ValueInstantiatorBase(JavaType valueType) {
            _valueType = valueType.getRawClass();
        }

        @Override
        public String getValueTypeDesc() {
            return _valueType.getName();
        }

        @Override
        public Class<?> getValueClass() {
            return _valueType;
        }
    }

    static class StringValueInstantiator extends ValueInstantiatorBase {
        @Override
        public boolean canCreateFromString() {
            return true;
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return value;
        }
    }

    static class IntValueInstantiator extends ValueInstantiatorBase {
        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return value;
        }
    }

    static class LongValueInstantiator extends ValueInstantiatorBase {
        @Override
        public boolean canCreateFromLong() {
            return true;
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return value;
        }
    }

    static class DoubleValueInstantiator extends ValueInstantiatorBase {
        @Override
        public boolean canCreateFromDouble() {
            return true;
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return value;
        }
    }

    static class BooleanValueInstantiator extends ValueInstantiatorBase {
        @Override
        public boolean canCreateFromBoolean() {
            return true;
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return value;
        }
    }

    static class DelegateValueInstantiator extends ValueInstantiatorBase {
        @Override
        public boolean canCreateUsingDelegate() {
            return true;
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            try {
                Constructor<Object> constructor = _valueType.getDeclaredConstructor(DelegateValueInstantiator.class);
                constructor.setAccessible(true);
                return constructor.newInstance(this);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class ArrayDelegateValueInstantiator extends ValueInstantiatorBase {
        @Override
        public boolean canCreateUsingArrayDelegate() {
            return true;
        }

        @Override
        public JavaType getArrayDelegateType(DeserializationConfig config) {
            return JavaType.constructParametricType(ArrayList.class, getValueClass());
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            AnnotatedClass ac = AnnotationIntrospector.findClassAnnotations(DelegateArrayValueInstantiator.class, getValueClass());
            PropertyNamingStrategy pns = ac.findPropertyAccessor(PropertyNamingStrategy.class);
            return mapper.readValue(ctxt.getText(), getArrayDelegateType(ctxt.getConfig()).withoutProperties());
        }
    }
}

class DelegateValueInstantiator {
    public DelegateValueInstantiator(ValueInstantiatorBase valueInstantiatorBase) {
        this.valueInstantiatorBase = valueInstantiatorBase;
    }

    private ValueInstantiatorBase valueInstantiatorBase;

    public Object createFromDelegate(DeserializationContext ctx) throws IOException {
        return valueInstantiatorBase.createUsingDefault(ctx);
    }
}

class ArrayDelegateValueInstantiator {
    public ArrayDelegateValueInstantiator(ValueInstantiatorBase valueInstantiatorBase) {
        this.valueInstantiatorBase = valueInstantiatorBase;
    }

    private ValueInstantiatorBase valueInstantiatorBase;
}