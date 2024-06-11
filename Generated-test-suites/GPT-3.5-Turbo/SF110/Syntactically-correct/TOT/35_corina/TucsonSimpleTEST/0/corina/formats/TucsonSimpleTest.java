package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class TucsonSimpleTest {

    // Test case for toString() method
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    // Test case for getDefaultExtension() method
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    // Test case for load() method - throwing WrongFiletypeException
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }

    // Test case for make6digitCode() method - when meta contains "id"
    @Test
    public void testMake6digitCodeWithMetaId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }

    // Test case for make6digitCode() method - when meta does not contain "id"
    @Test
    public void testMake6digitCodeWithoutMetaId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }

    // Test case for saveRowHeader() method - when y is less than start
    @Test
    public void testSaveRowHeaderWithYLessThanStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(1990));
        writer.flush();
        assertEquals("1234561990", writer.toString());
    }

    // Test case for saveRowHeader() method - when y is equal to start
    @Test
    public void testSaveRowHeaderWithYEqualToStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(2000));
        writer.flush();
        assertEquals("123456 2000", writer.toString());
    }

    // Test case for saveRowHeader() method - when y is greater than start
    @Test
    public void testSaveRowHeaderWithYGreaterThanStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveRowHeader(writer, sample, "123456", 4, new Year(1990));
        writer.flush();
        assertEquals("12341990", writer.toString());
    }

    // Test case for saveData() method
    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        tucsonSimple.saveData(sample, writer);
        writer.flush();
        assertEquals("      1   2   999\n", writer.toString());
    }

    // Test case for computeRange() method - when start is greater than or equal to Year(1)
    @Test
    public void testComputeRangeWithStartGreaterOrEqualToYear1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(2000));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(sample.range, range);
    }

    // Test case for computeRange() method - when start.add(8000) is greater than or equal to Year(1)
    @Test
    public void testComputeRangeWithStartAdd8000GreaterOrEqualToYear1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(9900), new Year(10000));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(sample.range.redateBy(8000), range);
    }

    // Test case for computeRange() method - when start is less than Year(1)
    @Test
    public void testComputeRangeWithStartLessThanYear1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-1000), new Year(0));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(1001), new Year(0)), range);
    }

    // Test case for save() method
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(2000));
        sample.data.put(0, 1);
        sample.data.put(1, 2);
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        tucsonSimple.save(sample, writer);
        writer.flush();
        assertEquals("      1   2   999\n\n", stringWriter.toString());
    }
}