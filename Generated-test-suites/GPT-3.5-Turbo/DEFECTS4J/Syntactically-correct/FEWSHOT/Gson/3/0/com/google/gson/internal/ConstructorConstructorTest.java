package com.google.gson.internal;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorConstructorTest {
    @Test
    public void testGet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

        assertNull(constructorConstructor.get(null));

        ObjectConstructor<Object> defaultConstructor = null;
        ObjectConstructor<Object> defaultImplementation = null;
        ObjectConstructor<Object> unsafeAllocator = null;

        try {
            defaultConstructor = constructorConstructor.newDefaultConstructor(Object.class);
            defaultImplementation = constructorConstructor.newDefaultImplementationConstructor(null, Object.class);
            unsafeAllocator = constructorConstructor.newUnsafeAllocator(null, Object.class);
        } catch (Throwable e) {
            fail();
        }

        assertNotNull(defaultConstructor);
        assertNotNull(defaultImplementation);
        assertNotNull(unsafeAllocator);
    }

    @Test
    public void testNewDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

        ObjectConstructor<Object> defaultConstructor = constructorConstructor.newDefaultConstructor(Object.class);

        try {
            Object object = defaultConstructor.construct();
            assertNotNull(object);
            assertTrue(object.getClass().equals(Object.class));
        } catch (Throwable e) {
            fail();
        }

        defaultConstructor = constructorConstructor.newDefaultConstructor(PrivateConstructor.class);

        try {
            Object object = defaultConstructor.construct();
            assertNotNull(object);
            assertTrue(object.getClass().equals(PrivateConstructor.class));
        } catch (Throwable e) {
            fail();
        }

        defaultConstructor = constructorConstructor.newDefaultConstructor(DefaultConstructor.class);

        try {
            Object object = defaultConstructor.construct();
            assertNotNull(object);
            assertTrue(object.getClass().equals(DefaultConstructor.class));
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

        ObjectConstructor<Object> defaultImplementation = constructorConstructor.newDefaultImplementationConstructor(null, Object.class);

        try {
            Object object = defaultImplementation.construct();
            assertNotNull(object);
            assertTrue(object.getClass().equals(ArrayList.class));
        } catch (Throwable e) {
            fail();
        }

        defaultImplementation = constructorConstructor.newDefaultImplementationConstructor(null, Map.class);

        try {
            Object object = defaultImplementation.construct();
            assertNotNull(object);
            assertTrue(object.getClass().equals(LinkedHashMap.class));
        } catch (Throwable e) {
            fail();
        }

        defaultImplementation = constructorConstructor.newDefaultImplementationConstructor(null, Set.class);

        try {
            Object object = defaultImplementation.construct();
            assertNotNull(object);
            assertTrue(object.getClass().equals(LinkedHashSet.class));
        } catch (Throwable e) {
            fail();
        }

        defaultImplementation = constructorConstructor.newDefaultImplementationConstructor(null, Queue.class);

        try {
            Object object = defaultImplementation.construct();
            assertNotNull(object);
            assertTrue(object.getClass().equals(LinkedList.class));
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

        ObjectConstructor<Object> unsafeAllocator = constructorConstructor.newUnsafeAllocator(null, Object.class);

        try {
            Object object = unsafeAllocator.construct();
            assertNotNull(object);
        } catch (Throwable e) {
            fail();
        }

        unsafeAllocator = constructorConstructor.newUnsafeAllocator(null, PrivateConstructor.class);

        try {
            Object object = unsafeAllocator.construct();
            assertNotNull(object);
        } catch (Throwable e) {
            fail();
        }

        unsafeAllocator = constructorConstructor.newUnsafeAllocator(null, DefaultConstructor.class);

        try {
            Object object = unsafeAllocator.construct();
            assertNotNull(object);
        } catch (Throwable e) {
            fail();
        }
    }

    private static class PrivateConstructor {
        private PrivateConstructor() {}
    }

    private static class DefaultConstructor {
        public DefaultConstructor() {}
    }
}