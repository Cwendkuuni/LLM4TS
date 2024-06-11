
import org.junit.Test;
import org.junit.Assert;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAst() {
        // Arrange
        DetailAST ast = null;
        String annotation = "TestAnnotation";

        // Act
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotation_NullAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = null;

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_EmptyAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "";

        // Act
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationHolder() {
        // Arrange
        DetailAST ast = new DetailAST();

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationHolder_NullAst() {
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

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAst() {
        // Arrange
        DetailAST ast = null;
        String annotation = "TestAnnotation";

        // Act
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = null;

        // Act
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "";

        // Act
        AnnotationUtility.getAnnotation(ast, annotation);
    }
}

