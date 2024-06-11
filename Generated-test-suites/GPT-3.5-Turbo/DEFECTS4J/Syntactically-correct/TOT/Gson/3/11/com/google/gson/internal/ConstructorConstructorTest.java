import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ConstructorConstructorTest {

    @Mock
    private InstanceCreator<Object> instanceCreator;
    private ConstructorConstructor constructorConstructor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(Object.class, instanceCreator);
        constructorConstructor = new ConstructorConstructor(instanceCreators);
    }

    @Test
    public void testGet_withTypeCreator() {
        final Type type = Object.class;
        final Class<Object> rawType = Object.class;

        when(instanceCreator.createInstance(type)).thenReturn(new Object());

        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(type));

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGet_withRawTypeCreator() {
        final Type type = Object.class;
        final Class<Object> rawType = Object.class;

        when(instanceCreator.createInstance(type)).thenReturn(new Object());

        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(rawType));

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGet_withDefaultConstructor() {
        final Class<Object> rawType = Object.class;

        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(rawType));

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGet_withDefaultImplementation() {
        final Type type = ArrayList.class;
        final Class<ArrayList<?>> rawType = ArrayList.class;

        ObjectConstructor<ArrayList<?>> objectConstructor = constructorConstructor.get(TypeToken.get(type));

        assertNotNull(objectConstructor);
        assertEquals(ArrayList.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGet_withUnsafeAllocator() {
        final Type type = Object.class;
        final Class<Object> rawType = Object.class;

        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(type));

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testNewDefaultConstructor_withValidConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Class<Object> rawType = Object.class;
        final Constructor<Object> constructor = rawType.getDeclaredConstructor();
        constructor.setAccessible(true);

        ObjectConstructor<Object> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);

        assertNotNull(objectConstructor);
        assertEquals(rawType, objectConstructor.construct().getClass());
    }

    @Test
    public void testNewDefaultConstructor_withInvalidConstructor() throws NoSuchMethodException {
        final Class<Object> rawType = DummyClass.class;

        ObjectConstructor<Object> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);

        assertNull(objectConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor_withCollection() {
        final Type type = LinkedList.class;
        final Class<LinkedList<?>> rawType = LinkedList.class;

        ObjectConstructor<LinkedList<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertEquals(LinkedList.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testNewDefaultImplementationConstructor_withSortedSet() {
        final Type type = TreeSet.class;
        final Class<TreeSet<?>> rawType = TreeSet.class;

        ObjectConstructor<TreeSet<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertEquals(TreeSet.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testNewDefaultImplementationConstructor_withEnumSet() {
        final Type type = EnumSet.class;
        final Class<EnumSet<?>> rawType = EnumSet.class;

        ObjectConstructor<EnumSet<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertEquals(EnumSet.class, objectConstructor.construct().getClass());
    }

    @Test(expected = JsonIOException.class)
    public void testNewDefaultImplementationConstructor_withInvalidEnumSetType() {
        final Type type = DummyClass.class;
        final Class<DummyClass> rawType = DummyClass.class;

        constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    }

    @Test
    public void testNewDefaultImplementationConstructor_withSet() {
        final Type type = HashSet.class;
        final Class<HashSet<?>> rawType = HashSet.class;

        ObjectConstructor<HashSet<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertEquals(HashSet.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testNewDefaultImplementationConstructor_withQueue() {
        final Type type = LinkedList.class;
        final Class<LinkedList<?>> rawType = LinkedList.class;

        ObjectConstructor<LinkedList<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertEquals(LinkedList.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testNewDefaultImplementationConstructor_withMap() {
        final Type type = HashMap.class;
        final Class<HashMap<?, ?>> rawType = HashMap.class;

        ObjectConstructor<HashMap<?, ?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertEquals(HashMap.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testNewDefaultImplementationConstructor_withSortedMap() {
        final Type type = TreeMap.class;
        final Class<TreeMap<?, ?>> rawType = TreeMap.class;

        ObjectConstructor<TreeMap<?, ?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertEquals(TreeMap.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testNewDefaultImplementationConstructor_withLinkedHashMap() {
        final Type type = LinkedHashMap.class;
        final Class<LinkedHashMap<?, ?>> rawType = LinkedHashMap.class;

        ObjectConstructor<LinkedHashMap<?, ?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertEquals(LinkedHashMap.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testToString() {
        String toString = constructorConstructor.toString();
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }

    private class DummyClass {}

}