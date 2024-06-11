package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ReferenceTypeTest {
    
    private ReferenceType referenceType;
    
    @Before
    public void setUp() {
        referenceType = new ReferenceType(Class.class, new JavaType(), null, null, false);
    }
    
    @Test
    public void testConstruct() {
        ReferenceType rt = ReferenceType.construct(Class.class, new JavaType(), null, null);
        assertNotNull(rt);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType rt = referenceType.withTypeHandler(h);
        assertSame(h, rt.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType rt = referenceType.withContentTypeHandler(h);
        assertSame(h, rt.getContentTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType rt = referenceType.withValueHandler(h);
        assertSame(h, rt.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType rt = referenceType.withContentValueHandler(h);
        assertSame(h, rt.getContentValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType rt = referenceType.withStaticTyping();
        assertTrue(rt.isStaticTyping());
    }
    
    @Test
    public void testBuildCanonicalName() {
        String name = referenceType.buildCanonicalName();
        assertNotNull(name);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        ReferenceType rt = referenceType._narrow(subclass);
        assertSame(subclass, rt.getRawClass());
    }
    
    @Test
    public void testGetReferencedType() {
        JavaType refType = referenceType.getReferencedType();
        assertNotNull(refType);
    }
    
    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testContainedTypeCount() {
        int count = referenceType.containedTypeCount();
        assertEquals(1, count);
    }
    
    @Test
    public void testContainedType() {
        JavaType type = referenceType.containedType(0);
        assertNotNull(type);
    }
    
    @Test
    public void testContainedTypeName() {
        String name = referenceType.containedTypeName(0);
        assertNotNull(name);
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> source = referenceType.getParameterSource();
        assertSame(Class.class, source);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertNotNull(result);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertNotNull(result);
    }
    
    @Test
    public void testToString() {
        String str = referenceType.toString();
        assertNotNull(str);
    }
    
    @Test
    public void testEquals() {
        assertTrue(referenceType.equals(referenceType));
    }
    
}