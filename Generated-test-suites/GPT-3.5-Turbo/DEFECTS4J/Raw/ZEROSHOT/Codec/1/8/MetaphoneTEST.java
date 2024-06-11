import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    @Test
    public void testMetaphone() {
        Assert.assertEquals("JN", metaphone.metaphone("john"));
        Assert.assertEquals("JN", metaphone.metaphone("jane"));
        Assert.assertNotEquals("KN", metaphone.metaphone("can"));
    }

    @Test
    public void testEncode() {
        String encoded = (String) metaphone.encode("john");
        Assert.assertEquals("JN", encoded);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Assert.assertTrue(metaphone.isMetaphoneEqual("john", "jane"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("can", "fan"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Assert.assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(5);
        Assert.assertEquals(5, metaphone.getMaxCodeLen());
    }

}