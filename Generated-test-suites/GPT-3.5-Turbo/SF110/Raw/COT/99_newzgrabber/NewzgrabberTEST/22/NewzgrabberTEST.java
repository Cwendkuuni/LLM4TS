import static org.junit.Assert.*;
import org.junit.Test;

public class NewzgrabberTest {
  @Test
  public void testNewzgrabber() {
    Newzgrabber ng = new Newzgrabber();
    
    // Test asserts here
  }
  
  @Test
  public void testNewzgrabberString() {
    String s = "Newzgrabber";
    Newzgrabber ng = new Newzgrabber(s);
    
    // Test asserts here
  }
  
  @Test
  public void testSetupBasicSystem() {
    Newzgrabber.setupBasicSystem();
    
    // Test asserts here
  }
  
  @Test
  public void testWindowClosed() {
    Newzgrabber ng = new Newzgrabber();
    ng.windowClosed(null);
    
    // Test asserts here
  }
  
  @Test
  public void testWindowOpened() {
    Newzgrabber ng = new Newzgrabber();
    ng.windowOpened(null);
    
    // Test asserts here
  }
  
  @Test
  public void testWindowActivated() {
    Newzgrabber ng = new Newzgrabber();
    ng.windowActivated(null);
    
    // Test asserts here
  }
  
  @Test
  public void testWindowDeactivated() {
    Newzgrabber ng = new Newzgrabber();
    ng.windowDeactivated(null);
    
    // Test asserts here
  }
  
  @Test
  public void testWindowIconified() {
    Newzgrabber ng = new Newzgrabber();
    ng.windowIconified(null);
    
    // Test asserts here
  }
  
  @Test
  public void testWindowDeiconified() {
    Newzgrabber ng = new Newzgrabber();
    ng.windowDeiconified(null);
    
    // Test asserts here
  }
  
  @Test
  public void testWindowClosing() {
    Newzgrabber ng = new Newzgrabber();
    ng.windowClosing(null);
    
    // Test asserts here
  }
  
  @Test
  public void testFindDirectory() {
    String classfile = "Newzgrabber.class";
    String directory = Newzgrabber.findDirectory(classfile);
    
    // Test asserts here
  }
  
  @Test
  public void testUpdateIni() {
    Newzgrabber ng = new Newzgrabber();
    ng.updateIni("inifile", "prop", "key", "value");
    
    // Test asserts here
  }
  
  @Test
  public void testMain() {
    String[] args = { "-verbose", "-batch", "-debug" };
    Newzgrabber.main(args);
    
    // Test asserts here
  }
}