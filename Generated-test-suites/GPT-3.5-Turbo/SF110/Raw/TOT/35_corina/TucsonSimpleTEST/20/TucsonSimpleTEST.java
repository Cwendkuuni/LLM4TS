
package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "TucsonSimple";
        assertEquals(expected, tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        assertEquals(expected, tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws Exception {
        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.load(null);
    }

    @Test
    public void testMake6digitCode_existingId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        String expected = "123456";
        assertEquals(expected, tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testMake6digitCode_noId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        assertEquals(expected, tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testMake6digitCode_longId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        String expected = "123456";
        assertEquals(expected, tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader_yBeforeStart() throws Exception {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample sample = new Sample();
        sample.range = new Range(new Year(1980), new Year(1990));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1970);
        tucsonSimple.saveRowHeader(mockWriter, sample, code, yearWidth, y);
        verify(mockWriter).write("123456 1970");
    }

    @Test
    public void testSaveRowHeader_yAfterStart() throws Exception {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample sample = new Sample();
        sample.range = new Range(new Year(1980), new Year(1990));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1995);
        tucsonSimple.saveRowHeader(mockWriter, sample, code, yearWidth, y);
        verify(mockWriter).write("123456 1995");
    }

    @Test
    public void testSaveData_notSummed_notProcessed() throws Exception {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample sample = new Sample();
        sample.range = new Range(new Year(1980), new Year(1990));
        sample.meta.put("id", "123456");
        tucsonSimple.saveData(sample, mockWriter);
        verify(mockWriter).write("123456 1980");
        verify(mockWriter).write("     ");
    }

    @Test
    public void testSaveData_beforeStart() throws Exception {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample sample = new Sample();
        sample.range = new Range(new Year(1980), new Year(1990));
        sample.meta.put("id", "123456");
        sample.data.put(0, 1);
        tucsonSimple.saveData(sample, mockWriter);
        verify(mockWriter).write("123456 1980");
        verify(mockWriter).write("     ");
        verify(mockWriter).write(StringUtils.leftPad(1, 6));
    }

    @Test
    public void testComputeRange_startGreaterThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1980), new Year(1990));
        Range expected = new Range(new Year(1980), new Year(1990));
        assertEquals(expected, tucsonSimple.computeRange(sample));
    }

    @Test
    public void testComputeRange_startPlus8000GreaterThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-1000), new Year(2000));
        Range expected = new Range(new Year(7000), new Year(9000));
        assertEquals(expected, tucsonSimple.computeRange(sample));
    }

    @Test
    public void testComputeRange_startLessThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-1000), new Year(0));
        Range expected = new Range(new Year(1001), new Year(1000));
        assertEquals(expected, tucsonSimple.computeRange(sample));
    }

    @Test
    public void testSave() throws Exception {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        Sample sample = new Sample();
        tucsonSimple.save(sample, mockWriter);
        verify(mockWriter).newLine();
    }
}
