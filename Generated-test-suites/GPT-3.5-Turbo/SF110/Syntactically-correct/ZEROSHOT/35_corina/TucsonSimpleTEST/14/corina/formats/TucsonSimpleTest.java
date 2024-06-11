import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    // Test the toString method
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    // Test the getDefaultExtension method
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    // Test the load method with WrongFiletypeException
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        tucsonSimple.load(reader);
    }

    // Test the make6digitCode method with metaDataContainingId
    @Test
    public void testMake6digitCodeWithMetaDataContainingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
    }

    // Test the make6digitCode method without metaDataContainingId
    @Test
    public void testMake6digitCodeWithoutMetaDataContainingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
    }

    // Test the saveRowHeader method with yLessThanSampleStartDate
    @Test
    public void testSaveRowHeaderWithYLessThanSampleStartDate() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.saveRowHeader(writer, sample, "123456", 4, new Year(2000));
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        assertEquals("1234 2000", reader.readLine());
        reader.close();
    }

    // Test the saveRowHeader method with yGreaterThanSampleStartDate
    @Test
    public void testSaveRowHeaderWithYGreaterThanSampleStartDate() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(2000));
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        assertEquals("123456", reader.readLine());
        reader.close();
    }

    // Test the saveData method
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.saveData(sample, writer);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        assertEquals("   999", reader.readLine());
        reader.close();
    }

    // Test the computeRange method with startGreaterThanOrEqualToYearOne
    @Test
    public void testComputeRangeWithStartGreaterThanOrEqualToYearOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        assertEquals(sample.range, tucsonSimple.computeRange(sample));
    }

    // Test the computeRange method with startPlus8000GreaterThanOrEqualToYearOne
    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanOrEqualToYearOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-8000), new Year(-7980));
        assertEquals(sample.range.redateBy(8000), tucsonSimple.computeRange(sample));
    }

    // Test the computeRange method with redateStartToYearOne
    @Test
    public void testComputeRangeWithRedateStartToYearOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(0), new Year(1000));
        assertEquals(sample.range.redateStartTo(new Year(1001)), tucsonSimple.computeRange(sample));
    }

    // Test the save method
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.save(sample, writer);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("test.rwl"));
        assertEquals("   999", reader.readLine());
        reader.close();
    }
}