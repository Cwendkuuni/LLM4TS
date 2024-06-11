import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.Before;
import org.junit.Test;

public class CaptureTest {

    private Capture capture;
    private GUI gui;

    @Before
    public void setUp() {
        gui = mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = gui;
        capture.m_Minutes = 1;
        capture.m_Seconds = 30;
        capture.m_GUI.strOutputDir = "/test/dir";
    }

    @Test
    public void testInit_withThreadSelected() {
        capture.m_GUI.menuChkThread = mock(MenuItem.class);
        when(gui.ErrorCheck(anyString())).thenReturn(true);

        boolean result = capture.init();

        verify(capture.m_Thread).start();
        assertEquals(false, result);
    }

    @Test
    public void testInit_withoutThreadSelected() {
        capture.m_GUI.menuChkThread = mock(MenuItem.class);
        when(gui.ErrorCheck(anyString())).thenReturn(true);
        capture.m_Thread = mock(Thread.class);

        boolean result = capture.init();

        verify(capture.m_Thread, never()).start();
        capture.DV_Capture();
        assertEquals(true, result);
    }

    @Test
    public void testDV_Capture_directoryAlreadyExists() throws IOException {
        capture.m_GUI.MessageBox("The Output Directory " + capture.m_GUI.strOutputDir + " already exists!\nWould you like to delete it?", 0);
        File createOutDir = mock(File.class);
        when(createOutDir.exists()).thenReturn(true);
        when(createOutDir.isDirectory()).thenReturn(true);
        when(capture.m_GUI.MessageBox(anyString(), anyInt())).thenReturn(0);

        capture.DV_Capture();

        verifyRuntimeExec("rm -fr /test/dir");
        verify(capture.m_GUI).MessageBox("Error executing Runtime.getRuntime().exec()\njava.io.IOException: null", 0);
    }

    @Test
    public void testDV_Capture_directoryAlreadyExists_cancelDelete() throws IOException {
        capture.m_GUI.MessageBox("The Output Directory " + capture.m_GUI.strOutputDir + " already exists!\nWould you like to delete it?", 0);
        File createOutDir = mock(File.class);
        when(createOutDir.exists()).thenReturn(true);
        when(createOutDir.isDirectory()).thenReturn(true);
        when(capture.m_GUI.MessageBox(anyString(), anyInt())).thenReturn(1);

        capture.DV_Capture();

        verify(capture.m_GUI).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
        assertEquals(false, capture.m_Error);
    }

    @Test
    public void testDV_Capture_fileWithDirectoryNameExists() throws IOException {
        capture.m_GUI.MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?", 0);
        File createOutDir = mock(File.class);
        when(createOutDir.exists()).thenReturn(true);
        when(createOutDir.isDirectory()).thenReturn(false);
        when(capture.m_GUI.MessageBox(anyString(), anyInt())).thenReturn(0);

        capture.DV_Capture();

        verifyRuntimeExec("rm -fr /test/dir");
        verify(capture.m_GUI).MessageBox("Error executing Runtime.getRuntime().exec()\njava.io.IOException: null", 0);
    }

    @Test
    public void testDV_Capture_fileWithDirectoryNameExists_cancelDelete() throws IOException {
        capture.m_GUI.MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?", 0);
        File createOutDir = mock(File.class);
        when(createOutDir.exists()).thenReturn(true);
        when(createOutDir.isDirectory()).thenReturn(false);
        when(capture.m_GUI.MessageBox(anyString(), anyInt())).thenReturn(1);

        capture.DV_Capture();

        verify(capture.m_GUI).MessageBox("Please choose a different name for your\ntitle or move the file to another location.", 2);
        assertEquals(false, capture.m_GUI.blnBegin);
    }

    @Test
    public void testDV_Capture_directoryDoesNotExist() throws IOException {
        File createOutDir = mock(File.class);
        when(createOutDir.exists()).thenReturn(false);
        when(createOutDir.mkdir()).thenReturn(true);

        capture.DV_Capture();

        verify(createOutDir, times(3)).mkdir();
        verifyRuntimeExec("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-");
        verify(capture.m_GUI).lblCapture.setEnabled(true);
        verify(capture.m_GUI).prgCapture.setEnabled(true);
        verify(capture.m_GUI).lblCaptureProg.setEnabled(true);
        verify(capture.m_GUI).prgCapture.setValue(100);
        verify(capture.m_GUI, times(2)).MessageBox(anyString(), anyInt());
        verify(capture.m_GUI, times(2)).ErrorMsg(anyString());
    }

    @Test
    public void testDV_Capture_errorOccurred() throws IOException {
        File createOutDir = mock(File.class);
        when(createOutDir.exists()).thenReturn(false);
        when(createOutDir.mkdir()).thenReturn(true);

        capture.DV_Capture();

        verify(capture.m_GUI).ErrorMsg("Capture Error - IO Error\njava.io.IOException: null");
        assertEquals(true, capture.m_Error);
    }

    private void verifyRuntimeExec(String command) throws IOException {
        Runtime runtime = mock(Runtime.class);
        Process process = mock(Process.class);

        when(runtime.exec(any(String[].class), any(String[].class), any(File.class))).thenReturn(process);
        when(command.split("\\s")).thenReturn(new String[] { "/bin/sh", "-c", command });
    }

}