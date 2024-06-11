import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    @Test
    public void testConstructorWithId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithIdAndResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("name", String.class, ObjectIdGenerator.class);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class)
            .withAlwaysAsId(true);
        assertTrue(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class);
        String expected = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=java.lang.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }
}