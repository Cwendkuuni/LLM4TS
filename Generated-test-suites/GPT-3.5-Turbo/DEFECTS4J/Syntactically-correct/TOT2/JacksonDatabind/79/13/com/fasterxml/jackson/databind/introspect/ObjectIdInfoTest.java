import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfo_Constructor1() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testObjectIdInfo_Constructor2() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testObjectIdInfo_Constructor3() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("name", String.class, MyObjectIdGenerator.class);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testObjectIdInfo_withAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class);
        assertFalse(objectIdInfo.getAlwaysAsId());
        
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfo_toString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class);
        assertEquals("ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=com.example.MyObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }
}