import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import corina.util.*;
import corina.*;
import corina.formats.TucsonSimple;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "TucsonSimple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        // Case 1: Sample with "id" key in meta
        Sample sample1 = new Sample();
        sample1.meta.put("id", "1234567");
        String expected1 = "123456";
        String actual1 = tucsonSimple.make6digitCode(sample1);
        assertEquals(expected1, actual1);
        
        // Case 2: Sample without "id" key in meta
        Sample sample2 = new Sample();
        String expected2 = "000000";
        String actual2 = tucsonSimple.make6digitCode(sample2);
        assertEquals(expected2, actual2);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2022);
        
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        String expected = "123456  2022";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        
        tucsonSimple.saveData(sample, writer);
        
        // Verify the content in the writer
        String expected = "000000        999\n";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        // Case 1: Sample range starts from year 1
        Sample sample1 = new Sample();
        sample1.range = new Range(new Year(1), new Year(10));
        Range expected1 = new Range(new Year(1), new Year(10));
        Range actual1 = tucsonSimple.computeRange(sample1);
        assertEquals(expected1, actual1);
        
        // Case 2: Sample range starts before year 1 but ends after year 1 + 8000
        Sample sample2 = new Sample();
        sample2.range = new Range(new Year(8000), new Year(8010));
        Range expected2 = new Range(new Year(8000), new Year(8010));
        Range actual2 = tucsonSimple.computeRange(sample2);
        assertEquals(expected2, actual2);
        
        // Case 3: Sample range starts before year 1 and ends before year 1 + 8000
        Sample sample3 = new Sample();
        sample3.range = new Range(new Year(999), new Year(1005));
        Range expected3 = new Range(new Year(1001), new Year(1005));
        Range actual3 = tucsonSimple.computeRange(sample3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        
        tucsonSimple.save(sample, writer);
        
        // Verify the content in the writer
        String expected = "000000        999\n\n";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
}