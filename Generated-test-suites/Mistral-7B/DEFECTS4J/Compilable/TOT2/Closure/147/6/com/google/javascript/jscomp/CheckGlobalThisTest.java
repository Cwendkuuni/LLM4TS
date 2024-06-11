package com.google.javascript.jscomp;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertNotNull(checkGlobalThis);
  }
}