import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        Class<String> rawType = String.class;
        int additionalHash = 123;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        JavaType javaType = new JavaType(rawType, additionalHash, valueHandler, typeHandler, asStatic) {
            @Override
            public JavaType withTypeHandler(Object h) {
                return new JavaType(this._class, this._hash, this._valueHandler, h, this._asStatic) {};
            }

            @Override
            public JavaType withContentTypeHandler(Object h) {
                return new JavaType(this._class, this._hash, h, this._typeHandler, this._asStatic) {};
            }

            @Override
            public JavaType withValueHandler(Object h) {
                return new JavaType(this._class, this._hash, h, this._typeHandler, this._asStatic) {};
            }

            @Override
            public JavaType withContentValueHandler(Object h) {
                return new JavaType(this._class, this._hash, this._valueHandler, h, this._asStatic) {};
            }

            @Override
            protected JavaType _narrow(Class<?> subclass) {
                return null;
            }

            @Override
            protected JavaType _widen(Class<?> superclass) {
                return new JavaType(superclass, this._hash, this._valueHandler, this._typeHandler, this._asStatic) {};
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
            public Class<?> getRawClass() {
                return this._class;
            }

            @Override
            public boolean hasRawClass(Class<?> clz) {
                return this._class == clz;
            }

            @Override
            public boolean isAbstract() {
                return false;
            }

            @Override
            public boolean isConcrete() {
                return true;
            }

            @Override
            public boolean isThrowable() {
                return false;
            }

            @Override
            public boolean isArrayType() {
                return false;
            }

            @Override
            public boolean isEnumType() {
                return false;
            }

            @Override
            public boolean isInterface() {
                return false;
            }

            @Override
            public boolean isPrimitive() {
                return false;
            }

            @Override
            public boolean isFinal() {
                return false;
            }

            @Override
            public boolean isContainerType() {
                return false;
            }

            @Override
            public boolean isCollectionLikeType() {
                return false;
            }

            @Override
            public boolean isMapLikeType() {
                return false;
            }

            @Override
            public boolean useStaticType() {
                return this._asStatic;
            }

            @Override
            public boolean hasGenericTypes() {
                return false;
            }

            @Override
            public JavaType getKeyType() {
                return null;
            }

            @Override
            public JavaType getContentType() {
                return null;
            }

            @Override
            public int containedTypeCount() {
                return 0;
            }

            @Override
            public JavaType containedType(int index) {
                return null;
            }

            @Override
            public String containedTypeName(int index) {
                return null;
            }

            @Override
            public Class<?> getParameterSource() {
                return null;
            }

            @Override
            public StringBuilder getGenericSignature(StringBuilder sb) {
                return sb;
            }

            @Override
            public StringBuilder getErasedSignature(StringBuilder sb) {
                return sb;
            }
        };

        assertEquals(rawType, javaType.getRawClass());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.useStaticType());
        assertFalse(javaType.hasGenericTypes());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertEquals(0, javaType.containedTypeCount());
        assertNull(javaType.containedType(0));
        assertNull(javaType.containedTypeName(0));
    }

    @Test
    public void testNarrowBy() {
        Class<String> rawType = String.class;
        int additionalHash = 123;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        JavaType javaTypeSource = new JavaType(rawType, additionalHash, valueHandler, typeHandler, asStatic) {};
        Class<Number> subclass = Number.class;

        JavaType javaTypeTarget = javaTypeSource.narrowBy(subclass);

        assertEquals(subclass, javaTypeTarget.getRawClass());
        assertTrue(javaTypeTarget.getClass().isAssignableFrom(javaTypeSource.getClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByFailure() {
        Class<String> rawType = String.class;
        int additionalHash = 123;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        JavaType javaTypeSource = new JavaType(rawType, additionalHash, valueHandler, typeHandler, asStatic) {};
        Class<Integer> subclass = Integer.class;

        JavaType javaTypeTarget = javaTypeSource.narrowBy(subclass);

        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testWidenBy() {
        Class<String> rawType = String.class;
        int additionalHash = 123;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        JavaType javaTypeSource = new JavaType(rawType, additionalHash, valueHandler, typeHandler, asStatic) {};
        Class<Object> superclass = Object.class;

        JavaType javaTypeTarget = javaTypeSource.widenBy(superclass);

        assertEquals(superclass, javaTypeTarget.getRawClass());
        assertTrue(javaTypeTarget.getClass().isAssignableFrom(javaTypeSource.getClass()));
    }

    @Test
    public void testWidenByFailure() {
        Class<String> rawType = String.class;
        int additionalHash = 123;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        JavaType javaTypeSource = new JavaType(rawType, additionalHash, valueHandler, typeHandler, asStatic) {};
        Class<Number> superclass = Number.class;

        JavaType javaTypeTarget = javaTypeSource.widenBy(superclass);

        fail("Should throw IllegalArgumentException");
    }
}