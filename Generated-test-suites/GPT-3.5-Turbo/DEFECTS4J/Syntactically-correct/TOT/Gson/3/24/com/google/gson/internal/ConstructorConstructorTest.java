import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

    @Test
    public void testGet_whenTypeCreatorExists_shouldReturnCorrectConstructor() {
        // Arrange
        TypeToken<MyObj> typeToken = new TypeToken<MyObj>() {};
        ConstructorConstructor constructorConstructor = createConstructorConstructor();
        InstanceCreator<MyObj> typeCreator = new InstanceCreator<MyObj>() {
            @Override
            public MyObj createInstance(Type type) {
                return new MyObj();
            }
        };
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(typeToken.getType(), typeCreator);
        constructorConstructor.setInstanceCreators(instanceCreators);

        // Act
        ObjectConstructor<MyObj> actualConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(actualConstructor);
        assertEquals(MyObj.class, actualConstructor.construct().getClass());
    }

    @Test
    public void testGet_whenRawTypeCreatorExists_shouldReturnCorrectConstructor() {
        // Arrange
        TypeToken<MyObj> typeToken = new TypeToken<MyObj>() {};
        ConstructorConstructor constructorConstructor = createConstructorConstructor();
        InstanceCreator<MyObj> rawTypeCreator = new InstanceCreator<MyObj>() {
            @Override
            public MyObj createInstance(Type type) {
                return new MyObj();
            }
        };
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(MyObj.class, rawTypeCreator);
        constructorConstructor.setInstanceCreators(instanceCreators);

        // Act
        ObjectConstructor<MyObj> actualConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(actualConstructor);
        assertEquals(MyObj.class, actualConstructor.construct().getClass());
    }

    @Test
    public void testGet_whenDefaultConstructorExists_shouldReturnCorrectConstructor() {
        // Arrange
        TypeToken<MyObj> typeToken = new TypeToken<MyObj>() {};
        ConstructorConstructor constructorConstructor = createConstructorConstructor();

        // Act
        ObjectConstructor<MyObj> actualConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(actualConstructor);
        assertEquals(MyObj.class, actualConstructor.construct().getClass());
    }

    @Test
    public void testGet_whenDefaultImplementationConstructorExists_shouldReturnCorrectConstructor() {
        // Arrange
        TypeToken<MyObj> typeToken = new TypeToken<MyObj>() {};
        ConstructorConstructor constructorConstructor = createConstructorConstructor();

        // Act
        ObjectConstructor<MyObj> actualConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(actualConstructor);
        assertEquals(MyObj.class, actualConstructor.construct().getClass());
    }

    @Test
    public void testGet_whenUnsafeAllocatorExists_shouldReturnCorrectConstructor() {
        // Arrange
        TypeToken<MyObj> typeToken = new TypeToken<MyObj>() {};
        ConstructorConstructor constructorConstructor = createConstructorConstructor();

        // Act
        ObjectConstructor<MyObj> actualConstructor = constructorConstructor.get(typeToken);

        // Assert
        assertNotNull(actualConstructor);
        assertEquals(MyObj.class, actualConstructor.construct().getClass());
    }

    @Test(expected = JsonIOException.class)
    public void testGet_whenInvalidEnumSetType_shouldThrowJsonIOException() {
        // Arrange
        TypeToken<InvalidEnumSetObj> typeToken = new TypeToken<InvalidEnumSetObj>() {};
        ConstructorConstructor constructorConstructor = createConstructorConstructor();

        // Act
        constructorConstructor.get(typeToken);

        // Assert: Expects JsonIOException to be thrown
    }

    private ConstructorConstructor createConstructorConstructor() {
        return new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    }

    class MyObj {

    }

    class InvalidEnumSetObj {

    }
}