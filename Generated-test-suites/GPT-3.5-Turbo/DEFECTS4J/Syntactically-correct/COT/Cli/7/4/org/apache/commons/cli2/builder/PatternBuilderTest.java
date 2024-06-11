import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

	private PatternBuilder patternBuilder;

	@Before
	public void setup() {
		patternBuilder = new PatternBuilder();
	}

	@Test
	public void testCreate() {
		Option option = patternBuilder.create();
		assertNotNull(option);
	}

	@Test
	public void testReset() {
		patternBuilder.reset();
		Option option = patternBuilder.create();
		assertNotNull(option);
	}

	@Test
	public void testWithPattern() {
		patternBuilder.withPattern("!@:/%+#<>*/");
		Option option = patternBuilder.create();
		assertNotNull(option);
	}

	@Test
	public void testValidator() {
		Validator validator = patternBuilder.validator('@');
		assertNotNull(validator);
	}

}