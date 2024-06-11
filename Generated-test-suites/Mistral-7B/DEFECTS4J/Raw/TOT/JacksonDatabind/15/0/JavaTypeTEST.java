package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    private ObjectMapper mapper;
    private TypeFactory typeFactory;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        typeFactory = mapper.getTypeFactory();
    }

    @Test
    public void testJavaTypeBasic() {
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType intType = typeFactory.constructType(int.class);
        JavaType javaType = typeFactory.constructType(JavaType.class);

        assertEquals(String.class, stringType.getRawClass());
        assertEquals(int.class, intType.getRawClass());
        assertTrue(javaType.isAbstract());

        assertEquals(stringType, stringType.narrowBy(String.class));
        assertEquals(intType, intType.widenBy(Number.class));

        JavaType stringArrayType = stringType.narrowBy(String[].class);
        assertEquals(String[].class, stringArrayType.getRawClass());

        JavaType intListType = typeFactory.constructCollectionType(ArrayList.class, int.class);
        JavaType stringListType = intListType.narrowBy(String.class);
        assertEquals(String.class, stringListType.getRawClass());
        assertEquals(ArrayList.class, stringListType.getParameterSource());
        assertEquals(int.class, intListType.getContentType().getRawClass());

        // Test with static typing
        JavaType stringTypeStatic = typeFactory.constructType(String.class).withStaticTyping();
        JavaType intTypeStatic = typeFactory.constructType(int.class).withStaticTyping();
        JavaType javaTypeStatic = typeFactory.constructType(JavaType.class).withStaticTyping();

        assertEquals(stringTypeStatic, stringType.withStaticTyping());
        assertEquals(intTypeStatic, intType.withStaticTyping());
        assertEquals(javaTypeStatic, javaType.withStaticTyping());
    }

    @Test
    public void testJavaTypeWithValueHandler() {
        JavaType stringType = typeFactory.constructType(String.class);
        Object stringValueHandler = new Object() {
            @Override
            public String convertValue(String value) {
                return value.toUpperCase();
            }
        };

        JavaType stringTypeWithValueHandler = stringType.withValueHandler(stringValueHandler);
        String uppercaseString = mapper.writeValueAsString("hello");
        assertEquals("HELLO", mapper.readValue(new TypeReference<String>() {}.getType(), new ObjectInputStream(new ByteArrayInputStream(uppercaseString.getBytes()))).getValue());
        assertEquals("HELLO", mapper.convertValue(mapper.readValue(stringTypeWithValueHandler.getType(), new ObjectInputStream(new ByteArrayInputStream(uppercaseString.getBytes()))), stringTypeWithValueHandler.getValueHandler()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidNarrowing() {
        JavaType intType = typeFactory.constructType(int.class);
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType unknownType = TypeFactory.unknownType();

        intType.narrowBy(String.class);
        intType.narrowBy(String.class); // Should throw exception

        intType.narrowBy(int[].class);
        JavaType intArrayType = typeFactory.constructType(int[].class);
        intType.narrowBy(intArrayType);

        intType.narrowBy(Object.class);
        JavaType objectType = typeFactory.constructType(Object.class);
        intType.narrowBy(objectType);

        intType.narrowBy(unknownType);
    }
}