package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeReference;

public class ReferenceTypeTest {

    @Test
    public void testReferenceType() {
        // Test basic construction
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(String.class, referenceType.getType());
        assertEquals(refType, referenceType.getReferencedType());
        assertTrue(referenceType.isReferenceType());

        // Test with type handlers
        JavaType valueHandlerType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeHandlerType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceTypeWithHandlers = new ReferenceType(String.class, refType, valueHandlerType, typeHandlerType, false);
        assertEquals(referenceTypeWithHandlers.getClass(), referenceType.getClass());
        assertFalse(referenceTypeWithHandlers._valueHandler.equals(referenceType._valueHandler));
        assertFalse(referenceTypeWithHandlers._typeHandler.equals(referenceType._typeHandler));

        // Test withStaticTyping
        ReferenceType staticReferenceType = referenceTypeWithHandlers.withStaticTyping();
        assertTrue(staticReferenceType._asStatic);
        assertFalse(referenceType._asStatic);

        // Test buildCanonicalName
        assertEquals("java.lang.String[java.lang.String]", referenceType.buildCanonicalName().toString());
        assertEquals("java.lang.String[Ljava.lang.Integer;]", staticReferenceType.buildCanonicalName().toString());

        // Test containedType methods
        assertEquals(1, referenceType.containedTypeCount());
        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));

        // Test narrow method
        ClassType subclass = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType narrowedType = referenceType.narrow(subclass.getType());
        assertEquals(subclass.getType(), narrowedType.getType());
        assertEquals(refType, narrowedType.getReferencedType());

        // Test equals method
        ReferenceType referenceType2 = new ReferenceType(String.class, refType, null, null, false);
        assertTrue(referenceType.equals(referenceType2));

        // Test getErasedSignature and getGenericSignature methods
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
        referenceType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.String;<TT;>', Ljava.lang.String;<TT;>>", sb.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        String expected = "[reference type, class [" + ReferenceType.class.getName() + "], class java.lang.String, referenced type, class java.lang.String]";
        assertEquals(expected, referenceType.toString());

        JavaType refTypeStatic = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType referenceTypeStatic = new ReferenceType(String.class, refTypeStatic, null, null, true);
        expected = "[reference type, class [" + ReferenceType.class.getName() + "], class [Ljava.lang.String;, referenced type, class [Ljava.lang.String;]]";
        assertEquals(expected, referenceTypeStatic.toString());
    }
}