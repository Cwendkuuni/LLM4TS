import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testInit() {
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", DVD_GUI);
        assertTrue(convert.init());
    }

    @Test(expected = NullPointerException.class)
    public void testInit_NullPointer() {
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert(null, "16:9", DVD_GUI);
        convert.init();
    }

    @Test
    public void testRun() {
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", DVD_GUI);
        convert.run();
    }

    @Test
    public void testTranscode() {
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", DVD_GUI);
        convert.Transcode();
    }

    @Test
    public void testMatrix() {
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", DVD_GUI);
        convert.matrix();
    }

    @Test
    public void testConstructor1() {
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", DVD_GUI);
        assertNotNull(convert);
    }

    @Test
    public void testConstructor2() {
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert(2, "dvd-ntsc", "16:9", DVD_GUI);
        assertNotNull(convert);
    }

}