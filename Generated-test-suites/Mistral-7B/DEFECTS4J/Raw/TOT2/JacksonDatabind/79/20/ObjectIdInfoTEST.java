
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ObjectIdInfoTest {
    @Test
    public void testObjectIdInfoNoArgsConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, false);
        assertNull(objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoOneArgConstructor() {
        PropertyName propertyName = mock(PropertyName.class);
        Class<?> scope = mock(Class.class);
        Class<? extends ObjectIdGenerator<?>> generator = mock(ObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoTwoArgConstructor() {
        PropertyName propertyName = mock(PropertyName.class);
        Class<?> scope = mock(Class.class);
        Class<? extends ObjectIdGenerator<?>> generator = mock(ObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, SimpleObjectIdResolver.class);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoThreeArgConstructor() {
        PropertyName propertyName = mock(PropertyName.class);
        Class<?> scope = mock(Class.class);
        Class<? extends ObjectIdGenerator<?>> generator = mock(ObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoFourArgConstructor() {
        PropertyName propertyName = mock(PropertyName.class);
        Class<?> scope = mock(Class.class);
        Class<? extends ObjectIdGenerator<?>> generator = mock(ObjectIdGenerator.class);
        boolean alwaysAsId = true;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, SimpleObjectIdResolver.class);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }
}