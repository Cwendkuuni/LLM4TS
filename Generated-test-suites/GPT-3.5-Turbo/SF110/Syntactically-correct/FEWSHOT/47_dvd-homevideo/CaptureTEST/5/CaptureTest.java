import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class CaptureTest {
    
    private Capture capture;
    private GUI guiMock;
    
    @Before
    public void setUp() {
        capture = new Capture();
        guiMock = mock(GUI.class);
        capture.m_GUI = guiMock;
    }
    
    @Test
    public void testConstructor() {
        Capture capture2 = new Capture(5, 30, guiMock);
        assertEquals(5, capture2.m_Minutes);
        assertEquals(30, capture2.m_Seconds);
        assertEquals(guiMock, capture2.m_GUI);
    }
    
    @Test
    public void testInit_menuChkThreadSelected() {
        when(guiMock.menuChkThread.isSelected()).thenReturn(true);
        Thread threadMock = mock(Thread.class);
        capture.m_Thread = threadMock;
        
        boolean result = capture.init();
        
        verify(threadMock).setPriority(10);
        verify(threadMock).start();
        assertEquals(false, result);
    }
    
    @Test
    public void testInit_menuChkThreadNotSelected_ErrorInGuiErrorCheck() throws IOException {
        when(guiMock.menuChkThread.isSelected()).thenReturn(false);
        when(guiMock.ErrorCheck(anyString())).thenReturn(true);
        
        boolean result = capture.init();
        
        verify(guiMock).ErrorCheck(anyString());
        assertEquals(true, result);
    }
    
    @Test
    public void testInit_menuChkThreadNotSelected_NoErrorInGuiErrorCheck() throws IOException {
        when(guiMock.menuChkThread.isSelected()).thenReturn(false);
        when(guiMock.ErrorCheck(anyString())).thenReturn(false);
        
        boolean result = capture.init();
        
        verify(guiMock).ErrorCheck(anyString());
        assertEquals(false, result);
    }
    
    @Test
    public void testRun() {
        Capture captureSpy = spy(capture);
        
        captureSpy.run();
        
        verify(captureSpy).DV_Capture();
    }
    
    @Test
    public void testDV_Capture_CreateOutDirExistsAndIsDirectory_DeleteDirectory() throws IOException, InterruptedException {
        File fileMock = mock(File.class);
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isDirectory()).thenReturn(true);
        whenNew(File.class).withArguments(anyString()).thenReturn(fileMock);
        when(guiMock.MessageBox(anyString(), anyInt())).thenReturn(0);
        Process processMock = mock(Process.class);
        when(processMock.waitFor()).thenReturn(0);
        when(Runtime.getRuntime().exec(any(String[].class), any(String[].class), any(File.class))).thenReturn(processMock);
        
        capture.DV_Capture();
        
        verify(guiMock).MessageBox(anyString(), anyInt());
        verify(processMock, atLeastOnce()).waitFor();
        verify(fileMock, atLeastOnce()).mkdir();
        verify(fileMock).exists();
        verify(fileMock).isDirectory();
        verify(fileMock).delete();
    }
    
    @Test
    public void testDV_Capture_CreateOutDirExistsAndNotDirectory_DoNothing() throws IOException, InterruptedException {
        File fileMock = mock(File.class);
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isDirectory()).thenReturn(false);
        whenNew(File.class).withArguments(anyString()).thenReturn(fileMock);
        
        capture.DV_Capture();
        
        verify(fileMock).exists();
        verify(fileMock).isDirectory();
        verify(fileMock, never()).delete();
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(Runtime.getRuntime(), never()).exec(any(String[].class), any(String[].class), any(File.class));
    }
    
    @Test
    public void testDV_Capture_CreateOutDirDoesNotExist_CreateDirectories() throws IOException, InterruptedException {
        File fileMock = mock(File.class);
        when(fileMock.exists()).thenReturn(false);
        whenNew(File.class).withArguments(anyString()).thenReturn(fileMock);
        when(fileMock.mkdir()).thenReturn(true);
        Process processMock = mock(Process.class);
        when(processMock.waitFor()).thenReturn(0);
        when(Runtime.getRuntime().exec(any(String[].class), any(String[].class), any(File.class))).thenReturn(processMock);
        
        capture.DV_Capture();
        
        verify(fileMock, atLeastOnce()).mkdir();
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(Runtime.getRuntime()).exec(any(String[].class), any(String[].class), any(File.class));
        verify(processMock).waitFor();
    }
    
    @Test
    public void testDV_Capture_ExceptionOccursInDeleteDirectory_PrintStackTrace() throws IOException, InterruptedException {
        File fileMock = mock(File.class);
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isDirectory()).thenReturn(true);
        whenNew(File.class).withArguments(anyString()).thenReturn(fileMock);
        when(guiMock.MessageBox(anyString(), anyInt())).thenReturn(0);
        Process processMock = mock(Process.class);
        when(processMock.waitFor()).thenThrow(IOException.class);
        when(Runtime.getRuntime().exec(any(String[].class), any(String[].class), any(File.class))).thenReturn(processMock);
        
        capture.DV_Capture();
        
        verify(guiMock).MessageBox(anyString(), anyInt());
        verify(processMock).waitFor();
        verify(fileMock, atLeastOnce()).delete();
    }
    
    @Test
    public void testDV_Capture_ExceptionOccursInCreateDirectories_PrintStackTrace() throws IOException, InterruptedException {
        File fileMock = mock(File.class);
        when(fileMock.exists()).thenReturn(false);
        whenNew(File.class).withArguments(anyString()).thenReturn(fileMock);
        when(fileMock.mkdir()).thenThrow(IOException.class);
        
        capture.DV_Capture();
        
        verify(fileMock, atLeastOnce()).mkdir();
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(Runtime.getRuntime(), never()).exec(any(String[].class), any(String[].class), any(File.class));
    }
    
    @Test
    public void testDV_Capture_ExceptionOccursInProcessRuntimeExec_PrintStackTrace() throws IOException, InterruptedException {
        File fileMock = mock(File.class);
        when(fileMock.exists()).thenReturn(false);
        whenNew(File.class).withArguments(anyString()).thenReturn(fileMock);
        Process processMock = mock(Process.class);
        when(Runtime.getRuntime().exec(any(String[].class), any(String[].class), any(File.class))).thenThrow(IOException.class);
        
        capture.DV_Capture();
        
        verify(fileMock, atLeastOnce()).mkdir();
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(Runtime.getRuntime()).exec(any(String[].class), any(String[].class), any(File.class));
    }
    
    @Test
    public void testDV_Capture_ExceptionOccursInProcessWaitFor_PrintStackTrace() throws IOException, InterruptedException {
        File fileMock = mock(File.class);
        when(fileMock.exists()).thenReturn(false);
        whenNew(File.class).withArguments(anyString()).thenReturn(fileMock);
        Process processMock = mock(Process.class);
        when(Runtime.getRuntime().exec(any(String[].class), any(String[].class), any(File.class))).thenReturn(processMock);
        when(processMock.waitFor()).thenThrow(InterruptedException.class);
        
        capture.DV_Capture();
        
        verify(fileMock, atLeastOnce()).mkdir();
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(Runtime.getRuntime()).exec(any(String[].class), any(String[].class), any(File.class));
        verify(processMock).waitFor();
    }
}