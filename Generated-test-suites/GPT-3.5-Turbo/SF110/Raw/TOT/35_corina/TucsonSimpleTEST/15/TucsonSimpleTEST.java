import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = "Tucson Simple";
        assertEquals(expected, tucson.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = ".RWL";
        assertEquals(expected, tucson.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new FileReader("invalidfile.txt"));
        tucson.load(reader);
    }
    
    @Test
    public void testMake6digitCodeWithExistingId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        String expected = "123456";
        assertEquals(expected, tucson.make6digitCode(sample));
    }
    
    @Test
    public void testMake6digitCodeWithoutExistingId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        assertEquals(expected, tucson.make6digitCode(sample));
    }
    
    @Test
    public void testMake6digitCodeWithLongExistingId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        String expected = "123456";
        assertEquals(expected, tucson.make6digitCode(sample));
    }
    
    @Test
    public void testSaveRowHeaderWithStartBeforeRangeStart() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedReader reader = new BufferedReader(new FileReader("validfile.txt"));
        sample.range = new Range(new Year(2000), new Year(2020));
        String code = "000001";
        int yearWidth = 6;
        Year year = new Year(1999);
        
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        
        tucson.saveRowHeader(writer, sample, code, yearWidth, year);
        
        String expected = " 2000" + code;
        assertEquals(expected, stringWriter.toString());
    }
    
    // Include more test cases for each method
    
}