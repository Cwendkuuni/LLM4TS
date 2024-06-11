import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstructor1() {
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, String.class, null, null, false);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType());
    }
    
    @Test
    public void testConstructor2() {
        CollectionType collectionType = new CollectionType(List.class, null, null, null, String.class, null, null, false);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType());
    }
    
    @Test
    public void testConstructor3() {
        CollectionType collectionType = new CollectionType(new TypeBase(), String.class);
        assertNotNull(collectionType);
        assertEquals(CollectionType.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType());
    }
    
    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType());
    }
    
    @Test
    public void testConstructDeprecated() {
        CollectionType collectionType = CollectionType.construct(List.class, String.class);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType());
    }
    
    @Test
    public void testNarrow() {
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, String.class, null, null, false);
        JavaType narrowedType = collectionType.narrow(ArrayList.class);
        assertNotNull(narrowedType);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType typeWithHandler = collectionType.withTypeHandler("handler");
        assertNotNull(typeWithHandler);
        assertEquals("handler", typeWithHandler.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType typeWithContentTypeHandler = collectionType.withContentTypeHandler("contentTypeHandler");
        assertNotNull(typeWithContentTypeHandler);
        assertEquals("contentTypeHandler", typeWithContentTypeHandler.getContentTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType typeWithValueHandler = collectionType.withValueHandler("valueHandler");
        assertNotNull(typeWithValueHandler);
        assertEquals("valueHandler", typeWithValueHandler.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType typeWithContentValueHandler = collectionType.withContentValueHandler("contentValueHandler");
        assertNotNull(typeWithContentValueHandler);
        assertEquals("contentValueHandler", typeWithContentValueHandler.getContentType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType staticType = collectionType.withStaticTyping();
        assertNotNull(staticType);
        assertTrue(staticType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, String.class, null, null, false);
        JavaType refinedType = collectionType.refine(ArrayList.class, new TypeBindings(), null, null);
        assertNotNull(refinedType);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType());
    }
    
    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(List.class, new TypeBindings(), null, null, String.class, null, null, false);
        String expectedString = "[collection type; class java.util.List, contains class java.lang.String]";
        assertEquals(expectedString, collectionType.toString());
    }
}