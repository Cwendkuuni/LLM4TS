package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

import org.junit.*;
import org.mockito.*;

public class SettableAnyPropertyTest {

    @Before
    public void setup() {
        // Initialize mocks here, if needed
    }

    @Test
    public void testSettableAnyPropertyConstructor() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty._setter);
        assertEquals(type, settableAnyProperty.getType());
        assertEquals(keyDeserializer, settableAnyProperty._keyDeserializer);
        assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
        assertEquals(valueTypeDeserializer, settableAnyProperty._valueTypeDeserializer);
        assertFalse(settableAnyProperty._setterIsField);
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(new JsonDeserializer<>());

        assertEquals(settableAnyProperty.getClass(), settableAnyProperty.getClass());
        assertNotSame(settableAnyProperty, newSettableAnyProperty);
        assertEquals(newSettableAnyProperty.getProperty(), settableAnyProperty.getProperty());
        assertEquals(newSettableAnyProperty._setter, settableAnyProperty._setter);
        assertEquals(newSettableAnyProperty.getType(), settableAnyProperty.getType());
        assertEquals(newSettableAnyProperty._keyDeserializer, settableAnyProperty._keyDeserializer);
        assertEquals(newSettableAnyProperty._valueDeserializer, newJsonDeserializer());
        assertEquals(newSettableAnyProperty._valueTypeDeserializer, settableAnyProperty._valueTypeDeserializer);
        assertTrue(newSettableAnyProperty._setterIsField);
    }

    @Test
    public void testFixAccess() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        DeserializationConfig config = mock(DeserializationConfig.class);
        settableAnyProperty.fixAccess(config);
        verify(setter).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullInstance() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        Object instance = null;

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, "propName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullPropName() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        Object instance = new Object();

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, null);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        Object instance = new Object();
        String propName = "propName";

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(valueDeserializer.getObjectIdReader()).thenReturn(new ObjectIdReader());

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(valueDeserializer).getNullValue(ctxt);
    }

    @Test
    public void testDeserialize() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        Object result = settableAnyProperty.deserialize(p, ctxt);

        assertEquals(valueDeserializer.getNullValue(ctxt), result);
    }

    @Test
    public void testSet() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        settableAnyProperty.set(instance, propName, value);

        if (setter instanceof AnnotatedField) {
            AnnotatedField field = (AnnotatedField) setter;
            Map<Object, Object> map = (Map<Object, Object>) field.getValue(instance);
            assertEquals(value, map.get(propName));
        } else {
            assertNotNull(setter.callOnWith(instance, propName, value));
        }
    }

    @Test(expected = IOException.class)
    public void testThrowAsIOE() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        Exception e = new IOException("Exception message");

        settableAnyProperty._throwAsIOE(e, propName, value);
    }

    @After
    public void teardown() {
        // Release mocks here, if needed
    }

    private JsonDeserializer<Object> newJsonDeserializer() {
        return mock(JsonDeserializer.class);
    }

    @Test
    public void testToString() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        assertEquals("[any property on class " + getClassName(settableAnyProperty._setter) + "]", settableAnyProperty.toString());
    }

    private String getClassName(AnnotatedMember annotatedMember) {
        return annotatedMember.getDeclaringClass().getName();
    }
}