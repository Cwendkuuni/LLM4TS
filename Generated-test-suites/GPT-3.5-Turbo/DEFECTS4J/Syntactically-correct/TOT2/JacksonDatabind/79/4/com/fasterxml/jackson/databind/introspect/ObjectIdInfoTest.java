import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor1() {
        // Test ObjectIdInfo constructor with all parameters
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        assertNotNull(info);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(CustomObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(CustomObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor2() {
        // Test deprecated ObjectIdInfo constructor without resolver
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class);
        assertNotNull(info);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(CustomObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor3() {
        // Test deprecated ObjectIdInfo constructor with name string
        ObjectIdInfo info = new ObjectIdInfo("name", String.class, CustomObjectIdGenerator.class);
        assertNotNull(info);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(CustomObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        // Test withAlwaysAsId method
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        ObjectIdInfo changedInfo = info.withAlwaysAsId(true);
        assertTrue(changedInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        // Test toString method
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        String expected = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=CustomObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, info.toString());
    }
}