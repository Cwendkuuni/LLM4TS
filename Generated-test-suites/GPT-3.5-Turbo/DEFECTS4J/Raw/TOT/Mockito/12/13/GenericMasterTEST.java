import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_WithGenericField_ReturnsCorrectType() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("genericField");
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(Integer.class, result);
    }
    
    @Test
    public void testGetGenericType_WithNonGenericField_ReturnsObjectType() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nonGenericField");
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_WithNestedGenerics_ReturnsCorrectType() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nestedGenericsField");
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(Long.class, result);
    }
    
    @Test
    public void testGetGenericType_WithNullField_ReturnsObjectType() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = null;
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_WithNonParameterizedType_ReturnsObjectType() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nonParameterizedField");
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_WithEmptyActualTypeArguments_ReturnsObjectType() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("emptyActualTypeArgumentsField");
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(Object.class, result);
    }
    
    private class MyClass<T> {
        private T genericField;
        private Object nonGenericField;
        private List<List<Long>> nestedGenericsField;
        private T nonParameterizedField;
        private List<?> emptyActualTypeArgumentsField;
    }
}