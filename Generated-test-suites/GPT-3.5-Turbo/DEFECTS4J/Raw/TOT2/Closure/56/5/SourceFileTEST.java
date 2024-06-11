import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {

    @Test
    public void testGetCode() {
        // Typical Use Case
        SourceFile sourceFile = new SourceFile("testFile");
        String code = "public class Test { }";
        sourceFile.setCode(code);

        try {
            assertEquals(code, sourceFile.getCode());
        } catch (Exception e) {
            fail("Exception thrown for getCode() method");
        }
        
        // Edge Case
        SourceFile emptySourceFile = new SourceFile("emptyFile");
        emptySourceFile.setCode("");
        
        try {
            assertEquals("", emptySourceFile.getCode());
        } catch (Exception e) {
            fail("Exception thrown for getCode() method with empty code");
        }
        
        // Error Scenario
        SourceFile nullSourceFile = new SourceFile("nullFile");
        
        try {
            nullSourceFile.getCode();
            fail("Exception not thrown for getCode() method with null code");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testGetLineOffset() {
        // Typical Use Case
        SourceFile sourceFile = new SourceFile("testFile");
        String code = "public class Test { }";
        sourceFile.setCode(code);

        try {
            assertEquals(0, sourceFile.getLineOffset(1));
            assertEquals(18, sourceFile.getLineOffset(2));
        } catch (Exception e) {
            fail("Exception thrown for getLineOffset() method");
        }

        // Edge Case
        SourceFile emptySourceFile = new SourceFile("emptyFile");
        emptySourceFile.setCode("");

        try {
            assertEquals(0, emptySourceFile.getLineOffset(1));
        } catch (Exception e) {
            fail("Exception thrown for getLineOffset() method with empty code");
        }
        
        // Error Scenario
        SourceFile nullSourceFile = new SourceFile("nullFile");
        
        try {
            nullSourceFile.getLineOffset(1);
            fail("Exception not thrown for getLineOffset() method with null code");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    // Add more test cases for other methods in the SourceFile class

}