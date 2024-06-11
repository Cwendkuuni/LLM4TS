package corina.formats;

import static org.junit.Assert.*;
import org.junit.Test;
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
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));

        sample.meta.remove("id");
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        Sample sample = new Sample();
        Year year = new Year(2020);
        tucsonSimple.saveRowHeader(writer, sample, "123456", 4, year);

        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        assertEquals("1234 2020", reader.readLine());
        reader.close();
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        Sample sample = new Sample();
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        sample.count.put(0, 3);
        sample.count.put(1, 4);
        tucsonSimple.saveData(sample, writer);

        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        assertEquals("      1   3", reader.readLine());
        assertEquals("      2   4", reader.readLine());
        assertEquals("   999", reader.readLine());
        reader.close();
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Year start = new Year(8000);
        Year end = new Year(9000);
        sample.range = new Range(start, end);
        assertEquals(sample.range, tucsonSimple.computeRange(sample));

        start = new Year(1000);
        sample.range = new Range(start, end);
        assertEquals(new Range(start, end), tucsonSimple.computeRange(sample));

        start = new Year(-1);
        sample.range = new Range(start, end);
        assertEquals(new Range(new Year(1001), end), tucsonSimple.computeRange(sample));
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        Sample sample = new Sample();
        tucsonSimple.save(sample, writer);

        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        assertEquals("", reader.readLine());
        reader.close();
    }
}