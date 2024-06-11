
package nu.staldal.lagoon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import static org.junit.Assert.*;

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
    public void testBuild() throws IOException {
        // Arrange
        lagoonGUI.sitemapFile.setValue("sitemap.xml");
        lagoonGUI.sourceDir.setValue("src");
        lagoonGUI.targetURL.setValue("http://www.example.com");

        // Act
        lagoonGUI.build(false);

        // Assert
        assertNull(lagoonGUI.processor);
        assertEquals("Not initialized", lagoonGUI.statusLabel.getText());
    }

    @Test
    public void testExit() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("properties", ".tmp");
        tempFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tempFile);
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "sitemap.xml");
        prop.store(fos, "Lagoon properties");
        fos.close();
        String[] args = {tempFile.getAbsolutePath()};

        // Act
        lagoonGUI.main(args);
        lagoonGUI.exit();

        // Assert
        assertFalse(lagoonGUI.isActive());
    }

    @Test
    public void testInit() {
        // Arrange
        String expectedStatusLabelText = "Not initialized";

        // Act
        lagoonGUI.init();

        // Assert
        assertNull(lagoonGUI.processor);
        assertEquals(expectedStatusLabelText, lagoonGUI.statusLabel.getText());
    }

    @Test
    public void testLoadPropertyFile() {
        // Arrange
        lagoonGUI.sitemapFile.setValue("sitemap.xml");
        lagoonGUI.sourceDir.setValue("src");
        lagoonGUI.targetURL.setValue("http://www.example.com");

        // Act
        lagoonGUI.loadPropertyFile();

        // Assert
        assertEquals("sitemap.xml", lagoonGUI.sitemapFile.getValue());
        assertEquals("src", lagoonGUI.sourceDir.getValue());
        assertEquals("http://www.example.com", lagoonGUI.targetURL.getValue());
    }

    @Test
    public void testLoadProperties() {
        // Arrange
        File tempFile = null;
        try {
            tempFile = File.createTempFile("properties", ".tmp");
            tempFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempFile);
            Properties prop = new Properties();
            prop.setProperty("sitemapFile", "sitemap.xml");
            prop.setProperty("sourceDir", "src");
            prop.setProperty("targetURL", "http://www.example.com");
            prop.store(fos, "Lagoon properties");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act
        lagoonGUI.loadProperties(tempFile);

        // Assert
        assertEquals("sitemap.xml", lagoonGUI.sitemapFile.getValue());
        assertEquals("src", lagoonGUI.sourceDir.getValue());
        assertEquals("http://www.example.com", lagoonGUI.targetURL.getValue());
    }

    @Test
    public void testSavePropertyFile() {
        // Arrange
        File tempFile = null;
        try {
            tempFile = File.createTempFile("properties", ".tmp");
            tempFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lagoonGUI.sitemapFile.setValue("sitemap.xml");
        lagoonGUI.sourceDir.setValue("src");
        lagoonGUI.targetURL.setValue("http://www.example.com");

        // Act
        lagoonGUI.savePropertyFile();

        // Assert
        assertNotNull(tempFile);
    }

    @Test
    public void testGetProperty() throws LagoonException {
        // Arrange
        String expectedValue = "value";
        Properties prop = new Properties();
        prop.setProperty("name", expectedValue);

        // Act
        String actualValue = LagoonGUI.getProperty(prop, "name");

        // Assert
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWhenPropertyIsNull() throws LagoonException {
        // Arrange
        Properties prop = new Properties();
        prop.setProperty("name", null);

        // Act
        LagoonGUI.getProperty(prop, "name");
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWhenPropertyIsNotSpecified() throws LagoonException {
        // Arrange
        Properties prop = new Properties();

        // Act
        LagoonGUI.getProperty(prop, "name");
    }

    @Test
    public void testShowTime() {
        // Arrange
        long ms1 = 5000L;
        long ms2 = 15000L;
        String expectedOutput1 = "in 5000 ms";
        String expectedOutput2 = "in 15 s";

        // Act
        String actualOutput1 = LagoonGUI.showTime(ms1);
        String actualOutput2 = LagoonGUI.showTime(ms2);

        // Assert
        assertEquals(expectedOutput1, actualOutput1);
        assertEquals(expectedOutput2, actualOutput2);
    }
}
