import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ClassInfoTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void testIsAbstract() {
        ClassInfo info = new ClassInfo();
        assertFalse(info.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo info = new ClassInfo();
        assertFalse(info.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo info = new ClassInfo();
        assertEquals("javax.swing.WindowConstants", info.getClassName());
    }

    // Additional test cases for other methods can be added here
    
    @Test
    public void testPrintFields() {
        ClassInfo info = new ClassInfo();
        assertNull(info.printFields());
    }

    @Test
    public void testPrintMethods() {
        ClassInfo info = new ClassInfo();
        assertNull(info.printMethods());
    }

    // Add test cases for other methods
    
    @Test
    public void testMainWithClassNameAsArgument() {
        String[] args = {"com.jstevh.viewer.ClassInfo"};
        ClassInfo.main(args);
        assertEquals("Total time: ", outContent.toString().contains("Total time:"));
    }

    @Test
    public void testMainWithoutClassNameAsArgument() {
        String[] args = {};
        ClassInfo.main(args);
        assertEquals("Total time: ", outContent.toString().contains("Total time:"));
    }
}