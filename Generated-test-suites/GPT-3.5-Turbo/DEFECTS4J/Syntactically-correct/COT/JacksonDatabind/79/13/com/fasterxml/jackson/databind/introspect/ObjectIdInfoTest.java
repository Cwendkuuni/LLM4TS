
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Assert;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);

        Assert.assertEquals(name, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        Assert.assertEquals(name, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithDeprecatedString() {
        String name = "testName";
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        Assert.assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        Assert.assertTrue(updatedObjectIdInfo.getAlwaysAsId());
        Assert.assertEquals(name, updatedObjectIdInfo.getPropertyName());
        Assert.assertEquals(scope, updatedObjectIdInfo.getScope());
        Assert.assertEquals(generator, updatedObjectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, updatedObjectIdInfo.getResolverType());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;

        String expectedString = "ObjectIdInfo: propName=testName, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);

        Assert.assertEquals(expectedString, objectIdInfo.toString());
    }
}

