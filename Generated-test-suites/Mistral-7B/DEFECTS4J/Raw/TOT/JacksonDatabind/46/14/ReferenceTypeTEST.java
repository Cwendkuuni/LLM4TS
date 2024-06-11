package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReferenceTypeTest {

    @Test
    public void testConstructorAndGetters() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, javaStringType, null, null);

        assertEquals(String.class, referenceType.getClass());
        assertEquals(javaStringType.hashCode(), referenceType.getHashCode());
        assertEquals(javaStringType, referenceType.getContentType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testSetters() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        JavaType newTypeHandler = TypeFactory.defaultInstance().constructType(int.class);
        JavaType newContentTypeHandler = TypeFactory.defaultInstance().constructType(double.class);
        JavaType newValueHandler = TypeFactory.defaultInstance().constructType(long.class);
        JavaType newContentValueHandler = TypeFactory.defaultInstance().constructType(boolean.class);

        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler)
                .withContentTypeHandler(newContentTypeHandler)
                .withValueHandler(newValueHandler)
                .withContentValueHandler(newContentValueHandler)
                .withStaticTyping();

        assertEquals(stringClass, newReferenceType.getClass());
        assertEquals(javaStringType.hashCode(), newReferenceType.getHashCode());
        assertEquals(javaStringType, newReferenceType.getContentType());
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
        assertEquals(newContentTypeHandler, newReferenceType.getContentTypeHandler());
        assertTrue(newReferenceType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTypeHandlerToSame() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        JavaType javaStringType2 = TypeFactory.defaultInstance().constructType(String.class);
        referenceType.withTypeHandler(javaStringType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetContentTypeHandlerToSame() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        JavaType javaStringType2 = TypeFactory.defaultInstance().constructType(String.class);
        referenceType.withContentTypeHandler(javaStringType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueHandlerToSame() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        ObjectMapper mapper = new ObjectMapper();
        referenceType.withValueHandler(mapper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetContentValueHandlerToSame() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        ObjectMapper mapper = new ObjectMapper();
        referenceType.withContentValueHandler(mapper);
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        ReferenceType narrowedType = referenceType.narrow(int.class);
        assertEquals(int.class, narrowedType.getClass());
        assertEquals(javaIntType.hashCode(), narrowedType.getHashCode());
        assertEquals(javaIntType, narrowedType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        assertEquals(javaStringType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        JavaType containedType = referenceType.containedType(0);
        assertEquals(javaStringType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        StringBuilder expected = new StringBuilder();
        expected.append(stringClass.getName().replaceAll("\\$", ""));
        StringBuilder result = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        StringBuilder expected = new StringBuilder();
        expected.append(stringClass.getName()).append('<').append("T").append(';');
        StringBuilder result = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaStringType, null, null, false);

        String expected = "[reference type, class " + stringClass.getName() + "<T>" +
                " {Hash = " + javaStringType.hashCode() + "}]";
        String result = referenceType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<String> stringClass1 = String.class;
        JavaType javaStringType1 = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType1 = new ReferenceType(stringClass1, javaStringType1, null, null, false);

        Class<String> stringClass2 = String.class;
        JavaType javaStringType2 = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType2 = new ReferenceType(stringClass2, javaStringType2, null, null, false);

        Class<String> stringClass3 = String.class;
        JavaType javaStringType3 = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType3 = new ReferenceType(stringClass3, javaStringType3, null, null, true);

        Class<String> stringClass4 = Object.class;
        JavaType javaStringType4 = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType4 = new ReferenceType(stringClass4, javaStringType4, null, null, false);

        JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaIntType2 = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType5 = new ReferenceType(int.class, javaIntType, null, null, false);
        ReferenceType referenceType6 = new ReferenceType(int.class, javaIntType2, null, null, false);

        assertEquals(referenceType1, referenceType1);
        assertEquals(referenceType1, referenceType1);
        assertNotEquals(referenceType1, referenceType2);
        assertNotEquals(referenceType1, referenceType3);
        assertNotEquals(referenceType1, referenceType4);
        assertNotEquals(referenceType1, referenceType5);
        assertNotEquals(referenceType1, null);

        assertEquals(referenceType1, new ReferenceType(stringClass1, javaStringType1, null, null, false));
        assertEquals(referenceType1, new ReferenceType(stringClass1, javaStringType1, null, null, true));
        assertEquals(referenceType1, new ReferenceType(stringClass1, javaStringType1, null, null, false).withTypeHandler(null));
        assertEquals(referenceType1, new ReferenceType(stringClass1, javaStringType1, null, null, false).withContentTypeHandler(null));
        assertEquals(referenceType1, new ReferenceType(stringClass1, javaStringType1, null, null, false).withValueHandler(null));
        assertEquals(referenceType1, new ReferenceType(stringClass1, javaStringType1, null, null, false).withContentValueHandler(null));
        assertEquals(referenceType1, new ReferenceType(stringClass1, javaStringType1, null, null, false).withStaticTyping());

        ReferenceType referenceType7 = new ReferenceType(stringClass1, javaStringType1, null, null, false);
        assertFalse(referenceType7.isStatic());

        ReferenceType referenceType8 = new ReferenceType(stringClass1, javaStringType1, null, null, true);
        assertTrue(referenceType8.isStatic());

        assertEquals(referenceType1, referenceType7.withStaticTyping());
        assertEquals(referenceType8, referenceType7.withStaticTyping());
    }
}