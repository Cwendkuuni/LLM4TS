
package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CaptureTest {
    private Capture capture;

    @Before
    public void setup() {
        capture = new Capture();
    }

    @Test
    public void testInitWithThreadSelected() {
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(true);
        assertFalse(capture.init());
    }

    @Test
    public void testInitWithoutThreadSelected() {
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(false);
        assertTrue(capture.init());
    }

    @Test
    public void testRun() {
        capture.m_GUI = new GUI();
        capture.run();
    }

    @Test
    public void testDV_CaptureWithExistingDirectory() {
        capture.m_GUI = new GUI();
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "output";
        assertTrue(capture.init());
    }

    @Test
    public void testDV_CaptureWithExistingDirectoryAndDeleteOptionSelected() {
        capture.m_GUI = new GUI();
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "output";

        capture.m_GUI.MessageBox("0");

        assertFalse(capture.init());
        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_CaptureWithExistingFile() {
        capture.m_GUI = new GUI();
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "file";
        assertTrue(capture.init());
    }

    @Test
    public void testDV_CaptureWithExistingFileAndDeleteOptionSelected() {
        capture.m_GUI = new GUI();
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "file";

        capture.m_GUI.MessageBox("0");

        assertFalse(capture.init());
        assertFalse(capture.m_GUI.blnBegin);
    }

    @Test
    public void testDV_CaptureWithNewDirectory() {
        capture.m_GUI = new GUI();
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "new_directory";
        assertTrue(capture.init());
    }

    @Test
    public void testDV_CaptureWithIOException() {
        capture.m_GUI = new GUI();
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "io_exception";
        assertTrue(capture.init());
    }

    @Test
    public void testDV_CaptureWithInterruptedException() {
        capture.m_GUI = new GUI();
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "interrupted_exception";
        assertTrue(capture.init());
    }

    @Test
    public void testDV_CaptureWithNoCameraExistsError() {
        capture.m_GUI = new GUI();
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "no_camera_exists";
        assertTrue(capture.init());
    }

    @Test
    public void testDV_CaptureWithOtherErrors() {
        capture.m_GUI = new GUI();
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "other_errors";
        assertTrue(capture.init());
    }
}
