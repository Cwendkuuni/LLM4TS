package com.puppycrawl.tools.checkstyle.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotation_NullAST() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(null, "TestAnnotation");
        });
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(holder);
    }

    @Test
    public void testGetAnnotationHolder_NullAST() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotationHolder(null);
        });
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotation_NullAST() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(null, "TestAnnotation");
        });
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, null);
        });
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, "");
        });
    }

    @Test
    public void testGetAnnotation_WhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, "   ");
        });
    }

}