
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_WhenAstIsNull_ShouldThrowIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationIsNull_ShouldThrowIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(new DetailAST(), null);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationIsEmptyOrSpaces_ShouldThrowIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(new DetailAST(), "   ");
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationIsPresent_ShouldReturnTrue() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getFirstChild().setType(159);
        ast.getFirstChild().addChild(new DetailAST());
        ast.getFirstChild().getFirstChild().setType(154);
        ast.getFirstChild().getFirstChild().setText("MyAnnotation");

        assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotation_WhenAnnotationIsNotPresent_ShouldReturnFalse() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getFirstChild().setType(159);
        ast.getFirstChild().addChild(new DetailAST());
        ast.getFirstChild().getFirstChild().setType(154);
        ast.getFirstChild().getFirstChild().setText("MyAnnotation");

        assertFalse(AnnotationUtility.containsAnnotation(ast, "NotPresentAnnotation"));
    }

    @Test
    public void testContainsAnnotation_WhenHolderIsNull_ShouldReturnFalse() {
        assertFalse(AnnotationUtility.containsAnnotation(new DetailAST()));
    }

    @Test
    public void testGetAnnotationHolder_WhenAstIsNull_ShouldThrowIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolder_WhenAstIsAnnotation_ShouldReturnItself() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());

        assertEquals(ast.getFirstChild(), AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_WhenAstIsNotAnnotation_ShouldReturnAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(156);
        ast.addChild(new DetailAST());
        ast.getFirstChild().setType(155);
        ast.getFirstChild().addChild(new DetailAST());

        assertEquals(ast.getFirstChild(), AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_WhenAnnotationHolderIsNotPresent_ShouldReturnNull() {
        DetailAST ast = new DetailAST();
        ast.setType(154);

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation_WhenAstIsNull_ShouldThrowIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_WhenAnnotationIsNull_ShouldThrowIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(new DetailAST(), null);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_WhenAnnotationIsEmptyOrSpaces_ShouldThrowIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(new DetailAST(), "   ");
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_WhenAnnotationIsPresent_ShouldReturnAnnotationDetailAST() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getFirstChild().setType(159);
        ast.getFirstChild().addChild(new DetailAST());
        ast.getFirstChild().getFirstChild().setType(154);
        ast.getFirstChild().getFirstChild().setText("MyAnnotation");

        assertEquals(ast.getFirstChild().getFirstChild(), AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotation_WhenAnnotationIsNotPresent_ShouldReturnNull() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getFirstChild().setType(159);
        ast.getFirstChild().addChild(new DetailAST());
        ast.getFirstChild().getFirstChild().setType(154);
        ast.getFirstChild().getFirstChild().setText("MyAnnotation");

        assertNull(AnnotationUtility.getAnnotation(ast, "NotPresentAnnotation"));
    }
}
