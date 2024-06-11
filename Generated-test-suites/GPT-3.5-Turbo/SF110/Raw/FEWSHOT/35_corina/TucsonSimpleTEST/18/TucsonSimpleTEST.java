package corina.formats;

import static org.junit.Assert.*;
import org.junit.Test;

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
        BufferedReader reader = new BufferedReader(new FileReader("testFile.txt"));
        Sample sample = tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample sampleWithId = new Sample();
        sampleWithId.meta.put("id", "1234567890");
        assertEquals("123456", tucsonSimple.make6digitCode(sampleWithId));
        
        Sample sampleWithoutId = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sampleWithoutId));
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("testFile.txt"));
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2022);
        
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        writer.close();
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("testFile.txt"));
        Sample sample = new Sample();
        
        tucsonSimple.saveData(sample, writer);
        
        writer.close();
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        
        Sample sampleWithStartYearGreaterThanOrEqualTo1 = new Sample();
        sampleWithStartYearGreaterThanOrEqualTo1.range = new Range(new Year(2022), new Year(2023));
        assertEquals(sampleWithStartYearGreaterThanOrEqualTo1.range, tucsonSimple.computeRange(sampleWithStartYearGreaterThanOrEqualTo1));
        
        Sample sampleWithStartYearGreaterThanOrEqualTo8000 = new Sample();
        sampleWithStartYearGreaterThanOrEqualTo8000.range = new Range(new Year(7999), new Year(8000));
        assertEquals(sampleWithStartYearGreaterThanOrEqualTo8000.range.redateBy(8000), tucsonSimple.computeRange(sampleWithStartYearGreaterThanOrEqualTo8000));
        
        Sample sampleWithStartYearLessThan1 = new Sample();
        sampleWithStartYearLessThan1.range = new Range(new Year(1000), new Year(1001));
        assertEquals(sampleWithStartYearLessThan1.range.redateStartTo(new Year(1001)), tucsonSimple.computeRange(sampleWithStartYearLessThan1));
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("testFile.txt"));
        Sample sample = new Sample();
        
        tucsonSimple.save(sample, writer);
        
        writer.close();
    }
}