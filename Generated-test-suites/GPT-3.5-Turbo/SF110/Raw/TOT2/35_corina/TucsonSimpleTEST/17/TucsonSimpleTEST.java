import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

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
    public void testLoadException() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader("some data"));
        assertThrows(IOException.class, () -> tucsonSimple.load(reader));
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String code = "123456";
        Year year = new Year(2022);
        assertDoesNotThrow(() -> tucsonSimple.saveRowHeader(writer, sample, code, 6, year));
    }

    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        assertDoesNotThrow(() -> tucsonSimple.saveData(sample, writer));
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2010));
        sample.range = range;
        assertEquals(range, tucsonSimple.computeRange(sample));
    }

    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        assertDoesNotThrow(() -> tucsonSimple.save(sample, writer));
    }
}