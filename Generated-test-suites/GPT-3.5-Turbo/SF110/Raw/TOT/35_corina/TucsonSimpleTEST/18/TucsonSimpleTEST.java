package corina.formats;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
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
        BufferedReader reader = new BufferedReader(new StringReader(""));

        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCodeWithExistingId() {
        Sample sample = new Sample();
        sample.addMeta("id", "1234567890");

        String expected = "123456";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        Sample sample = new Sample();

        String expected = "000000";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testMake6digitCodeWithLongId() {
        Sample sample = new Sample();
        sample.addMeta("id", "1234567890abcdef");

        String expected = "123456";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testSaveRowHeaderWithYearBeforeStart() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(2010), new Year(2013)));

        BufferedWriter writer = new BufferedWriter(new FileWriter("file.txt"));

        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2009);

        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);

        writer.close();
    }

    @Test
    public void testSaveRowHeaderWithYearAfterStart() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(2010), new Year(2013)));

        BufferedWriter writer = new BufferedWriter(new FileWriter("file.txt"));

        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2011);

        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);

        writer.close();
    }

    @Test
    public void testSaveData() {
        Sample sample = new Sample();

        BufferedWriter writer = new BufferedWriter(new FileWriter("file.txt"));

        tucsonSimple.saveData(sample, writer);

        writer.close();
    }

    @Test
    public void testComputeRangeWithStartAfterYearOne() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(2010), new Year(2013)));

        Range expected = sample.getRange();
        Range result = tucsonSimple.computeRange(sample);

        assertEquals(expected, result);
    }

    @Test
    public void testComputeRangeWithStartBeforeYearOne() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(100), new Year(2013)));

        Range expected = sample.getRange().redateStartTo(new Year(1001));
        Range result = tucsonSimple.computeRange(sample);

        assertEquals(expected, result);
    }

    @Test
    public void testSave() {
        Sample sample = new Sample();

        BufferedWriter writer = new BufferedWriter(new FileWriter("file.txt"));

        tucsonSimple.save(sample, writer);

        writer.close();
    }

}