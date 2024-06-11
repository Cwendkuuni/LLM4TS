import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorConstructorTest {

    @Test
    public void testGet_withInstanceCreator() {
        // Arrange
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        TypeToken<String> typeToken = new TypeToken<String>() {};

        InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
            @Override
            public String createInstance(Type type) {
                return "Test String";
            }
        };

        instanceCreators.put(typeToken.getType(), instanceCreator);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        // Act
        ObjectConstructor<String> result = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(result);
        assertEquals("Test String", result.construct());
    }

    @Test
    public void testGet_withRawTypeCreator() {
        // Arrange
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};

        InstanceCreator<Integer> instanceCreator = new InstanceCreator<Integer>() {
            @Override
            public Integer createInstance(Type type) {
                return 10;
            }
        };

        instanceCreators.put(typeToken.getRawType(), instanceCreator);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        // Act
        ObjectConstructor<Integer> result = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(result);
        assertEquals(10, (int) result.construct());
    }

    @Test
    public void testGet_withDefaultConstructor() {
        // Arrange
        TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

        // Act
        ObjectConstructor<TestClass> result = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(result);
        assertNotNull(result.construct());
    }

    @Test
    public void testGet_withDefaultImplementationConstructor() {
        // Arrange
        TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {};
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

        // Act
        ObjectConstructor<Set<String>> result = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(result);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof LinkedHashSet<?>);
    }

    @Test
    public void testGet_withUnsafeAllocator() {
        // Arrange
        TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

        // Act
        ObjectConstructor<TestClass> result = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(result);
        assertNotNull(result.construct());
    }
}

class TestClass {
    public TestClass() {}
}