import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {
    @Test
    public void testGet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        TypeToken<Integer> integerTypeToken = TypeToken.get(Integer.class);
        ObjectConstructor<Integer> integerConstructor = constructorConstructor.get(integerTypeToken);
        assertNull(integerConstructor);

        TypeToken<Collection<String>> collectionTypeToken = new TypeToken<Collection<String>>() {};
        ObjectConstructor<Collection<String>> collectionConstructor = constructorConstructor.get(collectionTypeToken);
        assertNotNull(collectionConstructor);

        TypeToken<SortedSet<Long>> sortedSetTypeToken = new TypeToken<SortedSet<Long>>() {};
        ObjectConstructor<SortedSet<Long>> sortedSetConstructor = constructorConstructor.get(sortedSetTypeToken);
        assertNotNull(sortedSetConstructor);

        TypeToken<EnumSet<TimeUnit>> enumSetTypeToken = new TypeToken<EnumSet<TimeUnit>>() {};
        ObjectConstructor<EnumSet<TimeUnit>> enumSetConstructor = constructorConstructor.get(enumSetTypeToken);
        assertNotNull(enumSetConstructor);

        TypeToken<Set<String>> setTypeToken = new TypeToken<Set<String>>() {};
        ObjectConstructor<Set<String>> setConstructor = constructorConstructor.get(setTypeToken);
        assertNotNull(setConstructor);

        TypeToken<Queue<Double>> queueTypeToken = new TypeToken<Queue<Double>>() {};
        ObjectConstructor<Queue<Double>> queueConstructor = constructorConstructor.get(queueTypeToken);
        assertNotNull(queueConstructor);

        TypeToken<Map<Integer, String>> mapTypeToken = new TypeToken<Map<Integer, String>>() {};
        ObjectConstructor<Map<Integer, String>> mapConstructor = constructorConstructor.get(mapTypeToken);
        assertNotNull(mapConstructor);

        TypeToken<SortedMap<Float, Double>> sortedMapTypeToken = new TypeToken<SortedMap<Float, Double>>() {};
        ObjectConstructor<SortedMap<Float, Double>> sortedMapConstructor = constructorConstructor.get(sortedMapTypeToken);
        assertNotNull(sortedMapConstructor);

        TypeToken<Map<Double, Integer>> invalidMapTypeToken = new TypeToken<Map<Double, Integer>>() {};
        ObjectConstructor<Map<Double, Integer>> invalidMapConstructor = constructorConstructor.get(invalidMapTypeToken);
        assertNull(invalidMapConstructor);

        TypeToken<List<Boolean>> defaultConstructorToken = new TypeToken<List<Boolean>>() {};
        ObjectConstructor<List<Boolean>> defaultConstructor = constructorConstructor.get(defaultConstructorToken);
        assertNotNull(defaultConstructor);

        TypeToken<List<Character>> defaultImplementationToken = new TypeToken<List<Character>>() {};
        ObjectConstructor<List<Character>> defaultImplementation = constructorConstructor.get(defaultImplementationToken);
        assertNotNull(defaultImplementation);

        TypeToken<List<Object>> unsafeAllocatorToken = new TypeToken<List<Object>>() {};
        ObjectConstructor<List<Object>> unsafeAllocator = constructorConstructor.get(unsafeAllocatorToken);
        assertNotNull(unsafeAllocator);
    }

    @Test
    public void testNewDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        Class<Integer> rawType = Integer.class;
        ObjectConstructor<Integer> defaultConstructor = constructorConstructor.newDefaultConstructor(rawType);
        assertNull(defaultConstructor);

        rawType = MyClass.class;
        defaultConstructor = constructorConstructor.newDefaultConstructor(rawType);
        assertNotNull(defaultConstructor);
    }

    private static class MyClass {
        private MyClass() {}
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        Type type = new TypeToken<Collection<String>>() {}.getType();
        Class<Collection<String>> rawType = (Class<Collection<String>>) new TypeToken<Collection<String>>() {}.getRawType();
        ObjectConstructor<Collection<String>> defaultImplementation = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
        assertNotNull(defaultImplementation);

        type = new TypeToken<Map<Integer, String>>() {}.getType();
        rawType = (Class<Map<Integer, String>>) new TypeToken<Map<Integer, String>>() {}.getRawType();
        defaultImplementation = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
        assertNotNull(defaultImplementation);

        type = new TypeToken<Map<Double, Integer>>() {}.getType();
        rawType = (Class<Map<Double, Integer>>) new TypeToken<Map<Double, Integer>>() {}.getRawType();
        defaultImplementation = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
        assertNull(defaultImplementation);
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        Type type = new TypeToken<List<Integer>>() {}.getType();
        Class<List<Integer>> rawType = (Class<List<Integer>>) new TypeToken<List<Integer>>() {}.getRawType();
        ObjectConstructor<List<Integer>> unsafeAllocator = constructorConstructor.newUnsafeAllocator(type, rawType);
        assertNotNull(unsafeAllocator);

        type = new TypeToken<Map<String, Object>>() {}.getType();
        rawType = (Class<Map<String, Object>>) new TypeToken<Map<String, Object>>() {}.getRawType();
        unsafeAllocator = constructorConstructor.newUnsafeAllocator(type, rawType);
        assertNotNull(unsafeAllocator);
    }
}