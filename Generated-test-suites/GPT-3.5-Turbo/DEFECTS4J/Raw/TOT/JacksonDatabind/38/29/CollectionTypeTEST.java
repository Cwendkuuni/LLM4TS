
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class CollectionTypeTest {
    
    private CollectionType collectionType;
    
    @Before
    public void setUp() {
        JavaType elementType = SimpleType.construct(String.class);
        collectionType = CollectionType.construct(
                java.util.List.class, null, null, null, elementType);
    }
    
    @Test
    public void testConstruct() {
        assertEquals(java.util.List.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType().getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        JavaType newContentType = SimpleType.construct(Integer.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        assertEquals(java.util.List.class, newCollectionType.getRawClass());
        assertEquals(Integer.class, newCollectionType.getContentType().getRawClass());
        assertNotEquals(collectionType, newCollectionType);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        assertEquals(java.util.List.class, newCollectionType.getRawClass());
        assertEquals(String.class, newCollectionType.getContentType().getRawClass());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertNotEquals(collectionType, newCollectionType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertEquals(java.util.List.class, newCollectionType.getRawClass());
        assertEquals(String.class, newCollectionType.getContentType().getRawClass());
        assertEquals(contentTypeHandler, newCollectionType.getContentType().getTypeHandler());
        assertNotEquals(collectionType, newCollectionType);
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertEquals(java.util.List.class, newCollectionType.getRawClass());
        assertEquals(String.class, newCollectionType.getContentType().getRawClass());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertNotEquals(collectionType, newCollectionType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
        assertEquals(java.util.List.class, newCollectionType.getRawClass());
        assertEquals(String.class, newCollectionType.getContentType().getRawClass());
        assertEquals(contentValueHandler, newCollectionType.getContentType().getValueHandler());
        assertNotEquals(collectionType, newCollectionType);
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertEquals(java.util.List.class, newCollectionType.getRawClass());
        assertEquals(String.class, newCollectionType.getContentType().getRawClass());
        assertTrue(newCollectionType.isStaticTyping());
        assertNotEquals(collectionType, newCollectionType);
    }
    
    @Test
    public void testRefine() {
        JavaType superClass = SimpleType.construct(Object.class);
        JavaType[] superInterfaces = new JavaType[] { SimpleType.construct(Cloneable.class) };
        CollectionType newCollectionType = collectionType.refine(
                java.util.ArrayList.class, null, superClass, superInterfaces);
        assertEquals(java.util.ArrayList.class, newCollectionType.getRawClass());
        assertEquals(String.class, newCollectionType.getContentType().getRawClass());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInterfaces[0], newCollectionType.getInterfaces()[0]);
        assertNotEquals(collectionType, newCollectionType);
    }
    
    @Test
    public void testToString() {
        assertEquals("[collection type; class java.util.List, contains class java.lang.String]",
                collectionType.toString());
    }
}

