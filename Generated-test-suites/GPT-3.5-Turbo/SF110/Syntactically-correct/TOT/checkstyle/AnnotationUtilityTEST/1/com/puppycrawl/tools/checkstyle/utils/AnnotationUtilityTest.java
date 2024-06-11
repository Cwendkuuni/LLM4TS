
package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {
    
    // Test case for containsAnnotation with valid input
    @Test
    public void testContainsAnnotation_withValidInput() {
        DetailAST ast = new DetailAST();
        ast.setType(5); // Non-annotation token
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Test"));
        
        ast.setType(155); // Annotation token
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Test"));
    }
    
    // Test case for containsAnnotation with null input
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullInput() {
        AnnotationUtility.containsAnnotation(null, "Test");
    }
    
    // Test case for containsAnnotation with null annotation
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        AnnotationUtility.containsAnnotation(ast, null);
    }
    
    // Test case for containsAnnotation with empty or spaces annotation
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withEmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        AnnotationUtility.containsAnnotation(ast, "    ");
    }
    
    // Test case for containsAnnotation (error scenario) where findFirstToken returns null
    @Test
    public void testContainsAnnotation_withNullToken() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    // Test case for containsAnnotation (error scenario) where branchContains returns false
    @Test
    public void testContainsAnnotation_withFalseBranchContains() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = new DetailAST(); // Mock holder with type other than 159
        ast.addChild(holder);
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    // Test case for containsAnnotation (typical use case) where branchContains returns true
    @Test
    public void testContainsAnnotation_withTrueBranchContains() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = new DetailAST(); // Mock holder with type 159
        ast.addChild(holder);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }
    
    // Test case for getAnnotationHolder with valid input (annotation token)
    @Test
    public void testGetAnnotationHolder_withAnnotationToken() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
        assertEquals(158, annotationHolder.getType());
    }
    
    // Test case for getAnnotationHolder with valid input (non-annotation token)
    @Test
    public void testGetAnnotationHolder_withNonAnnotationToken() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
        assertEquals(5, annotationHolder.getType());
    }
    
    // Test case for getAnnotationHolder with null input
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_withNullInput() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    // Test case for getAnnotation with valid input (matching annotation found)
    @Test
    public void testGetAnnotation_withMatchingAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = new DetailAST();
        ast.addChild(holder);
        
        DetailAST annotation = new DetailAST(); // Mock matching annotation with type 159
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        holder.addChild(annotation);
        assertEquals(annotation, AnnotationUtility.getAnnotation(ast, "Test"));
    }
    
    // Test case for getAnnotation with valid input (matching annotation not found)
    @Test
    public void testGetAnnotation_withNonMatchingAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = new DetailAST();
        ast.addChild(holder);
        
        DetailAST annotation = new DetailAST(); // Mock non-matching annotation with type 159
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        holder.addChild(annotation);
        assertNull(AnnotationUtility.getAnnotation(ast, "NonMatch"));
    }
    
    // Test case for getAnnotation with null input (ast)
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAst() {
        AnnotationUtility.getAnnotation(null, "Test");
    }
    
    // Test case for getAnnotation with null input (annotation)
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    // Test case for getAnnotation with empty or spaces annotation
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withEmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        AnnotationUtility.getAnnotation(ast, "    ");
    }
}

