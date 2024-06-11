package corina.formats;
import org.junit.Test;
import static org.junit.Assert.*;

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
        tucsonSimple.load(new BufferedReader(new StringReader("")));
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample1 = new Sample();
        sample1.meta.put("id", "1234567");
        assertEquals("123456", tucsonSimple.make6digitCode(sample1));
        
        Sample sample2 = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sample2));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1995));
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(1992));
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        assertEquals("1234561992", reader.readLine());
        reader.close();
        new File("output.txt").delete();
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.put(0, 12);
        sample.data.put(1, 34);
        sample.count.put(0, 1);
        sample.count.put(1, 2);
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        tucsonSimple.saveData(sample, writer);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        assertEquals("     12  134 11", reader.readLine());
        reader.close();
        new File("output.txt").delete();
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1990), new Year(1995));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(1990), range.getStart());
        assertEquals(new Year(1995), range.getEnd());
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.data.put(0, 12);
        sample.data.put(1, 34);
        sample.count.put(0, 1);
        sample.count.put(1, 2);
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        tucsonSimple.save(sample, writer);
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        assertEquals("     12  134 11", reader.readLine());
        reader.close();
        new File("output.txt").delete();
    }

}