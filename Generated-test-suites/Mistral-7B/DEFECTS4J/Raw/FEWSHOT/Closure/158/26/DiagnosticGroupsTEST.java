package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class DiagnosticGroupsTest {

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        Map<String, DiagnosticGroup> expectedGroups = Maps.newHashMap();
        expectedGroups.put("accessControls", ACCESS_CONTROLS);
        expectedGroups.put("ambiguousFunctionDecl", AMBIGUOUS_FUNCTION_DECL);
        expectedGroups.put("checkRegExp", CHECK_REGEXP);
        expectedGroups.put("checkTypes", CHECK_TYPES);
        expectedGroups.put("checkVars", CHECK_VARIABLES);
        expectedGroups.put("constantProperty", CONSTANT_PROPERTY);
        expectedGroups.put("deprecated", DEPRECATED);
        expectedGroups.put("externsValidation", EXTERNS_VALIDATION);
        expectedGroups.put("fileoverviewTags", FILEOVERVIEW_JSDOC);
        expectedGroups.put("globalThis", GLOBAL_THIS);
        expectedGroups.put("invalidCasts", INVALID_CASTS);
        expectedGroups.put("missingProperties", MISSING_PROPERTIES);
        expectedGroups.put("nonStandardJsDocs", NON_STANDARD_JSDOC);
        expectedGroups.put("strictModuleDepCheck", STRICT_MODULE_DEP_CHECK);
        expectedGroups.put("typeInvalidation", TYPE_INVALIDATION);
        expectedGroups.put("uselessCode", CHECK_USELESS_CODE);
        expectedGroups.put("visibility", VISIBILITY);
        Map<String, DiagnosticGroup> actualGroups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(actualGroups);
        assertEquals(expectedGroups.size(), actualGroups.size());
        for (String name : expectedGroups.keySet()) {
            DiagnosticGroup expectedGroup = expectedGroups.get(name);
            DiagnosticGroup actualGroup = actualGroups.get(name);
            assertNotNull(actualGroup);
            assertEquals(expectedGroup.getName(), actualGroup.getName());
        }
    }

    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
        DiagnosticGroup ambiguousFunctionDecl = diagnosticGroups.forName("ambiguousFunctionDecl");
        DiagnosticGroup checkRegExp = diagnosticGroups.forName("checkRegExp");
        DiagnosticGroup checkTypes = diagnosticGroups.forName("checkTypes");
        DiagnosticGroup checkVars = diagnosticGroups.forName("checkVars");
        DiagnosticGroup constantProperty = diagnosticGroups.forName("constantProperty");
        DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
        DiagnosticGroup externsValidation = diagnosticGroups.forName("externsValidation");
        DiagnosticGroup fileoverviewTags = diagnosticGroups.forName("fileoverviewTags");
        DiagnosticGroup globalThis = diagnosticGroups.forName("globalThis");
        DiagnosticGroup invalidCasts = diagnosticGroups.forName("invalidCasts");
        DiagnosticGroup missingProperties = diagnosticGroups.forName("missingProperties");
        DiagnosticGroup nonStandardJsDocs = diagnosticGroups.forName("nonStandardJsDocs");
        DiagnosticGroup strictModuleDepCheck = diagnosticGroups.forName("strictModuleDepCheck");
        DiagnosticGroup typeInvalidation = diagnosticGroups.forName("typeInvalidation");
        DiagnosticGroup uselessCode = diagnosticGroups.forName("uselessCode");
        DiagnosticGroup visibility = diagnosticGroups.forName("visibility");
        assertNotNull(accessControls);
        assertNotNull(ambiguousFunctionDecl);
        assertNotNull(checkRegExp);
        assertNotNull(checkTypes);
        assertNotNull(checkVars);
        assertNotNull(constantProperty);
        assertNotNull(deprecated);
        assertNotNull(externsValidation);
        assertNotNull(fileoverviewTags);
        assertNotNull(globalThis);
        assertNotNull(invalidCasts);
        assertNotNull(missingProperties);
        assertNotNull(nonStandardJsDocs);
        assertNotNull(strictModuleDepCheck);
        assertNotNull(typeInvalidation);
        assertNotNull(uselessCode);
        assertNotNull(visibility);
    }

    @Test
    public void testRegisterGroup() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        String name = "myGroup";
        DiagnosticGroup myGroup = new DiagnosticGroup(name, ImmutableList.of());
        diagnosticGroups.registerGroup(name, myGroup);
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(registeredGroups);
        DiagnosticGroup registeredMyGroup = registeredGroups.get(name);
        assertNotNull(registeredMyGroup);
        assertEquals(name, registeredMyGroup.getName());
    }

    @Test
    public void testRegisterGroupWithTypes() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        String name = "myGroup";
        DiagnosticType type1 = new DiagnosticType("TEST_1");
        DiagnosticType type2 = new DiagnosticType("TEST_2");
        DiagnosticGroup myGroup = new DiagnosticGroup(name, ImmutableList.of(type1, type2));
        diagnosticGroups.registerGroup(name, type1, type2);
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(registeredGroups);
        DiagnosticGroup registeredMyGroup = registeredGroups.get(name);
        assertNotNull(registeredMyGroup);
        assertEquals(name, registeredMyGroup.getName());
        assertEquals(ImmutableList.of(type1, type2), registeredMyGroup.getDiagnosticTypes());
    }

    @Test
    public void testRegisterGroupWithGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        String name = "myGroup";
        DiagnosticGroup group1 = new DiagnosticGroup("group1", ImmutableList.of());
        DiagnosticGroup group2 = new DiagnosticGroup("group2", ImmutableList.of());
        DiagnosticGroup myGroup = new DiagnosticGroup(name, ImmutableList.of(group1, group2));
        diagnosticGroups.registerGroup(name, group1, group2);
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(registeredGroups);
        DiagnosticGroup registeredMyGroup = registeredGroups.get(name);
        assertNotNull(registeredMyGroup);
        assertEquals(name, registeredMyGroup.getName());
        assertEquals(ImmutableList.of(group1, group2), registeredMyGroup.getGroups());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterGroupWithNullName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.registerGroup(null, ImmutableList.of());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterGroupWithEmptyName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.registerGroup("", ImmutableList.of());
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterGroupWithNullGroup() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.registerGroup("myGroup", null);
    }

    @Test
    public void testDIAGNOSTIC_GROUP_NAMES() {
        String diagnosticGroupName = "accessControls,ambiguousFunctionDecl,checkRegExp," +
                "checkTypes,checkVars,constantProperty,deprecated,externsValidation," +
                "fileoverviewTags,globalThis,internetExplorerChecks,invalidCasts,missingProperties," +
                "nonStandardJsDocs,strictModuleDepCheck,typeInvalidation,undefinedVars,uselessCode," +
                "visibility";
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        List<String> diagnosticGroupNames = ImmutableList.of(
                "accessControls", "ambiguousFunctionDecl", "checkRegExp",
                "checkTypes", "checkVars", "constantProperty", "deprecated",
                "externsValidation", "fileoverviewTags", "globalThis",
                "internetExplorerChecks", "invalidCasts", "missingProperties",
                "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation",
                "undefinedVars", "uselessCode", "visibility");
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        for (String diagnosticGroupNameStr : diagnosticGroupName.split(",")) {
            String diagnosticGroupNameTrimmed = diagnosticGroupNameStr.trim();
            DiagnosticGroup diagnosticGroup = registeredGroups.get(diagnosticGroupNameTrimmed);
            assertNotNull(diagnosticGroup);
        }
    }
}