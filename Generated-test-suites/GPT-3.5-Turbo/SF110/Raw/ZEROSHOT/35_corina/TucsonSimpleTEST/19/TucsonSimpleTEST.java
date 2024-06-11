import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "Tucson Simple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new FileReader("file.txt"));
        tucsonSimple.load(r);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.meta.put("id", "12345678");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(s);
        assertEquals(expected, actual);

        Sample s2 = new Sample();
        String expected2 = "000000";
        String actual2 = tucsonSimple.make6digitCode(s2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testSaveRowHeader() {
        try {
            TucsonSimple tucsonSimple = new TucsonSimple();
            Sample s = new Sample();
            BufferedWriter w = new BufferedWriter(new FileWriter("file.txt"));
            String code = "123456";
            int yearWidth = 6;
            Year y = new Year(2000);
            tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);

            w.close();

            BufferedReader br = new BufferedReader(new FileReader("file.txt"));
            String expected = "123456  2000";
            String actual = br.readLine();
            assertEquals(expected, actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveData() {
        try {
            TucsonSimple tucsonSimple = new TucsonSimple();
            Sample s = new Sample();
            s.data.put(0, 1.0);
            s.data.put(1, 2.0);
            s.data.put(2, 3.0);
            BufferedWriter w = new BufferedWriter(new FileWriter("file.txt"));
            tucsonSimple.saveData(s, w);

            w.close();

            BufferedReader br = new BufferedReader(new FileReader("file.txt"));
            String expected = "   100   200   300";
            String actual = br.readLine();
            assertEquals(expected, actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.range = new Range(new Year(1900), new Year(2000));
        Range expected = new Range(new Year(1900), new Year(2000));
        Range actual = tucsonSimple.computeRange(s);
        assertEquals(expected, actual);

        Sample s2 = new Sample();
        s2.range = new Range(new Year(1000), new Year(2000));
        Range expected2 = new Range(new Year(1800), new Year(2800));
        Range actual2 = tucsonSimple.computeRange(s2);
        assertEquals(expected2, actual2);

        Sample s3 = new Sample();
        s3.range = new Range(new Year(1), new Year(1000));
        Range expected3 = new Range(new Year(1001), new Year(2000));
        Range actual3 = tucsonSimple.computeRange(s3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testSave() {
        try {
            TucsonSimple tucsonSimple = new TucsonSimple();
            Sample s = new Sample();
            BufferedWriter w = new BufferedWriter(new FileWriter("file.txt"));
            tucsonSimple.save(s, w);

            w.close();

            BufferedReader br = new BufferedReader(new FileReader("file.txt"));
            String expected = "";
            String actual = br.readLine();
            assertEquals(expected, actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}