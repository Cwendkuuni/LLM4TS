package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testNarrowBy() {
        JavaType type = new JavaTypeTestInstance().narrowBy(JavaType.class);
        assertNotNull(type);
        assertEquals(JavaType.class, type.getRawClass());
    }

    @Test
    public void testForcedNarrowBy() {
        JavaType type = new JavaTypeTestInstance().forcedNarrowBy(JavaType.class);
        assertNotNull(type);
        assertEquals(JavaType.class, type.getRawClass());
    }

    @Test
    public void testWidenBy() {
        JavaType type = new JavaTypeTestInstance().widenBy(ResolvedType.class);
        assertNotNull(type);
        assertEquals(ResolvedType.class, type.getRawClass());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType type = new JavaTypeTestInstance().withStaticTyping();
        assertTrue(type.useStaticType());
    }

    @Test
    public void testContainedType() {
        JavaType type = new JavaTypeTestInstance().containedType(0);
        assertNull(type);
    }

    @Test
    public void testContainedTypeName() {
        JavaType type = new JavaTypeTestInstance().containedTypeName(0);
        assertNull(type);
    }

    @Test
    public void testIsCollectionLikeType() {
        boolean result = new JavaTypeTestInstance().isCollectionLikeType();
        assertFalse(result);
    }

    @Test
    public void testIsMapLikeType() {
        boolean result = new JavaTypeTestInstance().isMapLikeType();
        assertFalse(result);
    }

    private class JavaTypeTestInstance extends JavaType {

        protected JavaTypeTestInstance() {
            super(Object.class, 0, null, null, false);
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
        protected JavaType _narrow(Class<?> subclass) {
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
        public Class<?> getParameterSource() {
            return null;
        }

        @Override
        public boolean isContainerType() {
            return false;
        }

        @Override
        public StringBuilder getGenericSignature(StringBuilder sb) {
            return null;
        }

        @Override
        public StringBuilder getErasedSignature(StringBuilder sb) {
            return null;
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