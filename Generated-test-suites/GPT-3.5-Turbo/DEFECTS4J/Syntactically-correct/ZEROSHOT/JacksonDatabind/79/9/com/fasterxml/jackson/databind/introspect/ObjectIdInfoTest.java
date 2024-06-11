import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = SomeObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SomeObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = SomeObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithDeprecatedMethod() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = SomeObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo("name", scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = SomeObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SomeObjectIdResolver.class;
        ObjectIdInfo originalInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        ObjectIdInfo updatedInfo = originalInfo.withAlwaysAsId(true);

        assertEquals(propertyName, updatedInfo.getPropertyName());
        assertEquals(scope, updatedInfo.getScope());
        assertEquals(generator, updatedInfo.getGeneratorType());
        assertEquals(resolver, updatedInfo.getResolverType());
        assertTrue(updatedInfo.getAlwaysAsId());
    }
}

class SomeObjectIdGenerator extends ObjectIdGenerator<String> {
    @Override
    public String generateId(Object forPojo) {
        return null;
    }
}

class SomeObjectIdResolver extends ObjectIdResolver {
    @Override
    public void bindItem(ObjectIdGenerator.IdKey idKey, Object pojo) {

    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey idKey) {
        return null;
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return null;
    }
}