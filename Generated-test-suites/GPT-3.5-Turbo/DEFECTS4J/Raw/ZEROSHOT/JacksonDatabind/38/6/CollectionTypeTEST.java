import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();

        CollectionType collectionType = new CollectionType();
        collectionType.withContentType(contentType);

        assertEquals(contentType, collectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();

        CollectionType collectionType = new CollectionType();
        collectionType.withTypeHandler(h);

        assertEquals(h, collectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();

        CollectionType collectionType = new CollectionType();
        collectionType.withContentTypeHandler(h);

        assertEquals(h, collectionType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();

        CollectionType collectionType = new CollectionType();
        collectionType.withValueHandler(h);

        assertEquals(h, collectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();

        CollectionType collectionType = new CollectionType();
        collectionType.withContentValueHandler(h);

        assertEquals(h, collectionType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType();
        collectionType.withStaticTyping();

        assertTrue(collectionType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();

        CollectionType collectionType = new CollectionType();
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInts);

        assertNotNull(refinedType);
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInts, refinedType.getInterfaces());
        assertEquals(elemT, refinedType.getElementType());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType();
        String expected = "[collection type; class "+collectionType.getRawClass().getName()+", contains "+collectionType.getElementType()+"]";

        assertEquals(expected, collectionType.toString());
    }
}