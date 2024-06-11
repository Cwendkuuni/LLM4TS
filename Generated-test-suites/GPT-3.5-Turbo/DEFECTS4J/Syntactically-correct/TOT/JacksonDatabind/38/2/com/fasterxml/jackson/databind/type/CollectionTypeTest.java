import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;


public class CollectionTypeTest {
    
    private CollectionType collectionType;
    
    @Before
    public void setUp() {
        collectionType = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, Object.class);
    }
    
    @Test
    public void testConstruct() {
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(Object.class, collectionType.getContentType().getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        JavaType newContentType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        
        assertEquals(String.class, newCollectionType.getContentType().getRawClass());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new TypeHandler();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new TypeHandler();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);
        
        assertEquals(contentTypeHandler, newCollectionType.getContentType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new ValueHandler();
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        
        assertEquals(valueHandler, newCollectionType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new ValueHandler();
        CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
        
        assertEquals(contentValueHandler, newCollectionType.getContentType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType newCollectionType = collectionType.withStaticTyping();
        
        assertTrue(newCollectionType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(CollectionLikeType.class);
        JavaType[] superInterfaces = {TypeFactory.defaultInstance().constructType(List.class)};
        CollectionType newCollectionType = collectionType.refine(Vector.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        
        assertEquals(Vector.class, newCollectionType.getRawClass());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertArrayEquals(superInterfaces, newCollectionType.getInterfaces());
    }
    
    @Test
    public void testToString() {
        assertEquals("[collection type; class java.util.ArrayList, contains class java.lang.Object]", collectionType.toString());
    }
    
    private static class TypeHandler {
        // Test implementation of TypeHandler
    }
    
    private static class ValueHandler {
        // Test implementation of ValueHandler
    }
}