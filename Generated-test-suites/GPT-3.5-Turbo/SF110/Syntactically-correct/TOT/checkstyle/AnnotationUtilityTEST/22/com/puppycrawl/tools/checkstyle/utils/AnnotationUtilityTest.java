import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    private DetailAST ast;

    @Before
    public void setUp() {
        ast = new DetailAST();
    }

    @Test
    public void containsAnnotation_ValidAnnotation_ReturnsTrue() {
        // Arrange
        ast.setType(159);
        ast.addChild(new DetailAST());
        String annotation = "TestAnnotation";
        
        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void containsAnnotation_NullAST_ThrowsIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;
        String annotation = "TestAnnotation";
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, annotation);
        });
    }

    @Test
    public void containsAnnotation_NullAnnotation_ReturnsFalse() {
        // Arrange
        ast.setType(159);
        ast.addChild(new DetailAST());
        String annotation = null;

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getAnnotation_ValidAnnotation_ReturnsDetailAST() {
        // Arrange
        ast.setType(155);
        DetailAST childAST = new DetailAST();
        childAST.setType(159);
        DetailAST grandchildAST = new DetailAST();
        grandchildAST.setType(123);
        childAST.addChild(grandchildAST);
        ast.addChild(childAST);
        String annotation = "TestAnnotation";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void getAnnotation_AnnotationNotFound_ReturnsNull() {
        // Arrange
        ast.setType(155);
        DetailAST childAST = new DetailAST();
        childAST.setType(159);
        DetailAST grandchildAST = new DetailAST();
        grandchildAST.setType(123);
        grandchildAST.addChild(new DetailAST());
        childAST.addChild(grandchildAST);
        ast.addChild(childAST);
        String annotation = "WrongAnnotation";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        assertNull(result);
    }

    @Test
    public void getAnnotation_WhitespaceAnnotation_ThrowsIllegalArgumentException() {
        // Arrange
        ast.setType(155);
        ast.addChild(new DetailAST());
        String annotation = "   ";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }

    @Test
    public void getAnnotation_NullAST_ThrowsIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;
        String annotation = "TestAnnotation";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }

    @Test
    public void containsAnnotation_WithAnnotationHolder_ReturnsTrue() {
        // Arrange
        ast.setType(155);
        ast.addChild(new DetailAST());
        String annotation = "TestAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertTrue(result);
    }

    @Test
    public void containsAnnotation_WithoutAnnotationHolder_ReturnsFalse() {
        // Arrange
        ast.setType(156);
        ast.addChild(new DetailAST());

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertFalse(result);
    }

    @Test
    public void containsAnnotation_NullAST_ThrowsIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast);
        });
    }

    @Test
    public void getAnnotationHolder_ValidAST_ReturnsExpectedDetailAST() {
        // Arrange
        ast.setType(155);
        DetailAST childAST = new DetailAST();
        childAST.setType(159);
        ast.addChild(childAST);

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
        assertEquals(159, result.getType());
    }

    @Test
    public void getAnnotationHolder_ASTWithType5_ReturnsExpectedDetailAST() {
        // Arrange
        ast.setType(5);
        DetailAST childAST = new DetailAST();
        childAST.setType(158);
        ast.addChild(childAST);

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
        assertEquals(158, result.getType());
    }

    @Test
    public void getAnnotationHolder_NullAST_ThrowsIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotationHolder(ast);
        });
    }
}