import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewzgrabberTest {

    private Newzgrabber newzgrabber;

    @Before
    public void setUp() {
        newzgrabber = new Newzgrabber();
    }

    @Test
    public void testConstructor_IsBatchTrue() {
        assertTrue(Newzgrabber.IsBatch);
    }

    @Test
    public void testConstructor_JTabbedPaneNotNull() {
        assertNotNull(Newzgrabber.jtp);
    }

    @Test
    public void testConstructor_OptionsPanelNotNull() {
        assertNotNull(Newzgrabber.optionspanel);
    }

    @Test
    public void testConstructor_GroupsPanelNotNull() {
        assertNotNull(Newzgrabber.groupspanel);
    }

    @Test
    public void testConstructor_SearchPanelNotNull() {
        assertNotNull(Newzgrabber.searchpanel);
    }

    @Test
    public void testConstructor_TransferPanelNotNull() {
        assertNotNull(Newzgrabber.transferpanel);
    }

    @Test
    public void testSetupBasicSystem_NewzdirectoryNotNull() {
        assertNotNull(Newzgrabber.Newzdirectory);
    }

    @Test
    public void testSetupBasicSystem_NewsFactoryNotNull() {
        assertNotNull(Newzgrabber.nf);
    }

    @Test
    public void testWindowClosed() {
        newzgrabber.windowClosed(); 
        // Test for no exception
    }

    @Test
    public void testWindowOpened() {
        newzgrabber.windowOpened(); 
        // Test for no exception
    }

    @Test
    public void testWindowActivated() {
        newzgrabber.windowActivated(); 
        // Test for no exception
    }

    @Test
    public void testWindowDeactivated() {
        newzgrabber.windowDeactivated(); 
        // Test for no exception
    }

    @Test
    public void testWindowIconified() {
        newzgrabber.windowIconified(); 
        // Test for no exception
    }

    @Test
    public void testWindowDeiconified() {
        newzgrabber.windowDeiconified(); 
        // Test for no exception
    }

    @Test
    public void testWindowClosing() {
        newzgrabber.windowClosing(); 
        // Test for no exception
    }

    @Test
    public void testFindDirectory_ClassfileNotNull() {
        assertNotNull(Newzgrabber.findDirectory(Newzgrabber.JarFilename));
    }

    @Test
    public void testUpdateIni_InifileNotNull() {
        newzgrabber.updateIni("iniFile", "prop", "key", "value"); 
        // Test for no exception
    }

    @Test
    public void testMain_ArgsNotNull() {
        String[] args = {"-verbose"};
        Newzgrabber.main(args); 
        // Test for no exception
    }

    @Test
    public void testMain_ArgsEmpty() {
        String[] args = {};
        Newzgrabber.main(args); 
        // Test for no exception
    }

}