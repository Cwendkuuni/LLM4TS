
package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import corina.Sample;
import corina.Year;
import corina.util.Range;
import corina.util.WrongFiletypeException;

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
    public void testLoad() throws IOException {
        BufferedReader reader = null;
        tucsonSimple.load(reader);
        // expect WrongFiletypeException to be thrown
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sample));

        sample.meta.put("id", 123456);
        assertEquals("123456", tucsonSimple.make6digitCode(sample));

        sample.meta.put("id", 1234567890);
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        BufferedWriter writer = null;
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(1990);
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        // assert some condition based on writer output
    }

    @Test
    public void testSaveData() throws IOException {
        BufferedWriter writer = null;
        Sample sample = new Sample();
        tucsonSimple.saveData(sample, writer);
        // assert some condition based on writer output
    }

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(1980), new Year(1990));

        Range range = tucsonSimple.computeRange(sample);
        assertNotNull(range);
        assertEquals(new Year(1980), range.getStart());
        assertEquals(new Year(1990), range.getEnd());

        sample.range = new Range(new Year(8000), new Year(8010));
        range = tucsonSimple.computeRange(sample);
        assertNotNull(range);
        assertEquals(new Year(8800), range.getStart());
        assertEquals(new Year(8810), range.getEnd());

        sample.range = new Range(new Year(1), new Year(1990));
        range = tucsonSimple.computeRange(sample);
        assertNotNull(range);
        assertEquals(new Year(1001), range.getStart());
        assertEquals(new Year(1990), range.getEnd());
    }

    @Test
    public void testSave() throws IOException {
        BufferedWriter writer = null;
        Sample sample = new Sample();
        tucsonSimple.save(sample, writer);
        // assert some condition based on writer output
    }
}
