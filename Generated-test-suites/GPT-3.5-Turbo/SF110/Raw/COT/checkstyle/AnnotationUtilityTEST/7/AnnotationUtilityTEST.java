import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {
    
    // 
    
    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        boolean actual = AnnotationUtility.containsAnnotation(ast, "TestAnnotation");
        Assert.assertEquals(false, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "TestAnnotation");
    }
    
    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        boolean actual = AnnotationUtility.containsAnnotation(ast, null);
        Assert.assertEquals(false, actual);
    }
    
    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        boolean actual = AnnotationUtility.containsAnnotation(ast, "");
        Assert.assertEquals(false, actual);
    }
    
    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        DetailAST ast = new DetailAST();
        // Simulate annotation holder not containing any annotation
        ast.addFirstChild(new DetailAST());
        boolean actual = AnnotationUtility.containsAnnotation(ast);
        Assert.assertEquals(false, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst2() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotationWithAnnotation() {
        DetailAST ast = new DetailAST();
        // Simulate annotation holder containing an annotation (branch 159)
        ast.addFirstChild(new DetailAST(159));
        boolean actual = AnnotationUtility.containsAnnotation(ast);
        Assert.assertEquals(true, actual);
    }
    
    @Test
    public void testGetAnnotationHolderWithAnnotationHolder() {
        DetailAST ast = new DetailAST();
        // Simulate annotation holder (branch 158)
        ast.addFirstChild(new DetailAST(158));
        DetailAST actual = AnnotationUtility.getAnnotationHolder(ast);
        Assert.assertNotNull(actual);
    }
    
    @Test
    public void testGetAnnotationHolderWithoutAnnotationHolder() {
        DetailAST ast = new DetailAST();
        // Simulate no annotation holder
        ast.addFirstChild(new DetailAST());
        DetailAST actual = AnnotationUtility.getAnnotationHolder(ast);
        Assert.assertNull(actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        // Simulate annotation holder with the desired annotation (branch 159)
        DetailAST annotationHolder = new DetailAST(158);
        DetailAST annotation = new DetailAST(159);
        annotation.addFirstChild(new DetailAST());
        annotation.addFirstChild(new DetailAST());
        annotationHolder.addFirstChild(annotation);
        ast.addFirstChild(annotationHolder);
        
        DetailAST actual = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        Assert.assertNotNull(actual);
    }
    
    @Test
    public void testGetAnnotationWithInvalidAnnotation() {
        DetailAST ast = new DetailAST();
        // Simulate annotation holder with a different annotation (branch 159)
        DetailAST annotationHolder = new DetailAST(158);
        DetailAST annotation = new DetailAST(159);
        annotation.addFirstChild(new DetailAST());
        annotation.addFirstChild(new DetailAST());
        annotationHolder.addFirstChild(annotation);
        ast.addFirstChild(annotationHolder);
        
        DetailAST actual = AnnotationUtility.getAnnotation(ast, "InvalidAnnotation");
        Assert.assertNull(actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        AnnotationUtility.getAnnotation(null, "TestAnnotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    // 
}