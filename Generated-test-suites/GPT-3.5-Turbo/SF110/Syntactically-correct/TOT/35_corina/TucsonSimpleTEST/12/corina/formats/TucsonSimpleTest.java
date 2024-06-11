import org.junit.After;
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
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws Exception {
        tucsonSimple.load(null);
    }

    @Test
    public void testMake6digitCodeWithValidMetaKey() {
        Sample sample = new Sample();
        sample.meta.put("id", "12345");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }

    @Test
    public void testMake6digitCodeWithMissingMetaKey() {
        Sample sample = new Sample();
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }

    @Test
    public void testMake6digitCodeWithLongMetaKey() {
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }

    @Test
    public void testSaveRowHeaderWithLessThanYearWidth() {
        Sample sample = new Sample();
        Year year = new Year(2000);
        try {
            tucsonSimple.saveRowHeader(null, sample, "123456", 10, year);
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testSaveRowHeaderWithEqualYearWidth() {
        Sample sample = new Sample();
        Year year = new Year(2000);
        try {
            tucsonSimple.saveRowHeader(null, sample, "123456", 6, year);
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testSaveRowHeaderWithGreaterThanYearWidth() {
        Sample sample = new Sample();
        Year year = new Year(2000);
        try {
            tucsonSimple.saveRowHeader(null, sample, "1234567", 6, year);
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testSaveData() {
        Sample sample = new Sample();
        BufferedWriter writer = null;
        try {
            tucsonSimple.saveData(sample, writer);
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testComputeRangeWithStartGreaterThanEqualToYear1() {
        Sample sample = new Sample();
        Year start = new Year(2000);
        sample.range = new Range(start, start.add(10));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(sample.range, range);
    }

    @Test
    public void testComputeRangeWithStartPlus8000GreaterThanEqualToYear1() {
        Sample sample = new Sample();
        Year start = new Year(1);
        sample.range = new Range(start, start.add(10));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(sample.range.redateBy(8000), range);
    }

    @Test
    public void testComputeRangeWithStartLessThanYear1() {
        Sample sample = new Sample();
        Year start = new Year(100);
        sample.range = new Range(start, start.add(10));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(1001), start.add(10)), range);
    }

    @Test
    public void testSave() {
        Sample sample = new Sample();
        BufferedWriter writer = null;
        try {
            tucsonSimple.save(sample, writer);
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }

    @After
    public void tearDown() {
        tucsonSimple = null;
    }
}