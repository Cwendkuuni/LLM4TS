import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OptionBuilderTest {

  	@Test
  	public void testWithLongOpt() {
		OptionBuilder.withLongOpt("testOpt");
		Option option = OptionBuilder.create();
		
		assertEquals("testOpt", option.getLongOpt());
	}

	@Test
	public void testHasArg() {
		OptionBuilder.hasArg();
		Option option = OptionBuilder.create();
		
		assertEquals(1, option.getArgs());
	}

	@Test
	public void testHasArgBoolean() {
		OptionBuilder.hasArg(true);
		Option option = OptionBuilder.create();
		
		assertEquals(1, option.getArgs());
		
		OptionBuilder.reset();
		
		OptionBuilder.hasArg(false);
		option = OptionBuilder.create();
		
		assertEquals(-1, option.getArgs());
	}

	@Test
	public void testWithArgName() {
		OptionBuilder.withArgName("argName");
		Option option = OptionBuilder.create();
		
		assertEquals("argName", option.getArgName());
	}

	@Test
	public void testIsRequired() {
		OptionBuilder.isRequired();
		Option option = OptionBuilder.create();
		
		assertTrue(option.isRequired());
	}

	@Test
	public void testWithValuesSeparatorChar() {
		OptionBuilder.withValueSeparator(':');
		Option option = OptionBuilder.create();
		
		assertEquals(':', option.getValueSeparator());
	}

	@Test
	public void testWithValuesSeparator() {
		OptionBuilder.withValueSeparator();
		Option option = OptionBuilder.create();
		
		assertEquals('=', option.getValueSeparator());
	}

	@Test
	public void testIsRequiredBoolean() {
		OptionBuilder.isRequired(true);
		Option option = OptionBuilder.create();
		
		assertTrue(option.isRequired());
		
		OptionBuilder.reset();
		
		OptionBuilder.isRequired(false);
		option = OptionBuilder.create();
		
		assertFalse(option.isRequired());
	}

	@Test
	public void testHasArgs() {
		OptionBuilder.hasArgs();
		Option option = OptionBuilder.create();
		
		assertEquals(Integer.MAX_VALUE, option.getArgs());
	}

	@Test
	public void testHasArgsInt() {
		OptionBuilder.hasArgs(3);
		Option option = OptionBuilder.create();
		
		assertEquals(3, option.getArgs());
	}

	@Test
	public void testHasOptionalArg() {
		OptionBuilder.hasOptionalArg();
		Option option = OptionBuilder.create();
		
		assertEquals(1, option.getArgs());
		assertTrue(option.hasOptionalArg());
	}

	@Test
	public void testHasOptionalArgs() {
		OptionBuilder.hasOptionalArgs();
		Option option = OptionBuilder.create();
		
		assertEquals(Integer.MAX_VALUE, option.getArgs());
		assertTrue(option.hasOptionalArg());
	}

	@Test
	public void testHasOptionalArgsInt() {
		OptionBuilder.hasOptionalArgs(3);
		Option option = OptionBuilder.create();
		
		assertEquals(3, option.getArgs());
		assertTrue(option.hasOptionalArg());
	}

	@Test
	public void testWithType() {
		Object type = new Object();
		OptionBuilder.withType(type);
		Option option = OptionBuilder.create();
		
		assertEquals(type, option.getType());
	}

	@Test
	public void testWithDescription() {
		OptionBuilder.withDescription("testDescription");
		Option option = OptionBuilder.create();
		
		assertEquals("testDescription", option.getDescription());
	}

	@Test
	public void testCreateChar() {
		Option option = OptionBuilder.create('t');
		
		assertEquals("t", option.getOpt());
	}

	@Test
	public void testCreate() {
		OptionBuilder.withLongOpt("testOpt");
		Option option = OptionBuilder.create();
		
		assertEquals("testOpt", option.getLongOpt());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateException() {
		OptionBuilder.create();
	}
}