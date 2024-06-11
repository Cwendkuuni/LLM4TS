
import org.junit.*;
import static org.junit.Assert.*;

public class TucsonSimpleTest {
    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String actual = tucson.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucson.getDefaultExtension();
        
        assertEquals(expected, actual);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        
        tucson.load(reader);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        
        String expected = "123456";
        String actual = tucson.make6digitCode(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWithNoId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        
        String expected = "000000";
        String actual = tucson.make6digitCode(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWithLongId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        
        String expected = "123456";
        String actual = tucson.make6digitCode(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2000);
        
        tucson.saveRowHeader(writer, sample, code, yearWidth, year);
        writer.close();
        
        String expected = "1234562000";
        String actual = writer.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        
        tucson.saveData(sample, writer);
        writer.close();
        
        String expected = "";
        String actual = writer.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        
        Range expected = sample.range;
        Range actual = tucson.computeRange(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartBeforeYear1() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(0), new Year(2010));
        
        Range expected = new Range(new Year(1001), new Year(2010));
        Range actual = tucson.computeRange(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        
        Sample sample = new Sample();
        sample.data.put(new Year(2000).diff(new Year(2000)), "data1");
        
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucson.save(sample, writer);
        writer.close();
        
        String expected = "    data1";
        String actual = writer.toString();
        
        assertEquals(expected, actual);
    }
}
