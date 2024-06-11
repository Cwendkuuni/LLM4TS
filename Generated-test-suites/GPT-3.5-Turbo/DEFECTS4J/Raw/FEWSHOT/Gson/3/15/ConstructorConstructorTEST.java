package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

    private ConstructorConstructor constructorConstructor;

    @Test
    public void testGetWithInstanceCreator() {
        final InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
            @Override
            public String createInstance(Type type) {
                return "Test";
            }
        };

        constructorConstructor = new ConstructorConstructor(Map.of(TypeToken.get(String.class).getType(), instanceCreator));

        ObjectConstructor<String> objectConstructor = constructorConstructor.get(TypeToken.get(String.class));

        assertNotNull(objectConstructor);
        assertEquals("Test", objectConstructor.construct());
    }

    @Test
    public void testGetWithDefaultConstructor() {
        constructorConstructor = new ConstructorConstructor(Map.of());

        ObjectConstructor<String> objectConstructor = constructorConstructor.get(TypeToken.get(String.class));

        assertNotNull(objectConstructor);
        assertEquals("", objectConstructor.construct());
    }

    @Test
    public void testGetWithDefaultImplementationConstructor() {
        constructorConstructor = new ConstructorConstructor(Map.of());

        ObjectConstructor<Collection<String>> objectConstructor = constructorConstructor.get(TypeToken
                .getParameterized(Collection.class, String.class));

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGetWithUnsafeAllocator() {
        constructorConstructor = new ConstructorConstructor(Map.of());

        ObjectConstructor<String> objectConstructor = constructorConstructor.get(TypeToken.get(String.class));

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }
}