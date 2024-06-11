import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JavaUtilCollectionsDeserializersTest {
    
    private DeserializationContext ctxt;
    private JavaType type;
    private JsonDeserializer<?> deserializer;
    
    @Before
    public void setUp() {
        ctxt = new DeserializationContext();
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(Object.class));
    }
    
    @Test
    public void testFindForCollection_withAsListType_returnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.TYPE_AS_LIST));
        
        // Act
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_withSingletonListType_returnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST));
        
        // Act
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_withSingletonSetType_returnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET));
        
        // Act
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_withUnmodifiableListType_returnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST));
        
        // Act
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_withUnmodifiableSetType_returnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET));
        
        // Act
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_withInvalidType_returnsNull() throws JsonMappingException {
        // Arrange
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(Object.class));
        
        // Act
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertNull(deserializer);
    }
    
    @Test
    public void testFindForMap_withSingletonMapType_returnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP));
        
        // Act
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        // Assert
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForMap_withUnmodifiableMapType_returnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP));
        
        // Act
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        // Assert
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForMap_withInvalidType_returnsNull() throws JsonMappingException {
        // Arrange
        type = JavaType.construct(TypeFactory.defaultInstance().constructType(Object.class));
        
        // Act
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        // Assert
        assertNull(deserializer);
    }
    
}