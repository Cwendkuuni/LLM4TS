import org.junit.Test;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        // TODO: add assertion to check the value of result
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.getDefaultExtension();
        // TODO: add assertion to check the value of result
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new StringReader("test"));
        tucsonSimple.load(r);
        // TODO: add assertion to check if the exception was thrown
    }
    
    // TODO: write test cases for the remaining methods
    
}