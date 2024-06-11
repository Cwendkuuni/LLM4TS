import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    @Test
    public void testObjectIdInfoConstructor() {
        PropertyName prop = new PropertyName("testProp");
        Class<Integer> scope = Integer.class;
        Class<HashCodeBasedObjectIdGenerator> gen = HashCodeBasedObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, false, SimpleObjectIdResolver.class);
        assertEquals("testProp", objectIdInfo.getPropertyName().getName());
        assertEquals(Integer.class, objectIdInfo.getScope());
        assertEquals(HashCodeBasedObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
}