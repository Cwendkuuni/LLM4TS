import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    @Test
    public void toString_ReturnsCorrectText() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expectedText = "Tucson Simple";
        
        String result = tucsonSimple.toString();
        
        assertEquals(expectedText, result);
    }
    
    @Test
    public void getDefaultExtension_ReturnsDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expectedExtension = ".RWL";
        
        String result = tucsonSimple.getDefaultExtension();
        
        assertEquals(expectedExtension, result);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void load_ThrowsWrongFiletypeException() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        
        tucsonSimple.load(reader);
    }
    
    @Test
    public void make6digitCode_ReturnsValidCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "12345");
        String expectedCode = "12345 ";
        
        String result = tucsonSimple.make6digitCode(sample);
        
        assertEquals(expectedCode, result);
    }
    
    @Test
    public void make6digitCode_ReturnsDefaultCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expectedCode = "000000";
        
        String result = tucsonSimple.make6digitCode(sample);
        
        assertEquals(expectedCode, result);
    }
    
    @Test
    public void saveRowHeader_WritesHeaderToBuffer() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String expectedHeader = "000000 2000";
        
        tucsonSimple.saveRowHeader(writer, sample, "000000", 6, new Year(2000));
        writer.flush();
        String result = writer.toString().trim();
        
        assertEquals(expectedHeader, result);
    }
    
    // Additional test cases for saveRowHeader can be added to cover different scenarios
    
    @Test
    public void saveData_WritesDataToBuffer() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2002));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String expectedData = "   1  2  3";
        
        tucsonSimple.saveData(sample, writer);
        writer.flush();
        String result = writer.toString().trim();
        
        assertEquals(expectedData, result);
    }
    
    // Additional test cases for saveData can be added to cover different scenarios
    
    @Test
    public void computeRange_ReturnsValidRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(100), new Year(200));
        Range expectedRange = new Range(new Year(100), new Year(200));
        
        Range result = tucsonSimple.computeRange(sample);
        
        assertEquals(expectedRange, result);
    }
    
    // Additional test cases for computeRange can be added to cover different scenarios
    
    @Test
    public void save_CallsSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        
        tucsonSimple.save(sample, writer);
        
        // Verify if saveData has been called or not
    }
}