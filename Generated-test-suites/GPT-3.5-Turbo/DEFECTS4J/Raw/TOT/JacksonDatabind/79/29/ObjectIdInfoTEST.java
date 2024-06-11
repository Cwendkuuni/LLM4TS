import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testGetPropertyName() {
        // Typical use case
        PropertyName propertyName = new PropertyName("name");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, String.class, null, false);
        assertEquals(propertyName, objectIdInfo.getPropertyName());

        // Edge case
        propertyName = new PropertyName("");
        objectIdInfo = new ObjectIdInfo(propertyName, String.class, null, false);
        assertEquals(propertyName, objectIdInfo.getPropertyName());

        // Error scenario
        objectIdInfo = new ObjectIdInfo(null, String.class, null, false);
        assertNull(objectIdInfo.getPropertyName());
    }

    @Test
    public void testGetScope() {
        // Typical use case
        Class<?> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, null, false);
        assertEquals(scope, objectIdInfo.getScope());

        // Edge case
        scope = Object.class;
        objectIdInfo = new ObjectIdInfo(null, scope, null, false);
        assertEquals(scope, objectIdInfo.getScope());

        // Error scenario
        objectIdInfo = new ObjectIdInfo(null, null, null, false);
        assertNull(objectIdInfo.getScope());
    }

    @Test
    public void testGetGeneratorType() {
        // Typical use case
        Class<? extends ObjectIdGenerator<?>> generatorType = MyObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, generatorType, false);
        assertEquals(generatorType, objectIdInfo.getGeneratorType());

        // Edge case
        generatorType = AnotherObjectIdGenerator.class;
        objectIdInfo = new ObjectIdInfo(null, null, generatorType, false);
        assertEquals(generatorType, objectIdInfo.getGeneratorType());

        // Error scenario
        objectIdInfo = new ObjectIdInfo(null, null, null, false);
        assertNull(objectIdInfo.getGeneratorType());
    }

    @Test
    public void testGetResolverType() {
        // Typical use case
        Class<? extends ObjectIdResolver> resolverType = MyObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, false, resolverType);
        assertEquals(resolverType, objectIdInfo.getResolverType());

        // Edge case
        resolverType = AnotherObjectIdResolver.class;
        objectIdInfo = new ObjectIdInfo(null, null, null, false, resolverType);
        assertEquals(resolverType, objectIdInfo.getResolverType());

        // Error scenario
        objectIdInfo = new ObjectIdInfo(null, null, null, false, null);
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testGetAlwaysAsId() {
        // Typical use case
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, true);
        assertTrue(objectIdInfo.getAlwaysAsId());

        // Edge case
        objectIdInfo = new ObjectIdInfo(null, null, null, false);
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
}