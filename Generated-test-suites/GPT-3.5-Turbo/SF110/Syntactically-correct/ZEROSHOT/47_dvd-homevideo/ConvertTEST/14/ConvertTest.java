import org.junit.*;

public class ConvertTest {
    
    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        boolean result = convert.init();
        Assert.assertFalse(result);
    }
    
    @Test
    public void testRun() {
        Convert convert = new Convert("dvd-pal", "4:3", new GUI());
        convert.run();
        // Assert statements
    }
    
    @Test
    public void testTranscode() {
        Convert convert = new Convert(2, "dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        // Assert statements
    }
    
    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-pal", "4:3", new GUI());
        convert.matrix();
        // Assert statements
    }
}