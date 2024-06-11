
import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstructWithElemT() {
        Class<?> rawType = null; 
        JavaType elemT = null;
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withContentType(contentType);
        
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withTypeHandler(h);
        
        assertEquals(h, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withContentTypeHandler(h);
        
        assertEquals(h, result.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withValueHandler(h);
        
        assertEquals(h, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withContentValueHandler(h);
        
        assertEquals(h, result.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withStaticTyping();
        
        assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getTypeBindings());
        assertEquals(superClass, result.getSuperClass());
        assertEquals(superInterfaces, result.getInterfaces());
        assertEquals(elementType, result.getContentType());
        assertEquals(valueHandler, result.getValueHandler());
        assertEquals(typeHandler, result.getTypeHandler());
        assertEquals(asStatic, result.isStatic());
    }
}
