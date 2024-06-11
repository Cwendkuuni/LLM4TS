package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestUtils;
import com.google.javascript.rhino.NodeFactory;

@RunWith(Parameterized.class)
public class RenameVarsTest {

  private static final String RENAMED_CODE = "function foo(a, b) {" +
      "  var c = function(d, e) { return a; }" +
      "  var e = 42;" +
      "  var f = function(g, h) { return g; }" +
      "  var i = 43;" +
      "  var j = null;" +
      "  var k = {};" +
      "  var l = 44;" +
      "  var m = this;" +
      "  var n = [1, 2, 3];" +
      "  var o = function() {}" +
      "  var p = new Date();" +
      "  var q = Math.random();" +
      "  var r = Symbol('sym');" +
      "  var s = [a, [b], {a: a}, function(d) { return a; }, a.toString(), a + b];" +
      "  var t = 45.5;" +
      "  var u = void 0;" +
      "  var v = new Uint32Array(new Uint8Array(new ArrayBuffer(1024)));" +
      "  var w = new Float64Array(v);" +
      "  var x = new Int16Array(w);" +
      "  var y = new Int32Array(w);" +
      "  var z = new Uint16Array(w);" +
      "  var A = [a, null, true, [], function() {}, null];" +
      "}";

  private static final String RENAMED_DEBUG_LOG = "JS var assignments:\n" +
      "a => a0\n" +
      "b => b0\n" +
      "c => a1\n" +
      "d => a2\n" +
      "e => b1\n" +
      "g => a3\n" +
      "h => a4\n" +
      "i => a5\n" +
      "j => a6\n" +
      "k => a7\n" +
      "l => a8\n" +
      "m => m0\n" +
      "n => a9\n" +
      "o => a10\n" +
      "p => a11\n" +
      "q => a12\n" +
      "r => a13\n" +
      "s => a14\n" +
      "t => a15\n" +
      "u => a16\n" +
      "v => a17\n" +
      "w => a18\n" +
      "x => a19\n" +
      "y => a20\n" +
      "z => a21\n" +
      "A => a22";

  private final String inputCode;
  private final String expectedNewCode;
  private final String expectedDebugLog;

  public RenameVarsTest(String inputCode, String expectedNewCode,
      String expectedDebugLog) {
    this.inputCode = inputCode;
    this.expectedNewCode = expectedNewCode;
    this.expectedDebugLog = expectedDebugLog;
  }

  @Parameterized.Parameters(name = "{index}: input={inputCode}, output={expectedNewCode}, debugLog={expectedDebugLog}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        // Test case 0
        {
          "function foo(a, b) { " +
              "  var c = function(d, e) { return a; }" +
              "}",
          "function foo(a, b) { " +
              "  var c = function(d, e) { return a; }" +
              "}",
          ""
        },
        // Test case 1
        {
          RENAMED_CODE,
          RENAMED_CODE,
          RENAMED_DEBUG_LOG
        }
    });
  }

  @Test
  public void testRenameVars() {
    AbstractCompiler compiler = TestUtils.newCompiler();
    VariableMap prevUsedRenameMap = new VariableMap();

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false,
        prevUsedRenameMap, null, null);
    Node externs = NodeFactory.createScopeNode();
    Node root = NodeFactory.createBlockNode(NodeFactory.createStatementListNode());
    compiler.process(externs, root);
    compiler.process(NodeFactory.createStringNode(inputCode), root);

    assertEquals(expectedNewCode, TestUtils.getCompiledCode(compiler));
    assertEquals(expectedDebugLog, compiler.getDebugLog());
  }
}