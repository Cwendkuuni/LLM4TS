import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        
        assertEquals("test", objectIdInfo.getPropertyName().simpleAsEncoded());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class);
        
        assertEquals("test", objectIdInfo.getPropertyName().simpleAsEncoded());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("test", String.class, MyObjectIdGenerator.class);
        
        assertEquals("test", objectIdInfo.getPropertyName().simpleAsEncoded());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class);
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class);
        
        assertEquals("ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.example.MyObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }
}