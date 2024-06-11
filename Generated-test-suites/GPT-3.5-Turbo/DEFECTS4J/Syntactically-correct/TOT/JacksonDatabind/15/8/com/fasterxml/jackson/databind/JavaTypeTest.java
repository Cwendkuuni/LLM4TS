import org.junit.Test;

import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testWithGenericTypeHandler() {
        JavaType javaType = new ConcreteJavaType(Object.class, 0, null, null, false);
        Object handler = new Object();
        JavaType modifiedType = javaType.withTypeHandler(handler);
        assertEquals(handler, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithGenericValueHandler() {
        JavaType javaType = new ConcreteJavaType(Object.class, 0, null, null, false);
        Object handler = new Object();
        JavaType modifiedType = javaType.withValueHandler(handler);
        assertEquals(handler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new ConcreteJavaType(Object.class, 0, null, null, false);
        JavaType modifiedType = javaType.withStaticTyping();
        assertTrue(modifiedType.useStaticType());
    }

    private static class ConcreteJavaType extends JavaType {
        public ConcreteJavaType(Class<?> raw, int additionalHash, Object valueHandler, Object typeHandler, boolean asStatic) {
            super(raw, additionalHash, valueHandler, typeHandler, asStatic);
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