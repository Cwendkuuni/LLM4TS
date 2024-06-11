import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    private final DetailAST validAst = new DetailAST();
    private final DetailAST nullAst = null;
    private final String validAnnotation = "TestAnnotation";
    private final String nullAnnotation = null;
    private final String emptyAnnotation = "";
    private final String spaceAnnotation = " ";
    
    @Test
    public void testContainsAnnotation_ValidAstAndValidAnnotation_ReturnsTrue() {
        boolean result = AnnotationUtility.containsAnnotation(validAst, validAnnotation);
        assertTrue(result);
    }
    
    @Test
    public void testContainsAnnotation_ValidAstAndNullAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(validAst, nullAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_ValidAstAndEmptyAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(validAst, emptyAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_ValidAstAndSpaceAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(validAst, spaceAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_NullAst_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(nullAst, validAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_NullAstAndValidAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(nullAst, validAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_ValidAstAndNonExistingAnnotation_ReturnsFalse() {
        boolean result = AnnotationUtility.containsAnnotation(validAst, "NonExistingAnnotation");
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotation_NullAstAndNullAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(nullAst, nullAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_NullAstAndEmptyAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(nullAst, emptyAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_NullAstAndSpaceAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(nullAst, spaceAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_NullAstAndNonExistingAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(nullAst, "NonExistingAnnotation");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(validAst, nullAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationHolder_ValidAst_ReturnsNotNull() {
        DetailAST result = AnnotationUtility.getAnnotationHolder(validAst);
        assertNotNull(result);
    }
    
    @Test
    public void testGetAnnotationHolder_ValidAstWithoutAnnotation_ReturnsNull() {
        DetailAST result = AnnotationUtility.getAnnotationHolder(validAst);
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotationHolder_NullAst_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotationHolder(nullAst);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_ValidAstAndValidAnnotation_ReturnsNotNull() {
        DetailAST result = AnnotationUtility.getAnnotation(validAst, validAnnotation);
        assertNotNull(result);
    }
    
    @Test
    public void testGetAnnotation_ValidAstAndNonExistingAnnotation_ReturnsNull() {
        DetailAST result = AnnotationUtility.getAnnotation(validAst, "NonExistingAnnotation");
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotation_ValidAstAndNullAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(validAst, nullAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_ValidAstAndEmptyAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(validAst, emptyAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_ValidAstAndSpaceAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(validAst, spaceAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAst_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(nullAst, validAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAstAndValidAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(nullAst, validAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(validAst, nullAnnotation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationHolder_NullAst_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotationHolder(nullAst);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
}

