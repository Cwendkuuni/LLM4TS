import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setup() {
        collectionType = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
            null, null, StringType.instance);
    }

    @Test
    public void testConstruct() {
        assertNotNull(collectionType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(StringType.instance, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        CollectionType newType = collectionType.withTypeHandler(handler);
        assertNotSame(collectionType, newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(collectionType.getContentType(), newType.getContentType());
        assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        assertEquals(handler, newType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        CollectionType newType = collectionType.withContentTypeHandler(handler);
        assertNotSame(collectionType, newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertNotSame(collectionType.getContentType(), newType.getContentType());
        assertEquals(collectionType.getContentType().getValueHandler(), newType.getContentType().getValueHandler());
        assertEquals(collectionType.getContentType().getTypeHandler(), newType.getContentType().getTypeHandler());
        assertEquals(handler, newType.getContentType().getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(TestClass.class);
        CollectionType newType = collectionType.withContentType(contentType);
        assertNotSame(collectionType, newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertNotSame(collectionType.getContentType(), newType.getContentType());
        assertEquals(contentType, newType.getContentType());
        assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        CollectionType newType = collectionType.withValueHandler(handler);
        assertNotSame(collectionType, newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(collectionType.getContentType(), newType.getContentType());
        assertEquals(handler, newType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        CollectionType newType = collectionType.withContentValueHandler(handler);
        assertNotSame(collectionType, newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertNotSame(collectionType.getContentType(), newType.getContentType());
        assertEquals(handler, newType.getContentType().getValueHandler());
        assertEquals(collectionType.getContentType().getTypeHandler(), newType.getContentType().getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType newType = collectionType.withStaticTyping();
        assertNotSame(collectionType, newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(collectionType.getContentType().withStaticTyping(), newType.getContentType());
        assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType superClass = new JavaType(SuperClass.class);
        JavaType[] superInterfaces = new JavaType[]{new JavaType(SuperInterface.class)};
        CollectionType newType = collectionType.refine(TestClass.class, TypeBindings.emptyBindings(),
            superClass, superInterfaces);
        assertNotSame(collectionType, newType);
        assertEquals(TestClass.class, newType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(superInterfaces, newType.getSuperInterfaces());
        assertEquals(collectionType.getContentType(), newType.getContentType());
        assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testToString() {
        String expected = "[collection type; class " + collectionType.getRawClass().getName() +
            ", contains " + collectionType.getContentType() + "]";
        assertEquals(expected, collectionType.toString());
    }
}