import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    
    @Test
    public void testWithAlwaysAsId() {
        // Arrange
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        // Act
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        // Assert
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testGetPropertyName() {
        // Arrange
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        // Act
        PropertyName result = objectIdInfo.getPropertyName();
        
        // Assert
        assertEquals(propertyName, result);
    }
    
    @Test
    public void testGetScope() {
        // Arrange
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        // Act
        Class<?> result = objectIdInfo.getScope();
        
        // Assert
        assertEquals(scope, result);
    }
    
    @Test
    public void testGetGeneratorType() {
        // Arrange
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        // Act
        Class<? extends ObjectIdGenerator<?>> result = objectIdInfo.getGeneratorType();
        
        // Assert
        assertEquals(generator, result);
    }
    
    @Test
    public void testGetResolverType() {
        // Arrange
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        // Act
        Class<? extends ObjectIdResolver> result = objectIdInfo.getResolverType();
        
        // Assert
        assertEquals(resolver, result);
    }
    
    @Test
    public void testGetAlwaysAsId() {
        // Arrange
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        // Act
        boolean result = objectIdInfo.getAlwaysAsId();
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testToString() {
        // Arrange
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        // Act
        String result = objectIdInfo.toString();
        
        // Assert
        assertNotNull(result);
    }
}