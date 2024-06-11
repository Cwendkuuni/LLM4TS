
package com.example;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;

public class CaptureTest {

    @Test
    public void testConstructor1() {
        int min = 10;
        int sec = 30;
        GUI gui = new GUI();
        Capture capture = new Capture(min, sec, gui);
        
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);
        assertEquals(min, capture.m_Minutes);
        assertEquals(sec, capture.m_Seconds);
        assertEquals(gui, capture.m_GUI);
    }
    
    @Test
    public void testConstructor2() {
        Capture capture = new Capture();
        
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);
        assertEquals(0, capture.m_Minutes);
        assertEquals(0, capture.m_Seconds);
        assertNull(capture.m_GUI);
    }
    
    @Test
    public void testInitWithThread() {
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(true);
        Capture capture = new Capture(10, 30, gui);
        assertFalse(capture.init());
        assertNotNull(capture.m_Thread);
        assertTrue(capture.m_Thread.isAlive());
    }
    
    @Test
    public void testInitWithoutThread() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        assertFalse(capture.init());
        assertNull(capture.m_Thread);
    }
    
    @Test
    public void testRun() {
        int min = 10;
        int sec = 30;
        GUI gui = new GUI();
        Capture capture = new Capture(min, sec, gui);
        capture.run();
        
        // test the execution of DV_Capture inside the run method
        // logic inside DV_Capture needs to be tested separately
    }
    
    @Test
    public void testDV_Capture() throws IOException, InterruptedException {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        
        // test case for existing output directory
        // needs to be implemented
        
        // test case for existing file with the output directory name
        // needs to be implemented
        
        // test case for successful capture
        File createOutDir = new File(gui.strOutputDir);
        
        if (!createOutDir.exists() || !createOutDir.isDirectory()) {
            createOutDir.mkdir();
        }
        
        createOutDir = new File(gui.strOutputDir + "/DVD");
        if (!createOutDir.exists() || !createOutDir.isDirectory()) {
            createOutDir.mkdir();
        }
        
        createOutDir = new File(gui.strOutputDir + "/log");
        if (!createOutDir.exists() || !createOutDir.isDirectory()) {
            createOutDir.mkdir();
        }
        
        createOutDir = new File(gui.strOutputDir + "/dv");
        if (!createOutDir.exists() || !createOutDir.isDirectory()) {
            createOutDir.mkdir();
        }
        
        ProcessBuilder processBuilder = new ProcessBuilder("/usr/bin/touch", gui.strOutputDir + "/log/dvgrab.log");
        Process p = processBuilder.start();
        p.waitFor();
        
        capture.DV_Capture();
        // Assert the expected outputs or behaviors
        
        // test case for IOException
        // needs to be implemented
        
        // test case for NullPointerException
        // needs to be implemented
        
        // test case for IllegalArgumentException
        // needs to be implemented
        
        // test case for other exceptions
        // needs to be implemented
        
    }
}
