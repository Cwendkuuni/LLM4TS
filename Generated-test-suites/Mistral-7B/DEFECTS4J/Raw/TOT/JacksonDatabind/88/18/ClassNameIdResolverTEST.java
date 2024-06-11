package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {

    private static final Object[] VALUES = new Object[] {
            new ArrayList<>(), new EnumSet.of(Enum.values()[0]), new HashMap<>(),
            new Object(){}, new Integer(1), new String("test"), new Enum.valueOf(MyEnum.class, "ONE")
    };

    private static final Class<?>[] TYPES = new Class<?>[] {
            ArrayList.class, EnumSet.class, HashMap.class,
            Object.class, Integer.class, String.class, MyEnum.class
    };

    private static final String[] IDS = new String[] {
            "java.util.ArrayList", "java.util.EnumSet", "java.util.HashMap",
            "java.lang.Object", "java.lang.Integer", "java.lang.String",
            "com.test.MyEnum"
    };

    @org.junit.Test
    public void testIdFromValue() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(null, null);
        for (int i = 0; i < VALUES.length; i++) {
            Object value = VALUES[i];
            String id = resolver.idFromValue(value);
            assertEquals(IDS[i], id);
        }
    }

    @org.junit.Test
    public void testIdFromValueAndType() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(null, null);
        for (int i = 0; i < VALUES.length; i++) {
            Object value = VALUES[i];
            Class<?> type = TYPES[i];
            String id = resolver.idFromValueAndType(value, type);
            assertEquals(IDS[i], id);
        }
    }

    @org.junit.Test
    public void testTypeFromId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        TypeIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        for (String id : IDS) {
            JavaType javaType = resolver.typeFromId(null, id);
            assertEquals(TYPES[Arrays.asList(IDs).indexOf(id)], javaType.getRawClass());
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testUnknownTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        TypeIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        resolver.typeFromId(null, "UnknownTypeId");
    }

    static class MyEnum {
        enum MyEnum {
            ONE, TWO, THREE
        }

        public static MyEnum valueOf(String name) {
            return MyEnum.valueOf(name);
        }
    }
}