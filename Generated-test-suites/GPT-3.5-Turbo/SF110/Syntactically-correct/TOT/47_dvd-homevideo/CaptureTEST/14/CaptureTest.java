import static org.junit.Assert.*;
import org.junit.Test;

public class CaptureTest {

  @Test
  public void testInit() {
    // Typical use case
    Capture capture = new Capture();
    capture.init();

    // Edge case
    int minutes = 25;
    int seconds = 61;
    GUI gui = new GUI();
    Capture capture2 = new Capture(minutes, seconds, gui);
    capture2.init();

    // Error scenario
    GUI gui2 = new GUI();
    gui2.menuChkThread.setSelected(true);
    Capture capture3 = new Capture(10, 10, gui2);
    capture3.init();
  }

  @Test
  public void testRun() {
    Capture capture = new Capture();
    capture.run();

    // Edge case
    int minutes = 59;
    int seconds = 59;
    GUI gui = new GUI();
    Capture capture2 = new Capture(minutes, seconds, gui);
    capture2.run();
  }

  @Test
  public void testDV_Capture() {
    Capture capture = new Capture();

    // Typical use case
    capture.DV_Capture();

    // Edge case
    GUI gui = new GUI();
    gui.strOutputDir = "/existing/directory";
    Capture capture2 = new Capture(0, 0, gui);
    capture2.DV_Capture();

    // Error scenario
    GUI gui2 = new GUI();
    gui2.strOutputDir = "/existing/file";
    Capture capture3 = new Capture(0, 0, gui2);
    capture3.DV_Capture();
  }
}