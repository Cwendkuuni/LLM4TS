import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import corina.util.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String actual = tucson.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucson.getDefaultExtension();
        assertEquals(expected, actual);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader("dummy content"));
        tucson.load(reader);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        String expected = "123456";
        String actual = tucson.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWithDefault() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucson.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWithLongId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        sample.meta.put("extra", "1234567890");
        String expected = "123456";
        String actual = tucson.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range.setStart(new Year(2000));
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2001);
        tucson.saveRowHeader(writer, sample, code, yearWidth, year);
        
        writer.flush();
        String expected = "123456 2001";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.add(10);
        sample.data.add(20);
        sample.data.add(30);
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucson.saveData(sample, writer);
        
        writer.flush();
        String expected = "      10      20      30\n";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(1000));
        sample.range.setEnd(new Year(2000));
        
        Range expected = new Range(new Year(1000), new Year(2000));
        Range actual = tucson.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartGreaterThanOne() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(3000));
        
        Range expected = new Range(new Year(2000), new Year(3000));
        Range actual = tucson.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanOne() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(1));
        sample.range.setEnd(new Year(100));
        
        Range expected = new Range(new Year(8001), new Year(8100));
        Range actual = tucson.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartLessThanOne() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(-1000));
        sample.range.setEnd(new Year(1000));
        
        Range expected = new Range(new Year(1001), new Year(1000));
        Range actual = tucson.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucson.save(sample, writer);
        
        writer.flush();
        String expected = "\n";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
}