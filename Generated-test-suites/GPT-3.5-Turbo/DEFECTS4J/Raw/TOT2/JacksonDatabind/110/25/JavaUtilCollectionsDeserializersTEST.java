import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JavaUtilCollectionsDeserializersTest {

    @Mock
    DeserializationContext mockContext;

    @Test
    public void testFindForCollection_AsArraysList() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(Arrays.asList().getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singletonList(null).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singleton(null).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableList(Collections.singletonList(null)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableSet(Collections.singleton(null)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollection_NullType() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singletonMap(null, null).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mockContext, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(Collections.singletonMap(null, null)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mockContext, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForMap_NullType() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mockContext, type);
        assertNull(deserializer);
    }
}