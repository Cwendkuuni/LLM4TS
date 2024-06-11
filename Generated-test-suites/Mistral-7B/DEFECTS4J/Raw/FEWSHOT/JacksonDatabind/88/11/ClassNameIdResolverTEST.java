package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private final DatabindContext _context;
    private final TypeFactory _typeFactory;

    public ClassNameIdResolverTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        _context = mapper.getPolymorphicSerializerProvider().getDeserializationContext();
        _typeFactory = _context.getTypeFactory();
    }

    @Test
    public void testIdFromValue() throws IOException {
        assertEquals("java.lang.String", ClassNameIdResolver.getInstance(_typeFactory, String.class).idFromValue("test"));
        assertEquals("java.util.ArrayList<java.util.HashMap<java.lang.Integer,java.lang.Long>>",
                ClassNameIdResolver.getInstance(_typeFactory, Arrays.asList(new HashMap<Integer, Long>() {{}})).idFromValue(null));
        assertEquals("java.util.EnumSet<java.lang.Enum>", ClassNameIdResolver.getInstance(_typeFactory, EnumSet.of(Enum.VALUES[0])).idFromValue(null));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        Object value = "test";
        assertEquals("java.lang.String", ClassNameIdResolver.getInstance(_typeFactory, String.class).idFromValueAndType(value, String.class));
        assertEquals("java.util.ArrayList<java.util.HashMap<java.lang.Integer,java.lang.Long>>",
                ClassNameIdResolver.getInstance(_typeFactory, ArrayList.class).idFromValueAndType(null, HashMap.class));
        assertEquals("java.util.EnumSet<java.lang.Enum>", ClassNameIdResolver.getInstance(_typeFactory, EnumSet.class).idFromValueAndType(null, Enum.class));
    }

    @Test
    public void testTypeFromId() throws IOException {
        JavaType javaType = ClassNameIdResolver.getInstance(_typeFactory, String.class).typeFromId(_context, "java.lang.String");
        assertEquals(String.class, javaType.getRawClass());
        javaType = ClassNameIdResolver.getInstance(_typeFactory, ArrayList.class).typeFromId(_context, "java.util.ArrayList<java.util.HashMap<java.lang.Integer,java.lang.Long>>");
        assertEquals(ArrayList.class, javaType.getRawClass());
        javaType = ClassNameIdResolver.getInstance(_typeFactory, EnumSet.class).typeFromId(_context, "java.util.EnumSet<java.lang.Enum>");
        assertEquals(EnumSet.class, javaType.getRawClass());
        
        try {
            javaType = ClassNameIdResolver.getInstance(_typeFactory, String.class).typeFromId(_context, "java.util.ArrayList<java.util.HashMap<java.lang.Integer,java.lang.Long>>");
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testTypeFromIdWithGenericType() throws IOException {
        JavaType javaType = ClassNameIdResolver.getInstance(_typeFactory, ArrayList.class).typeFromId(_context, "java.util.ArrayList<java.util.HashMap<java.lang.Integer,java.lang.Long>>");
        assertEquals(ArrayList.class, javaType.getRawClass());
        javaType = ClassNameIdResolver.getInstance(_typeFactory, ArrayList.class).typeFromId(_context, "java.util.ArrayList<java.util.Map<java.lang.Integer,java.lang.Long>>");
        assertEquals(ArrayList.class, javaType.getRawClass());
        
        javaType = ClassNameIdResolver.getInstance(_typeFactory, ArrayList.class).typeFromId(_context, "java.util.List<java.util.Map<java.lang.Integer,java.lang.Long>>");
        assertEquals(ArrayList.class, javaType.getRawClass());
        
        javaType = ClassNameIdResolver.getInstance(_typeFactory, ArrayList.class).typeFromId(_context, "java.util.List");
        assertNull(javaType);
    }

    @Test
    public void testIdFromValueAndTypeWithEnum() throws IOException {
        Class<?> clazz = Enum.class;
        assertEquals("java.lang.Class<java.lang.Enum>", ClassNameIdResolver.getInstance(_typeFactory, clazz).idFromValueAndType(clazz, clazz));
        clazz = EnumSet.class;
        assertEquals("java.util.EnumSet<java.lang.Enum>", ClassNameIdResolver.getInstance(_typeFactory, clazz).idFromValueAndType(EnumSet.of(Enum.VALUES[0]), clazz));
        clazz = HashMap.class;
        assertEquals("java.util.Map<java.lang.Integer,java.lang.Long>", ClassNameIdResolver.getInstance(_typeFactory, clazz).idFromValueAndType(null, clazz));
    }

    @Test
    public void testIdFromValueAndTypeWithInnerClass() throws IOException {
        Class<?> baseClass = Object.class;
        Class<?> innerClass = InnerClass.class;
        assertEquals("java.lang.Object", ClassNameIdResolver.getInstance(_typeFactory, baseClass).idFromValueAndType(null, baseClass));
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.InnerClass", ClassNameIdResolver.getInstance(_typeFactory, innerClass).idFromValueAndType(new InnerClass(), innerClass));
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "className")
    static class InnerClass {
        private String className;
    }
}

class InnerClass {
    // empty class
}