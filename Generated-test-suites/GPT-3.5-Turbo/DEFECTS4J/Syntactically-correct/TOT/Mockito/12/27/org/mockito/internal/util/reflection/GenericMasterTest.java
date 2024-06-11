import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_GenericField_ReturnsGenericType() {
        // Arrange
        Field field = getField("genericField");

        // Act
        Class genericType = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(String.class, genericType);
    }

    @Test
    public void testGetGenericType_NonGenericField_ReturnsObjectClass() {
        // Arrange
        Field field = getField("nonGenericField");

        // Act
        Class genericType = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(Object.class, genericType);
    }

    @Test
    public void testGetGenericType_NullField_ReturnsObjectClass() {
        // Arrange
        Field field = null;

        // Act
        Class genericType = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(Object.class, genericType);
    }

    @Test
    public void testGetGenericType_NestedGenericField_ReturnsNestedGenericType() {
        // Arrange
        Field field = getField("nestedGenericField");

        // Act
        Class genericType = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(Integer.class, genericType);
    }

    private Field getField(String fieldName) {
        try {
            return TestClass.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class TestClass {
        private List<String> genericField;
        private String nonGenericField;
        private List<List<Integer>> nestedGenericField;
    }

}