
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor_1() {
        // Typical use case
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse("alwaysAsId should default to false", objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor_2() {
        // Edge case: Using deprecated constructor
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull("resolver should default to null", objectIdInfo.getResolverType());
        assertFalse("alwaysAsId should default to false", objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor_3() {
        // Edge case: Using deprecated constructor
        String propertyName = "id";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName().getSimple());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull("resolver should default to null", objectIdInfo.getResolverType());
        assertFalse("alwaysAsId should default to false", objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        // Typical use case
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        ObjectIdInfo modifiedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertEquals("withAlwaysAsId should modify alwaysAsId state", true, modifiedObjectIdInfo.getAlwaysAsId());
        assertNotSame("withAlwaysAsId should create a new ObjectIdInfo object", objectIdInfo, modifiedObjectIdInfo);
        assertEquals("withAlwaysAsId should not modify other properties", propertyName, modifiedObjectIdInfo.getPropertyName());
        assertEquals("withAlwaysAsId should not modify other properties", scope, modifiedObjectIdInfo.getScope());
        assertEquals("withAlwaysAsId should not modify other properties", generator, modifiedObjectIdInfo.getGeneratorType());
        assertEquals("withAlwaysAsId should not modify other properties", resolver, modifiedObjectIdInfo.getResolverType());
    }
}

