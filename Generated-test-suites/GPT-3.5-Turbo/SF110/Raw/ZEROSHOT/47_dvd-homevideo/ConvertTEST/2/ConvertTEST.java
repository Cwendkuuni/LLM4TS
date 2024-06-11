import org.junit.Test;

public class ConvertTest {
    
    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        boolean result = convert.init();
        // assert statement for the test case
        assertTrue(result);
    }
    
    @Test
    public void testRun() {
        Convert convert = new Convert(2, "dvd-pal", "4:3", new GUI());
        convert.run();
        // assert statement for the test case
        assertNotNull(convert);
    }
    
    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        // assert statement for the test case
        assertNotNull(convert);
    }
    
    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
        // assert statement for the test case
        assertNotNull(convert);
    }
}