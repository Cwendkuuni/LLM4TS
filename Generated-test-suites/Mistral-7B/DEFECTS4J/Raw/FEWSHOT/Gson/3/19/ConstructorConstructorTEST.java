package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

    @Test
    public void testGetConstructorForCollectionSubtypes() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

        // Test for LinkedHashSet
        Type linkedHashSetType = new TypeToken<LinkedHashSet<String>>() {}.getType();
        ObjectConstructor<LinkedHashSet<String>> linkedHashSetConstructor = constructorConstructor.get(
            new TypeToken<LinkedHashSet<String>>() {}.getType());
        assertNotNull(linkedHashSetConstructor);
        LinkedHashSet<String> linkedHashSet = linkedHashSetConstructor.construct();
        assertNotNull(linkedHashSet);

        // Test for TreeSet
        Type treeSetType = new TypeToken<TreeSet<Integer>>() {}.getType();
        ObjectConstructor<TreeSet<Integer>> treeSetConstructor = constructorConstructor.get(
            new TypeToken<TreeSet<Integer>>() {}.getType());
        assertNotNull(treeSetConstructor);
        TreeSet<Integer> treeSet = treeSetConstructor.construct();
        assertNotNull(treeSet);

        // Test for ArrayList
        Type arrayListType = new TypeToken<ArrayList<Double>>() {}.getType();
        ObjectConstructor<ArrayList<Double>> arrayListConstructor = constructorConstructor.get(
            new TypeToken<ArrayList<Double>>() {}.getType());
        assertNotNull(arrayListConstructor);
        ArrayList<Double> arrayList = arrayListConstructor.construct();
        assertNotNull(arrayList);

        // Test for LinkedList
        Type linkedListType = new TypeToken<LinkedList<Character>>() {}.getType();
        ObjectConstructor<LinkedList<Character>> linkedListConstructor = constructorConstructor.get(
            new TypeToken<LinkedList<Character>>() {}.getType());
        assertNotNull(linkedListConstructor);
        LinkedList<Character> linkedList = linkedListConstructor.construct();
        assertNotNull(linkedList);

        // Test for ArrayList with parameterized type
        Type parameterizedArrayListType = new TypeToken<ArrayList<Pair<Integer, String>>>() {}.getType();
        ObjectConstructor<ArrayList<Pair<Integer, String>>> parameterizedArrayListConstructor = constructorConstructor.get(
            new TypeToken<ArrayList<Pair<Integer, String>>>() {}.getType());
        assertNotNull(parameterizedArrayListConstructor);
        ArrayList<Pair<Integer, String>> parameterizedArrayList = parameterizedArrayListConstructor.construct();
        assertNotNull(parameterizedArrayList);

        // Test for LinkedHashMap
        Type linkedHashMapType = new TypeToken<LinkedHashMap<String, Float>>() {}.getType();
        ObjectConstructor<LinkedHashMap<String, Float>> linkedHashMapConstructor = constructorConstructor.get(
            new TypeToken<LinkedHashMap<String, Float>>() {}.getType());
        assertNotNull(linkedHashMapConstructor);
        LinkedHashMap<String, Float> linkedHashMap = linkedHashMapConstructor.construct();
        assertNotNull(linkedHashMap);

        // Test for TreeMap
        Type treeMapType = new TypeToken<TreeMap<Character, Double>>() {}.getType();
        ObjectConstructor<TreeMap<Character, Double>> treeMapConstructor = constructorConstructor.get(
            new TypeToken<TreeMap<Character, Double>>() {}.getType());
        assertNotNull(treeMapConstructor);
        TreeMap<Character, Double> treeMap = treeMapConstructor.construct();
        assertNotNull(treeMap);
    }

    @Test
    public void testGetConstructorForMapSubtypes() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

        // Test for LinkedTreeMap
        Type linkedTreeMapType = new TypeToken<LinkedTreeMap<String, Boolean>>() {}.getType();
        ObjectConstructor<LinkedTreeMap<String, Boolean>> linkedTreeMapConstructor = constructorConstructor.get(
            new TypeToken<LinkedTreeMap<String, Boolean>>() {}.getType());
        assertNotNull(linkedTreeMapConstructor);
        LinkedTreeMap<String, Boolean> linkedTreeMap = linkedTreeMapConstructor.construct();
        assertNotNull(linkedTreeMap);

        // Test for TreeMap
        Type treeMapType = new TypeToken<TreeMap<Integer, Long>>() {}.getType();
        ObjectConstructor<TreeMap<Integer, Long>> treeMapConstructor = constructorConstructor.get(
            new TypeToken<TreeMap<Integer, Long>>() {}.getType());
        assertNotNull(treeMapConstructor);
        TreeMap<Integer, Long> treeMap = treeMapConstructor.construct();
        assertNotNull(treeMap);

        // Test for LinkedHashMap
        Type linkedHashMapType = new TypeToken<LinkedHashMap<String, Double>>() {}.getType();
        ObjectConstructor<LinkedHashMap<String, Double>> linkedHashMapConstructor = constructorConstructor.get(
            new TypeToken<LinkedHashMap<String, Double>>() {}.getType());
        assertNotNull(linkedHashMapConstructor);
        LinkedHashMap<String, Double> linkedHashMap = linkedHashMapConstructor.construct();
        assertNotNull(linkedHashMap);

        // Test for HashMap
        Type hashMapType = new TypeToken<HashMap<Character, Float>>() {}.getType();
        ObjectConstructor<HashMap<Character, Float>> hashMapConstructor = constructorConstructor.get(
            new TypeToken<HashMap<Character, Float>>() {}.getType());
        assertNotNull(hashMapConstructor);
        HashMap<Character, Float> hashMap = hashMapConstructor.construct();
        assertNotNull(hashMap);
    }

    @Test
    public void testGetConstructorForCommonTypes() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

        // Test for EnumSet
        Type enumSetType = new TypeToken<EnumSet<Month>>() {}.getType();
        ObjectConstructor<EnumSet<Month>> enumSetConstructor = constructorConstructor.get(
            new TypeToken<EnumSet<Month>>() {}.getType());
        assertNotNull(enumSetConstructor);
        EnumSet<Month> enumSet = enumSetConstructor.construct();
        assertNotNull(enumSet);

        // Test for Map
        Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
        ObjectConstructor<Map<String, Integer>> mapConstructor = constructorConstructor.get(
            new TypeToken<Map<String, Integer>>() {}.getType());
        assertNotNull(mapConstructor);
        Map<String, Integer> map = mapConstructor.construct();
        assertNotNull(map);

        // Test for Collection
        Type collectionType = new TypeToken<Collection<Integer>>() {}.getType();
        ObjectConstructor<Collection<Integer>> collectionConstructor = constructorConstructor.get(
            new TypeToken<Collection<Integer>>() {}.getType());
        assertNotNull(collectionConstructor);
        Collection<Integer> collection = collectionConstructor.construct();
        assertNotNull(collection);

        // Test for Queue
        Type queueType = new TypeToken<Queue<Character>>() {}.getType();
        ObjectConstructor<Queue<Character>> queueConstructor = constructorConstructor.get(
            new TypeToken<Queue<Character>>() {}.getType());
        assertNotNull(queueConstructor);
        Queue<Character> queue = queueConstructor.construct();
        assertNotNull(queue);
    }

    @Test
    public void testGetConstructorForNullTypes() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

        // Test for null type
        assertNull(constructorConstructor.get(null));

        // Test for null raw type
        assertNull(constructorConstructor.get(new TypeToken<Object>() {}.getType()));
    }
}