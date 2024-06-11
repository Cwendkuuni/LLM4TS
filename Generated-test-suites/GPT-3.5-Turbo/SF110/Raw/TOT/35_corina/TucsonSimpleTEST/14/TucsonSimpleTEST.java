import org.junit.*;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    // Test case for toString() method
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "Tucson Simple Format";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    // Test case for getDefaultExtension() method
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    // Test case for load() method with IOException
    @Test(expected = IOException.class)
    public void testLoadWithIOException() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }

    // Test case for make6digitCode() method with "id" present in the meta
    @Test
    public void testMake6digitCodeWithId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    // Test case for make6digitCode() method with "id" not present in the meta
    @Test
    public void testMake6digitCodeWithoutId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    // Test case for saveRowHeader() method where y is less than range start
    @Test
    public void testSaveRowHeaderWithYLessThanStart() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Year y = new Year(1999);
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        try {
            tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, y);
            bufferedWriter.flush();
            String expected = "123456 1999";
            String actual = writer.toString();
            assertEquals(expected, actual);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for saveRowHeader() method where y is greater than or equal to range start
    @Test
    public void testSaveRowHeaderWithYGreaterThanOrEqualStart() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Year y = new Year(2000);
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        try {
            tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, y);
            bufferedWriter.flush();
            String expected = "123456 2000";
            String actual = writer.toString();
            assertEquals(expected, actual);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for saveData() method
    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.put(0, "10");
        sample.data.put(1, "20");
        sample.data.put(2, "30");
        sample.count.put(0, 2);
        sample.count.put(1, 3);
        sample.count.put(2, 1);
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        try {
            tucsonSimple.saveData(sample, bufferedWriter);
            bufferedWriter.flush();
            String expected = "      10   220\n      20   310\n      30   001\n";
            String actual = writer.toString();
            assertEquals(expected, actual);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for computeRange() method where start is greater than or equal to 1
    @Test
    public void testComputeRangeWithStartGreaterThanOrEqualOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        Range expected = new Range(new Year(2000), new Year(2010));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    // Test case for computeRange() method where start.add(8000) is greater than or equal to 1
    @Test
    public void testComputeRangeWithStartAdd8000GreaterThanOrEqualOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1), new Year(2010));
        Range expected = new Range(new Year(1), new Year(8010));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    // Test case for computeRange() method where start.add(8000) is less than 1
    @Test
    public void testComputeRangeWithStartAdd8000LessThanOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1000), new Year(2010));
        Range expected = new Range(new Year(1001), new Year(2010));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    // Test case for save() method
    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        try {
            tucsonSimple.save(sample, bufferedWriter);
            bufferedWriter.flush();
            String expected = "   999";
            String actual = writer.toString();
            assertTrue(actual.contains(expected));
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

}