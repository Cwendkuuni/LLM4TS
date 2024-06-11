import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class jgaapGUITest {
	
	private jgaapGUI gui;
	
	@Before
	public void setup() {
		gui = new jgaapGUI();
	}
	
	@After
	public void teardown() {
		gui = null;
	}
	
	@Test
	public void testActionPerformed() {
		// Create ActionEvent and set ActionCommand to "Exit"
		ActionEvent event = new ActionEvent(new Object(), 1, "Exit");
		
		// Call actionPerformed method
		gui.actionPerformed(event);
		
		// Assert that System.exit(-1) is called
		
	}
	
	@Test
	public void testLoadDemo() {
		// Call loadDemo method
		
		// Assert that "Need to Demo" message is printed
		
	}
	
	// Add additional test methods for other methods in jgaapGUI class
	
}