import static org.junit.Assert.*;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    
    @Test
    public void testFindForCollection_typicalUseCase() {
        // Typical use case: type has rawClass CLASS_AS_ARRAYS_LIST
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(CLASS_AS_ARRAYS_LIST);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
    }
    
    @Test
    public void testFindForCollection_edgeCase() {
        // Edge case: type has rawClass CLASS_SINGLETON_LIST
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(CLASS_SINGLETON_LIST);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
    }
    
    @Test
    public void testFindForCollection_errorScenario() {
        // Error scenario: type has rawClass CLASS_UNMODIFIABLE_SET
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(CLASS_UNMODIFIABLE_SET);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertNull(deserializer);
    }
    
    @Test
    public void testFindForMap_typicalUseCase() {
        // Typical use case: type has rawClass CLASS_SINGLETON_MAP
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(CLASS_SINGLETON_MAP);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
    }
    
    @Test
    public void testFindForMap_edgeCase() {
        // Edge case: type has rawClass CLASS_UNMODIFIABLE_MAP
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(CLASS_UNMODIFIABLE_MAP);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
    }
    
    @Test
    public void testFindForMap_errorScenario() {
        // Error scenario: type has rawClass CLASS_SINGLETON_SET
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(CLASS_SINGLETON_SET);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertNull(deserializer);
    }
    
    @Test
    public void testConverter_typicalUseCase() {
        // Typical use case: kind is TYPE_SINGLETON_SET and concreteType is Set.class
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Set.class);
        
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_SET, concreteType, Set.class);
        
        assertNotNull(converter);
        assertEquals(concreteType, converter.getInputType());
    }
    
    @Test
    public void testConverter_edgeCase() {
        // Edge case: kind is TYPE_SINGLETON_LIST and concreteType is List.class
        JavaType concreteType = TypeFactory.defaultInstance().constructType(List.class);
        
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_LIST, concreteType, List.class);
        
        assertNotNull(converter);
        assertEquals(concreteType, converter.getInputType());
    }
    
    @Test
    public void testConverter_errorScenario() {
        // Error scenario: kind is TYPE_AS_LIST and concreteType is Set.class
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Set.class);
        
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(TYPE_AS_LIST, concreteType, List.class);
        
        assertNotNull(converter);
        assertEquals(concreteType, converter.getInputType());
    }
}