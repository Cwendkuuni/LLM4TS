package com.puppycrawl.tools.checkstyle.utils;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;

public final class AnnotationUtility
{
    private static final String THE_AST_IS_NULL = "the ast is null";
    
    private AnnotationUtility() {
        throw new UnsupportedOperationException("do not instantiate.");
    }
    
    public static boolean containsAnnotation(final DetailAST ast, final String annotation) {
        if (ast == null) {
            throw new IllegalArgumentException("the ast is null");
        }
        return getAnnotation(ast, annotation) != null;
    }
    
    public static boolean containsAnnotation(final DetailAST ast) {
        if (ast == null) {
            throw new IllegalArgumentException("the ast is null");
        }
        final DetailAST holder = getAnnotationHolder(ast);
        return holder != null && holder.branchContains(159);
    }
    
    public static DetailAST getAnnotationHolder(final DetailAST ast) {
        if (ast == null) {
            throw new IllegalArgumentException("the ast is null");
        }
        DetailAST annotationHolder;
        if (ast.getType() == 155 || ast.getType() == 16) {
            annotationHolder = ast.findFirstToken(158);
        }
        else {
            annotationHolder = ast.findFirstToken(5);
        }
        return annotationHolder;
    }
    
    public static DetailAST getAnnotation(final DetailAST ast, final String annotation) {
        if (ast == null) {
            throw new IllegalArgumentException("the ast is null");
        }
        if (annotation == null) {
            throw new IllegalArgumentException("the annotation is null");
        }
        if (CharMatcher.WHITESPACE.matchesAllOf(annotation)) {
            throw new IllegalArgumentException("the annotation is empty or spaces");
        }
        final DetailAST holder = getAnnotationHolder(ast);
        for (DetailAST child = holder.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getType() == 159) {
                final DetailAST firstChild = child.getFirstChild();
                final String name = FullIdent.createFullIdent(firstChild.getNextSibling()).getText();
                if (annotation.equals(name)) {
                    return child;
                }
            }
        }
        return null;
    }
}