package com.google.gson.internal;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

public class ConstructorConstructorTest {
    @Test
    public void testGet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
        TypeToken<String> typeToken = new TypeToken<String>(){};
        ObjectConstructor<String> objectConstructor = constructor.get(typeToken);
        assertNull(objectConstructor);
        
        instanceCreators.put(String.class, new InstanceCreator<String>() {
            @Override
            public String createInstance(Type type) {
                return "test";
            }
        });
        objectConstructor = constructor.get(typeToken);
        assertNotNull(objectConstructor);
        
        TypeToken<ArrayList<String>> listTypeToken = new TypeToken<ArrayList<String>>(){};
        objectConstructor = constructor.get(listTypeToken);
        assertNotNull(objectConstructor);
        
        instanceCreators.put(ArrayList.class, new InstanceCreator<ArrayList<String>>() {
            @Override
            public ArrayList<String> createInstance(Type type) {
                return new ArrayList<>();
            }
        });
        objectConstructor = constructor.get(listTypeToken);
        assertNotNull(objectConstructor);
        
        TypeToken<Map<String, Integer>> mapTypeToken = new TypeToken<Map<String, Integer>>(){};
        objectConstructor = constructor.get(mapTypeToken);
        assertNotNull(objectConstructor);
        
        instanceCreators.put(Map.class, new InstanceCreator<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> createInstance(Type type) {
                return new LinkedHashMap<>();
            }
        });
        objectConstructor = constructor.get(mapTypeToken);
        assertNotNull(objectConstructor);
        
        TypeToken<EnumSet<Days>> enumSetTypeToken = new TypeToken<EnumSet<Days>>(){};
        objectConstructor = constructor.get(enumSetTypeToken);
        assertNotNull(objectConstructor);
        
        instanceCreators.put(EnumSet.class, new InstanceCreator<EnumSet<Days>>() {
            @Override
            public EnumSet<Days> createInstance(Type type) {
                return EnumSet.noneOf(Days.class);
            }
        });
        objectConstructor = constructor.get(enumSetTypeToken);
        assertNotNull(objectConstructor);
        
        TypeToken<SortedMap<String, Integer>> sortedMapTypeToken = new TypeToken<SortedMap<String, Integer>>(){};
        objectConstructor = constructor.get(sortedMapTypeToken);
        assertNotNull(objectConstructor);
        
        instanceCreators.put(SortedMap.class, new InstanceCreator<SortedMap<String, Integer>>() {
            @Override
            public SortedMap<String, Integer> createInstance(Type type) {
                return new TreeMap<>();
            }
        });
        objectConstructor = constructor.get(sortedMapTypeToken);
        assertNotNull(objectConstructor);
        
        TypeToken<Object> objectTypeToken = new TypeToken<Object>(){};
        objectConstructor = constructor.get(objectTypeToken);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testNewDefaultConstructor() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<>());
        Class<String> rawType = String.class;
        ObjectConstructor<String> objectConstructor = constructor.newDefaultConstructor(rawType);
        assertNull(objectConstructor);
        
        rawType = ArrayList.class;
        objectConstructor = constructor.newDefaultConstructor(rawType);
        assertNull(objectConstructor);
        
        rawType = LinkedList.class;
        objectConstructor = constructor.newDefaultConstructor(rawType);
        assertNull(objectConstructor);
        
        rawType = Map.class;
        objectConstructor = constructor.newDefaultConstructor(rawType);
        assertNull(objectConstructor);
        
        rawType = TreeMap.class;
        objectConstructor = constructor.newDefaultConstructor(rawType);
        assertNull(objectConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<>());
        Type type = ArrayList.class;
        Class<ArrayList<String>> rawType = ArrayList.class;
        ObjectConstructor<ArrayList<String>> objectConstructor = constructor.newDefaultImplementationConstructor(type, rawType);
        assertNotNull(objectConstructor);
        
        type = Set.class;
        rawType = HashSet.class;
        objectConstructor = constructor.newDefaultImplementationConstructor(type, rawType);
        assertNull(objectConstructor);
        
        rawType = TreeSet.class;
        objectConstructor = constructor.newDefaultImplementationConstructor(type, rawType);
        assertNotNull(objectConstructor);
        
        rawType = LinkedHashSet.class;
        objectConstructor = constructor.newDefaultImplementationConstructor(type, rawType);
        assertNotNull(objectConstructor);
        
        type = Queue.class;
        rawType = LinkedList.class;
        objectConstructor = constructor.newDefaultImplementationConstructor(type, rawType);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructor = new ConstructorConstructor(new LinkedHashMap<>());
        Type type = String.class;
        Class<String> rawType = String.class;
        ObjectConstructor<String> objectConstructor = constructor.newUnsafeAllocator(type, rawType);
        assertNotNull(objectConstructor);
        
        type = ArrayList.class;
        rawType = ArrayList.class;
        objectConstructor = constructor.newUnsafeAllocator(type, rawType);
        assertNotNull(objectConstructor);
    }
}