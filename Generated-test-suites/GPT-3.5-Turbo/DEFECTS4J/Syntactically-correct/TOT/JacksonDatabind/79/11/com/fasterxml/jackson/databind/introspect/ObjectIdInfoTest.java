import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {

    // Test method for ObjectIdInfo(PropertyName, Class, Class, Class)
    @Test
    public void testConstructor1() {
        ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("prop"), Integer.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        
        assertEquals("prop", objIdInfo.getPropertyName().getSimpleName());
        assertEquals(Integer.class, objIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objIdInfo.getGeneratorType());
        assertEquals(CustomObjectIdResolver.class, objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    // Test method for ObjectIdInfo(PropertyName, Class, Class, boolean, Class)
    @Test
    public void testConstructor2() {
        ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("prop"), Integer.class, CustomObjectIdGenerator.class, true, CustomObjectIdResolver.class);
        
        assertEquals("prop", objIdInfo.getPropertyName().getSimpleName());
        assertEquals(Integer.class, objIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objIdInfo.getGeneratorType());
        assertEquals(CustomObjectIdResolver.class, objIdInfo.getResolverType());
        assertTrue(objIdInfo.getAlwaysAsId());
    }

    // Test method for withAlwaysAsId(boolean)
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objIdInfo1 = new ObjectIdInfo(new PropertyName("prop"), Integer.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        ObjectIdInfo objIdInfo2 = objIdInfo1.withAlwaysAsId(true);
        
        assertNotSame(objIdInfo1, objIdInfo2);
        assertTrue(objIdInfo2.getAlwaysAsId());
    }

    // Test method for toString()
    @Test
    public void testToString() {
        ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("prop"), Integer.class, CustomObjectIdGenerator.class, true, CustomObjectIdResolver.class);
        
        String expected = "ObjectIdInfo: propName=prop, scope=java.lang.Integer, generatorType=CustomObjectIdGenerator, alwaysAsId=true";
        assertEquals(expected, objIdInfo.toString());
    }

    // Custom ObjectIdGenerator class for testing
    private static class CustomObjectIdGenerator implements ObjectIdGenerator<Integer> {
        // implementation
    }

    // Custom ObjectIdResolver class for testing
    private static class CustomObjectIdResolver implements ObjectIdResolver {
        // implementation
    }
}