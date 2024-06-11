import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import corina.formats.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "TucsonSimple";
        assertEquals(expected, tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        assertEquals(expected, tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCodeWithId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeaderWithStartYearBeforeSampleRange() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String expected = "   2000";
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(1998));
        writer.flush();
        String actual = writer.toString().split("\\s+")[0]; // Get the first word
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeaderWithStartYearAfterSampleRange() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String expected = "   2020";
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(2022));
        writer.flush();
        String actual = writer.toString().split("\\s+")[0]; // Get the first word
        assertEquals(expected, actual);
    }

    // Write more test cases for saveRowHeader method
    
    @Test
    public void testComputeRangeWithStartYearGreaterThanEqualToOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Range expectedRange = new Range(new Year(2000), new Year(2020));
        Range actualRange = tucsonSimple.computeRange(sample);
        assertEquals(expectedRange, actualRange);
    }

    @Test
    public void testComputeRangeWithStartYearPlus8000GreaterThanEqualToOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-7999), new Year(2020));
        Range expectedRange = new Range(new Year(1), new Year(10019));
        Range actualRange = tucsonSimple.computeRange(sample);
        assertEquals(expectedRange, actualRange);
    }

    @Test
    public void testComputeRangeWithStartYearLessThanOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-9000), new Year(2020));
        Range expectedRange = new Range(new Year(1001), new Year(2020));
        Range actualRange = tucsonSimple.computeRange(sample);
        assertEquals(expectedRange, actualRange);
    }

    // Write more test cases for computeRange method
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String expected = "123456        ";
        tucsonSimple.saveData(sample, writer);
        writer.flush();
        String actual = writer.toString().split("\\s+")[0]; // Get the first word
        assertEquals(expected, actual);
    }

    // Write more test cases for saveData method
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.save(sample, writer);
        // Add assertion if required
    }

}