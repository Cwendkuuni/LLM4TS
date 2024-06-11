package com.example.test;

import static org.junit.Assert.*;
import org.junit.Test;

public class CaptureTest {
    
    @Test
    public void testConstructor() {
        Capture capture = new Capture();
        assertNotNull(capture);
    }
    
    @Test
    public void testConstructorWithParameters() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        assertNotNull(capture);
        assertEquals(10, capture.m_Minutes);
        assertEquals(30, capture.m_Seconds);
        assertEquals(gui, capture.m_GUI);
    }
    
    @Test
    public void testInitWithThreadSelected() {
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(true);
        Capture capture = new Capture(10, 30, gui);
        assertFalse(capture.init());
    }
    
    @Test
    public void testInitWithoutThreadSelected() {
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(false);
        Capture capture = new Capture(10, 30, gui);
        assertFalse(capture.init());
    }
    
    @Test
    public void testRun() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.run();
    }
    
    @Test
    public void testDV_CaptureExistingDirectory() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        
        File createOutDir = new File(gui.strOutputDir);
        assertTrue(createOutDir.exists());
        assertTrue(createOutDir.isDirectory());
        
        int response = gui.MessageBox("The Output Directory " + gui.strOutputDir + " already exists!" +
                "\nWould you like to delete it?");
        if (response == 0) {
            try {
                String rm = "rm -fr " + gui.strOutputDir;
                String[] rm_cmd = { "/bin/sh", "-c", rm };
                Process remove_dir = Runtime.getRuntime().exec(rm_cmd, null);
                remove_dir.waitFor();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex2) {
                ex2.printStackTrace();
            }
        } else {
            gui.MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
            gui.blnBegin = false;
        }
    }
    
    @Test
    public void testDV_CaptureExistingFile() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        
        File createOutDir = new File(gui.strOutputDir);
        assertFalse(createOutDir.isDirectory());
        
        int response = gui.MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?");
        if (response == 0) {
            try {
                String rm = "rm -fr " + gui.strOutputDir;
                String[] rm_cmd = { "/bin/sh", "-c", rm };
                Process remove_dir = Runtime.getRuntime().exec(rm_cmd, null);
                remove_dir.waitFor();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex2) {
                ex2.printStackTrace();
            }
        } else {
            gui.MessageBox("Please choose a different name for your\ntitle or move the file to another location.", 2);
            gui.blnBegin = false;
        }
    }
    
    // Test other methods...
}