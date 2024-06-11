import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_WithGenericField_ReturnsCorrectGenericType() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("genericField");

        // Act
        Class<?> result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_WithNonGenericField_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("nonGenericField");

        // Act
        Class<?> result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_WithNestedGenerics_ReturnsCorrectGenericType() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("nestedGenericField");

        // Act
        Class<?> result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Integer.class, result);
    }
    
    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericType_WithNonExistingField_ThrowsNoSuchFieldException() throws NoSuchFieldException {
        // Arrange
        Field field = mock(Field.class);

        // Act
        genericMaster.getGenericType(field);

        // Assert
        // Exception expected: NoSuchFieldException
    }

    private class MyClass {
        private List<String> genericField;
        private Object nonGenericField;
        private List<List<Integer>> nestedGenericField;
    }
}