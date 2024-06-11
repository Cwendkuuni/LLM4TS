import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, generator, resolver);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testDeprecatedConstructor() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, generator);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertNull(info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testDeprecatedStringConstructor() {
        String name = "test";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, generator);

        assertEquals(new PropertyName(name), info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(generator, info.getGeneratorType());
        assertNull(info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, generator, resolver);

        ObjectIdInfo updatedInfo = info.withAlwaysAsId(true);

        assertEquals(info.getPropertyName(), updatedInfo.getPropertyName());
        assertEquals(info.getScope(), updatedInfo.getScope());
        assertEquals(info.getGeneratorType(), updatedInfo.getGeneratorType());
        assertEquals(info.getResolverType(), updatedInfo.getResolverType());
        assertTrue(updatedInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, generator, resolver);

        String expected = "ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";

        assertEquals(expected, info.toString());
    }

}