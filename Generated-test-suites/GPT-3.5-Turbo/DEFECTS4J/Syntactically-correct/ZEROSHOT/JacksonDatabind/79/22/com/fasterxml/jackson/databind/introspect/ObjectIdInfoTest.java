import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithIdResolver() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(ObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutIdResolver() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithOldSignature() {
        ObjectIdInfo info = new ObjectIdInfo("name", String.class, ObjectIdGenerator.class);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class)
                .withAlwaysAsId(true);
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class);
        String expected = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, info.toString());
    }
}