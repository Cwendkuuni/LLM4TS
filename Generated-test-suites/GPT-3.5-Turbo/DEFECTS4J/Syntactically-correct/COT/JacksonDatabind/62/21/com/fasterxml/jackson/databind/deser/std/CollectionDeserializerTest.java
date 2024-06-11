import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class CollectionDeserializerTest {

    private CollectionDeserializer collectionDeserializer;
    
    @Before
    public void setUp() {
        JavaType collectionType = new TypeFactory.defaultInstance().constructCollectionType(Collection.class, Object.class);
        collectionDeserializer = new CollectionDeserializer(collectionType, new JsonArrayDeserializer(), TypeDeserializer.thisInstance, new ValueInstantiator());
    }
    
    @Test
    public void testDeserializeWithNullType() {
        try {
            collectionDeserializer.deserialize(null, null);
            fail("Expected Exception to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (IOException e) {
            fail("Unexpected Exception to be thrown");
        }
    }
    
    @Test
    public void testDeserializeWithEmptyString() {
        try {
            JsonParser p = new JsonFactory().createParser("\"\"");
            Object result = collectionDeserializer.deserialize(p, null);
            assertTrue(result instanceof Collection);
            assertEquals(0, ((Collection) result).size());
            
        } catch (IOException e) {
            fail("Unexpected Exception to be thrown");
        }
    }
    
    @Test
    public void testDeserializeWithNonArray() {
        try {
            JsonParser p = new JsonFactory().createParser("10");
            Object result = collectionDeserializer.deserialize(p, null);
            assertTrue(result instanceof Collection);
            assertEquals(1, ((Collection) result).size());
            assertEquals(10, ((Collection) result).iterator().next());
            
        } catch (IOException e) {
            fail("Unexpected Exception to be thrown");
        }
    }
    
    @Test
    public void testDeserializeWithArray() {
        try {
            JsonParser p = new JsonFactory().createParser("[1,2,3]");
            Object result = collectionDeserializer.deserialize(p, null);
            assertTrue(result instanceof Collection);
            assertEquals(3, ((Collection) result).size());
            
        } catch (IOException e) {
            fail("Unexpected Exception to be thrown");
        }
    }
    
    @Test
    public void testDeserializeWithType() {
        try {
            JsonParser p = new JsonFactory().createParser("[1,2,3]");
            Object result = collectionDeserializer.deserializeWithType(p, null, new TypeDeserializer());
            assertTrue(result instanceof Collection);
            assertEquals(3, ((Collection) result).size());
            
        } catch (IOException e) {
            fail("Unexpected Exception to be thrown");
        }
    }
    
    @Test
    public void testIsCachable() {
        assertFalse(collectionDeserializer.isCachable());
    }
}