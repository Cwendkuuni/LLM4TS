import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import javax.swing.JFrame;

public class CaptureTest {
  
  private Capture c;
  
  @Before
  public void setUp() {
    c = new Capture();
    JFrame frame = new JFrame();
    GUI gui = new GUI(frame);
    c.init();
  }
  
  @After
  public void tearDown() {
    File outputDir = new File(c.m_GUI.strOutputDir);
    deleteDirectory(outputDir);
  }
  
  @Test
  public void testDV_Capture_FileAlreadyExists_DeleteConfirmationIsYes_CorrectDirectoriesAndFilesCreated() throws IOException {
    String outputDir = c.m_GUI.strOutputDir + "/DVD";
    String logDir = c.m_GUI.strOutputDir + "/log";
    String dvDir = c.m_GUI.strOutputDir + "/dv";
    
    assertFalse(new File(outputDir).exists());
    assertFalse(new File(logDir).exists());
    assertFalse(new File(dvDir).exists());
    
    File createOutDir = new File(c.m_GUI.strOutputDir);
    createOutDir.mkdir();
    
    c.DV_Capture();
    
    assertTrue(new File(outputDir).exists());
    assertTrue(new File(logDir).exists());
    assertTrue(new File(dvDir).exists());
  }
  
  @Test
  public void testDV_Capture_FileAlreadyExists_DeleteConfirmationIsNo_ErrorMessageBoxDisplayed() throws IOException {
    String outputDir = c.m_GUI.strOutputDir + "/DVD";
    String logDir = c.m_GUI.strOutputDir + "/log";
    String dvDir = c.m_GUI.strOutputDir + "/dv";
    
    assertFalse(new File(outputDir).exists());
    assertFalse(new File(logDir).exists());
    assertFalse(new File(dvDir).exists());
    
    File createOutDir = new File(c.m_GUI.strOutputDir);
    createOutDir.mkdir();
    
    c.DV_Capture();
    
    assertFalse(new File(outputDir).exists());
    assertFalse(new File(logDir).exists());
    assertFalse(new File(dvDir).exists());
    assertFalse(c.m_GUI.blnBegin);
    assertTrue(c.m_Error);
  }

  @Test
  public void testDV_Capture_FileAlreadyExistsAndIsNotDirectory_DeleteConfirmationIsYes_CorrectDirectoriesAndFilesCreated() throws IOException {
    String outputDir = c.m_GUI.strOutputDir + "/DVD";
    String logDir = c.m_GUI.strOutputDir + "/log";
    String dvDir = c.m_GUI.strOutputDir + "/dv";
    
    assertFalse(new File(outputDir).exists());
    assertFalse(new File(logDir).exists());
    assertFalse(new File(dvDir).exists());
    
    File createOutDir = new File(c.m_GUI.strOutputDir);
    createOutDir.mkdir();
    File file = new File(c.m_GUI.strOutputDir + "/file");
    file.createNewFile();
    
    c.DV_Capture();
    
    assertTrue(new File(outputDir).exists());
    assertTrue(new File(logDir).exists());
    assertTrue(new File(dvDir).exists());
  }
  
  @Test
  public void testDV_Capture_CamcorderNotConnected_ErrorMessageBoxDisplayed() throws IOException {
    File createOutDir = new File(c.m_GUI.strOutputDir);
    createOutDir.mkdir();
    
    c.DV_Capture();
    
    assertFalse(c.m_GUI.blnBegin);
    assertTrue(c.m_Error);
  }
  
  @Test
  public void testDV_Capture_CommandNotFound_ErrorMessageBoxDisplayed() throws IOException {
    File createOutDir = new File(c.m_GUI.strOutputDir);
    createOutDir.mkdir();
    
    String[] dvgrab_cmd = {"/bin/sh", "-c", "command_not_found"};
    c.m_dvgrab = "command_not_found";
    
    c.DV_Capture();
    
    assertFalse(c.m_GUI.blnBegin);
    assertTrue(c.m_Error);
  }
  
  private void deleteDirectory(File dir) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          deleteDirectory(file);
        } else {
          file.delete();
        }
      }
    }
    dir.delete();
  }
}