package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationShouldThrowIllegalArgumentExceptionWhenAstIsNull() {
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationShouldReturnFalseWhenAstDoesNotContainAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationShouldReturnTrueWhenAstContainsAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        holder.addChild(new DetailAST());
        ast.addChild(holder);
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertTrue(result);
    }

    @Test
    public void testGetAnnotationHolderShouldThrowIllegalArgumentExceptionWhenAstIsNull() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderShouldReturnNullWhenAstTypeIsNotAnnotationOrMethodCall() {
        DetailAST ast = new DetailAST();
        ast.setType(50);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationHolderShouldReturnAnnotationHolderWhenAstTypeIsAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = new DetailAST();
        ast.addChild(holder);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertSame(holder, result);
    }

    @Test
    public void testGetAnnotationHolderShouldReturnAnnotationHolderWhenAstTypeIsMethodCall() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        DetailAST holder = new DetailAST();
        ast.addChild(holder);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertSame(holder, result);
    }

    @Test
    public void testGetAnnotationShouldThrowIllegalArgumentExceptionWhenAstIsNull() {
        try {
            AnnotationUtility.getAnnotation(null, "Test");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationShouldThrowIllegalArgumentExceptionWhenAnnotationIsNull() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationShouldThrowIllegalArgumentExceptionWhenAnnotationIsEmptyOrSpaces() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, "    ");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationShouldReturnNullWhenAnnotationIsNotFound() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = new DetailAST();
        ast.addChild(holder);
        DetailAST child = new DetailAST();
        child.setType(160);
        child.addChild(new DetailAST());
        holder.addChild(child);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "Test");
        assertNull(result);
    }

    @Test
    public void testGetAnnotationShouldReturnAnnotationWhenAnnotationIsFound() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = new DetailAST();
        ast.addChild(holder);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        child.addChild(firstChild);
        holder.addChild(child);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "Test");
        assertSame(child, result);
    }

}