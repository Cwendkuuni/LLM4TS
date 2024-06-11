import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testGetCode() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("test.js");
        assertNotNull(sourceFile);
        try {
            String code = sourceFile.getCode();
            assertNotNull(code);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Edge case: Empty file source
        SourceFile emptyFile = new SourceFile("");
        assertNotNull(emptyFile);
        try {
            String emptyCode = emptyFile.getCode();
            assertNotNull(emptyCode);
            assertEquals("", emptyCode);
        } catch (IOException e) {
            fail("IOException should not be thrown for empty code");
        }

        // Error scenario: File not found
        SourceFile nonExistingFile = new SourceFile("non_existing_file.js");
        assertNotNull(nonExistingFile);
        try {
            String nonExistingCode = nonExistingFile.getCode();
            assertNull(nonExistingCode);
        } catch (IOException e) {
            fail("IOException should not be thrown for non-existing file");
        }
    }

    @Test
    public void testGetRegion() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("test.js");
        assertNotNull(sourceFile);
        Region region = sourceFile.getRegion(1);
        assertNotNull(region);

        // Edge case: Line number beyond total lines
        Region invalidRegion = sourceFile.getRegion(100);
        assertNull(invalidRegion);

        // Error scenario: IOException
        SourceFile errorFile = new SourceFile("error.js");
        assertNotNull(errorFile);
        Region errorRegion = errorFile.getRegion(1);
        assertNull(errorRegion);
    }

    @Test
    public void testGetLine() {
        // Typical use case
        SourceFile sourceFile = new SourceFile("test.js");
        assertNotNull(sourceFile);
        String line = sourceFile.getLine(1);
        assertNotNull(line);

        // Edge case: Last line
        String lastLine = sourceFile.getLine(sourceFile.getNumLines());
        assertNotNull(lastLine);

        // Error scenario: IOException
        SourceFile errorFile = new SourceFile("error.js");
        assertNotNull(errorFile);
        String errorLine = errorFile.getLine(1);
        assertNull(errorLine);
    }
}