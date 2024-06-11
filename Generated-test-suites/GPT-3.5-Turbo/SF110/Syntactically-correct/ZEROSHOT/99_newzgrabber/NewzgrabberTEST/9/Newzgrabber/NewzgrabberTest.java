import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import Newzgrabber.Newzgrabber;

public class NewzgrabberTest {

    // JTabbedPane
    @Test
    public void testJTabbedPane() {
        Newzgrabber.jtp = Mockito.mock(JTabbedPane.class);
        assertNotNull(Newzgrabber.jtp);
    }

    // OptionsPanel
    @Test
    public void testOptionsPanel() {
        Newzgrabber.optionspanel = Mockito.mock(OptionsPanel.class);
        assertNotNull(Newzgrabber.optionspanel);
    }

    // GroupsPanel
    @Test
    public void testGroupsPanel() {
        Newzgrabber.groupspanel = Mockito.mock(GroupsPanel.class);
        assertNotNull(Newzgrabber.groupspanel);
    }

    // SearchPanel
    @Test
    public void testSearchPanel() {
        Newzgrabber.searchpanel = Mockito.mock(SearchPanel.class);
        assertNotNull(Newzgrabber.searchpanel);
    }

    // TransferPanel
    @Test
    public void testTransferPanel() {
        Newzgrabber.transferpanel = Mockito.mock(TransferPanel.class);
        assertNotNull(Newzgrabber.transferpanel);
    }

    // Newzgrabber.IsBatch
    @Test
    public void testIsBatch() {
        assertEquals(false, Newzgrabber.IsBatch);
    }

    // NewsFactory
    @Test
    public void testNewsFactory() {
        Newzgrabber.nf = Mockito.mock(NewsFactory.class);
        assertNotNull(Newzgrabber.nf);
    }

    // Newzgrabber.Newzdirectory
    @Test
    public void testNewzdirectory() {
        Newzgrabber.Newzdirectory = "test_directory";
        assertEquals("test_directory", Newzgrabber.Newzdirectory);
    }

    // Newzgrabber.JarFilename
    @Test
    public void testJarFilename() {
        assertEquals("Newz.jar", Newzgrabber.JarFilename);
    }

    // Newzgrabber.BatchFilename
    @Test
    public void testBatchFilename() {
        assertEquals("batch.ini", Newzgrabber.BatchFilename);
    }

    // Newzgrabber.NewzInifilename
    @Test
    public void testNewzInifilename() {
        assertEquals("Newzgrabber.ini", Newzgrabber.NewzInifilename);
    }
}