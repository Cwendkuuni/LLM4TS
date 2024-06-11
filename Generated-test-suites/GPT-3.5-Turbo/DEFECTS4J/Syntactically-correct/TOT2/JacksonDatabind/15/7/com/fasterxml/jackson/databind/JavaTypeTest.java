import static org.junit.Assert.*;
import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testWithStaticTyping() {
        JavaType type = new ConcreteJavaType(Integer.class, 0, null, null, false);
        JavaType result = type.withStaticTyping();
        assertTrue(result.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        JavaType type = new ConcreteJavaType(String.class, 0, null, null, false);
        JavaType result = type.narrowBy(Object.class);
        assertEquals(Object.class, result.getRawClass());
    }

    @Test
    public void testForcedNarrowBy() {
        JavaType type = new ConcreteJavaType(Double.class, 0, null, null, false);
        JavaType result = type.forcedNarrowBy(Number.class);
        assertEquals(Number.class, result.getRawClass());
    }

    @Test
    public void testWidenBy() {
        JavaType type = new ConcreteJavaType(Integer.class, 0, null, null, false);
        JavaType result = type.widenBy(Number.class);
        assertEquals(Number.class, result.getRawClass());
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType type = new ConcreteJavaType(Double.class, 0, null, null, false);
        JavaType result = type.narrowContentsBy(Number.class);
        assertEquals(Number.class, result.getContentType().getRawClass());
    }

    @Test
    public void testWidenContentsBy() {
        JavaType type = new ConcreteJavaType(Integer.class, 0, null, null, false);
        JavaType result = type.widenContentsBy(Number.class);
        assertEquals(Number.class, result.getContentType().getRawClass());
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
        public Class<?> getParameterSource() {
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