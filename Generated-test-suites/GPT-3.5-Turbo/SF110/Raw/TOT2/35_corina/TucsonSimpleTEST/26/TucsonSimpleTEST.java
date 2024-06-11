import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.io.*;
import corina.*;

public class TucsonSimpleTest {
    
    // Test toString method
    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucson.toString());
    }
    
    // Test getDefaultExtension method
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals(".RWL", tucson.getDefaultExtension());
    }
    
    // Test load method for WrongFiletypeException
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        tucson.load(mockReader);
    }
    
    // Test make6digitCode method with different scenarios
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample1 = new Sample();
        sample1.meta.put("id", 123456789);
        assertEquals("123456", tucson.make6digitCode(sample1));
        
        Sample sample2 = new Sample();
        assertEquals("000000", tucson.make6digitCode(sample2));
    }
    
    // Test saveRowHeader method with specific parameters
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedWriter mockWriter = Mockito.mock(BufferedWriter.class);
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2021);
        
        tucson.saveRowHeader(mockWriter, sample, code, yearWidth, year);
        Mockito.verify(mockWriter).write("123456 2021");
    }
    
    // Test saveData method with specific parameters
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.put(0, 10);
        sample.data.put(1, 20);
        
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        
        tucson.saveData(sample, writer);
        
        String expectedOutput = "      10      20";
        assertEquals(expectedOutput, stringWriter.toString().trim());
    }
    
    // Test computeRange method with different scenarios
    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample1 = new Sample(new Range(new Year(1000), new Year(2000)));
        assertEquals(new Range(new Year(1001), new Year(2000)), tucson.computeRange(sample1));
        
        Sample sample2 = new Sample(new Range(new Year(-1000), new Year(1000)));
        assertEquals(new Range(new Year(1), new Year(8001)), tucson.computeRange(sample2));
        
        Sample sample3 = new Sample(new Range(new Year(5000), new Year(6000)));
        assertEquals(new Range(new Year(1001), new Year(6000)), tucson.computeRange(sample3));
    }
    
    // Test save method
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        
        tucson.save(sample, writer);
        
        // Check the output in writer if needed
    }
}