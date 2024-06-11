import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);
        
        assertEquals(new PropertyName("id"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class);

        assertEquals(new PropertyName("id"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithStringName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("id", String.class, ObjectIdGenerator.class);

        assertEquals(new PropertyName("id"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class);
        
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        assertEquals(new PropertyName("id"), updatedObjectIdInfo.getPropertyName());
        assertEquals(String.class, updatedObjectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, updatedObjectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, updatedObjectIdInfo.getResolverType());
        assertTrue(updatedObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class);
        
        String expected = "ObjectIdInfo: propName=id, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }

}