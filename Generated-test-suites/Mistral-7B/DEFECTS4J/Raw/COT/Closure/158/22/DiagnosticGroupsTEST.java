package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

import java.util.List;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisterGroup() {
    DiagnosticGroup actual = DiagnosticGroups.registerGroup("testGroup", new DiagnosticType("TestType"));
    assertNotNull(actual);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testNullNameWhenRegisteringGroup() {
    DiagnosticGroups.registerGroup(null, new DiagnosticType("TestType"));
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testNullDiagnosticTypeWhenRegisteringGroup() {
    DiagnosticGroups.registerGroup("testGroup", null);
  }

  @org.junit.Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroup actual = DiagnosticGroups.registerGroup("testGroupWithTypes",
        DiagnosticType.ERROR, DiagnosticType.WARNING);
    List<DiagnosticType> expected = List.of(DiagnosticType.ERROR, DiagnosticType.WARNING);
    assertArrayEquals(expected.stream().mapToInt(DiagnosticType::getCode).toArray(),
        actual.getTypes().stream().mapToInt(DiagnosticType::getCode).toArray());
  }

  @org.junit.Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroup parentGroup = new DiagnosticGroup("parentGroup");
    DiagnosticGroup childGroup = new DiagnosticGroup("childGroup");

    DiagnosticGroup actual = DiagnosticGroups.registerGroup("testGroupWithGroups", parentGroup, childGroup);
    assertEquals(parentGroup, actual.getParent());
    assertEquals(List.of(parentGroup, childGroup), actual.getChildren());
  }

  @org.junit.Test
  public void testGetRegisteredGroups() {
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", new DiagnosticType("TestType1"));
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", new DiagnosticType("TestType2"));

    Map<String, DiagnosticGroup> expected = Maps.newHashMap();
    expected.put("group1", group1);
    expected.put("group2", group2);

    Map<String, DiagnosticGroup> actual = DiagnosticGroups.getInstance().getRegisteredGroups();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testForName() {
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", new DiagnosticType("TestType1"));
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", new DiagnosticType("TestType2"));

    DiagnosticGroup actual = DiagnosticGroups.getInstance().forName("group1");
    assertEquals(group1, actual);

    assertNull(DiagnosticGroups.getInstance().forName("unknownGroup"));
  }

  @org.junit.Test
  public void testFindDiagnosticTypesInGroup() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroupWithTypes",
        DiagnosticType.ERROR, DiagnosticType.WARNING);

    List<DiagnosticType> expected = List.of(DiagnosticType.ERROR, DiagnosticType.WARNING);
    List<DiagnosticType> actual = group.getTypes();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @org.junit.Test
  public void testFindParentDiagnosticGroup() {
    DiagnosticGroup parentGroup = DiagnosticGroups.registerGroup("parentGroup");
    DiagnosticGroup childGroup = DiagnosticGroups.registerGroup("childGroup", parentGroup);

    DiagnosticGroup actual = childGroup.getParent();
    assertEquals(parentGroup, actual);
  }

  @org.junit.Test
  public void testFindChildrenDiagnosticGroups() {
    DiagnosticGroup parentGroup = DiagnosticGroups.registerGroup("parentGroup");
    DiagnosticGroup childGroup1 = DiagnosticGroups.registerGroup("childGroup1", parentGroup);
    DiagnosticGroup childGroup2 = DiagnosticGroups.registerGroup("childGroup2", parentGroup);

    List<DiagnosticGroup> expected = List.of(childGroup1, childGroup2);
    List<DiagnosticGroup> actual = parentGroup.getChildren();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testGetDiagnosticGroupByName() {
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", new DiagnosticType("TestType1"));
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", new DiagnosticType("TestType2"));

    DiagnosticGroup actual = DiagnosticGroups.getInstance().getDiagnosticGroupByName("group1");
    assertEquals(group1, actual);

    assertNull(DiagnosticGroups.getInstance().getDiagnosticGroupByName("unknownGroup"));
  }
}