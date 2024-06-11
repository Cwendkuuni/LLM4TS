package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

    // Test the main method with valid arguments
    @Test
    public void testMainValidArgs() {
        String[] args = {"domainDef-file-name", "problemDef-file-name"};
        JSJshop.main(args);
    }

    // Test the main method with invalid number of arguments
    @Test
    public void testMainInvalidArgs() {
        String[] args = {"domainDef-file-name"};
        JSJshop.main(args);
    }

    // Test the parserFile method with a valid file
    @Test
    public void testParserFileValidFile() {
        JSJshop jsJshop = new JSJshop();
        boolean result = jsJshop.parserFile("nameDomainFile");
        assertTrue(result);
    }

    // Test the parserFile method with an invalid file
    @Test
    public void testParserFileInvalidFile() {
        JSJshop jsJshop = new JSJshop();
        boolean result = jsJshop.parserFile("invalidFile");
        assertFalse(result);
    }

    // Test the setFile method with a valid nameFile and pred
    @Test
    public void testSetFileValid() {
        JSJshop jsJshop = new JSJshop();
        JSTaskAtom pred = new JSTaskAtom();
        JSPairPlanTSListNodes result = jsJshop.setFile("nameFile", pred);
        assertNotNull(result);
    }

    // Test the setFile method with an invalid nameFile
    @Test
    public void testSetFileInvalid() {
        JSJshop jsJshop = new JSJshop();
        JSTaskAtom pred = new JSTaskAtom();
        JSPairPlanTSListNodes result = jsJshop.setFile("invalidFile", pred);
        assertNull(result);
    }
}