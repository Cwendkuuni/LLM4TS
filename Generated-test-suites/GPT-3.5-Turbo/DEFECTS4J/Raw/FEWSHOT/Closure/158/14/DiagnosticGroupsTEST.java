package com.google.javascript.jscomp;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.List;
public class DiagnosticGroupsTest {
    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        assertEquals(group, DiagnosticGroups.registerGroup("testGroup", group));
    }
    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, DiagnosticGroups.forName("testGroup"));
    }
}