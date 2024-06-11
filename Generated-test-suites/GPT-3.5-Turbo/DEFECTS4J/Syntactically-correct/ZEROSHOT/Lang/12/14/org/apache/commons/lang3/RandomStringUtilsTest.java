import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        assertEquals(0, RandomStringUtils.random(0).length());
        assertEquals(5, RandomStringUtils.random(5).length());
        assertEquals(10, RandomStringUtils.random(10).length());
    }

    @Test
    public void testRandomAscii() {
        assertEquals(0, RandomStringUtils.randomAscii(0).length());
        assertEquals(5, RandomStringUtils.randomAscii(5).length());
        assertEquals(10, RandomStringUtils.randomAscii(10).length());
    }
    
    @Test
    public void testRandomAlphabetic() {
        assertEquals(0, RandomStringUtils.randomAlphabetic(0).length());
        assertEquals(5, RandomStringUtils.randomAlphabetic(5).length());
        assertEquals(10, RandomStringUtils.randomAlphabetic(10).length());
    }
    
    @Test
    public void testRandomAlphanumeric() {
        assertEquals(0, RandomStringUtils.randomAlphanumeric(0).length());
        assertEquals(5, RandomStringUtils.randomAlphanumeric(5).length());
        assertEquals(10, RandomStringUtils.randomAlphanumeric(10).length());
    }
    
    @Test
    public void testRandomNumeric() {
        assertEquals(0, RandomStringUtils.randomNumeric(0).length());
        assertEquals(5, RandomStringUtils.randomNumeric(5).length());
        assertEquals(10, RandomStringUtils.randomNumeric(10).length());
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        assertEquals(0, RandomStringUtils.random(0, true, true).length());
        assertEquals(5, RandomStringUtils.random(5, true, true).length());
        assertEquals(10, RandomStringUtils.random(10, true, true).length());
    }
    
    @Test
    public void testRandomWithCustomCharSet() {
        assertEquals(0, RandomStringUtils.random(0, "ABC").length());
        assertEquals(5, RandomStringUtils.random(5, "ABC").length());
        assertEquals(10, RandomStringUtils.random(10, "ABC").length());
    }
}