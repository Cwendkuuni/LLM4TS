package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
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
    public void cleanup() {
        lagoonGUI = null;
    }
    
    @Test
    public void testBuild() {
        lagoonGUI.build(false);
    }
    
    @Test
    public void testExit() {
        lagoonGUI.exit();
    }
    
    @Test
    public void testInit() {
        lagoonGUI.init();
    }
    
    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
    }
    
    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
    }
    
    @Test
    public void testWindowOpened() {
        lagoonGUI.windowOpened(null);
    }
    
    @Test
    public void testWindowClosing() {
        lagoonGUI.windowClosing(null);
    }
    
    @Test
    public void testWindowClosed() {
        lagoonGUI.windowClosed(null);
    }
    
    @Test
    public void testWindowIconified() {
        lagoonGUI.windowIconified(null);
    }
    
    @Test
    public void testWindowDeiconified() {
        lagoonGUI.windowDeiconified(null);
    }
    
    @Test
    public void testWindowActivated() {
        lagoonGUI.windowActivated(null);
    }
    
    @Test
    public void testWindowDeactivated() {
        lagoonGUI.windowDeactivated(null);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }
    
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testMain() throws Exception {
        String[] args = new String[1];
        args[0] = "property_file.properties";
        LagoonGUI.main(args);
    }
    
    @Test
    public void testGetProperty() throws LagoonException {
        Properties prop = new Properties();
        prop.setProperty("testProp", "value");
        
        assertEquals("value", LagoonGUI.getProperty(prop, "testProp"));
    }
    
    @Test(expected = LagoonException.class)
    public void testGetPropertyThrowsException() throws LagoonException {
        Properties prop = new Properties();
        LagoonGUI.getProperty(prop, "testProp");
    }
    
    @Test
    public void testShowTime() {
        assertEquals("in 5000 ms", LagoonGUI.showTime(5000));
        assertEquals("in 10 s", LagoonGUI.showTime(10000));
    }
}