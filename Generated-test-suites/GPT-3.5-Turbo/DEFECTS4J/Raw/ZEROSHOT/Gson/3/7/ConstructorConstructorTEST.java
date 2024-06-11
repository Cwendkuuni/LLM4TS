import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ConstructorConstructorTest {

    private ConstructorConstructor constructorConstructor;

    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    }

    @Test
    public void testGet_InstanceCreatorExists_ReturnsInstanceCreatorConstructor() {
        // Arrange
        TypeToken<Object> typeToken = new TypeToken<Object>() {};
        final InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
            @Override
            public Object createInstance(Type type) {
                return new Object();
            }
        };
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>() {{
            put(typeToken.getType(), instanceCreator);
        }});

        // Act
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        Object instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

    @Test
    public void testGet_InstanceCreatorDoesNotExist_ReturnsNull() {
        // Arrange
        TypeToken<Object> typeToken = new TypeToken<Object>() {};
        final InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
            @Override
            public Object createInstance(Type type) {
                return new Object();
            }
        };

        // Act
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNull(objectConstructor);
    }

    @Test
    public void testGet_RawTypeInstanceCreatorExists_ReturnsRawTypeInstanceCreatorConstructor() {
        // Arrange
        TypeToken<Object> typeToken = new TypeToken<Object>() {};
        final InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
            @Override
            public Object createInstance(Type type) {
                return new Object();
            }
        };
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>() {{
            put(typeToken.getRawType(), instanceCreator);
        }});

        // Act
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        Object instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultConstructorExists_ReturnsDefaultConstructor() {
        // Arrange
        TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};

        // Act
        ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        TestClass instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(TestClass.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Collection_ReturnsLinkedHashSetConstructor() {
        // Arrange
        TypeToken<Collection<Object>> typeToken = new TypeToken<Collection<Object>>() {};

        // Act
        ObjectConstructor<Collection<Object>> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        Collection<Object> instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(LinkedHashSet.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_SortedSet_ReturnsTreeSetConstructor() {
        // Arrange
        TypeToken<SortedSet<Object>> typeToken = new TypeToken<SortedSet<Object>>() {};

        // Act
        ObjectConstructor<SortedSet<Object>> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        SortedSet<Object> instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(TreeSet.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_EnumSet_ReturnsEnumSetConstructor() {
        // Arrange
        TypeToken<EnumSet<TestEnum>> typeToken = new TypeToken<EnumSet<TestEnum>>() {};

        // Act
        ObjectConstructor<EnumSet<TestEnum>> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        EnumSet<TestEnum> instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(EnumSet.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Set_ReturnsLinkedHashSetConstructor() {
        // Arrange
        TypeToken<Set<Object>> typeToken = new TypeToken<Set<Object>>() {};

        // Act
        ObjectConstructor<Set<Object>> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        Set<Object> instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(LinkedHashSet.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Queue_ReturnsLinkedListConstructor() {
        // Arrange
        TypeToken<Queue<Object>> typeToken = new TypeToken<Queue<Object>>() {};

        // Act
        ObjectConstructor<Queue<Object>> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        Queue<Object> instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(LinkedList.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Default_ReturnsArrayListConstructor() {
        // Arrange
        TypeToken<Object> typeToken = new TypeToken<Object>() {};

        // Act
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        Object instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(ArrayList.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_SortedMap_ReturnsTreeMapConstructor() {
        // Arrange
        TypeToken<SortedMap<Object, Object>> typeToken = new TypeToken<SortedMap<Object, Object>>() {};

        // Act
        ObjectConstructor<SortedMap<Object, Object>> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        SortedMap<Object, Object> instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(TreeMap.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Map_ParameterizedType_ReturnsLinkedHashMapConstructor() {
        // Arrange
        TypeToken<Map<Object, Object>> typeToken = new TypeToken<Map<Object, Object>>() {};

        // Act
        ObjectConstructor<Map<Object, Object>> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        Map<Object, Object> instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(LinkedHashMap.class, instance.getClass());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Map_Default_ReturnsLinkedTreeMapConstructor() {
        // Arrange
        TypeToken<Object> typeToken = new TypeToken<Object>() {};

        // Act
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        Object instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(LinkedTreeMap.class, instance.getClass());
    }

    @Test
    public void testGet_UnsafeAllocatorConstructor_ReturnsUnsafeAllocatorConstructor() {
        // Arrange
        TypeToken<Object> typeToken = new TypeToken<Object>() {};

        // Act
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(objectConstructor);
        Object instance = objectConstructor.construct();
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

    private static class TestClass {
    }

    private enum TestEnum {
        VALUE1,
        VALUE2
    }
}