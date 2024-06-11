import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert c = new Convert("format", "aspectRatio", new GUI());
        boolean result = c.init();
        assertFalse(result);
    }

    @Test
    public void testRun() {
        Convert c = new Convert(0, "format", "aspectRatio", new GUI());
        c.run();
    }

    @Test
    public void testTranscode() {
        Convert c = new Convert("format", "aspectRatio", new GUI());
        c.Transcode();
    }

    @Test
    public void testMatrix() {
        Convert c = new Convert("format", "aspectRatio", new GUI());
        c.matrix();
    }
}