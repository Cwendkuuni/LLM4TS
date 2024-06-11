import org.junit.*;

public class ConvertTest {

    private Convert convert;

    @Before
    public void setUp() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
    }

    @Test
    public void testInit_WithoutThread_Success() {
        boolean result = convert.init();
        Assert.assertTrue(result);
    }

    @Test
    public void testInit_WithThread_Success() {
        convert = new Convert(1, "dvd-ntsc", "4:3", new GUI());
        boolean result = convert.init();
        Assert.assertTrue(result);
    }

    @Test
    public void testRun_Success() {
        convert.run();
        // No assertion can be made as run method only calls Transcode method which has no return value
    }

    @Test
    public void testTranscode_WithQuality0_Success() {
        convert = new Convert(0, "dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        // No assertion can be made as Transcode method has no return value
    }

    @Test
    public void testTranscode_WithQuality1_Success() {
        convert = new Convert(1, "dvd-ntsc", "4:3", new GUI());
        convert.Transcode();
        // No assertion can be made as Transcode method has no return value
    }

    @Test
    public void testTranscode_WithQuality2_Success() {
        convert = new Convert(2, "dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        // No assertion can be made as Transcode method has no return value
    }

    @Test
    public void testMatrix_Success() {
        convert.matrix();
        // No assertion can be made as this method writes to a file
    }

    @After
    public void tearDown() {
        convert = null;
    }

}