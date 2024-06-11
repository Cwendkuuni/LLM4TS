import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import org.mockito.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }

    @Test
    public void testToString() {
        String expected = "format.tucsonsimple";
        String result = tucsonSimple.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetDefaultExtension() {
        String expected = ".RWL";
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(expected, result);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader r = Mockito.mock(BufferedReader.class);
        tucsonSimple.load(r);
    }

    @Test
    public void testMake6digitCodeWithId() {
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        String expected = "123456";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        Sample sample = new Sample();
        String expected = "000000";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        BufferedWriter w = Mockito.mock(BufferedWriter.class);
        Sample sample = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2022);
        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
        Mockito.verify(w).write("123456  2022");
    }

    @Test
    public void testSaveData() throws IOException {
        BufferedWriter w = Mockito.mock(BufferedWriter.class);
        Sample sample = new Sample();
        sample.data.put(0, 10);
        sample.data.put(1, 20);
        sample.count.put(0, 5);
        sample.count.put(1, 3);
        tucsonSimple.saveData(sample, w);
        Mockito.verify(w).write("      10   5");
        Mockito.verify(w).write("      20   3");
    }

    @Test
    public void testComputeRangeStartGreaterThanOrEqualToOne() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Range expected = sample.range;
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRangeStartPlus8000GreaterThanOrEqualToOne() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(8000), new Year(9000));
        Range expected = sample.range.redateBy(8000);
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRangeStartLessThanOne() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(-1000), new Year(1000));
        Range expected = sample.range.redateStartTo(new Year(1001));
        Range result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testSave() throws IOException {
        BufferedWriter w = Mockito.mock(BufferedWriter.class);
        Sample sample = new Sample();
        tucsonSimple.save(sample, w);
        Mockito.verify(w).newLine();
    }
}