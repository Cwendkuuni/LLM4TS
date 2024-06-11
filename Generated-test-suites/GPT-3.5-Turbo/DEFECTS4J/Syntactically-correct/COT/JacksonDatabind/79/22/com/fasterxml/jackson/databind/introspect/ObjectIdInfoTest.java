import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        assertEquals(new PropertyName("name"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class);
        assertEquals(new PropertyName("name"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("name", String.class, MyObjectIdGenerator.class);
        assertEquals(new PropertyName("name"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        assertFalse(objectIdInfo.getAlwaysAsId());
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(objectIdInfo.getAlwaysAsId());
    }
}