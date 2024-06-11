
package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import corina.Range;
import corina.Sample;
import corina.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {
    
    private TucsonSimple tucsonSimple;
    
    @Before
    public void setUp() {
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
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
        fail("Expected WrongFiletypeException to be thrown");
    }
    
    @Test
    public void testMake6digitCode() {
        Sample s = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(s));
        
        s.meta.put("id", "1234567");
        assertEquals("123456", tucsonSimple.make6digitCode(s));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        Sample s = new Sample();
        s.range = new Range(new Year(2000), new Year(2010));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveRowHeader(writer, s, "123456", 6, new Year(2005));
        
        String expectedOutput = "123456  2005";
        assertEquals(expectedOutput, writer.toString());
    }
    
    @Test
    public void testSaveData() throws IOException {
        Sample s = new Sample();
        s.data.put(0, 1.0);
        s.data.put(1, 2.0);
        s.range = new Range(new Year(2000), new Year(2001));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(s, writer);
        
        String expectedOutput = "      1     2";
        assertEquals(expectedOutput, writer.toString().trim());
    }
    
    @Test
    public void testComputeRange() {
        Sample s = new Sample();
        s.range = new Range(new Year(-1), new Year(10));
        
        Range result = tucsonSimple.computeRange(s);
        
        assertEquals(new Year(1001), result.getStart());
        assertEquals(new Year(10), result.getEnd());
    }
    
    @Test
    public void testSave() throws IOException {
        Sample s = new Sample();
        s.data.put(0, 1.0);
        s.data.put(1, 2.0);
        s.range = new Range(new Year(2000), new Year(2001));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.save(s, writer);
        
        String expectedOutput = "      1     2";
        assertEquals(expectedOutput, writer.toString().trim());
    }
}
