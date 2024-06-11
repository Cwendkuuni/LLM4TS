package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

    @Test
    public void testGetInstanceCreatorForPrimitiveType() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        InstanceCreator<Byte> byteCreator = new PrimitiveCreator<Byte>(Byte.TYPE);
        instanceCreators.put(Byte.TYPE, byteCreator);

        ObjectConstructor<Byte> constructor = constructorConstructor.get(TypeToken.of(Byte.class));
        assertNotNull(constructor);
        Byte instance = constructor.construct();
        assertTrue(instance instanceof Byte);
    }

    @Test
    public void testGetInstanceCreatorForString() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        InstanceCreator<String> stringCreator = new StringCreator();
        instanceCreators.put(String.class, stringCreator);

        ObjectConstructor<String> constructor = constructorConstructor.get(TypeToken.of(String.class));
        assertNotNull(constructor);
        String instance = constructor.construct();
        assertTrue(instance instanceof String);
    }

    @Test
    public void testGetInstanceCreatorForCollection() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        InstanceCreator<ArrayList<Integer>> arrayListCreator = new CollectionCreator<>(ArrayList.class);
        instanceCreators.put(ArrayList.class);
        instanceCreators.put(new TypeToken<Collection<Integer>>(){}.getType(), arrayListCreator);

        ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(TypeToken.of(ArrayList.class));
        assertNotNull(constructor);
        ArrayList<Integer> instance = constructor.construct();
        assertTrue(instance instanceof ArrayList);
    }

    @Test
    public void testGetInstanceCreatorForMap() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        InstanceCreator<LinkedHashMap<String, Object>> linkedHashMapCreator = new MapCreator<>(LinkedHashMap.class);
        instanceCreators.put(LinkedHashMap.class);
        instanceCreators.put(new TypeToken<Map<String, Object>>(){}.getType(), linkedHashMapCreator);

        ObjectConstructor<Map<String, Object>> constructor = constructorConstructor.get(TypeToken.of(Map.class));
        assertNotNull(constructor);
        Map<String, Object> instance = constructor.construct();
        assertTrue(instance instanceof Map);
    }

    @Test
    public void testGetInstanceCreatorForEnumSet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        InstanceCreator<EnumSet<DayOfWeek>> enumSetCreator = new EnumSetCreator<>(EnumSet.noneOf(DayOfWeek.class));
        instanceCreators.put(EnumSet.class);
        instanceCreators.put(new TypeToken<EnumSet<DayOfWeek>>(){}.getType(), enumSetCreator);

        ObjectConstructor<EnumSet<DayOfWeek>> constructor = constructorConstructor.get(TypeToken.of(EnumSet.class));
        assertNotNull(constructor);
        EnumSet<DayOfWeek> instance = constructor.construct();
        assertTrue(instance instanceof EnumSet);
    }

    @Test(expected = RuntimeException.class)
    public void testGetInstanceCreatorForNullType() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<Object> constructor = constructorConstructor.get(null);
    }

    @Test(expected = RuntimeException.class)
    public void testGetInstanceCreatorForUnknownType() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<Object> constructor = constructorConstructor.get(TypeToken.of(UnknownType.class));
    }
}

class PrimitiveCreator<T> implements InstanceCreator<T> {
    private final Class<T> type;

    public PrimitiveCreator(Class<T> type) {
        this.type = type;
    }

    @Override public T createInstance(Type type) {
        try {
            return type.isPrimitive() ? (T) type.getField("VALUE").get(null) : null;
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }
}

class StringCreator implements InstanceCreator<String> {
    @Override public String createInstance(Type type) {
        return "";
    }
}

class CollectionCreator<T> implements InstanceCreator<Collection<T>> {
    private final Class<Collection<T>> collectionType;

    public CollectionCreator(Class<Collection<T>> collectionType) {
        this.collectionType = collectionType;
    }

    @Override public Collection<T> createInstance(Type type) {
        return collectionType.newInstance();
    }
}

class MapCreator<K, V> implements InstanceCreator<Map<K, V>> {
    private final Class<Map<K, V>> mapType;

    public MapCreator(Class<Map<K, V>> mapType) {
        this.mapType = mapType;
    }

    @Override public Map<K, V> createInstance(Type type) {
        return mapType.newInstance();
    }
}

class EnumSetCreator<E extends Enum> implements InstanceCreator<EnumSet<E>> {
    private final EnumSet<E> emptySet;

    public EnumSetCreator(EnumSet<E> emptySet) {
        this.emptySet = emptySet;
    }

    @Override public EnumSet<E> createInstance(Type type) {
        if (type instanceof ParameterizedType) {
            Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (elementType instanceof Class) {
                return EnumSet.copyOf(EnumSet.allOf((Class<E>)elementType));
            } else {
                throw new RuntimeException("Invalid EnumSet type: " + type.toString());
            }
        } else {
            throw new RuntimeException("Invalid EnumSet type: " + type.toString());
        }
    }
}

class UnknownType {}

enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

class UnsafeAllocator {
    private static final Unsafe UNSAFE;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
        } catch (SecurityException e) {
            throw newExceptionInInitializeError("Could not get UNSAFE", e);
        }
    }

    public static UnsafeAllocator create() {
        return new UnsafeAllocator();
    }

    private UnsafeAllocator() {}

    public Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return UNSAFE.allocateInstance(clazz);
    }
}