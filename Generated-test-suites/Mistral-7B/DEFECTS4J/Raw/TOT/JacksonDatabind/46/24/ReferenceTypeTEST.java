package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType simpleType = JavaTypeFactory.instance.constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, simpleType, null, null);

        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(simpleType.getRawClass(), referenceType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeHandlers() {
        JavaType simpleType = JavaTypeFactory.instance.constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, simpleType, null, null);

        JavaType referenceTypeWithValueHandler = referenceType.withValueHandler(new Object());
        JavaType referenceTypeWithContentTypeHandler = referenceType.withContentTypeHandler(new Object());
        JavaType referenceTypeWithTypeHandler = referenceType.withTypeHandler(new Object());

        assertEquals(referenceType.hashCode(), referenceTypeWithValueHandler.hashCode());
        assertEquals(referenceType.hashCode(), referenceTypeWithContentTypeHandler.hashCode());
        assertEquals(referenceType.hashCode(), referenceTypeWithTypeHandler.hashCode());
        assertEquals(referenceType._valueHandler, referenceTypeWithValueHandler._valueHandler);
        assertEquals(referenceType._referencedType, referenceTypeWithContentTypeHandler._referencedType);
        assertEquals(referenceType._typeHandler, referenceTypeWithTypeHandler._typeHandler);
    }

    @Test
    public void testReferenceTypeStaticTyping() {
        JavaType simpleType = JavaTypeFactory.instance.constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, simpleType, null, null);
        JavaType staticReferenceType = referenceType.withStaticTyping();

        assertEquals(Integer.class, staticReferenceType.getRawClass());
        assertEquals(simpleType.getRawClass(), staticReferenceType.getContentType().getRawClass());
        assertEquals(true, staticReferenceType._asStatic);
    }

    @Test
    public void testReferenceTypeNarrow() {
        JavaType simpleType = JavaTypeFactory.instance.constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, simpleType, null, null);

        JavaType narrowType = referenceType._narrow(Long.class);

        assertEquals(Long.class, narrowType.getRawClass());
        assertEquals(simpleType.getRawClass(), narrowType.getContentType().getRawClass());
    }
}