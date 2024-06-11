package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import corina.Sample;
import corina.WrongFiletypeException;
import corina.Year;
import corina.util.Range;
import corina.util.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = null;
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample1 = new Sample();
        Sample sample2 = new Sample();
        Map<String, Object> meta1 = new HashMap<>();
        meta1.put("id", "123456");
        sample1.meta = meta1;
        Map<String, Object> meta2 = new HashMap<>();
        sample2.meta = meta2;
        assertEquals("123456", tucsonSimple.make6digitCode(sample1));
        assertEquals("000000", tucsonSimple.make6digitCode(sample2));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(2020));
        BufferedWriter w = null;
        tucsonSimple.saveRowHeader(w, sample, "123456", 4, new Year(1995));
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(2020));
        BufferedWriter w = null;
        tucsonSimple.saveData(sample, w);
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample1 = new Sample();
        Sample sample2 = new Sample();
        sample1.range = new Range(new Year(1990), new Year(2020));
        sample2.range = new Range(new Year(0), new Year(10));
        assertEquals(new Range(new Year(1990), new Year(2020)), tucsonSimple.computeRange(sample1));
        assertEquals(new Range(new Year(8000), new Year(8010)), tucsonSimple.computeRange(sample2));
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter w = null;
        tucsonSimple.save(sample, w);
    }
    
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
}