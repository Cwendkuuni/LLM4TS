import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {
    
    private ObjectIdInfo objectIdInfo;
    private PropertyName propertyName;
    private Class<?> scope;
    private Class<?> generator;
    private Class<?> resolver;
    private boolean alwaysAsId;
    
    @Before
    public void setUp() throws Exception {
        propertyName = new PropertyName("test");
        scope = String.class;
        generator = ObjectIdGenerator.class;
        resolver = ObjectIdResolver.class;
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
        boolean newAlwaysAsId = true;
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(newAlwaysAsId);
        
        assertNotNull(newObjectIdInfo);
        assertEquals(propertyName, newObjectIdInfo.getPropertyName());
        assertEquals(scope, newObjectIdInfo.getScope());
        assertEquals(generator, newObjectIdInfo.getGeneratorType());
        assertEquals(resolver, newObjectIdInfo.getResolverType());
        assertEquals(newAlwaysAsId, newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expectedResult = "ObjectIdInfo: propName=" + propertyName + ", scope=" + scope.getName()
                + ", generatorType=" + generator.getName() + ", alwaysAsId=" + alwaysAsId;
        assertEquals(expectedResult, objectIdInfo.toString());
    }
}