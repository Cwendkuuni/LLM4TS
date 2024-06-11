import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.Range;
import corina.util.Year;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Sample sample;

    private class Sample implements Filetype.Sample {
        public HashMap meta;
        public Range range;
        public HashMap data;
        public HashMap count;

        public Sample() {
            meta = new HashMap();
            range = new Range(new Year(1), new Year(10));
            data = new HashMap();
            count = new HashMap();
        }
    }

    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
        bufferedReader = null;
        bufferedWriter = null;
        sample = new Sample();
    }

    @Test
    public void testToString() {
        String expected = "Tucson Simple";
        String result = tucsonSimple.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetDefaultExtension() {
        String expected = ".RWL";
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(expected, result);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        bufferedReader = new BufferedReader(new FileReader("testFile.txt"));
        tucsonSimple.load(bufferedReader);
    }

    @Test
    public void testMake6digitCode_MetaHasId() {
        sample.meta.put("id", "12345678");
        String expected = "123456";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testMake6digitCode_MetaDoesNotHaveId() {
        sample.meta.remove("id");
        String expected = "000000";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testMake6digitCode_CodeLengthGreaterThan6() {
        sample.meta.put("id", "1234567890");
        String expected = "123456";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testMake6digitCode_CodeLengthLessThan6() {
        sample.meta.put("id", "1234");
        String expected = "1234  ";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testSaveRowHeader_StartYearLessThanSampleStartRange() throws IOException {
        String expected = "0001  ";
        bufferedWriter = new BufferedWriter(new FileWriter("testFile.txt"));
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "000000", 6, new Year(1));
        bufferedWriter.close();
        bufferedReader = new BufferedReader(new FileReader("testFile.txt"));
        String result = bufferedReader.readLine();
        bufferedReader.close();
        assertEquals(expected, result);
    }

    @Test
    public void testSaveRowHeader_StartYearGreaterThanOrEqualSampleStartRange() throws IOException {
        String expected = "0001  ";
        bufferedWriter = new BufferedWriter(new FileWriter("testFile.txt"));
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "000000", 6, new Year(2));
        bufferedWriter.close();
        bufferedReader = new BufferedReader(new FileReader("testFile.txt"));
        String result = bufferedReader.readLine();
        bufferedReader.close();
        assertEquals(expected, result);
    }

    @Test
    public void testSaveRowHeader_StartYearGreaterThanSampleStartRange_And_IsProcessed() throws IOException {
        String expected = " 0002";
        bufferedWriter = new BufferedWriter(new FileWriter("testFile.txt"));
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "000000", 4, new Year(2));
        bufferedWriter.close();
        bufferedReader = new BufferedReader(new FileReader("testFile.txt"));
        String result = bufferedReader.readLine();
        bufferedReader.close();
        assertEquals(expected, result);
    }

    @Test
    public void testSaveRowHeader_StartYearLessThanSampleStartRange_And_IsProcessed() throws IOException {
        String expected = "0001  ";
        bufferedWriter = new BufferedWriter(new FileWriter("testFile.txt"));
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "000000", 4, new Year(1));
        bufferedWriter.close();
        bufferedReader = new BufferedReader(new FileReader("testFile.txt"));
        String result = bufferedReader.readLine();
        bufferedReader.close();
        assertEquals(expected, result);
    }

    @Test
    public void testSaveData() throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter("testFile.txt"));
        tucsonSimple.saveData(sample, bufferedWriter);
        bufferedWriter.close();
        bufferedReader = new BufferedReader(new FileReader("testFile.txt"));
        String result = bufferedReader.readLine();
        bufferedReader.close();
        assertNotNull(result);
    }

    @Test
    public void testComputeRange_StartIsGreaterThanEqualToYearOne() {
        Range expected = new Range(new Year(1), new Year(10));
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRange_StartPlus8000IsGreaterThanEqualToYearOne() {
        sample.range = new Range(new Year(8000), new Year(8010));
        Range expected = new Range(new Year(8000), new Year(8010));
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRange_StartPlus8000IsLessThanYearOne() {
        sample.range = new Range(new Year(8000), new Year(8010));
        Range expected = new Range(new Year(1001), new Year(1011));
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testSave() throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter("testFile.txt"));
        tucsonSimple.save(sample, bufferedWriter);
        bufferedWriter.close();
        bufferedReader = new BufferedReader(new FileReader("testFile.txt"));
        String result = bufferedReader.readLine();
        bufferedReader.close();
        assertNotNull(result);
    }
}