import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithDeprecatedStringName() {
        String name = "testName";
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver)
                .withAlwaysAsId(true);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        String expected = "ObjectIdInfo: propName=testName, scope=java.lang.String, generatorType=java.util.UUID, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }
}