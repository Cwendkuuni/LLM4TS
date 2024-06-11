import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }

    @After
    public void tearDown() {
        tucsonSimple = null;
    }

    @Test
    public void testToString() {
        String expected = "Tucson Simple";
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
        BufferedReader r = null;
        tucsonSimple.load(r);
    }

    @Test
    public void testMake6digitCode() {
        Sample s = new Sample();
        s.meta.put("id", "123456");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(s);
        assertEquals(expected, actual);

        s.meta.remove("id");
        expected = "000000";
        actual = tucsonSimple.make6digitCode(s);
        assertEquals(expected, actual);

        s.meta.put("id", "1234567890");
        expected = "123456";
        actual = tucsonSimple.make6digitCode(s);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeader() {
        Sample s = new Sample();
        BufferedWriter w = null;
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(2000);
        try {
            tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        // Add assertions here
    }

    @Test
    public void testSaveData() {
        Sample s = new Sample();
        BufferedWriter w = null;
        try {
            tucsonSimple.saveData(s, w);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        // Add assertions here
    }

    @Test
    public void testComputeRange() {
        Sample s = new Sample();
        Year start = new Year(1990);
        s.range = new Range(start, new Year(2020));

        Range expected = new Range(start, new Year(2020));
        Range actual = tucsonSimple.computeRange(s);
        assertEquals(expected, actual);

        start = new Year(8000);
        s.range = new Range(start, new Year(2020));
        expected = new Range(start, new Year(10020));
        actual = tucsonSimple.computeRange(s);
        assertEquals(expected, actual);

        start = new Year(1);
        s.range = new Range(start, new Year(2020));
        expected = new Range(new Year(1001), new Year(2020));
        actual = tucsonSimple.computeRange(s);
        assertEquals(expected, actual);
    }

    @Test
    public void testSave() {
        Sample s = new Sample();
        BufferedWriter w = null;
        try {
            tucsonSimple.save(s, w);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        // Add assertions here
    }

}