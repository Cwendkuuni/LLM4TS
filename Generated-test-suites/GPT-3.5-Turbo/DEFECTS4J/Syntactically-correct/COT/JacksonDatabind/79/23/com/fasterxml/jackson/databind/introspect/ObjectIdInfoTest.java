import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithNameScopeGenResolver() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<?> gen = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameScopeGen() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<?> gen = ObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(ObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameScopeGenAlwaysAsId() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<?> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, alwaysAsId);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(ObjectIdResolver.class, info.getResolverType());
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameScopeGenAlwaysAsIdResolver() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<?> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = true;
        Class<?> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, alwaysAsId, resolver);

        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<?> gen = ObjectIdGenerator.class;
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);

        ObjectIdInfo updatedInfo = info.withAlwaysAsId(true);

        assertEquals(name, updatedInfo.getPropertyName());
        assertEquals(scope, updatedInfo.getScope());
        assertEquals(gen, updatedInfo.getGeneratorType());
        assertEquals(ObjectIdResolver.class, updatedInfo.getResolverType());
        assertTrue(updatedInfo.getAlwaysAsId());
    }
}