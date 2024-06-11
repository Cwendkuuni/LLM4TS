import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import corina.util.Range;
import corina.util.Year;
import corina.formats.TucsonSimple;
import corina.Sample;
import corina.WrongFiletypeException;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    private Sample sample;
    
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        sample = new Sample();
    }
    
    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader reader = null; // provide actual reader implementation
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCode() {
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }
    
    @Test
    public void testSaveRowHeader() {
        BufferedWriter writer = null; // provide actual writer implementation
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2021);
        assertDoesNotThrow(() -> tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year));
    }
    
    @Test
    public void testSaveData() {
        BufferedWriter writer = null; // provide actual writer implementation
        assertDoesNotThrow(() -> tucsonSimple.saveData(sample, writer));
    }
    
    @Test
    public void testComputeRange() {
        Range range = tucsonSimple.computeRange(sample);
        assertNotNull(range);
    }
    
    @Test
    public void testSave() {
        BufferedWriter writer = null; // provide actual writer implementation
        assertDoesNotThrow(() -> tucsonSimple.save(sample, writer));
    }
}