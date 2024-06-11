import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class DiagnosticGroupsTest {
    
    private DiagnosticGroups diagnosticGroups;
    private CompilerOptions mockCompilerOptions;

    @Before
    public void setup() {
        diagnosticGroups = new DiagnosticGroups();
        mockCompilerOptions = mock(CompilerOptions.class);
    }

    @Test
    public void testSetWarningLevels() {
        String[] diagnosticGroups = DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(",");
        diagnosticGroups = Util.stripLeadingHyphens(diagnosticGroups);

        DiagnosticGroup mockDiagnosticGroup = mock(DiagnosticGroup.class);
        for (String groupName : diagnosticGroups) {
            diagnosticGroups.registerGroup(groupName, mockDiagnosticGroup);
        }

        CheckLevel mockCheckLevel = mock(CheckLevel.class);
        diagnosticGroups.setWarningLevels(mockCompilerOptions, diagnosticGroups, mockCheckLevel);

        ArgumentCaptor<DiagnosticGroup> diagnosticGroupCaptor = ArgumentCaptor.forClass(DiagnosticGroup.class);
        ArgumentCaptor<CheckLevel> checkLevelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
        verify(mockCompilerOptions, times(diagnosticGroups.length)).setWarningLevel(diagnosticGroupCaptor.capture(), checkLevelCaptor.capture());

        List<DiagnosticGroup> capturedGroups = diagnosticGroupCaptor.getAllValues();
        List<CheckLevel> capturedLevels = checkLevelCaptor.getAllValues();

        for (int i = 0; i < diagnosticGroups.length; i++) {
            assertEquals(mockDiagnosticGroup, capturedGroups.get(i));
            assertEquals(mockCheckLevel, capturedLevels.get(i));
        }
    }

    @Test
    public void testGetRegisteredGroups() {
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertEquals(DiagnosticGroups.groupsByName.size(), registeredGroups.size());

        for (Map.Entry<String, DiagnosticGroup> entry : DiagnosticGroups.groupsByName.entrySet()) {
            assertTrue(registeredGroups.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), registeredGroups.get(entry.getKey()));
        }
    }

    @Test
    public void testForName() {
        String groupName = "accessControls";
        DiagnosticGroup expectedGroup = DiagnosticGroups.groupsByName.get(groupName);

        DiagnosticGroup group = diagnosticGroups.forName(groupName);

        assertEquals(expectedGroup, group);
    }

    @Test
    public void testForName_NonExistentGroup() {
        String groupName = "nonExistentGroup";

        DiagnosticGroup group = diagnosticGroups.forName(groupName);

        assertNull(group);
    }
}