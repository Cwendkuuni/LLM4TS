
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.util.Sample;
import corina.util.WrongFiletypeException;

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
        BufferedReader r = null; // create BufferedReader object
        
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample(new HashMap<String, Object>());
        
        String code1 = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code1);
        
        sample.meta.put("id", "1234567");
        String code2 = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code2);
        
        String code3 = tucsonSimple.make6digitCode(null);
        assertEquals("000000", code3);
    }
    
    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample(new HashMap<String, Object>());
        BufferedWriter w = null; // create BufferedWriter object
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2021);
        
        try {
            tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // assert the expected result
        
    }
    
    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample(new HashMap<String, Object>());
        BufferedWriter w = null; // create BufferedWriter object
        
        try {
            tucsonSimple.saveData(sample, w);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // assert the expected result
        
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample(new HashMap<String, Object>());
        
        Range range1 = tucsonSimple.computeRange(sample);
        assertEquals(sample.range, range1);
        
        Year start2 = new Year(0);
        sample.range = new Range(start2, new Year(10));
        Range range2 = tucsonSimple.computeRange(sample);
        assertEquals(new Range(start2, new Year(10)), range2);
        
        Year start3 = new Year(-8000);
        sample.range = new Range(start3, new Year(10));
        Range range3 = tucsonSimple.computeRange(sample);
        assertEquals(new Range(start3.add(8000), new Year(10)), range3);
        
        Year start4 = new Year(-9000);
        sample.range = new Range(start4, new Year(10));
        Range range4 = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(1001), new Year(10)), range4);
    }
    
    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample(new HashMap<String, Object>());
        BufferedWriter w = null; // create BufferedWriter object
        
        try {
            tucsonSimple.save(sample, w);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // assert the expected result
        
    }
}
