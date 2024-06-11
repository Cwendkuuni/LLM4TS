
package corina.formats;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import corina.Sample;
import corina.Year;
import corina.range.Range;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;

    @Before
    public void setup() {
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
        BufferedReader reader = new BufferedReader(new StringReader("sample data"));
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode_withId() {
        Sample sample = new Sample();
        sample.getMeta().put("id", "1234567890");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }

    @Test
    public void testMake6digitCode_withoutId() {
        Sample sample = new Sample();
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }

    @Test
    public void testMake6digitCode_withLongId() {
        Sample sample = new Sample();
        sample.getMeta().put("id", "12345678901234");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }

    @Test
    public void testSaveRowHeader_beforeStart() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Year year = new Year(1999);
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(2000));
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
        writer.flush();
        assertEquals("123456 1999", writer.toString());
    }

    @Test
    public void testSaveRowHeader_afterStart() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Year year = new Year(2005);
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(2000));
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
        writer.flush();
        assertEquals("123456   2005", writer.toString());
    }

    @Test
    public void testSaveData() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2005));
        sample.setRange(range);
        for (Year year = range.getStart(); year.compareTo(range.getEnd()) <= 0; year = year.add(1)) {
            sample.getData().put(year.diff(range.getStart()), 10.2);
        }
        tucsonSimple.saveData(sample, writer);
        writer.flush();
        String expected = "                                     10.2 10.2 10.2 10.2 10.2 10.2\n";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testComputeRange_withStartAfterYear1000() {
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(2000));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(2000), range.getStart());
        assertEquals(new Year(2000), range.getEnd());
    }

    @Test
    public void testComputeRange_withStartAfterYear9000() {
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(9000));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(8201), range.getStart());
        assertEquals(new Year(9000), range.getEnd());
    }

    @Test
    public void testComputeRange_withStartBeforeYear1() {
        Sample sample = new Sample();
        sample.getRange().setStart(new Year(100));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(1001), range.getStart());
        assertEquals(new Year(100), range.getEnd());
    }

    @Test
    public void testSave() throws IOException {
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        Range range = new Range(new Year(2000), new Year(2005));
        sample.setRange(range);
        for (Year year = range.getStart(); year.compareTo(range.getEnd()) <= 0; year = year.add(1)) {
            sample.getData().put(year.diff(range.getStart()), 10.2);
        }
        tucsonSimple.save(sample, writer);
        writer.flush();
        String expected = "000000        2000 10.2 10.2 10.2 10.2 10.2 10.2\n";
        expected += "000000   2001 10.2 10.2 10.2 10.2 10.2 10.2\n";
        expected += "000000   2002 10.2 10.2 10.2 10.2 10.2 10.2\n";
        expected += "000000   2003 10.2 10.2 10.2 10.2 10.2 10.2\n";
        expected += "000000   2004 10.2 10.2 10.2 10.2 10.2 10.2\n";
        expected += "000000   2005 10.2 10.2 10.2 10.2 10.2 10.2\n";
        expected += "   999";
        assertEquals(expected, writer.toString());
    }
}

