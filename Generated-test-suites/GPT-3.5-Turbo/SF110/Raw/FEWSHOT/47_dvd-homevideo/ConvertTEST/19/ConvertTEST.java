import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Util;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "1.85", new GUI());
        Assert.assertTrue(convert.init());
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "1.85", new GUI());
        convert.Transcode();
        // Add specific assertions here
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "1.85", new GUI());
        convert.matrix();
        // Add specific assertions here
    }

    // Add more test methods to cover other methods in Convert class

}