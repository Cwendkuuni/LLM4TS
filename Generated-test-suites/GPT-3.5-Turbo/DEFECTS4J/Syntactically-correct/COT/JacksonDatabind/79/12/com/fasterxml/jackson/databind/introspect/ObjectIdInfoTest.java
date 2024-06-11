
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructorWithResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, resolverType);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertEquals(resolverType, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithoutResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorUsingDeprecatedMethod() {
        String name = "name";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generatorType);
        
        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, resolverType);
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        assertEquals(propertyName, updatedObjectIdInfo.getPropertyName());
        assertEquals(scope, updatedObjectIdInfo.getScope());
        assertEquals(generatorType, updatedObjectIdInfo.getGeneratorType());
        assertEquals(resolverType, updatedObjectIdInfo.getResolverType());
        assertTrue(updatedObjectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, resolverType);
        
        String expectedString = "ObjectIdInfo: propName=name, scope=java.lang.Object, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}
