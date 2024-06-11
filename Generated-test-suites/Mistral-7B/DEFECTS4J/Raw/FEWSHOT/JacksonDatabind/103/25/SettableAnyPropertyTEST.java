package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class SettableAnyPropertyTest extends BaseTest {

    @Test
    public void testSettableAnyPropertyCreation() {
        BeanProperty property = mockBeanProperty();
        AnnotatedMember setter = mockSetter();
        JavaType type = mockJavaType();
        KeyDeserializer keyDeserializer = mockKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mockValueDeserializer();
        TypeDeserializer valueTypeDeserializer = mockTypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertEquals(keyDeserializer, settableAnyProperty.getKeyDeserializer());
        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertEquals(valueTypeDeserializer, settableAnyProperty.getValueTypeDeserializer());
        assertFalse(settableAnyProperty.isSetterIsField());

        SettableAnyProperty settableAnyPropertyIsField = new SettableAnyProperty(property, setter, type, valueDeserializer, valueTypeDeserializer);
        assertTrue(settableAnyPropertyIsField.isSetterIsField());
    }

    @Test
    public void testSettableAnyPropertyWithSetterIsField() throws IOException {
        BeanProperty property = mockBeanProperty();
        AnnotatedField setter = mockSetterField();
        JavaType type = mockJavaType();
        JsonDeserializer<Object> valueDeserializer = mockValueDeserializer();
        TypeDeserializer valueTypeDeserializer = mockTypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        Map<Object, Object> map = new HashMap<>();

        settableAnyProperty.set(instance, "propertyName", "value");
        assertEquals("value", map.get("propertyName"));
    }

    @Test
    public void testSettableAnyPropertyWithoutSetterIsField() throws IOException {
        BeanProperty property = mockBeanProperty();
        AnnotatedMethod setter = mockSetterMethod();
        JavaType type = mockJavaType();
        JsonDeserializer<Object> valueDeserializer = mockValueDeserializer();
        TypeDeserializer valueTypeDeserializer = mockTypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "propertyName";

        settableAnyProperty.deserializeAndSet(createMockParser(), createMockContext(), instance, propName);
        verifySetterCall(instance, propName, "value");
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() throws IOException {
        BeanProperty property = mockBeanProperty();
        AnnotatedMember setter = mockSetter();
        JavaType type = mockJavaType();
        KeyDeserializer keyDeserializer = mockKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mockValueDeserializer();
        TypeDeserializer valueTypeDeserializer = mockTypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "propertyName";
        String expectedValue = "expectedValue";

        settableAnyProperty.deserializeAndSet(createMockParser(), createMockContext(), instance, propName);
        verify(valueDeserializer).deserialize(createMockParser(), createMockContext());
        assertEquals(expectedValue, valueDeserializer.deserialize(createMockParser(), createMockContext()));
        verifySetterCall(instance, propName, expectedValue);
    }

    @Test
    public void testSettableAnyPropertyWithTypeDeserializer() throws IOException {
        BeanProperty property = mockBeanProperty();
        AnnotatedMember setter = mockSetter();
        JavaType type = mockJavaType();
        KeyDeserializer keyDeserializer = mockKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mockValueDeserializer();
        TypeDeserializer valueTypeDeserializer = mockTypeDeserializer();

        when(valueTypeDeserializer.deserializeValue(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(new Object());

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "propertyName";

        settableAnyProperty.deserializeAndSet(createMockParser(), createMockContext(), instance, propName);
        verify(valueTypeDeserializer).deserializeValue(createMockParser(), createMockContext());
        assertEquals(new Object(), settableAnyProperty.deserialize(createMockParser(), createMockContext()));
        verifySetterCall(instance, propName, new Object());
    }

    @Test
    public void testSettableAnyPropertyWithKeyDeserializer() throws IOException {
        BeanProperty property = mockBeanProperty();
        AnnotatedMember setter = mockSetter();
        JavaType type = mockJavaType();
        KeyDeserializer keyDeserializer = mockKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mockValueDeserializer();
        TypeDeserializer valueTypeDeserializer = mockTypeDeserializer();

        when(keyDeserializer.deserializeKey("propertyName", createMockContext())).thenReturn("keyValue");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "propertyName";

        settableAnyProperty.deserializeAndSet(createMockParser(), createMockContext(), instance, propName);
        assertEquals("keyValue", settableAnyProperty.getKeyDeserializer().deserializeKey(propName, createMockContext()));
        verifySetterCall(instance, propName, "value");
    }

    @Test
    public void testSettableAnyPropertyWithNullValueDeserializer() throws IOException {
        BeanProperty property = mockBeanProperty();
        AnnotatedMember setter = mockSetter();
        JavaType type = mockJavaType();
        KeyDeserializer keyDeserializer = mockKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = mockTypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "propertyName";

        settableAnyProperty.deserializeAndSet(createMockParser(), createMockContext(), instance, propName);
        assertNull(valueDeserializer);
    }

    @Test
    public void testSettableAnyPropertyWithUnresolvedForwardReference() throws IOException {
        BeanProperty property = mockBeanProperty();
        AnnotatedMember setter = mockSetter();
        JavaType type = mockJavaType();
        KeyDeserializer keyDeserializer = mockKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mockValueDeserializer();
        TypeDeserializer valueTypeDeserializer = mockTypeDeserializer();

        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "propertyName";

        settableAnyProperty.deserializeAndSet(createMockParser(), createMockContext(), instance, propName);

        AnySetterReferring referring = new AnySetterReferring(settableAnyProperty, reference, type, instance, propName);
        verify(reference).getRoid().appendReferring(referring);
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return mock(DeserializationContext.class);
    }

    private BeanProperty mockBeanProperty() {
        return mock(BeanProperty.class);
    }

    private AnnotatedMember mockSetter() {
        return mock(AnnotatedMember.class);
    }

    private AnnotatedField mockSetterField() {
        AnnotatedField annField = mock(AnnotatedField.class);
        when(annField.getValue(any(Object.class))).thenReturn(new HashMap<>());
        return annField;
    }

    private AnnotatedMethod mockSetterMethod() {
        return mock(AnnotatedMethod.class);
    }

    private JsonDeserializer<Object> mockValueDeserializer() {
        return mock(JsonDeserializer.class);
    }

    private TypeDeserializer mockTypeDeserializer() {
        return mock(TypeDeserializer.class);
    }

    private JavaType mockJavaType() {
        return mock(JavaType.class);
    }

    private KeyDeserializer mockKeyDeserializer() {
        return mock(KeyDeserializer.class);
    }

    private void verifySetterCall(Object instance, String propName, Object value) throws IOException {
        AnnotatedMethod setter = mock(AnnotatedMethod.class);
        when(setter.callOnWith(instance, propName, any(Object.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArgs();
            Object argumentPropName = args[1];
            Object argumentValue = args[2];
            assertEquals(propName, argumentPropName);
            assertEquals(value, argumentValue);
            return null;
        });

        when(getProperty().getMember()).thenReturn(setter);
    }
}