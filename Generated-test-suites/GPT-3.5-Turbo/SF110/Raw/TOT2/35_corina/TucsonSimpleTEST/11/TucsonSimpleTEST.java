import static org.junit.Assert.*;
import org.junit.Test;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucson.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals(".RWL", tucson.getDefaultExtension());
    }

    @Test
    public void testLoad() {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        try {
            tucson.load(reader);
            fail("Expected WrongFiletypeException");
        } catch (IOException e) {
            assertTrue(e instanceof WrongFiletypeException);
        }
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");
        assertEquals("123456", tucson.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        String code = "123456";
        Year year = new Year(2021);
        try {
            tucson.saveRowHeader(writer, sample, code, 6, year);
            assertEquals("1234562021", writer.toString());
        } catch (IOException e) {
            fail("IOException");
        }
    }

    @Test
    public void testSaveData() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        try {
            tucson.saveData(sample, writer);
            // Add assertions for expected output in writer
        } catch (IOException e) {
            fail("IOException");
        }
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        Year start = new Year(1000);
        sample.range.setStart(start);
        sample.range.setEnd(new Year(1050));
        Range expectedRange = new Range(start, new Year(1050));
        assertEquals(expectedRange, tucson.computeRange(sample));
    }

    @Test
    public void testSave() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        try {
            tucson.save(sample, writer);
            // Add assertions for expected output in writer
        } catch (IOException e) {
            fail("IOException");
        }
    }
}