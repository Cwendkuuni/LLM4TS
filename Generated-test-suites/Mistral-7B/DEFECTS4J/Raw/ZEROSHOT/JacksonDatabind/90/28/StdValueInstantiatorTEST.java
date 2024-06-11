package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorImpl;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdValueInstantiatorTest {

    private StdValueInstantiator instantiator;
    private DeserializationContext context;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        context = mock(DeserializationContext.class);
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);
        instantiator = new StdValueInstantiator(mapper.getConfig(), mapper.getTypeFactory().constructType(TestClass.class));
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        assertEquals("UNKNOWN TYPE", instantiator.getValueTypeDesc());
        assertSame(Object.class, instantiator.getValueClass());
    }

    @Test
    public void testConstructorWithJavaType() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        instantiator = new StdValueInstantiator(mapper.getConfig(), javaType);
        assertEquals("TestClass", instantiator.getValueTypeDesc());
        assertSame(TestClass.class, instantiator.getValueClass());
    }

    @Test
    public void testCopyConstructor() throws IOException {
        StdValueInstantiator src = new StdValueInstantiator(mapper.getConfig(), mapper.getTypeFactory().constructType(TestClass.class));
        StdValueInstantiator dest = new StdValueInstantiator(src);
        assertEquals("TestClass", dest.getValueTypeDesc());
        assertSame(TestClass.class, dest.getValueClass());
        assertNull(dest._defaultCreator);
        assertNull(dest._constructorArguments);
        assertNull(dest._delegateCreator);
        assertNull(dest._delegateArguments);
        assertNull(dest._fromStringCreator);
        assertNull(dest._fromIntCreator);
        assertNull(dest._fromLongCreator);
        assertNull(dest._fromDoubleCreator);
        assertNull(dest._fromBooleanCreator);
        assertNull(dest._incompleteParameter);
    }

    @Test
    public void testConfigureFromObjectSettings() throws IOException {
        AnnotatedWithParams defaultCreator = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams delegateCreator = Mockito.mock(AnnotatedWithParams.class);
        JavaType delegateType = mock(JavaType.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[1];
        SettableBeanProperty[] constructorArguments = new SettableBeanProperty[1];

        when(defaultCreator.getDeclaringClass()).thenReturn(TestClass.TestDefaultConstructor.class);
        when(delegateCreator.getDeclaringClass()).thenReturn(TestClass.TestDelegateConstructor.class);
        when(delegateType.getRawClass()).thenReturn(String.class);

        instantiator.configureFromObjectSettings(defaultCreator, delegateCreator, delegateType, delegateArguments,
                null, constructorArguments);

        assertNull(instantiator._defaultCreator);
        assertSame(delegateCreator, instantiator._delegateCreator);
        assertSame(delegateType, instantiator._delegateType);
        assertSame(delegateArguments, instantiator._delegateArguments);
        assertNull(instantiator._constructorArguments);
        assertNull(instantiator._fromStringCreator);
        assertNull(instantiator._fromIntCreator);
        assertNull(instantiator._fromLongCreator);
        assertNull(instantiator._fromDoubleCreator);
        assertNull(instantiator._fromBooleanCreator);
        assertNull(instantiator._incompleteParameter);
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        TestClass testClass = new TestClass();
        when(context.findValueInstantiator(TestClass.class)).thenReturn(instantiator);
        when(context.getDefaultPropertyValue("value")).thenReturn("test");

        Object result = instantiator.createUsingDefault(context);
        assertEquals(testClass, result);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        TestClass testClass = new TestClass("test");
        when(context.findValueInstantiator(TestClass.class)).thenReturn(instantiator);
        when(context.findProperty("value")).thenReturn(new PropertyName("value"));
        when(context.findCreator(TestClass.class, new PropertyName("value"))).thenReturn(Mockito.mock(ValueDeserializer.class));
        when(context.handleParameterBinding(new PropertyName("value"), TestClass.class)).thenReturn(new TestPropertyBinding());

        Object[] args = new Object[]{"test"};
        Object result = instantiator.createFromObjectWith(context, args);
        assertEquals(testClass, result);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        TestClass testClass = new TestClass("test");
        TestClassDelegate testClassDelegate = new TestClassDelegate();
        when(context.findValueInstantiator(String.class)).thenReturn(new SimpleValueInstantiator());
        when(context.handleParameterBinding(new PropertyName("delegate"), TestClass.class)).thenReturn(new TestDelegateBinding());

        Object result = instantiator.createUsingDelegate(context, testClassDelegate);
        assertEquals(testClass, result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        TestArray testArray = new TestArray(new TestClass[1]);
        when(context.findValueInstantiator(TestClass[].class)).thenReturn(instantiator);
        when(context.handleParameterBinding(new PropertyName("delegate"), TestClass[].class)).thenReturn(new TestDelegateBinding());

        Object result = instantiator.createUsingArrayDelegate(context, testArray);
        assertEquals(testArray, result);
    }

    @Test
    public void testCreateFromString() throws IOException {
        TestClass testClass = new TestClass("test");
        when(context.findValueInstantiator(TestClass.class)).thenReturn(instantiator);
        when(context.findValueInstantiator(String.class)).thenReturn(new SimpleValueInstantiator());
        when(context.handleParameterBinding(new PropertyName("value"), TestClass.class)).thenReturn(new TestValueBinding());

        Object result = instantiator.createFromString(context, "test");
        assertEquals(testClass, result);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        TestClass testClass = new TestClass(1);
        when(context.findValueInstantiator(TestClass.class)).thenReturn(instantiator);
        when(context.findValueInstantiator(Integer.class)).thenReturn(new SimpleValueInstantiator());
        when(context.handleParameterBinding(new PropertyName("value"), TestClass.class)).thenReturn(new TestValueBinding());

        Object result = instantiator.createFromInt(context, 1);
        assertEquals(testClass, result);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        TestClass testClass = new TestClass(1L);
        when(context.findValueInstantiator(TestClass.class)).thenReturn(instantiator);
        when(context.findValueInstantiator(Long.class)).thenReturn(new SimpleValueInstantiator());
        when(context.handleParameterBinding(new PropertyName("value"), TestClass.class)).thenReturn(new TestValueBinding());

        Object result = instantiator.createFromLong(context, 1L);
        assertEquals(testClass, result);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        TestClass testClass = new TestClass(1.0);
        when(context.findValueInstantiator(TestClass.class)).thenReturn(instantiator);
        when(context.findValueInstantiator(Double.class)).thenReturn(new SimpleValueInstantiator());
        when(context.handleParameterBinding(new PropertyName("value"), TestClass.class)).thenReturn(new TestValueBinding());

        Object result = instantiator.createFromDouble(context, 1.0);
        assertEquals(testClass, result);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        TestClass testClass = new TestClass(true);
        when(context.findValueInstantiator(TestClass.class)).thenReturn(instantiator);
        when(context.findValueInstantiator(Boolean.class)).thenReturn(new SimpleValueInstantiator());
        when(context.handleParameterBinding(new PropertyName("value"), TestClass.class)).thenReturn(new TestValueBinding());

        Object result = instantiator.createFromBoolean(context, true);
        assertEquals(testClass, result);
    }

    @Test
    public void testGetDelegateType() {
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        instantiator = new StdValueInstantiator(mapper.getConfig(), javaType);
        assertEquals(String.class, instantiator.getDelegateType(mapper.getConfig()).getRawClass());
    }

    @Test
    public void testGetArrayDelegateType() {
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass[].class);
        instantiator = new StdValueInstantiator(mapper.getConfig(), javaType);
        assertEquals(TestClass.class, instantiator.getArrayDelegateType(mapper.getConfig()).getRawClass());
    }

    @Test
    public void testGetFromObjectArguments() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] arguments = new SettableBeanProperty[1];
        instantiator.configureFromObjectSettings(creator, null, null, null, null, arguments);
        assertEquals(arguments, instantiator.getFromObjectArguments(mapper.getConfig()));
    }

    private static class TestClass {
        @JsonProperty
        private String value;

        public TestClass() {
        }

        public TestClass(String value) {
            this.value = value;
        }

        public static class TestDefaultConstructor {
        }

        public static class TestDelegateConstructor {
            @JsonCreator
            public static TestClass create(@JsonProperty("delegate") TestClass delegate) {
                return new TestClass(delegate.value);
            }
        }
    }

    private static class TestArray {
        private TestClass[] values;

        public TestArray(TestClass[] values) {
            this.values = values;
        }

        public static class TestDelegateBinding extends AnnotatedWithParams {
            public TestDelegateBinding() {
                super(new TestClassDelegate(), TestClass[].class, null, null, null);
            }
        }
    }

    private static class TestClassDelegate {
        @JsonCreator
        public static TestClass create(@JsonProperty("delegate") TestClass delegate) {
            return new TestClass(delegate.value);
        }
    }

    private static class TestPropertyBinding extends SettableBeanProperty {
        public TestPropertyBinding() {
            super(new PropertyName("value"), TestClass.class.getDeclaredField("value"),
                    new TestClassDeserializer(), null, null);
        }
    }

    private static class TestValueBinding extends SettableBeanProperty {
        public TestValueBinding() {
            super(new PropertyName("value"), TestClass.class.getDeclaredField("value"),
                    new SimpleValueInstantiator(), null, null);
        }
    }

    private static class TestClassDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromArray(DeserializationContext ctxt, JsonDeserializer<?> deserializer,
                Object ctxtObj, Type deserialType, Type var1, int[] acceptedTypes, Object[] paramArrayOfObject)
                throws IOException {
            return new TestClass((String) ctxtObj);
        }
    }

    private static class SimpleValueInstantiator extends StdValueInstantiator {
        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new Object();
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return new Object();
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return new Object();
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return new Object();
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return new Object();
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return new Object();
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return new Object();
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return new Object();
        }
    }
}

class NopAnnotationIntrospector extends AnnotationIntrospector {

    @Override
    public AnnotatedMember findMember(DeserializationConfig config, JavaType type, String memberName) {
        return null;
    }

    @Override
    public AnnotatedMethod findMethod(DeserializationConfig config, JavaType type, String memberName,
            Class<?>[] paramTypes) {
        return null;
    }

    @Override
    public AnnotatedField findField(DeserializationConfig config, JavaType type, String memberName) {
        return null;
    }

    @Override
    public AnnotatedConstructor findConstructor(DeserializationConfig config, JavaType type, Class<?>[] paramTypes) {
        return null;
    }

    @Override
    public AnnotatedClass findClassAnnotations(DeserializationConfig config, JavaType type) {
        return null;
    }

    @Override
    public boolean hasMarkerAnnotation(DeserializationConfig config, JavaType type, Class<?> annotationType) {
        return false;
    }
}