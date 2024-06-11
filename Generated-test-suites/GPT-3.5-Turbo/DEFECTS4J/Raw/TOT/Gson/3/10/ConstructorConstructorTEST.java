import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {

    private ConstructorConstructor constructorConstructor;

    @Before
    public void setUp() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        constructorConstructor = new ConstructorConstructor(instanceCreators);
    }

    @Test
    public void get_InstanceCreatorExist_ReturnObjectConstructorWithInstanceCreator() {
        // Arrange
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        MyType instanceCreatorType = new MyType();
        instanceCreators.put(MyClass.class, instanceCreatorType);
        constructorConstructor = new ConstructorConstructor(instanceCreators);

        // Act
        ObjectConstructor<MyClass> result = constructorConstructor.get(TypeToken.get(MyClass.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertEquals(instanceCreatorType.createInstance(), result.construct());
    }

    @Test
    public void get_InstanceCreatorNotExist_ReturnObjectConstructorWithDefaultConstructor() {
        // Arrange
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        constructorConstructor = new ConstructorConstructor(instanceCreators);

        // Act
        ObjectConstructor<MyClass> result = constructorConstructor.get(TypeToken.get(MyClass.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof MyClass);
    }

    @Test
    public void get_RawTypeInstanceCreatorExist_ReturnObjectConstructorWithInstanceCreator() {
        // Arrange
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        MyType rawTypeInstanceCreator = new MyType();
        instanceCreators.put(MyInterface.class, rawTypeInstanceCreator);
        constructorConstructor = new ConstructorConstructor(instanceCreators);

        // Act
        ObjectConstructor<MyInterface> result = constructorConstructor.get(TypeToken.get(MyInterface.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertEquals(rawTypeInstanceCreator.createInstance(), result.construct());
    }

    @Test
    public void get_DefaultConstructorExists_ReturnObjectConstructorWithDefaultConstructor() {
        // Arrange

        // Act
        ObjectConstructor<AnotherClass> result = constructorConstructor.get(TypeToken.get(AnotherClass.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof AnotherClass);
    }

    @Test
    public void get_NoDefaultConstructorExists_ReturnNull() {
        // Arrange

        // Act
        ObjectConstructor<MyAbstractClass> result = constructorConstructor.get(TypeToken.get(MyAbstractClass.class));

        // Assert
        assertNull(result);
    }

    @Test
    public void get_DefaultImplementationExists_ReturnObjectConstructorWithDefaultImplementation() {
        // Arrange

        // Act
        ObjectConstructor<List<MyOtherClass>> result = constructorConstructor.get(TypeToken.getParameterized(List.class, MyOtherClass.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof ArrayList);
    }

    @Test
    public void get_InvalidEnumSetType_ReturnJsonIOException() {
        // Arrange

        // Act
        Exception exception = assertThrows(JsonIOException.class, () -> {
            constructorConstructor.get(TypeToken.getParameterized(EnumSet.class, InvalidEnumType.class));
        });

        // Assert
        String expectedMessage = "Invalid EnumSet type: " + TypeToken.getParameterized(EnumSet.class, InvalidEnumType.class).getType().toString();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void get_SortedSetType_ReturnObjectConstructorWithTreeSet() {
        // Arrange

        // Act
        ObjectConstructor<SortedSet<MyOtherClass>> result = constructorConstructor.get(TypeToken.getParameterized(SortedSet.class, MyOtherClass.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof TreeSet);
    }

    @Test
    public void get_EnumSetType_ReturnObjectConstructorWithEnumSet() {
        // Arrange

        // Act
        ObjectConstructor<EnumSet<MyEnum>> result = constructorConstructor.get(TypeToken.getParameterized(EnumSet.class, MyEnum.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof EnumSet);
    }

    @Test
    public void get_SetType_ReturnObjectConstructorWithLinkedHashSet() {
        // Arrange

        // Act
        ObjectConstructor<Set<MyOtherClass>> result = constructorConstructor.get(TypeToken.getParameterized(Set.class, MyOtherClass.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof LinkedHashSet);
    }

    @Test
    public void get_QueueType_ReturnObjectConstructorWithLinkedList() {
        // Arrange

        // Act
        ObjectConstructor<Queue<MyOtherClass>> result = constructorConstructor.get(TypeToken.getParameterized(Queue.class, MyOtherClass.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof LinkedList);
    }

    @Test
    public void get_SortedMapType_ReturnObjectConstructorWithTreeMap() {
        // Arrange

        // Act
        ObjectConstructor<SortedMap<String, MyOtherClass>> result = constructorConstructor.get(TypeToken.getParameterized(SortedMap.class, String.class, MyOtherClass.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof TreeMap);
    }

    @Test
    public void get_MapType_ReturnObjectConstructorWithLinkedHashMap() {
        // Arrange
        TypeToken<Map<String, MyOtherClass>> typeToken = new TypeToken<Map<String, MyOtherClass>>() {};

        // Act
        ObjectConstructor<Map<String, MyOtherClass>> result = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof LinkedHashMap);
    }

    @Test
    public void get_DefaultConstructorNotAccessible_ReturnObjectConstructorWithDefaultConstructor() {
        // Arrange
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        MyType defaultType = new MyType();
        instanceCreators.put(PrivateConstructorClass.class, defaultType);
        constructorConstructor = new ConstructorConstructor(instanceCreators);

        // Act
        ObjectConstructor<PrivateConstructorClass> result = constructorConstructor.get(TypeToken.get(PrivateConstructorClass.class));

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ObjectConstructor);
        assertNotNull(result.construct());
        assertTrue(result.construct() instanceof PrivateConstructorClass);
    }

    @Test
    public void toString_NotEmpty_ReturnStringRepresentation() {
        // Arrange
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        instanceCreators.put(MyClass.class, new MyType());
        instanceCreators.put(MyInterface.class, new MyType());
        constructorConstructor = new ConstructorConstructor(instanceCreators);

        // Act
        String stringRepresentation = constructorConstructor.toString();

        // Assert
        assertNotNull(stringRepresentation);
        assertFalse(stringRepresentation.isEmpty());
        assertTrue(stringRepresentation.contains("MyClass"));
        assertTrue(stringRepresentation.contains("MyInterface"));
    }

    private class MyClass {
        // Some code here
    }
    
    private class MyInterface {
        // Some code here
    }
    
    private class AnotherClass {
        // Some code here
    }
    
    private abstract class MyAbstractClass {
        // Some code here
    }
    
    private class MyOtherClass {
        // Some code here
    }
    
    private enum MyEnum {
        // Some code here
    }
    
    private class InvalidEnumType {
        // Some code here
    }
    
    private class PrivateConstructorClass {
        private PrivateConstructorClass() {
            // Some code here
        }
    }
    
    private class MyType implements InstanceCreator<Object> {
        @Override
        public Object createInstance(Type type) {
            if (type.equals(MyClass.class)) {
                return new MyClass();
            } else if (type.equals(MyInterface.class)) {
                return new MyInterface() {
                    // Some code here
                };
            } else {
                return null;
            }
        }
    }
}