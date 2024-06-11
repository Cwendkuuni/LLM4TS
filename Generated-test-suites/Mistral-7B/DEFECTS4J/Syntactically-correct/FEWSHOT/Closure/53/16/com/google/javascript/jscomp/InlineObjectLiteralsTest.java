package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {
    private static final String VAR_PREFIX = "JSCompiler_object_inline_";

    private InlineObjectLiterals createInlineObjectLiterals(CompilerPass compilerPass) {
        return new InlineObjectLiterals(compilerPass, mock(Supplier.class));
    }

    @Test
    public void testInlineObjectLiterals() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "function foo() {\n" +
                "  var obj = {\n" +
                "    a: 1,\n" +
                "    b: 2,\n" +
                "  };\n" +
                "  return obj.a;\n" +
                "}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertEquals(5, functionDeclaration.getChildCount());

        Scope functionScope = findScopeByFunction(functionDeclaration, root);
        Iterator<Scope.Var> varIterator = functionScope.getVars();

        List<String> expectedVariables = Lists.newArrayList(
                VAR_PREFIX + "a", VAR_PREFIX + "b"
        );

        int i = 0;
        while (varIterator.hasNext()) {
            Scope.Var variable = varIterator.next();
            String variableName = variable.name;
            assertEquals(expectedVariables.get(i++), variableName);
        }
    }

    @Test
    public void testInlineObjectLiteralsWithInitialValues() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "function foo() {\n" +
                "  var obj = {\n" +
                "    a: 1,\n" +
                "    b: 2,\n" +
                "  };\n" +
                "  return obj.a;\n" +
                "  var obj2 = {\n" +
                "    a: 1,\n" +
                "    b: 2,\n" +
                "    c: {d: 3}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertEquals(12, functionDeclaration.getChildCount());

        Scope functionScope = findScopeByFunction(functionDeclaration, root);
        Iterator<Scope.Var> varIterator = functionScope.getVars();

        List<String> expectedVariables = Lists.newArrayList(
                VAR_PREFIX + "a", VAR_PREFIX + "b", VAR_PREFIX + "c_d"
        );

        int i = 0;
        while (varIterator.hasNext()) {
            Scope.Var variable = varIterator.next();
            String variableName = variable.name;
            assertEquals(expectedVariables.get(i++), variableName);
        }
    }

    @Test
    public void testInlineObjectLiteralsWithNestedObjects() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "function foo() {\n" +
                "  var obj = {\n" +
                "    a: 1,\n" +
                "    b: {\n" +
                "      c: 2,\n" +
                "      d: 3\n" +
                "    },\n" +
                "  };\n" +
                "  return obj.b.c;\n" +
                "}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertEquals(10, functionDeclaration.getChildCount());

        Scope functionScope = findScopeByFunction(functionDeclaration, root);
        Iterator<Scope.Var> varIterator = functionScope.getVars();

        List<String> expectedVariables = Lists.newArrayList(
                VAR_PREFIX + "b_c", VAR_PREFIX + "b_d"
        );

        int i = 0;
        while (varIterator.hasNext()) {
            Scope.Var variable = varIterator.next();
            String variableName = variable.name;
            assertEquals(expectedVariables.get(i++), variableName.substring(VAR_PREFIX.length()));
        }
    }

    @Test
    public void testInlineObjectLiteralsWithSelfReferentialAssignment() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "function foo() {\n" +
                "  var obj = {\n" +
                "    a: 1,\n" +
                "    b: {\n" +
                "      c: obj.a\n" +
                "    },\n" +
                "  };\n" +
                "  return obj.b.c;\n" +
                "}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertNotNull(functionDeclaration);

        // Self referential assignment is not allowed to be inlined.
        assertTrue(findNodeByFunctionName("foo", root).getChildCount() > 10);
    }

    @Test
    public void testInlineObjectLiteralsWithFunctionDeclaration() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "function foo() {\n" +
                "  function bar() {\n" +
                "    var obj = {\n" +
                "      a: 1,\n" +
                "      b: 2,\n" +
                "    };\n" +
                "  }\n" +
                "  return bar();\n" +
                "}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertEquals(4, functionDeclaration.getChildCount());

        Scope functionScope = findScopeByFunction(functionDeclaration, root);
        Iterator<Scope.Var> varIterator = functionScope.getVars();

        List<String> expectedVariables = Lists.newArrayList();

        int i = 0;
        while (varIterator.hasNext()) {
            Scope.Var variable = varIterator.next();
            String variableName = variable.name;
            if (!variableName.startsWith(VAR_PREFIX)) {
                expectedVariables.add(variableName);
            }
        }

        Node barFunctionDeclaration = findNodeByFunctionName("bar", root);
        assertEquals(3, barFunctionDeclaration.getChildCount());

        Scope barFunctionScope = findScopeByFunction(barFunctionDeclaration, root);
        Iterator<Scope.Var> barVarIterator = barFunctionScope.getVars();

        int j = 0;
        while (barVarIterator.hasNext()) {
            Scope.Var variable = barVarIterator.next();
            String variableName = variable.name;
            expectedVariables.add(VAR_PREFIX + variableName);
            j++;
        }

        assertEquals(2, expectedVariables.size());
        assertEquals(expectedVariables.get(0), findNodeByName("a", root).getString());
        assertEquals(expectedVariables.get(1), findNodeByName("b", root).getString());
    }

    @Test
    public void testInlineObjectLiteralsWithGlobalVariable() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "var obj = {\n" +
                "  a: 1,\n" +
                "  b: 2,\n" +
                "};\n" +
                "function foo() {\n" +
                "  return obj.a;\n" +
                "}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertNotNull(functionDeclaration);

        // Global variables are not inlined.
        assertTrue(findNodeByName("obj", root).getChildCount() > 1);
    }

    @Test
    public void testInlineObjectLiteralsWithVariablesUsedInEntirety() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "function foo() {\n" +
                "  var obj = {\n" +
                "    a: 1,\n" +
                "    b: 2,\n" +
                "  };\n" +
                "  return obj;\n" +
                "}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertNotNull(functionDeclaration);

        // Variables used in entirety are not inlined.
        assertTrue(findNodeByName("obj", root).getChildCount() > 1);
    }

    @Test
    public void testInlineObjectLiteralsWithExportedVariable() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "var obj = {\n" +
                "  a: exports.b,\n" +
                "  b: 2,\n" +
                "};\n" +
                "exports.c = obj.b;\n" +
                "function foo() {\n" +
                "  return obj.a;\n" +
                "}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertNotNull(functionDeclaration);

        // Exported variables are not inlined.
        assertTrue(findNodeByName("obj", root).getChildCount() > 1);
    }

    @Test
    public void testInlineObjectLiteralsWithForbiddenVariable() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "function foo() {\n" +
                "  var RENAME_PROPERTY_FUNCTION_NAME = 1;\n" +
                "  var obj = {\n" +
                "    a: 1,\n" +
                "  };\n" +
                "  return obj.a;\n" +
                "}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertNotNull(functionDeclaration);

        // Forbidden variables are not inlined.
        assertTrue(findNodeByName("obj", root).getChildCount() > 1);
    }

    @Test
    public void testInlineObjectLiteralsWithStaleVariable() {
        CompilerPass compilerPass = newNoOpCompilerPass();
        InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiterals(compilerPass);

        String source = "function foo() {\n" +
                "  var obj = {\n" +
                "    a: 1,\n" +
                "  };\n" +
                "  return obj.a;\n" +
                "}\n" +
                "\n" +
                "function bar() {\n" +
                "  obj.a = 2;\n" +
                "}";

        Node externs = parseJs(source, null);
        Node root = externs;

        inlineObjectLiterals.process(externs, root);

        Node functionDeclaration = findNodeByFunctionName("foo", root);
        assertNotNull(functionDeclaration);

        // Stale variables are not inlined.
        inlineObjectLiterals = createInlineObjectLiterals(compilerPass);
        ReferenceCollectingCallback referenceCollectingCallback = new ReferenceCollectingCallback(
                compilerPass, new InliningBehavior());
        referenceCollectingCallback.process(externs, root);

        List<Reference> refs = referenceCollectingCallback.getReferences(
                findVariableByName("obj", root.getChild(0).getChild(0).getChildCount() - 1));
        assertFalse(refs.get(0).isLvalue());

        Node functionDeclarationWithReferences = findNodeByFunctionName("foo", root);
        inlineObjectLiterals.process(externs, functionDeclarationWithReferences);

        Node functionDeclarationAfterInlining = findNodeByFunctionName("foo", root);
        assertNotNull(functionDeclarationAfterInlining);

        assertTrue(findNodeByName("obj", root).getChildCount() > 1);
    }
}

class InliningBehavior implements ReferenceCollectingCallback.Behavior {
    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
        // Empty implementation for test purposes
    }
}