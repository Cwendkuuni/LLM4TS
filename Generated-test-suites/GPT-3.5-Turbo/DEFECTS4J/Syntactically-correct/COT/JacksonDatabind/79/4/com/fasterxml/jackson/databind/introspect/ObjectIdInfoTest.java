import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = Integer.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        String name = "testName";
        Class<?> scope = Boolean.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);

        assertEquals(new PropertyName(name), info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = Double.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);

        ObjectIdInfo modifiedInfo = info.withAlwaysAsId(true);

        assertNotSame(info, modifiedInfo);
        assertEquals(name, modifiedInfo.getPropertyName());
        assertEquals(scope, modifiedInfo.getScope());
        assertEquals(gen, modifiedInfo.getGeneratorType());
        assertEquals(resolver, modifiedInfo.getResolverType());
        assertTrue(modifiedInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = Float.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);

        String expected = "ObjectIdInfo: propName=testName, scope=java.lang.Float, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, info.toString());
    }
}