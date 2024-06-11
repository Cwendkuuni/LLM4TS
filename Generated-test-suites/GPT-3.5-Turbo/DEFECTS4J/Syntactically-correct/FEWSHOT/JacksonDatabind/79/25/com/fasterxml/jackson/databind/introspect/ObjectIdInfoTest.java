import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;

import java.lang.reflect.Field;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        PropertyName propertyName = new PropertyName("foo");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        String propertyName = "foo";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        PropertyName propertyName = new PropertyName("foo");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor4() {
        PropertyName propertyName = new PropertyName("foo");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("foo");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        assertEquals(false, objectIdInfo.getAlwaysAsId());

        objectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertEquals(true, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() throws NoSuchFieldException, IllegalAccessException {
        PropertyName propertyName = new PropertyName("foo");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        Field propertyNameField = objectIdInfo.getClass().getDeclaredField("_propertyName");
        propertyNameField.setAccessible(true);
        propertyNameField.set(objectIdInfo, null);

        Field scopeField = objectIdInfo.getClass().getDeclaredField("_scope");
        scopeField.setAccessible(true);
        scopeField.set(objectIdInfo, null);

        Field generatorField = objectIdInfo.getClass().getDeclaredField("_generator");
        generatorField.setAccessible(true);
        generatorField.set(objectIdInfo, null);

        Field alwaysAsIdField = objectIdInfo.getClass().getDeclaredField("_alwaysAsId");
        alwaysAsIdField.setAccessible(true);
        alwaysAsIdField.set(objectIdInfo, false);

        assertEquals("ObjectIdInfo: propName=null, scope=null, generatorType=null, alwaysAsId=false", objectIdInfo.toString());
    }
}