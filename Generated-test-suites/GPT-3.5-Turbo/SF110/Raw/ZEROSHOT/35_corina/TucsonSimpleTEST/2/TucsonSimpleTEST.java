package corina.formats;

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
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new StringReader(""));
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample1 = new Sample();
        Sample sample2 = new Sample();
        sample2.meta.put("id", "1234567");
        Sample sample3 = new Sample();
        sample3.meta.put("id", "12345678");
        
        assertEquals("000000", tucsonSimple.make6digitCode(sample1));
        assertEquals("123456", tucsonSimple.make6digitCode(sample2));
        assertEquals("123456", tucsonSimple.make6digitCode(sample3));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedWriter w = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2022);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        
        assertEquals("1234562022", w.toString());
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedWriter w = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.data.put(0, 42);
        sample.data.put(1, 23);
        Year start = new Year(2020);
        sample.range.setStart(start);
        Year end = new Year(2021);
        sample.range.setEnd(end);
        
        tucsonSimple.saveData(sample, w);
        
        assertEquals("      42      23", w.toString().trim());
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample sample1 = new Sample();
        sample1.range.setStart(new Year(2020));
        sample1.range.setEnd(new Year(2021));
        Range range1 = tucsonSimple.computeRange(sample1);
        assertEquals(new Year(2020), range1.getStart());
        assertEquals(new Year(2021), range1.getEnd());
        
        Sample sample2 = new Sample();
        sample2.range.setStart(new Year(1));
        Range range2 = tucsonSimple.computeRange(sample2);
        assertEquals(new Year(1), range2.getStart());
        assertEquals(new Year(8001), range2.getEnd());
        
        Sample sample3 = new Sample();
        sample3.range.setStart(new Year(0));
        Range range3 = tucsonSimple.computeRange(sample3);
        assertEquals(new Year(1001), range3.getStart());
        assertEquals(new Year(1001), range3.getEnd());
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        BufferedWriter w = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.data.put(0, 42);
        sample.data.put(1, 23);
        Year start = new Year(2020);
        sample.range.setStart(start);
        Year end = new Year(2021);
        sample.range.setEnd(end);
        
        tucsonSimple.save(sample, w);
        
        assertEquals("      42      23", w.toString().trim());
    }
}