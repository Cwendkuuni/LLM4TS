
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
  
  @Test
  public void testContainsAnnotation_ValidAnnotation_ReturnsTrue() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // valid AST type
    String annotation = "MyAnnotation";
    DetailAST annotationChild = new DetailAST();
    annotationChild.setType(159);
    DetailAST firstChild = new DetailAST();
    firstChild.setNextSibling(annotationChild);
    firstChild.setParent(ast);
    ast.setFirstChild(firstChild);
    
    // act
    boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
    
    // assert
    assertTrue(result);
  }
  
  @Test
  public void testContainsAnnotation_InvalidAnnotation_ReturnsFalse() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // valid AST type
    String annotation = "MyAnnotation";
    
    // act
    boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
    
    // assert
    assertFalse(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsAnnotation_NullAST_ThrowsIllegalArgumentException() {
    // act
    AnnotationUtility.containsAnnotation(null, "MyAnnotation");
  }
  
  @Test
  public void testContainsAnnotation_ASTIsNull_ReturnsFalse() {
    // act
    boolean result = AnnotationUtility.containsAnnotation(null);
    
    // assert
    assertFalse(result);
  }
  
  @Test
  public void testContainsAnnotation_ASTWithHolder_ReturnsTrue() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // valid AST type
    DetailAST holder = new DetailAST();
    holder.branchContains(159);
    ast.addChild(holder);
    
    // act
    boolean result = AnnotationUtility.containsAnnotation(ast);
    
    // assert
    assertTrue(result);
  }
  
  @Test
  public void testContainsAnnotation_ASTWithoutHolder_ReturnsFalse() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // valid AST type
    
    // act
    boolean result = AnnotationUtility.containsAnnotation(ast);
    
    // assert
    assertFalse(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsAnnotation_ASTHolderIsNull_ThrowsIllegalArgumentException() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // valid AST type
    DetailAST holder = null;
    ast.addChild(holder);
    
    // act
    AnnotationUtility.containsAnnotation(ast);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotation_NullAST_ThrowsIllegalArgumentException() {
    // act
    AnnotationUtility.getAnnotation(null, "MyAnnotation");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
    // arrange
    DetailAST ast = new DetailAST();
    
    // act
    AnnotationUtility.getAnnotation(ast, null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotation_EmptyAnnotation_ThrowsIllegalArgumentException() {
    // arrange
    DetailAST ast = new DetailAST();
    
    // act
    AnnotationUtility.getAnnotation(ast, "");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotation_WhiteSpaceAnnotation_ThrowsIllegalArgumentException() {
    // arrange
    DetailAST ast = new DetailAST();
    
    // act
    AnnotationUtility.getAnnotation(ast, "   ");
  }
  
  @Test
  public void testGetAnnotation_ValidAnnotation_ReturnsAnnotation() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // valid AST type
    String annotation = "MyAnnotation";
    DetailAST annotationChild = new DetailAST();
    annotationChild.setType(159);
    DetailAST firstChild = new DetailAST();
    firstChild.setNextSibling(annotationChild);
    firstChild.setParent(ast);
    ast.setFirstChild(firstChild);
    
    // act
    DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
    
    // assert
    assertNotNull(result);
  }
  
  @Test
  public void testGetAnnotation_InvalidAnnotation_ReturnsNull() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // valid AST type
    String annotation = "MyAnnotation";
    
    // act
    DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
    
    // assert
    assertNull(result);
  }
  
  @Test
  public void testGetAnnotation_ASTWithoutMatches_ReturnsNull() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // valid AST type
    String annotation = "MyAnnotation";
    DetailAST annotationChild = new DetailAST();
    annotationChild.setType(159);
    DetailAST firstChild = new DetailAST();
    firstChild.setNextSibling(annotationChild);
    firstChild.setParent(ast);
    ast.setFirstChild(firstChild);
    
    // act
    DetailAST result = AnnotationUtility.getAnnotation(ast, "DifferentAnnotation");
    
    // assert
    assertNull(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotation_HolderIsNull_ThrowsIllegalArgumentException() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155); // valid AST type
    DetailAST holder = null;
    ast.addChild(holder);
    
    // act
    AnnotationUtility.getAnnotation(ast, "MyAnnotation");
  }
  
}
