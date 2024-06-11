import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {
    
    private ObjectIdInfo objectIdInfo;
    private PropertyName propertyName;
    private Class<?> scope;
    private Class<? extends ObjectIdGenerator<?>> generator;
    private Class<? extends ObjectIdResolver> resolver;
    private boolean alwaysAsId;
    
    @Before
    public void setUp() {
        propertyName = new PropertyName("testName");
        scope = String.class;
        generator = ObjectIdGenerator.class;
        resolver = SimpleObjectIdResolver.class;
        alwaysAsId = true;
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
    }
    
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
        assertFalse(newObjectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals(propertyName, objectIdInfo.getPropertyName());
    }
    
    @Test
    public void testGetScope() {
        assertEquals(scope, objectIdInfo.getScope());
    }
    
    @Test
    public void testGetGeneratorType() {
        assertEquals(generator, objectIdInfo.getGeneratorType());
    }
    
    @Test
    public void testGetResolverType() {
        assertEquals(resolver, objectIdInfo.getResolverType());
    }
    
    @Test
    public void testGetAlwaysAsId() {
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        String expected = "ObjectIdInfo: propName=" + propertyName + ", scope=" + scope.getName() + ", generatorType=" + generator.getName() + ", alwaysAsId=" + alwaysAsId;
        assertEquals(expected, objectIdInfo.toString());
    }
}