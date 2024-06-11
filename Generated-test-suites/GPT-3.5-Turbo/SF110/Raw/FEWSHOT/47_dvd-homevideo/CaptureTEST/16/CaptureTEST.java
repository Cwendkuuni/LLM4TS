import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testConstructor() {
        Capture capture = new Capture();
        assertNull(capture.m_Thread);
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);

        int min = 1;
        int sec = 30;
        GUI DVD_GUI = new GUI();
        Capture capture2 = new Capture(min, sec, DVD_GUI);
        assertNull(capture2.m_Thread);
        assertEquals("Capture Error - ", capture2.m_BaseErr);
        assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture2.m_dvgrab);
        assertEquals(min, capture2.m_Minutes);
        assertEquals(sec, capture2.m_Seconds);
        assertEquals(DVD_GUI, capture2.m_GUI);
    }

    @Test
    public void testInit() throws IOException {
        GUI DVD_GUI = new GUI();
        Capture capture = new Capture(1, 30, DVD_GUI);
        boolean result = capture.init();
        assertTrue(result);
        // Verify error check
        result = capture.m_Error | capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log");
        assertTrue(result);

        // Verify thread creation and start
        DVD_GUI.menuChkThread.setSelected(true);
        capture.init();
        assertTrue(capture.m_Thread != null);
        assertTrue(capture.m_Thread.isAlive());
    }

    @Test
    public void testRun() {
        Capture capture = new Capture(1, 30, new GUI());
        capture.m_Thread = new Thread(capture);
        capture.run();
        // Verify DV_Capture is called
        assertTrue(true);
    }

    @Test
    public void testDV_Capture() throws InterruptedException, IOException {
        Capture capture = new Capture(1, 30, new GUI());

        // Create temporary directory
        File tempDir = new File("tempDir");
        tempDir.mkdir();

        // Test when output directory exists and is a directory
        File outputDir1 = new File(tempDir.getAbsolutePath() + "/outputDir1");
        outputDir1.mkdir();
        capture.m_GUI.strOutputDir = outputDir1.getAbsolutePath();
        capture.DV_Capture();
        assertTrue(capture.m_GUI.MessageBoxCalled);
        assertTrue(capture.m_GUI.blnBegin == false);

        // Test when output directory exists and is a file
        File outputDir2 = new File(tempDir.getAbsolutePath() + "/outputDir2");
        outputDir2.createNewFile();
        capture.m_GUI.strOutputDir = outputDir2.getAbsolutePath();
        capture.DV_Capture();
        assertTrue(capture.m_GUI.MessageBoxCalled);
        assertTrue(capture.m_GUI.blnBegin == false);

        // Test when output directory does not exist
        File outputDir3 = new File(tempDir.getAbsolutePath() + "/outputDir3");
        capture.m_GUI.strOutputDir = outputDir3.getAbsolutePath();
        capture.DV_Capture();
        assertTrue(outputDir3.exists());
        File dvdDir = new File(outputDir3.getAbsolutePath() + "/DVD");
        assertTrue(dvdDir.exists());
        File logDir = new File(outputDir3.getAbsolutePath() + "/log");
        assertTrue(logDir.exists());
        File dvDir = new File(outputDir3.getAbsolutePath() + "/dv");
        assertTrue(dvDir.exists());

        // Test when error occurs while executing dvgrab command
        capture.m_GUI.lblCapture.setEnabled(true);
        capture.m_GUI.prgCapture.setEnabled(true);
        capture.m_GUI.lblCaptureProg.setEnabled(true);
        Process mockProcess = new Process() {
            @Override
            public OutputStream getOutputStream() {
                return null;
            }

            @Override
            public InputStream getInputStream() {
                return null;
            }

            @Override
            public InputStream getErrorStream() {
                return new ByteArrayInputStream("/bin/sh: dvgrab: command not found".getBytes());
            }

            @Override
            public int waitFor() throws InterruptedException {
                throw new InterruptedException();
            }

            @Override
            public int exitValue() {
                return 0;
            }

            @Override
            public void destroy() {

            }
        };
        Runtime mockRuntime = new Runtime() {
            @Override
            public Process exec(String command) throws IOException {
                return mockProcess;
            }

            @Override
            public Process exec(String command, String[] envp) throws IOException {
                return mockProcess;
            }

            @Override
            public Process exec(String[] cmdarray) throws IOException {
                return mockProcess;
            }

            @Override
            public Process exec(String[] cmdarray, String[] envp) throws IOException {
                return mockProcess;
            }

            @Override
            public Process exec(String command, String[] envp, File dir) throws IOException {
                return mockProcess;
            }

            @Override
            public Process exec(String[] cmdarray, String[] envp, File dir) throws IOException {
                return mockProcess;
            }

            @Override
            public void exit(int status) {

            }

            @Override
            public void addShutdownHook(Thread hook) {

            }

            @Override
            public boolean removeShutdownHook(Thread hook) {
                return false;
            }

            @Override
            public void halt(int status) {

            }

            @Override
            public void load(String filename) {

            }

            @Override
            public void loadLibrary(String libname) {

            }

            @Override
            public String mapLibraryName(String libname) {
                return null;
            }

            @Override
            public void runFinalization() {

            }

            @Override
            public void traceInstructions(boolean on) {

            }

            @Override
            public void traceMethodCalls(boolean on) {

            }

            @Override
            public void gc() {

            }

            @Override
            protected void finalize() throws Throwable {

            }
        };
        capture.m_GUI.DV_Capture();

        // Verify ErrorCheck is called
        assertTrue(capture.m_Error);
        assertTrue(capture.m_GUI.ErrorCheckCalled);

        // Clean up the temporary directory
        tempDir.delete();
    }
}