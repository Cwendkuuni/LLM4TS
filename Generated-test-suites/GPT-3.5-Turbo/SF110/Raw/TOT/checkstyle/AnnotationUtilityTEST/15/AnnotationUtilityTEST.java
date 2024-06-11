
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationUtilityTest {

    @Test
    void containsAnnotation_WithNonNullAstAndExistingAnnotation_ShouldReturnTrue() {
        // Arrange
        DetailAST ast = createDummyAst();
        String annotation = "SomeAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void containsAnnotation_WithNonNullAstAndNonExistingAnnotation_ShouldReturnFalse() {
        // Arrange
        DetailAST ast = createDummyAst();
        String annotation = "NonExistingAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void containsAnnotation_WithNullAst_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;
        String annotation = "SomeAnnotation";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, annotation);
        });
    }

    @Test
    void containsAnnotation_WithNullAnnotation_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = createDummyAst();
        String annotation = null;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, annotation);
        });
    }

    @Test
    void containsAnnotation_WithEmptyAnnotation_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = createDummyAst();
        String annotation = "";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, annotation);
        });
    }

    @Test
    void containsAnnotation_WithSpacesOnlyAnnotation_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = createDummyAst();
        String annotation = "   ";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, annotation);
        });
    }

    @Test
    void containsAnnotation_WithNullAst_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast);
        });
    }

    @Test
    void containsAnnotation_WithAstContainingAnnotation_ShouldReturnTrue() {
        // Arrange
        DetailAST ast = createDummyAstWithAnnotation();

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void containsAnnotation_WithAstNotContainingAnnotationAndNonExistingBranch_ShouldReturnFalse() {
        // Arrange
        DetailAST ast = createDummyAst();

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void containsAnnotation_WithAstNotContainingAnnotationAndExistingBranch_ShouldReturnFalse() {
        // Arrange
        DetailAST ast = createDummyAstWithBranch();

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void getAnnotationHolder_WithNonNullAstAndAnnotationHolder_ShouldReturnAnnotationHolder() {
        // Arrange
        DetailAST ast = createDummyAstWithAnnotation();

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(158, result.getType());
    }

    @Test
    void getAnnotationHolder_WithNonNullAstAndNoAnnotationHolder_ShouldReturnNull() {
        // Arrange
        DetailAST ast = createDummyAst();

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        Assertions.assertNull(result);
    }

    @Test
    void getAnnotation_WithNonNullAstAndExistingAnnotation_ShouldReturnAnnotation() {
        // Arrange
        DetailAST ast = createDummyAstWithAnnotation();
        String annotation = "ExistingAnnotation";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(159, result.getType());
    }

    @Test
    void getAnnotation_WithNonNullAstAndNonExistingAnnotation_ShouldReturnNull() {
        // Arrange
        DetailAST ast = createDummyAstWithAnnotation();
        String annotation = "NonExistingAnnotation";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        Assertions.assertNull(result);
    }

    @Test
    void getAnnotation_WithNullAst_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;
        String annotation = "SomeAnnotation";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }

    @Test
    void getAnnotation_WithNonNullAstAndNullAnnotation_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = createDummyAstWithAnnotation();
        String annotation = null;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }

    @Test
    void getAnnotation_WithNonNullAstAndEmptyAnnotation_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = createDummyAstWithAnnotation();
        String annotation = "";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }

    @Test
    void getAnnotation_WithNonNullAstAndSpacesOnlyAnnotation_ShouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = createDummyAstWithAnnotation();
        String annotation = "   ";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }

    private DetailAST createDummyAst() {
        return new DetailAST();
    }

    private DetailAST createDummyAstWithAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST(158);
        ast.setFirstChild(annotationHolder);

        return ast;
    }

    private DetailAST createDummyAstWithBranch() {
        DetailAST ast = new DetailAST();
        DetailAST branch = new DetailAST(159);
        ast.setFirstChild(branch);

        return ast;
    }
}
