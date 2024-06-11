
package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.Range;
import corina.Sample;
import corina.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private Map<String, Object> meta;
    private Range range;
    private Sample sample;

    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
        meta = new HashMap<>();
        range = new Range(new Year(2000), new Year(2009));
        sample = new Sample(meta, range, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
    }

    @Test
    public void testToString() {
        assertEquals("tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader("test"));
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCodeWithIdInMeta() {
        meta.put("id", "1234567");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }

    @Test
    public void testMake6digitCodeWithoutIdInMeta() {
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }

    @Test
    public void testSaveRowHeaderStartBeforeSampleRange() throws IOException {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        Year y = new Year(1999);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, y);
        bufferedWriter.flush();
        assertEquals("123456 1999", writer.toString());
    }

    @Test
    public void testSaveRowHeaderStartAfterSampleRange() throws IOException {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        Year y = new Year(2020);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, "123456", 6, y);
        bufferedWriter.flush();
        assertEquals("123456      2020", writer.toString());
    }

    @Test
    public void testSaveData() throws IOException {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        tucsonSimple.saveData(sample, bufferedWriter);
        bufferedWriter.flush();
        String expectedOutput = "000000 2000 2001 2002 2003 2004 2005 2006 2007 2008 2009";
        expectedOutput += "   1    1    1    1    1    1    1    1    1    1    1";
        expectedOutput += "999999 999999 9990   ";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testComputeRangeStartAfterYearOne() {
        Range s = new Range(new Year(2001), new Year(2010));
        Range r = tucsonSimple.computeRange(new Sample(meta, s, new int[]{}, new int[]{}));
        assertNotNull(r);
        assertEquals(s, r);
    }

    @Test
    public void testComputeRangeStartDateBeforeYearOne() {
        Range s = new Range(new Year(1999), new Year(2010));
        Range r = tucsonSimple.computeRange(new Sample(meta, s, new int[]{}, new int[]{}));
        assertNotNull(r);
        assertEquals(s.redateBy(8000), r);
    }

    @Test
    public void testComputeRangeStartDateBeforeYearOneAndEndBeforeYearOne() {
        Range s = new Range(new Year(-500), new Year(-400));
        Range r = tucsonSimple.computeRange(new Sample(meta, s, new int[]{}, new int[]{}));
        assertNotNull(r);
        assertEquals(new Range(new Year(1001), new Year(-400)), r);
    }

    @Test
    public void testSave() throws IOException {
        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        tucsonSimple.save(sample, bufferedWriter);
        bufferedWriter.flush();
        String expectedOutput = "000000 2000 2001 2002 2003 2004 2005 2006 2007 2008 2009";
        expectedOutput += "   1    1    1    1    1    1    1    1    1    1    1";
        expectedOutput += "999999 999999 9990   ";
        assertEquals(expectedOutput, writer.toString());
    }
}
