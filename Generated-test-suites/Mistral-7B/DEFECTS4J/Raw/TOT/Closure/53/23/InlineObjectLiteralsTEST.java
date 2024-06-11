package com.google.javascript.jscomp.tests;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.rhino.NodeOperations.assertToString;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestingEnvironment;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  public static void main(String[] args) throws Exception {
    CompilerTestingEnvironment env = new CompilerTestingEnvironment();
    Compiler compiler = env.newCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "id_test");

    String input = "function main() {" +
        "  var o = {" +
        "    a: 1," +
        "    b: {'c': 3}," +
        "    d: function() { return 4; }" +
        "  };" +
        "  var x = o.a;" +
        "  var y = o.b.c;" +
        "  z = o.d();" +
        "}" +
        "function test() {" +
        "  main();" +
        "}" +
        "test();";

    Node root = Node.parse(input);

    pass.process(null, root);
    Node expected = Node.parse("function main() {" +
        "  var a = 1;" +
        "  var b_c = 3;" +
        "  var d = function d() { return 4; };" +
        "  var o = null;" +
        "  a = a;" +
        "  b_c = b_c;" +
        "  d = d;" +
        "  o = { a: a, b: { c: b_c } };" +
        "  a = o.a;" +
        "  b_c = o.b.c;" +
        "  d = o.d;" +
        "  z = d();" +
        "}" +
        "function test() {" +
        "  main();" +
        "}" +
        "test();");

    assertToString(expected, compiler.getSource());

    Map<String, Node> expectedMap = ImmutableMap.of(
        "a", Node.newNumber(1),
        "b_c", Node.newNumber(3),
        "d", Node.newFunction(
            Node.newIdentifier("d"), ImmutableMap.of(), Node.newUndefined(), Node.newUndefined())
        );

    Map<String, Node> actualMap = NodeUtil.getIdentifiedNodes(compiler.getSource(), true);

    assertThat(actualMap).isEqualTo(expectedMap);
  }

  private static Node NodeUtil_getIdentifiedNodes(Node node, boolean isTraverse) {
    // Implementation omitted for brevity
  }
}