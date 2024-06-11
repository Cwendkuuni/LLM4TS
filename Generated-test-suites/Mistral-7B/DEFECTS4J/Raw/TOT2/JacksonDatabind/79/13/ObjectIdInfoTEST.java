import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoWithValidArgs() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdByFieldNameGenerator> generator = IdByFieldNameGenerator.class;
        boolean alwaysAsId = false;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(IdByFieldNameGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullPropertyName() {
        Class<String> scope = String.class;
        Class<IdByFieldNameGenerator> generator = IdByFieldNameGenerator.class;
        boolean alwaysAsId = false;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        new ObjectIdInfo(null, scope, generator, alwaysAsId, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<IdByFieldNameGenerator> generator = IdByFieldNameGenerator.class;
        boolean alwaysAsId = false;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        new ObjectIdInfo(propertyName, null, generator, alwaysAsId, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        boolean alwaysAsId = false;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        new ObjectIdInfo(propertyName, scope, null, alwaysAsId, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdByFieldNameGenerator> generator = IdByFieldNameGenerator.class;
        boolean alwaysAsId = false;

        new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdByFieldNameGenerator> generator = IdByFieldNameGenerator.class;
        boolean alwaysAsId = null;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
    }

    @Test
    public void testObjectIdInfoWithValidArgsAndCustomResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdResolver> generator = CustomObjectIdResolver.class;
        boolean alwaysAsId = false;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, null);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdResolver.class, objectIdInfo.getGeneratorType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithCustomAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdByFieldNameGenerator> generator = IdByFieldNameGenerator.class;
        boolean alwaysAsId = true;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);

        assertEquals(objectIdInfo.getPropertyName().getName(), newObjectIdInfo.getPropertyName().getName());
        assertEquals(objectIdInfo.getScope(), newObjectIdInfo.getScope());
        assertEquals(objectIdInfo.getGeneratorType(), newObjectIdInfo.getGeneratorType());
        assertFalse(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdByFieldNameGenerator> generator = IdByFieldNameGenerator.class;
        boolean alwaysAsId = false;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);

        String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.BeanProperty.IdByFieldNameGenerator, alwaysAsId=false";
        String actual = objectIdInfo.toString();

        assertEquals(expected, actual);
    }
}