
package corina.formats;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

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
        BufferedReader r = new BufferedReader(new StringReader(""));
        tucsonSimple.load(r);
    }

    @Test
    public void testMake6digitCode() {
        Sample sampleWithId = new Sample();
        sampleWithId.meta.put("id", "12345678");
        assertEquals("123456", tucsonSimple.make6digitCode(sampleWithId));

        Sample sampleWithoutId = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sampleWithoutId));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        BufferedWriter w = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        String code = "000000";
        int yearWidth = 6;
        Year year = new Year(2000);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, year);
        assertEquals("000000  2000", w.toString());
    }

    @Test
    public void testSaveData() throws IOException {
        BufferedWriter w = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.data.put(0, 1.0);
        sample.count.put(0, 2);
        tucsonSimple.saveData(sample, w);
        assertEquals("     1   2", w.toString().trim());
    }

    @Test
    public void testComputeRange() {
        Sample sampleInRange = new Sample();
        sampleInRange.range = new Range(new Year(1), new Year(10));
        assertEquals(new Range(new Year(1), new Year(10)), tucsonSimple.computeRange(sampleInRange));

        Sample sampleOutOfRange = new Sample();
        sampleOutOfRange.range = new Range(new Year(-10), new Year(0));
        assertEquals(new Range(new Year(1001), new Year(1010)), tucsonSimple.computeRange(sampleOutOfRange));
    }

    @Test
    public void testSave() throws IOException {
        BufferedWriter w = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.data.put(0, 1.0);
        sample.count.put(0, 2);
        tucsonSimple.save(sample, w);
        assertEquals("     1   2", w.toString().trim());
    }
}

