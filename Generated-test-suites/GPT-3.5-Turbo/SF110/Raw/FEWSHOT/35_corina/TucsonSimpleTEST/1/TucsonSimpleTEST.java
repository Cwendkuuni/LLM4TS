package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import corina.util.*;
import corina.ui.Sample;

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
        Sample s = new Sample();
        s.meta.put("id", "123456");
        assertEquals("123456", tucsonSimple.make6digitCode(s));
        
        s.meta.put("id", "123456789");
        assertEquals("123456", tucsonSimple.make6digitCode(s));
        
        s.meta.remove("id");
        assertEquals("000000", tucsonSimple.make6digitCode(s));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(1000), new Year(2000));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1990);
        
        tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
        assertEquals("123456  1990", w.toString());
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.data.add(10);
        s.count.add(5);
        s.range = new Range(new Year(1000), new Year(2000));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        
        tucsonSimple.saveData(s, w);
        
        String expectedOutput = "     10   005";
        expectedOutput += System.lineSeparator();
        
        assertEquals(expectedOutput, w.toString());
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(1000), new Year(2000));
        Range range = tucsonSimple.computeRange(s);
        assertEquals(s.range, range);
        
        s.range = new Range(new Year(1), new Year(100));
        range = tucsonSimple.computeRange(s);
        assertEquals(new Range(new Year(1001), new Year(1100)), range);
        
        s.range = new Range(new Year(-100), new Year(100));
        range = tucsonSimple.computeRange(s);
        assertEquals(new Range(new Year(7900), new Year(8100)), range);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.data.add(10);
        s.count.add(5);
        s.range = new Range(new Year(1000), new Year(2000));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        
        tucsonSimple.save(s, w);
        
        String expectedOutput = "     10   005";
        expectedOutput += System.lineSeparator();
        
        assertEquals(expectedOutput, w.toString());
    }
}