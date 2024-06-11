package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.*;
import org.xml.sax.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class LagoonGUITest {
    private LagoonGUI lagoonGUI;
    
    @Before
    public void setUp() throws Exception {
        lagoonGUI = new LagoonGUI();
    }
    
    @Test
    public void testLagoonGUIConstructor() {
        assertNotNull(lagoonGUI.getComponents());
        assertNull(lagoonGUI.getLayout());
        assertNull(lagoonGUI.getBackground());
        assertNotNull(lagoonGUI.getButtonPanel());
        assertNotNull(lagoonGUI.getInputPanel());
        assertNotNull(lagoonGUI.getCenterPanel());
        assertNotNull(lagoonGUI.getLoadButton());
        assertNotNull(lagoonGUI.getSaveButton());
        assertNotNull(lagoonGUI.getBuildButton());
        assertNotNull(lagoonGUI.getForceButton());
        assertNotNull(lagoonGUI.getExitButton());
        assertNotNull(lagoonGUI.getSitemapFile());
        assertNotNull(lagoonGUI.getSourceDir());
        assertNotNull(lagoonGUI.getTargetURL());
        assertNull(lagoonGUI.getPassword());
        assertNotNull(lagoonGUI.getStatusLabel());
        assertNotNull(lagoonGUI.getProgressArea());
        assertNull(lagoonGUI.getProcessor());
        assertEquals(0L, lagoonGUI.getSitemapLastModified());
    }
    
    @Test
    public void testBuild() {
        lagoonGUI.build(false);
        
        assertFalse(lagoonGUI.getLoadButton().isEnabled());
        assertFalse(lagoonGUI.getSaveButton().isEnabled());
        assertFalse(lagoonGUI.getBuildButton().isEnabled());
        assertFalse(lagoonGUI.getForceButton().isEnabled());
        assertFalse(lagoonGUI.getSitemapFile().isEnabled());
        assertFalse(lagoonGUI.getSourceDir().isEnabled());
        assertFalse(lagoonGUI.getTargetURL().isEnabled());
        
        lagoonGUI.setProcessor(new LagoonProcessor("", null, null, null, null, null));
        
        lagoonGUI.build(false);
        
        assertFalse(lagoonGUI.getLoadButton().isEnabled());
        assertFalse(lagoonGUI.getSaveButton().isEnabled());
        assertFalse(lagoonGUI.getBuildButton().isEnabled());
        assertFalse(lagoonGUI.getForceButton().isEnabled());
        assertFalse(lagoonGUI.getSitemapFile().isEnabled());
        assertFalse(lagoonGUI.getSourceDir().isEnabled());
        assertFalse(lagoonGUI.getTargetURL().isEnabled());
        
        // Add additional build test cases
    }
    
    @Test
    public void testExit() {
        lagoonGUI.exit();
        
        // Need to assert System.out statement
    }
    
    @Test
    public void testInit() {
        lagoonGUI.init();
        
        assertNull(lagoonGUI.getProcessor());
        
        // Add additional init test cases
    }
    
    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
        
        // Need to assert FileDialog.show() statement
    }
    
    @Test
    public void testLoadProperties() {
        File propertyFile = new File("test.properties");
        
        if (!propertyFile.exists()) {
            try {
                propertyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        lagoonGUI.loadProperties(propertyFile);
        
        // Add test for setting properties and checking if values are set correctly
        
        propertyFile.delete(); // clean up test.properties file
    }
    
    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
        
        // Need to assert FileDialog.show() statement
    }
    
    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("testProperty", "testValue");
        
        try {
            String value = LagoonGUI.getProperty(prop, "testProperty");
            assertEquals("testValue", value);
        } catch (LagoonException e) {
            e.printStackTrace();
        }
        
        // Add test for when property is not specified
    }
    
}