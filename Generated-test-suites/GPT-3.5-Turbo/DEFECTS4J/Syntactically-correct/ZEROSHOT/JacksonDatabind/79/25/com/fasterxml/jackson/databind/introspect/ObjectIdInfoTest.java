import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Assert;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<?> generator = MyObjectIdGenerator.class;
        Class<?> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<?> generator = MyObjectIdGenerator.class;
        Class<?> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<?> generator = MyObjectIdGenerator.class;
        Class<?> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        Assert.assertEquals(objectIdInfo.getPropertyName(), newObjectIdInfo.getPropertyName());
        Assert.assertEquals(objectIdInfo.getScope(), newObjectIdInfo.getScope());
        Assert.assertEquals(objectIdInfo.getGeneratorType(), newObjectIdInfo.getGeneratorType());
        Assert.assertEquals(objectIdInfo.getResolverType(), newObjectIdInfo.getResolverType());
        Assert.assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    static class MyObjectIdGenerator {
        // mock generator class
    }

    static class MyObjectIdResolver {
        // mock resolver class
    }
}