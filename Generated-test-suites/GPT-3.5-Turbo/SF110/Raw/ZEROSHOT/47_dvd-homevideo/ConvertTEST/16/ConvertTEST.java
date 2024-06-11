import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {

    private Convert convert;

    @Before
    public void setUp() {
        convert = new Convert("format", "aspectRatio", new GUI());
    }

    @Test
    public void testInit() {
        assertFalse(convert.init());
    }

    @Test
    public void testRun() {
        convert.run();
    }

    @Test
    public void testTranscode() {
        convert.Transcode();
    }

    @Test
    public void testMatrix() {
        convert.matrix();
    }

}