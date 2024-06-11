package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple t = new TucsonSimple();
        assertEquals("format.tucsonsimple", t.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple t = new TucsonSimple();
        assertEquals(".RWL", t.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple t = new TucsonSimple();
        BufferedReader r = null;
        t.load(r);
    }

    @Test
    public void testMake6digitCode_withValidSample() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.meta.put("id", "12345678");
        String code = t.make6digitCode(s);
        assertEquals("123456", code);
    }

    @Test
    public void testMake6digitCode_withEmptyId() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        String code = t.make6digitCode(s);
        assertEquals("000000", code);
    }

    @Test
    public void testMake6digitCode_withIdLongerThan6Digits() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.meta.put("id", "123456789");
        String code = t.make6digitCode(s);
        assertEquals("123456", code);
    }

    @Test
    public void testSaveRowHeader_withYearBeforeSampleRangeStart() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(2000), new Year(2010));
        BufferedWriter w = null;
        String code = "000000";
        int yearWidth = 6;
        Year y = new Year(1999);
        IOException exception = assertThrows(IOException.class, () -> {
            t.saveRowHeader(w, s, code, yearWidth, y);
        });
        assertEquals("IOException: Range start year is later than row header year", exception.getMessage());
    }

    @Test
    public void testSaveRowHeader_withYearEqualToSampleRangeStart() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(2000), new Year(2010));
        BufferedWriter w = null;
        String code = "000000";
        int yearWidth = 6;
        Year y = new Year(2000);
        IOException exception = assertThrows(IOException.class, () -> {
            t.saveRowHeader(w, s, code, yearWidth, y);
        });
        assertEquals("IOException: Range start year is later than row header year", exception.getMessage());
    }

    @Test
    public void testSaveRowHeader_withYearAfterSampleRangeStart() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(2000), new Year(2010));
        BufferedWriter w = null;
        String code = "000000";
        int yearWidth = 6;
        Year y = new Year(2001);
        t.saveRowHeader(w, s, code, yearWidth, y);
    }

    @Test
    public void testSaveRowHeader_withYearBeforeSampleRangeStart_whenIsProcessed() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(2000), new Year(2010));
        BufferedWriter w = null;
        String code = "0000";
        int yearWidth = 4;
        Year y = new Year(1999);
        t.saveRowHeader(w, s, code, yearWidth, y);
    }

    @Test
    public void testSaveRowHeader_withYearEqualToSampleRangeStart_whenIsProcessed() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(2000), new Year(2010));
        BufferedWriter w = null;
        String code = "0000";
        int yearWidth = 4;
        Year y = new Year(2000);
        IOException exception = assertThrows(IOException.class, () -> {
            t.saveRowHeader(w, s, code, yearWidth, y);
        });
        assertEquals("IOException: Range start year is later than row header year", exception.getMessage());
    }

    @Test
    public void testSaveRowHeader_withYearAfterSampleRangeStart_whenIsProcessed() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(2000), new Year(2010));
        BufferedWriter w = null;
        String code = "0000";
        int yearWidth = 4;
        Year y = new Year(2001);
        t.saveRowHeader(w, s, code, yearWidth, y);
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter w = null;
        t.saveData(s, w);
        // add assertions
    }

    @Test
    public void testComputeRange_withStartYearAfterYear1() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(2000), new Year(2010));
        Range computedRange = t.computeRange(s);
        assertEquals(s.range, computedRange);
    }

    @Test
    public void testComputeRange_withStartYearBeforeYear1AndAdd8000_isAfterYear1() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(-5000), new Year(2010));
        Range computedRange = t.computeRange(s);
        Range expectedRange = new Range(new Year(3000), new Year(10010));
        assertEquals(expectedRange, computedRange);
    }

    @Test
    public void testComputeRange_withStartYearBeforeYear1AndAdd8000_notAfterYear1() {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(-5000), new Year(1000));
        Range computedRange = t.computeRange(s);
        Range expectedRange = new Range(new Year(1001), new Year(10010));
        assertEquals(expectedRange, computedRange);
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple t = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter w = null;
        t.save(s, w);
        // add assertions
    }

}