import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    private DeserializationConfig config;
    private JavaType type;
    
    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
        config = new ObjectMapper().getDeserializationConfig();
        type = config.constructType(Collection.class);
    }

    @Test
    public void testAddMapping_ValidMapping_ReturnsResolver() {
        SimpleAbstractTypeResolver result = resolver.addMapping(Collection.class, ArrayList.class);
        assertEquals(resolver, result);
        assertFalse(resolver._mappings.isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SameClass_ThrowsIllegalArgumentException() {
        resolver.addMapping(Collection.class, Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SubTypeNotSubclass_ThrowsIllegalArgumentException() {
        resolver.addMapping(Collection.class, String.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SuperTypeNotAbstract_ThrowsIllegalArgumentException() {
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }
    
    @Test
    public void testFindTypeMapping_MappingExists_ReturnsJavaType() {
        resolver.addMapping(Collection.class, ArrayList.class);
        JavaType result = resolver.findTypeMapping(config, type);
        assertNotNull(result);
        assertEquals(ArrayList.class, result.getRawClass());
    }
    
    @Test
    public void testFindTypeMapping_NoMappingExists_ReturnsNull() {
        JavaType result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }
    
    @Test
    public void testResolveAbstractType_ReturnsNull() {
        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }
}