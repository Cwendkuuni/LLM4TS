import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(CustomObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithDeprecated() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("id", String.class, CustomObjectIdGenerator.class);
        
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("id", String.class, CustomObjectIdGenerator.class);
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        assertNotEquals(objectIdInfo, updatedObjectIdInfo);
        assertTrue(updatedObjectIdInfo.getAlwaysAsId());
    }

    // Edge cases and exception handling scenarios should be added here

}