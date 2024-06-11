
package nu.staldal.lagoon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {
    
    private LagoonGUI lagoonGUI;
    
    @Before
    public void setup() {
        lagoonGUI = new LagoonGUI();
    }
    
    @After
    public void tearDown() {
        lagoonGUI = null;
    }
    
    @Test
    public void testBuild() {
        // Test build method with force parameter set to false
        // Assumptions:
        // - loadButton, saveButton, buildButton, forceButton, sitemapFile, sourceDir, targetURL buttons are enabled before build
        // - statusLabel is set to "Ready" before build
        // - processor is null or has changes in sitemapFile, sourceDir or targetURL
        // - sitemapLastModified is less than sm.lastModified()
        // Expected Output:
        // - loadButton, saveButton, buildButton, forceButton, sitemapFile, sourceDir, targetURL buttons are disabled during build
        // - progressArea text is set to "--- Start build ---\n"
        // - statusLabel text is set to "Building..."
        // - processor.build(true) is called
        // - if success is true, progressArea text is appended with "--- Build finished successfully " + showTime(timeElapsed) + " ---"
        // - if success is false, progressArea text is appended with "--- Build finished with error(s) " + showTime(timeElapsed) + " ---"
        // - statusLabel text is set to "Ready"
        // - loadButton, saveButton, buildButton, forceButton, sitemapFile, sourceDir, targetURL buttons are enabled after build
        
    }
    
    @Test
    public void testExit() {
        // Test exit method
        // Assumptions:
        // - processor is not null
        // Expected Output:
        // - processor.destroy() is called
        // - System.exit(0) is called
    }
    
    @Test
    public void testInit() {
        // Test init method
        // Assumptions:
        // - statusLabel is set to "Initializing..."
        // Expected Output:
        // - If processor is not null, processor.destroy() is called
        // - processor is set to null
        // - FileInputStream and InputSource are used to parse XML file
        // - PrintWriter and TextAreaWriter are used to create LagoonProcessor instance
        // - If AuthenticationMissingException is thrown, PasswordDialog is shown
        // - If AuthenticationException is thrown, PasswordDialog is shown
        // - If FileNotFoundException is thrown, MessageDialog is shown
        // - If IOException is thrown, MessageDialog is shown
        // - If LagoonException is thrown, MessageDialog is shown
    }
    
    @Test
    public void testLoadPropertyFile() {
        // Test loadPropertyFile method
        // Assumptions:
        // - FileDialog is used to get property file
        // Expected Output:
        // - FileInputStream is used to read property file
        // - saveProperties is called with properties from property file
    }
    
    @Test
    public void testLoadProperties() {
        // Test loadProperties method
        // Assumptions:
        // - FileInputStream is used to read property file
        // Expected Output:
        // - Properties are loaded from FileInputStream
        // - Properties are set to corresponding input components
    }
    
    @Test
    public void testSavePropertyFile() {
        // Test savePropertyFile method
        // Assumptions:
        // - FileDialog is used to save property file
        // - YesNoQueryDialog is used to ask for saving password in property file
        // Expected Output:
        // - FileOutputStream is used to write property file
        // - Properties are created and set with values from input components
        // - Properties are stored in FileOutputStream
        // - If password is not null, password is saved in properties
        // - statusLabel text is set to "Properties saved"
    }
    
    @Test
    public void testGetProperty() {
        // Test getProperty method
        // Assumptions:
        // - Property value is null
        // Expected Output:
        // - LagoonException is thrown with message "Property {property name} not specified"
    }
    
    @Test
    public void testShowTime() {
        // Test showTime method
        // Assumptions:
        // - ms is less than 10000
        // Expected Output:
        // - String of time in milliseconds is returned
        // - ms is appended with " ms"
    }
}
