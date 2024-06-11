import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CollectionTypeTest {
    
    private CollectionType collectionType;
    
    @Before
    public void setUp() {
        collectionType = new CollectionType(List.class, null,
                null, null, null,
                null, null, false);
    }
    
    @Test
    public void testConstruct() {
        CollectionType constructedType = CollectionType.construct(Set.class, null,
                null, null, null);
        assertNotNull(constructedType);
        assertEquals(Set.class, constructedType.getRawClass());
    }
    
    @Test
    public void testDeprecatedConstruct() {
        CollectionType deprecatedType = CollectionType.construct(Set.class, null);
        assertNotNull(deprecatedType);
        assertEquals(Set.class, deprecatedType.getRawClass());
    }
    
    @Test
    public void testNarrow() {
        JavaType narrowType = collectionType._narrow(ArrayList.class);
        assertNotNull(narrowType);
        assertEquals(ArrayList.class, narrowType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(AnyType.class);
        CollectionType typeWithContent = collectionType.withContentType(contentType);
        assertNotNull(typeWithContent);
        assertEquals(contentType, typeWithContent.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new TypeHandler();
        CollectionType typeWithTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertNotNull(typeWithTypeHandler);
        assertEquals(typeHandler, typeWithTypeHandler.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new ContentTypeHandler();
        CollectionType typeWithContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(typeWithContentTypeHandler);
        assertEquals(contentTypeHandler, typeWithContentTypeHandler.getContentType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new ValueHandler();
        CollectionType typeWithValueHandler = collectionType.withValueHandler(valueHandler);
        assertNotNull(typeWithValueHandler);
        assertEquals(valueHandler, typeWithValueHandler.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new ContentValueHandler();
        CollectionType typeWithContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
        assertNotNull(typeWithContentValueHandler);
        assertEquals(contentValueHandler, typeWithContentValueHandler.getContentType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType staticType = collectionType.withStaticTyping();
        assertNotNull(staticType);
        assertTrue(staticType.isStatic());
    }
    
    @Test
    public void testRefine() {
        CollectionType refinedType = collectionType.refine(Set.class, null,
                null, null);
        assertNotNull(refinedType);
        assertEquals(Set.class, refinedType.getRawClass());
    }
    
    @Test
    public void testToString() {
        String expectedString = "[collection type; class java.util.List, contains null]";
        assertEquals(expectedString, collectionType.toString());
    }
}