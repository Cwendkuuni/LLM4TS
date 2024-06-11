
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object newTypeHandler = new Object();
        ReferenceType resultType = referenceType.withContentTypeHandler(newTypeHandler);
        
        assertNotSame(referenceType, resultType);
        assertSame(cls, resultType.getRawClass());
        assertSame(refType, resultType.getReferencedType());
        assertSame(valueHandler, resultType.getValueHandler());
        assertSame(newTypeHandler, resultType.getReferencedType().getTypeHandler());
        assertFalse(resultType.isStaticTyping());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object newValueHandler = new Object();
        ReferenceType resultType = referenceType.withContentValueHandler(newValueHandler);
        
        assertNotSame(referenceType, resultType);
        assertSame(cls, resultType.getRawClass());
        assertSame(refType, resultType.getReferencedType());
        assertSame(newValueHandler, resultType.getReferencedType().getValueHandler());
        assertSame(typeHandler, resultType.getTypeHandler());
        assertFalse(resultType.isStaticTyping());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType resultType = referenceType.withStaticTyping();
        
        assertNotSame(referenceType, resultType);
        assertSame(cls, resultType.getRawClass());
        assertSame(refType, resultType.getReferencedType());
        assertSame(valueHandler, resultType.getValueHandler());
        assertSame(typeHandler, resultType.getTypeHandler());
        assertTrue(resultType.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String expected = cls.getName() + '<' + refType.toCanonical() + '>';
        assertEquals(expected, referenceType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        StringBuilder sb = new StringBuilder();
        String expected = SignatureHelper.typeErasure(cls).toString();
        assertEquals(expected, referenceType.getErasedSignature(sb).toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        StringBuilder sb = new StringBuilder();
        sb.append('L').append(cls.getName().replace(".", "/")).append('<');
        sb.append(refType.getGenericSignature(null)).append('>');
        sb.append(';');
        String expected = sb.toString();
        assertEquals(expected, referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        StringBuilder sb = new StringBuilder(40)
            .append("[reference type, class ")
            .append(cls.getName())
            .append('<')
            .append(refType)
            .append('>')
            .append(']');

        assertEquals(sb.toString(), referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        JavaType refType1 = new SimpleType(String.class);
        Object valueHandler1 = new Object();
        Object typeHandler1 = new Object();

        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, valueHandler1, typeHandler1, false);

        Class<?> cls2 = Integer.class;
        JavaType refType2 = new SimpleType(Integer.class);
        Object valueHandler2 = new Object();
        Object typeHandler2 = new Object();

        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, valueHandler2, typeHandler2, false);

        assertFalse(referenceType1.equals(referenceType2));
    }
}
