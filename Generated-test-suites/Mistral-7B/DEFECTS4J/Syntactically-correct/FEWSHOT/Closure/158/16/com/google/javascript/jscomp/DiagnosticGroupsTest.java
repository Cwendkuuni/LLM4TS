package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterAndGetGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup group1 = diagnosticGroups.registerGroup("group1", new DiagnosticType[] {});
    DiagnosticGroup group2 = diagnosticGroups.registerGroup("group2", group1);

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    List<String> groupNames = Lists.newArrayList(registeredGroups.keySet().toArray(new String[0]));

    assertTrue(groupNames.contains("group1"));
    assertTrue(groupNames.contains("group2"));

    DiagnosticGroup group1Found = diagnosticGroups.forName("group1");
    DiagnosticGroup group2Found = diagnosticGroups.forName("group2");

    assertNotNull(group1Found);
    assertNotNull(group2Found);
    checkNotNull(group1Found.getGroups());
    checkNotNull(group2Found.getGroups());
    List<DiagnosticGroup> groups1 = ImmutableList.copyOf(group1Found.getGroups());
    List<DiagnosticGroup> groups2 = ImmutableList.copyOf(group2Found.getGroups());

    assertTrue(groups1.isEmpty());
    assertTrue(groups2.contains(group1Found));
  }
}