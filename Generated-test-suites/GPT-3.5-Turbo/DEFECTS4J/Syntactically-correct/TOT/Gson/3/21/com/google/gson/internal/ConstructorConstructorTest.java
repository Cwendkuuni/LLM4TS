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
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConstructorConstructorTest {

    private Map<Type, InstanceCreator<?>> instanceCreators;
    private ConstructorConstructor constructorConstructor;

    @Before
    public void setup() {
        instanceCreators = new LinkedHashMap<>();
        constructorConstructor = new ConstructorConstructor(instanceCreators);
    }

    @Test
    public void testGet_InstanceCreator() {
        TypeToken<TestObject> typeToken = new TypeToken<TestObject>() {};
        instanceCreators.put(TestObject.class, new TestObjectInstanceCreator());

        ObjectConstructor<TestObject> result = constructorConstructor.get(typeToken);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TestObjectInstanceConstructor);
    }

    @Test
    public void testGet_RawTypeCreator() {
        TypeToken<TestObject> typeToken = new TypeToken<TestObject>() {};
        instanceCreators.put(TestObject.class, new TestObjectInstanceCreator());

        ObjectConstructor<TestObject> result = constructorConstructor.get(typeToken);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TestObjectRawTypeConstructor);
    }

    @Test
    public void testGet_DefaultConstructor() {
        TypeToken<TestObject> typeToken = new TypeToken<TestObject>() {};

        ObjectConstructor<TestObject> result = constructorConstructor.get(typeToken);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof DefaultConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor() {
        TypeToken<TestCollection> typeToken = new TypeToken<TestCollection>() {};

        ObjectConstructor<TestCollection> result = constructorConstructor.get(typeToken);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof DefaultImplementationConstructor);
    }

    @Test
    public void testGet_UnsafeAllocator() {
        TypeToken<TestObject> typeToken = new TypeToken<TestObject>() {};

        ObjectConstructor<TestObject> result = constructorConstructor.get(typeToken);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof UnsafeAllocatorConstructor);
    }

    private class TestObject {}

    private class TestCollection {}

    private class TestObjectInstanceCreator implements InstanceCreator<TestObject> {
        @Override
        public TestObject createInstance(Type type) {
            return new TestObject();
        }
    }

    private class TestObjectInstanceConstructor implements ObjectConstructor<TestObject> {
        @Override
        public TestObject construct() {
            return new TestObject();
        }
    }

    private class TestObjectRawTypeConstructor implements ObjectConstructor<TestObject> {
        @Override
        public TestObject construct() {
            return new TestObject();
        }
    }

    private class DefaultConstructor implements ObjectConstructor<TestObject> {
        @Override
        public TestObject construct() {
            try {
                Constructor<TestObject> constructor = TestObject.class.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                    NoSuchMethodException e) {
                throw new RuntimeException("Failed to invoke default constructor", e);
            }
        }
    }

    private class DefaultImplementationConstructor implements ObjectConstructor<TestCollection> {
        @Override
        public TestCollection construct() {
            return new TestCollection();
        }
    }

    private class UnsafeAllocatorConstructor implements ObjectConstructor<TestObject> {
        @Override
        public TestObject construct() {
            try {
                UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
                Object newInstance = unsafeAllocator.newInstance(TestObject.class);
                return (TestObject) newInstance;
            } catch (Exception e) {
                throw new RuntimeException("Unable to invoke no-args constructor", e);
            }
        }
    }
}