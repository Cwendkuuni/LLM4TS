package org.apache.commons.cli; // For testing utility class in the same package

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testToModuleName() {
    assertEquals("module$foo", ProcessCommonJSModules.toModuleName("foo.js"));
    assertEquals("module$bar$baz", ProcessCommonJSModules.toModuleName("bar/baz.js"));
    assertEquals("module$a/b/c", ProcessCommonJSModules.toModuleName("a/b/c.js"));
    assertEquals("module$.", ProcessCommonJSModules.toModuleName("."));
    assertEquals("module$..", ProcessCommonJSModules.toModuleName("../"));
    assertEquals("module$./", ProcessCommonJSModules.toModuleName("./"));
  }

  @Test
  public void testGuessCJSModuleName() {
    assertEquals("module$foo", ProcessCommonJSModules.guessCJSModuleName("foo.js"));
    assertEquals("module$bar$baz", ProcessCommonJSModules.guessCJSModuleName("bar/baz.js"));
    assertEquals("module$a/b/c", ProcessCommonJSModules.guessCJSModuleName("a/b/c.js"));
    assertEquals("module$.", ProcessCommonJSModules.guessCJSModuleName("."));
    assertEquals("module$..", ProcessCommonJSModules.guessCJSModuleName("../"));
    assertEquals("module$./", ProcessCommonJSModules.guessCJSModuleName("./"));
  }

  @Test
  public void testNormalizeSourceName() {
    assertEquals("foo", ProcessCommonJSModules.normalizeSourceName("foo.js"));
    assertEquals("bar/baz", ProcessCommonJSModules.normalizeSourceName("bar/baz.js"));
    assertEquals("a/b/c", ProcessCommonJSModules.normalizeSourceName("a/b/c.js"));
    assertEquals(".", ProcessCommonJSModules.normalizeSourceName("."));
    assertEquals("../", ProcessCommonJSModules.normalizeSourceName("../"));
    assertEquals("./", ProcessCommonJSModules.normalizeSourceName("./"));
  }

  @Test
  public void testScriptNodeCount() {
    ProcessCommonJSModules p = new ProcessCommonJSModules(null, "");
    Node root = IR.script();
    p.process(null, root);
    assertEquals(1, p.getScriptNodeCount());
  }

  @Test
  public void testProcessCommonJsModulesCallback() {
    ProcessCommonJSModules p = new ProcessCommonJSModules(null, "");
    Node root = IR.script();
    root.addChild(IR.call(IR.getprop(IR.name("require"), IR.string("fs")),
        IR.string("readFileSync"), IR.string("./test.js"))).copyInformationFromForTree(root);
    p.process(null, root);

    Node moduleExports = root.getFirstChild().getChildAtIndex(1);
    assertEquals("module$fs", moduleExports.getString());
  }
}