
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;

public class AnnotationUtilityTest {

    private DetailAST ast;

    @Before
    public void setUp() {
        ast = new DetailAST();
    }

    @Test
    public void testContainsAnnotation_withNonNullASTAndExistingAnnotation_true() {
        // Arrange
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        DetailAST nameIdentifier = new DetailAST();
        nameIdentifier.setType(FullIdent.createFullIdent(annotation).getType());
        nameIdentifier.setText("MyAnnotation");
        annotation.addChild(nameIdentifier);
        annotationHolder.addChild(annotation);

        // Act
        boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast, "MyAnnotation");

        // Assert
        assertTrue(containsAnnotation);
    }

    @Test
    public void testContainsAnnotation_withNonNullASTAndNonExistingAnnotation_false() {
        // Arrange
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        DetailAST nameIdentifier = new DetailAST();
        nameIdentifier.setType(FullIdent.createFullIdent(annotation).getType());
        nameIdentifier.setText("MyAnnotation");
        annotation.addChild(nameIdentifier);
        annotationHolder.addChild(annotation);

        // Act
        boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast, "AnotherAnnotation");

        // Assert
        assertFalse(containsAnnotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAST_throwsIllegalArgumentException() {
        // Act
        AnnotationUtility.containsAnnotation(null, "MyAnnotation");
    }

    @Test
    public void testContainsAnnotation_withoutAnnotationHolder_false() {
        // Act
        boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertFalse(containsAnnotation);
    }

    @Test
    public void testGetAnnotationHolder_withASTContainingAnnotationHolder_returnsAnnotationHolder() {
        // Arrange
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        annotationHolder.setType(158);

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertSame(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolder_withASTNotContainingAnnotationHolder_returnsNull() {
        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_withNullAST_throwsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotation_withNonNullASTExistingAnnotation_returnsAnnotation() {
        // Arrange
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        DetailAST nameIdentifier = new DetailAST();
        nameIdentifier.setType(FullIdent.createFullIdent(annotation).getType());
        nameIdentifier.setText("MyAnnotation");
        annotation.addChild(nameIdentifier);
        annotationHolder.addChild(annotation);

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation");

        // Assert
        assertSame(annotation, result);
    }

    @Test
    public void testGetAnnotation_withNonNullASTNonExistingAnnotation_returnsNull() {
        // Arrange
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        DetailAST nameIdentifier = new DetailAST();
        nameIdentifier.setType(FullIdent.createFullIdent(annotation).getType());
        nameIdentifier.setText("MyAnnotation");
        annotation.addChild(nameIdentifier);
        annotationHolder.addChild(annotation);

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, "AnotherAnnotation");

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAST_throwsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotation(null, "MyAnnotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNonNullASTAndNullAnnotation_throwsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNonNullASTAndEmptyAnnotation_throwsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNonNullASTAndAnnotationWithSpacesOnly_throwsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotation(ast, "   ");
    }

}

