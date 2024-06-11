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
    public void setUp() throws Exception {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_WithGenericField_ReturnsCorrectType() {
        // Arrange
        Field field = SomeClass.class.getDeclaredField("genericField");

        // Act
        Class<?> resultType = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, resultType);
    }

    @Test
    public void testGetGenericType_WithNonGenericField_ReturnsObjectClass() {
        // Arrange
        Field field = SomeClass.class.getDeclaredField("nonGenericField");

        // Act
        Class<?> resultType = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, resultType);
    }

    @Test
    public void testGetGenericType_WithNestedGenericField_ReturnsCorrectType() {
        // Arrange
        Field field = SomeClass.class.getDeclaredField("nestedGenericField");

        // Act
        Class<?> resultType = genericMaster.getGenericType(field);

        // Assert
        assertEquals(List.class, resultType);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericType_WithInvalidField_ThrowsNoSuchFieldException() throws NoSuchFieldException {
        // Arrange
        Field field = SomeClass.class.getDeclaredField("invalidField");

        // Act & Assert
        genericMaster.getGenericType(field);
    }

    private static class SomeClass {
        private List<String> genericField;
        private Object nonGenericField;
        private List<List<String>> nestedGenericField;
    }
}