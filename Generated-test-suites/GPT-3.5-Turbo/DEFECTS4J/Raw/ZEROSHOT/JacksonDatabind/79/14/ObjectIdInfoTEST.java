import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Assert;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithPropertyName() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScope() {
        String name = "test";
        Class<?> scope = String.class;
        Class<ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        Assert.assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameScopeAndGenerator() {
        String name = "test";
        Class<?> scope = String.class;
        Class<ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        Assert.assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        objectIdInfo = objectIdInfo.withAlwaysAsId(true);

        Assert.assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        String expected = "ObjectIdInfo: propName=" + propertyName + ", scope=" + scope.getName() + ", generatorType=" + generator.getName() + ", alwaysAsId=true";

        Assert.assertEquals(expected, objectIdInfo.toString());
    }
}