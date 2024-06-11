import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStaticTyping());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = Object.class;
        JavaType elemT = null;

        CollectionType collectionType = CollectionType.construct(rawType, elemT);

        assertEquals(rawType, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType narrowedType = collectionType._narrow(subclass);

        assertEquals(subclass, narrowedType.getRawClass());
        assertNull(narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getInterfaces());
        assertNull(narrowedType.getElementType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType updatedType = collectionType.withContentType(contentType);

        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(contentType, updatedType.getElementType());
        assertEquals(collectionType.getValueHandler(), updatedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), updatedType.getTypeHandler());
        assertEquals(collectionType.isStaticTyping(), updatedType.isStaticTyping());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withTypeHandler(h);

        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType(), updatedType.getElementType());
        assertEquals(collectionType.getValueHandler(), updatedType.getValueHandler());
        assertEquals(h, updatedType.getTypeHandler());
        assertEquals(collectionType.isStaticTyping(), updatedType.isStaticTyping());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withContentTypeHandler(h);

        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType().withTypeHandler(h), updatedType.getElementType());
        assertEquals(collectionType.getValueHandler(), updatedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), updatedType.getTypeHandler());
        assertEquals(collectionType.isStaticTyping(), updatedType.isStaticTyping());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withValueHandler(h);

        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType(), updatedType.getElementType());
        assertEquals(h, updatedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), updatedType.getTypeHandler());
        assertEquals(collectionType.isStaticTyping(), updatedType.isStaticTyping());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withContentValueHandler(h);

        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType().withValueHandler(h), updatedType.getElementType());
        assertEquals(collectionType.getValueHandler(), updatedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), updatedType.getTypeHandler());
        assertEquals(collectionType.isStaticTyping(), updatedType.isStaticTyping());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType updatedType = collectionType.withStaticTyping();

        assertEquals(collectionType.getRawClass(), updatedType.getRawClass());
        assertEquals(collectionType.getBindings(), updatedType.getBindings());
        assertEquals(collectionType.getSuperClass(), updatedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(collectionType.getElementType().withStaticTyping(), updatedType.getElementType());
        assertEquals(collectionType.getValueHandler(), updatedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), updatedType.getTypeHandler());
        assertTrue(updatedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInterfaces, refinedType.getInterfaces());
        assertEquals(collectionType.getElementType(), refinedType.getElementType());
        assertEquals(collectionType.getValueHandler(), refinedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), refinedType.getTypeHandler());
        assertEquals(collectionType.isStaticTyping(), refinedType.isStaticTyping());
    }

    @Test
    public void testToString() {
        Class<?> rawType = Object.class;
        JavaType elemT = null;

        CollectionType collectionType = new CollectionType(rawType, null,
                null, null, elemT, null, null, false);
        
        assertEquals("[collection type; class " + rawType.getName() + ", contains " + elemT + "]", collectionType.toString());
    }
}