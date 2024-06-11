
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        Assert.assertEquals(cls, referenceType._class);
        Assert.assertEquals(refType, referenceType._referencedType);
    }
    
    @Test
    public void testWithTypeHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        
        Assert.assertEquals(cls, newReferenceType._class);
        Assert.assertEquals(refType, newReferenceType._referencedType);
        Assert.assertEquals(typeHandler, newReferenceType._typeHandler);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(typeHandler);
        
        Assert.assertEquals(cls, newReferenceType._class);
        Assert.assertEquals(refType.withTypeHandler(typeHandler), newReferenceType._referencedType);
    }
    
    @Test
    public void testWithValueHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        
        Assert.assertEquals(cls, newReferenceType._class);
        Assert.assertEquals(refType, newReferenceType._referencedType);
        Assert.assertEquals(valueHandler, newReferenceType._valueHandler);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withContentValueHandler(valueHandler);
        
        Assert.assertEquals(cls, newReferenceType._class);
        Assert.assertEquals(refType.withValueHandler(valueHandler), newReferenceType._referencedType);
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        
        Assert.assertEquals(cls, newReferenceType._class);
        Assert.assertEquals(refType.withStaticTyping(), newReferenceType._referencedType);
        Assert.assertTrue(newReferenceType._asStatic);
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}
