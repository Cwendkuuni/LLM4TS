package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.MapType.TypeBindingsBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MapType.class)
public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType keyType = Mockito.mock(JavaType.class);
        JavaType valueType = Mockito.mock(JavaType.class);

        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithTypeBindingsBuilder() {
        Class<?> rawType = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType keyType = Mockito.mock(JavaType.class);
        JavaType valueType = Mockito.mock(JavaType.class);

        TypeBindingsBuilder builder = PowerMockito.mockStatic(TypeBindings.class);
        PowerMockito.whenNew(TypeBindings.class).withAnyArguments().thenReturn(bindings);

        MapType mapType = MapType.construct(rawType, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType keyType = Mockito.mock(JavaType.class);
        JavaType valueType = Mockito.mock(JavaType.class);
        Object valueHandler = Mockito.mock(Object.class);

        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType mapTypeWithHandler = mapType.withTypeHandler(valueHandler);

        assertNotNull(mapTypeWithHandler);
        assertEquals(mapType.getRawClass(), mapTypeWithHandler.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithHandler.getBindings());
        assertEquals(mapType.getKeyType(), mapTypeWithHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithHandler.getValueType());
        assertEquals(valueHandler, mapTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testToString() {
        Class<?> rawType = Mockito.mock(Class.class);
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType keyType = Mockito.mock(JavaType.class);
        JavaType valueType = Mockito.mock(JavaType.class);

        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        String toString = mapType.toString();

        assertEquals("[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + "]", toString);
    }
}