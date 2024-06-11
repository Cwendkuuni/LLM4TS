package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private TypeFactory typeFactory;
    private DatabindContext context;
    private ClassNameIdResolver resolver;

    @org.junit.Before
    public void setUp() throws Exception {
        typeFactory = new TypeFactoryBuilder().build();
        context = new DeserializationContext(new AnnotationIntrospector(), new ObjectMapper(), typeFactory);
        resolver = new ClassNameIdResolver(null, typeFactory);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        String id = resolver.idFromValueAndType(new Object(), String.class);
        assertEquals("java.lang.String", id);

        id = resolver.idFromValueAndType(new ArrayList<>(), ArrayList.class);
        assertEquals("java.util.ArrayList", id);

        id = resolver.idFromValueAndType(new EnumSet.OfInt(1), EnumSet.OfInt.class);
        assertEquals("java.util.EnumSet", id);

        id = resolver.idFromValueAndType(new EnumMap<String, Integer>(Map.of("k1", 1, "k2", 2)), EnumMap.class);
        assertEquals("java.util.EnumMap", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        JavaType javaType = resolver.typeFromId(context, "java.lang.String");
        assertEquals(String.class, javaType.getRawClass());

        javaType = resolver.typeFromId(context, "java.util.ArrayList");
        assertEquals(ArrayList.class, javaType.getRawClass());

        javaType = resolver.typeFromId(context, "java.util.EnumSet");
        assertEquals(EnumSet.class, javaType.getRawClass());

        javaType = resolver.typeFromId(context, "java.util.EnumMap");
        assertEquals(EnumMap.class, javaType.getRawClass());

        javaType = resolver.typeFromId(context, "java.util.Collections$UnmodifiableListViewSubList");
        assertEquals(List.class, javaType.getRawClass());

        javaType = resolver.typeFromId(context, "java.util.Arrays$ArrayList");
        assertEquals(ArrayList.class, javaType.getRawClass());

        javaType = resolver.typeFromId(context, "com.example.MyEnum");
        assertNotNull(javaType);
        assertEquals(MyEnum.class, javaType.getRawClass());

        javaType = resolver.typeFromId(context, "com.example.MyClass$InnerStaticClass");
        assertNotNull(javaType);
        assertEquals(InnerStaticClass.class, javaType.getRawClass());

        javaType = resolver.typeFromId(context, "com.example.MyClass$InnerInstanceClass");
        assertNotNull(javaType);
        assertEquals(InnerInstanceClass.class, javaType.getRawClass());

        javaType = resolver.typeFromId(context, "com.example.MyClass$InnerAnonymousClass");
        assertNotNull(javaType);
        assertEquals(InnerAnonymousClass.class, javaType.getRawClass());
    }

    @Test
    public void testIdFromValue() throws IOException {
        String id = resolver.idFromValue(new Object());
        assertEquals("java.lang.Object", id);

        id = resolver.idFromValue(new Integer(1));
        assertEquals("int", id);

        id = resolver.idFromValue(new Long(1L));
        assertEquals("long", id);

        id = resolver.idFromValue(new Float(1.0f));
        assertEquals("float", id);

        id = resolver.idFromValue(new Double(1.0));
        assertEquals("double", id);

        id = resolver.idFromValue(new Boolean(true));
        assertEquals("boolean", id);

        id = resolver.idFromValue(new Character('a'));
        assertEquals("char", id);

        id = resolver.idFromValue(new byte[1]);
        assertEquals("byte[]", id);

        id = resolver.idFromValue(new short[1]);
        assertEquals("short[]", id);

        id = resolver.idFromValue(new char[1]);
        assertEquals("char[]", id);

        id = resolver.idFromValue(new int[1]);
        assertEquals("int[]", id);

        id = resolver.idFromValue(new long[1]);
        assertEquals("long[]", id);

        id = resolver.idFromValue(new float[1]);
        assertEquals("float[]", id);

        id = resolver.idFromValue(new double[1]);
        assertEquals("double[]", id);

        id = resolver.idFromValue(new boolean[1]);
        assertEquals("boolean[]", id);

        id = resolver.idFromValue(new String("foo"));
        assertEquals("java.lang.String", id);

        id = resolver.idFromValue(new ArrayList<>());
        assertEquals("java.util.ArrayList", id);

        id = resolver.idFromValue(new EnumSet.OfInt(1));
        assertEquals("java.util.EnumSet", id);

        id = resolver.idFromValue(new EnumMap<String, Integer>(Map.of("k1", 1, "k2", 2)));
        assertEquals("java.util.EnumMap", id);

        id = resolver.idFromValue(new Object[]{1, 2, 3});
        assertEquals("java.lang.Object[]", id);

        id = resolver.idFromValue(new Object[0][0]);
        assertEquals("java.lang.Object[][]", id);

        id = resolver.idFromValue(new Object[0][0][0]);
        assertEquals("java.lang.Object[][][]", id);

        id = resolver.idFromValue(new Object[0][0][0][0]);
        assertEquals("java.lang.Object[][][][]", id);
    }

    static class InnerStaticClass {
    }

    static class InnerInstanceClass {
        int value;

        public InnerInstanceClass(int value) {
            this.value = value;
        }
    }

    static class InnerAnonymousClass {
        int value;

        InnerAnonymousClass(int value) {
            this.value = value;
        }
    }

    static class MyClass {
        static class InnerStaticClass {
        }

        class InnerInstanceClass {
            int value;

            public InnerInstanceClass(int value) {
                this.value = value;
            }
        }

        class InnerAnonymousClass {
            int value;

            InnerAnonymousClass(int value) {
                this.value = value;
            }
        }

        static MyEnum MyEnum;
    }

    enum MyEnum {
        VALUE1, VALUE2
    }
}