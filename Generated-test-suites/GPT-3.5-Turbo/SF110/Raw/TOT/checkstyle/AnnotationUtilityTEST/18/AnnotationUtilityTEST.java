import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotationUtilityTest {

    private DetailAST ast;

    @BeforeEach
    public void setUp() {
        ast = new DetailAST();
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationExists_ShouldReturnTrue() {
        // Arrange
        String annotation = "TestAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationDoesNotExist_ShouldReturnFalse() {
        // Arrange
        String annotation = "NonExistentAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotation_WhenASTIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST nullAst = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(nullAst, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        String nullAnnotation = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, nullAnnotation));
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationIsEmpty_ShouldThrowIllegalArgumentException() {
        // Arrange
        String emptyAnnotation = "";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, emptyAnnotation));
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationIsSpaces_ShouldThrowIllegalArgumentException() {
        // Arrange
        String spacesAnnotation = "     ";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, spacesAnnotation));
    }

    @Test
    public void testContainsAnnotation_WhenASTIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST nullAst = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(nullAst));
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationHolderIsNull_ShouldReturnFalse() {
        // Arrange
        DetailAST astWithoutAnnotationHolder = new DetailAST();

        // Act
        boolean result = AnnotationUtility.containsAnnotation(astWithoutAnnotationHolder);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetAnnotationHolder_WhenASTContainsAnnotationOfTypes155Or16_ShouldReturnAnnotationHolder() {
        // Arrange
        DetailAST astWithAnnotationOfType155 = new DetailAST(155);
        DetailAST astWithAnnotationOfType16 = new DetailAST(16);

        // Act
        DetailAST result1 = AnnotationUtility.getAnnotationHolder(astWithAnnotationOfType155);
        DetailAST result2 = AnnotationUtility.getAnnotationHolder(astWithAnnotationOfType16);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
    }

    @Test
    public void testGetAnnotationHolder_WhenASTDoesNotContainAnnotationOfType155Or16_ShouldReturnAnnotationHolder() {
        // Arrange
        DetailAST astWithoutAnnotationHolder = new DetailAST();

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(astWithoutAnnotationHolder);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_WhenASTContainsAnnotationWithNameMatching_ShouldReturnAnnotation() {
        // Arrange
        String annotation = "TestAnnotation";
        DetailAST astWithMatchingAnnotation = new DetailAST();

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(astWithMatchingAnnotation, annotation);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_WhenASTDoesNotContainAnnotationWithNameMatching_ShouldReturnNull() {
        // Arrange
        String annotation = "NonExistentAnnotation";
        DetailAST astWithoutMatchingAnnotation = new DetailAST();

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(astWithoutMatchingAnnotation, annotation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_WhenASTIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST nullAst = null;
        String annotation = "TestAnnotation";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(nullAst, annotation));
    }

    @Test
    public void testGetAnnotation_WhenAnnotationIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        String nullAnnotation = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, nullAnnotation));
    }

    @Test
    public void testGetAnnotation_WhenAnnotationIsEmpty_ShouldThrowIllegalArgumentException() {
        // Arrange
        String emptyAnnotation = "";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, emptyAnnotation));
    }

    @Test
    public void testGetAnnotation_WhenAnnotationIsSpaces_ShouldThrowIllegalArgumentException() {
        // Arrange
        String spacesAnnotation = "     ";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, spacesAnnotation));
    }
}