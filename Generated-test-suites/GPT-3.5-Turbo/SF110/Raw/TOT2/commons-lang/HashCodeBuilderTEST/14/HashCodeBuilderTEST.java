import org.junit.Test;
import org.junit.Assert;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        // Test for null object
        try {
            HashCodeBuilder.reflectionHashCode(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testHashCodeBuilderConstructor() {
        // Test for odd initial value
        try {
            new HashCodeBuilder(2, 37);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        // Test for odd multiplier
        try {
            new HashCodeBuilder(17, 4);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        Assert.assertEquals(54, hcb.toHashCode());
    }

    // Add more test cases for other methods as needed

}