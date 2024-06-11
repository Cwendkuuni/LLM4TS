import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import corina.formats.*;
import corina.ui.*;
import corina.*;
import java.util.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "Tucson Simple";
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
        BufferedReader r = new BufferedReader(new StringReader(""));
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCodeWithExistingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        HashMap<String, Object> meta = new HashMap<>();
        meta.put("id", "1234567");
        sample.meta = meta;
        String code = tucsonSimple.make6digitCode(sample);
        String expected = "123456";
        assertEquals(expected, code);
    }
    
    @Test
    public void testMake6digitCodeWithoutExistingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String code = tucsonSimple.make6digitCode(sample);
        String expected = "000000";
        assertEquals(expected, code);
    }
    
    @Test
    public void testSaveRowHeaderWhenStartYearBeforeGivenYear() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1992));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1989);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        String expected = "123456          1989";
        assertEquals(expected, w.toString());
    }
    
    @Test
    public void testSaveRowHeaderWhenStartYearEqualsGivenYear() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1992));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        String code = "123456";
        int yearWidth = 4;
        Year y = new Year(1990);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        String expected = "1234    1990";
        assertEquals(expected, w.toString());
    }
    
    @Test
    public void testSaveRowHeaderWhenStartYearAfterGivenYear() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1992));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1991);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        String expected = "123456    1991";
        assertEquals(expected, w.toString());
    }
    
    @Test
    public void testSaveDataWhenRangeEndBeforeStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1995), new Year(1993));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(sample, w);
        String expected = "";
        assertEquals(expected, w.toString());
    }
    
    @Test
    public void testSaveDataWithoutProcessedAndSummed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1992));
        sample.data = new ArrayList<>(Arrays.asList(10, 20, 30));
        sample.count = new ArrayList<>(Arrays.asList(100, 200, 300));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(sample, w);
        String expected = "000000    1990  10   100\n";
        expected += "000000    1991  20   200\n";
        expected += "000000    1992  30   300\n";
        expected += "   999";
        assertEquals(expected, w.toString());
    }
    
    @Test
    public void testSaveDataWithProcessed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1996));
        sample.data = new ArrayList<>(Arrays.asList(10, 20, 30));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(sample, w);
        String expected = "000000   1990  10   \n";
        expected += "000000   1991  20   \n";
        expected += "000000   1992  30   \n";
        expected += "   999";
        assertEquals(expected, w.toString());
    }
    
    @Test
    public void testSaveDataWithProcessedAndSummed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1992));
        sample.data = new ArrayList<>(Arrays.asList(10, 20, 30));
        sample.count = new ArrayList<>(Arrays.asList(100, 200, 300));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(sample, w);
        String expected = "000000    1990  10   100\n";
        expected += "000000    1991  20   200\n";
        expected += "000000    1992  30   300\n";
        expected += "9990   ";
        assertEquals(expected, w.toString());
    }
    
    @Test
    public void testComputeRangeWhenStartYearGreaterThanOrEqualToOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1992));
        Range range = tucsonSimple.computeRange(sample);
        Range expected = new Range(new Year(1990), new Year(1992));
        assertEquals(expected, range);
    }
    
    @Test
    public void testComputeRangeWhenStartYearPlus8000GreaterThanOrEqualToOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-8000), new Year(-7998));
        Range range = tucsonSimple.computeRange(sample);
        Range expected = new Range(new Year(-8000), new Year(-7998));
        assertEquals(expected, range);
    }
    
    @Test
    public void testComputeRangeWhenStartYearPlus8000LessThanOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-8005), new Year(-8003));
        Range range = tucsonSimple.computeRange(sample);
        Range expected = new Range(sample.range.getStart(), new Year(1001));
        assertEquals(expected, range);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1992));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        tucsonSimple.save(sample, w);
        String expected = "000000    1990  \n";
        expected += "000000    1991  \n";
        expected += "000000    1992  \n";
        expected += "   999\n";
        assertEquals(expected, w.toString());
    }
}