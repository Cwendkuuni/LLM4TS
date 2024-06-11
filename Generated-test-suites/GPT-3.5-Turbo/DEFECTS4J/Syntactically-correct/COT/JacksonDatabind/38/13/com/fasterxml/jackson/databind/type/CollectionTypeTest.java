
import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        // Assert statements
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withContentType(contentType);
        
        // Assert statements
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withTypeHandler(h);
        
        // Assert statements
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withContentTypeHandler(h);
        
        // Assert statements
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withValueHandler(h);
        
        // Assert statements
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withContentValueHandler(h);
        
        // Assert statements
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withStaticTyping();
        
        // Assert statements
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = null;
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.refine(rawType, bindings, superClass, superInterfaces, elementType);
        
        // Assert statements
    }
}
