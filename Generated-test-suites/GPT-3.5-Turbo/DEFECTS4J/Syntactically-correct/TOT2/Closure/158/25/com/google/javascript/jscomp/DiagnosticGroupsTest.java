import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", DiagnosticGroups.GLOBAL_THIS);
        assertNotNull(group);
        assertEquals("testGroup", group.getName());
        assertEquals(1, group.getTypes().size());
        assertEquals(DiagnosticGroups.GLOBAL_THIS, group.getTypes().get(0));
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertEquals(19, diagnosticGroups.getRegisteredGroups().size());
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = DiagnosticGroups.forName("globalThis");
        assertNotNull(group);
        assertEquals("globalThis", group.getName());
    }

    // Add more test cases for other methods as needed

}