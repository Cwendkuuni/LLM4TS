import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    // Test case for toString method
    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucson.toString());
    }

    // Test case for getDefaultExtension method
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals(".RWL", tucson.getDefaultExtension());
    }

    // Test case for load method throwing WrongFiletypeException
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        tucson.load(null);
    }

    // Test case for make6digitCode method with code length greater than 6
    @Test
    public void testMake6digitCodeWithGreaterLength() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        String code = tucson.make6digitCode(sample);
        assertEquals("123456", code);
    }

    // Test case for make6digitCode method with code length less than 6
    @Test
    public void testMake6digitCodeWithLessLength() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        String code = tucson.make6digitCode(sample);
        assertEquals("000000", code);
    }

    // Test case for computeRange method
    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1), new Year(10));
        Range range = tucson.computeRange(sample);
        assertEquals(new Year(1001), range.getStart());
    }

    // Test case for save method
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new FileWriter("test.txt"));
        tucson.save(sample, writer);
        // assert some conditions on the output file
    }
}