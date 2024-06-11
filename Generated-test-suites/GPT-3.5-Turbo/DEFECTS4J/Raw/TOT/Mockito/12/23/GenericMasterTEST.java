import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void getGenericType_WithGenericField_ReturnsActualType() throws NoSuchFieldException {
        // Arrange
        Field field = ExampleClass.class.getDeclaredField("genericField");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }
    
    @Test
    public void getGenericType_WithNonGenericField_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field field = ExampleClass.class.getDeclaredField("nonGenericField");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void getGenericType_WithNestedGenericField_ReturnsActualType() throws NoSuchFieldException {
        // Arrange
        Field field = ExampleClass.class.getDeclaredField("nestedGenericField");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(List.class, result);
    }

    @Test
    public void getGenericType_WithNullGeneric_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field field = ExampleClass.class.getDeclaredField("nullGenericField");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void getGenericType_WithNoFieldType_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field field = ExampleClass.class.getDeclaredField("noFieldType");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    private static class ExampleClass {
        private List<String> genericField;
        private int nonGenericField;
        private List<List<Integer>> nestedGenericField;
        private Class<?> nullGenericField;
        private Field noFieldType;
    }
}