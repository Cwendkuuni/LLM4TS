package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino. Logger;
import com.google.javascript.rhino.ParserRunner.ParseResult;
import com.google.javascript.rhino.Node;

public class JsAstTest {

 private AbstractCompiler compiler;
 private ErrorReporter errorReporter;
 private ParserConfig parserConfig;
 private Logger logger;
 private SourceFile sourceFile;
 private JsAst jsAst;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 errorReporter = mock(ErrorReporter.class);
 parserConfig = mock(ParserConfig.class);
 logger = mock(Logger.class);
 sourceFile = mock(SourceFile.class);
 when(sourceFile.getName()).thenReturn("test.js");
 when(sourceFile.getCode()).thenReturn("// Test source code".getBytes());
 when(sourceFile.clearCachedSource()).thenReturn(true);
 jsAst = new JsAst(sourceFile);
 }

 @Test
 public void testGetAstRoot() {
 Node root = jsAst.getAstRoot(compiler);
 assertNotNull(root);
 }

 @Test
 public void testClearAst() {
 jsAst.clearAst();
 assertNull(jsAst.getAstRoot(compiler));
 }

 @Test
 public void testGetInputId() {
 InputId inputId = jsAst.getInputId();
 assertNotNull(inputId);
 assertEquals("test.js", inputId.getName());
 }

 @Test
 public void testGetSourceFile() {
 SourceFile sourceFile = jsAst.getSourceFile();
 assertEquals(this.sourceFile, sourceFile);
 }

 @Test
 public void testSetSourceFile() {
 SourceFile newSourceFile = mock(SourceFile.class);
 jsAst.setSourceFile(newSourceFile);
 assertEquals(newSourceFile, jsAst.getSourceFile());
 }

 @Test
 public void testParse() {
 Preconditions.checkState(jsAst.root == null);
 jsAst.parse(compiler);
 assertNotNull(jsAst.root);
 }

 @Test
 public void testParseWithErrors() {
 when(compiler.hasHaltingErrors()).thenReturn(true);
 jsAst.parse(compiler);
 assertNotNull(jsAst.root);
 }

 @Test
 public void testParseWithIOException() {
 when(compiler.getParserConfig()).thenReturn(parserConfig);
 when(parserConfig.getErrorReporter()).thenReturn(errorReporter);
 when(errorReporter.error(Integer.MIN_VALUE, "Test error", 1, null)).thenReturn(true);
 when(compiler.getDefaultErrorReporter()).thenReturn(errorReporter);
 jsAst.parse(compiler);
 assertNotNull(jsAst.root);
 }

 @Test
 public void testSetOldParseTree() {
 Node oldAst = IR.script();
 jsAst.setOldParseTree("test.js", oldAst);
 assertEquals(oldAst, jsAst.getParserConfig().getOldParseTree("test.js"));
 }
}