
package corina.formats;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = I18n.getText("format.tucsonsimple");
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
        BufferedReader r = null; // provide appropriate BufferedReader object
        Sample sample = tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sampleWithId = new Sample();
        sampleWithId.meta.put("id", "1234567890");
        Sample sampleWithoutId = new Sample();
        String expectedWithId = "123456";
        String actualWithId = tucsonSimple.make6digitCode(sampleWithId);
        assertEquals(expectedWithId, actualWithId);
        String expectedWithoutId = "000000";
        String actualWithoutId = tucsonSimple.make6digitCode(sampleWithoutId);
        assertEquals(expectedWithoutId, actualWithoutId);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter w = null; // provide appropriate BufferedWriter object
        String code = "123456";
        int yearWidth = 4;
        Year y = new Year(1990);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        // Add appropriate assertions
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.count.put(0, 3);
        sample.count.put(1, 4);
        BufferedWriter w = null; // provide appropriate BufferedWriter object
        tucsonSimple.saveData(sample, w);
        // Add appropriate assertions
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1995));
        Range expected = new Range(new Year(1001), new Year(1995));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter w = null; // provide appropriate BufferedWriter object
        tucsonSimple.save(sample, w);
        // Add appropriate assertions
    }
}
