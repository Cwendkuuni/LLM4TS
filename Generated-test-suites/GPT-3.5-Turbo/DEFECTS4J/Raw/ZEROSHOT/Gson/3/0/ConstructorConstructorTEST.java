import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
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
import static org.junit.Assert.assertTrue;

public class ConstructorConstructorTest {

    @Test
    public void testConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        assertNotNull(constructorConstructor);
        assertTrue(constructorConstructor.toString().isEmpty());
    }

    @Test
    public void testGetWithTypeCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        instanceCreators.put(String.class, new InstanceCreator<String>() {
            @Override
            public String createInstance(Type type) {
                return "test";
            }
        });

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        TypeToken<String> typeToken = new TypeToken<String>() {};

        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertEquals("test", objectConstructor.construct());
    }

    @Test
    public void testGetWithRawTypeCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
        instanceCreators.put(ArrayList.class, new InstanceCreator<ArrayList>() {
            @Override
            public ArrayList createInstance(Type type) {
                return new ArrayList();
            }
        });

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        TypeToken<ArrayList> typeToken = new TypeToken<ArrayList>() {};

        ObjectConstructor<ArrayList> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGetWithDefaultConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        TypeToken<ConstructorConstructorTest> typeToken = new TypeToken<ConstructorConstructorTest>() {};

        ObjectConstructor<ConstructorConstructorTest> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGetWithDefaultImplementationConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        TypeToken<ArrayList> typeToken = new TypeToken<ArrayList>() {};

        ObjectConstructor<ArrayList> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test(expected = RuntimeException.class)
    public void testGetWithUnsafeAllocator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        TypeToken<Object> typeToken = new TypeToken<Object>() {};

        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testNewDefaultConstructor() throws NoSuchMethodException {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        Class<? super ConstructorConstructor> rawType = constructorConstructor.getClass();
        Constructor<? super ConstructorConstructor> constructor = rawType.getDeclaredConstructor();
        constructor.setAccessible(true);

        ObjectConstructor<ConstructorConstructor> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testNewDefaultConstructorWithNoSuchMethodException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        Class<? super ConstructorConstructorTest> rawType = ConstructorConstructorTest.class;

        ObjectConstructor<ConstructorConstructorTest> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);

        assertEquals(null, objectConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructorWithCollectionAndSortedSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            @Override
            public Type getRawType() {
                return null;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        Class<? super SortedSet<?>> rawType = SortedSet.class;

        ObjectConstructor<SortedSet<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct().isEmpty());
    }

    @Test(expected = JsonIOException.class)
    public void testNewDefaultImplementationConstructorWithInvalidEnumSetType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{ConstructorConstructorTest.class};
            }

            @Override
            public Type getRawType() {
                return null;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        Class<? super EnumSet<?>> rawType = EnumSet.class;

        ObjectConstructor<EnumSet<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
        objectConstructor.construct();
    }

    @Test
    public void testNewDefaultImplementationConstructorWithSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            @Override
            public Type getRawType() {
                return null;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        Class<? super Set<?>> rawType = Set.class;

        ObjectConstructor<Set<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct().isEmpty());
    }

    @Test
    public void testNewDefaultImplementationConstructorWithQueue() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            @Override
            public Type getRawType() {
                return null;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        Class<? super Queue<?>> rawType = Queue.class;

        ObjectConstructor<Queue<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct().isEmpty());
    }

    @Test
    public void testNewDefaultImplementationConstructorWithList() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            @Override
            public Type getRawType() {
                return null;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        Class<? super Collection<?>> rawType = Collection.class;

        ObjectConstructor<Collection<?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct().isEmpty());
    }

    @Test
    public void testNewDefaultImplementationConstructorWithSortedMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class, String.class};
            }

            @Override
            public Type getRawType() {
                return null;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        Class<? super SortedMap<?,?>> rawType = SortedMap.class;

        ObjectConstructor<SortedMap<?,?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct().isEmpty());
    }

    @Test
    public void testNewDefaultImplementationConstructorWithMapAndParameterizedType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};

        Type type = typeToken.getType();
        Class<? super Map<?,?>> rawType = Map.class;

        ObjectConstructor<Map<?,?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct().isEmpty());
    }

    @Test
    public void testNewDefaultImplementationConstructorWithMapAndStringType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};

        Type type = typeToken.getType();
        Class<? super Map<?,?>> rawType = LinkedTreeMap.class;

        ObjectConstructor<Map<?,?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct().isEmpty());
    }

    @Test
    public void testNewDefaultImplementationConstructorWithNullException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};

        Type type = typeToken.getType();
        Class<? super Map<?,?>> rawType = null;

        ObjectConstructor<Map<?,?>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertEquals(null, objectConstructor);
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        TypeToken<Object> typeToken = new TypeToken<Object>() {};

        ObjectConstructor<Object> objectConstructor = constructorConstructor.newUnsafeAllocator(typeToken.getType(), Object.class);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test(expected = RuntimeException.class)
    public void testNewUnsafeAllocatorWithException() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());

        TypeToken<Object> typeToken = new TypeToken<Object>() {};

        ObjectConstructor<Object> objectConstructor = constructorConstructor.newUnsafeAllocator(typeToken.getType(), null);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

}