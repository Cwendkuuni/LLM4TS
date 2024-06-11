package Newzgrabber;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NewzgrabberTest {

    @Test
    public void testSetupBasicSystem() {
        // Initialize variables
        Newzgrabber.Newzdirectory = "test";
        Newzgrabber.JarFilename = "Test.jar";
        Newzgrabber.verbose = true;
        Newzgrabber.debug = true;

        // Call the method
        Newzgrabber.setupBasicSystem();

        // Verify the output
        assertEquals("test", Newzgrabber.Newzdirectory);
        assertEquals("Test.jar" + System.getProperty("path.separator") + System.getProperty("java.library.path"), System.getProperty("java.library.path"));
    }

    @Test
    public void testFindDirectory() {
        // Initialize variables
        String classfile = "Test.jar";
        System.setProperty("java.class.path", "test1.jar" + System.getProperty("path.separator") + "test2.jar" + System.getProperty("path.separator") + "Test.jar");

        // Call the method
        String result = Newzgrabber.findDirectory(classfile);

        // Verify the output
        assertEquals("test", result);
    }

    @Test
    public void testWindowClosing() {
        // Initialize variables
        Newzgrabber.optionspanel = mock(OptionsPanel.class);
        Newzgrabber.groupspanel = mock(GroupsPanel.class);
        Newzgrabber.transferpanel = mock(TransferPanel.class);

        // Call the method
        Newzgrabber ng = new Newzgrabber();
        ng.windowClosing(null);

        // Verify the calls
        verify(Newzgrabber.optionspanel, times(1)).updateIni();
        verify(Newzgrabber.groupspanel, times(1)).updateIni();
        verify(Newzgrabber.transferpanel, times(1)).updateIni();
    }

    @Test
    public void testUpdateIni() {
        // Initialize variables
        String inifile = "test.ini";
        String prop = "prop";
        String key = "key";
        String value = "value";

        // Call the method
        Newzgrabber ng = new Newzgrabber();
        ng.updateIni(inifile, prop, key, value);

        // Verify the output using a mock object for Ini class
        Ini ini = mock(Ini.class);
        verify(ini, times(1)).writeIni(anyObject());
    }

}