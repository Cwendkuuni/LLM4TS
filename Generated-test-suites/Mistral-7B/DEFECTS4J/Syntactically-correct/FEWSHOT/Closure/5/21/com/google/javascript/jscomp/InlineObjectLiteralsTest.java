package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.ImmutableList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {
    @Test
    public void testInlineObjectLiterals() {
        Node root = new Node(Token.SCRIPTLET, null);
        Node externs = new Node(Token.SCRIPTLET, null);
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test";

        InlineObjectLiterals inlineObjectLiterals =
                new InlineObjectLiterals(compiler, safeNameIdSupplier);

        inlineObjectLiterals.process(externs, root);

        ReferenceCollectingCallback callback =
                new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
        callback.process(externs, root);

        // Test a simple object literal assignment
        checkObjectInline("var x = {a: 1, b: 2};", ImmutableMap.of("x", "JSCompiler_object_inline_a_test"), ImmutableMap.of("a", "1"));

        // Test a simple object literal assignment with a single property
        checkObjectInline("var x = {a: 1};", ImmutableMap.of("x", "JSCompiler_object_inline_a_test_1"), ImmutableMap.of("a", "1"));

        // Test an object literal assignment as a getter property
        checkObjectInline("var x = {get a() {return {a: 1};}};", ImmutableMap.of("x", "JSCompiler_object_inline_a_test_getter"), ImmutableMap.of("a", "1"));

        // Test an object literal assignment as an assignment to a property
        checkObjectInline("var x = {}; x.a = {b: 1};", ImmutableMap.of("x", "JSCompiler_object_inline_a_test_prop"), ImmutableMap.of("b", "1"));

        // Test an object literal assignment as a method call
        checkObjectInline("var x = {}; x.fn = function() {return {c: 1};}; x.fn();", ImmutableMap.of("x", "JSCompiler_object_inline_a_test_method"), ImmutableMap.of("c", "1"));

        // Test an object literal assignment as a delete operation
        checkObjectInline("var x = {a: 1}; delete x.a;", ImmutableMap.of("x", "JSCompiler_object_inline_a_test_delete"), ImmutableMap.of());

        // Test an object literal with self-referential assignment
        checkObjectInline("var x = {a: {b: x.a}};", ImmutableMap.of());

        // Test an object literal with circular reference
        checkObjectInline("var x = {a: {b: x}}; var y = {c: x};", ImmutableMap.of());

        // Test an object literal with property reference
        checkObjectInline("var x = {a: 1, b: {c: x.a}};", ImmutableMap.of("x", "JSCompiler_object_inline_a_test_prop_ref"), ImmutableMap.of("c", "1"));

        // Test an object literal with a var reference
        checkObjectInline("var x = {}; var y = 1; x.a = y;", ImmutableMap.of("x", "JSCompiler_object_inline_a_test_var_ref"), ImmutableMap.of());

        // Test an object literal with multiple properties referencing the same variable
        checkObjectInline("var x = {}; x.a = 1; x.b = x.a;", ImmutableMap.of("x", "JSCompiler_object_inline_a_test_multi_ref"), ImmutableMap.of());

        // Test an object literal with a single property referencing another variable
        checkObjectInline("var x = {}; x.a = y;", ImmutableMap.of());

        // Test an object literal with multiple properties referencing another variable
        checkObjectInline("var x = {}; x.a = y; x.b = x.a;", ImmutableMap.of());

        // Test an object literal with a single property referencing another object literal
        checkObjectInline("var x = {}; x.a = {b: 1};", ImmutableMap.of());

        // Test an object literal with multiple properties referencing another object literal
        checkObjectInline("var x = {}; x.a = {b: 1}; x.b = x.a;", ImmutableMap.of());

        // Test an object literal with a single property referencing another object literal and a variable
        checkObjectInline("var x = {}; x.a = {b: y};", ImmutableMap.of());

        // Test an object literal with multiple properties referencing another object literal and a variable
        checkObjectInline("var x = {}; x.a = {b: y}; x.b = x.a;", ImmutableMap.of());

        // Test an object literal with a single property referencing another object literal and multiple variables
        checkObjectInline("var x = {}; x.a = {b: y, c: z};", ImmutableMap.of());

        // Test an object literal with multiple properties referencing another object literal and multiple variables
        checkObjectInline("var x = {}; x.a = {b: y, c: z}; x.b = x.a; x.c = x.a;", ImmutableMap.of());

        // Test an object literal with a single property referencing a local variable
        checkObjectInline("function f() {var x = {a: x};}", ImmutableMap.of());

        // Test an object literal with multiple properties referencing a local variable
        checkObjectInline("function f() {var x = {a: x, b: x};}", ImmutableMap.of());

        // Test an object literal with a single property referencing a global variable
        checkObjectInline("var x = {}; x.a = window;", ImmutableMap.of());

        // Test an object literal with multiple properties referencing a global variable
        checkObjectInline("var x = {}; x.a = window; x.b = window;", ImmutableMap.of());

        // Test an object literal with a single property referencing a global variable and a local variable
        checkObjectInline("var x = {}; x.a = window; x.b = x;", ImmutableMap.of());

        // Test an object literal with multiple properties referencing a global variable and a local variable
        checkObjectInline("var x = {}; x.a = window; x.b = x; x.c = x;", ImmutableMap.of());

        // Test an object literal with a single property referencing a global variable and a local variable and a constant
        checkObjectInline("var x = {}; x.a = window; x.b = 1;", ImmutableMap.of());

        // Test an object literal with multiple properties referencing a global variable and a local variable and a constant
        checkObjectInline("var x = {}; x.a = window; x.b = x; x.c = x; x.d = 1;", ImmutableMap.of());

        // Test an object literal with a single property referencing a global variable and a local variable and a global constant
        checkObjectInline("var x = {}; x.a = window; x.b = 1; x.c = window;", ImmutableMap.of());

        // Test an object literal with multiple properties referencing a global variable and a local variable and a global constant
        checkObjectInline("var x = {}; x.a = window; x.b = x; x.c = x; x.d = 1; x.e = window;", ImmutableMap.of());

        // Test an object literal with a single property referencing a global variable and a local variable and another object literal
        checkObjectInline("var x = {}; x.a = window; x.b = x; x.c = {d: 1};", ImmutableMap.of());

        // Test an object literal with multiple properties referencing a global variable and a local variable and another object literal
        checkObjectInline("var x = {}; x.a = window; x.b = x; x.c = x; x.d = {e: 1}; x.e = x;", ImmutableMap.of());

        // Test an object literal with a single property referencing a global variable and a local variable and another
        // object literal and a constant
        checkObjectInline("var x = {}; x.a = window; x.b = x; x.c = {d: 1}; x.d = 1;", ImmutableMap.of());

        // Test an object literal with multiple properties referencing a global variable and a local variable and another
        // object literal and a constant
        checkObjectInline("var x = {}; x.a = window; x.b = x; x.c = x; x.d = x; x.e = 1; x.f = 1;", ImmutableMap.of());
    }

    private void checkObjectInline(String code, Map<String, String> expectedVars, Map<String, String> expectedValues) {
        Scope scope = new Scope(new Node(Token.SCRIPTLET, null), null);
        AbstractCompiler compiler = new AbstractCompiler();

        CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
        pass.process(new Node(Token.SCRIPTLET, null), new Node(Token.SCRIPTLET, null));

        Node parsedCode = compiler.parse(code, new Scope(null, scope), null);
        ReferenceCollectingCallback callback =
                new ReferenceCollectingCallback(compiler, new InliningBehavior());
        callback.process(new Node(Token.SCRIPTLET, null), parsedCode);

        Map<String, List<Reference>> refs = Maps.newLinkedHashMap();
        for (Map.Entry<String, Var> entry : scope.getVariables().entrySet()) {
            refs.put(entry.getKey(), Lists.newArrayList(callback.getReferenceInfo(entry.getValue()).references));
        }

        Map<String, Map<String, Object>> actualVars = Maps.newLinkedHashMap();
        for (Map.Entry<String, List<Reference>> entry : refs.entrySet()) {
            String varName = entry.getKey();
            Map<String, Object> vars = Maps.newLinkedHashMap();
            for (Reference ref : entry.getValue()) {
                Var var = scope.getVariable(varName);
                Node node = ref.getNode();
                if (node.isGetProp()) {
                    String propName = node.getLastChild().getString();
                    vars.put(propName, ref.getAssignedValue());
                } else {
                    vars.put("", ref.getAssignedValue());
                }
            }
            actualVars.put(varName, vars);
        }

        Map<String, Object> actualValues = Maps.newLinkedHashMap();
        for (Node node : parsedCode.traverseChildrenPostOrder()) {
            if (node.isObjectLit()) {
                for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
                    if (!child.isName()) continue;
                    String key = child.getString();
                    actualValues.put(key, node.getNext().getFirstChild().getValue());
                }
            }
        }

        assertEquals(expectedVars, actualVars);
        assertEquals(expectedValues, actualValues);
    }
}

class InliningBehavior extends ReferenceCollectingCallback.Behavior {

    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
        super.afterExitScope(t, referenceMap);

        InlineObjectLiterals inlineObjectLiterals = (InlineObjectLiterals) this.parentPass;

        for (Var v : t.getScope().getVars()) {
            if (inlineObjectLiterals.isVarInlineForbidden(v)) continue;

            ReferenceCollection referenceInfo = referenceMap.getReferences(v);
            if (inlineObjectLiterals.isInlinableObject(referenceInfo.references)) {
                inlineObjectLiterals.splitObject(v, referenceInfo.declaration, referenceInfo.initial, referenceInfo);
            }
        }
    }
}