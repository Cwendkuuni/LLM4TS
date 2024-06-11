package corina.formats.test;

import corina.formats.TucsonSimple;
import corina.ui.Sample;
import corina.util.Range;
import corina.util.Year;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }
    
    @Test
    public void testToString() {
        String expected = "Tucson Simple";
        String result = tucsonSimple.toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetDefaultExtension() {
        String expected = ".RWL";
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(expected, result);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader r = new BufferedReader(new StringReader(""));
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        Sample sampleWithId = new Sample();
        sampleWithId.meta.put("id", "1234567"); // length > 6
        String expected = "123456";
        String result = tucsonSimple.make6digitCode(sampleWithId);
        assertEquals(expected, result);
        
        Sample sampleWithoutId = new Sample();
        expected = "000000";
        result = tucsonSimple.make6digitCode(sampleWithoutId);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        Sample sample = new Sample();
        BufferedWriter w = new BufferedWriter(new StringWriter());
        String code = "123456";
        int yearWidth = 4;
        Year y = new Year(2021);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        String expected = "1234562021";
        String result = w.toString().trim();
        assertEquals(expected, result);
    }
    
    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();
        sample.data.put(0, 60); // assuming start year is 1960
        sample.data.put(1, 70);
        sample.data.put(2, 80);
        sample.range = new Range(new Year(1960), new Year(1962));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(sample, w);
        String expected = "     1960" + System.lineSeparator()
            + "000060" + System.lineSeparator()
            + "000070" + System.lineSeparator()
            + "000080";
        String result = w.toString().trim();
        assertEquals(expected, result);
    }
    
    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(1950), new Year(1960));
        Range expected = new Range(new Year(1001), new Year(1960));
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
        
        sample.range = new Range(new Year(1990), new Year(2000));
        expected = new Range(new Year(1990), new Year(2000));
        result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
        
        sample.range = new Range(new Year(8000), new Year(8010));
        expected = new Range(new Year(1001), new Year(8002));
        result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSave() throws IOException {
        Sample sample = new Sample();
        sample.data.put(0, 60); // assuming start year is 1960
        sample.data.put(1, 70);
        sample.data.put(2, 80);
        sample.range = new Range(new Year(1960), new Year(1962));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        tucsonSimple.save(sample, w);
        String expected = "     1960" + System.lineSeparator()
            + "000060" + System.lineSeparator()
            + "000070" + System.lineSeparator()
            + "000080" + System.lineSeparator();
        String result = w.toString().trim();
        assertEquals(expected, result);
    }
}