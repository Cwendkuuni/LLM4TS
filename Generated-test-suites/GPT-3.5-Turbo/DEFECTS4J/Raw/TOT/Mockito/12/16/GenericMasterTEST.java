import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_whenFieldIsGeneric_shouldReturnActualTypeArgument() {
        // Arrange
        Field field = getField("genericField1");
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_whenFieldIsNotGeneric_shouldReturnObjectClass() {
        // Arrange
        Field field = getField("nonGenericField");
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_whenFieldIsNestedGeneric_shouldReturnActualTypeArgumentOfOuterGeneric() {
        // Arrange
        Field field = getField("nestedGenericField");
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Integer.class, result);
    }

    private Field getField(String fieldName) {
        try {
            return TestClass.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class TestClass {
        private Field genericField1; // String type
        private Field nonGenericField; // Object type
        private Field nestedGenericField; // List<Integer> type
    }

}