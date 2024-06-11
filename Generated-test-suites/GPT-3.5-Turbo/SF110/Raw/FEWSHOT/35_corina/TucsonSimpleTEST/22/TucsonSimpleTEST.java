package corina.formats;

import static org.junit.Assert.assertEquals;

import corina.Sample;
import corina.WrongFiletypeException;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private Sample sample;
    private BufferedReader reader;
    private BufferedWriter writer;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        sample = new Sample();
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
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        sample.meta.put("id", "123456789");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));

        sample.meta.remove("id");
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        String code = "123456";
        int yearWidth = 4;
        Year year = new Year(2022);

        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        
        // Assert your expectations here
    }

    @Test
    public void testSaveData() throws IOException {
        tucsonSimple.saveData(sample, writer);

        // Assert your expectations here
    }

    @Test
    public void testComputeRange() {
        assertEquals(new Year(1001), tucsonSimple.computeRange(sample).getStart());

        sample.range.setStart(new Year(8000));
        assertEquals(new Year(8000), tucsonSimple.computeRange(sample).getStart());

        sample.range.setStart(new Year(10000));
        assertEquals(new Year(10000), tucsonSimple.computeRange(sample).getStart());
    }

    @Test
    public void testSave() throws IOException {
        tucsonSimple.save(sample, writer);

        // Assert your expectations here
    }
}