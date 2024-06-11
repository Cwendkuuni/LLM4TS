package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    
    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
    }
    
    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader r = mock(BufferedReader.class);
        tucsonSimple.load(r);
        fail("Expected WrongFiletypeException");
    }
    
    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        
        // test when s.meta.containsKey("id") is true
        sample.meta.put("id", "1234567");
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", result);
        
        // test when s.meta.containsKey("id") is false
        sample.meta.remove("id");
        result = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", result);
    }
    
    @Test
    public void testSaveRowHeader() {
        BufferedWriter w = mock(BufferedWriter.class);
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2000);
        try {
            tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
            verify(w).write("123456  2000");
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testSaveData() {
        BufferedWriter w = mock(BufferedWriter.class);
        Sample sample = new Sample();
        try {
            tucsonSimple.saveData(sample, w);
            // perform assertion for expected output
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(2000));
        
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(1990), new Year(2000)), result);
        
        sample.range = new Range(new Year(8000), new Year(8010));
        result = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(8000), new Year(8010)), result);
        
        sample.range = new Range(new Year(1000), new Year(1010));
        result = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(1001), new Year(1010)), result);
    }
    
    @Test
    public void testSave() {
        BufferedWriter w = mock(BufferedWriter.class);
        Sample sample = new Sample();
        try {
            tucsonSimple.save(sample, w);
            // perform assertion for expected output
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
}