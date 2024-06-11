import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple ts = new TucsonSimple();
        assertEquals("format.tucsonsimple", ts.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple ts = new TucsonSimple();
        assertEquals(".RWL", ts.getDefaultExtension());
    }
    
    @Test
    public void testLoad() {
        boolean exceptionThrown = false;
        TucsonSimple ts = new TucsonSimple();
        try {
            ts.load(new BufferedReader(new StringReader("")));
        } catch (IOException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple ts = new TucsonSimple();
        Sample sample = new Sample();
        sample.getMeta().put("id", "12345");
        assertEquals("12345 ", ts.make6digitCode(sample));
    }
    
    @Test
    public void testSaveRowHeader() {
        TucsonSimple ts = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String code = "123456";
        Year year = new Year(2000);
        try {
            ts.saveRowHeader(writer, sample, code, 6, year);
            assertEquals("1234562000", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSaveData() {
        TucsonSimple ts = new TucsonSimple();
        Sample sample = new Sample();
        sample.getData().add(4);
        sample.getData().add(6);
        sample.getRange().setStart(new Year(2000));
        sample.getRange().setEnd(new Year(2001));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        
        try {
            ts.saveData(sample, writer);
            assertEquals("000004000002000005000001999   ", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple ts = new TucsonSimple();
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(100));
        
        Range range = ts.computeRange(sample);
        assertEquals(new Year(1001), range.getStart());
        assertEquals(new Year(9999), range.getEnd());
    }
    
    @Test
    public void testSave() {
        TucsonSimple ts = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        
        try {
            ts.save(sample, writer);
            assertEquals("000000200000201999   ", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}