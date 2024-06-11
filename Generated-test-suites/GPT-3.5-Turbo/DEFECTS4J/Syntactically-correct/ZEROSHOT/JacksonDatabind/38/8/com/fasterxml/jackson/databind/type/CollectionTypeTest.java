
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = new CollectionType(TestCollection.class, null, null, null, JavaType.INT_TYPE,
                null, null, false);
    }

    @Test
    public void testConstruct() {
        CollectionType constructedType = CollectionType.construct(TestCollection.class, null, null, null, JavaType.INT_TYPE);
        assertEquals(collectionType, constructedType);
    }

    @Test
    public void testConstructWithDeprecatedMethod() {
        CollectionType constructedType = CollectionType.construct(TestCollection.class, JavaType.INT_TYPE);
        assertEquals(collectionType, constructedType);
    }

    @Test
    public void testNarrow() {
        JavaType narrowedType = collectionType._narrow(AnotherTestCollection.class);
        assertNotEquals(collectionType, narrowedType);
        assertEquals(AnotherTestCollection.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = JavaType.LONG_TYPE;
        JavaType withContentType = collectionType.withContentType(contentType);
        assertNotEquals(collectionType, withContentType);
        assertEquals(contentType, withContentType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertNotEquals(collectionType, withTypeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType contentType = JavaType.LONG_TYPE;
        JavaType contentTypeWithHandler = contentType.withTypeHandler(typeHandler);
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(typeHandler);
        assertNotEquals(collectionType, withContentTypeHandler);
        assertEquals(contentTypeWithHandler.getTypeHandler(), withContentTypeHandler.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertNotEquals(collectionType, withValueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        JavaType contentType = JavaType.STRING_TYPE;
        JavaType contentTypeWithValueHandler = contentType.withValueHandler(valueHandler);
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(valueHandler);
        assertNotEquals(collectionType, withContentValueHandler);
        assertEquals(contentTypeWithValueHandler.getValueHandler(), withContentValueHandler.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType withStaticTyping = collectionType.withStaticTyping();
        assertNotEquals(collectionType, withStaticTyping);
        assertEquals(true, withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = AnotherTestCollection.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotEquals(collectionType, refinedType);
        assertEquals(rawType, refinedType.getRawClass());
    }

    class TestCollection implements java.util.Collection<Integer> {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public java.util.Iterator<Integer> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Integer integer) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(java.util.Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(java.util.Collection<? extends Integer> c) {
            return false;
        }

        @Override
        public boolean removeAll(java.util.Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(java.util.Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    }

    class AnotherTestCollection implements java.util.Collection<Integer> {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public java.util.Iterator<Integer> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Integer integer) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(java.util.Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(java.util.Collection<? extends Integer> c) {
            return false;
        }

        @Override
        public boolean removeAll(java.util.Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(java.util.Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    }
}
