package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Test;

/**
 * Test class for StdValueInstantiator.
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    @Test
    public void testGetValueTypeDesc() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertEquals("UNKNOWN TYPE", instantiator.getValueTypeDesc());

        JavaType javaType = createJavaType("java.lang.String");
        StdValueInstantiator instantiatorWithType = new StdValueInstantiator(null, javaType);
        assertEquals("java.lang.String", instantiatorWithType.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertEquals(Object.class, instantiator.getValueClass());

        JavaType javaType = createJavaType("java.lang.String");
        StdValueInstantiator instantiatorWithType = new StdValueInstantiator(null, javaType);
        assertEquals(javaType.getRawClass(), instantiatorWithType.getValueClass());
    }

    @Test
    public void testCanCreateFromString() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromString());

        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestStringCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        assertTrue(instantiatorWithCreator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromInt());

        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestIntCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        assertTrue(instantiatorWithCreator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromLong());

        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestLongCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        assertTrue(instantiatorWithCreator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromDouble());

        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestDoubleCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        assertTrue(instantiatorWithCreator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromBoolean());

        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestBooleanCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        assertTrue(instantiatorWithCreator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateUsingDefault());

        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestDefaultCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        assertTrue(instantiatorWithCreator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateUsingDelegate());

        JavaType delegateType = createJavaType("com.TestDelegateType");
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestDelegateCreator");
        StdValueInstantiator instantiatorWithDelegate = new StdValueInstantiator(null, null, null, null, null, null, null, null, null, null, null, null, creator, delegateType, null);
        assertTrue(instantiatorWithDelegate.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateUsingArrayDelegate());

        JavaType arrayDelegateType = createJavaType("[Ljava.lang.String;");
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestArrayDelegateCreator");
        StdValueInstantiator instantiatorWithArrayDelegate = new StdValueInstantiator(null, null, null, null, null, null, null, null, null, null, null, null, null, null, creator, arrayDelegateType, null);
        assertTrue(instantiatorWithArrayDelegate.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromObjectWith());

        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestWithArgsCreator");
        SettableBeanProperty[] constructorArguments = createSettableBeanProperties(new String[]{"arg1", "arg2"});
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, null, null, creator, constructorArguments);
        assertTrue(instantiatorWithCreator.canCreateFromObjectWith());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestDefaultCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        Object result = instantiatorWithCreator.createUsingDefault(createDeserializationContext());
        assertNotNull(result);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestWithArgsCreator");
        SettableBeanProperty[] constructorArguments = createSettableBeanProperties(new String[]{"arg1", "arg2"});
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, null, null, creator, constructorArguments);
        Object[] args = new Object[]{"arg1Value", "arg2Value"};
        Object result = instantiatorWithCreator.createFromObjectWith(createDeserializationContext(), args);
        assertNotNull(result);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        JavaType delegateType = createJavaType("com.TestDelegateType");
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestDelegateCreator");
        StdValueInstantiator instantiatorWithDelegate = new StdValueInstantiator(null, null, null, null, null, null, null, null, null, null, null, null, creator, delegateType, null);
        Object delegate = createObjectOfType(delegateType);
        Object result = instantiatorWithDelegate.createUsingDelegate(createDeserializationContext(), delegate);
        assertNotNull(result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        JavaType arrayDelegateType = createJavaType("[Ljava.lang.String;");
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestArrayDelegateCreator");
        StdValueInstantiator instantiatorWithArrayDelegate = new StdValueInstantiator(null, null, null, null, null, null, null, null, null, null, null, null, null, null, creator, arrayDelegateType, null);
        Object arrayDelegate = createArrayOfStrings(new String[]{"arrayElement1", "arrayElement2"});
        Object result = instantiatorWithArrayDelegate.createUsingArrayDelegate(createDeserializationContext(), arrayDelegate);
        assertNotNull(result);
    }

    @Test
    public void testCreateFromString() throws IOException {
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestStringCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        String stringValue = "testString";
        Object result = instantiatorWithCreator.createFromString(createDeserializationContext(), stringValue);
        assertNotNull(result);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestIntCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        int intValue = 10;
        Object result = instantiatorWithCreator.createFromInt(createDeserializationContext(), intValue);
        assertNotNull(result);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestLongCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        long longValue = 10L;
        Object result = instantiatorWithCreator.createFromLong(createDeserializationContext(), longValue);
        assertNotNull(result);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestDoubleCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        double doubleValue = 10.5;
        Object result = instantiatorWithCreator.createFromDouble(createDeserializationContext(), doubleValue);
        assertNotNull(result);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        AnnotatedWithParams creator = createAnnotatedWithParams("com.TestBooleanCreator");
        StdValueInstantiator instantiatorWithCreator = new StdValueInstantiator(null, null, creator);
        boolean booleanValue = true;
        Object result = instantiatorWithCreator.createFromBoolean(createDeserializationContext(), booleanValue);
        assertNotNull(result);
    }

    private Object createDeserializationContext() {
        return null; // not used in these tests
    }

    private AnnotatedWithParams createAnnotatedWithParams(String className) {
        return new AnnotatedWithParams(createClass(className), null);
    }

    private JavaType createJavaType(String className) {
        return createContext().getTypeFactory().constructType(className);
    }

    private Class<?> createClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createObjectOfType(JavaType javaType) throws IOException {
        ObjectReader reader = createContext().getObjectMapper().getFactory().createValueFactory()
                .constructValue(javaType.getRawClass());
        return reader.getValue(createDeserializationContext(), createObjectWriter(javaType).writeValueAsString(new Object()));
    }

    private ObjectWriter createObjectWriter(JavaType javaType) {
        return createContext().getObjectMapper().writerForType(javaType);
    }

    private SettableBeanProperty[] createSettableBeanProperties(String[] propertyNames) {
        SettableBeanProperty[] properties = new SettableBeanProperty[propertyNames.length];
        for (int i = 0; i < propertyNames.length; i++) {
            properties[i] = new SettableBeanProperty(createClass(propertyNames[i]), null);
        }
        return properties;
    }

    private Object createArrayOfStrings(String[] strings) {
        return createContext().getObjectMapper().getValueFactory().createArrayBuilder(String[].class).add(strings).build();
    }

    private ObjectMapper createContext() {
        return new ObjectMapper();
    }
}