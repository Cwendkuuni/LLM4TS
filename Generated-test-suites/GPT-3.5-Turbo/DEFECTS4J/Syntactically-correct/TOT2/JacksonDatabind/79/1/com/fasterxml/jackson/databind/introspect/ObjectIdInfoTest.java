import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        
        assertEquals("test", objectIdInfo.getPropertyName().getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorNoResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class);
        
        assertEquals("test", objectIdInfo.getPropertyName().getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithStringName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("test", String.class, MyObjectIdGenerator.class);
        
        assertEquals("test", objectIdInfo.getPropertyName().getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class);
        
        ObjectIdInfo updatedInfo = objectIdInfo.withAlwaysAsId(true);
        
        assertTrue(updatedInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class);
        
        String expectedString = "ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.example.MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}