
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollectionTypeTest {
    
    @Test
    public void testConstruct() {
        // Given
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        
        // When
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        // Then
        // Add assertion here
    }
    
    @Test
    public void test_construct() {
        // Given
        Class<?> rawType = null;
        JavaType elemT = null;
        
        // When
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        
        // Then
        // Add assertion here
    }
    
    @Test
    public void test_withContentType() {
        // Given
        JavaType contentType = null;
        
        // When
        CollectionType collectionType = new CollectionType().withContentType(contentType);
        
        // Then
        // Add assertion here
    }
    
    @Test
    public void test_withTypeHandler() {
        // Given
        Object h = null;
        
        // When
        CollectionType collectionType = new CollectionType().withTypeHandler(h);
        
        // Then
        // Add assertion here
    }

    @Test
    public void test_withContentTypeHandler() {
        // Given
        Object h = null;
        
        // When
        CollectionType collectionType = new CollectionType().withContentTypeHandler(h);
        
        // Then
        // Add assertion here
    }
    
    @Test
    public void test_withValueHandler() {
        // Given
        Object h = null;
        
        // When
        CollectionType collectionType = new CollectionType().withValueHandler(h);
        
        // Then
        // Add assertion here
    }
    
    @Test
    public void test_withContentValueHandler() {
        // Given
        Object h = null;
        
        // When
        CollectionType collectionType = new CollectionType().withContentValueHandler(h);
        
        // Then
        // Add assertion here
    }
    
    @Test
    public void test_withStaticTyping() {
        // Given
        
        // When
        CollectionType collectionType = new CollectionType().withStaticTyping();
        
        // Then
        // Add assertion here
    }
    
    @Test
    public void test_refine() {
        // Given
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        // When
        CollectionType collectionType = new CollectionType().refine(rawType, bindings, superClass, superInterfaces);
        
        // Then
        // Add assertion here
    }
}
