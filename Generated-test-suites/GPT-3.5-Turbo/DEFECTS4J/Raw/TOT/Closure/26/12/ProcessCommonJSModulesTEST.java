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
    public void testGuessCJSModuleName_normalFilename() {
        String filename = "test.js";
        String expected = "module$test";

        String actual = processCommonJSModules.guessCJSModuleName(filename);

        assertEquals(expected, actual);
    }

    @Test
    public void testGuessCJSModuleName_filenameWithLeadingDotAndSlash() {
        String filename = "./test.js";
        String expected = "module$test";

        String actual = processCommonJSModules.guessCJSModuleName(filename);

        assertEquals(expected, actual);
    }

    @Test
    public void testGuessCJSModuleName_filenameWithLeadingDoubleDotAndSlash() {
        String filename = "../test.js";
        String expected = "module$test";

        String actual = processCommonJSModules.guessCJSModuleName(filename);

        assertEquals(expected, actual);
    }

    @Test
    public void testGuessCJSModuleName_filenameWithExtension() {
        String filename = "test.js";
        String expected = "module$test";

        String actual = processCommonJSModules.guessCJSModuleName(filename);

        assertEquals(expected, actual);
    }

    @Test
    public void testToModuleName_normalFilename() {
        String filename = "test.js";
        String expected = "module$test";

        String actual = ProcessCommonJSModules.toModuleName(filename);

        assertEquals(expected, actual);
    }

    @Test
    public void testToModuleName_filenameWithLeadingDotAndSlash() {
        String filename = "./test.js";
        String expected = "module$test";

        String actual = ProcessCommonJSModules.toModuleName(filename);

        assertEquals(expected, actual);
    }

    @Test
    public void testToModuleName_filenameWithLeadingDoubleDotAndSlash() {
        String filename = "../test.js";
        String expected = "module$test";

        String actual = ProcessCommonJSModules.toModuleName(filename);

        assertEquals(expected, actual);
    }

    @Test
    public void testToModuleName_filenameWithExtension() {
        String filename = "test.js";
        String expected = "module$test";

        String actual = ProcessCommonJSModules.toModuleName(filename);

        assertEquals(expected, actual);
    }
}