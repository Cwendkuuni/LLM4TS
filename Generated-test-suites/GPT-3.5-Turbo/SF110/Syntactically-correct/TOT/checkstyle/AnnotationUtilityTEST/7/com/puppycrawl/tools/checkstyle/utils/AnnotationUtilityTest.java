
import static org.junit.Assert.*;
import org.junit.*;

public class AnnotationUtilityTest {

  @Test
  public void containsAnnotation_shouldReturnTrue_ifAnnotationPresent() {
    // Arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // Arbitrary type to simulate annotation holder
    ast.addFirstToken(new DetailAST()); // Simulate valid annotation
  
    // Act
    boolean result = AnnotationUtility.containsAnnotation(ast);
  
    // Assert
    assertTrue(result);
  }

  @Test
  public void containsAnnotation_shouldReturnFalse_ifAnnotationNotPresent() {
    // Arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // Arbitrary type to simulate annotation holder
  
    // Act
    boolean result = AnnotationUtility.containsAnnotation(ast);
  
    // Assert
    assertFalse(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void containsAnnotation_shouldThrowIllegalArgumentException_ifAstIsNull() {
    // Arrange
    DetailAST ast = null;
  
    // Act
    AnnotationUtility.containsAnnotation(ast);
  
    // Assert
    // Expects IllegalArgumentException
  }

  @Test
  public void getAnnotationHolder_shouldReturnNonNull_whenAstNotNull_annotationHolderPresent() {
    // Arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // Arbitrary type to simulate annotation holder
    ast.addFirstToken(new DetailAST()); // Simulate valid annotation
  
    // Act
    DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
  
    // Assert
    assertNotNull(result);
  }

  @Test
  public void getAnnotationHolder_shouldReturnNull_whenAstNotNull_annotationHolderNotPresent() {
    // Arrange
    DetailAST ast = new DetailAST();
    ast.setType(16); // Arbitrary type to simulate non-annotation holder
  
    // Act
    DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
  
    // Assert
    assertNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getAnnotationHolder_shouldThrowIllegalArgumentException_ifAstIsNull() {
    // Arrange
    DetailAST ast = null;
  
    // Act
    AnnotationUtility.getAnnotationHolder(ast);
  
    // Assert
    // Expects IllegalArgumentException
  }

  @Test
  public void getAnnotation_shouldReturnDetailAST_whenAnnotationPresent() {
    // Arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // Arbitrary type to simulate annotation holder
    DetailAST annotation = new DetailAST();
    annotation.setType(159); // Arbitrary type to simulate annotation
    annotation.addChild(new DetailAST()); // Simulate annotation name
    ast.addFirstToken(annotation);
  
    // Act
    DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation");
  
    // Assert
    assertNotNull(result);
  }

  @Test
  public void getAnnotation_shouldReturnNull_whenAnnotationNotPresent() {
    // Arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // Arbitrary type to simulate annotation holder
    DetailAST annotation = new DetailAST();
    annotation.setType(159); // Arbitrary type to simulate non-matching annotation
    annotation.addChild(new DetailAST()); // Simulate annotation name
    ast.addFirstToken(annotation);
  
    // Act
    DetailAST result = AnnotationUtility.getAnnotation(ast, "OtherAnnotation");
  
    // Assert
    assertNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getAnnotation_shouldThrowIllegalArgumentException_ifAstIsNull() {
    // Arrange
    DetailAST ast = null;
  
    // Act
    AnnotationUtility.getAnnotation(ast, "MyAnnotation");
  
    // Assert
    // Expects IllegalArgumentException
  }

  @Test(expected = IllegalArgumentException.class)
  public void getAnnotation_shouldThrowIllegalArgumentException_ifAnnotationIsNull() {
    // Arrange
    DetailAST ast = new DetailAST();
  
    // Act
    AnnotationUtility.getAnnotation(ast, null);
  
    // Assert
    // Expects IllegalArgumentException
  }

  @Test(expected = IllegalArgumentException.class)
  public void getAnnotation_shouldThrowIllegalArgumentException_ifAnnotationIsEmptyOrNull() {
    // Arrange
    DetailAST ast = new DetailAST();
  
    // Act
    AnnotationUtility.getAnnotation(ast, "");
  
    // Assert
    // Expects IllegalArgumentException
  }

}
