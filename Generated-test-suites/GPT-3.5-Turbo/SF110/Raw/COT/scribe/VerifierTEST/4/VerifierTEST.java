
import org.junit.Assert;
import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        String expected = "test";
        String actual = verifier.getValue();
        Assert.assertEquals(expected, actual);
    }
}
