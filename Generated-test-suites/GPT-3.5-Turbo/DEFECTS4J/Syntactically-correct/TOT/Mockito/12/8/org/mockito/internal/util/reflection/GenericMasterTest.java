import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_GenericField_ReturnsGenericClass() throws NoSuchFieldException {
        // Arrange
        Field field = ExampleClass.class.getDeclaredField("genericField");
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(GenericType.class, result);
    }

    @Test
    public void testGetGenericType_NonGenericField_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field field = ExampleClass.class.getDeclaredField("nonGenericField");
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_NestedGenericField_ReturnsActualTypeClass() throws NoSuchFieldException {
        // Arrange
        Field field = ExampleClass.class.getDeclaredField("nestedGenericField");
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_NullField_ReturnsObjectClass() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(null);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_InterfaceField_ReturnsInterfaceClass() throws NoSuchFieldException {
        // Arrange
        Field field = ExampleClass.class.getDeclaredField("interfaceField");
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(ExampleInterface.class, result);
    }
}