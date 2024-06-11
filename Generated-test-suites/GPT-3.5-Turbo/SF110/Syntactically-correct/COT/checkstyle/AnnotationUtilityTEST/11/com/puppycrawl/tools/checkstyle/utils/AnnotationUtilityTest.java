
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        boolean expectedResult = true;
        
        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        
        // Assert
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        // Arrange
        DetailAST ast = null;
        String annotation = "TestAnnotation";
        
        // Act
        AnnotationUtility.containsAnnotation(ast, annotation);
    }
    
    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        boolean expectedResult = false;
        
        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);
        
        // Assert
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithoutAnnotationWithNullAST() {
        // Arrange
        DetailAST ast = null;
        
        // Act
        AnnotationUtility.containsAnnotation(ast);
    }
    
    @Test
    public void testGetAnnotationHolderForClass() {
        // Arrange
        DetailAST ast = new DetailAST();
        ast.setType(155);
        int expectedTokenType = 158;
        
        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedTokenType, result.getType());
    }
    
    @Test
    public void testGetAnnotationHolderForInterface() {
        // Arrange
        DetailAST ast = new DetailAST();
        ast.setType(16);
        int expectedTokenType = 158;
        
        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedTokenType, result.getType());
    }
    
    @Test
    public void testGetAnnotationHolderForOtherType() {
        // Arrange
        DetailAST ast = new DetailAST();
        int expectedTokenType = 5;
        
        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedTokenType, result.getType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAST() {
        // Arrange
        DetailAST ast = null;
        
        // Act
        AnnotationUtility.getAnnotationHolder(ast);
    }
    
    @Test
    public void testGetAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST expectedChild = new DetailAST();
        expectedChild.setType(159);
        
        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        
        // Assert
        Assert.assertEquals(expectedChild, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        // Arrange
        DetailAST ast = null;
        String annotation = "TestAnnotation";
        
        // Act
        AnnotationUtility.getAnnotation(ast, annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = null;
        
        // Act
        AnnotationUtility.getAnnotation(ast, annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "";
        
        // Act
        AnnotationUtility.getAnnotation(ast, annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithWhitespaceAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "   ";
        
        // Act
        AnnotationUtility.getAnnotation(ast, annotation);
    }
    
    @Test
    public void testGetAnnotationWithNoMatchingAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST expectedChild = null;
        
        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        
        // Assert
        Assert.assertEquals(expectedChild, result);
    }
}
