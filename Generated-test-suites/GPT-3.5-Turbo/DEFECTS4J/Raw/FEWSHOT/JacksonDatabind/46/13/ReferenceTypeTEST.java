
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
        
        assertEquals(String.class, refType.getRawClass());
        assertEquals(null, refType.getValueHandler());
        assertEquals(null, refType.getTypeHandler());
        assertEquals(false, refType.isStatic());
        assertEquals(null, refType.getContentType());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
        ReferenceType staticRefType = refType.withStaticTyping();
        
        assertEquals(String.class, staticRefType.getRawClass());
        assertEquals(null, staticRefType.getValueHandler());
        assertEquals(null, staticRefType.getTypeHandler());
        assertEquals(true, staticRefType.isStatic());
        assertEquals(null, staticRefType.getContentType());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
        ReferenceType contentTypeHandler = refType.withContentTypeHandler(null);
        
        assertEquals(String.class, contentTypeHandler.getRawClass());
        assertEquals(null, contentTypeHandler.getValueHandler());
        assertEquals(null, contentTypeHandler.getTypeHandler());
        assertEquals(false, contentTypeHandler.isStatic());
        assertEquals(null, contentTypeHandler.getContentType().getTypeHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        ReferenceType refType = ReferenceType.construct(String.class, null, null, null);
        ReferenceType contentValueHandler = refType.withContentValueHandler(null);
        
        assertEquals(String.class, contentValueHandler.getRawClass());
        assertEquals(null, contentValueHandler.getValueHandler());
        assertEquals(null, contentValueHandler.getTypeHandler());
        assertEquals(false, contentValueHandler.isStatic());
        assertEquals(null, contentValueHandler.getContentType().getValueHandler());
    }
}
