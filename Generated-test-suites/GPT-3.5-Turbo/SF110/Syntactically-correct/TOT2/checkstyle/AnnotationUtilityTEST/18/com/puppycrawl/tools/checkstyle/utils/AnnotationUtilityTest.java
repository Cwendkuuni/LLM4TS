package com.puppycrawl.tools.checkstyle.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation() {
        // typical use case
        DetailAST ast = new DetailAST();
        
        boolean result = AnnotationUtility.containsAnnotation(ast, "TestAnnotation");

        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotation_NullAST() {
        // edge case: null AST
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(null, "TestAnnotation");
        });
    }
    
    // additional test methods for other public methods...

}