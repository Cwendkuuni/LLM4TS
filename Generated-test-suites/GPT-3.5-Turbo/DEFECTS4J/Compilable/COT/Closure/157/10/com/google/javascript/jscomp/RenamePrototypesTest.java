package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {
  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    renamePrototypes = new RenamePrototypes(null, true, null, null);
  }

  @Test
  public void testConstructor() {
    assertNotNull(renamePrototypes);
  }

  @Test
  public void testProcess() {
    // TODO: write test case for the process method
  }
}
