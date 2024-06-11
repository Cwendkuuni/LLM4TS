import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test
    public void testLoad() {
        BufferedReader reader = new BufferedReader(new StringReader("test"));
        assertThrows(IOException.class, () -> tucsonSimple.load(reader));
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", result);
    }

    // Additional tests for make6digitCode to cover edge cases and exceptions

    @Test
    public void testSaveRowHeader() {
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2022);
        assertDoesNotThrow(() -> tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year));
    }

    // Additional tests for saveRowHeader to cover edge cases and exceptions

    @Test
    public void testSaveData() {
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        assertDoesNotThrow(() -> tucsonSimple.saveData(sample, writer));
    }

    // Additional tests for saveData to cover edge cases and exceptions

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(1), new Year(1000));
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(new Year(1001), result.getStart());
        assertEquals(new Year(1000), result.getEnd());
    }

    // Additional tests for computeRange to cover edge cases and exceptions

    @Test
    public void testSave() {
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        assertDoesNotThrow(() -> tucsonSimple.save(sample, writer));
    }

    // Additional tests for save to cover edge cases and exceptions

}