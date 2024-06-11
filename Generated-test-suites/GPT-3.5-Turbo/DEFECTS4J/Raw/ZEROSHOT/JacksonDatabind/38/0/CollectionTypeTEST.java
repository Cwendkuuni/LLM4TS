import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, JavaType.construct(String.class));
    }

    @Test
    public void testConstruct() {
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getInterfaces());
        assertEquals(JavaType.construct(String.class), collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = JavaType.construct(Integer.class);
        CollectionType result = collectionType.withContentType(contentType);
        assertEquals(collectionType.getRawClass(), result.getRawClass());
        assertEquals(collectionType.getBindings(), result.getBindings());
        assertEquals(collectionType.getSuperClass(), result.getSuperClass());
        assertEquals(collectionType.getInterfaces(), result.getInterfaces());
        assertEquals(contentType, result.getContentType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType result = collectionType.withTypeHandler(typeHandler);
        assertEquals(collectionType.getRawClass(), result.getRawClass());
        assertEquals(collectionType.getBindings(), result.getBindings());
        assertEquals(collectionType.getSuperClass(), result.getSuperClass());
        assertEquals(collectionType.getInterfaces(), result.getInterfaces());
        assertEquals(collectionType.getContentType(), result.getContentType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(typeHandler, result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionType result = collectionType.withContentTypeHandler(typeHandler);
        assertEquals(collectionType.getRawClass(), result.getRawClass());
        assertEquals(collectionType.getBindings(), result.getBindings());
        assertEquals(collectionType.getSuperClass(), result.getSuperClass());
        assertEquals(collectionType.getInterfaces(), result.getInterfaces());
        assertEquals(collectionType.getContentType().withTypeHandler(typeHandler), result.getContentType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType result = collectionType.withValueHandler(valueHandler);
        assertEquals(collectionType.getRawClass(), result.getRawClass());
        assertEquals(collectionType.getBindings(), result.getBindings());
        assertEquals(collectionType.getSuperClass(), result.getSuperClass());
        assertEquals(collectionType.getInterfaces(), result.getInterfaces());
        assertEquals(collectionType.getContentType(), result.getContentType());
        assertEquals(valueHandler, result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        CollectionType result = collectionType.withContentValueHandler(valueHandler);
        assertEquals(collectionType.getRawClass(), result.getRawClass());
        assertEquals(collectionType.getBindings(), result.getBindings());
        assertEquals(collectionType.getSuperClass(), result.getSuperClass());
        assertEquals(collectionType.getInterfaces(), result.getInterfaces());
        assertEquals(collectionType.getContentType().withValueHandler(valueHandler), result.getContentType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType result = collectionType.withStaticTyping();
        assertEquals(collectionType.getRawClass(), result.getRawClass());
        assertEquals(collectionType.getBindings(), result.getBindings());
        assertEquals(collectionType.getSuperClass(), result.getSuperClass());
        assertEquals(collectionType.getInterfaces(), result.getInterfaces());
        assertEquals(collectionType.getContentType().withStaticTyping(), result.getContentType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = LinkedList.class;
        TypeBindings bindings = TypeBindings.create(Arrays.asList(JavaType.construct(Integer.class)));
        JavaType superClass = JavaType.construct(HashMap.class);
        JavaType[] superInterfaces = new JavaType[]{JavaType.construct(Comparable.class)};
        JavaType elementType = JavaType.construct(Double.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        CollectionType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInterfaces, result.getInterfaces());
        assertEquals(collectionType.getContentType(), result.getContentType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }

    @Test
    public void testToString() {
        String expected = "[collection type; class java.util.ArrayList, contains java.lang.String]";
        assertEquals(expected, collectionType.toString());
    }
}