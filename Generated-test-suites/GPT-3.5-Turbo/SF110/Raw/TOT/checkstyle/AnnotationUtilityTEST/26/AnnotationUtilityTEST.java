
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullDetailAST_ShouldThrowIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
    
    @Test
    public void testContainsAnnotation_AnnotationExists_ShouldReturnTrue() {
        DetailAST ast = new DetailAST();
        // Add code to mock the presence of the annotation in the ast
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testContainsAnnotation_AnnotationDoesNotExist_ShouldReturnFalse() {
        DetailAST ast = new DetailAST();
        // Add code to mock the absence of the annotation in the ast
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullDetailAST_ShouldThrowIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotation_AnnotationHolderExists_ShouldReturnTrue() {
        DetailAST ast = new DetailAST();
        // Add code to mock the presence of an annotation holder in the ast
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testContainsAnnotation_AnnotationHolderDoesNotExist_ShouldReturnFalse() {
        DetailAST ast = new DetailAST();
        // Add code to mock the absence of an annotation holder in the ast
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullDetailAST_ShouldThrowIllegalArgumentException() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationHolder_AnnotationHolderExistsOfType155_ShouldReturnNotNull() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        // Add code to mock the presence of an annotation holder in the ast of type 155
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolder_AnnotationHolderExistsOfType16_ShouldReturnNotNull() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        // Add code to mock the presence of an annotation holder in the ast of type 16
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolder_AnnotationHolderDoesNotExist_ShouldReturnNull() {
        DetailAST ast = new DetailAST();
        // Add code to mock the absence of an annotation holder in the ast
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullDetailAST_ShouldThrowIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation_ShouldThrowIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyOrSpacesAnnotation_ShouldThrowIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }
    
    @Test
    public void testGetAnnotation_AnnotationExists_ShouldReturnNotNull() {
        DetailAST ast = new DetailAST();
        // Add code to mock the presence of the annotation in the ast
        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testGetAnnotation_AnnotationDoesNotExist_ShouldReturnNull() {
        DetailAST ast = new DetailAST();
        // Add code to mock the absence of the annotation in the ast
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }
}
