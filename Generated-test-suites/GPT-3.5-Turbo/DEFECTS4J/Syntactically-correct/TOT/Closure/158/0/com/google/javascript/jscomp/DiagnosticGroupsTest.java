import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroupWithNameAndGroup() {
        DiagnosticGroups groups = new DiagnosticGroups();
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(name);
        DiagnosticGroup result = groups.registerGroup(name, group);

        assertNotNull(result);
        assertEquals(group, result);
        assertEquals(group, groups.getRegisteredGroups().get(name));
    }

    @Test
    public void testRegisterGroupWithNameAndTypes() {
        DiagnosticGroups groups = new DiagnosticGroups();
        String name = "testGroup";
        DiagnosticType[] types = new DiagnosticType[]{new DiagnosticType()};
        DiagnosticGroup result = groups.registerGroup(name, types);

        assertNotNull(result);
        assertEquals(types.length, result.getTypes().size());
        assertEquals(result, groups.getRegisteredGroups().get(name));
    }

    @Test
    public void testForName() {
        DiagnosticGroups groups = new DiagnosticGroups();
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(name);
        groups.registerGroup(name, group);

        DiagnosticGroup result = groups.forName(name);

        assertNotNull(result);
        assertEquals(group, result);
    }

    @Test
    public void testSetWarningLevels() {
        DiagnosticGroups groups = new DiagnosticGroups();
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
        CheckLevel level = CheckLevel.WARNING;

        groups.setWarningLevels(options, diagnosticGroups, level);

        for (String name : diagnosticGroups) {
            DiagnosticGroup group = groups.forName(name);
            assertNotNull(group);
            assertEquals(level, options.getWarningLevel(group));
        }
    }

    // Additional test cases for other methods in DiagnosticGroups class

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups groups = new DiagnosticGroups();
        groups.registerGroup("group1", new DiagnosticGroup("group1"));
        groups.registerGroup("group2", new DiagnosticGroup("group2"));

        Map<String, DiagnosticGroup> registeredGroups = groups.getRegisteredGroups();

        assertNotNull(registeredGroups);
        assertEquals(2, registeredGroups.size());
    }

    @Test
    public void testRegisterGroupWithNameAndGroups() {
        DiagnosticGroups groups = new DiagnosticGroups();
        String name = "testGroup";
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        DiagnosticGroup[] groupsArr = new DiagnosticGroup[]{group1, group2};
        DiagnosticGroup result = groups.registerGroup(name, groupsArr);

        assertNotNull(result);
        assertEquals(groupsArr.length, result.getGroups().size());
        assertEquals(result, groups.getRegisteredGroups().get(name));
    }

    // Additional test cases for predefined DiagnosticGroups

    @Test
    public void testGlobalThisGroup() {
        DiagnosticGroup group = DiagnosticGroups.GLOBAL_THIS;
        String name = "globalThis";
        assertTrue(group.contains(DiagnosticGroups.GLOBAL_THIS));
        assertEquals(name, group.getName());
    }

    @Test
    public void testDeprecatedGroup() {
        DiagnosticGroup group = DiagnosticGroups.DEPRECATED;
        String name = "deprecated";
        assertTrue(group.contains(DiagnosticGroups.DEPRECATED));
        assertEquals(name, group.getName());
    }

    @Test
    public void testVisibilityGroup() {
        DiagnosticGroup group = DiagnosticGroups.VISIBILITY;
        String name = "visibility";
        assertTrue(group.contains(DiagnosticGroups.VISIBILITY));
        assertEquals(name, group.getName());
    }

    // Additional test cases for other predefined DiagnosticGroups
    
    @Test
    public void testConstantPropertyGroup() {
        DiagnosticGroup group = DiagnosticGroups.CONSTANT_PROPERTY;
        String name = "constantProperty";
        assertTrue(group.contains(DiagnosticGroups.CONSTANT_PROPERTY));
        assertEquals(name, group.getName());
    }

    @Test
    public void testNonStandardJsDocsGroup() {
        DiagnosticGroup group = DiagnosticGroups.NON_STANDARD_JSDOC;
        String name = "nonStandardJsDocs";
        assertTrue(group.contains(DiagnosticGroups.NON_STANDARD_JSDOC));
        assertEquals(name, group.getName());
    }

    // Additional test cases for other methods and DiagnosticTypes
    
    @Test
    public void testRegisterGroupWithNameAndTypes_DiagnosticTypeIsNull() {
        DiagnosticGroups groups = new DiagnosticGroups();
        String name = "testGroup";
        DiagnosticType[] types = new DiagnosticType[]{null};
        DiagnosticGroup result = groups.registerGroup(name, types);

        assertNotNull(result);
        assertEquals(1, result.getTypes().size());
        assertEquals(result, groups.getRegisteredGroups().get(name));
    }

    @Test
    public void testCheckUselessCodeGroup() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_USELESS_CODE;
        String name = "uselessCode";
        assertTrue(group.contains(DiagnosticGroups.CHECK_USELESS_CODE));
        assertEquals(name, group.getName());
    }

    @Test
    public void testCheckTypesGroup() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_TYPES;
        String name = "checkTypes";
        assertTrue(group.contains(DiagnosticGroups.CHECK_TYPES));
        assertEquals(name, group.getName());
    }
}