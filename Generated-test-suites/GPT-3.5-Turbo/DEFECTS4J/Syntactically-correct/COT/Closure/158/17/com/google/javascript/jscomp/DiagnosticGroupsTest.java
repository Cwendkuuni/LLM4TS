
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testRegisterGroup_WithNameAndGroup_ReturnsGroup() {
        String name = "testGroup";
        DiagnosticGroup group = mock(DiagnosticGroup.class);
        
        DiagnosticGroup result = diagnosticGroups.registerGroup(name, group);

        assertEquals(group, result);
    }

    @Test
    public void testRegisterGroup_WithNameAndTypes_ReturnsGroup() {
        String name = "testGroup";
        DiagnosticType[] types = { mock(DiagnosticType.class) };

        DiagnosticGroup result = diagnosticGroups.registerGroup(name, types);

        assertEquals(name, result.getName());
        assertEquals(1, result.getTypes().length);
        assertEquals(types[0], result.getTypes()[0]);
    }

    @Test
    public void testRegisterGroup_WithNameAndGroups_ReturnsGroup() {
        String name = "testGroup";
        DiagnosticGroup[] groups = { mock(DiagnosticGroup.class) };

        DiagnosticGroup result = diagnosticGroups.registerGroup(name, groups);

        assertEquals(name, result.getName());
        assertEquals(1, result.getGroups().length);
        assertEquals(groups[0], result.getGroups()[0]);
    }

    @Test
    public void testGetRegisteredGroups_ReturnsRegisteredGroups() {
        assertEquals(21, diagnosticGroups.getRegisteredGroups().size());
    }

    @Test
    public void testForName_WithExistingName_ReturnsGroup() {
        String name = "testGroup";
        DiagnosticGroup group = mock(DiagnosticGroup.class);
        diagnosticGroups.registerGroup(name, group);

        DiagnosticGroup result = diagnosticGroups.forName(name);

        assertEquals(group, result);
    }

    @Test
    public void testForName_WithNonExistingName_ReturnsNull() {
        DiagnosticGroup result = diagnosticGroups.forName("nonExistingGroup");

        assertEquals(null, result);
    }

    @Test
    public void testSetWarningLevels_AllGroupsExist_SetsWarningLevels() {
        CompilerOptions options = mock(CompilerOptions.class);
        List<String> diagnosticGroups = Arrays.asList(
            "accessControls", "ambiguousFunctionDecl", "checkRegExp",
            "checkTypes", "checkVars", "constantProperty", "deprecated",
            "externsValidation", "fileoverviewTags", "globalThis",
            "internetExplorerChecks", "invalidCasts", "missingProperties",
            "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation",
            "undefinedVars", "unknownDefines", "uselessCode", "visibility"
        );
        CheckLevel level = CheckLevel.WARNING;

        diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);

        for (String name : diagnosticGroups) {
            DiagnosticGroup group = diagnosticGroups.forName(name);
            verify(options).setWarningLevel(group, level);
        }
    }
}