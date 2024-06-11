import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorConstructorTest {

    @Test
    public void testConstructorConstructorWithEmptyMap() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        assertNotNull(constructor);
    }

    @Test
    public void testConstructorConstructorWithInstanceCreators() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(Type.class, mock(InstanceCreator.class));

        ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
        assertNotNull(constructor);
    }

    @Test
    public void testGetWithInstanceCreatorForTypeAndRawType() {
        TypeToken<Type> typeToken = new TypeToken<Type>() {};

        InstanceCreator<Type> instanceCreator = mock(InstanceCreator.class);
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(Type.class, instanceCreator);

        ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<Type> objectConstructor = constructor.get(typeToken);
        assertNotNull(objectConstructor);
        verify(instanceCreator, times(1)).createInstance(eq(Type.class));
    }

    @Test
    public void testGetWithInstanceCreatorForRawType() {
        TypeToken<Type> typeToken = new TypeToken<Type>() {};

        InstanceCreator<Type> instanceCreator = mock(InstanceCreator.class);
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(Type.class, instanceCreator);
        instanceCreators.put((Type) typeToken.getRawType(), instanceCreator);

        ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<Type> objectConstructor = constructor.get(typeToken);
        assertNotNull(objectConstructor);
        verify(instanceCreator, times(1)).createInstance(eq(Type.class));
    }

    @Test
    public void testGetWithDefaultConstructorForRawType() {
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};

        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();

        ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<Integer> objectConstructor = constructor.get(typeToken);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testGetWithDefaultImplementationConstructorForCollectionTypeWithGenericType() {
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};

        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<List<String>> objectConstructor = constructor.get(typeToken);
        assertNotNull(objectConstructor);
    }

    @Test(expected = JsonIOException.class)
    public void testGetWithDefaultImplementationConstructorForCollectionTypeWithoutGenericType() {
        TypeToken<List> typeToken = new TypeToken<List>() {};

        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        constructor.get(typeToken);
    }

    @Test
    public void testGetWithDefaultImplementationConstructorForMapTypeWithGenericType() {
        TypeToken<Map<Integer, String>> typeToken = new TypeToken<Map<Integer, String>>() {};

        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Map<Integer, String>> objectConstructor = constructor.get(typeToken);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testGetWithDefaultImplementationConstructorForMapTypeWithoutGenericType() {
        TypeToken<Map> typeToken = new TypeToken<Map>() {};

        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Map> objectConstructor = constructor.get(typeToken);
        assertNotNull(objectConstructor);
    }

    @Test(expected = RuntimeException.class)
    public void testGetWithUnsafeAllocator() {
        TypeToken<Type> typeToken = new TypeToken<Type>() {};

        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        constructor.get(typeToken);
    }

}