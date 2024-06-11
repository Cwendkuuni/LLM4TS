import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.io.*;
import corina.*;
import corina.ui.I18n;

public class TucsonSimpleTest {

    @Mock
    Sample sampleMock = Mockito.mock(Sample.class);
    Sample sample = new Sample();
    
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
        sample.meta.put("id", "12345678");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
        
        sample.meta.remove("id");
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
    }
    
    @Test
    public void testSaveRowHeader() {
        try {
            TucsonSimple tucsonSimple = new TucsonSimple();
            BufferedWriter writerMock = Mockito.mock(BufferedWriter.class);
            String code = "123456";
            int yearWidth = 6;
            Year year = new Year();
            tucsonSimple.saveRowHeader(writerMock, sample, code, yearWidth, year);
            Mockito.verify(writerMock).write(code + "      ");
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testSaveData() {
        try {
            TucsonSimple tucsonSimple = new TucsonSimple();
            BufferedWriter writerMock = Mockito.mock(BufferedWriter.class);
            tucsonSimple.saveData(sample, writerMock);
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(1000), new Year(1001));
        assertEquals(new Range(new Year(1000), new Year(1001)), tucsonSimple.computeRange(sample));
        
        sample.range = new Range(new Year(1), new Year(10));
        assertEquals(new Range(new Year(8001), new Year(8010)), tucsonSimple.computeRange(sample));
        
        sample.range = new Range(new Year(-10), new Year(-1));
        assertEquals(new Range(new Year(1001), new Year(1010)), tucsonSimple.computeRange(sample));
    }
    
    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        try {
            BufferedWriter writerMock = Mockito.mock(BufferedWriter.class);
            tucsonSimple.save(sample, writerMock);
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }
}