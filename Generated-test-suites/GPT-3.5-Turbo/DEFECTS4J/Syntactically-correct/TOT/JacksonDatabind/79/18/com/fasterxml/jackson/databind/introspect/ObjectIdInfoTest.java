import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {
    
    ObjectIdInfo objectIdInfo;
    PropertyName propertyName;
    Class<?> scope;
    Class<? extends ObjectIdGenerator<?>> generator;
    Class<? extends ObjectIdResolver> resolver;
    
    @Before
    public void setUp() {
        propertyName = new PropertyName("testProperty");
        scope = String.class;
        generator = CustomObjectIdGenerator.class;
        resolver = CustomObjectIdResolver.class;
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    }
    
    @Test
    public void testConstructorWithResolver() {
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithoutResolver() {
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithoutPropertyName() {
        objectIdInfo = new ObjectIdInfo(null, scope, generator);
        assertNull(objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithStringPropertyName() {
        String propertyNameString = "testProperty";
        objectIdInfo = new ObjectIdInfo(propertyNameString, scope, generator);
        assertEquals(new PropertyName(propertyNameString), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsIdTrue() {
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsIdFalse() {
        objectIdInfo = objectIdInfo.withAlwaysAsId(false);
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        String expectedString = "ObjectIdInfo: propName=testProperty, scope=java.lang.String, " +
            "generatorType=CustomObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
    
    // Custom classes for testing
    static class CustomObjectIdGenerator<T> extends ObjectIdGenerator<T> {
        // Implement required methods
    }
    
    static class CustomObjectIdResolver extends ObjectIdResolver {
        // Implement required methods
    }

}