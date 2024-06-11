import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, JavaType.String);
        
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
    }
    
    @Test
    public void testConstructDeprecated() {
        CollectionType collectionType = CollectionType.construct(List.class, JavaType.String);
        
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
    }
    
    @Test
    public void testContentType() {
        JavaType contentType = JavaType.String;
        CollectionType collectionType = new CollectionType(List.class, null, null, null, JavaType.String, null, null, false);
        CollectionType newCollectionType = collectionType.withContentType(contentType);
        
        assertNotNull(newCollectionType);
        assertEquals(contentType, newCollectionType.getContentType());
    }
    
    // Additional tests for other methods can be added here
    
}