
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckSideEffects.DiagnosticType;
import com.google.javascript.jscomp.CheckSideEffects.StripProtection;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.CodingConvention.Bind;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSSourceFile;

import java.util.List;

public class CheckSideEffectsTest {

  private static CompilerOptions options = new CompilerOptions();
  private Compiler compiler;

  @Before
  public void initCompiler() {
    compiler = new Compiler();
    options.setPreserveTypeAnnotations(true);
    options.setLanguageIn(CompilerOptions.LanguageMode.ECMASCRIPT_2015);
    options.setWarningLevel(DiagnosticType.USELESS_CODE_ERROR, CheckLevel.WARNING);
  }

  @Test
  public void testProcess_withUselessCode() throws Exception {
    String code = "var a = 1; var result = a + 1; alert(result);";
    String expectedWarning = "This code lacks side-effects. Is there a bug?";
    String warningLocation = "var result = a + 1";
    assertWarning(code, expectedWarning, warningLocation);
  }

  @Test
  public void testProcess_withStringConcatenation() throws Exception {
    String code = "var s = 'hello' 'world';";
    String expectedWarning = "Is there a missing '+' on the previous line?";
    String warningLocation = "var s = 'hello' 'world'";
    assertWarning(code, expectedWarning, warningLocation);
  }

  @Test
  public void testProcess_withUnusedResult() throws Exception {
    String code = "1 + 2;";
    String expectedWarning = "The result of the '+' operator is not being used.";
    String warningLocation = "1 + 2";
    assertWarning(code, expectedWarning, warningLocation);
  }

  @Test
  public void testHotSwapScript() throws Exception {
    String code = "var a = 1; var result = a + 1; alert(result);";
    String expectedWarning = "This code lacks side-effects. Is there a bug?";
    String warningLocation = "var result = a + 1";

    JSSourceFile source = JSSourceFile.fromCode("input.js", code);
    compiler.compile(Lists.newArrayList(source), Lists.<SourceFile>newArrayList(), options);
    assertEquals(expectedWarning, compiler.getWarnings()[0].getDescription());
    assertEquals(warningLocation, compiler.getWarnings()[0].getSourceName() + ":" + compiler.getWarnings()[0].getLineNo());

    code = "var a = 1;";
    expectedWarning = "This code lacks side-effects. Is there a bug?";
    warningLocation = "var a = 1";

    source = JSSourceFile.fromCode("input.js", code);
    compiler.disableThreads();
    compiler.hotSwapScript(Lists.newArrayList(source), null);
    assertEquals(expectedWarning, compiler.getWarnings()[0].getDescription());
    assertEquals(warningLocation, compiler.getWarnings()[0].getSourceName() + ":" + compiler.getWarnings()[0].getLineNo());
  }

  @Test
  public void testHotSwapScript_withStripProtection() throws Exception {
    String code = "var a = 1; var result = a + 1; alert(result);";
    String expectedWarning = "This code lacks side-effects. Is there a bug?";
    String warningLocation = "var result = a + 1";

    JSSourceFile source = JSSourceFile.fromCode("input.js", code);
    compiler.compile(Lists.newArrayList(source), Lists.<SourceFile>newArrayList(), options);

    // Strip protection
    StripProtection stripProtection = new StripProtection(compiler);
    stripProtection.process(null, compiler.getRoot());
    assertEquals(expectedWarning, compiler.getWarnings()[0].getDescription());
    assertEquals(warningLocation, compiler.getWarnings()[0].getSourceName() + ":" + compiler.getWarnings()[0].getLineNo());
  }

  private void assertWarning(String code, String expectedWarning, String warningLocation)
      throws Exception {
    JSSourceFile source = JSSourceFile.fromCode("input.js", code);
    compiler.compile(Lists.newArrayList(source), Lists.<SourceFile>newArrayList(), options);
    assertEquals(expectedWarning, compiler.getWarnings()[0].getDescription());
    assertEquals(warningLocation, compiler.getWarnings()[0].getSourceName() + ":" + compiler.getWarnings()[0].getLineNo());
  }
}

