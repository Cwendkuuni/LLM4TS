import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructorWithResolver() {
        ObjectIdInfo oidInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        
        assertNotNull(oidInfo);
        assertEquals("id", oidInfo.getPropertyName().getSimpleName());
        assertEquals(Object.class, oidInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, oidInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, oidInfo.getResolverType());
        assertFalse(oidInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithoutResolver() {
        ObjectIdInfo oidInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class);
        
        assertNotNull(oidInfo);
        assertEquals("name", oidInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, oidInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, oidInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, oidInfo.getResolverType());
        assertFalse(oidInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        ObjectIdInfo oidInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, MyObjectIdGenerator.class);
        
        String expectedString = "ObjectIdInfo: propName=id, scope=java.lang.Object, generatorType=com.example.MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, oidInfo.toString());
    }
  
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo oidInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, MyObjectIdGenerator.class);
        ObjectIdInfo updatedOidInfo = oidInfo.withAlwaysAsId(true);
        
        assertFalse(oidInfo.getAlwaysAsId());
        assertTrue(updatedOidInfo.getAlwaysAsId());
    }
    
    // Edge cases and exceptions testing can be added here
    
}