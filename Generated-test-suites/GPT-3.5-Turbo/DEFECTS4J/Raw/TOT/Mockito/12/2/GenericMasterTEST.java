import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_WithGenericField_ReturnsGenericType() throws NoSuchFieldException {
        // Arrange
        Field genericField = MyGenericClass.class.getDeclaredField("genericList");

        // Act
        Class result = GenericMaster.getGenericType(genericField);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_WithNonGenericField_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field nonGenericField = MyGenericClass.class.getDeclaredField("nonGenericField");

        // Act
        Class result = GenericMaster.getGenericType(nonGenericField);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_WithNestedGenericField_ReturnsNestedGenericType() throws NoSuchFieldException {
        // Arrange
        Field nestedGenericField = MyGenericClass.class.getDeclaredField("nestedGenericList");

        // Act
        Class result = GenericMaster.getGenericType(nestedGenericField);

        // Assert
        assertEquals(Integer.class, result);
    }

    // Edge Cases

    @Test
    public void testGetGenericType_WithNullGeneric_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field nullGenericField = MyGenericClass.class.getDeclaredField("nullGenericField");

        // Act
        Class result = GenericMaster.getGenericType(nullGenericField);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_WithEmptyGeneric_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field emptyGenericField = MyGenericClass.class.getDeclaredField("emptyGenericField");

        // Act
        Class result = GenericMaster.getGenericType(emptyGenericField);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_WithInvalidGeneric_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field invalidGenericField = MyGenericClass.class.getDeclaredField("invalidGenericField");

        // Act
        Class result = GenericMaster.getGenericType(invalidGenericField);

        // Assert
        assertEquals(Object.class, result);
    }

    // Error Scenarios

    @Test (expected = NoSuchFieldException.class)
    public void testGetGenericType_WithNonexistentField_ThrowsNoSuchFieldException() throws NoSuchFieldException {
        // Arrange
        Field nonexistentField = MyGenericClass.class.getDeclaredField("nonexistentField");

        // Act
        GenericMaster.getGenericType(nonexistentField);
    }

    private static class MyGenericClass {
        private List<String> genericList;
        private String nonGenericField;
        private List<List<Integer>> nestedGenericList;
        private List<?> nullGenericField;
        private List<Class> emptyGenericField;
        private List<String>[] invalidGenericField;
    }
}