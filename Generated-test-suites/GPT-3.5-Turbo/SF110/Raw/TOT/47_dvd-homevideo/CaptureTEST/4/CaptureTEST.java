import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {
  
  private Capture capture;
  private GUI gui;
  
  @Before
  public void setup() {
    gui = new GUI();
    capture = new Capture(0, 0, gui);
  }
  
  @Test
  public void testInit_menuChkThreadSelected() {
    gui.setMenuChkThreadSelected(true);
    capture.init();
    assertTrue(capture.getThread().isAlive());
  }
  
  @Test
  public void testInit_menuChkThreadNotSelected() {
    gui.setMenuChkThreadSelected(false);
    capture.init();
    assertFalse(capture.getThread().isAlive());
    assertFalse(capture.isError());
  }
  
  @Test
  public void testInit_ErrorOutputCheck() {
    gui.setMenuChkThreadSelected(false);
    gui.setErrorCheck(true);
    capture.init();
    assertTrue(capture.isError());
  }
  
  @Test
  public void testRun() {
    capture.run();
    assertFalse(capture.isError());
  }
  
  @Test
  public void testDV_Capture_OutDirExists_DeleteDir_YES() {
    capture.setGUI(mockGUI());
    File outDirMock = mock(File.class);
    when(outDirMock.exists()).thenReturn(true);
    when(outDirMock.isDirectory()).thenReturn(true);
    when(capture.getGUI().MessageBox(anyString())).thenReturn(0);
    
    String expectedCmd = "rm -fr " + capture.getGUI().strOutputDir;
    String[] expectedCmds = { "/bin/sh", "-c", expectedCmd };
    
    Process processMock = mock(Process.class);
    when(Runtime.getRuntime().exec(expectedCmds, null)).thenReturn(processMock);
    when(processMock.waitFor()).thenReturn(0);
    
    capture.DV_Capture();
    verify(outDirMock).exists();
    verify(outDirMock).isDirectory();
    verify(capture.getGUI()).MessageBox("The Output Directory " + capture.getGUI().strOutputDir + " already exists!" + "\nWould you like to delete it?");
    verify(capture.getGUI()).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
    verify(capture.getGUI(), never()).MessageBox("Please choose a different name for your\ntitle or move the file to another location.", 2);
    verify(outDirMock).mkdir();
  }
  
  @Test
  public void testDV_Capture_OutDirExists_DeleteDir_NO() {
    capture.setGUI(mockGUI());
    File outDirMock = mock(File.class);
    when(outDirMock.exists()).thenReturn(true);
    when(outDirMock.isDirectory()).thenReturn(true);
    when(capture.getGUI().MessageBox(anyString())).thenReturn(1);
    
    String expectedCmd = "rm -fr " + capture.getGUI().strOutputDir;
    String[] expectedCmds = { "/bin/sh", "-c", expectedCmd };
    
    Process processMock = mock(Process.class);
    when(Runtime.getRuntime().exec(expectedCmds, null)).thenReturn(processMock);
    when(processMock.waitFor()).thenReturn(0);
    
    capture.DV_Capture();
    verify(outDirMock).exists();
    verify(outDirMock).isDirectory();
    verify(capture.getGUI()).MessageBox("The Output Directory " + capture.getGUI().strOutputDir + " already exists!" + "\nWould you like to delete it?");
    verify(capture.getGUI(), never()).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
    verify(outDirMock).mkdir();
  }
  
  @Test
  public void testDV_Capture_OutFileExists_DeleteNO() {
    capture.setGUI(mockGUI());
    File outDirMock = mock(File.class);
    when(outDirMock.exists()).thenReturn(true);
    when(outDirMock.isDirectory()).thenReturn(false);
    when(capture.getGUI().MessageBox(anyString())).thenReturn(1);
    
    String expectedCmd = "rm -fr " + capture.getGUI().strOutputDir;
    String[] expectedCmds = { "/bin/sh", "-c", expectedCmd };
    
    Process processMock = mock(Process.class);
    when(Runtime.getRuntime().exec(expectedCmds, null)).thenReturn(processMock);
    when(processMock.waitFor()).thenReturn(0);
    
    capture.DV_Capture();
    verify(outDirMock).exists();
    verify(outDirMock).isDirectory();
    verify(capture.getGUI()).MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?");
    verify(capture.getGUI(), never()).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
    verify(outDirMock).mkdir();
  }
  
  @Test
  public void testDV_Capture_OutDirDoesNotExist() {
    capture.setGUI(mockGUI());
    File outDirMock = mock(File.class);
    when(outDirMock.exists()).thenReturn(false);
    when(outDirMock.isDirectory()).thenReturn(true);
    
    capture.DV_Capture();
    verify(outDirMock).exists();
    verify(outDirMock).mkdir();
  }
  
  private GUI mockGUI() {
    GUI guiMock = mock(GUI.class);
    guiMock.setStrOutputDir("testOutputDir");
    guiMock.setMenuChkThreadSelected(false);
    guiMock.setErrorCheck(false);
    guiMock.setTxtAreaOutput(mock(TextArea.class));
    guiMock.setLblCapture(mock(Label.class));
    guiMock.setPrgCapture(mock(ProgressBar.class));
    guiMock.setLblCaptureProg(mock(Label.class));
    guiMock.setMessageBox(new MessageBox());
    return guiMock;
  }
}