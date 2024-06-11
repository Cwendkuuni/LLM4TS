import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules();
    }

    @Test
    public void testGuessCJSModuleName_NormalFileName_ReturnsValidModuleName() {
        String fileName = "test.js";
        String expectedModuleName = "module$test";
        
        String actualModuleName = processCommonJSModules.guessCJSModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testGuessCJSModuleName_FileNameWithLeadingDotSlash_ReturnsValidModuleName() {
        String fileName = "./test.js";
        String expectedModuleName = "module$test";
        
        String actualModuleName = processCommonJSModules.guessCJSModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testGuessCJSModuleName_FileNameWithLeadingDoubleDotSlash_ReturnsValidModuleName() {
        String fileName = "../test.js";
        String expectedModuleName = "module$test";
        
        String actualModuleName = processCommonJSModules.guessCJSModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_NormalFileName_ReturnsValidModuleName() {
        String fileName = "test.js";
        String expectedModuleName = "module$test";
        
        String actualModuleName = ProcessCommonJSModules.toModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_FileNameWithLeadingDotSlash_ReturnsValidModuleName() {
        String fileName = "./test.js";
        String expectedModuleName = "module$test";
        
        String actualModuleName = ProcessCommonJSModules.toModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_FileNameWithLeadingDoubleDotSlash_ReturnsValidModuleName() {
        String fileName = "../test.js";
        String expectedModuleName = "module$test";
        
        String actualModuleName = ProcessCommonJSModules.toModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_InvalidFileName_ReturnsValidModuleName() {
        String fileName = "test$file.js";
        String expectedModuleName = "module$test_file";
        
        String actualModuleName = ProcessCommonJSModules.toModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_FileNameWithMultipleSlashes_ReturnsValidModuleName() {
        String fileName = "test/file.js";
        String expectedModuleName = "module$test$file";
        
        String actualModuleName = ProcessCommonJSModules.toModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_FileNameWithTrailingJs_ReturnsValidModuleName() {
        String fileName = "test.js";
        String expectedModuleName = "module$test";
        
        String actualModuleName = ProcessCommonJSModules.toModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_EmptyFileName_ReturnsValidModuleName() {
        String fileName = "";
        String expectedModuleName = "module$";
        
        String actualModuleName = ProcessCommonJSModules.toModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_FileNameWithHyphen_ReturnsValidModuleName() {
        String fileName = "test-file.js";
        String expectedModuleName = "module$test_file";
        
        String actualModuleName = ProcessCommonJSModules.toModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_InvalidFileNameWithHyphen_ReturnsValidModuleName() {
        String fileName = "test$file.js";
        String expectedModuleName = "module$test_file";
        
        String actualModuleName = ProcessCommonJSModules.toModuleName(fileName);
        
        assertEquals(expectedModuleName, actualModuleName);
    }
}