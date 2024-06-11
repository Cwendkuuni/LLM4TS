package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

 private ProcessCommonJSModules processor;

 @Before
 public void setUp() {
 processor = new ProcessCommonJSModules(null, ".");
 }

 @Test
 public void testGuessCJSModuleName() {
 assertEquals("a/b/module$c", processor.guessCJSModuleName("a/b/c.js"));
 assertEquals("module$d", processor.guessCJSModuleName("d.js"));
 assertEquals("module$e/f", processor.guessCJSModuleName("e/f.js"));
 assertEquals("g/h$i", processor.guessCJSModuleName("g/h-i.js"));
 }

 @Test
 public void testToModuleName() {
 assertEquals("a.b.c", ProcessCommonJSModules.toModuleName("a/b/c.js"));
 assertEquals("d", ProcessCommonJSModules.toModuleName("d.js"));
 assertEquals("e.f", ProcessCommonJSModules.toModuleName("e/f.js"));
 assertEquals("g.h._i", ProcessCommonJSModules.toModuleName("g/h-i.js"));
 }

 @Test
 public void testToModuleNameWithRelativePath() {
 String current = "file1.js";
 String required = "a/b.js";
 assertEquals("a.b", ProcessCommonJSModules.toModuleName(required, current));
 required = "../c/d.js";
 assertEquals("c.d", ProcessCommonJSModules.toModuleName(required, current));
 }

 @Test
 public void testNormalizeSourceName() {
 assertEquals("a/b/c.js", processor.normalizeSourceName("a/b/c.js"));
 assertEquals("a/b/c.js", processor.normalizeSourceName("a/b/c.js"));
 assertEquals("file1.js", processor.normalizeSourceName("file1.js"));
 assertEquals("file1.js", processor.normalizeSourceName("file1.js"));
 assertEquals("a/b/c/d.js", processor.normalizeSourceName("a/b/c/d.js"));
 assertEquals("a/b/c/d.js", processor.normalizeSourceName("a/b/c/d.js"));
 }

 @Test
 public void testProcess() {
 Node root = IR.program();
 NodeTraversal.traverse(processor.compiler, root, processor.new ProcessCommonJsModulesCallback());
 // Add assertions for the expected code changes here.
 processor.compiler.reportCodeChange();
 }

}