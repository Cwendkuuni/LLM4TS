package com.google.gson.internal;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
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
import com.google.gson.JsonIOException;

public class ConstructorConstructorTest {
    
    @Test
    public void testGet_InstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
        ObjectConstructor<ArrayList<String>> result = constructorConstructor.get(typeToken);
        
        assertNull(result);
    }
    
    @Test
    public void testGet_RawTypeCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        instanceCreators.put(ArrayList.class, new InstanceCreator<ArrayList<String>>() {
            public ArrayList<String> createInstance(Type type) {
                return new ArrayList<String>();
            }
        });
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
        ObjectConstructor<ArrayList<String>> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
        assertTrue(result.construct() instanceof ArrayList);
    }
    
    @Test
    public void testGet_DefaultConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<String> typeToken = new TypeToken<String>() {};
        ObjectConstructor<String> result = constructorConstructor.get(typeToken);
        
        assertNull(result);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Collection_SortedSet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<SortedSet<String>> typeToken = new TypeToken<SortedSet<String>>() {};
        ObjectConstructor<SortedSet<String>> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
        assertTrue(result.construct() instanceof TreeSet);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Collection_EnumSet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<EnumSet<ExampleEnum>> typeToken = new TypeToken<EnumSet<ExampleEnum>>() {};
        ObjectConstructor<EnumSet<ExampleEnum>> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
        assertTrue(result.construct() instanceof EnumSet);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Collection_Set() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {};
        ObjectConstructor<Set<String>> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
        assertTrue(result.construct() instanceof LinkedHashSet);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Collection_Queue() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<Queue<String>> typeToken = new TypeToken<Queue<String>>() {};
        ObjectConstructor<Queue<String>> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
        assertTrue(result.construct() instanceof LinkedList);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Collection_Default() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<Collection<String>> typeToken = new TypeToken<Collection<String>>() {};
        ObjectConstructor<Collection<String>> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
        assertTrue(result.construct() instanceof ArrayList);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Map_SortedMap() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<SortedMap<String, Integer>> typeToken = new TypeToken<SortedMap<String, Integer>>() {};
        ObjectConstructor<SortedMap<String, Integer>> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
        assertTrue(result.construct() instanceof TreeMap);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Map_ParameterizedType() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<Map<ExampleEnum, String>> typeToken = new TypeToken<Map<ExampleEnum, String>>() {};
        ObjectConstructor<Map<ExampleEnum, String>> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
        assertTrue(result.construct() instanceof LinkedHashMap);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Map_Default() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
        ObjectConstructor<Map<String, Integer>> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
        assertTrue(result.construct() instanceof LinkedTreeMap);
    }
    
    @Test
    public void testGet_UnsafeAllocator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        TypeToken<String> typeToken = new TypeToken<String>() {};
        ObjectConstructor<String> result = constructorConstructor.get(typeToken);
        
        assertNotNull(result);
    }
}