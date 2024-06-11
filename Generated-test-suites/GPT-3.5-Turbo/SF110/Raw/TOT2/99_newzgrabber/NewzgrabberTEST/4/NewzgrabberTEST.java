import static org.junit.Assert.*;
import org.junit.Test;

public class NewzgrabberTest {
  
  @Test
  public void testNewzgrabberConstructor() {
    Newzgrabber.newzdirectory = "C:\\Newz";
    Newzgrabber.jarFilename = "Newz.jar";
    Newzgrabber.batchFilename = "batch.ini";
    Newzgrabber.newzInifilename = "Newzgrabber.ini";
    
    Newzgrabber newzgrabber = new Newzgrabber();
    
    assertNotNull(newzgrabber);
    assertTrue(Newzgrabber.isBatch);
  }
  
  @Test
  public void testSetupBasicSystem() {
    Newzgrabber.jarFilename = "Newz.jar";
    
    Newzgrabber.setupBasicSystem();
    
    assertNotNull(Newzgrabber.newzdirectory);
  }
  
  @Test
  public void testWindowClosing() {
    Newzgrabber optionspanel = new OptionsPanel();
    Newzgrabber groupspanel = new GroupsPanel();
    Newzgrabber transferpanel = new TransferPanel();
    
    Newzgrabber newzgrabber = new Newzgrabber();
    newzgrabber.windowClosing(null);
    
    assertNull(optionspanel.getInifile());
    assertNull(groupspanel.getInifile());
    assertNull(transferpanel.getInifile());
  }
  
  @Test
  public void testUpdateIni() {
    Newzgrabber newzgrabber = new Newzgrabber();
    String inifile = "test.ini";
    String prop = "test";
    String key = "testkey";
    String value = "testvalue";
    
    newzgrabber.updateIni(inifile, prop, key, value);
    
    // Add assertion here
  }
  
  @Test
  public void testFindDirectory() {
    String classfile = "Newz.jar";
    
    String directory = Newzgrabber.findDirectory(classfile);
    
    assertNotNull(directory);
  }
  
  @Test
  public void testMain() {
    String[] args = {"-verbose"};
    
    Newzgrabber.main(args);
    
    assertTrue(Newzgrabber.verbose);
  }
  
}