import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
    
    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        assertNotNull(collectionType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType());
    }
    
    @Test
    public void testWithContentType() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType modifiedType = collectionType.withContentType(Integer.class);
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertEquals(Integer.class, modifiedType.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType typeWithHandler = collectionType.withTypeHandler(new TypeHandler());
        assertNotNull(typeWithHandler);
        assertEquals(ArrayList.class, typeWithHandler.getRawClass());
        assertNotNull(typeWithHandler.getTypeHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType typeWithValueHandler = collectionType.withContentValueHandler(new ValueHandler());
        assertNotNull(typeWithValueHandler);
        assertEquals(ArrayList.class, typeWithValueHandler.getRawClass());
        assertNotNull(typeWithValueHandler.getContentValueHandler());
    }
    
    @Test
    public void testRefine() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType refinedType = (CollectionType) collectionType.refine(Vector.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(Vector.class, refinedType.getRawClass());
    }

}