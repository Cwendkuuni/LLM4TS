package org.apache.commons.cli;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processor;

  @Before
  public void setup() {
    processor = new ProcessCommonJSModules(new NoOpCompiler(), "");
  }

  @Test
  public void testToModuleName() {
    assertEquals("module$foo", ProcessCommonJSModules.toModuleName("foo.js"));
    assertEquals("module$foo$bar", ProcessCommonJSModules.toModuleName("foo/bar.js"));
    assertEquals("module$foo$bar$baz", ProcessCommonJSModules.toModuleName("foo/bar/baz.js"));
    assertEquals("module$foo$bar-$baz", ProcessCommonJSModules.toModuleName("foo/bar-baz.js"));
    assertEquals("module$foo$bar--baz", ProcessCommonJSModules.toModuleName("foo/bar--baz.js"));
  }

  @Test
  public void testGuessCJSModuleName() {
    assertEquals("module$foo", processor.guessCJSModuleName("foo.js"));
    assertEquals("module$foo$bar", processor.guessCJSModuleName("foo/bar.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    assertEquals("foo", processor.normalizeSourceName("foo.js"));
    assertEquals("foo", processor.normalizeSourceName("foo/"));
    assertEquals("bar", processor.normalizeSourceName("foo/bar.js"));
    assertEquals("bar", processor.normalizeSourceName("foo/bar/"));
  }

  @Test
  public void testVisitRequireCall() {
    Node root = IR.program();
    Node requireCall = IR.call(IR.getprop(IR.name("require"), null),
        IR.string("module$foo"));
    root.addChildToFront(requireCall);

    processor.process(null, root);

    Node moduleRef = root.getChildAtIndex(0);
    Node module = root.getChildAtIndex(1);
    Node require = root.getChildAtIndex(2);

    assertEquals("module$foo", moduleRef.getString());
    assertEquals("module$foo", module.getString());
    assertEquals("module$foo", require.getString());
  }

  @Test
  public void testVisitScript() {
    String sourceName = "foo.js";
    Node script = IR.script(null, null, IR.program());
    script.setString(sourceName);

    processor.process(null, script);

    Node moduleName = script.getChildAtIndex(0);
    Node googProvide = script.getChildAtIndex(1);
    Node googRequire = script.getChildAtIndex(2);

    assertEquals("module$foo", moduleName.getString());
    assertEquals("goog.provide('module$foo');", googProvide.getString());
    assertEquals("goog.require('module$foo');", googRequire.getString());
  }

  @Test
  public void testVisitModuleExports() {
    Node script = IR.script(null, null, IR.program());
    Node module = IR.objectlit().addChild(IR.property(
        IR.string("exports"), IR.objectlit()));
    script.setChildAtIndex(0, module);

    processor.process(null, script);

    Node moduleExportsProp = script.getChildAtIndex(0).getChildAtIndex(0);
    Node moduleName = script.getChildAtIndex(0).getChildAtIndex(1);

    assertEquals("exports", moduleExportsProp.getString());
    assertEquals("module$foo", moduleName.getString());
  }

  @Test
  public void testSuffixVarsCallback() {
    String sourceName = "foo.js";
    Node script = IR.script(null, null, IR.program());
    script.setString(sourceName);

    Node var1 = IR.name("var1");
    Node var2 = IR.name("var2");
    Node var3 = IR.name("var3");

    script.addChildToBack(var1);
    script.addChildToBack(var2);
    script.addChildToBack(var3);

    processor.process(null, script);

    Node var1Renamed = script.getChildAtIndex(script.getChildCount() - 3);
    Node var2Renamed = script.getChildAtIndex(script.getChildCount() - 2);
    Node var3Renamed = script.getChildAtIndex(script.getChildCount() - 1);

    assertEquals("var1$$foo", var1Renamed.getString());
    assertEquals("var2$$foo", var2Renamed.getString());
    assertEquals("var3$$foo", var3Renamed.getString());
  }
}