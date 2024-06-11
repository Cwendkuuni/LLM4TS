package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeBinding;
import com.fasterxml.jackson.databind.type.Types;

public class ClassNameIdResolverTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(String.class), new TypeFactory());
        TestBean bean = new TestBean("hello");
        assertEquals("java.lang.String", resolver.idFromValue(bean));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(String.class), new TypeFactory());
        TestBean bean = new TestBean("hello");
        assertEquals("java.lang.String", resolver.idFromValueAndType(bean, String.class));
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(TestBean.class), new TypeFactory());
        TypeIdResolver baseTypeResolver = mapper.getTypeIdResolver();
        String id = resolver.idFromValue(new TestBean("hello"));
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(TestBean.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdWithGenericType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(Map.class), new TypeFactory());
        TypeBinding collectionType = Types.constructCollectionType(HashMap.class, String.class);
        String id = resolver._idFrom(new HashMap<>(), Map.class, new TypeFactory());
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(collectionType.getRawClass(), javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdWithEnum() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(ColorEnum.RED.getClass()), new TypeFactory());
        String id = resolver._idFrom(ColorEnum.RED, ColorEnum.RED.getClass(), new TypeFactory());
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(ColorEnum.RED.getClass(), javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdWithInnerClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(TestInnerClass.class), new TypeFactory());
        TestBean bean = new TestBean("outer");
        String id = resolver._idFrom(bean, TestInnerClass.class, new TypeFactory());
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(TestInnerClass.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdWithCollectionOfEnum() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(EnumSet.class), new TypeFactory());
        EnumSet<ColorEnum> set = EnumSet.allOf(ColorEnum.class);
        String id = resolver._idFrom(set, EnumSet.class, new TypeFactory());
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        TypeReference<?> typeRef = new TypeReference<EnumSet<ColorEnum>>() {}.getType();
        assertEquals(typeRef.getType(), javaType.getType());
    }

    @Test(expected = JsonMappingException.ClassIdMismatch.class)
    public void testTypeFromIdWithMismatchedType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(TestBean.class), new TypeFactory());
        TestBean bean = new TestBean("hello");
        String id = resolver.idFromValue(bean);
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.Integer");
        // This should fail with JsonMappingException.ClassIdMismatch
        mapper.readValue(mapper.getObjectWriter().writeValueAsString(bean), javaType);
    }

    @Test
    public void testIdFromTypeWithArray() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(int[].class), new TypeFactory());
        String id = resolver.idFromValueAndType(new int[0], int[].class);
        assertEquals("int[]", id);
    }

    @Test
    public void testIdFromTypeWithAnonymousClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(new AnonymousClass().getClass()), new TypeFactory());
        String id = resolver.idFromValueAndType(new AnonymousClass(), AnonymousClass.class);
        assertNull(id);
    }

    @Test
    public void testIdFromTypeWithAnonymousStaticClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Types.unconstructedMarkedType(new AnonymousStaticClass().getClass()), new TypeFactory());
        String id = resolver.idFromValueAndType(new AnonymousStaticClass(), AnonymousStaticClass.class);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.AnonymousStaticClass", id);
    }

    static class TestBean {
        private final String value;

        TestBean(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "TestBean{" + value + '}';
        }
    }

    static class TestInnerClass {
        private final String outerValue;

        TestInnerClass(String outerValue) {
            this.outerValue = outerValue;
        }

        public String getOuterValue() {
            return outerValue;
        }
    }

    static class AnonymousClass {
        // empty class
    }

    static class AnonymousStaticClass {
        // empty static class
    }

    enum ColorEnum {
        RED,
        GREEN,
        BLUE
    }
}