import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import java.io.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

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
        BufferedReader reader = Mockito.mock(BufferedReader.class);
        Sample sample = Mockito.mock(Sample.class);
        assertThrows(IOException.class, () -> tucsonSimple.load(reader));
    }
    
    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
        
        sample.meta.remove("id");
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
    }
    
    @Test
    public void testSaveRowHeader() {
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Year year = Mockito.mock(Year.class);
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year); // test case for prefix
        tucsonSimple.saveRowHeader(writer, sample, "123456", 4, year); // test case for prefix
    }
    
    @Test
    public void testSaveData() {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        assertDoesNotThrow(() -> tucsonSimple.saveData(sample, writer));
    }
    
    @Test
    public void testComputeRange() {
        Sample sample = Mockito.mock(Sample.class);
        Year startYear = new Year(1000);
        Year endYear = new Year(9000);
        sample.range = new Range(startYear, endYear);
        Range range = tucsonSimple.computeRange(sample);
        assertEquals("1000", range.start.toString());
        assertEquals("1000", range.end.toString());
        
        sample.range = new Range(new Year(1), new Year(8000));
        range = tucsonSimple.computeRange(sample);
        assertEquals("8001", range.start.toString());
        assertEquals("8000", range.end.toString());
    }
    
    @Test
    public void testSave() {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        assertDoesNotThrow(() -> tucsonSimple.save(sample, writer));
    }
}