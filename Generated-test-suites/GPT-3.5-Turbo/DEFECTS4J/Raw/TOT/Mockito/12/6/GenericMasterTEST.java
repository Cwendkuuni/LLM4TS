import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;

public class GenericMasterTest {
    
    @Test
    public void testGetGenericType_WhenFieldIsGeneric_ShouldReturnCorrectType() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("genericField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        Assert.assertEquals(String.class, result);
    }
    
    @Test
    public void testGetGenericType_WhenFieldIsNotGeneric_ShouldReturnObjectClass() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nonGenericField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        Assert.assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_WhenFieldIsNestedGeneric_ShouldReturnCorrectType() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nestedGenericField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        Assert.assertEquals(Integer.class, result);
    }
    
    @Test
    public void testGetGenericType_WhenFieldIsNull_ShouldReturnObjectClass() {
        // Arrange
        GenericMaster gm = new GenericMaster();
        
        // Act
        Class result = gm.getGenericType(null);
        
        // Assert
        Assert.assertEquals(Object.class, result);
    }
    
    // edge case: generic type with multiple arguments
    @Test
    public void testGetGenericType_WhenFieldHasMultipleGenericTypes_ShouldReturnFirstGenericType() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("multipleGenericField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        Assert.assertEquals(String.class, result);
    }
    
    // edge case: generic type with wildcard
    @Test
    public void testGetGenericType_WhenFieldHasWildcardGenericTypes_ShouldReturnWildcardClass() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("wildcardGenericField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        Assert.assertEquals(String.class, result);
    }
    
    // edge case: generic type with upper bound wildcard
    @Test
    public void testGetGenericType_WhenFieldHasUpperBoundWildcard_ShouldReturnWildcardUpperBoundClass() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("upperBoundWildcardField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        Assert.assertEquals(String.class, result);
    }
    
    // edge case: generic type with lower bound wildcard
    @Test
    public void testGetGenericType_WhenFieldHasLowerBoundWildcard_ShouldReturnWildcardLowerBoundClass() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("lowerBoundWildcardField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        Assert.assertEquals(Number.class, result);
    }
    
    // error scenario: field with no class
    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericType_WhenFieldDoesNotExist_ShouldThrowException() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nonExistentField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert - Exception expected
    }
    
    // error scenario: non-parameterized type
    @Test
    public void testGetGenericType_WhenFieldTypeIsNonParameterized_ShouldReturnObjectClass() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nonParameterizedField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        Assert.assertEquals(Object.class, result);
    }
    
    private class MyClass {
        private List<String> genericField;
        private String nonGenericField;
        private List<List<Integer>> nestedGenericField;
        private List<String> multipleGenericField;
        private List<?> wildcardGenericField;
        private List<? extends String> upperBoundWildcardField;
        private List<? super Number> lowerBoundWildcardField;
        private List nonParameterizedField;
    }
}