import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
public class DiagnosticGroupsTest {
	@Test
	public void testGetRegisteredGroups() {
		DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
		assertEquals(ImmutableMap.copyOf(Util.EMPTY_STRING_ARRAY), diagnosticGroups.getRegisteredGroups());
	}
	@Test
	public void testForName() {
		DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
		assertNull(diagnosticGroups.forName(null));
	}
	@Test
	public void testSetWarningLevels() {
		DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
		CompilerOptions options = new CompilerOptions();
		List<String> diagnosticGroupsList = Arrays.asList(Util.EMPTY_STRING_ARRAY);
		CheckLevel level = CheckLevel.WARNING;
		diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, level);
	}
}