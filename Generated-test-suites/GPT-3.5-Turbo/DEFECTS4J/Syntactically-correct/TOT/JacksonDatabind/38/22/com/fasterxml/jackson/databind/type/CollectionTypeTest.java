import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testConstruct_WithElementTypeOnly() {
        Class<?> rawType = Class.class;
        JavaType elemT = JavaType.class;

        CollectionType collectionType = CollectionType.construct(rawType, elemT);

        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(null, collectionType.getBindings());
        assertNotEquals(null, collectionType.getSuperClass());
        assertEquals(null, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testNarrow() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType narrowedType = collectionType._narrow(Class.class);

        assertNotNull(narrowedType);
        assertEquals(Class.class, narrowedType.getRawClass());
        assertEquals(bindings, narrowedType.getBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertEquals(superInts, narrowedType.getInterfaces());
        assertEquals(elemT, narrowedType.getContentType());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;
        JavaType contentType = JavaType.class;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType newType = collectionType.withContentType(contentType);

        assertNotNull(newType);
        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(superInts, newType.getInterfaces());
        assertEquals(contentType, newType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;
        Object typeHandler = new Object();

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newType = collectionType.withTypeHandler(typeHandler);

        assertNotNull(newType);
        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(superInts, newType.getInterfaces());
        assertEquals(elemT, newType.getContentType());
        assertEquals(typeHandler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;
        Object typeHandler = new Object();

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newType = collectionType.withContentTypeHandler(typeHandler);

        assertNotNull(newType);
        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(superInts, newType.getInterfaces());
        assertNotEquals(elemT, newType.getContentType()); // Content type should be different as it has a new type handler
        assertEquals(typeHandler, newType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;
        Object valueHandler = new Object();

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newType = collectionType.withValueHandler(valueHandler);

        assertNotNull(newType);
        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(superInts, newType.getInterfaces());
        assertEquals(elemT, newType.getContentType());
        assertEquals(valueHandler, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;
        Object valueHandler = new Object();

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newType = collectionType.withContentValueHandler(valueHandler);

        assertNotNull(newType);
        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(superInts, newType.getInterfaces());
        assertNotEquals(elemT, newType.getContentType()); // Content type should be different as it has a new value handler
        assertEquals(valueHandler, newType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newType = collectionType.withStaticTyping();

        assertNotNull(newType);
        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(superInts, newType.getInterfaces());
        assertEquals(elemT, newType.getContentType());
        assertEquals(true, newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;
        Class<?> newRawType = String.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType newSuperClass = JavaType.class;
        JavaType[] newSuperInts = new JavaType[0];

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType newType = collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInts);

        assertNotNull(newType);
        assertEquals(newRawType, newType.getRawClass());
        assertEquals(newBindings, newType.getBindings());
        assertEquals(newSuperClass, newType.getSuperClass());
        assertEquals(newSuperInts, newType.getInterfaces());
        assertEquals(elemT, newType.getContentType());
    }

    @Test
    public void testToString() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.class;

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        String toString = collectionType.toString();

        assertEquals("[collection type; class java.lang.Class, contains class com.fasterxml.jackson.databind.JavaType]", toString);
    }
}