import org.junit.Test;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.PropertyName;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructorWithNameScopeGeneratorResolver() {
        // Arrange
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        
        // Act
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);
        
        // Assert
        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithNameScopeGenerator() {
        // Arrange
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        
        // Act
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        
        // Assert
        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithNameScopeStringGenerator() {
        // Arrange
        String name = "test";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        
        // Act
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        
        // Assert
        assertEquals(new PropertyName(name), info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        // Arrange
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        boolean state = true;
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);
        
        // Act
        ObjectIdInfo withAlwaysAsId = info.withAlwaysAsId(state);
        
        // Assert
        assertNotSame(info, withAlwaysAsId);
        assertEquals(name, withAlwaysAsId.getPropertyName());
        assertEquals(scope, withAlwaysAsId.getScope());
        assertEquals(gen, withAlwaysAsId.getGeneratorType());
        assertEquals(resolver, withAlwaysAsId.getResolverType());
        assertEquals(state, withAlwaysAsId.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        // Arrange
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = true;
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, alwaysAsId);
        
        // Act
        String result = info.toString();
        
        // Assert
        assertEquals("ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true", result);
    }
}