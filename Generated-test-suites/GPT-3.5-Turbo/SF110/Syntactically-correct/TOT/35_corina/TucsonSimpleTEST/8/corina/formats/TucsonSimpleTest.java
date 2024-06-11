import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import corina.util.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private Sample sample;
    private StringWriter stringWriter;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        sample = new Sample();
        stringWriter = new StringWriter();
        bufferedWriter = new BufferedWriter(stringWriter);
        bufferedReader = new BufferedReader(new StringReader(""));
    }

    @Test
    public void testToString() {
        String expected = "Tucson Simple Format";
        assertEquals(expected, tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        String expected = ".RWL";
        assertEquals(expected, tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        tucsonSimple.load(bufferedReader);
    }

    @Test
    public void testMake6digitCodeWithExistingId() {
        sample.meta.put("id", "12345678");
        String expected = "123456";
        assertEquals(expected, tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        String expected = "000000";
        assertEquals(expected, tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testMake6digitCodeWithLongId() {
        sample.meta.put("id", "1234567890");
        String expected = "123456";
        assertEquals(expected, tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeaderWithPrefixStartBeforeYear() throws IOException {
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2020));
        Year y = new Year(1999);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 4, y);
        bufferedWriter.flush();
        String expected = "1234 1999";
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testSaveRowHeaderWithPrefixStartAfterYear() throws IOException {
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2020));
        Year y = new Year(2001);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 4, y);
        bufferedWriter.flush();
        String expected = "1234 2001";
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testSaveRowHeaderWithPrefixStartEqualYear() throws IOException {
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2020));
        Year y = new Year(2000);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 4, y);
        bufferedWriter.flush();
        String expected = "123456   ";
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testSaveDataWithUnprocessedData() throws IOException {
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2003));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        sample.data.put(3, 4);
        tucsonSimple.saveData(sample, bufferedWriter);
        bufferedWriter.flush();
        String expected = "     0  0  0  0\n" +
                          "     1  2  3  4\n" +
                          "   999";
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testSaveDataWithProcessedData() throws IOException {
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2003));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        sample.data.put(3, 4);
        sample.meta.put("processed", "true");
        tucsonSimple.saveData(sample, bufferedWriter);
        bufferedWriter.flush();
        String expected = "000000   0  0  0\n" +
                          "     1  2  3  4\n" +
                          "   999";
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testSaveDataWithSummedData() throws IOException {
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2003));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        sample.data.put(3, 4);
        sample.meta.put("summed", "true");
        tucsonSimple.saveData(sample, bufferedWriter);
        bufferedWriter.flush();
        String expected = "000000   0  0  0  001\n" +
                          "     1  2  3  4  000\n" +
                          "   9990   ";
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testSaveDataWithProcessedAndSummedData() throws IOException {
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2003));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        sample.data.put(3, 4);
        sample.meta.put("processed", "true");
        sample.meta.put("summed", "true");
        tucsonSimple.saveData(sample, bufferedWriter);
        bufferedWriter.flush();
        String expected = "000000   0  0  0  001\n" +
                          "     1  2  3  4  000\n" +
                          "   9990  0 ";
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testComputeRangeWithStartAfterYear1() {
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2020));
        Range expected = new Range(new Year(2000), new Year(2020));
        assertEquals(expected.toString(), tucsonSimple.computeRange(sample).toString());
    }

    @Test
    public void testComputeRangeWithStartBeforeYear1() {
        sample.range.setStart(new Year(1000));
        sample.range.setEnd(new Year(2020));
        Range expected = new Range(new Year(1001), new Year(2020));
        assertEquals(expected.toString(), tucsonSimple.computeRange(sample).toString());
    }

    @Test
    public void testComputeRangeWithStartBeforeYear1AndEndAfterYear8000() {
        sample.range.setStart(new Year(1000));
        sample.range.setEnd(new Year(9000));
        Range expected = new Range(new Year(1801), new Year(9000));
        assertEquals(expected.toString(), tucsonSimple.computeRange(sample).toString());
    }

    @Test
    public void testSave() throws IOException {
        sample.range.setStart(new Year(2000));
        sample.range.setEnd(new Year(2003));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.data.put(2, 3);
        sample.data.put(3, 4);
        tucsonSimple.save(sample, bufferedWriter);
        bufferedWriter.flush();
        String expected = "     1  2  3  4\n" +
                          "   999";
        assertEquals(expected, stringWriter.toString());
    }

}