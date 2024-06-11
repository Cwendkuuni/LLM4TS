import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample1 = new Sample();
        sample1.meta.put("id", "1234567");
        assertEquals("123456", tucsonSimple.make6digitCode(sample1));

        Sample sample2 = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sample2));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2020), new Year(2021));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(2020));
        writer.flush();
        assertEquals("123456 2020", writer.toString());
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2020), new Year(2021));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.count.put(0, 3);
        sample.count.put(1, 4);
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(sample, writer);
        writer.flush();
        assertEquals("000000 2020   1   3   2   4\n", writer.toString());
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample1 = new Sample();
        sample1.range = new Range(new Year(2020), new Year(2021));
        assertEquals(new Year(2020), tucsonSimple.computeRange(sample1).getStart());
        assertEquals(new Year(2021), tucsonSimple.computeRange(sample1).getEnd());

        Sample sample2 = new Sample();
        sample2.range = new Range(new Year(-2), new Year(1));
        assertEquals(new Year(999), tucsonSimple.computeRange(sample2).getStart());
        assertEquals(new Year(1), tucsonSimple.computeRange(sample2).getEnd());
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2020), new Year(2021));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.count.put(0, 3);
        sample.count.put(1, 4);
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.save(sample, writer);
        writer.flush();
        assertEquals("000000 2020   1   3   2   4\n", writer.toString());
    }
}