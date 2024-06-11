import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
  
    private ObjectIdInfo objectIdInfo;
    private PropertyName propertyName;
    private Class<?> scope;
    private Class<ObjectIdGenerator<?>> generator;
    private Class<ObjectIdResolver> resolver;
    private boolean alwaysAsId;
    
    @Before
    public void setUp() {
        propertyName = new PropertyName("id");
        scope = Object.class;
        generator = ObjectIdGenerator.class;
        resolver = SimpleObjectIdResolver.class;
        alwaysAsId = false;
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        boolean newState = !alwaysAsId;
        ObjectIdInfo updatedInfo = objectIdInfo.withAlwaysAsId(newState);
        assertNotNull(updatedInfo);
        assertNotEquals(objectIdInfo, updatedInfo);
        assertEquals(newState, updatedInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        String expectedString = "ObjectIdInfo: propName=id, scope=java.lang.Object, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}