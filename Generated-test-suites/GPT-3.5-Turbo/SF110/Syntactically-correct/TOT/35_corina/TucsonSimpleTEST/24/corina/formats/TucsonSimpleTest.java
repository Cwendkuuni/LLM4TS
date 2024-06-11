import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import corina.util.Year;
import corina.formats.*;
import corina.ui.*;
import corina.Sample;
import corina.WrongFiletypeException;

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
    public void testMake6digitCodeWithId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", 123);
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123   ", code);
    }
    
    @Test
    public void testMake6digitCodeWithoutId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }
    
    @Test
    public void testMake6digitCodeWithIdGreaterThan6Digits() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", 123456789);
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }
    
    @Test
    public void testSaveRowHeaderStartBeforeRange() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(2000));
        tucsonSimple.saveRowHeader(writer, sample, "123   ", 6, new Year(1980));
        assertEquals("123   1980", writer.toString());
    }
    
    @Test
    public void testSaveRowHeaderStartAfterRange() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(2000));
        tucsonSimple.saveRowHeader(writer, sample, "123   ", 6, new Year(2010));
        assertEquals("123   ", writer.toString());
    }
    
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.data.put(0, 100);
        sample.data.put(1, 200);
        sample.count.put(0, 1);
        sample.count.put(1, 2);
        sample.range = new Range(new Year(2000), new Year(2001));
        tucsonSimple.saveData(sample, writer);
        assertEquals("100  2001   100   200\n", writer.toString());
    }
    
    @Test
    public void testComputeRangeStartGreaterThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2001));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(2000), new Year(2001)), range);
    }
    
    @Test
    public void testComputeRangeStartPlus8000GreaterThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1), new Year(8001));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(8001), new Year(16001)), range);
    }
    
    @Test
    public void testComputeRangeStartLessThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(199), new Year(200));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(1001), new Year(200)), range);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.data.put(0, 100);
        sample.data.put(1, 200);
        sample.count.put(0, 1);
        sample.count.put(1, 2);
        sample.range = new Range(new Year(2000), new Year(2001));
        tucsonSimple.save(sample, writer);
        assertEquals("100  2001   100   200\n", writer.toString());
    }
}