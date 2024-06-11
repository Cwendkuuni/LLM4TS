import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_WithNonGenericType_ShouldReturnObjectClass() {
        // Arrange
        Field field = null;

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_WithGenericType_ShouldReturnActualType() {
        // Arrange
        class DummyClass<T> {
            T genericField;
        }
        Field field = DummyClass.class.getDeclaredFields()[0];

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_WithNestedGenericType_ShouldReturnActualType() {
        // Arrange
        class DummyClass<T> {
            DummyClass<String> nestedField;
        }
        Field field = DummyClass.class.getDeclaredFields()[0];

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_WithNullGenericType_ShouldReturnObjectClass() {
        // Arrange
        class DummyClass<T> {
            T genericField;
        }
        Field field = DummyClass.class.getDeclaredFields()[0];
        ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0] = null;

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_WithNoActualTypeArguments_ShouldReturnObjectClass() {
        // Arrange
        class DummyClass<T> {
            T genericField;
        }
        Field field = DummyClass.class.getDeclaredFields()[0];
        ((ParameterizedType) field.getGenericType()).getActualTypeArguments() = new Type[0];

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }
}