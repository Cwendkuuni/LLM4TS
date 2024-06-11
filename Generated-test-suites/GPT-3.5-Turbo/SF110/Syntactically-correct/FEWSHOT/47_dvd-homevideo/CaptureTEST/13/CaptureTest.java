import static org.junit.Assert.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class CaptureTest {

    @Test
    public void testInit_menuChkThreadSelected() {
        Capture capture = new Capture();
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(true);
        capture.m_GUI = gui;

        assertFalse(capture.init());
        assertFalse(capture.m_Error);
    }

    @Test
    public void testInit_menuChkThreadNotSelected() {
        Capture capture = new Capture();
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(false);
        capture.m_GUI = gui;

        assertTrue(capture.init());
    }

    @Test
    public void testInit_ErrorCheck() throws IOException {
        Capture capture = new Capture();
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(false);
        gui.strOutputDir = "/tmp";
        capture.m_GUI = gui;

        assertTrue(capture.init());
    }

    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();

        capture.run();
    }

    @Test
    public void testDV_Capture_existingOutputDir() throws IOException, InterruptedException {
        Capture capture = new Capture();
        GUI gui = new GUI();
        gui.strOutputDir = "/tmp";
        capture.m_GUI = gui;

        File outputDir = new File(gui.strOutputDir);
        outputDir.mkdir();

        ProcessBuilder processBuilder = new ProcessBuilder("touch", gui.strOutputDir + "/log/dvgrab.log");
        Process process = processBuilder.start();
        process.waitFor(5, TimeUnit.SECONDS);

        capture.DV_Capture();

        assertFalse(capture.m_GUI.blnBegin);
        assertTrue(capture.m_Error);

        outputDir.delete();
    }

    @Test
    public void testDV_Capture_existingOutputDirFile() throws IOException, InterruptedException {
        Capture capture = new Capture();
        GUI gui = new GUI();
        gui.strOutputDir = "/tmp";
        capture.m_GUI = gui;

        File outputDir = new File(gui.strOutputDir);
        outputDir.mkdir();

        ProcessBuilder processBuilder = new ProcessBuilder("touch", gui.strOutputDir);
        Process process = processBuilder.start();
        process.waitFor(5, TimeUnit.SECONDS);

        capture.DV_Capture();

        assertFalse(capture.m_GUI.blnBegin);

        outputDir.delete();
    }

    @Test
    public void testDV_Capture_success() throws IOException, InterruptedException {
        Capture capture = new Capture();
        GUI gui = new GUI();
        gui.strOutputDir = "/tmp";
        capture.m_GUI = gui;
        capture.m_Minutes = 1;
        capture.m_Seconds = 30;

        File outputDir = new File(gui.strOutputDir);
        outputDir.mkdir();

        ProcessBuilder processBuilder = new ProcessBuilder("touch", gui.strOutputDir + "/log/dvgrab.log");
        Process process = processBuilder.start();
        process.waitFor(5, TimeUnit.SECONDS);

        capture.DV_Capture();

        assertTrue(capture.m_GUI.blnBegin);

        outputDir.delete();
    }
}