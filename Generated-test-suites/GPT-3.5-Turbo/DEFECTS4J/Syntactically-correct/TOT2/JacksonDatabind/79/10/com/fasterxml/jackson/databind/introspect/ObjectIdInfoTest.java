import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Test
    public void testConstructorWithResolver() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Integer.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Long.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertNotNull(newObjectIdInfo);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Double.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        String expected = "ObjectIdInfo: propName=" + propertyName + ", scope=" + scope.getName() +
                ", generatorType=" + generator.getName() + ", alwaysAsId=false";

        assertEquals(expected, objectIdInfo.toString());
    }
}