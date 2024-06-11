
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAST_throwsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void testContainsAnnotation_withValidASTAndNullAnnotation_returnFalse() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotation_withValidASTAndEmptyAnnotation_returnFalse() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotation_withValidASTAndNonExistentAnnotation_returnFalse() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "NonExistentAnnotation"));
    }

    @Test
    public void testContainsAnnotation_withValidASTAndExistingAnnotation_returnTrue() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationDetailAST("ExistingAnnotation"));
        assertTrue(AnnotationUtility.containsAnnotation(ast, "ExistingAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAST_throwsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotation_withValidAST_returnFalse() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotation_withValidASTAndAnnotationHolderWithBranch159_returnTrue() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationDetailAST("ExistingAnnotation"));
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_withNullAST_throwsIllegalArgumentException() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_withValidASTAndAnnotationWithBranch158_returnNotNullAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationDetailAST("ExistingAnnotation")).setType(155);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_withValidASTAndAnnotationWithDifferentType_returnNotNullAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationDetailAST("ExistingAnnotation")).setType(16);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_withValidASTAndAnnotationWithDifferentTypeAndNoAnnotation_returnNotNullAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationDetailAST(null)).setType(16);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAST_throwsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withValidASTAndNullAnnotation_throwsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withValidASTAndEmptyAnnotation_throwsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withValidASTAndEmptySpacesAnnotation_throwsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void testGetAnnotation_withValidASTAndNonExistentAnnotation_returnsNull() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "NonExistentAnnotation"));
    }

    @Test
    public void testGetAnnotation_withValidASTAndExistingAnnotation_returnsAnnotationDetailAST() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationDetailAST("ExistingAnnotation"));
        assertNotNull(AnnotationUtility.getAnnotation(ast, "ExistingAnnotation"));
    }

    private DetailAST createAnnotationDetailAST(String name) {
        DetailAST annotationAST = new DetailAST();
        annotationAST.addChild(createFullIdentDetailAST(name));
        annotationAST.setType(159);
        return annotationAST;
    }

    private DetailAST createFullIdentDetailAST(String name) {
        DetailAST fullIdentAST = new DetailAST();
        fullIdentAST.setText(name);
        return fullIdentAST;
    }
}
