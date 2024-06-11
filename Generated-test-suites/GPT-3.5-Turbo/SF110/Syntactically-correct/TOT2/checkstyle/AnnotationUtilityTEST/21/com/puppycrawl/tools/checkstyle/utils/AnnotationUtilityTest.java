package com.puppycrawl.tools.checkstyle.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_withValidAnnotation_returnsTrue() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "ValidAnnotation"));
    }

    @Test
    public void testContainsAnnotation_withNullAST_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(null, "ValidAnnotation");
        });
    }

    @Test
    public void testContainsAnnotation_noAnnotation_returnsFalse() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolder_withAnnotationAST_returnsCorrectHolder() {
        DetailAST ast = new DetailAST();
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(holder);
    }

    @Test
    public void testGetAnnotation_withValidAnnotation_returnsCorrectAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "ValidAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_withNullAST_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(null, "ValidAnnotation");
        });
    }

    @Test
    public void testGetAnnotation_withNullAnnotation_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(new DetailAST(), null);
        });
    }

    @Test
    public void testGetAnnotation_withEmptyAnnotation_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(new DetailAST(), "");
        });
    }
}