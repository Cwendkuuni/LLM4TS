
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup_WithNameAndGroup_ReturnsGroup() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", DiagnosticType.ERROR);
        assertNotNull(group);
        assertEquals("testGroup", group.getName());
        assertArrayEquals(new DiagnosticType[]{DiagnosticType.ERROR}, group.getTypes().toArray());
    }

    @Test
    public void testRegisterGroup_WithNameAndTypes_ReturnsGroup() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", DiagnosticType.ERROR, DiagnosticType.WARNING);
        assertNotNull(group);
        assertEquals("testGroup", group.getName());
        assertArrayEquals(new DiagnosticType[]{DiagnosticType.ERROR, DiagnosticType.WARNING}, group.getTypes().toArray());
    }

    @Test
    public void testRegisterGroup_WithNameAndGroups_ReturnsGroup() {
        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", DiagnosticType.ERROR);
        DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", DiagnosticType.WARNING);
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", group1, group2);
        assertNotNull(group);
        assertEquals("testGroup", group.getName());
        assertArrayEquals(new DiagnosticGroup[]{group1, group2}, group.getGroups().toArray());
    }

    @Test
    public void testGetRegisteredGroups_ReturnsCorrectSizeAndContent() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.registerGroup("group1", DiagnosticType.ERROR);
        diagnosticGroups.registerGroup("group2", DiagnosticType.WARNING);
        diagnosticGroups.registerGroup("group3", DiagnosticType.INFO);
        assertEquals(3, diagnosticGroups.getRegisteredGroups().size());
        assertNotNull(diagnosticGroups.getRegisteredGroups().get("group1"));
        assertNotNull(diagnosticGroups.getRegisteredGroups().get("group2"));
        assertNotNull(diagnosticGroups.getRegisteredGroups().get("group3"));
    }

    @Test
    public void testForName_ReturnsRegisteredGroup() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        DiagnosticGroup group1 = diagnosticGroups.registerGroup("group1", DiagnosticType.ERROR);
        DiagnosticGroup group2 = diagnosticGroups.registerGroup("group2", DiagnosticType.WARNING);
        DiagnosticGroup group3 = diagnosticGroups.registerGroup("group3", DiagnosticType.INFO);
        assertEquals(group1, diagnosticGroups.forName("group1"));
        assertEquals(group2, diagnosticGroups.forName("group2"));
        assertEquals(group3, diagnosticGroups.forName("group3"));
    }

    @Test
    public void testForName_ReturnsNullIfGroupNotRegistered() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNull(diagnosticGroups.forName("nonexistent"));
    }

    @Test
    public void testSetWarningLevels_SetsCorrectWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.registerGroup("group1", DiagnosticType.ERROR);
        diagnosticGroups.registerGroup("group2", DiagnosticType.WARNING);
        diagnosticGroups.registerGroup("group3", DiagnosticType.INFO);
        diagnosticGroups.setWarningLevels(options, Arrays.asList("group1", "group2"), CheckLevel.ERROR);
        assertEquals(CheckLevel.ERROR, options.getWarningLevel(diagnosticGroups.forName("group1")));
        assertEquals(CheckLevel.ERROR, options.getWarningLevel(diagnosticGroups.forName("group2")));
        assertEquals(CheckLevel.OFF, options.getWarningLevel(diagnosticGroups.forName("group3")));
    }
}
