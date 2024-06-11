import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("id", String.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class);
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);
        assertEquals("ObjectIdInfo: propName=id, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }
}