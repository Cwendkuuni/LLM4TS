package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {
    @Mock
    private InstanceCreator<Object> instanceCreator;
    
    private ConstructorConstructor constructorConstructor;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
    }
    
    @Test
    public void testGetWithTypeTokenThatHasInstanceCreator() {
        TypeToken<Object> typeToken = new TypeToken<Object>() {};
        when(instanceCreator.createInstance(typeToken.getType())).thenReturn(new Object());
        constructorConstructor.instanceCreators.put(typeToken.getType(), instanceCreator);
        
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);
        
        assertEquals(Object.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGetWithRawTypeThatHasInstanceCreator() {
        TypeToken<Object> typeToken = new TypeToken<Object>() {};
        when(instanceCreator.createInstance(typeToken.getType())).thenReturn(new Object());
        constructorConstructor.instanceCreators.put(typeToken.getRawType(), instanceCreator);
        
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);
        
        assertEquals(Object.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGetWithRawTypeThatHasDefaultConstructor() {
        TypeToken<Object> typeToken = new TypeToken<Object>() {};
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);
        
        assertEquals(Object.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGetWithRawTypeThatHasDefaultImplementationConstructor() {
        TypeToken<Set<Object>> typeToken = new TypeToken<Set<Object>>() {};
        ObjectConstructor<Set<Object>> objectConstructor = constructorConstructor.get(typeToken);
        
        assertEquals(LinkedHashSet.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGetWithRawTypeThatHasDefaultImplementationConstructorForEnumSet() {
        TypeToken<EnumSet<TestEnum>> typeToken = new TypeToken<EnumSet<TestEnum>>() {};
        ObjectConstructor<EnumSet<TestEnum>> objectConstructor = constructorConstructor.get(typeToken);
        
        assertEquals(EnumSet.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGetWithRawTypeThatHasDefaultImplementationConstructorForQueue() {
        TypeToken<Queue<Object>> typeToken = new TypeToken<Queue<Object>>() {};
        ObjectConstructor<Queue<Object>> objectConstructor = constructorConstructor.get(typeToken);
        
        assertEquals(LinkedList.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGetWithRawTypeThatHasDefaultImplementationConstructorForMap() {
        TypeToken<LinkedHashMap<String, Object>> typeToken = new TypeToken<LinkedHashMap<String, Object>>() {};
        ObjectConstructor<LinkedHashMap<String, Object>> objectConstructor = constructorConstructor.get(typeToken);
        
        assertEquals(LinkedHashMap.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGetWithRawTypeThatHasDefaultImplementationConstructorForDefaultMap() {
        TypeToken<LinkedTreeMap<String, Object>> typeToken = new TypeToken<LinkedTreeMap<String, Object>>() {};
        ObjectConstructor<LinkedTreeMap<String, Object>> objectConstructor = constructorConstructor.get(typeToken);
        
        assertEquals(LinkedTreeMap.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGetWithRawTypeThatHasNoMatchingConstructor() {
        TypeToken<TestInterface> typeToken = new TypeToken<TestInterface>() {};
        ObjectConstructor<TestInterface> objectConstructor = constructorConstructor.get(typeToken);
        
        assertNull(objectConstructor);
    }
    
    private interface TestInterface {
    }
    
    private enum TestEnum {
    }
}