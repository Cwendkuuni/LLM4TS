
import static org.junit.Assert.*;

import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.*;
import com.google.common.base.*;

public class AnnotationUtilityTest {

    // Test cases for containsAnnotation(final DetailAST ast, final String annotation) method
    
    @Test
    public void testContainsAnnotation_withValidAstAndAnnotation_shouldReturnTrue(){
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getChild(0).setType(159);
        DetailAST child = new DetailAST();
        ast.getChild(0).addChild(child);
        child.addChild(new DetailAST());
        child.getChild(0).setText("MyAnnotation");

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, "MyAnnotation");

        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testContainsAnnotation_withValidAstAndEmptyAnnotation_shouldThrowIllegalArgumentException(){
        // Arrange
        DetailAST ast = new DetailAST();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.containsAnnotation(ast, ""));
    }
    
    @Test
    public void testContainsAnnotation_withValidAstAndNullAnnotation_shouldThrowIllegalArgumentException(){
        // Arrange
        DetailAST ast = new DetailAST();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.containsAnnotation(ast, null));
    }
    
    @Test
    public void testContainsAnnotation_withNullAst_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }
    
    // Test cases for containsAnnotation(final DetailAST ast) method
    
    @Test
    public void testContainsAnnotation_withoutAnnotationHolder_shouldReturnFalse(){
        // Arrange
        DetailAST ast = new DetailAST();

        // Act 
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotation_withAnnotationHolderButNoChild_shouldReturnFalse(){
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());

        // Act 
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotation_withAnnotationHolderContainingChildWithType159_shouldReturnTrue(){
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getChild(0).setType(159);

        // Act 
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testContainsAnnotation_withNullAst_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.containsAnnotation(ast));
    }
    
    // Test cases for getAnnotationHolder(final DetailAST ast) method
    
    @Test
    public void testGetAnnotationHolder_withValidAstWithType155_shouldReturnTokenOfType158(){
        // Arrange
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());

        // Act 
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
        assertEquals(158, result.getType());
    }
    
    @Test
    public void testGetAnnotationHolder_withValidAstWithType16_shouldReturnTokenOfType158(){
        // Arrange
        DetailAST ast = new DetailAST();
        ast.setType(16);
        ast.addChild(new DetailAST());

        // Act 
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
        assertEquals(158, result.getType());
    }
    
    @Test
    public void testGetAnnotationHolder_withValidAstNotOfType155Or16_shouldReturnTokenOfType5(){
        // Arrange
        DetailAST ast = new DetailAST();
        ast.setType(100);
        ast.addChild(new DetailAST());

        // Act 
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getType());
    }
    
    @Test
    public void testGetAnnotationHolder_withNullAst_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.getAnnotationHolder(ast));
    }
    
    // Test cases for getAnnotation(final DetailAST ast, final String annotation) method
    
    @Test
    public void testGetAnnotation_withValidAstAndExistingAnnotation_shouldReturnNotNull(){
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getChild(0).setType(159);
        DetailAST child = new DetailAST();
        ast.getChild(0).addChild(child);
        child.addChild(new DetailAST());
        child.getChild(0).setText("MyAnnotation");

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation");

        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testGetAnnotation_withValidAstAndNonExistingAnnotation_shouldReturnNull(){
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getChild(0).setType(159);
        DetailAST child = new DetailAST();
        ast.getChild(0).addChild(child);
        child.addChild(new DetailAST());
        child.getChild(0).setText("MyAnnotation");

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, "NonExistingAnnotation");

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_withValidAstAndEmptyAnnotation_shouldThrowIllegalArgumentException(){
        // Arrange
        DetailAST ast = new DetailAST();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotation_withValidAstAndNullAnnotation_shouldThrowIllegalArgumentException(){
        // Arrange
        DetailAST ast = new DetailAST();

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotation_withNullAst_shouldThrowIllegalArgumentException(){
        // Arrange
        DetailAST ast = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotation_withValidAstAndAnnotationContainingChildNotOfType159_shouldReturnNull(){
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getChild(0).setType(159);
        DetailAST child = new DetailAST();
        ast.getChild(0).addChild(child);
        child.addChild(new DetailAST());
        child.getChild(0).setText("MyAnnotation");

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, "NonExistingAnnotation");

        // Assert
        assertNull(result);
    }
}

