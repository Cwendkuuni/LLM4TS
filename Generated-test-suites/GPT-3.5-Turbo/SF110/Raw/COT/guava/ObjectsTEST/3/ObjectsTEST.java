
import org.junit.Assert;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Assert.assertTrue(Objects.equal(null, null));
        Assert.assertFalse(Objects.equal(null, ""));
        Assert.assertTrue(Objects.equal("abc", "abc"));
        Assert.assertFalse(Objects.equal("abc", "def"));
    }
    
    @Test
    public void testHashCode() {
        Assert.assertEquals(0, Objects.hashCode());
        Assert.assertEquals(1, Objects.hashCode(null));
        Assert.assertEquals(123, Objects.hashCode(123));
        Assert.assertEquals(277, Objects.hashCode("abc", 123));
    }
    
    @Test
    public void testToStringHelper() {
        // Test omitted null values
        String expectedString = "TestClass{name=abc, value=123}";
        Assert.assertEquals(expectedString, Objects.toStringHelper(TestClass.class)
                .add("name", "abc")
                .add("value", 123)
                .omitNullValues()
                .toString());
        
        // Test not omitted null values
        expectedString = "TestClass{name=abc, value=123, test=null}";
        Assert.assertEquals(expectedString, Objects.toStringHelper(TestClass.class)
                .add("name", "abc")
                .add("value", 123)
                .add("test", null)
                .toString());
    }
    
    @Test
    public void testFirstNonNull() {
        Assert.assertEquals("abc", Objects.firstNonNull("abc", null));
        Assert.assertEquals("def", Objects.firstNonNull(null, "def"));
        Assert.assertNull(Objects.firstNonNull(null, null));
    }
    
    private static class TestClass {
        private String name;
        private int value;
        private Object test;
        
        public TestClass(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
