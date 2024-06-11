package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreatorPropertyTest {

    private ObjectMapper objectMapper;
    private CreatorProperty property;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                "testValueId",
                null
        );
        property.setValueDeserializer(stringDeserializer);
    }

    @Test
    public void testSerializable() throws Exception {
        assertEquals(getClass(), property.getClass());
    }

    @Test
    public void testGetInjectableValueId() throws Exception {
        assertEquals("testValueId", property.getInjectableValueId());
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        when(objectMapper.findInjectableValue(eq("testValueId"), any(CreatorProperty.class), any()))
                .thenReturn("injectedValue");

        Object injectedValue = property.findInjectableValue(objectMapper, new Object());
        assertEquals("injectedValue", injectedValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueWithNullInjectableValueId() throws Exception {
        property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null
        );
        property.findInjectableValue(objectMapper, new Object());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueWithNullProperty() throws Exception {
        Object injectedValue = property.findInjectableValue(objectMapper, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueWithNullObject() throws Exception {
        property.findInjectableValue(objectMapper, null);
    }

    @Test
    public void testInject() throws Exception {
        when(objectMapper.findInjectableValue(eq("testValueId"), any(CreatorProperty.class), any()))
                .thenReturn("injectedValue");

        Object injectedObject = new Object();
        property.inject(objectMapper, injectedObject);

        assertEquals("injectedValue", property.get(injectedObject));
    }

    @Test(expected = IOException.class)
    public void testInjectWithNullProperty() throws Exception {
        property.inject(objectMapper, null);
    }

    @Test
    public void testIsIgnorable() throws Exception {
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testIsIgnorableAfterConstruction() throws Exception {
        CreatorProperty ignoredProperty = new CreatorProperty(
                PropertyName.construct("ignoredProperty"),
                JavaType.construct(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null
        );
        ignoredProperty.markAsIgnorable();
        assertFalse(ignoredProperty.isIgnorable());
    }

    @Test
    public void testSetFallbackSetter() throws Exception {
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);

        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFallbackSetterWithNullProperty() throws Exception {
        property.setFallbackSetter(null);
    }
}