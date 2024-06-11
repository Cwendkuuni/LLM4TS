package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterAndGetGroups() {
    DiagnosticGroup deprecatedGroup = new DiagnosticGroup("deprecated",
        Arrays.asList(new DiagnosticType("DeprecatedName"),
            new DiagnosticType("DeprecatedNameReason"),
            new DiagnosticType("DeprecatedProp"),
            new DiagnosticType("DeprecatedPropReason"),
            new DiagnosticType("DeprecatedClass"),
            new DiagnosticType("DeprecatedClassReason")));

    DiagnosticGroup visibilityGroup = new DiagnosticGroup("visibility",
        Arrays.asList(new DiagnosticType("BadPrivateGlobalAccess"),
            new DiagnosticType("BadPrivatePropertyAccess"),
            new DiagnosticType("BadProtectedPropertyAccess"),
            new DiagnosticType("PrivateOverride"),
            new DiagnosticType("VisibilityMismatch")));

    DiagnosticGroup constantPropertyGroup = new DiagnosticGroup("constantProperty",
        Arrays.asList(new DiagnosticType("ConstPropertyReassignedValue")));

    DiagnosticGroup diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup registeredDeprecated = diagnosticGroups.registerGroup("deprecated", deprecatedGroup);
    DiagnosticGroup registeredVisibility = diagnosticGroups.registerGroup("visibility", visibilityGroup);
    DiagnosticGroup registeredConstantProperty = diagnosticGroups.registerGroup("constantProperty", constantPropertyGroup);

    checkState(registeredDeprecated == deprecatedGroup, "Unexpected registered group");
    checkState(registeredVisibility == visibilityGroup, "Unexpected registered group");
    checkState(registeredConstantProperty == constantPropertyGroup, "Unexpected registered group");

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    checkState(registeredGroups.containsKey("deprecated"), "Expected group 'deprecated' in registered map");
    checkState(registeredGroups.containsKey("visibility"), "Expected group 'visibility' in registered map");
    checkState(registeredGroups.containsKey("constantProperty"), "Expected group 'constantProperty' in registered map");

    DiagnosticGroup groupFromMap = registeredGroups.get("deprecated");
    assertEquals(registeredDeprecated, groupFromMap);
  }

  @Test
  public void testFindGroupByName() {
    DiagnosticGroup deprecatedGroup = new DiagnosticGroup("deprecated",
        Arrays.asList(new DiagnosticType("DeprecatedName"),
            new DiagnosticType("DeprecatedNameReason"),
            new DiagnosticType("DeprecatedProp"),
            new DiagnosticType("DeprecatedPropReason"),
            new DiagnosticType("DeprecatedClass"),
            new DiagnosticType("DeprecatedClassReason")));

    DiagnosticGroup visibilityGroup = new DiagnosticGroup("visibility",
        Arrays.asList(new DiagnosticType("BadPrivateGlobalAccess"),
            new DiagnosticType("BadPrivatePropertyAccess"),
            new DiagnosticType("BadProtectedPropertyAccess"),
            new DiagnosticType("PrivateOverride"),
            new DiagnosticType("VisibilityMismatch")));

    DiagnosticGroup constantPropertyGroup = new DiagnosticGroup("constantProperty",
        Arrays.asList(new DiagnosticType("ConstPropertyReassignedValue")));

    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    diagnosticGroups.registerGroup("deprecated", deprecatedGroup);
    diagnosticGroups.registerGroup("visibility", visibilityGroup);
    diagnosticGroups.registerGroup("constantProperty", constantPropertyGroup);

    DiagnosticGroup groupByNameDeprecated = diagnosticGroups.forName("deprecated");
    assertEquals(deprecatedGroup, groupByNameDeprecated);

    DiagnosticGroup groupByNameVisibility = diagnosticGroups.forName("visibility");
    assertEquals(visibilityGroup, groupByNameVisibility);

    DiagnosticGroup groupByNameUnknown = diagnosticGroups.forName("unknown");
    assertNull(groupByNameUnknown);
  }

  @Test
  public void testGetDiagnosticGroupName() {
    DiagnosticGroup deprecatedGroup = new DiagnosticGroup("deprecated",
        Arrays.asList(new DiagnosticType("DeprecatedName"),
            new DiagnosticType("DeprecatedNameReason"),
            new DiagnosticType("DeprecatedProp"),
            new DiagnosticType("DeprecatedPropReason"),
            new DiagnosticType("DeprecatedClass"),
            new DiagnosticType("DeprecatedClassReason")));

    DiagnosticGroup visibilityGroup =
        new DiagnosticGroup("visibility", Arrays.asList(new DiagnosticType[] {}));

    DiagnosticGroup constantPropertyGroup = new DiagnosticGroup("constantProperty",
        Arrays.asList(new DiagnosticType("ConstPropertyReassignedValue")));

    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    diagnosticGroups.registerGroup("deprecated", deprecatedGroup);
    diagnosticGroups.registerGroup("visibility", visibilityGroup);
    diagnosticGroups.registerGroup("constantProperty", constantPropertyGroup);

    assertEquals("deprecated", diagnosticGroups.getGroupName(deprecatedGroup));
    assertEquals("visibility", diagnosticGroups.getGroupName(visibilityGroup));
    assertEquals("constantProperty", diagnosticGroups.getGroupName(constantPropertyGroup));
    assertNull(diagnosticGroups.getGroupName(null));
  }

  @Test
  public void testRegisterGroupWithDiagnosticTypes() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup",
        Arrays.asList(new DiagnosticType[] {}));

    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", group);

    assertEquals(registeredGroup, diagnosticGroups.getRegisteredGroups().get("testGroup"));
  }

  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup("group1", Arrays.asList(new DiagnosticType[] {}));
    DiagnosticGroup group2 = new DiagnosticGroup("group2", Arrays.asList(new DiagnosticType[] {}));

    DiagnosticGroup parentGroup = new DiagnosticGroup("parent",
        Arrays.asList(new DiagnosticType[] {}, group1, group2));

    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup registeredParent = diagnosticGroups.registerGroup("parent", parentGroup);

    assertEquals(registeredParent, diagnosticGroups.getRegisteredGroups().get("parent"));
    assertEquals(group1, registeredParent.getGroups());
    assertEquals(group2, registeredParent.getGroups().get(0));
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroup deprecatedGroup = new DiagnosticGroup("deprecated",
        Arrays.asList(new DiagnosticType("DeprecatedName"),
            new DiagnosticType("DeprecatedNameReason"),
            new DiagnosticType("DeprecatedProp"),
            new DiagnosticType("DeprecatedPropReason"),
            new DiagnosticType("DeprecatedClass"),
            new DiagnosticType("DeprecatedClassReason")));

    DiagnosticGroup visibilityGroup = new DiagnosticGroup("visibility",
        Arrays.asList(new DiagnosticType("BadPrivateGlobalAccess"),
            new DiagnosticType("BadPrivatePropertyAccess"),
            new DiagnosticType("BadProtectedPropertyAccess"),
            new DiagnosticType("PrivateOverride"),
            new DiagnosticType("VisibilityMismatch")));

    DiagnosticGroup constantPropertyGroup = new DiagnosticGroup("constantProperty",
        Arrays.asList(new DiagnosticType("ConstPropertyReassignedValue")));

    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    diagnosticGroups.registerGroup("deprecated", deprecatedGroup);
    diagnosticGroups.registerGroup("visibility", visibilityGroup);
    diagnosticGroups.registerGroup("constantProperty", constantPropertyGroup);

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(3, registeredGroups.size());

    List<DiagnosticGroup> groups = Arrays.asList(registeredGroups.values().toArray(new DiagnosticGroup[0]));
    assertEquals(ImmutableMap.of("deprecated", deprecatedGroup, "visibility", visibilityGroup, "constantProperty", constantPropertyGroup), groups);
  }

  @Test
  public void testToString() {
    DiagnosticGroup deprecatedGroup = new DiagnosticGroup("deprecated",
        Arrays.asList(new DiagnosticType[] {}));

    DiagnosticGroup visibilityGroup = new DiagnosticGroup("visibility",
        Arrays.asList(new DiagnosticType[] {}));

    DiagnosticGroup constantPropertyGroup = new DiagnosticGroup("constantProperty",
        Arrays.asList(new DiagnosticType[] {}));

    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    diagnosticGroups.registerGroup("deprecated", deprecatedGroup);
    diagnosticGroups.registerGroup("visibility", visibilityGroup);
    diagnosticGroups.registerGroup("constantProperty", constantPropertyGroup);

    assertEquals("DiagnosticGroups{groups={deprecated=DiagnosticGroup{name='deprecated', diagnostics=[], groups=[]}, visibility=DiagnosticGroup{name='visibility', diagnostics=[], groups=[()]}, constantProperty=DiagnosticGroup{name='constantProperty', diagnostics=[], groups=[]}}}", diagnosticGroups.toString());
  }
}