import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    ObjectIdInfo objectIdInfo;

    @Test
    public void testConstructor1() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        String name = "test";
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;

        objectIdInfo = new ObjectIdInfo(name, scope, generator);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertEquals(propertyName, newObjectIdInfo.getPropertyName());
        assertEquals(scope, newObjectIdInfo.getScope());
        assertEquals(generator, newObjectIdInfo.getGeneratorType());
        assertEquals(resolver, newObjectIdInfo.getResolverType());
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        String expected = "ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }
}