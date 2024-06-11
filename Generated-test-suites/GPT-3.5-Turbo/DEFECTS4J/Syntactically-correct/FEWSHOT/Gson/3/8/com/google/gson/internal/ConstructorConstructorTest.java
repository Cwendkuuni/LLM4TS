package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void testGet_InstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        instanceCreators.put(Integer.class, new IntegerInstanceCreator());
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(new TypeToken<Integer>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_RawType() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(new TypeToken<String>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_DefaultConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(new TypeToken<TestClass>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Collection() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(new TypeToken<ArrayList<String>>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_SortedSet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<TreeSet<String>> objectConstructor = constructorConstructor.get(new TypeToken<TreeSet<String>>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_EnumSet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<EnumSet<TestEnum>> objectConstructor = constructorConstructor.get(new TypeToken<EnumSet<TestEnum>>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Set() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<LinkedHashSet<String>> objectConstructor = constructorConstructor.get(new TypeToken<LinkedHashSet<String>>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Queue() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<LinkedList<String>> objectConstructor = constructorConstructor.get(new TypeToken<LinkedList<String>>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Map_SortedMap() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<TreeMap<String, Integer>> objectConstructor = constructorConstructor.get(new TypeToken<TreeMap<String, Integer>>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Map_ParameterizedType() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<LinkedHashMap<String, Integer>> objectConstructor = constructorConstructor.get(new TypeToken<LinkedHashMap<String, Integer>>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Map_Default() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<LinkedTreeMap<String, Integer>> objectConstructor = constructorConstructor.get(new TypeToken<LinkedTreeMap<String, Integer>>() {});
        
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_UnsafeAllocator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(new TypeToken<TestClass>() {});
        
        assertNotNull(objectConstructor);
    }
    
    private static class IntegerInstanceCreator implements InstanceCreator<Integer> {
        @Override
        public Integer createInstance(Type type) {
            return 1;
        }
    }
    
    private static class TestClass {
        
    }
    
    private enum TestEnum {
        VALUE1,
        VALUE2
    }
}