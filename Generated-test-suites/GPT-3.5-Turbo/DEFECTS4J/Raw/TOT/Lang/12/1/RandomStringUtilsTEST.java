
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Before
    public void setUp() {
        // Setup code goes here
    }

    @After
    public void tearDown() {
        // Teardown code goes here
    }

    @Test
    public void testRandom() {
        // Typical use case
        String randomString = RandomStringUtils.random(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());

        // Edge case
        randomString = RandomStringUtils.random(0);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(0, randomString.length());

        // Error scenario
        try {
            randomString = RandomStringUtils.random(-5);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException is expected
        }
    }

    @Test
    public void testRandomAscii() {
        // Typical use case
        String randomString = RandomStringUtils.randomAscii(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());

        // Edge case
        randomString = RandomStringUtils.randomAscii(0);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(0, randomString.length());
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String randomString = RandomStringUtils.randomAlphabetic(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());

        // Edge case
        randomString = RandomStringUtils.randomAlphabetic(0);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(0, randomString.length());
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());

        // Edge case
        randomString = RandomStringUtils.randomAlphanumeric(0);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(0, randomString.length());
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case
        String randomString = RandomStringUtils.randomNumeric(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());

        // Edge case
        randomString = RandomStringUtils.randomNumeric(0);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(0, randomString.length());
    }

    @Test
    public void testRandomWithChars() {
        // Typical use case
        String randomString = RandomStringUtils.random(5, true, true, new char[]{'a', 'b', 'c'});
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches("[a-c]*"));

        // Edge case
        randomString = RandomStringUtils.random(0, true, true, new char[]{'a', 'b', 'c'});
        Assert.assertNotNull(randomString);
        Assert.assertEquals(0, randomString.length());
    }

    @Test
    public void testRandomWithString() {
        // Typical use case
        String randomString = RandomStringUtils.random(5, "abc");
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches("[abc]*"));

        // Edge case
        randomString = RandomStringUtils.random(0, "abc");
        Assert.assertNotNull(randomString);
        Assert.assertEquals(0, randomString.length());
    }

    @Test
    public void testRandomWithOutOfRangeChars() {
        // Typical use case
        String randomString = RandomStringUtils.random(5, 100, 200, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches("[d-n]*"));

        // Edge case
        randomString = RandomStringUtils.random(0, 100, 200, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(0, randomString.length());
    }
}

