
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_withValidAnnotation() {
        // Arrange
        DetailAST ast = getValidAST();
        String annotation = "SampleAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_withInvalidAnnotation() {
        // Arrange
        DetailAST ast = getValidAST();
        String annotation = "InvalidAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAST() {
        // Arrange
        DetailAST ast = null;
        String annotation = "SampleAnnotation";

        // Act
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotation_withNullAnnotation() {
        // Arrange
        DetailAST ast = getValidAST();
        String annotation = null;

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withEmptyAnnotation() {
        // Arrange
        DetailAST ast = getValidAST();
        String annotation = "";

        // Act
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotation_withSpacesAnnotation() {
        // Arrange
        DetailAST ast = getValidAST();
        String annotation = "   ";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        Assert.assertFalse(result);
    }

    private DetailAST getValidAST() {
        // Code to create a valid AST for testing
        return new DetailAST();
    }
}
