package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "Tucson Simple";
        String result = tucsonSimple.toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(expected, result);
    }
    
    @Test
    public void testLoad() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader("test input"));
        assertThrows(IOException.class, () -> {
            tucsonSimple.load(reader);
        });
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", result);
        
        sample.meta.remove("id");
        result = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", result);
    }
    
    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Year year = new Year(2022);
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        assertThrows(IOException.class, () -> {
            tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
        });
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(sample.range, result);
        
        sample.range = new Range(new Year(-1000), new Year(1000));
        result = tucsonSimple.computeRange(sample);
        assertTrue(result.getStart().compareTo(new Year(1001)) >= 0);
    }
    
    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        assertDoesNotThrow(() -> {
            tucsonSimple.save(sample, writer);
        });
    }
}