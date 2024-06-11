import static org.junit.Assert.*;
import org.junit.*;

public class ConvertTest {

    Convert convert;

    @Before
    public void setUp() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
    }

    @Test
    public void testInit_ThreadSelected() {
        convert.init();
        boolean result = convert.m_Thread.isAlive();
        assertTrue(result);
    }

    @Test
    public void testInit_ThreadNotSelected() {
        convert.m_GUI.menuChkThread.setSelected(false);
        convert.init();
        boolean result = convert.m_Thread.isAlive();
        assertFalse(result);
    }

    @Test
    public void testTranscode_Quality0() {
        convert.m_Quality = 0;
        convert.Transcode();
        String expectedFlags = "'8, -c -q 2 -4 1 -2 1 -H -K file=matrix.txt -R 2'";
        assertEquals(expectedFlags, convert.m_flags);
    }

    @Test
    public void testTranscode_Quality1() {
        convert.m_Quality = 1;
        convert.Transcode();
        String expectedFlags = "'8, -c -q 4 -4 2 -2 2 -K file=matrix.txt -R 1'";
        assertEquals(expectedFlags, convert.m_flags);
    }

    @Test
    public void testTranscode_Quality2() {
        convert.m_Quality = 2;
        convert.Transcode();
        String expectedFlags = "'8, -c -q 6 -4 3 -2 3 -N 0.5 -E -10 -K tmpgenc -R 0'";
        assertEquals(expectedFlags, convert.m_flags);
    }

    @After
    public void tearDown() {
        convert = null;
    }

}