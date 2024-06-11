import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        assertEquals("Expected format.tucsonsimple", result);
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(".RWL", result);
    }

    @Test
    public void testLoad() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader mockReader = mock(BufferedReader.class);
        try {
            tucsonSimple.load(mockReader);
            fail("Expected WrongFiletypeException");
        } catch (WrongFiletypeException e) {
            // Exception expected
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample mockSample = mock(Sample.class);
        when(mockSample.getMeta()).thenReturn(Collections.singletonMap("id", "1234567"));
        String result = tucsonSimple.make6digitCode(mockSample);
        assertEquals("123456", result);
    }

    // Add test cases for saveRowHeader, saveData, computeRange, and save methods

}