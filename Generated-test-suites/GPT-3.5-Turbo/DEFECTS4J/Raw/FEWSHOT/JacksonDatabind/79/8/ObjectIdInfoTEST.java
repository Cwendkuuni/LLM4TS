import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectIdInfoTest {
    @Test
    public void testConstructor1() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        
        assertEquals(new PropertyName("name"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructor2() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("name", String.class, MyObjectIdGenerator.class);
        
        assertEquals(new PropertyName("name"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructor3() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, true);
        
        assertEquals(new PropertyName("name"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        assertEquals(new PropertyName("name"), newObjectIdInfo.getPropertyName());
        assertEquals(String.class, newObjectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, newObjectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, newObjectIdInfo.getResolverType());
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }
}