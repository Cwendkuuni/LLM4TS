package corina.formats;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    @Before
    public void setUp() {
        // Add setup logic here
    }

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
        BufferedReader r = new BufferedReader(new FileReader("path/to/input/file"));
        tucsonSimple.load(r);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");
        String expectedCode = "123456";
        String actualCode = tucsonSimple.make6digitCode(sample);
        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(1990));
        sample.range.setEnd(new Year(1995));
        BufferedWriter w = new BufferedWriter(new FileWriter("path/to/output/file"));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1990);
        try {
            tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
            w.close();
            BufferedReader r = new BufferedReader(new FileReader("path/to/output/file"));
            String expectedRowHeader = "123456 1990";
            String actualRowHeader = r.readLine();
            assertEquals(expectedRowHeader, actualRowHeader);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(1990));
        sample.range.setEnd(new Year(1995));
        BufferedWriter w = new BufferedWriter(new FileWriter("path/to/output/file"));
        try {
            tucsonSimple.saveData(sample, w);
            w.close();
            BufferedReader r = new BufferedReader(new FileReader("path/to/output/file"));
            String expectedData = "   000   001   002   003   004   005";
            String actualData = r.readLine();
            assertEquals(expectedData, actualData);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(1000));
        sample.range.setEnd(new Year(2000));
        Range expectedRange = new Range(new Year(1000), new Year(2000));
        Range actualRange = tucsonSimple.computeRange(sample);
        assertEquals(expectedRange, actualRange);
    }

    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range.setStart(new Year(1990));
        sample.range.setEnd(new Year(1995));
        BufferedWriter w = new BufferedWriter(new FileWriter("path/to/output/file"));
        try {
            tucsonSimple.save(sample, w);
            w.close();
            BufferedReader r = new BufferedReader(new FileReader("path/to/output/file"));
            String expectedData = "   000   001   002   003   004   005";
            String actualData = r.readLine();
            assertEquals(expectedData, actualData);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}