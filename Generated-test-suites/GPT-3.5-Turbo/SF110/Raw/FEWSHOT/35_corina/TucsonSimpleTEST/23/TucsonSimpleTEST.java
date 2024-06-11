
package corina.formats;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.Sample;
import corina.util.Range;
import corina.util.Year;

public class TucsonSimpleTest {

    private TucsonSimple tucson;

    @Before
    public void setUp() {
        tucson = new TucsonSimple();
    }

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucson.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucson.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        tucson.load(null);
    }

    @Test
    public void testMake6digitCode_withExistingId() {
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "12345678");
        sample.setMeta(meta);

        String code = tucson.make6digitCode(sample);

        assertEquals("123456", code);
    }

    @Test
    public void testMake6digitCode_withoutExistingId() {
        Sample sample = new Sample();
        String code = tucson.make6digitCode(sample);

        assertEquals("000000", code);
    }

    @Test
    public void testMake6digitCode_withLongerExistingId() {
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "1234567890");
        sample.setMeta(meta);

        String code = tucson.make6digitCode(sample);

        assertEquals("123456", code);
    }

    @Test
    public void testSaveRowHeader_withYearLessThanStart() throws IOException {
        Sample sample = new Sample();
        Year start = new Year(2000);
        Range range = new Range(start, new Year(2020));
        sample.setRange(range);

        BufferedWriter writer = null; // create a BufferedWriter for testing

        tucson.saveRowHeader(writer, sample, "123456", 6, new Year(1999));
        
        // Assert expected behavior
    }

    @Test
    public void testSaveRowHeader_withYearGreaterThanStart() throws IOException {
        Sample sample = new Sample();
        Year start = new Year(2000);
        Range range = new Range(start, new Year(2020));
        sample.setRange(range);

        BufferedWriter writer = null; // create a BufferedWriter for testing

        tucson.saveRowHeader(writer, sample, "123456", 6, new Year(2001));
        
        // Assert expected behavior
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();
        BufferedWriter writer = null; // create a BufferedWriter for testing

        tucson.saveData(sample, writer);
        
        // Assert expected behavior
    }

    @Test
    public void testComputeRange_withStartGreaterThanOne() {
        Sample sample = new Sample();
        Year start = new Year(2000);
        Range range = new Range(start, new Year(2020));
        sample.setRange(range);

        Range computedRange = tucson.computeRange(sample);

        assertEquals(range, computedRange);
    }

    @Test
    public void testComputeRange_withStartPlus8000GreaterThanOne() {
        Sample sample = new Sample();
        Year start = new Year(1990);
        Range range = new Range(start, new Year(2020));
        sample.setRange(range);

        Range computedRange = tucson.computeRange(sample);

        assertEquals(range.redateBy(8000), computedRange);
    }

    @Test
    public void testComputeRange_withStartLessThanOne() {
        Sample sample = new Sample();
        Year start = new Year(1000);
        Range range = new Range(start, new Year(2020));
        sample.setRange(range);

        Range computedRange = tucson.computeRange(sample);

        assertEquals(range.redateStartTo(new Year(1001)), computedRange);
    }

    @Test
    public void testSave() throws IOException {
        Sample sample = new Sample();
        BufferedWriter writer = null; // create a BufferedWriter for testing

        tucson.save(sample, writer);
        
        // Assert expected behavior
    }
}
