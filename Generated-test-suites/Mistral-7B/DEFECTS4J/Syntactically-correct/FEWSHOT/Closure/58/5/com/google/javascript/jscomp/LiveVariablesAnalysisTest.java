package org.apache.commons.cli;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.BitSet;
import java.util.Set;

public class LiveVariablesAnalysisTest {
    @Test
    public void testLiveVariablesAnalysis() {
        // Given
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new Compiler();

        String sourceCode = "function foo(a, b) {\n" +
                "  var c = a + b;\n" +
                "  if (c > 10) {\n" +
                "    var d = a + b;\n" +
                "    return d;\n" +
                "  } else {\n" +
                "    var e = a + b;\n" +
                "    return e * 2;\n" +
                "  }\n" +
                "}";

        jsScope.compile(sourceCode, compiler);

        // When
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

        // Then
        assertThat(escapedLocals).containsExactly(jsScope.getVar("arguments"));
        assertThat(liveVariablesAnalysis.isLive(liveVariablesAnalysis.getVarIndex("a"))).isTrue();
        assertThat(liveVariablesAnalysis.isLive(liveVariablesAnalysis.getVarIndex("b"))).isTrue();
        assertThat(liveVariablesAnalysis.isLive(liveVariablesAnalysis.getVarIndex("c"))).isTrue();
        assertThat(liveVariablesAnalysis.isLive(liveVariablesAnalysis.getVarIndex("d"))).isTrue();
        assertThat(liveVariablesAnalysis.isLive(liveVariablesAnalysis.getVarIndex("e"))).isFalse();
    }

    @Test
    public void testEmptyFunction() {
        // Given
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new Compiler();

        String sourceCode = "function foo() {}";

        jsScope.compile(sourceCode, compiler);

        // When
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

        // Then
        assertThat(escapedLocals).isEmpty();
    }

    @Test
    public void testFunctionWithNoLocalVariables() {
        // Given
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new Compiler();

        String sourceCode = "function foo() { return 42; }";

        jsScope.compile(sourceCode, compiler);

        // When
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

        // Then
        assertThat(escapedLocals).isEmpty();
    }

    @Test
    public void testFunctionWithEscapedVariable() {
        // Given
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new Compiler();

        String sourceCode = "function foo(a) {\n" +
                "  var bar = a;\n" +
                "  return function(b) {\n" +
                "    return bar + b;\n" +
                "  };\n" +
                "}\n" +
                "foo(1);";

        jsScope.compile(sourceCode, compiler);

        // When
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

        // Then
        assertThat(escapedLocals).containsExactly(jsScope.getVar("bar"));
    }

    @Test
    public void testNullInput() {
        // Given
        ControlFlowGraph<Node> cfg = null;
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new Compiler();

        // When & Then
        assertNull(new LiveVariablesAnalysis(cfg, jsScope, compiler));
    }
}