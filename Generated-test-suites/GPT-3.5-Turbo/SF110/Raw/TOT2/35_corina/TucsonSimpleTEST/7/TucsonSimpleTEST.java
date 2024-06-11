import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import corina.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple ts = new TucsonSimple();
        assertEquals("Should return correct string", "format.tucsonsimple", ts.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple ts = new TucsonSimple();
        assertEquals("Should return default extension", ".RWL", ts.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple ts = new TucsonSimple();
        ts.load(new BufferedReader(new StringReader("test")));
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple ts = new TucsonSimple();
        Sample s = new Sample();
        s.meta.put("id", "123");
        assertEquals("Should return correct code when id exists in meta",
                     "123   ", ts.make6digitCode(s));
                     
        Sample s2 = new Sample();
        assertEquals("Should return default code when id does not exist in meta",
                     "000000", ts.make6digitCode(s2));
    }
    
    @Test
    public void testSaveRowHeader() {
        TucsonSimple ts = new TucsonSimple();
        Sample s = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2021);
        StringWriter writer = new StringWriter();
        BufferedWriter bw = new BufferedWriter(writer);
        
        try {
            ts.saveRowHeader(bw, s, code, yearWidth, y);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        assertEquals("Should write correct row header to buffer", "1234562021", writer.toString());
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple ts = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(1990), new Year(1995));
        assertEquals("Should return same range if start year is not less than 1",
                     new Range(new Year(1990), new Year(1995)), ts.computeRange(s));
                     
        s.range = new Range(new Year(-5), new Year(1995));
        assertEquals("Should return range re-dated by 8000 if start year + 8000 is not less than 1",
                     new Range(new Year(7995), new Year(1995)), ts.computeRange(s));
                     
        s.range = new Range(new Year(-100), new Year(1995));
        assertEquals("Should return range re-dated to start year 1001 if start year is less than 1",
                     new Range(new Year(1001), new Year(1995)), ts.computeRange(s));
    }
    
    @Test
    public void testSaveData() {
        // Test for saveData method can be written based on specific implementation details
    }
    
    @Test
    public void testSave() {
        // Test for save method can be written based on specific implementation details
    }
}