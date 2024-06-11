
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    private final PropertyName propertyName = new PropertyName("test");
    private final Class<?> scope = String.class;
    private final Class<MyObjectIdGenerator> generatorClass = MyObjectIdGenerator.class;
    private final Class<MyObjectIdResolver> resolverClass = MyObjectIdResolver.class;

    @Test
    public void testConstructorWithAllParameters() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorClass, resolverClass);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorClass, objectIdInfo.getGeneratorType());
        assertEquals(resolverClass, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorClass, true);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorClass, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithDeprecatedParameters() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorClass);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorClass, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo initialObjectIdInfo = new ObjectIdInfo(propertyName, scope, generatorClass, resolverClass);
        ObjectIdInfo modifiedObjectIdInfo = initialObjectIdInfo.withAlwaysAsId(true);

        assertEquals(propertyName, modifiedObjectIdInfo.getPropertyName());
        assertEquals(scope, modifiedObjectIdInfo.getScope());
        assertEquals(generatorClass, modifiedObjectIdInfo.getGeneratorType());
        assertEquals(resolverClass, modifiedObjectIdInfo.getResolverType());
        assertTrue(modifiedObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorClass, true);

        assertEquals("ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.databind.introspect.ObjectIdInfoTest$MyObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
    }

    private static class MyObjectIdGenerator {
    }

    private static class MyObjectIdResolver {
    }
}
