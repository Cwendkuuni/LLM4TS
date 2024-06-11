package corina.formats;

import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class TucsonSimpleTest {
    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", new TucsonSimple().toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", new TucsonSimple().getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.load(new BufferedReader(new FileReader("testfile.txt")));
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");

        String result = new TucsonSimple().make6digitCode(sample);
        assertEquals("123456", result);
    }

    @Test
    public void testMake6digtCodeWhenIdIsNull() {
        Sample sample = new Sample();

        String result = new TucsonSimple().make6digitCode(sample);
        assertEquals("000000", result);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        Sample sample = new Sample();
        sample.range = new Range(Year.fromString("2000"), Year.fromString("2010"));
        sample.meta.put("id", "1234567");

        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        new TucsonSimple().saveRowHeader(writer, sample, "123456", 6, Year.fromString("2003"));
        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        String result = reader.readLine();
        reader.close();

        assertEquals("123456  2003", result);
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();

        sample.range = new Range(Year.fromString("2000"), Year.fromString("2001"));
        sample.data.put(0, "1");
        sample.data.put(1, "2");
        sample.count.put(0, "3");
        sample.count.put(1, "4");

        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        new TucsonSimple().saveData(sample, writer);
        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        String result1 = reader.readLine();
        String result2 = reader.readLine();
        reader.close();

        assertEquals("      1    3", result1);
        assertEquals("      2    4", result2);
    }

    @Test
    public void testComputeRangeWhenStartIsAfterYear1() {
        Sample sample = new Sample();
        sample.range = new Range(Year.fromString("2000"), Year.fromString("2010"));

        Range result = new TucsonSimple().computeRange(sample);
        assertEquals(sample.range, result);
    }

    @Test
    public void testComputeRangeWhenStartPlus8000IsAfterYear1() {
        Sample sample = new Sample();
        sample.range = new Range(Year.fromString("7999"), Year.fromString("8000"));

        Range result = new TucsonSimple().computeRange(sample);
        assertEquals(sample.range.redateBy(8000), result);
    }

    @Test
    public void testComputeRangeWhenStartPlus8000IsBeforeYear1() {
        Sample sample = new Sample();
        sample.range = new Range(Year.fromString("1"), Year.fromString("10"));

        Range result = new TucsonSimple().computeRange(sample);
        assertEquals(sample.range.redateStartTo(new Year(1001)), result);
    }

    @Test
    public void testSave() throws IOException {
        Sample sample = new Sample();
        sample.range = new Range(Year.fromString("2000"), Year.fromString("2001"));
        sample.data.put(0, "1");
        sample.data.put(1, "2");
        sample.count.put(0, "3");
        sample.count.put(1, "4");

        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        new TucsonSimple().save(sample, writer);
        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        String result1 = reader.readLine();
        String result2 = reader.readLine();
        reader.close();

        assertEquals("123456  2000", result1);
        assertEquals("      1    3", result2);
    }
}