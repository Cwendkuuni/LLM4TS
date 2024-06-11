import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    @Test
    public void testRegisterGroupWithStringAndDiagnosticGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, registeredGroup);
    }

    @Test
    public void testRegisterGroupWithStringAndDiagnosticTypeArray() {
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup",
                new DiagnosticType("type1"), new DiagnosticType("type2"));
        assertNotNull(registeredGroup);
        assertEquals("testGroup", registeredGroup.getName());
        assertEquals(2, registeredGroup.getTypes().size());
    }

    // Additional test cases for other methods in DiagnosticGroups class can be added here

}