import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_WhenFieldIsGeneric_ReturnsGenericType() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = gm.getClass().getDeclaredField("field");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        // Add assertion for expected result
    }
    
    @Test
    public void testGetGenericType_WhenFieldIsNotGeneric_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = gm.getClass().getDeclaredField("field");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        // Add assertion for expected result
    }
}