
package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation_NullAST_ExceptionThrown(){
        try{
            AnnotationUtility.containsAnnotation(null);
            Assert.fail("Exception not thrown for null AST");
        } catch (IllegalArgumentException e){
            Assert.assertEquals("the ast is null",e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationHolder_NullAST_ExceptionThrown(){
        try{
            AnnotationUtility.getAnnotationHolder(null);
            Assert.fail("Exception not thrown for null AST");
        } catch (IllegalArgumentException e){
            Assert.assertEquals("the ast is null",e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAST_ExceptionThrown(){
        try{
            AnnotationUtility.getAnnotation(null,"@SuppressWarnings");
            Assert.fail("Exception not thrown for null AST");
        } catch (IllegalArgumentException e){
            Assert.assertEquals("the ast is null",e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAnnotation_ExceptionThrown(){
        try{
            AnnotationUtility.getAnnotation(DetailAST.emptyToken(),"");
            Assert.fail("Exception not thrown for null annotation");
        } catch (IllegalArgumentException e){
            Assert.assertEquals("the annotation is empty or spaces",e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_EmptyAnnotation_ExceptionThrown(){
        try{
            AnnotationUtility.getAnnotation(DetailAST.emptyToken(),"   ");
            Assert.fail("Exception not thrown for empty annotation");
        } catch (IllegalArgumentException e){
            Assert.assertEquals("the annotation is empty or spaces",e.getMessage());
        }
    }
}
