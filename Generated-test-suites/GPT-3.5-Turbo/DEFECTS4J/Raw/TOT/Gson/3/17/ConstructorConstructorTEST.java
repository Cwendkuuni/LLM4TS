import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorConstructorTest {
    
    @Mock
    private InstanceCreator instanceCreator;
    
    private ConstructorConstructor constructorConstructor;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Map<Type, InstanceCreator<?>> instanceCreatorMap = new LinkedHashMap<>();
        instanceCreatorMap.put(String.class, instanceCreator);
        instanceCreatorMap.put(ArrayList.class, instanceCreator);
        instanceCreatorMap.put(Map.class, instanceCreator);
        instanceCreatorMap.put(SortedMap.class, instanceCreator);
        instanceCreatorMap.put(Collection.class, instanceCreator);
        instanceCreatorMap.put(SortedSet.class, instanceCreator);
        instanceCreatorMap.put(Set.class, instanceCreator);
        instanceCreatorMap.put(Queue.class, instanceCreator);
        constructorConstructor = new ConstructorConstructor(instanceCreatorMap);
    }
    
    @Test
    public void testGet_WithTypeCreator_ReturnsObjectConstructorWithTypeCreator() {
        TypeToken<String> typeToken = new TypeToken<>(String.class){};
        Type type = typeToken.getType();
        
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
        
        assertNotNull(objectConstructor);
        verifyNoMoreInteractions(instanceCreator);
    }
    
    @Test
    public void testGet_WithRawTypeCreator_ReturnsObjectConstructorWithRawTypeCreator() {
        TypeToken<ArrayList> typeToken = new TypeToken<ArrayList>(){};
        Type type = typeToken.getType();
        
        ObjectConstructor<ArrayList> objectConstructor = constructorConstructor.get(typeToken);
        
        assertNotNull(objectConstructor);
        verifyNoMoreInteractions(instanceCreator);
    }
    
    @Test
    public void testGet_WithDefaultConstructor_ReturnsObjectConstructorWithDefaultConstructor() {
        TypeToken<CustomClass> typeToken = new TypeToken<CustomClass>(){};
        Type type = typeToken.getType();
        
        ObjectConstructor<CustomClass> objectConstructor = constructorConstructor.get(typeToken);
        
        assertNotNull(objectConstructor);
        verifyNoMoreInteractions(instanceCreator);
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_ReturnsObjectConstructorWithDefaultImplementationConstructor() {
        TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>(){};
        Type type = typeToken.getType();
        
        ObjectConstructor<Map<String, Object>> objectConstructor = constructorConstructor.get(typeToken);
        
        assertNotNull(objectConstructor);
        verifyNoMoreInteractions(instanceCreator);
    }
    
    @Test
    public void testGet_WithUnsafeAllocator_ReturnsObjectConstructorWithUnsafeAllocator() {
        TypeToken<UnsafeClass> typeToken = new TypeToken<UnsafeClass>(){};
        Type type = typeToken.getType();
        
        ObjectConstructor<UnsafeClass> objectConstructor = constructorConstructor.get(typeToken);
        
        assertNotNull(objectConstructor);
        verifyNoMoreInteractions(instanceCreator);
    }
    
    @Test
    public void testGet_WithInvalidType_ReturnsNull() {
        TypeToken<Integer> typeToken = new TypeToken<>(Integer.class){};
        Type type = typeToken.getType();
        
        ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
        
        assertNull(objectConstructor);
        verifyNoMoreInteractions(instanceCreator);
    }
    
    private class CustomClass {
        
    }
    
    private class UnsafeClass {
        
    }
}