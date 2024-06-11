import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucson.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucson = new TucsonSimple();
        assertEquals(".RWL", tucson.getDefaultExtension());
    }
    
    @Test
    public void testLoad() {
        TucsonSimple tucson = new TucsonSimple();
        BufferedReader r = Mockito.mock(BufferedReader.class);
        
        try {
            tucson.load(r);
            fail("Exception not thrown for load method");
        } catch (IOException ex) {
            assertEquals("WrongFiletypeException", ex.getClass().getSimpleName());
        }
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = Mockito.mock(Sample.class);
        Mockito.when(sample.getMeta()).thenReturn(new HashMap<>());
        
        String code = tucson.make6digitCode(sample);
        assertEquals("000000", code);
    }

    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucson = new TucsonSimple();
        BufferedWriter w = Mockito.mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Year year = Mockito.mock(Year.class);
        
        assertThrows(IOException.class, () -> tucson.saveRowHeader(w, sample, "123456", 6, year));
    }
    
    @Test
    public void testSaveData() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter w = Mockito.mock(BufferedWriter.class);
        
        assertDoesNotThrow(() -> tucson.saveData(sample, w));
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = Mockito.mock(Sample.class);
        
        Range range = tucson.computeRange(sample);
        assertNotNull(range);
    }
    
    @Test
    public void testSave() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter w = Mockito.mock(BufferedWriter.class);
        
        assertDoesNotThrow(() -> tucson.save(sample, w));
    }
}