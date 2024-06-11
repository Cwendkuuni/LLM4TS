import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

    @Test
    public void testGetCode() {
        // Typical use case
        // Test if getCode() returns the correct source code
        SourceFile sourceFile = new SourceFile("TestFile.java");
        String code = "public class Test {}";
        sourceFile.setCode(code);
        try {
            assertEquals(code, sourceFile.getCode());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetCodeReader() {
        // Typical use case
        // Test if getCodeReader() returns a valid Reader
        SourceFile sourceFile = new SourceFile("TestFile.java");
        String code = "public class Test {}";
        sourceFile.setCode(code);
        try {
            assertNotNull(sourceFile.getCodeReader());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetLineOffset() {
        // Edge case
        // Test for line number out of bounds
        SourceFile sourceFile = new SourceFile("TestFile.java");
        try {
            sourceFile.getLineOffset(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // test passed
        }
    }

    @Test
    public void testGetNumLines() {
        // Typical use case
        // Test if getNumLines() returns the correct number of lines
        SourceFile sourceFile = new SourceFile("TestFile.java");
        String code = "first line\nsecond line\nthird line";
        sourceFile.setCode(code);
        try {
            assertEquals(3, sourceFile.getNumLines());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetLine() {
        // Error scenario
        // Test for IO exception
        SourceFile sourceFile = new SourceFile("TestFile.java");
        try {
            sourceFile.getLine(1);
            fail("Expected IOException");
        } catch (IOException e) {
            // test passed
        }

    }

    @Test
    public void testGetRegion() {
        // Error scenario
        // Test for IO exception
        SourceFile sourceFile = new SourceFile("TestFile.java");
        try {
            sourceFile.getRegion(1);
            fail("Expected IOException");
        } catch (IOException e) {
            // test passed
        }
    }

    @Test
    public void testFromCode() {
        // Typical use case
        // Test creating SourceFile from code
        SourceFile sourceFile = SourceFile.fromCode("TestFile.java", "public class Test {}");
        assertNotNull(sourceFile);
    }

    @Test
    public void testFromFile() {
        // Typical use case
        // Test creating SourceFile from file
        SourceFile sourceFile = SourceFile.fromFile("TestFile.java");
        assertNotNull(sourceFile);
    }
}