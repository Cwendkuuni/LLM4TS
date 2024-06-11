import org.junit.*;

public class ObjectsTest {
    @Test
    public void testEqual() {
        Assert.assertTrue(Objects.equal(null, null));
        Assert.assertFalse(Objects.equal(null, "test"));
        Assert.assertFalse(Objects.equal("test", null));
        Assert.assertTrue(Objects.equal("test", "test"));
        Assert.assertFalse(Objects.equal("test", "TEST"));
    }
    
    @Test
    public void testHashCode() {
        Assert.assertEquals(Objects.hashCode(null), Arrays.hashCode(new Object[] { null }));
        Assert.assertEquals(Objects.hashCode("test1", "test2"), Arrays.hashCode(new Object[] { "test1", "test2" }));
        Assert.assertEquals(Objects.hashCode(1, 2, 3), Arrays.hashCode(new Object[] { 1, 2, 3 }));
    }
    
    @Test
    public void testToStringHelper() {
        Object obj = new Object();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
        Assert.assertEquals(obj.getClass().getSimpleName(), toStringHelper.toString());
        
        Class<?> clazz = Objects.class;
        toStringHelper = Objects.toStringHelper(clazz);
        Assert.assertEquals(clazz.getSimpleName(), toStringHelper.toString());
        
        String className = "Test";
        toStringHelper = Objects.toStringHelper(className);
        Assert.assertEquals(className, toStringHelper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        Assert.assertNull(Objects.firstNonNull(null, null));
        Assert.assertEquals("test", Objects.firstNonNull("test", null));
        Assert.assertEquals("test", Objects.firstNonNull(null, "test"));
        Assert.assertEquals("test", Objects.firstNonNull("test", "TEST"));
    }
}