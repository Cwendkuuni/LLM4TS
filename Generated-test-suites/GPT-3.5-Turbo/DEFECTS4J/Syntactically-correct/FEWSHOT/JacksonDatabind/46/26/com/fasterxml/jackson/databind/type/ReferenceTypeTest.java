package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
    }                                   
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        ReferenceType newType = type.withContentTypeHandler(h);
        assertEquals(h, newType.getContentType().getTypeHandler());        
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        ReferenceType newType = type.withContentValueHandler(h);
        assertEquals(h, newType.getContentType().getValueHandler());        
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        ReferenceType newType = type.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(1, type.containedTypeCount());
    }
    
    @Test
    public void testContainedType() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertNull(type.containedType(1));
        assertEquals(String.class, type.containedType(0).getRawClass());
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertNull(type.containedTypeName(1));
        assertEquals("T", type.containedTypeName(0));
    }
    
    @Test
    public void testGetRawClass() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(String.class, type.getRawClass());
    }
    
    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType(Object.class);
        ReferenceType type = ReferenceType.construct(String.class, refType, null, null);
        assertEquals(refType, type.getReferencedType());
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertTrue(type.isReferenceType());
    }
    
    @Test
    public void testToString() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        String expected = "[reference type, class java.lang.String<null>]";
        assertEquals(expected, type.toString());
    }
    
    @Test
    public void testEquals() {
        ReferenceType type1 = ReferenceType.construct(String.class, null, null, null);
        ReferenceType type2 = ReferenceType.construct(String.class, null, null, null);
        assertTrue(type1.equals(type2));
        
        ReferenceType type3 = ReferenceType.construct(Integer.class, null, null, null);
        assertFalse(type1.equals(type3));
    }
}