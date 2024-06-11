import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class);
        ObjectIdInfo updatedInfo = info.withAlwaysAsId(true);
        assertTrue(updatedInfo.getAlwaysAsId());
        
        updatedInfo = info.withAlwaysAsId(false);
        assertFalse(updatedInfo.getAlwaysAsId());
        
        // Test that the original info object remains unchanged
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = new PropertyName("name");
        ObjectIdInfo info = new ObjectIdInfo(propertyName, String.class, CustomObjectIdGenerator.class);
        assertEquals(propertyName, info.getPropertyName());
    }
    
    @Test
    public void testGetScope() {
        Class<?> scope = String.class;
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), scope, CustomObjectIdGenerator.class);
        assertEquals(scope, info.getScope());
    }
    
    @Test
    public void testGetGeneratorType() {
        Class<? extends ObjectIdGenerator<?>> generatorType = CustomObjectIdGenerator.class;
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, generatorType);
        assertEquals(generatorType, info.getGeneratorType());
    }
    
    @Test
    public void testGetResolverType() {
        Class<? extends ObjectIdResolver> resolverType = CustomObjectIdResolver.class;
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, resolverType);
        assertEquals(resolverType, info.getResolverType());
    }
    
    @Test
    public void testGetAlwaysAsId() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class);
        assertFalse(info.getAlwaysAsId());
        
        info = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, true);
        assertTrue(info.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = CustomObjectIdGenerator.class;
        boolean alwaysAsId = true;
        
        ObjectIdInfo info = new ObjectIdInfo(propertyName, scope, generatorType, alwaysAsId);
        String expectedString = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=CustomObjectIdGenerator, alwaysAsId=true";
        
        assertEquals(expectedString, info.toString());
    }
}