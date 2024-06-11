import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "Tucson Simple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "12345");
        String expected = "12345 ";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWithNoId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2005));
        Year y = new Year(2002);
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, y);
        String expected = "123456 2002";
        String actual = writer.toString().trim();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2005));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(sample, writer);
        String expected = "000000 2000";
        String actual = writer.toString().split("\n")[0].trim();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1000), new Year(1005));
        Range expected = new Range(new Year(1000), new Year(1005));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartGreaterThanOrEqual() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1001), new Year(1005));
        Range expected = new Range(new Year(1001), new Year(1005));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanOrEqual() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-8000), new Year(-7995));
        Range expected = new Range(new Year(1), new Year(5));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRangeWithStartLessThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-9999), new Year(-9995));
        Range expected = new Range(new Year(1001), new Year(1005));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.save(sample, writer);
        // Check the contents of the writer
    }
}