import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class FSPathResultTest {
	
	private File testFile;
	private String testString;
	private Double testDouble;
	private Date testDate;
	private URI testURI;
	private Boolean testBoolean;
	private FSPathResult fsPathResult;
	
	@Before
	public void setup() {
		testFile = new File("test.txt");
		testString = "test";
		testDouble = 3.14;
		testDate = new Date();
		testURI = URI.create("http://example.com");
		testBoolean = true;
	}
	
	@Test
	public void testGetFile() {
		fsPathResult = new FSPathResult(testFile);
		assertEquals(testFile, fsPathResult.getFile());
	}
	
	@Test
	public void testGetString() {
		fsPathResult = new FSPathResult(testString);
		assertEquals(testString, fsPathResult.getString());
	}
	
	@Test
	public void testGetDouble() {
		fsPathResult = new FSPathResult(testDouble);
		assertEquals(testDouble, fsPathResult.getDouble());
	}
	
	@Test
	public void testGetDate() {
		fsPathResult = new FSPathResult(testDate);
		assertEquals(testDate, fsPathResult.getDate());
	}
	
	@Test
	public void testGetURI() {
		fsPathResult = new FSPathResult(testURI);
		assertEquals(testURI, fsPathResult.getURI());
	}
	
	@Test
	public void testGetBoolean() {
		fsPathResult = new FSPathResult(testBoolean);
		assertEquals(testBoolean, fsPathResult.getBoolean());
	}
	
	@Test
	public void testToStringWithFile() {
		fsPathResult = new FSPathResult(testFile);
		assertEquals(testFile.getAbsolutePath(), fsPathResult.toString());
	}
	
	@Test
	public void testToStringWithString() {
		fsPathResult = new FSPathResult(testString);
		assertEquals(testString, fsPathResult.toString());
	}
	
	@Test
	public void testToStringWithDouble() {
		fsPathResult = new FSPathResult(testDouble);
		assertEquals(testDouble.toString(), fsPathResult.toString());
	}
	
	@Test
	public void testToStringWithBoolean() {
		fsPathResult = new FSPathResult(testBoolean);
		assertEquals(testBoolean.toString(), fsPathResult.toString());
	}
	
	@Test
	public void testToStringWithDate() {
		fsPathResult = new FSPathResult(testDate);
		assertEquals(testDate.toString(), fsPathResult.toString());
	}
	
	@Test
	public void testToStringWithURI() {
		fsPathResult = new FSPathResult(testURI);
		assertEquals(testURI.toString(), fsPathResult.toString());
	}
	
	@Test
	public void testToStringWithEmptyResult() {
		fsPathResult = new FSPathResult(null);
		assertEquals("FSPathResult : empty", fsPathResult.toString());
	}
	
}