package corina.formats;
import org.junit.*;
import java.io.*;
import corina.util.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        Assert.assertEquals(result, "format.tucsonsimple");
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.getDefaultExtension();
        Assert.assertEquals(result, ".RWL");
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new FileReader("test.rwl"));
        tucsonSimple.load(r);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.meta.put("id", "123456789");
        String result = tucsonSimple.make6digitCode(s);
        Assert.assertEquals(result, "123456");
    }

    @Test
    public void testMake6digitCodeWithNoId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        String result = tucsonSimple.make6digitCode(s);
        Assert.assertEquals(result, "000000");
    }

    @Test
    public void testMake6digitCodeWithLongId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.meta.put("id", "1234567890");
        String result = tucsonSimple.make6digitCode(s);
        Assert.assertEquals(result, "123456");
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.saveRowHeader(w, s, "123456", 6, new Year(2021));
        w.close();
        BufferedReader r = new BufferedReader(new FileReader("test.rwl"));
        String result = r.readLine();
        r.close();
        Assert.assertEquals(result, "123456  2021");
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.data.put(0, 1);
        s.data.put(1, 2);
        s.data.put(2, 3);
        BufferedWriter w = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.saveData(s, w);
        w.close();
        BufferedReader r = new BufferedReader(new FileReader("test.rwl"));
        String result1 = r.readLine();
        String result2 = r.readLine();
        String result3 = r.readLine();
        r.close();
        Assert.assertEquals(result1, "      1      2      3   999");
        Assert.assertNull(result2);
        Assert.assertNull(result3);
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(1000), new Year(2000));
        Range result = tucsonSimple.computeRange(s);
        Assert.assertEquals(result.getStart(), new Year(1001));
        Assert.assertEquals(result.getEnd(), new Year(2000));
    }

    @Test
    public void testComputeRangeWithStartGreaterThan1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(2000), new Year(3000));
        Range result = tucsonSimple.computeRange(s);
        Assert.assertEquals(result.getStart(), new Year(2000));
        Assert.assertEquals(result.getEnd(), new Year(3000));
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter w = new BufferedWriter(new FileWriter("test.rwl"));
        tucsonSimple.save(s, w);
        w.close();
        BufferedReader r = new BufferedReader(new FileReader("test.rwl"));
        String result = r.readLine();
        r.close();
        Assert.assertNull(result);
    }
}