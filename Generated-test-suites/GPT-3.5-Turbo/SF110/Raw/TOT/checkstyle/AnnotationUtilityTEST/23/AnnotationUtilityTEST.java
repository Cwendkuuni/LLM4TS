import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    private DetailAST ast;

    @Before
    public void setUp() {
        ast = new DetailAST();
    }

    @Test
    public void testContainsAnnotation_WithNonNullAST_ReturnsFalse() {
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithNullAST_ThrowsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null, "TestAnnotation");
    }

    @Test
    public void testContainsAnnotation_WithASTContainingAnnotation_ReturnsTrue() {
        ast.addChild(createAnnotationNode("TestAnnotation"));
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotation_WithASTNotContainingAnnotation_ReturnsFalse() {
        ast.addChild(createAnnotationNode("AnotherAnnotation"));
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotation_WithASTContainingMultipleAnnotations_ReturnsTrue() {
        ast.addChild(createAnnotationNode("AnotherAnnotation"));
        ast.addChild(createAnnotationNode("TestAnnotation"));
        ast.addChild(createAnnotationNode("YetAnotherAnnotation"));
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotation_WithASTContainingEmptyAnnotations_ReturnsFalse() {
        ast.addChild(createAnnotationNode(""));
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotation_WithASTContainingAnnotationWithSpaces_ReturnsFalse() {
        ast.addChild(createAnnotationNode("    "));
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotation_WithASTContainingNestedAnnotations_ReturnsTrue() {
        DetailAST nestedAnnotation = createAnnotationNode("NestedAnnotation");
        ast.addChild(nestedAnnotation);
        DetailAST childAnnotation = createAnnotationNode("TestAnnotation");
        nestedAnnotation.addChild(childAnnotation);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithNullAnnotation_ThrowsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(ast, null);
    }

    @Test
    public void testGetAnnotationHolder_WithNullAST_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotationHolder(null);
        });
    }

    @Test
    public void testGetAnnotationHolder_WithASTContainingAnnotation_ReturnsAnnotationHolder() {
        ast.addChild(createAnnotationNode("TestAnnotation"));
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_WithASTNotContainingAnnotation_ReturnsNull() {
        ast.addChild(createAnnotationNode("AnotherAnnotation"));
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_WithASTContainingMultipleAnnotations_ReturnsFirstAnnotationHolder() {
        ast.addChild(createAnnotationNode("AnotherAnnotation"));
        ast.addChild(createAnnotationNode("TestAnnotation"));
        ast.addChild(createAnnotationNode("YetAnotherAnnotation"));
        assertEquals(ast.getChild(1), AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_WithASTContainingEmptyAnnotations_ReturnsNull() {
        ast.addChild(createAnnotationNode(""));
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_WithASTContainingAnnotationWithSpaces_ReturnsNull() {
        ast.addChild(createAnnotationNode("    "));
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_WithASTContainingNestedAnnotations_ReturnsOutermostAnnotationHolder() {
        DetailAST nestedAnnotation = createAnnotationNode("NestedAnnotation");
        ast.addChild(nestedAnnotation);
        DetailAST childAnnotation = createAnnotationNode("TestAnnotation");
        nestedAnnotation.addChild(childAnnotation);
        assertEquals(nestedAnnotation, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation_WithNonNullASTAndNonNullAnnotation_ReturnsAnnotationNode() {
        ast.addChild(createAnnotationNode("TestAnnotation"));
        assertNotNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithNullASTAndNonNullAnnotation_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "TestAnnotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithNonNullASTAndNullAnnotation_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithNullASTAndNullAnnotation_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, null);
    }

    @Test
    public void testGetAnnotation_WithASTContainingAnnotation_ReturnsAnnotationNode() {
        ast.addChild(createAnnotationNode("TestAnnotation"));
        assertNotNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotation_WithASTNotContainingAnnotation_ReturnsNull() {
        ast.addChild(createAnnotationNode("AnotherAnnotation"));
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotation_WithASTContainingMultipleAnnotations_ReturnsCorrectAnnotationNode() {
        ast.addChild(createAnnotationNode("AnotherAnnotation"));
        ast.addChild(createAnnotationNode("TestAnnotation"));
        ast.addChild(createAnnotationNode("YetAnotherAnnotation"));
        assertNotNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
        assertNull(AnnotationUtility.getAnnotation(ast, "NonExistentAnnotation"));
    }

    @Test
    public void testGetAnnotation_WithASTContainingEmptyAnnotations_ReturnsNull() {
        ast.addChild(createAnnotationNode(""));
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotation_WithASTContainingAnnotationWithSpaces_ReturnsNull() {
        ast.addChild(createAnnotationNode("    "));
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotation_WithASTContainingNestedAnnotations_ReturnsCorrectAnnotationNode() {
        DetailAST nestedAnnotation = createAnnotationNode("NestedAnnotation");
        ast.addChild(nestedAnnotation);
        DetailAST childAnnotation1 = createAnnotationNode("AnotherAnnotation");
        nestedAnnotation.addChild(childAnnotation1);
        DetailAST childAnnotation2 = createAnnotationNode("TestAnnotation");
        nestedAnnotation.addChild(childAnnotation2);
        assertEquals(childAnnotation2, AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    private DetailAST createAnnotationNode(String annotationName) {
        DetailAST annotationNode = new DetailAST();
        annotationNode.setType(AnnotationUtility.ANNOTATION_TYPE);
        DetailAST nameNode = new DetailAST();
        nameNode.setType(AnnotationUtility.NAME_TYPE);
        nameNode.setText(annotationName);
        annotationNode.addChild(nameNode);
        return annotationNode;
    }

}