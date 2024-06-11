import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void constructTest() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertNotNull(collectionType);
    }

    @Test
    public void withContentTypeTest() {
        JavaType contentType = null;
        
        CollectionType collectionType = new CollectionType();

        CollectionType result = collectionType.withContentType(contentType);

        assertNotNull(result);
        assertEquals(contentType, result.getContentType());
    }
    
    @Test
    public void withTypeHandlerTest() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType();

        CollectionType result = collectionType.withTypeHandler(h);

        assertNotNull(result);
        assertEquals(h, result.getTypeHandler());
    }

    @Test
    public void withContentTypeHandlerTest() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType();

        CollectionType result = collectionType.withContentTypeHandler(h);

        assertNotNull(result);
        assertEquals(h, result.getContentTypeHandler());
    }

    @Test
    public void withValueHandlerTest() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType();

        CollectionType result = collectionType.withValueHandler(h);

        assertNotNull(result);
        assertEquals(h, result.getValueHandler());
    }

    @Test
    public void withContentValueHandlerTest() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType();

        CollectionType result = collectionType.withContentValueHandler(h);

        assertNotNull(result);
        assertEquals(h, result.getContentValueHandler());
    }

    @Test
    public void withStaticTypingTest() {
        CollectionType collectionType = new CollectionType();

        CollectionType result = collectionType.withStaticTyping();

        assertNotNull(result);
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void refineTest() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        CollectionType collectionType = new CollectionType();

        CollectionType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertNotNull(result);
        assertEquals(rawType, result.getRawType());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertEquals(superInterfaces, result.getInterfaces());
    }

}