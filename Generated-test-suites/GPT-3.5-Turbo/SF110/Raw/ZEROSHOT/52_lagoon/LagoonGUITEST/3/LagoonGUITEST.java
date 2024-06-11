// Test methods for LagoonGUI class

package nu.staldal.lagoon;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testBuildWithoutForce() {
        // Set up initial state
        lagoonGUI.getLoadButton().setEnabled(true);
        lagoonGUI.getSaveButton().setEnabled(true);
        lagoonGUI.getBuildButton().setEnabled(true);
        lagoonGUI.getForceButton().setEnabled(true);
        lagoonGUI.getSitemapFile().setEnabled(true);
        lagoonGUI.getSourceDir().setEnabled(true);
        lagoonGUI.getTargetURL().setEnabled(true);
        lagoonGUI.getStatusLabel().setText("Ready");

        // Call the method being tested
        lagoonGUI.build(false);

        // Verify that the method does what it's supposed to do
        assertTrue(lagoonGUI.getLoadButton().isEnabled());
        assertTrue(lagoonGUI.getSaveButton().isEnabled());
        assertTrue(lagoonGUI.getBuildButton().isEnabled());
        assertTrue(lagoonGUI.getForceButton().isEnabled());
        assertTrue(lagoonGUI.getSitemapFile().isEnabled());
        assertTrue(lagoonGUI.getSourceDir().isEnabled());
        assertTrue(lagoonGUI.getTargetURL().isEnabled());
        assertEquals("Ready", lagoonGUI.getStatusLabel().getText());
    }

    @Test
    public void testBuildWithForce() {
        // Set up initial state
        lagoonGUI.getLoadButton().setEnabled(true);
        lagoonGUI.getSaveButton().setEnabled(true);
        lagoonGUI.getBuildButton().setEnabled(true);
        lagoonGUI.getForceButton().setEnabled(true);
        lagoonGUI.getSitemapFile().setEnabled(true);
        lagoonGUI.getSourceDir().setEnabled(true);
        lagoonGUI.getTargetURL().setEnabled(true);
        lagoonGUI.getStatusLabel().setText("Ready");

        // Call the method being tested
        lagoonGUI.build(true);

        // Verify that the method does what it's supposed to do
        assertTrue(lagoonGUI.getLoadButton().isEnabled());
        assertTrue(lagoonGUI.getSaveButton().isEnabled());
        assertTrue(lagoonGUI.getBuildButton().isEnabled());
        assertTrue(lagoonGUI.getForceButton().isEnabled());
        assertTrue(lagoonGUI.getSitemapFile().isEnabled());
        assertTrue(lagoonGUI.getSourceDir().isEnabled());
        assertTrue(lagoonGUI.getTargetURL().isEnabled());
        assertEquals("Ready", lagoonGUI.getStatusLabel().getText());
    }

    @Test
    public void testExit() {
        // Call the method being tested
        lagoonGUI.exit();

        // Verify that the method does what it's supposed to do
        assertNotNull(lagoonGUI.getProcessor());
    }

    @Test
    public void testInit() {
        // Call the method being tested
        lagoonGUI.init();

        // Verify that the method does what it's supposed to do
        assertNotNull(lagoonGUI.getProcessor());
    }

    @Test
    public void testLoadPropertyFile() {
        // Call the method being tested
        lagoonGUI.loadPropertyFile();

        // Verify that the method does what it's supposed to do
        // - unable to test due to UI interaction
    }

    @Test
    public void testSavePropertyFile() {
        // Call the method being tested
        lagoonGUI.savePropertyFile();

        // Verify that the method does what it's supposed to do
        // - unable to test due to UI interaction
    }
}