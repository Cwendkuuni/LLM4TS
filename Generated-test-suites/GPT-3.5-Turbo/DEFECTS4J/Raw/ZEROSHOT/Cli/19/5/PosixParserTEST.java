import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @After
    public void tearDown() {
        parser = null;
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        
        String[] arguments = {"--a", "-b", "value", "-c", "arg1", "arg2"};
        
        // Act
        String[] result = parser.flatten(options, arguments, true);
        
        // Assert
        assertEquals(3, result.length);
        assertEquals("--a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }
    
    @Test
    public void testFlatten_DoNotStopAtNonOption() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        
        String[] arguments = {"--a", "-b", "value", "-c", "arg1", "arg2"};
        
        // Act
        String[] result = parser.flatten(options, arguments, false);
        
        // Assert
        assertEquals(6, result.length);
        assertEquals("--a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("arg2", result[5]);
    }
    
    @Test
    public void testFlatten_EmptyArguments() {
        // Arrange
        Options options = new Options();
        
        String[] arguments = {};
        
        // Act
        String[] result = parser.flatten(options, arguments, true);
        
        // Assert
        assertEquals(0, result.length);
    }
}