package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.types.ClassType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);

        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new ClassType[0], intType);
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(intType, referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);

        JavaType simpleType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, intType);
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(intType, referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeGetters() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);

        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new ClassType[0], intType);
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(intType, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.isContainerType());
        assertFalse(referenceType.isCollectionLikeType());
        assertFalse(referenceType.isMapLikeType());
        assertFalse(referenceType.isStaticType());
        assertFalse(referenceType.isAbstract());
        assertFalse(referenceType.hasGenericParameters());
        assertFalse(referenceType.hasValueHandler());
        assertFalse(referenceType.hasContentValueHandler());
        assertFalse(referenceType.hasTypeHandler());
        assertFalse(referenceType.hasContentTypeHandler());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
        assertNotNull(referenceType.getErasedSignature());
        assertNotNull(referenceType.getGenericSignature());
        assertNotNull(referenceType.toString());
        assertEquals(referenceType, referenceType.withContentType(intType));
        assertEquals(referenceType, referenceType.withTypeHandler(null));
        assertNotNull(referenceType.withValueHandler(null));
        assertNotNull(referenceType.withContentValueHandler(null));
        assertNotNull(referenceType.withStaticTyping());
        assertEquals(referenceType, referenceType.refine(intClass.getComponentType(), TypeBindings.emptyBindings(), null, new ClassType[0]));
    }

    @Test
    public void testReferenceTypeEquals() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType refType1 = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new ClassType[0], intType);
        JavaType refType2 = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new ClassType[0], intType);
        JavaType refType3 = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new ClassType[0], new ClassType(Long.TYPE));
        JavaType simpleType = TypeFactory.defaultInstance().constructType(stringClass);

        assertEquals(refType1, refType1);
        assertEquals(refType1, refType2);
        assertNotEquals(refType1, simpleType);
        assertNotEquals(refType1, refType3);
    }

    @Test
    public void testReferenceTypeToString() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, new ClassType[0], intType);

        String expected = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testReferenceTypeNarrow() {
        // This method is deprecated since 2.7
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new ClassType[0], intType);

        // Should throw an exception as the types are not related
        try {
            JavaType narrowType = referenceType._narrow(String.class);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}