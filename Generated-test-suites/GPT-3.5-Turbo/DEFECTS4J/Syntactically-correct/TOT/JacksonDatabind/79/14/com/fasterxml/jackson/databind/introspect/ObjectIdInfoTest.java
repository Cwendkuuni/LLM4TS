import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorAndResolver() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorAndResolverAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver, alwaysAsId);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithStringNameAndScopeAndGenerator() {
        String propertyName = "id";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertNotNull(objectIdInfo);
        assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    // Custom ObjectIdGenerator implementation for testing

    public static class MyObjectIdGenerator<T> extends ObjectIdGenerator<T> {
        @Override
        public T generateId(Object forPojo) {
            return null;
        }

        @Override
        public IdKey key(Object key) {
            return null;
        }
    }

    // Custom ObjectIdResolver implementation for testing

    public static class MyObjectIdResolver extends ObjectIdResolver {
        @Override
        public void bindItem(IdKey id, Object pojo) {}

        @Override
        public Object resolveId(IdKey id) {
            return null;
        }

        @Override
        public ObjectIdResolver newForDeserialization(Object context) {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdResolver resolverType) {
            return false;
        }
    }

}