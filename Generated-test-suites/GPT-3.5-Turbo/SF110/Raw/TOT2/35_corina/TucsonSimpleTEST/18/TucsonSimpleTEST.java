import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.StringWriter;
import corina.util.Sample;
import corina.util.Year;
import java.io.IOException;
import corina.formats.TucsonSimple;
import corina.WrongFiletypeException;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Year year = new Year(2021);
        StringWriter writer = new StringWriter();
        try {
            tucsonSimple.saveRowHeader(new BufferedWriter(writer), sample, "000000", 6, year);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("0000002021", writer.toString());
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(1000));
        sample.range.setEnd(new Year(2000));
        assertEquals(sample.range, tucsonSimple.computeRange(sample));
    }

    @Test
    public void testLoadException() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        try {
            tucsonSimple.load(reader);
            fail("Expected WrongFiletypeException");
        } catch (WrongFiletypeException e) {
            // expected
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add more test cases for edge cases and error scenarios as needed

}