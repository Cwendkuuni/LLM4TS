import static org.junit.Assert.*;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "Tucson Simple";
        assertEquals(expected, tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        assertEquals(expected, tucsonSimple.getDefaultExtension());
    }

    @Test
    public void testLoad() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        assertThrows(IOException.class, () -> tucsonSimple.load(reader));
    }

    @Test
    public void testMake6digitCodeEdgeCase() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }

    @Test
    public void testSaveRowHeaderIOException() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        doThrow(IOException.class).when(writer).write(anyString());
        assertThrows(IOException.class, () -> tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year()));
    }

    @Test
    public void testComputeRangeEdgeCase() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample(new Range(new Year(0), new Year(1000)));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Range(new Year(1001), new Year(1000)), range);
    }
}