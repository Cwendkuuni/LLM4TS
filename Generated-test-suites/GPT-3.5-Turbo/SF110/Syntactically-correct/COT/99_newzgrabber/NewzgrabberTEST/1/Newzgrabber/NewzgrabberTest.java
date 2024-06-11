import static org.junit.Assert.*;
import org.junit.*;

public class NewzgrabberTest {
	
	@Test
	public void testSetupBasicSystem() {
		Newzgrabber.setupBasicSystem();
		// Check if Newzdirectory is not null
		assertNotNull(Newzgrabber.Newzdirectory);
	}
	
	@Test
	public void testFindDirectory() {
		String classfile = "Newz.jar";
		String directory = Newzgrabber.findDirectory(classfile);
		// Check if directory is not null
		assertNotNull(directory);
	}
	
	@Test
	public void testUpdateIni() {
		String inifile = "Newzgrabber.ini";
		String prop = "Messageids";
		String key = "group";
		String value = "100";
		Newzgrabber.newzgrabber = new Newzgrabber();
		
		Newzgrabber.newzgrabber.updateIni(inifile, prop, key, value);
		// Test if the value is updated in the ini file
		assertEquals(value, Newzgrabber.newzgrabber.getIniValue(inifile, prop, key));
	}
	
	@Test
	public void testWindowClosing() {
		Newzgrabber.newzgrabber = new Newzgrabber();
		Newzgrabber.newzgrabber.windowClosing(null);
		
		// Test if the application exits after the window is closed
		assertFalse(Newzgrabber.newzgrabber.isVisible());
	}
}