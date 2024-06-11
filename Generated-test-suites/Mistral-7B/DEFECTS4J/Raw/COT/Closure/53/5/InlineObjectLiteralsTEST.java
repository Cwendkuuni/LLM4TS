import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

    @org.junit.Test
    public void testInlineObjectLiterals() {
        String input = "function f() {" +
                "  var o = {a: 1, b: 2};" +
                "  var x = o.a;" +
                "  o.a = 3;" +
                "  return x;}" +
                "function g() {" +
                "  var o = {a: 1};" +
                "  return o.a;}" +
                "function h() {" +
                "  return {a: 1};}" +
                "function i() {" +
                "  var o = {a: {b: 1}};" +
                "  return o.a.b;}" +
                "function j() {" +
                "  return {a: 1, get x() { return 2; }};}" +
                "function k() {" +
                "  return {a: 1, get x() { return 2; }, b: 2};}" +
                "function l() {" +
                "  var o = {a: 1, get x() { return 2; }};" +
                "  return o.x;}" +
                "function m() {" +
                "  return {a: 1, b: {c: 1}};}" +
                "function n() {" +
                "  return {a: {b: 1}, c: 1};}" +
                "function o() {" +
                "  return {a: 1, b: 1};" +
                "  function innerFunction() { return 2; }}" +
                "}";

        CompilerOptions options = new CompilerOptions(null);
        Compiler compiler = new Compiler(options);

        CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_var_");
        compiler.addPass(pass);

        Node root = NodePrinter.printToAst(input);
        pass.process(null, root);

        List<String> expectedOutput = Arrays.asList(
                "function f() {" +
                        "var a = 1;" +
                        "var b = 2;" +
                        "var x = a;" +
                        "a = 3;" +
                        "return x;" +
                        "}",
                "function g() {" +
                        "return 1;" +
                        "}",
                "function h() {" +
                        "return 1;" +
                        "}",
                "function i() {" +
                        "return 1;" +
                        "}",
                "function j() {" +
                        "var a = 1;" +
                        "return a;" +
                        "}",
                "function k() {" +
                        "var a = 1;" +
                        "return a;" +
                        "var b = 2;" +
                        "return b;" +
                        "}",
                "function l() {" +
                        "var a = 1;" +
                        "return a;" +
                        "}",
                "function m() {" +
                        "var c = 1;" +
                        "return {" +
                            "a: 1," +
                            "c: c" +
                        "};" +
                        "}",
                "function n() {" +
                        "return {" +
                            "a: {" +
                                "b: 1" +
                            "}," +
                            "c: 1" +
                        "};" +
                        "}",
                "function o() {" +
                        "var a = 1;" +
                        "var b = 1;" +
                        "return a;" +
                        "function innerFunction() { return 2; }" +
                        "}",
                "");

        Node output = NodePrinter.printToNode(compiler.getCompiledResult());
        List<String> outputStrings = NodePrinter.printNodesToList(output);

        assertEquals(expectedOutput, outputStrings);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testEdgeCases() {
        String input = "function f() {" +
                "  var o = {get x() { return 1; }};" +
                "  return o.x;" +
                "}" +
                "function g() {" +
                "  return {get x() { return 1; }};" +
                "}" +
                "function h() {" +
                "  return {get x() { return 1; }, y: 2};" +
                "}" +
                "function i() {" +
                "  return {get x() { return 1; }, get y() { return 2; }};" +
                "}" +
                "function j() {" +
                "  return {get x() { return 1; }, y: 1, z: {get a() { return 1; }}};" +
                "}" +
                "function k() {" +
                "  return {get x() { return 1; }, get y() { return 2; }, z: 1};" +
                "}" +
                "function l() {" +
                "  return {get x() { return 1; }, y: {get a() { return 1; }}};" +
                "}" +
                "function m() {" +
                "  return {get x() { return 1; }, get y() { return 1; }, z: 1};" +
                "}" +
                "function n() {" +
                "  return {get x() { return 1; }, y: 1, z: {get a() { return 1; }, get b() { return 2; }}};" +
                "}" +
                "function o() {" +
                "  return {get x() { return 1, 2; }, y: 1};" +
                "}" +
                "function p() {" +
                "  return {get x() { return 1, 2; }, y: 1, z: 3};" +
                "}" +
                "function q() {" +
                "  return {get x() { return [1, 2]; }, y: 1};" +
                "}" +
                "function r() {" +
                "  return {get x() { return [1, 2]; }, y: 1, z: 3};" +
                "}" +
                "function s() {" +
                "  return {get x() { return null; }, y: 1};" +
                "}" +
                "function t() {" +
                "  return {get x() { return null; }, y: 1, z: 3};" +
                "}" +
                "function u() {" +
                "  return {get x() { return undefined; }, y: 1};" +
                "}" +
                "function v() {" +
                "  return {get x() { return undefined; }, y: 1, z: 3};" +
                "}" +
                "function w() {" +
                "  return {get x() { return this; }, y: 1};" +
                "}" +
                "function x() {" +
                "  return {get x() { return this; }, y: 1};" +
                "}" +
                "function y() {" +
                "  return {get x() { return this; }, get y() { return this; }};" +
                "}" +
                "function z() {" +
                "  return {get x() { return this; }, get y() { return this; }, get z() { return this; }};" +
                "}" +
                "function a() {" +
                "  return {get x() { return 1; }, get y() { return 1; }, z: 1};" +
                "  function innerFunction() { return 2; }" +
                "}" +
                "}" +
                "";

        CompilerOptions options = new CompilerOptions(null);
        Compiler compiler = new Compiler(options);

        CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_var_");
        compiler.addPass(pass);

        Node root = NodePrinter.printToAst(input);
        pass.process(null, root);
    }
}