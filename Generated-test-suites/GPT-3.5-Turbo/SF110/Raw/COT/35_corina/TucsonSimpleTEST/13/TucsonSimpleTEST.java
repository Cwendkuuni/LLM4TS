
package corina.formats;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }

    @Test
    public void testToString() {
        String expected = "format.tucsonsimple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultExtension() {
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCodeWithId() {
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeaderWithStartYearBeforeSampleRangeStart() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(1990);
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        String expected = "123456 1990";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeaderWithStartYearAfterSampleRangeStart() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 4;
        Year year = new Year(1995);
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        String expected = "1234 1995";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveData() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        tucsonSimple.saveData(sample, writer);
        String expected = "   999";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRangeWithStartYearGreaterThanOrEqualToOne() {
        Sample sample = new Sample();
        Year start = new Year(1);
        Range range = new Range(start, start);
        sample.range = range;
        Range expected = range;
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRangeWithStartYearAdd8000GreaterThanOrEqualToOne() {
        Sample sample = new Sample();
        Year start = new Year(1);
        Year rangeStart = new Year(8001);
        Range range = new Range(rangeStart, rangeStart);
        sample.range = new Range(start, rangeStart.add(-1));
        Range expected = range;
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRangeWithStartYearAdd8000LessThanToOne() {
        Sample sample = new Sample();
        Year rangeStart = new Year(1001);
        Range range = new Range(rangeStart, rangeStart);
        sample.range = new Range(new Year(500), new Year(8000));
        Range expected = range;
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSave() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        tucsonSimple.save(sample, writer);
        String expected = "   999\n";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }
}

