import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_WhenGenericField_ReturnsGenericType() {
        // Arrange
        Field field = createFieldWithGenericType();

        // Act
        Class result = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(Integer.class, result);
    }

    @Test
    public void testGetGenericType_WhenNonGenericField_ReturnsObjectClass() {
        // Arrange
        Field field = createFieldWithoutGenericType();

        // Act
        Class result = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_WhenNestedGenericField_ReturnsGenericTypeOfFirstLevelGeneric() {
        // Arrange
        Field field = createFieldWithNestedGenericType();

        // Act
        Class result = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_WhenFieldIsNull_ReturnsObjectClass() {
        // Arrange
        Field field = null;

        // Act
        Class result = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    private Field createFieldWithGenericType() {
        try {
            return SampleClass.class.getDeclaredField("genericField");
        } catch (NoSuchFieldException e) {
            // Handle exception or fail test
        }
        return null;
    }

    private Field createFieldWithoutGenericType() {
        try {
            return SampleClass.class.getDeclaredField("nonGenericField");
        } catch (NoSuchFieldException e) {
            // Handle exception or fail test
        }
        return null;
    }

    private Field createFieldWithNestedGenericType() {
        try {
            return SampleClass.class.getDeclaredField("nestedGenericField");
        } catch (NoSuchFieldException e) {
            // Handle exception or fail test
        }
        return null;
    }

    private class SampleClass {
        private Integer genericField;
        private Object nonGenericField;
        private List<String> nestedGenericField;
    }
}