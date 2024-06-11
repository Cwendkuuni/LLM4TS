package corina.formats;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import corina.util.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple format = new TucsonSimple();
        assertEquals("format.tucsonsimple", format.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple format = new TucsonSimple();
        assertEquals(".RWL", format.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple format = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new FileReader("testfile.rwl"));
        format.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple format = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        assertEquals("123456", format.make6digitCode(sample));

        Sample sample2 = new Sample();
        assertEquals("000000", format.make6digitCode(sample2));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple format = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("testfile.rwl"));
        Sample sample = new Sample();
        format.saveRowHeader(writer, sample, "123456", 6, new Year());
        writer.close();

        // assertion logic here
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple format = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("testfile.rwl"));
        Sample sample = new Sample();
        format.saveData(sample, writer);
        writer.close();

        // assertion logic here
    }

    @Test
    public void testComputeRange() {
        TucsonSimple format = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2010));
        assertEquals(sample.range, format.computeRange(sample));
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple format = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new FileWriter("testfile.rwl"));
        Sample sample = new Sample();
        format.save(sample, writer);
        writer.close();

        // assertion logic here
    }

}