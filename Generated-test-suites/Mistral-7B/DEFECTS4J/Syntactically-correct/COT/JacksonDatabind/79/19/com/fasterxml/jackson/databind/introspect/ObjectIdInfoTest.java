import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.junit.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithNameScopeGenerator() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, Object.class, generator, resolver);

        Assert.assertEquals(name, objectIdInfo.getPropertyName());
        Assert.assertEquals(Object.class, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithNameScopeGeneratorAlwaysAsId() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, Object.class, generator, resolver, true);

        ObjectIdInfo objectIdInfo2 = objectIdInfo.withAlwaysAsId(false);
        Assert.assertNotEquals(objectIdInfo, objectIdInfo2);
        Assert.assertFalse(objectIdInfo2.getAlwaysAsId());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = new ObjectIdInfo(name, Object.class, generator, resolver, true);
        Assert.assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithNameScopeGeneratorNullResolver() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;

        try {
            ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, Object.class, generator, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testObjectIdInfoConstructorWithNameScope() {
        PropertyName name = new PropertyName("testProperty");
        Class<?> scope = Object.class;
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        try {
            Constructor<ObjectIdInfo> constructor = ObjectIdInfo.class.getConstructor(PropertyName.class,
                    Class.class, Class[].class, Class[].class, Class[].class);
            ObjectIdInfo objectIdInfo = constructor.newInstance(name, scope, new Class<?>[]{generator, resolver, resolver});

            Assert.assertEquals(name, objectIdInfo.getPropertyName());
            Assert.assertEquals(scope, objectIdInfo.getScope());
            Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
            Assert.assertEquals(resolver, objectIdInfo.getResolverType());
            Assert.assertFalse(objectIdInfo.getAlwaysAsId());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, Object.class, generator, resolver);
        String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.JsonDeserializer$IntArrayIdGenerator, alwaysAsId=false";
        String actual = objectIdInfo.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, Object.class, generator, resolver);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        Assert.assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdInfoCannotSetPropertyName() {
        PropertyName name = new PropertyName("testProperty");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, Object.class, ObjectIdGenerators.IntArrayIdGenerator.class, SimpleObjectIdResolver.class);
        try {
            objectIdInfo.setPropertyName(new PropertyName("newName"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testObjectIdInfoEquals() {
        PropertyName name1 = new PropertyName("testProperty");
        PropertyName name2 = new PropertyName("testProperty2");
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(name1, Object.class, generator, resolver);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(name2, Object.class, generator, resolver);
        ObjectIdInfo objectIdInfo3 = new ObjectIdInfo(name1, Object.class, generator, resolver);

        Assert.assertEquals(objectIdInfo1, objectIdInfo3);
        Assert.assertNotEquals(objectIdInfo1, objectIdInfo2);
    }

    @Test
    public void testObjectIdInfoHashCode() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(name, Object.class, generator, resolver);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(name, Object.class, generator, resolver);

        int expectedHashCode = objectIdInfo1.hashCode();
        int actualHashCode = objectIdInfo2.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}