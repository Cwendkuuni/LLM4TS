import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private Sample sample;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }

    @Test
    public void testToString() {
        assertEquals("Expected format name", "Tucson Simple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals("Expected default extension", ".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test
    public void testLoad() {
        try {
            tucsonSimple.load(new BufferedReader(new StringReader("")));
            fail("Expected WrongFiletypeException to be thrown");
        } catch (WrongFiletypeException e) {
            // Expected exception
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testMake6digitCode() {
        sample = new Sample();
        sample.meta.put("id", "123456789");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("Expected 6 digit code", "123456", code);
    }

    @Test
    public void testSaveRowHeader() {
        sample = new Sample();
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        tucsonSimple.saveRowHeader(writer, sample, "123456", 10, new Year());
        assertEquals("Expected row header", "123456    ", stringWriter.toString());
    }

    @Test
    public void testSaveData() {
        sample = new Sample();
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        try {
            tucsonSimple.saveData(sample, writer);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        // Add more assertions for specific cases
    }

    @Test
    public void testComputeRange() {
        sample = new Sample(new Range(new Year(5000), new Year(6000)));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals("Expected range to be re-dated", new Year(1001), range.getStart());
    }

    @Test
    public void testSave() {
        sample = new Sample();
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        try {
            tucsonSimple.save(sample, writer);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        // Add more assertions for specific cases
    }
}