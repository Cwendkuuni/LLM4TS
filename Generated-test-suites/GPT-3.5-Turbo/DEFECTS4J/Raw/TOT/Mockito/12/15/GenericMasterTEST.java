import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_GivenGenericField_ReturnsActualClass() {
        // Arrange
        Field field = null; // Replace with actual Field object
        
        // Act
        Class result = GenericMaster.getGenericType(field);
        
        // Assert
        // Replace with assertions
    }

    @Test
    public void testGetGenericType_GivenNonGenericField_ReturnsObjectClass() {
        // Arrange
        Field field = null; // Replace with actual Field object
        
        // Act
        Class result = GenericMaster.getGenericType(field);
        
        // Assert
        // Replace with assertions
    }

    @Test
    public void testGetGenericType_GivenNullField_ReturnsObjectClass() {
        // Arrange
        Field field = null;
        
        // Act
        Class result = GenericMaster.getGenericType(field);
        
        // Assert
        // Replace with assertions
    }

    @Test
    public void testGetGenericType_GivenNestedGenericField_ReturnsActualClass() {
        // Arrange
        Field field = null; // Replace with actual Field object
        
        // Act
        Class result = GenericMaster.getGenericType(field);
        
        // Assert
        // Replace with assertions
    }

    @Test
    public void testGetGenericType_GivenFieldWithNoGenericType_ReturnsObjectClass() {
        // Arrange
        Field field = null; // Replace with actual Field object
        
        // Act
        Class result = GenericMaster.getGenericType(field);
        
        // Assert
        // Replace with assertions
    }
}