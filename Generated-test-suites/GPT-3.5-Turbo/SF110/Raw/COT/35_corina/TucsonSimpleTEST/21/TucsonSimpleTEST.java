
import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    // Test toString() method
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    // Test getDefaultExtension() method
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    // Test load() method when WrongFiletypeException is thrown
    @Test(expected = WrongFiletypeException.class)
    public void testLoadWithWrongFiletypeException() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new StringReader(""));
        tucsonSimple.load(r);
    }

    // Test make6digitCode() method when meta contains "id" key
    @Test
    public void testMake6digitCodeWithMetaContainingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    // Test make6digitCode() method when meta does not contain "id" key
    @Test
    public void testMake6digitCodeWithoutMetaContainingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    // Test saveRowHeader() method when y is less than s.range.getStart()
    @Test
    public void testSaveRowHeaderWithYLessThanStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(10), new Year(20));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        String code = "123456";

        tucsonSimple.saveRowHeader(w, sample, code, 6, new Year(5));

        w.close();

        String expected = "123456   5";
        String actual = w.toString().trim();
        assertEquals(expected, actual);
    }

    // Test saveRowHeader() method when y is greater than or equal to s.range.getStart()
    @Test
    public void testSaveRowHeaderWithYGreaterThanOrEqualStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(10), new Year(20));
        BufferedWriter w = new BufferedWriter(new StringWriter());
        String code = "123456";

        tucsonSimple.saveRowHeader(w, sample, code, 6, new Year(15));

        w.close();

        String expected = "123456  15";
        String actual = w.toString().trim();
        assertEquals(expected, actual);
    }

    // Test saveData() method
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(10), new Year(20));
        BufferedWriter w = new BufferedWriter(new StringWriter());

        tucsonSimple.saveData(sample, w);

        w.close();

        String expected = "";
        String actual = w.toString().trim();
        assertEquals(expected, actual);
    }

    // Test computeRange() method when start is greater than or equal to Year(1)
    @Test
    public void testComputeRangeWithStartGreaterThanOrEqualOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(10), new Year(20));

        Range expected = new Range(new Year(10), new Year(20));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    // Test computeRange() method when start.add(8000) is greater than or equal to Year(1)
    @Test
    public void testComputeRangeWithStartAdd8000GreaterThanOrEqualOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-8000), new Year(-7990));

        Range expected = new Range(new Year(1), new Year(11));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    // Test computeRange() method when start.add(8000) is less than Year(1)
    @Test
    public void testComputeRangeWithStartAdd8000LessThanOne() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-7999), new Year(-7989));

        Range expected = new Range(new Year(1001), new Year(1011));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    // Test save() method
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter w = new BufferedWriter(new StringWriter());

        tucsonSimple.save(sample, w);

        w.close();

        String expected = "";
        String actual = w.toString().trim();
        assertEquals(expected, actual);
    }

}

