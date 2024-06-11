import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        assertEquals("format.tucsonsimple", result);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(".RWL", result);
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
        sample.meta.put("id", "1234567");
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", result);
        
        Sample sample2 = new Sample();
        String result2 = tucsonSimple.make6digitCode(sample2);
        assertEquals("000000", result2);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        Sample sample = new Sample();
        Year year = new Year(2000);
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
        writer.close();
        
        // Verify the output in the file "test.rwl"
        // code + " " + year
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.saveData(sample, writer);
        writer.close();
        
        // Verify the output in the file "test.rwl"
        // should match the expected format of TucsonSimple.saveData() method
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(2000));
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(sample.range, result);
        
        Sample sample2 = new Sample();
        sample2.range = new Range(new Year(0), new Year(2000));
        Range result2 = tucsonSimple.computeRange(sample2);
        assertEquals(sample2.range.redateBy(8000), result2);
        
        Sample sample3 = new Sample();
        sample3.range = new Range(new Year(0), new Year(1000));
        Range result3 = tucsonSimple.computeRange(sample3);
        assertEquals(sample3.range.redateStartTo(new Year(1001)), result3);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.save(sample, writer);
        writer.close();
        
        // Verify the output in the file "test.rwl"
        // should match the expected format of TucsonSimple.save() method
    }
}