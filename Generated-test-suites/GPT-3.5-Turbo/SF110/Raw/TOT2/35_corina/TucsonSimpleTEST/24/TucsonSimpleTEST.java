import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucson.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals(".RWL", tucson.getDefaultExtension());
    }
    
    @Test
    public void testLoad() {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        assertThrows(WrongFiletypeException.class, () -> {
            tucson.load(reader);
        });
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        assertEquals("123456", tucson.make6digitCode(sample));
        
        sample.meta.remove("id");
        assertEquals("000000", tucson.make6digitCode(sample));
    }
    
    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String code = "123456";
        int yearWidth = 4;
        Year year = new Year(2000);
        
        assertDoesNotThrow(() -> {
            tucson.saveRowHeader(writer, sample, code, yearWidth, year);
        });
    }
    
    @Test
    public void testSaveData() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        
        assertDoesNotThrow(() -> {
            tucson.saveData(sample, writer);
        });
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(1000));
        sample.range.setEnd(new Year(2000));
        
        assertEquals(new Range(new Year(1000), new Year(2000)), tucson.computeRange(sample));
    }
    
    @Test
    public void testSave() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        
        assertDoesNotThrow(() -> {
            tucson.save(sample, writer);
        });
    }
}