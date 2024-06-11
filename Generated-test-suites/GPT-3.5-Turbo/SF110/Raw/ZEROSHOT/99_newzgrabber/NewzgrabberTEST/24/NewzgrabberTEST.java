package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class NewzgrabberTest {

  @Test
  public void testNewzgrabber() {
    Newzgrabber n = new Newzgrabber();
    assertNotNull(n);
  }
  
  @Test
  public void testNewzgrabberString() {
    Newzgrabber n = new Newzgrabber("Newzgrabber");
    assertNotNull(n);
  }
  
  @Test
  public void testSetupBasicSystem() {
    Newzgrabber.setupBasicSystem();
    // add assertions here
  }
  
  @Test
  public void testWindowClosed() {
    Newzgrabber n = new Newzgrabber();
    n.windowClosed(null);
    // add assertions here
  }
  
  @Test
  public void testWindowOpened() {
    Newzgrabber n = new Newzgrabber();
    n.windowOpened(null);
    // add assertions here
  }
  
  @Test
  public void testWindowActivated() {
    Newzgrabber n = new Newzgrabber();
    n.windowActivated(null);
    // add assertions here
  }
  
  @Test
  public void testWindowDeactivated() {
    Newzgrabber n = new Newzgrabber();
    n.windowDeactivated(null);
    // add assertions here
  }
  
  @Test
  public void testWindowIconified() {
    Newzgrabber n = new Newzgrabber();
    n.windowIconified(null);
    // add assertions here
  }
  
  @Test
  public void testWindowDeiconified() {
    Newzgrabber n = new Newzgrabber();
    n.windowDeiconified(null);
    // add assertions here
  }
  
  @Test
  public void testWindowClosing() {
    Newzgrabber n = new Newzgrabber();
    n.windowClosing(null);
    // add assertions here
  }
  
  @Test
  public void testFindDirectory() {
    String directory = Newzgrabber.findDirectory("Newz.jar");
    assertNotNull(directory);
  }
  
  @Test
  public void testUpdateIni() {
    Newzgrabber n = new Newzgrabber();
    n.updateIni("iniFile", "prop", "key", "value");
    // add assertions here
  }
  
  @Test
  public void testMain() {
    Newzgrabber.main(new String[] {});
    // add assertions here
  }
}