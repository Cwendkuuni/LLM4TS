import org.junit.Before;

public class TarUtilsTest {
    
    private TarUtils tarUtils;
    private byte[] buffer;

    @Before
    public void setUp() {
        tarUtils = new TarUtils();
        buffer = new byte[100];
    }

    @org.junit.Test
    public void testParseOctal() {
        long result = tarUtils.parseOctal(buffer, 0, 10);
        // test the assertion here
    }

    @org.junit.Test(expected=IllegalArgumentException.class)
    public void testParseOctalInvalidLength() {
        long result = tarUtils.parseOctal(buffer, 0, 1);
        // test the assertion here
    }

    @org.junit.Test
    public void testParseOctalAllNUL() {
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        long result = tarUtils.parseOctal(buffer, 0, 3);
        // test the assertion here
    }

    // add more test methods for other methods in TarUtils class

}