import com.google.javascript.jscomp.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testGetRegisteredGroups() {
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(registeredGroups);
        assertTrue(registeredGroups.containsKey("accessControls"));
        assertTrue(registeredGroups.containsKey("ambiguousFunctionDecl"));
        assertTrue(registeredGroups.containsKey("checkRegExp"));
        assertTrue(registeredGroups.containsKey("checkTypes"));
        assertTrue(registeredGroups.containsKey("checkVars"));
        assertTrue(registeredGroups.containsKey("constantProperty"));
        assertTrue(registeredGroups.containsKey("deprecated"));
        assertTrue(registeredGroups.containsKey("externsValidation"));
        assertTrue(registeredGroups.containsKey("fileoverviewTags"));
        assertTrue(registeredGroups.containsKey("globalThis"));
        assertTrue(registeredGroups.containsKey("internetExplorerChecks"));
        assertTrue(registeredGroups.containsKey("invalidCasts"));
        assertTrue(registeredGroups.containsKey("missingProperties"));
        assertTrue(registeredGroups.containsKey("nonStandardJsDocs"));
        assertTrue(registeredGroups.containsKey("strictModuleDepCheck"));
        assertTrue(registeredGroups.containsKey("typeInvalidation"));
        assertTrue(registeredGroups.containsKey("undefinedVars"));
        assertTrue(registeredGroups.containsKey("unknownDefines"));
        assertTrue(registeredGroups.containsKey("uselessCode"));
        assertTrue(registeredGroups.containsKey("visibility"));
    }

    @Test
    public void testForName() {
        DiagnosticGroup globalThisGroup = diagnosticGroups.forName("globalThis");
        assertNotNull(globalThisGroup);
        assertEquals("globalThis", globalThisGroup.getName());

        DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
        assertNotNull(deprecatedGroup);
        assertEquals("deprecated", deprecatedGroup.getName());

        DiagnosticGroup visibilityGroup = diagnosticGroups.forName("visibility");
        assertNotNull(visibilityGroup);
        assertEquals("visibility", visibilityGroup.getName());

        DiagnosticGroup constantPropertyGroup = diagnosticGroups.forName("constantProperty");
        assertNotNull(constantPropertyGroup);
        assertEquals("constantProperty", constantPropertyGroup.getName());

        DiagnosticGroup nonStandardJsDocsGroup = diagnosticGroups.forName("nonStandardJsDocs");
        assertNotNull(nonStandardJsDocsGroup);
        assertEquals("nonStandardJsDocs", nonStandardJsDocsGroup.getName());

        DiagnosticGroup accessControlsGroup = diagnosticGroups.forName("accessControls");
        assertNotNull(accessControlsGroup);
        assertEquals("accessControls", accessControlsGroup.getName());

        DiagnosticGroup invalidCastsGroup = diagnosticGroups.forName("invalidCasts");
        assertNotNull(invalidCastsGroup);
        assertEquals("invalidCasts", invalidCastsGroup.getName());

        DiagnosticGroup fileoverviewTagsGroup = diagnosticGroups.forName("fileoverviewTags");
        assertNotNull(fileoverviewTagsGroup);
        assertEquals("fileoverviewTags", fileoverviewTagsGroup.getName());

        DiagnosticGroup strictModuleDepCheckGroup = diagnosticGroups.forName("strictModuleDepCheck");
        assertNotNull(strictModuleDepCheckGroup);
        assertEquals("strictModuleDepCheck", strictModuleDepCheckGroup.getName());

        DiagnosticGroup externsValidationGroup = diagnosticGroups.forName("externsValidation");
        assertNotNull(externsValidationGroup);
        assertEquals("externsValidation", externsValidationGroup.getName());

        DiagnosticGroup ambiguousFunctionDeclGroup = diagnosticGroups.forName("ambiguousFunctionDecl");
        assertNotNull(ambiguousFunctionDeclGroup);
        assertEquals("ambiguousFunctionDecl", ambiguousFunctionDeclGroup.getName());

        DiagnosticGroup unknownDefinesGroup = diagnosticGroups.forName("unknownDefines");
        assertNotNull(unknownDefinesGroup);
        assertEquals("unknownDefines", unknownDefinesGroup.getName());

        DiagnosticGroup tweaksGroup = diagnosticGroups.forName("tweakValidation");
        assertNotNull(tweaksGroup);
        assertEquals("tweakValidation", tweaksGroup.getName());

        DiagnosticGroup missingPropertiesGroup = diagnosticGroups.forName("missingProperties");
        assertNotNull(missingPropertiesGroup);
        assertEquals("missingProperties", missingPropertiesGroup.getName());

        DiagnosticGroup internetExplorerChecksGroup = diagnosticGroups.forName("internetExplorerChecks");
        assertNotNull(internetExplorerChecksGroup);
        assertEquals("internetExplorerChecks", internetExplorerChecksGroup.getName());

        DiagnosticGroup undefinedVarsGroup = diagnosticGroups.forName("undefinedVars");
        assertNotNull(undefinedVarsGroup);
        assertEquals("undefinedVars", undefinedVarsGroup.getName());

        DiagnosticGroup checkRegExpGroup = diagnosticGroups.forName("checkRegExp");
        assertNotNull(checkRegExpGroup);
        assertEquals("checkRegExp", checkRegExpGroup.getName());

        DiagnosticGroup checkTypesGroup = diagnosticGroups.forName("checkTypes");
        assertNotNull(checkTypesGroup);
        assertEquals("checkTypes", checkTypesGroup.getName());

        DiagnosticGroup checkVarsGroup = diagnosticGroups.forName("checkVars");
        assertNotNull(checkVarsGroup);
        assertEquals("checkVars", checkVarsGroup.getName());

        DiagnosticGroup checkUselessCodeGroup = diagnosticGroups.forName("uselessCode");
        assertNotNull(checkUselessCodeGroup);
        assertEquals("uselessCode", checkUselessCodeGroup.getName());

        DiagnosticGroup typeInvalidationGroup = diagnosticGroups.forName("typeInvalidation");
        assertNotNull(typeInvalidationGroup);
        assertEquals("typeInvalidation", typeInvalidationGroup.getName());
    }
}