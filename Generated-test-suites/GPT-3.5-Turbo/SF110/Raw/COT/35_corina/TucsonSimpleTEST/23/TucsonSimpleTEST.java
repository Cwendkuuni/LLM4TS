
import static org.junit.Assert.*;
import org.junit.Test;
import corina.formats.*;
import java.io.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "format.tucsonsimple";
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
        BufferedReader reader = new BufferedReader(new StringReader("sample data"));
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCode_existingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCode_invalidId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader_startIsBeforeSampleRangeStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1999);
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, y);
        
        String expected = "123456 2000";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader_startIsAfterSampleRangeStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2001);
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, y);
        
        String expected = "123456 2001";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader_startIsSameAsSampleRangeStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2000);
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, y);
        
        String expected = "123456 2000";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader_isProcessed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        String code = "123456";
        int yearWidth = 4;
        Year y = new Year(2000);
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, y);
        
        String expected = "1234 2000";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSaveData_nullSample() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(null, writer);
    }
    
    // Add more test cases for saveData method
    
    @Test
    public void testComputeRange_startIsBeforeYear1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(9000), new Year(9020));
        
        Range expected = new Range(new Year(1001), new Year(1001).add(9000));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRange_startIsAfterYear1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        
        Range expected = new Range(new Year(2000), new Year(2020));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    // Add more test cases for computeRange method
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.save(sample, writer);
        
        // Add assertions for the written content on the writer
    }
}

