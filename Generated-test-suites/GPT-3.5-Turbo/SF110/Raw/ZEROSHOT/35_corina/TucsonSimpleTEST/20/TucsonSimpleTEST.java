

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import corina.Range;
import corina.Sample;
import corina.Year;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.WrongFiletypeException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }

    @Test
    public void testToString() {
        String expected = I18n.getText("format.tucsonsimple");
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultExtension() {
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader reader = null;
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", "12345678");
        sample.meta = meta;

        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);

        meta.put("id", "12345");
        expected = "12345 ";
        actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);

        meta.remove("id");
        expected = "000000";
        actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("resource")
    @Test
    public void testSaveRowHeader() throws IOException {
        Sample sample = new Sample();
        Range range = new Range();
        range.setStart(new Year(2000));
        sample.range = range;

        BufferedWriter writer = new BufferedWriter(null);
        String code = "123456";
        int yearWidth = 6;
        Year year = new Year(2001);
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);

        writer.flush();
        String expected = "123456 2001";
        String actual = writer.toString().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();
        Range range = new Range();
        range.setStart(new Year(2000));
        range.setEnd(new Year(2002));
        sample.range = range;

        BufferedWriter writer = new BufferedWriter(null);
        tucsonSimple.saveData(sample, writer);

        writer.flush();
        String expected = "";
        String actual = writer.toString().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        Range range = new Range();
        Year start = new Year(5000);
        Year end = new Year(6000);
        range.setStart(start);
        range.setEnd(end);
        sample.range = range;

        Range expected = range;
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);

        start = new Year(9000);
        range.setStart(start);
        expected = range.redateBy(8000);
        actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);

        start = new Year(1);
        range.setStart(start);
        expected = range.redateStartTo(new Year(1001));
        actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("resource")
    @Test
    public void testSave() throws IOException {
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(null);
        tucsonSimple.save(sample, writer);

        writer.flush();
        String expected = "";
        String actual = writer.toString().trim();
        assertEquals(expected, actual);
    }
}
