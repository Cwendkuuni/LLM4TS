package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NewzgrabberTest {
	@Test
	public void testNewzgrabber() {
		Newzgrabber n1 = new Newzgrabber();
		assertNotNull(n1);
		assertTrue(n1.IsBatch);

		Newzgrabber n2 = new Newzgrabber("Newzgrabber");
		assertNotNull(n2);
		assertFalse(n2.IsBatch);
	}

	@Test
	public void testSetupBasicSystem() {
		Newzgrabber.setupBasicSystem();
		String newzdirectory = System.getProperty("user.dir");
		assertEquals(newzdirectory, Newzgrabber.Newzdirectory);
	}

	@Test
	public void testWindowClosing() {
		Newzgrabber ng = new Newzgrabber();
		ng.windowClosing(null);

		assertTrue(ng.IsBatch);
	}

	@Test
	public void testFindDirectory() {
		String directory = Newzgrabber.findDirectory("Newz.jar");
		assertNotNull(directory);
	}

	@Test
	public void testUpdateIni() {
		Newzgrabber ng = new Newzgrabber();
		ng.updateIni("inifile", "prop", "key", "value");
		// assertions
	}

	@Test
	public void testMain() {
		Newzgrabber.main(new String[] {});
		Newzgrabber.main(new String[] { "-verbose" });
		Newzgrabber.main(new String[] { "-batch" });
		Newzgrabber.main(new String[] { "-debug" });
		Newzgrabber.main(new String[] { "-batch" });
	}

	@Test
	public void testStripLeadingAndTrailingQuotes() {
		String str1 = Newzgrabber.stripLeadingAndTrailingQuotes("\"foo\"");
		assertEquals("foo", str1);

		String str2 = Newzgrabber.stripLeadingAndTrailingQuotes("foo \"bar\"");
		assertEquals("foo \"bar\"", str2);

		String str3 = Newzgrabber.stripLeadingAndTrailingQuotes("\"foo\" bar");
		assertEquals("\"foo\" bar", str3);

		String str4 = Newzgrabber.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\"");
		assertEquals("\"foo\" and \"bar\"", str4);

		String str5 = Newzgrabber.stripLeadingAndTrailingQuotes("");
		assertEquals("", str5);
	}

	@Test
	public void testStripLeadingHyphens() {
		String str1 = Newzgrabber.stripLeadingHyphens("-f");
		assertEquals("f", str1);

		String str2 = Newzgrabber.stripLeadingHyphens("--foo");
		assertEquals("foo", str2);

		String str3 = Newzgrabber.stripLeadingHyphens("---foo");
		assertEquals("-foo", str3);

		assertNull(Newzgrabber.stripLeadingHyphens(null));
	}
}