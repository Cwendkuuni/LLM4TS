import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_WithGenericField_ReturnsGenericType() {
        // Arrange
        Field field = Mockito.mock(Field.class);
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(field.getGenericType()).thenReturn(parameterizedType);
        Type[] actualTypeArguments = {String.class};
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(actualTypeArguments);
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_WithNonGenericField_ReturnsObjectClass() {
        // Arrange
        Field field = Mockito.mock(Field.class);
        Mockito.when(field.getGenericType()).thenReturn(null);
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_WithNestedGenericField_ReturnsNestedGenericType() {
        // Arrange
        Field field = Mockito.mock(Field.class);
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        ParameterizedType nestedParameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(field.getGenericType()).thenReturn(parameterizedType);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{nestedParameterizedType});
        Mockito.when(nestedParameterizedType.getActualTypeArguments()).thenReturn(new Type[]{Integer.class});
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Integer.class, result);
    }

    @Test
    public void testGetGenericType_WithNullParameterizedType_ReturnsObjectClass() {
        // Arrange
        Field field = Mockito.mock(Field.class);
        Mockito.when(field.getGenericType()).thenReturn(null);
        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }
}