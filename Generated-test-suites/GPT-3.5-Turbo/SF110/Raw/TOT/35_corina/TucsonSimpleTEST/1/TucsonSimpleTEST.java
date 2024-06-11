import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.io.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader reader = Mockito.mock(BufferedReader.class);
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode_WhenMetaDataIdExists() {
        Sample sample = Mockito.mock(Sample.class);
        Mockito.when(sample.meta.containsKey("id")).thenReturn(true);
        Mockito.when(sample.meta.get("id").toString()).thenReturn("123456789");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }

    @Test
    public void testMake6digitCode_WhenMetaDataIdDoesNotExist() {
        Sample sample = Mockito.mock(Sample.class);
        Mockito.when(sample.meta.containsKey("id")).thenReturn(false);
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("      ", code);
    }

    @Test
    public void testSaveRowHeader_WhenYearLessThanStart() throws IOException {
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Year year = new Year(2020);
        String code = "123456";
        int yearWidth = 6;
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        Mockito.verify(writer).write("123456  2020");
    }
    
    @Test
    public void testSaveRowHeader_WhenYearGreaterThanStart() throws IOException {
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Year year = new Year(2022);
        String code = "123456";
        int yearWidth = 6;
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        Mockito.verify(writer).write("123456  2022");
    }

    @Test
    public void testSaveData() throws IOException {
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Range range = new Range(new Year(2020), new Year(2022));
        Mockito.when(sample.range).thenReturn(range);
        Mockito.when(sample.meta.containsKey("id")).thenReturn(true);
        Mockito.when(sample.meta.get("id").toString()).thenReturn("123456789");
        Mockito.when(sample.data.get(Mockito.anyInt())).thenReturn(10);
        Mockito.when(sample.count.get(Mockito.anyInt())).thenReturn(5);
        tucsonSimple.saveData(sample, writer);
        Mockito.verify(writer).write("123456  2020");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("   999");
    }

    @Test
    public void testComputeRange_WhenStartYearGreaterThanEqualOne() {
        Sample sample = Mockito.mock(Sample.class);
        Range range = new Range(new Year(1), new Year(2022));
        Mockito.when(sample.range).thenReturn(range);
        Range computedRange = tucsonSimple.computeRange(sample);
        assertEquals(range, computedRange);
    }

    @Test
    public void testComputeRange_WhenStartYearPlus8000GreaterThanEqualOne() {
        Sample sample = Mockito.mock(Sample.class);
        Range range = new Range(new Year(-7999), new Year(2022));
        Mockito.when(sample.range).thenReturn(new Range(new Year(-8000), new Year(2022)));
        Range computedRange = tucsonSimple.computeRange(sample);
        assertEquals(range, computedRange);
    }

    @Test
    public void testComputeRange_WhenStartYearPlus8000LessThanOne() {
        Sample sample = Mockito.mock(Sample.class);
        Range range = new Range(new Year(1001), new Year(2022));
        Mockito.when(sample.range).thenReturn(new Range(new Year(500), new Year(2022)));
        Range computedRange = tucsonSimple.computeRange(sample);
        assertEquals(range, computedRange);
    }

    @Test
    public void testSave() throws IOException {
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Range range = new Range(new Year(2020), new Year(2022));
        Mockito.when(sample.range).thenReturn(range);
        Mockito.when(sample.meta.containsKey("id")).thenReturn(true);
        Mockito.when(sample.meta.get("id").toString()).thenReturn("123456789");
        Mockito.when(sample.data.get(Mockito.anyInt())).thenReturn(10);
        Mockito.when(sample.count.get(Mockito.anyInt())).thenReturn(5);
        tucsonSimple.save(sample, writer);
        Mockito.verify(writer).write("123456  2020");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("  10  5");
        Mockito.verify(writer).write("   999");
        Mockito.verify(writer).newLine();
    }

}