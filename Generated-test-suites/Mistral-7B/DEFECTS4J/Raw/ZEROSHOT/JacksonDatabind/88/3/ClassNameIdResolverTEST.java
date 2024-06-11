package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link ClassNameIdResolver}.
 */
public class ClassNameIdResolverTest {

    private static final Object NULL_VALUE = null;
    private static final Class<?> STRING_CLASS = String.class;
    private static final Class<?> INT_CLASS = Integer.class;
    private static final Class<?> LIST_CLASS = ArrayList.class;
    private static final Class<?> ENUM_CLASS_A = EnumA.class;
    private static final Class<?> ENUM_CLASS_B = EnumB.class;
    private static final Class<?> ENUM_SET_CLASS = EnumSet.of(EnumA.A1, EnumA.A2).getClass();
    private static final Class<?> ENUM_MAP_CLASS = new HashMap<EnumA, String>() {{
        put(EnumA.A1, "value1");
        put(EnumA.A2, "value2");
    }}.getClass();

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws IOException {
        jsonFactory = new JsonFactory();
        jsonFactory.setFailOnEmptyFields(false);
        objectMapper = new ObjectMapper(jsonFactory);
    }

    @Test
    public void testIdFromValue() throws IOException {
        // Test with Java classes
        assertEquals(STRING_CLASS.getName(), ClassNameIdResolver.instance.idFromValue(new String("test")));
        assertEquals(INT_CLASS.getName(), ClassNameIdResolver.instance.idFromValue(42));

        // Test with objects
        assertEquals(LIST_CLASS.getName(), ClassNameIdResolver.instance.idFromValue(new ArrayList<>()));
        assertEquals(ENUM_CLASS_A.getName(), ClassNameIdResolver.instance.idFromValue(EnumA.A1));
        assertEquals(ENUM_CLASS_B.getName(), ClassNameIdResolver.instance.idFromValue(EnumB.B1));

        // Test with null value
        assertNull(ClassNameIdResolver.instance.idFromValue(NULL_VALUE));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        // Test with Java classes
        assertEquals(STRING_CLASS.getName(), ClassNameIdResolver.instance.idFromValueAndType(new String("test"), STRING_CLASS));
        assertEquals(INT_CLASS.getName(), ClassNameIdResolver.instance.idFromValueAndType(42, INT_CLASS));

        // Test with objects
        assertEquals(LIST_CLASS.getName(), ClassNameIdResolver.instance.idFromValueAndType(new ArrayList<>(), LIST_CLASS));
        assertEquals(ENUM_CLASS_A.getName(), ClassNameIdResolver.instance.idFromValueAndType(EnumA.A1, ENUM_CLASS_A));
        assertEquals(ENUM_CLASS_B.getName(), ClassNameIdResolver.instance.idFromValueAndType(EnumB.B1, ENUM_CLASS_B));

        // Test with null value
        assertNull(ClassNameIdResolver.instance.idFromValueAndType(NULL_VALUE, STRING_CLASS));
    }

    @Test
    public void testTypeFromId() throws IOException {
        // Test with Java classes
        JavaType stringType = ClassNameIdResolver.instance.typeFromId(jsonFactory.getDeserializationContext(), STRING_CLASS.getName());
        assertEquals(STRING_CLASS, stringType.getRawClass());

        JavaType intType = ClassNameIdResolver.instance.typeFromId(jsonFactory.getDeserializationContext(), INT_CLASS.getName());
        assertEquals(INT_CLASS, intType.getRawClass());

        // Test with collections
        JavaType enumSetType = ClassNameIdResolver.instance.typeFromId(jsonFactory.getDeserializationContext(), ENUM_SET_CLASS.getName());
        assertEquals(EnumSet.class, enumSetType.getRawClass());
        TypeReference<EnumSet<EnumA>> enumSetTypeRef = new TypeReference<EnumSet<EnumA>>() {};
        assertEquals(enumSetTypeRef.getType(), enumSetType);

        JavaType enumMapType = ClassNameIdResolver.instance.typeFromId(jsonFactory.getDeserializationContext(), ENUM_MAP_CLASS.getName());
        assertEquals(HashMap.class, enumMapType.getRawClass());
        TypeReference<Map<EnumA, String>> enumMapTypeRef = new TypeReference<Map<EnumA, String>>() {};
        assertEquals(enumMapTypeRef.getType(), enumMapType);

        // Test with inner classes
        JavaType listType = ClassNameIdResolver.instance.typeFromId(jsonFactory.getDeserializationContext(), LIST_CLASS.getName());
        assertEquals(ArrayList.class, listType.getRawClass());

        // Test with null id
        assertNull(ClassNameIdResolver.instance.typeFromId(jsonFactory.getDeserializationContext(), null));

        // Test with unknown id
        assertNull(ClassNameIdResolver.instance.typeFromId(jsonFactory.getDeserializationContext(), "com.unknown.UnknownClass"));
    }

    @Test(expected = JsonMappingException.class)
    public void testTypeFromIdWithInvalidId() throws IOException {
        // Test with invalid id
        ClassNameIdResolver.instance.typeFromId(jsonFactory.getDeserializationContext(), "InvalidId");
    }

    @Test
    public void testIdFromValueWithEnum() throws IOException {
        String id = ClassNameIdResolver.instance.idFromValue(EnumA.A1);
        assertEquals(ENUM_CLASS_A.getName(), id);
    }

    @Test
    public void testIdFromValueWithArray() throws IOException {
        String id = ClassNameIdResolver.instance.idFromValue(new int[] { 1, 2, 3 });
        assertNotNull(id);
        assertFalse(id.isEmpty());
        assertTrue(id.contains("int[]"));
    }
}

class EnumA {
    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="@class")
    public static enum EnumA {
        A1, A2;
    }
}

class EnumB {
    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="@class")
    public static enum EnumB {
        B1, B2;
    }
}