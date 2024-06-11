package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new JavaTypeTestClass();
        JavaType result = javaType.withStaticTyping();

        assertTrue(result.useStaticType());
    }

    @Test
    public void testNarrowBy_sameClass() {
        JavaType javaType = new JavaTypeTestClass();
        JavaType result = javaType.narrowBy(JavaTypeTestClass.class);

        assertSame(javaType, result);
    }

    @Test
    public void testForcedNarrowBy_sameClass() {
        JavaType javaType = new JavaTypeTestClass();
        JavaType result = javaType.forcedNarrowBy(JavaTypeTestClass.class);

        assertSame(javaType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowBy_incompatibleClass() {
        JavaType javaType = new JavaTypeTestClass();
        javaType.narrowBy(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForcedNarrowBy_incompatibleClass() {
        JavaType javaType = new JavaTypeTestClass();
        javaType.forcedNarrowBy(String.class);
    }

    @Test
    public void testWidenBy_sameClass() {
        JavaType javaType = new JavaTypeTestClass();
        JavaType result = javaType.widenBy(JavaTypeTestClass.class);

        assertSame(javaType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenBy_incompatibleClass() {
        JavaType javaType = new JavaTypeTestClass();
        javaType.widenBy(String.class);
    }

    @Test
    public void testGetValueHandler() {
        JavaType javaType = new JavaTypeTestClass();
        Object valueHandler = javaType.getValueHandler();

        assertNull(valueHandler);
    }

    private class JavaTypeTestClass extends JavaType {
        public JavaTypeTestClass() {
            super(JavaType.class, 0, null, null, false);
        }

        @Override
        public JavaType withTypeHandler(Object h) {
            return null;
        }

        @Override
        public JavaType withContentTypeHandler(Object h) {
            return null;
        }

        @Override
        public JavaType withValueHandler(Object h) {
            return null;
        }

        @Override
        public JavaType withContentValueHandler(Object h) {
            return null;
        }

        @Override
        public JavaType withStaticTyping() {
            return null;
        }

        @Override
        public JavaType narrowContentsBy(Class<?> contentClass) {
            return null;
        }

        @Override
        public JavaType widenContentsBy(Class<?> contentClass) {
            return null;
        }

        @Override
        protected JavaType _narrow(Class<?> subclass) {
            return null;
        }

        @Override
        public Class<?> getParameterSource() {
            return null;
        }

        @Override
        public boolean isContainerType() {
            return false;
        }

        @Override
        public String toString() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }
}