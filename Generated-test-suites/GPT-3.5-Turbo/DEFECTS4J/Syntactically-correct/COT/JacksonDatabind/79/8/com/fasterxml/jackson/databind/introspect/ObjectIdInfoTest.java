
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    
    private ObjectIdInfo objectIdInfo;
    
    @Before
    public void setUp() {
        objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);
    }
    
    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals("name", propertyName.getSimpleName());
    }
    
    @Test
    public void testGetScope() {
        Class<?> scope = objectIdInfo.getScope();
        assertEquals(String.class, scope);
    }
    
    @Test
    public void testGetGeneratorType() {
        Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
        assertEquals(ObjectIdGenerator.class, generatorType);
    }
    
    @Test
    public void testGetResolverType() {
        Class<? extends ObjectIdResolver> resolverType = objectIdInfo.getResolverType();
        assertEquals(SimpleObjectIdResolver.class, resolverType);
    }
    
    @Test
    public void testGetAlwaysAsId() {
        boolean alwaysAsId = objectIdInfo.getAlwaysAsId();
        assertFalse(alwaysAsId);
    }
    
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        String expected = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        String result = objectIdInfo.toString();
        assertEquals(expected, result);
    }
}
