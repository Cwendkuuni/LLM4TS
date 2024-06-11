
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

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
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
    }

    // Test edge cases and exceptions for saveRowHeader() method
    @Test
    public void testSaveRowHeader_startBeforeSampleRange() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(1995));
        assertEquals("123456 1995", writer.toString().trim());
    }

    @Test
    public void testSaveRowHeader_startAfterSampleRange() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        tucsonSimple.saveRowHeader(writer, sample, "123456", 4, new Year(2015));
        assertEquals("123456   2015", writer.toString().trim());
    }

    // Test edge cases and exceptions for saveData() method
    @Test
    public void testSaveData_isProcessed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.data.put(0, 10.0);
        sample.data.put(1, 20.0);
        sample.count.put(0, 5);
        sample.count.put(1, 7);
        tucsonSimple.saveData(sample, writer);
        assertEquals("123456     10  520", writer.toString().trim());
    }

    @Test
    public void testSaveData_isSummed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.data.put(0, 10.0);
        sample.count.put(0, 5);
        tucsonSimple.saveData(sample, writer);
        assertEquals("123456 10    5", writer.toString().trim());
    }

    // Test computeRange() method
    @Test
    public void testComputeRange_startGreaterThanOrEqualToOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(2000), range.getStart());
        assertEquals(new Year(2010), range.getEnd());
    }

    @Test
    public void testComputeRange_startPlus8000GreaterThanOrEqualToOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-2000), new Year(2010));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(6000), range.getStart());
        assertEquals(new Year(8010), range.getEnd());
    }

    @Test
    public void testComputeRange_startLessThanOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-2000), new Year(-1990));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(1001), range.getStart());
        assertEquals(new Year(-1990), range.getEnd());
    }

}
