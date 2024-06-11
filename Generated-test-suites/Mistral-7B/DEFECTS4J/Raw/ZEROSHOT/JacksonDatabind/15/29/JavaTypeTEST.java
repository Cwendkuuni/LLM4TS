package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);

        assertEquals(String.class, stringJavaType.getRawClass());
        assertEquals(Integer.TYPE, stringJavaType.getContentType().getRawClass());
        assertFalse(stringJavaType.isContainerType());
        assertFalse(stringJavaType.isCollectionLikeType());
        assertFalse(stringJavaType.isMapLikeType());
        assertEquals(false, stringJavaType.useStaticType());
    }

    @Test
    public void testJavaTypeCopyMethods() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType stringJavaTypeWithTypeHandler = stringJavaType.withTypeHandler(new Object() {});
        assertEquals(stringClass, stringJavaTypeWithTypeHandler.getRawClass());
        assertNotNull(stringJavaTypeWithTypeHandler.<Object>getTypeHandler());

        JavaType stringJavaTypeWithContentTypeHandler = stringJavaType.withContentTypeHandler(new Object() {});
        assertEquals(stringClass, stringJavaTypeWithContentTypeHandler.getRawClass());
        assertNotNull(stringJavaTypeWithContentTypeHandler.<Object>getValueHandler());

        JavaType stringJavaTypeWithValueHandler = stringJavaType.withValueHandler(new Object() {});
        assertEquals(stringClass, stringJavaTypeWithValueHandler.getRawClass());
        assertNotNull(stringJavaTypeWithValueHandler.<Object>getValueHandler());

        JavaType stringJavaTypeWithContentValueHandler = stringJavaType.withContentValueHandler(new Object() {});
        assertEquals(stringClass, stringJavaTypeWithContentValueHandler.getRawClass());
        assertNotNull(stringJavaTypeWithContentValueHandler.<Object>getTypeHandler());
        assertNotNull(stringJavaTypeWithContentValueHandler.<Object>getValueHandler());

        JavaType stringJavaTypeWithStaticTyping = stringJavaType.withStaticTyping();
        assertEquals(stringClass, stringJavaTypeWithStaticTyping.getRawClass());
        assertTrue(stringJavaTypeWithStaticTyping.useStaticType());
    }

    @Test
    public void testJavaTypeNarrowingAndWidening() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);

        Class<Integer> integerClass = Integer.class;
        JavaType integerJavaType = stringJavaType.narrowBy(integerClass);
        assertEquals(integerClass, integerJavaType.getRawClass());
        assertEquals(Integer.TYPE, integerJavaType.getContentType().getRawClass());
        assertTrue(integerJavaType.useStaticType());

        Class<String> stringArrayClass = String[].class;
        JavaType stringArrayJavaType = TypeFactory.defaultInstance().constructType(stringArrayClass);
        JavaType stringJavaTypeFromArray = stringArrayJavaType.widenBy(String.class);
        assertEquals(stringClass, stringJavaTypeFromArray.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingWithIncompatibleType() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);

        Class<Integer> integerClass = Integer.TYPE;
        JavaType integerJavaType = stringJavaType.narrowBy(integerClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningWithIncompatibleType() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);

        Class<Integer> integerClass = Integer.TYPE;
        JavaType integerJavaType = TypeFactory.defaultInstance().constructType(integerClass);
        JavaType stringJavaTypeFromInteger = integerJavaType.widenBy(String.class);
    }

    @Test
    public void testJavaTypeIsAbstract() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);

        assertTrue(Modifier.isAbstract(stringClass.getModifiers()));
        assertTrue(stringJavaType.isAbstract());
    }

    @Test
    public void testJavaTypeIsConcrete() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);

        assertFalse(stringJavaType.isConcrete());
        assertTrue(stringClass.isPrimitive());
    }

    @Test
    public void testJavaTypeIsInterface() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);

        assertFalse(stringJavaType.isInterface());
        assertTrue(stringClass.isInterface());
    }

    @Test
    public void testJavaTypeIsEnumType() {
        Class<String> stringEnumClass = String.class;
        JavaType stringEnumJavaType = TypeFactory.defaultInstance().constructType(stringEnumClass);

        assertTrue(stringEnumJavaType.isEnumType());
    }

    @Test
    public void testJavaTypeIsPrimitive() {
        Class<Boolean> booleanClass = Boolean.TYPE;
        JavaType booleanJavaType = TypeFactory.defaultInstance().constructType(booleanClass);

        assertTrue(booleanJavaType.isPrimitive());
    }

    @Test
    public void testJavaTypeHasGenericTypes() {
        Class<List<String>> listStringClass = List.class.cast(new Class[] { String[].class, Collection.class });
        JavaType listStringJavaType = TypeFactory.defaultInstance().constructType(listStringClass);

        assertTrue(listStringJavaType.hasGenericTypes());
    }

    @Test
    public void testJavaTypeGetKeyType() {
        Class<Map.Entry<String, Integer>> entryClass = Map.Entry.class.cast(new Class[] { String.class, Object.class, Map.Entry.class });
        JavaType entryJavaType = TypeFactory.defaultInstance().constructType(entryClass);

        assertNull(entryJavaType.getKeyType());
    }

    @Test
    public void testJavaTypeGetContentType() {
        Class<Map.Entry<String, Integer>> entryClass = Map.Entry.class.cast(new Class[] { String.class, Object.class, Map.Entry.class });
        JavaType entryJavaType = TypeFactory.defaultInstance().constructType(entryClass);

        assertEquals(Integer.TYPE, entryJavaType.getContentType().getRawClass());
    }

    @Test
    public void testJavaTypeContainedTypeCount() {
        Class<Map<String, List<Integer>>> mapListIntegerClass = Map.class.cast(new Class[] { Map.class, String.class, List.class, Collection.class, Map.class });
        JavaType mapListIntegerJavaType = TypeFactory.defaultInstance().constructType(mapListIntegerClass);

        assertEquals(2, mapListIntegerJavaType.containedTypeCount());
    }

    @Test
    public void testJavaTypeContainedType() {
        Class<Map<String, List<Integer>>> mapListIntegerClass = Map.class.cast(new Class[] { Map.class, String.class, List.class, Collection.class, Map.class });
        JavaType mapListIntegerJavaType = TypeFactory.defaultInstance().constructType(mapListIntegerClass);

        JavaType listIntegerJavaType = mapListIntegerJavaType.containedType(0);
        assertEquals(List.class, listIntegerJavaType.getRawClass());

        JavaType stringJavaType = mapListIntegerJavaType.containedType(1);
        assertEquals(String.class, stringJavaType.getRawClass());
    }

    @Test
    public void testJavaTypeContainedTypeOrUnknown() {
        Class<Map<String, List<Integer>>> mapListIntegerClass = Map.class.cast(new Class[] { Map.class, String.class, List.class, Collection.class, Map.class });
        JavaType mapListIntegerJavaType = TypeFactory.defaultInstance().constructType(mapListIntegerClass);

        JavaType stringJavaType = mapListIntegerJavaType.containedTypeOrUnknown(1);
        assertEquals(String.class, stringJavaType.getRawClass());

        JavaType unknownJavaType = mapListIntegerJavaType.containedTypeOrUnknown(10);
        assertEquals(Object.class, unknownJavaType.getRawClass());
    }

    @Test
    public void testJavaTypeGetGenericSignature() {
        Class<Map<String, List<Integer>>> mapListIntegerClass = Map.class.cast(new Class[] { Map.class, String.class, List.class, Collection.class, Map.class });
        JavaType mapListIntegerJavaType = TypeFactory.defaultInstance().constructType(mapListIntegerClass);

        StringBuilder sb = new StringBuilder();
        mapListIntegerJavaType.getGenericSignature(sb);
        String genericSignature = sb.toString();

        System.out.println(genericSignature);
    }

    @Test
    public void testJavaTypeGetErasedSignature() {
        Class<Map<String, List<Integer>>> mapListIntegerClass = Map.class.cast(new Class[] { Map.class, String.class, List.class, Collection.class, Map.class });
        JavaType mapListIntegerJavaType = TypeFactory.defaultInstance().constructType(mapListIntegerClass);

        StringBuilder sb = new StringBuilder();
        mapListIntegerJavaType.getErasedSignature(sb);
        String erasedSignature = sb.toString();

        System.out.println(erasedSignature);
    }
}