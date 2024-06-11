import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.DeserializerPropertyMicroscope;
import com.fasterxml.jackson.databind.introspect.NestedCreatableAnnotatedClass;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StdValueInstantiatorTest {

    @Test
    public void testStdValueInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        assertNotNull(stdValueInstantiator);
        assertEquals("UNKNOWN TYPE", stdValueInstantiator.getValueTypeDesc());
        assertTrue(stdValueInstantiator.canCreateUsingDefault());
        assertTrue(stdValueInstantiator.canCreateUsingDelegate());
    }

    @Test(expected = JsonMappingException.class)
    public void testStdValueInstantiator_InvalidDefaultCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        stdValueInstantiator.configureFromArraySettings(null, javaType, null);
        stdValueInstantiator.createUsingDefault(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testStdValueInstantiator_InvalidDelegateCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        stdValueInstantiator.configureFromDelegateSettings(null, javaType, null);
        stdValueInstantiator.createUsingDelegate(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testStdValueInstantiator_NullValueType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureFromObjectSettings(null, null, null, null, null, null);
        stdValueInstantiator.createUsingDefault(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testStdValueInstantiator_NullValueClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureFromObjectSettings(null, null, null, null, null, null);
        stdValueInstantiator.createUsingDefault(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testStdValueInstantiator_NullWithArgsCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureFromObjectSettings(null, null, null, null, null, null);
        stdValueInstantiator.createFromObjectWith(null, new Object[0]);
    }

    @Test(expected = JsonMappingException.class)
    public void testStdValueInstantiator_NullDelegateCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureFromDelegateSettings(null, null, null, null);
        stdValueInstantiator.createUsingDelegate(null, null);
    }

    @Test
    public void testStdValueInstantiator_DefaultConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        Object result = stdValueInstantiator.createUsingDefault(null);
        assertNotNull(result);
    }

    @Test
    public void testStdValueInstantiator_WithArgsConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        JavaType valueType = mapper.getTypeFactory().constructType(MyClass.class);
        Object[] arguments = new Object[]{new MyClass(), new MyClass()};
        Object result = stdValueInstantiator.createFromObjectWith(null, arguments);
        assertNotNull(result);
        assertEquals(MyClass.class, result.getClass());
    }

    @Test
    public void testStdValueInstantiator_DelegateConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper delegateMapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        ValueInstantiator delegateValueInstantiator = delegateMapper.getValueInstantiator();
        JavaType valueType = mapper.getTypeFactory().constructType(MyClass.class);
        Object delegate = delegateValueInstantiator;
        Object result = stdValueInstantiator.createUsingDelegate(null, delegate);
        assertNotNull(result);
        assertEquals(MyClass.class, result.getClass());
    }

    @Test
    public void testStdValueInstantiator_ArrayDelegateConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper delegateMapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        ValueInstantiator delegateValueInstantiator = delegateMapper.getValueInstantiator();
        JavaType valueType = mapper.getTypeFactory().constructType(MyClass[].class);
        Object delegate = delegateValueInstantiator;
        Object result = stdValueInstantiator.createUsingArrayDelegate(null, delegate);
        assertNotNull(result);
        assertEquals(MyClass[].class, result.getClass());
    }

    @Test
    public void testStdValueInstantiator_FromStringConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureFromStringCreator(mapper.findValueInstantiator(String.class));
        String value = "test";
        Object result = stdValueInstantiator.createFromString(null, value);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testStdValueInstantiator_FromIntConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureFromIntCreator(mapper.findValueInstantiator(int.class));
        int value = 1;
        Object result = stdValueInstantiator.createFromInt(null, value);
        assertNotNull(result);
        assertEquals(int.class, result.getClass());
    }

    @Test
    public void testStdValueInstantiator_FromLongConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureFromLongCreator(mapper.findValueInstantiator(long.class));
        long value = 1L;
        Object result = stdValueInstantiator.createFromLong(null, value);
        assertNotNull(result);
        assertEquals(long.class, result.getClass());
    }

    @Test
    public void testStdValueInstantiator_FromDoubleConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureFromDoubleCreator(mapper.findValueInstantiator(double.class));
        double value = 1.0;
        Object result = stdValueInstantiator.createFromDouble(null, value);
        assertNotNull(result);
        assertEquals(double.class, result.getClass());
    }

    @Test
    public void testStdValueInstantiator_FromBooleanConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureFromBooleanCreator(mapper.findValueInstantiator(boolean.class));
        boolean value = true;
        Object result = stdValueInstantiator.createFromBoolean(null, value);
        assertNotNull(result);
        assertEquals(boolean.class, result.getClass());
    }

    @Test
    public void testStdValueInstantiator_IncompleteParameter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = mapper.getValueInstantiator();
        stdValueInstantiator.configureIncompleteParameter(mapper.findParameter("testParameter"));
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(MyClass.class);
        Object result = stdValueInstantiator.createUsingDefault(null);
        assertNotNull(result);
    }

    static class MyClass {
        @JsonCreator
        public MyClass(@JsonProperty("test") int test) {
            // constructor
        }
    }
}