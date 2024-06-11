package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {
    
    @Test
    public void testConstructorConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        assertNotNull(constructorConstructor);
    }

    @Test
    public void testGet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<ArrayList<String>> arrayListToken = new TypeToken<ArrayList<String>>() {};
        ObjectConstructor<ArrayList<String>> objectConstructor1 = constructorConstructor.get(arrayListToken);
        assertNotNull(objectConstructor1);
        
        TypeToken<LinkedHashSet<Integer>> linkedHashSetToken = new TypeToken<LinkedHashSet<Integer>>() {};
        ObjectConstructor<LinkedHashSet<Integer>> objectConstructor2 = constructorConstructor.get(linkedHashSetToken);
        assertNotNull(objectConstructor2);
        
        TypeToken<TreeMap<String, Long>> treeMapToken = new TypeToken<TreeMap<String, Long>>() {};
        ObjectConstructor<TreeMap<String, Long>> objectConstructor3 = constructorConstructor.get(treeMapToken);
        assertNotNull(objectConstructor3);
        
        TypeToken<TreeSet<Double>> treeSetToken = new TypeToken<TreeSet<Double>>() {};
        ObjectConstructor<TreeSet<Double>> objectConstructor4 = constructorConstructor.get(treeSetToken);
        assertNotNull(objectConstructor4);
    }

    @Test
    public void testNewDefaultConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        Class<? super ArrayList<String>> rawType = ArrayList.class;
        ObjectConstructor<ArrayList<String>> objectConstructor1 = constructorConstructor.newDefaultConstructor(rawType);
        assertNotNull(objectConstructor1);
        
        Class<? super LinkedHashMap<Integer, String>> rawType2 = LinkedHashMap.class;
        ObjectConstructor<LinkedHashMap<Integer, String>> objectConstructor2 = constructorConstructor.newDefaultConstructor(rawType2);
        assertNotNull(objectConstructor2);
        
        Class<? super LinkedList<Long>> rawType3 = LinkedList.class;
        ObjectConstructor<LinkedList<Long>> objectConstructor3 = constructorConstructor.newDefaultConstructor(rawType3);
        assertNotNull(objectConstructor3);
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<SortedSet<Character>> sortedSetToken = new TypeToken<SortedSet<Character>>() {};
        ObjectConstructor<SortedSet<Character>> objectConstructor1 = constructorConstructor.newDefaultImplementationConstructor(sortedSetToken.getType(), sortedSetToken.getRawType());
        assertNotNull(objectConstructor1);
        
        TypeToken<EnumSet<DayOfWeek>> enumSetToken = new TypeToken<EnumSet<DayOfWeek>>() {};
        ObjectConstructor<EnumSet<DayOfWeek>> objectConstructor2 = constructorConstructor.newDefaultImplementationConstructor(enumSetToken.getType(), enumSetToken.getRawType());
        assertNotNull(objectConstructor2);
        
        TypeToken<Set<Float>> setToken = new TypeToken<Set<Float>>() {};
        ObjectConstructor<Set<Float>> objectConstructor3 = constructorConstructor.newDefaultImplementationConstructor(setToken.getType(), setToken.getRawType());
        assertNotNull(objectConstructor3);
        
        TypeToken<Queue<Byte>> queueToken = new TypeToken<Queue<Byte>>() {};
        ObjectConstructor<Queue<Byte>> objectConstructor4 = constructorConstructor.newDefaultImplementationConstructor(queueToken.getType(), queueToken.getRawType());
        assertNotNull(objectConstructor4);
        
        TypeToken<ArrayList<Character>> arrayListToken = new TypeToken<ArrayList<Character>>() {};
        ObjectConstructor<ArrayList<Character>> objectConstructor5 = constructorConstructor.newDefaultImplementationConstructor(arrayListToken.getType(), arrayListToken.getRawType());
        assertNotNull(objectConstructor5);
        
        TypeToken<TreeMap<Integer, Float>> treeMapToken = new TypeToken<TreeMap<Integer, Float>>() {};
        ObjectConstructor<TreeMap<Integer, Float>> objectConstructor6 = constructorConstructor.newDefaultImplementationConstructor(treeMapToken.getType(), treeMapToken.getRawType());
        assertNotNull(objectConstructor6);
        
        TypeToken<LinkedHashMap<String, Double>> linkedHashMapToken = new TypeToken<LinkedHashMap<String, Double>>() {};
        ObjectConstructor<LinkedHashMap<String, Double>> objectConstructor7 = constructorConstructor.newDefaultImplementationConstructor(linkedHashMapToken.getType(), linkedHashMapToken.getRawType());
        assertNotNull(objectConstructor7);
        
        TypeToken<Set<String>> setStringToken = new TypeToken<Set<String>>() {};
        ObjectConstructor<LinkedTreeMap<String, Object>> objectConstructor8 = constructorConstructor.newDefaultImplementationConstructor(setStringToken.getType(), setStringToken.getRawType());
        assertNotNull(objectConstructor8);
    }

    @Test
    public void testNewUnsafeAllocator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<ArrayList<String>> arrayListToken = new TypeToken<ArrayList<String>>() {};
        ObjectConstructor<ArrayList<String>> objectConstructor1 = constructorConstructor.newUnsafeAllocator(arrayListToken.getType(), arrayListToken.getRawType());
        assertNotNull(objectConstructor1);
        
        TypeToken<LinkedHashSet<Integer>> linkedHashSetToken = new TypeToken<LinkedHashSet<Integer>>() {};
        ObjectConstructor<LinkedHashSet<Integer>> objectConstructor2 = constructorConstructor.newUnsafeAllocator(linkedHashSetToken.getType(), linkedHashSetToken.getRawType());
        assertNotNull(objectConstructor2);
    }

}