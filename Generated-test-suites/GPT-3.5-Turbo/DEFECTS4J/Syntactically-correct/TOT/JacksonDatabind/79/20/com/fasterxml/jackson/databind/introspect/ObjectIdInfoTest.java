
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructors() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertEquals(propertyName, objectIdInfo1.getPropertyName());
        assertEquals(scope, objectIdInfo1.getScope());
        assertEquals(generator, objectIdInfo1.getGeneratorType());
        assertEquals(resolver, objectIdInfo1.getResolverType());
        assertFalse(objectIdInfo1.getAlwaysAsId());
        
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propertyName, scope, generator);
        
        assertEquals(propertyName, objectIdInfo2.getPropertyName());
        assertEquals(scope, objectIdInfo2.getScope());
        assertEquals(generator, objectIdInfo2.getGeneratorType());
        assertNull(objectIdInfo2.getResolverType());
        assertFalse(objectIdInfo2.getAlwaysAsId());
        
        ObjectIdInfo objectIdInfo3 = new ObjectIdInfo("test", scope, generator);
        
        assertEquals(propertyName, objectIdInfo3.getPropertyName()); // Verify conversion of String name to PropertyName object
        assertEquals(scope, objectIdInfo3.getScope());
        assertEquals(generator, objectIdInfo3.getGeneratorType());
        assertNull(objectIdInfo3.getResolverType());
        assertFalse(objectIdInfo3.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propertyName, scope, generator, true);
        
        assertEquals(propertyName, objectIdInfo1.getPropertyName());
        assertEquals(scope, objectIdInfo1.getScope());
        assertEquals(generator, objectIdInfo1.getGeneratorType());
        assertEquals(resolver, objectIdInfo1.getResolverType());
        assertTrue(objectIdInfo1.getAlwaysAsId());
        
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propertyName, scope, generator, false);
        
        assertEquals(propertyName, objectIdInfo2.getPropertyName());
        assertEquals(scope, objectIdInfo2.getScope());
        assertEquals(generator, objectIdInfo2.getGeneratorType());
        assertEquals(resolver, objectIdInfo2.getResolverType());
        assertFalse(objectIdInfo2.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propertyName, scope, generator, resolver);
        ObjectIdInfo objectIdInfo2 = objectIdInfo1.withAlwaysAsId(true);
        assertTrue(objectIdInfo2.getAlwaysAsId());
        
        ObjectIdInfo objectIdInfo3 = objectIdInfo1.withAlwaysAsId(false);
        assertFalse(objectIdInfo3.getAlwaysAsId());
        
        assertNotSame(objectIdInfo1, objectIdInfo2);
        assertNotSame(objectIdInfo1, objectIdInfo3);
    }
    
    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        String expectedToString = "ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        
        assertEquals(expectedToString, objectIdInfo.toString());
    }
}

