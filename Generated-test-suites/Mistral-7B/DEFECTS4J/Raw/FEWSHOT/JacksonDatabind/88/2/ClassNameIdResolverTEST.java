package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private TypeFactory typeFactory;
    private ClassNameIdResolver resolver;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        typeFactory = new ObjectMapper().getTypeFactory();
        resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
    }

    @Test
    public void testIdFromValue() {
        Object[] values = new Object[] {
                "HelloWorld.java",
                new String("HelloWorld"),
                new Integer(42),
                new EnumTest.MyEnum("FIRST"),
                new EnumSet.of(EnumTest.MyEnum.SECOND, EnumTest.MyEnum.THIRD),
                new HashMap<String, String>()
        };

        for (Object value : values) {
            String id = resolver.idFromValue(value);
            String expectedId;

            if (value instanceof String) {
                expectedId = ((String) value).replace(".", "/");
            } else {
                expectedId = ClassUtil.classNameToSimpleName(value.getClass());
            }

            assertEquals(expectedId, id);
        }
    }

    @Test
    public void testIdFromValueAndType() {
        String[] types = new String[] {
                "java.lang.String",
                "com.fasterxml.jackson.core.util.DefaultPrePreParser$Token",
                "java.util.EnumSet<com.fasterxml.jackson.databind.JsonTest$EnumTest$MyEnum>"
        };

        Object[] values = new Object[] {
                "HelloWorld.java",
                new String("HelloWorld"),
                new Integer(42),
                new EnumTest.MyEnum("FIRST"),
                new EnumSet.of(EnumTest.MyEnum.SECOND, EnumTest.MyEnum.THIRD),
                new HashMap<String, String>()
        };

        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            Object value = values[i];
            String id = resolver.idFromValueAndType(value, Class.forName(type));
            assertEquals(type, typeFactory.constructType(id));
        }
    }

    @Test
    public void testTypeFromId() throws IOException {
        String[] ids = new String[] {
                "java.lang.String",
                "com.fasterxml.jackson.core.util.DefaultPrePreParser$Token",
                "java.util.EnumSet<com.fasterxml.jackson.databind.JsonTest$EnumTest$MyEnum>",
                "java.util.HashMap<java.lang.String, java.lang.String>"
        };

        for (String id : ids) {
            JavaType javaType = resolver.typeFromId(new JsonFactory(), id);
            String expectedType = id.replace('/', '.');
            assertEquals(expectedType, javaType.getClassName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidId() throws IOException {
        resolver.typeFromId(new JsonFactory(), "InvalidTypeId");
    }

    @Test
    public void testIdFromValueWithUnknownClass() {
        Object unknownClass = new Object() {
            // Unknown class definition
        };

        try {
            String id = resolver.idFromValue(unknownClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIdFromValueWithEnumSetAndEnumMap() {
        Class<? extends Enum> enumClass = EnumTest.MyEnum.class;

        Object enumSetValue = new EnumSet.of(EnumTest.MyEnum.FIRST, EnumTest.MyEnum.SECOND);
        String idFromEnumSet = resolver.idFromValue(enumSetValue);
        assertEquals("java.util.EnumSet<" + enumClass.getName() + ">", idFromEnumSet);

        Object enumMapValue = new HashMap<String, String>();
        String idFromEnumMap = resolver.idFromValue(enumMapValue);
        assertEquals("java.util.HashMap<java.lang.String, java.lang.String>", idFromEnumMap);
    }
}