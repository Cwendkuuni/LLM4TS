
import corina.formats.TucsonSimple;
import corina.ui.Sample;
import corina.util.Range;
import corina.util.Year;
import org.junit.Test;
import static org.junit.Assert.*;

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
        BufferedReader reader = mock(BufferedReader.class);
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);

        sample.meta.remove("id");
        code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }

    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        String code = "123456";
        int yearWidth = 4;
        Year year = new Year(2000);
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        verify(writer).write(code + " 2000");

        year = new Year(1999);
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        verify(writer).write(code + StringUtils.repeat(" ", yearWidth - 4) + "1999");
    }

    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2005));
        sample.range = range;
        sample.data.put(0, 1.0);
        sample.data.put(1, 2.0);
        sample.data.put(2, 3.0);
        sample.data.put(3, 4.0);
        sample.data.put(4, 5.0);
        sample.data.put(5, 6.0);
        BufferedWriter writer = mock(BufferedWriter.class);
        tucsonSimple.saveData(sample, writer);
        verify(writer).write(" 999999");
        verify(writer).write(" 1.00002.00003.00004.00005.00006.0000");
        verify(writer).newLine();
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-10), new Year(5));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(-10), range.getStart());
        assertEquals(new Year(5), range.getEnd());

        sample.range = new Range(new Year(-10), new Year(-1));
        range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(7991), range.getStart());
        assertEquals(new Year(1), range.getEnd());

        sample.range = new Range(new Year(-10), new Year(0));
        range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(1001), range.getStart());
        assertEquals(new Year(1001), range.getEnd());
    }

    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        tucsonSimple.save(sample, writer);
        verify(writer).newLine();
    }
}
