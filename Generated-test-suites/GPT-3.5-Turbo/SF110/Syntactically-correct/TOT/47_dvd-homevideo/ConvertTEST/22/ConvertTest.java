import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

	@Test
	public void testInit() {
		// Typical Use Case
		Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
		convert.init();
		assertFalse(convert.init());
		
		// Edge Case - Quality 0 (minimum value)
		Convert convert1 = new Convert(0, "dvd-ntsc", "4:3", new GUI());
		convert1.init();
		assertFalse(convert1.init());
		
		// Edge Case - Quality 2 (maximum value)
		Convert convert2 = new Convert(2, "dvd-ntsc", "4:3", new GUI());
		convert2.init();
		assertFalse(convert2.init());
		
		// Error Scenario - Invalid format
		Convert convert3 = new Convert("invalid-format", "4:3", new GUI());
		convert3.init();
		assertTrue(convert3.init());
	}

	@Test
	public void testRun() {
		Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
		convert.run();
	}

	@Test
	public void testTranscode() {
		Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
		convert.Transcode();
	}

	@Test
	public void testMatrix() {
		Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
		convert.matrix();
	}

}