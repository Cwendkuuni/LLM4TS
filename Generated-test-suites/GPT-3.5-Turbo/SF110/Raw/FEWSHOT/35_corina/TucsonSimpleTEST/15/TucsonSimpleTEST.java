package corina.formats;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

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
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCodeWithId() {
        sample.meta.put("id", "1234567");

        String code = tucsonSimple.make6digitCode(sample);

        assertEquals("123456", code);
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        String code = tucsonSimple.make6digitCode(sample);

        assertEquals("000000", code);
    }

    @Test
    public void testMake6digitCodeWithLongId() {
        sample.meta.put("id", "123456789");

        String code = tucsonSimple.make6digitCode(sample);

        assertEquals("123456", code);
    }

    @Test
    public void testSaveRowHeaderWithPrefixYearLessThanSampleStart() throws IOException {
        Year prefix = new Year(2000);
        sample.range = new Range(new Year(2010), new Year(2020));

        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, prefix);

        assertEquals("123456 2000", writer.toString());
    }

    @Test
    public void testSaveRowHeaderWithPrefixYearGreaterThanSampleStart() throws IOException {
        Year prefix = new Year(2015);
        sample.range = new Range(new Year(2010), new Year(2020));

        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, prefix);

        assertEquals("123456 2015", writer.toString());
    }

    @Test
    public void testSaveRowHeaderWithPrefixYearGreaterThanSampleStartAndIsProcessed() throws IOException {
        Year prefix = new Year(2015);
        sample.range = new Range(new Year(2010), new Year(2020));

        tucsonSimple.saveRowHeader(writer, sample, "123456", 4, prefix);

        assertEquals("1234 2015", writer.toString());
    }

    @Test
    public void testSaveData() throws IOException {
        Year start = new Year(2010);
        Year end = new Year(2015);
        sample.range = new Range(start, end);
        sample.data = new int[]{1, 2, 3, 4, 5};
        sample.count = new int[]{1, 1, 1, 1, 1};

        tucsonSimple.saveData(sample, writer);

        String expectedOutput = "000000 2010 2011 2012 2013 2014 2015" +
                                "000001 1    2    3    4    5    " +
                                "9990   ";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testComputeRangeWithStartGreaterThanOne() {
        sample.range = new Range(new Year(2010), new Year(2020));

        Range computedRange = tucsonSimple.computeRange(sample);

        assertEquals(new Range(new Year(2010), new Year(2020)), computedRange);
    }

    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanOne() {
        sample.range = new Range(new Year(8000), new Year(8010));

        Range computedRange = tucsonSimple.computeRange(sample);

        assertEquals(new Range(new Year(8000), new Year(8010)), computedRange);
    }

    @Test
    public void testComputeRangeWithStartPlus8000LessThanOne() {
        sample.range = new Range(new Year(999), new Year(1001));

        Range computedRange = tucsonSimple.computeRange(sample);

        assertEquals(new Range(new Year(1001), new Year(1001)), computedRange);
    }

    @Test
    public void testSave() throws IOException {
        tucsonSimple.save(sample, writer);

        String expectedOutput = "000000 999";
        assertEquals(expectedOutput, writer.toString());
    }
}