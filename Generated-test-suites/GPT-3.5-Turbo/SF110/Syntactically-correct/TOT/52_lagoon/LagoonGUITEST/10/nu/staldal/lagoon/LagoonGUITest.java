
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @After
    public void tearDown() {
        lagoonGUI = null;
    }

    @Test
    public void testBuild_withValidInput_buildButtonEnabled() {
        lagoonGUI.sourceDir.setValue("src");
        lagoonGUI.sitemapFile.setValue("lagoon.xml");
        lagoonGUI.targetURL.setValue("http://example.com");
        lagoonGUI.buildButton.setEnabled(true);

        // Verify that buildButton is enabled before build() is called
        assertTrue(lagoonGUI.buildButton.isEnabled());

        lagoonGUI.build(false);

        // Verify that buildButton is disabled after build() is called
        assertFalse(lagoonGUI.buildButton.isEnabled());
    }

    @Test
    public void testBuild_withInvalidInput_showErrorMessage() {
        lagoonGUI.sourceDir.setValue("invalid_dir");
        lagoonGUI.sitemapFile.setValue("invalid_file.xml");
        lagoonGUI.targetURL.setValue("http://example.com");

        // Verify that progressArea is empty before build() is called
        assertEquals("", lagoonGUI.progressArea.getText());

        lagoonGUI.build(false);

        // Verify that progressArea has error message after build() is called
        assertTrue(lagoonGUI.progressArea.getText().contains("Error(s) occured while building"));
    }

    @Test
    public void testExit_callDestroyMethod() {
        lagoonGUI.processor = new LagoonProcessor();
        lagoonGUI.exit();

        // Verify that destroy() method of LagoonProcessor is called
        assertTrue(lagoonGUI.processor.isDestroyed());
    }

    @Test
    public void testInit_withInvalidSitemapFile_showErrorMessage() {
        lagoonGUI.sitemapFile.setValue("invalid_sitemap.xml");

        // Verify that progressArea is empty before init() is called
        assertEquals("", lagoonGUI.progressArea.getText());

        lagoonGUI.init();

        // Verify that progressArea has error message after init() is called
        assertTrue(lagoonGUI.progressArea.getText().contains("File not found"));
    }

    // Other test cases for remaining methods in LagoonGUI class

    // ...

}

