import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        
        assertEquals("propertyName", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testDeprecatedConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("propertyName", String.class, ObjectIdGenerator.class);
        
        assertEquals("propertyName", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        assertTrue(updatedObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        
        assertEquals("ObjectIdInfo: propName=propertyName, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }
}