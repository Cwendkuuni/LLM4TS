import static org.junit.Assert.*;
import org.junit.Test;
import corina.formats.*;

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

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws Exception {
        TucsonSimple tucson = new TucsonSimple();
        tucson.load(null);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        assertEquals("123456", tucson.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2005));
        sample.meta.put("id", "123456");
        try {
            tucson.saveRowHeader(null, sample, tucson.make6digitCode(sample), 8, new Year(2003));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-1), new Year(5));
        Range range = tucson.computeRange(sample);
        assertEquals(new Year(1001), range.getStart());
        assertEquals(new Year(2005), range.getEnd());
    }

    @Test
    public void testSaveData() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2005));
        try {
            tucson.saveData(sample, null);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSave() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        try {
            tucson.save(sample, null);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}