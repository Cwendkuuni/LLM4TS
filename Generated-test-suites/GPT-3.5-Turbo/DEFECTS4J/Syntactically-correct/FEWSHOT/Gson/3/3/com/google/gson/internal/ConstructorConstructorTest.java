package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class ConstructorConstructorTest {

    private static final Class<?> STRING_CLASS = String.class;
    private static final Class<?> COLLECTION_CLASS = Collection.class;
    private static final Class<?> MAP_CLASS = Map.class;

    @Test
    public void testConstructorConstructor() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        assertNotNull(constructor);
    }

    @Test
    public void testGetWithInstanceCreator() {
        InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
            public String createInstance(Type type) {
                return "Test String";
            }
        };

        LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        instanceCreators.put(STRING_CLASS, instanceCreator);
        ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<String> objectConstructor = constructor.get(TypeToken.get(STRING_CLASS));
        assertNotNull(objectConstructor);
        assertEquals("Test String", objectConstructor.construct());
    }

    @Test
    public void testGetWithRawTypeInstanceCreator() {
        InstanceCreator<Collection<String>> instanceCreator = new InstanceCreator<Collection<String>>() {
            public Collection<String> createInstance(Type type) {
                return new ArrayList<String>();
            }
        };

        LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        instanceCreators.put(COLLECTION_CLASS, instanceCreator);
        ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<Collection<String>> objectConstructor = constructor.get(TypeToken.get(COLLECTION_CLASS));
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(ArrayList.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGetWithDefaultConstructor() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<String> objectConstructor = constructor.get(TypeToken.get(STRING_CLASS));
        assertNotNull(objectConstructor);

        try {
            String s = objectConstructor.construct();
            assertNotNull(s);
            assertEquals("", s);
        } catch (RuntimeException e) {
            assertNull(e);
        }
    }

    @Test
    public void testGetWithDefaultImplementationConstructor() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<Set<String>> objectConstructor = constructor.get(TypeToken.get(Set.class));
        assertNotNull(objectConstructor);

        try {
            Set<String> set = objectConstructor.construct();
            assertNotNull(set);
            assertEquals(LinkedHashSet.class, set.getClass());
        } catch (RuntimeException e) {
            assertNull(e);
        }

        constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        objectConstructor = constructor.get(TypeToken.get(TreeSet.class));
        assertNotNull(objectConstructor);

        try {
            Set<String> set = objectConstructor.construct();
            assertNotNull(set);
            assertEquals(TreeSet.class, set.getClass());
        } catch (RuntimeException e) {
            assertNull(e);
        }

        constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        objectConstructor = constructor.get(TypeToken.get(SortedSet.class));
        assertNotNull(objectConstructor);

        try {
            Set<String> set = objectConstructor.construct();
            assertNotNull(set);
            assertEquals(TreeSet.class, set.getClass());
        } catch (RuntimeException e) {
            assertNull(e);
        }
    }

    @Test
    public void testGetWithUnsafeAllocator() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<String> objectConstructor = constructor.get(TypeToken.get(STRING_CLASS));
        assertNotNull(objectConstructor);

        try {
            String s = objectConstructor.construct();
            assertNotNull(s);
            assertEquals(String.class, s.getClass());
        } catch (RuntimeException e) {
            assertNull(e);
        }
    }

    @Test
    public void testNewDefaultConstructor() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<String> objectConstructor = constructor.newDefaultConstructor(String.class);
        assertNotNull(objectConstructor);

        try {
            String s = objectConstructor.construct();
            assertNotNull(s);
            assertEquals("", s);
        } catch (RuntimeException e) {
            assertNull(e);
        }
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<Set<String>> objectConstructor = constructor.newDefaultImplementationConstructor(Set.class, Set.class);
        assertNotNull(objectConstructor);

        try {
            Set<String> set = objectConstructor.construct();
            assertNotNull(set);
            assertEquals(LinkedHashSet.class, set.getClass());
        } catch (RuntimeException e) {
            assertNull(e);
        }

        constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        objectConstructor = constructor.newDefaultImplementationConstructor(Map.class, Map.class);
        assertNotNull(objectConstructor);

        try {
            Set<String> set = objectConstructor.construct();
            assertNotNull(set);
            assertEquals(LinkedHashMap.class, set.getClass());
        } catch (RuntimeException e) {
            assertNull(e);
        }
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<String> objectConstructor = constructor.newUnsafeAllocator(String.class, String.class);
        assertNotNull(objectConstructor);

        try {
            String s = objectConstructor.construct();
            assertNotNull(s);
            assertEquals(String.class, s.getClass());
        } catch (RuntimeException e) {
            assertNull(e);
        }
    }

    @Test
    public void testToString() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        assertEquals("{}", constructor.toString());
    }
    
}