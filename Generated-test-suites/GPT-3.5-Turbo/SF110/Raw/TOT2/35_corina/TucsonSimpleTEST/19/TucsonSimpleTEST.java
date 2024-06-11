import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import corina.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "TucsonSimple";
        assertEquals(expected, tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        assertEquals(expected, tucsonSimple.getDefaultExtension());
    }
    
    @Test
    public void testLoad() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new StringReader("sample data"));
        try {
            tucsonSimple.load(r);
            fail("Exception not thrown");
        } catch (IOException e) {
            assertTrue(e instanceof WrongFiletypeException);
        }
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");
        String expected = "123456";
        assertEquals(expected, tucsonSimple.make6digitCode(sample));
    }
    
    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter w = new BufferedWriter(new StringWriter());
        String expected = "000123";
        tucsonSimple.saveRowHeader(w, sample, "000", 6, new Year(123));
        assertEquals(expected, w.toString());
    }
    
    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter w = new BufferedWriter(new StringWriter());
        assertDoesNotThrow(() -> tucsonSimple.saveData(sample, w));
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range expected = new Range(new Year(1001), new Year(8000));
        assertEquals(expected, tucsonSimple.computeRange(sample));
    }
    
    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter w = new BufferedWriter(new StringWriter());
        assertDoesNotThrow(() -> tucsonSimple.save(sample, w));
    }
}