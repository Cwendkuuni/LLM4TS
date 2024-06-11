import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private Sample sample;
    private BufferedReader reader;
    private BufferedWriter writer;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        sample = new Sample();
        reader = new BufferedReader(new StringReader(""));
        writer = new BufferedWriter(new StringWriter());
    }
    
    @Test
    public void testToString() {
        assertEquals("Format should be formatted correctly", "format.tucsonsimple", tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        assertEquals("Default extension should be .RWL", ".RWL", tucsonSimple.getDefaultExtension());
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCode_ExistingId() {
        sample.meta.put("id", "1234567");
        assertEquals("Code should be the same as existing id", "123456", tucsonSimple.make6digitCode(sample));
    }
    
    @Test
    public void testMake6digitCode_NoExistingId() {
        sample.meta.clear();
        assertEquals("Code should be 000000", "000000", tucsonSimple.make6digitCode(sample));
    }
    
    @Test
    public void testMake6digitCode_CodeLengthLessThan6() {
        sample.meta.put("id", "1");
        assertEquals("Code length should be 6", 6, tucsonSimple.make6digitCode(sample).length());
    }
    
    @Test
    public void testMake6digitCode_CodeLengthGreaterThan6() {
        sample.meta.put("id", "123456789");
        assertEquals("Code length should be 6", 6, tucsonSimple.make6digitCode(sample).length());
    }
    
    @Test
    public void testSaveRowHeader_YEqualToStart() throws IOException {
        Year y = new Year(2000);
        Sample s = new Sample(new Range(new Year(2000), new Year(2001)));
        tucsonSimple.saveRowHeader(writer, s, "123456", 6, y);
        assertEquals("Row header should start with code", "123456", writer.toString());
    }
    
    @Test
    public void testSaveRowHeader_YLessThanStart() throws IOException {
        Year y = new Year(1999);
        Sample s = new Sample(new Range(new Year(2000), new Year(2001)));
        tucsonSimple.saveRowHeader(writer, s, "123456", 6, y);
        assertEquals("Row header should start with start year", "2000", writer.toString().trim());
    }
    
    @Test
    public void testSaveRowHeader_YGreaterThanStart() throws IOException {
        Year y = new Year(2001);
        Sample s = new Sample(new Range(new Year(2000), new Year(2001)));
        tucsonSimple.saveRowHeader(writer, s, "123456", 6, y);
        assertEquals("Row header should start with given year", "2001", writer.toString().trim());
    }
    
    @Test
    public void testSaveData() throws IOException {
        Sample s = new Sample(new Range(new Year(2000), new Year(2002)));
        tucsonSimple.saveData(s, writer);
        String expectedOutput = "000000  2000" +
                                "001002  " +
                                "001003  " +
                                "   999" +
                                "9990  0" +
                                "000000  2001" +
                                "   999" +
                                "9990  0" +
                                "000000  2002";
        assertEquals("Data should be saved correctly", expectedOutput, writer.toString().trim());
    }

    @Test
    public void testComputeRange_StartGreaterThan1() {
        sample.range = new Range(new Year(2000), new Year(2001));
        Range result = tucsonSimple.computeRange(sample);
        assertEquals("Range should be the same as sample range", sample.range, result);
    }
    
    @Test
    public void testComputeRange_StartPlus8000GreaterThan1() {
        sample.range = new Range(new Year(-2000), new Year(2001));
        Range result = tucsonSimple.computeRange(sample);
        Range expectedRange = new Range(new Year(6001), new Year(1001));
        assertEquals("Range should be re-dated by 8000 years", expectedRange, result);
    }
    
    @Test
    public void testComputeRange_StartLessThan1() {
        sample.range = new Range(new Year(-2000), new Year(200));
        Range result = tucsonSimple.computeRange(sample);
        Range expectedRange = new Range(new Year(1001), new Year(200));
        assertEquals("Range should be re-dated start year to 1001", expectedRange, result);
    }
    
    @Test
    public void testSave() throws IOException {
        tucsonSimple.save(sample, writer);
        String expectedOutput = "        1" +
                                "   999";
        assertEquals("Data should be saved correctly", expectedOutput, writer.toString().trim());
    }
}