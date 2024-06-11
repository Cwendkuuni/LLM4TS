import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, SomeObjectIdGenerator.class, true, SomeObjectIdResolver.class);
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
        assertFalse(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals("name", propertyName.getSimpleName());
    }

    @Test
    public void testGetScope() {
        Class<?> scope = objectIdInfo.getScope();
        assertEquals(String.class, scope);
    }

    @Test
    public void testGetGeneratorType() {
        Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
        assertEquals(SomeObjectIdGenerator.class, generatorType);
    }

    @Test
    public void testGetResolverType() {
        Class<? extends ObjectIdResolver> resolverType = objectIdInfo.getResolverType();
        assertEquals(SomeObjectIdResolver.class, resolverType);
    }

    @Test
    public void testGetAlwaysAsId() {
        boolean alwaysAsId = objectIdInfo.getAlwaysAsId();
        assertTrue(alwaysAsId);
    }

    @Test
    public void testToString() {
        String toString = objectIdInfo.toString();
        assertEquals("ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=com.example.SomeObjectIdGenerator, alwaysAsId=true", toString);
    }
}