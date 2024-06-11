package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NameInfo;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Property;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Symbol;
import com.google.javascript.jscomp.TypedScope;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.jscomp.logging.LoggerErrorManager;
import com.google.javascript.jscomp.testing.JSChunkGraphBuilder;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@RunWith(JUnit4.class)
public final class CrossModuleMethodMotionTest {

  private static final String EXTERNS = "";
  private static final DiagnosticType SOME_ERROR = DiagnosticType.error(
      "JSC_SOME_ERROR", "Some error.");

  private static final SourceFile FAKE_INPUT = SourceFile.fromCode(
        "fake-input", "");

  private AbstractCompiler compiler;
  private JSModuleGraph moduleGraph;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    moduleGraph = null;
  }

  @Test
  public void testDependsOnNegative() {
    JSModule m1 = new JSModule("m1");
    JSModule m2 = new JSModule("m2");
    JSModule m3 = new JSModule("m3");

    m1.addDependency(m2);
    m2.addDependency(m3);
    moduleGraph = new JSModuleGraph(m1);

    assertFalse(CrossModuleMethodMotion.dependsOn(m1, m1, moduleGraph));
  }

  @Test
  public void testDependsOnTransitivePositive() {
    JSModule m1 = new JSModule("m1");
    JSModule m2 = new JSModule("m2");
    JSModule m3 = new JSModule("m3");

    m1.addDependency(m2);
    m2.addDependency(m3);
    moduleGraph = new JSModuleGraph(m1);

    assertTrue(CrossModuleMethodMotion.dependsOn(m1, m2, moduleGraph));
    assertTrue(CrossModuleMethodMotion.dependsOn(m1, m3, moduleGraph));
    assertTrue(CrossModuleMethodMotion.dependsOn(m2, m3, moduleGraph));
  }

  @Test
  public void testDependsOnDirectPositive() {
    JSModule m1 = new JSModule("m1");
    JSModule m2 = new JSModule("m2");

    m1.addDependency(m2);
    moduleGraph = new JSModuleGraph(m1);

    assertTrue(CrossModuleMethodMotion.dependsOn(m1, m2, moduleGraph));
  }

  @Test
  public void testMoveMethodsNoTranspile() {
    JSChunk[] chunks = createModulesForTestMoveMethods();
    List<JSModule> moduleList = chunks[0].getAllModules();
    moduleGraph =
        new JSModuleGraph(new ArrayDeque<>(moduleList));

    Node source = chunks[0].getRootNode();
    Node expected = chunks[1].getRootNode();

    for (JSModule m : moduleList) {
      if (m != moduleList.get(0)) {
        expected.addChildToFront(m.getRootNode());
      }
    }

    CompilerOptions options = new CompilerOptions();
    options.setClosurePass(true);
    options.setModuleResolutionMode(CompilerOptions.ResolutionMode.BROWSER);
    options.setSkipTranspileOnlyPass(true);
    options.setChecksOnly(true);
    options.setEmitUseStrict(false);
    options.setLanguageIn(CompilerOptions.LanguageMode.ECMASCRIPT_2018);
    options.setLanguageOut(CompilerOptions.LanguageMode.ECMASCRIPT_2018);

    Compiler compiler = new Compiler();

    Compiler.setLoggingLevel(Level.WARNING);
    LoggerErrorManager loggerErrorManager = new LoggerErrorManager();
    compiler.setErrorManager(loggerErrorManager);
    compiler.initOptions(options);

    MarkNoSideEffectCalls markNoSideEffectCalls =
        new MarkNoSideEffectCalls(compiler);
    markNoSideEffectCalls.process(externsRoot, source);
    if (loggerErrorManager.getErrorCount() > 0) {
      loggerErrorManager.printSummary();
      return;
    }

    if (markNoSideEffectCalls.hasChanged()) {
      compiler.removeJSDocInfoAndPermanentlyDisableThreads();
      if (options.getIncrementalCheckMode().isOn()) {
        compiler.getIncrementalCheckHandler().updateGlobalSymbols(compiler);
      }
      NodeUtil.applyMarkNoSideEffectCallsRemove(compiler, source);
    }

    CrossModuleMethodMotion pass =
        new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), false);
    pass.process(externsRoot, source);

    NodeUtil.assertEqualsAst(expected, source);
  }

  private JSChunk[] createModulesForTestMoveMethods() {
    JSModule inputModule = new JSModule("test");
    JSModule depModule = new JSModule("dep");
    JSModuleGraph moduleGraph =
        new JSChunkGraphBuilder()
            .addChunk(inputModule)
            .addChunk(depModule)
            .setDependency(depModule, inputModule)
            .build();

    // m1.a = function() {}
    inputModule.add(SourceFile.fromCode("input1", "m1.a=function(){}"));

    // dep.b = function() {}
    depModule.add(SourceFile.fromCode("dep", "dep.b=function(){}"));

    return new JSChunk[] {new JSChunk(moduleGraph)};
  }
}