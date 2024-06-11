import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructors() {
        ObjectIdInfo objInfo = new ObjectIdInfo(new PropertyName("test"), String.class, Object.class, SimpleObjectIdGenerator.class);
        assertNotNull(objInfo);
        
        assertEquals("test", objInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objInfo.getScope());
        assertEquals(Object.class, objInfo.getGeneratorType());
        assertEquals(SimpleObjectIdGenerator.class, objInfo.getResolverType());
        assertFalse(objInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objInfo = new ObjectIdInfo(new PropertyName("test"), String.class, Object.class, SimpleObjectIdGenerator.class);
        
        ObjectIdInfo newObjInfo = objInfo.withAlwaysAsId(true);
        assertNotNull(newObjInfo);
        
        assertTrue(newObjInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        ObjectIdInfo objInfo = new ObjectIdInfo(new PropertyName("test"), String.class, Object.class, SimpleObjectIdGenerator.class);
        
        String infoString = objInfo.toString();
        assertNotNull(infoString);
        
        assertTrue(infoString.contains("propName=test"));
        assertTrue(infoString.contains("scope=String"));
        assertTrue(infoString.contains("generatorType=java.lang.Object"));
        assertTrue(infoString.contains("alwaysAsId=false"));
    }
}