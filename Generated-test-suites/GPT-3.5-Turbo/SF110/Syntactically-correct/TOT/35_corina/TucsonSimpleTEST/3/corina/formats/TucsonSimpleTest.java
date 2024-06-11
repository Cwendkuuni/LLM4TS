import static org.junit.Assert.*;
import org.junit.Test;

public class TucsonSimpleTest {
    
    // Test for toString() method
    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = "Tucson Simple";
        assertEquals(expected, tucson.toString());
    }
    
    // Test for getDefaultExtension() method
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        String expected = ".RWL";
        assertEquals(expected, tucson.getDefaultExtension());
    }
    
    // Test for load() method with WrongFiletypeException
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader("Sample data"));
        tucson.load(reader);
    }
    
    // Test for make6digitCode() method when meta contains "id" key
    @Test
    public void testMake6digitCodeWithId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "12345678");
        String expected = "123456";
        assertEquals(expected, tucson.make6digitCode(sample));
    }
    
    // Test for make6digitCode() method when meta does not contain "id" key
    @Test
    public void testMake6digitCodeWithoutId() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        assertEquals(expected, tucson.make6digitCode(sample));
    }
    
    // Test for saveRowHeader() method when y is less than s.range.getStart()
    @Test
    public void testSaveRowHeaderWithYLessThanStart() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(2000));
        Year y = new Year(1999);
        String code = "123456";
        int yearWidth = 6;
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        try {
            tucson.saveRowHeader(writer, sample, code, yearWidth, y);
        } catch (IOException e) {
            fail("IOException should not occur");
        }
        assertEquals("123456 1999", writer.toString());
    }
    
    // Test for saveRowHeader() method when y is greater than or equal to s.range.getStart()
    @Test
    public void testSaveRowHeaderWithYGreaterThanOrEqualStart() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(2000));
        Year y = new Year(2001);
        String code = "123456";
        int yearWidth = 4;
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        try {
            tucson.saveRowHeader(writer, sample, code, yearWidth, y);
        } catch (IOException e) {
            fail("IOException should not occur");
        }
        assertEquals("123456 2001", writer.toString());
    }
    
    // Test for saveData() method
    @Test
    public void testSaveData() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.put(0, 10);
        sample.count.put(0, 5);
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2000));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        try {
            tucson.saveData(sample, writer);
        } catch (IOException e) {
            fail("IOException should not occur");
        }
        String expected = "         2000   10  5\n";
        assertEquals(expected, writer.toString());
    }
    
    // Test for computeRange() method when start year is greater than or equal to 1
    @Test
    public void testComputeRangeWithStartGreaterThanOrEqualOne() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2001));
        Range expectedRange = new Range(new Year(2000), new Year(2001));
        assertEquals(expectedRange, tucson.computeRange(sample));
    }
    
    // Test for computeRange() method when start year plus 8000 is greater than or equal to 1
    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanOrEqualOne() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(5000));
        sample.range.setEnd(new Year(6000));
        Range expectedRange = new Range(new Year(5000), new Year(6000));
        assertEquals(expectedRange, tucson.computeRange(sample));
    }
    
    // Test for computeRange() method when start year plus 8000 is less than 1
    @Test
    public void testComputeRangeWithStartPlus8000LessThanOne() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(-1000));
        sample.range.setEnd(new Year(1));
        Range expectedRange = new Range(new Year(1001), new Year(1));
        assertEquals(expectedRange, tucson.computeRange(sample));
    }
    
    // Test for save() method
    @Test
    public void testSave() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.put(0, 10);
        sample.count.put(0, 5);
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2000));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        try {
            tucson.save(sample, writer);
        } catch (IOException e) {
            fail("IOException should not occur");
        }
        String expected = "         2000   10  5\n";
        assertEquals(expected, writer.toString());
    }
}