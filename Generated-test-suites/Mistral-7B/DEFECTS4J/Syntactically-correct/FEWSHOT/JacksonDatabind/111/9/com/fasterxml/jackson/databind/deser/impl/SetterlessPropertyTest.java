package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.TestBase;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import java.io.IOException;
import java.lang.reflect.Method;

public class SetterlessPropertyTest extends TestBase {

    private static final BeanPropertyDefinition PROP_DEF = new BeanPropertyDefinition() {
        @Override
        public String getName() {
            return "testProperty";
        }

        @Override
        public Class<?> getDeclaringClass() {
            return Object.class;
        }

        @Override
        public PropertyType getPropertyType() {
            return null;
        }
    };

    private static final JavaType TYPE = ObjectType.construct(Object.class);

    @Test
    public void testSetterlessProperty() throws Exception {
        Object instance = new Object();
        TypeDeserializer typeDeserializer = null;
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, typeDeserializer, Annotations.empty(),
                getTestAnnotatedMethod("setTestProperty", Object.class, instance.getClass()));

        JsonParser p = createParser(getTestObjectMapper(), "[1, 2, 3]");
        p.nextToken();
        property.deserializeAndSet(p, getTestContext(), instance);
        assertEquals("[1, 2, 3]", Arrays.toString((List) instance.getClass().getMethod("getTestProperty").invoke(instance)));
    }

    @Test
    public void testGetAnnotation() throws Exception {
        AnnotatedMethod method = getTestAnnotatedMethod("testAnnotation", void.class, Object.class);
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(), method);
        assertEquals(MyAnnotation.class, property.getAnnotation(MyAnnotation.class));
    }

    @Test
    public void testFixAccess() throws Exception {
        SetterlessProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(),
                getTestAnnotatedMethod("testFixAccess", void.class, Object.class));
        property.fixAccess(getTestContext().getDefaultConfig());
        assertFalse(property.getAnnotated().isSynthetic());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(),
                getTestAnnotatedMethod("testSet", void.class, Object.class));
        property.set(new Object(), new Object());
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws Exception {
        Object instance = new Object();
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(),
                getTestAnnotatedMethod("testDeserializeAndSetWithNullValue", void.class, Object.class));

        JsonParser p = createParser(getTestObjectMapper(), "null");
        property.deserializeAndSet(p, getTestContext(), instance);
        assertNull(instance.getClass().getMethod("getTestProperty").invoke(instance));
    }

    @Test
    public void testDeserializeAndSetWithValue() throws Exception {
        Object instance = new Object();
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(),
                getTestAnnotatedMethod("testDeserializeAndSetWithValue", void.class, Object.class));

        JsonParser p = createParser(getTestObjectMapper(), "[1, 2, 3]");
        property.deserializeAndSet(p, getTestContext(), instance);
        assertEquals("[1, 2, 3]", Arrays.toString((List) instance.getClass().getMethod("getTestProperty").invoke(instance)));
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws Exception {
        Object instance = new Object();
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(),
                getTestAnnotatedMethod("testDeserializeSetAndReturnWithNullValue", void.class, Object.class));

        JsonParser p = createParser(getTestObjectMapper(), "null");
        Object result = property.deserializeSetAndReturn(p, getTestContext(), instance);
        assertNull(instance.getClass().getMethod("getTestProperty").invoke(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithValue() throws Exception {
        Object instance = new Object();
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(),
                getTestAnnotatedMethod("testDeserializeSetAndReturnWithValue", void.class, Object.class));

        JsonParser p = createParser(getTestObjectMapper(), "[1, 2, 3]");
        Object result = property.deserializeSetAndReturn(p, getTestContext(), instance);
        assertEquals("[1, 2, 3]", Arrays.toString((List) instance.getClass().getMethod("getTestProperty").invoke(instance)));
        assertEquals(instance, result);
    }

    @Test
    public void testWithName() throws Exception {
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(),
                getTestAnnotatedMethod("testProperty", void.class, Object.class));
        assertEquals("testProperty", property.getName());
        SettableBeanProperty newProperty = property.withName("newName");
        assertEquals("newName", newProperty.getName());
        assertNotSame(property, newProperty);
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        JsonDeserializer<List> valueDeserializer = new StandardDeserializer<>(List.class);
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(),
                getTestAnnotatedMethod("testProperty", void.class, Object.class));
        SettableBeanProperty newProperty = property.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, newProperty.getValueDeserializer());
        assertNotSame(property, newProperty);
    }

    @Test
    public void testWithNullProvider() throws Exception {
        NullValueProvider nva = new StandardNullValueProvider();
        SettableBeanProperty property = new SetterlessProperty(PROP_DEF, TYPE, null, Annotations.empty(),
                getTestAnnotatedMethod("testProperty", void.class, Object.class));
        SettableBeanProperty newProperty = property.withNullProvider(nva);
        assertEquals(nva, newProperty.getNullProvider());
        assertNotSame(property, newProperty);
    }

    private Method getTestAnnotatedMethod(String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        return Object.class.getMethod(name, parameterTypes);
    }

}