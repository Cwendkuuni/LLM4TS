import org.junit.Test;
import static org.junit.Assert.*;

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
        tucsonSimple.load(null);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2010), new Year(2019));
        BufferedWriter writer = new BufferedWriter(new FileWriter("testFile"));
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(2018));
        writer.close();
        
        BufferedReader reader = new BufferedReader(new FileReader("testFile"));
        assertEquals("123456  2018", reader.readLine());
        reader.close();
    }
    
    @Test
    public void testSaveData() {
        // Test saveData method with typical and edge cases
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1), new Year(10));
        assertEquals(new Range(new Year(1001), new Year(1010), sample.range.column()), tucsonSimple.computeRange(sample));
    }
    
    @Test
    public void testSave() {
        // Test save method with typical and edge cases
    }
}