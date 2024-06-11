package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.CompilationUnit;
import org.apache.commons.cli.DefaultCompiler;
import org.apache.commons.cli.DefaultEnvironment;
import org.apache.commons.cli.DiagnosticType;
import org.apache.commons.cli.Environment;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseResult;
import org.apache.commons.cli.ScopedAliases;
import org.apache.commons.cli.Traversal;
import org.junit.Test;

public class ScopedAliasesTest {

    @Test
    public void testScopedAliases() throws Exception {
        String input = "package test;\n" +
                "\n" +
                "import static com.google.javascript.jscomp.Diagnostics.type;\n" +
                "\n" +
                "goog.scope(function() {\n" +
                "    function foo() {}\n" +
                "    var bar = goog.foo;\n" +
                "});\n" +
                "\n" +
                "goog.scope(function() {\n" +
                "    function foo() {}\n" +
                "    var baz = goog.foo;\n" +
                "});\n" +
                "\n" +
                "goog.scope(function() {\n" +
                "    function foo() {}\n" +
                "    var qux = goog.foo;\n" +
                "    var quux = goog.foo;\n" +
                "});\n" +
                "\n" +
                "function main() {\n" +
                "    var foo = test.foo;\n" +
                "    assertEquals(typeof foo, \"function\");\n" +
                "    assertEquals(typeof bar, \"function\");\n" +
                "    assertEquals(typeof baz, \"function\");\n" +
                "    assertEquals(typeof qux, \"function\");\n" +
                "    assertEquals(typeof quux, \"function\");\n" +
                "}\n" +
                "\n" +
                "main();";

        CompilationUnit unit = new CompilationUnit(new DefaultEnvironment());
        unit.parse(input);

        Options options = new Options();
        options.addOption(AliasTransformation.aliasTransformationKey, true);

        DefaultCompiler compiler = new DefaultCompiler(new DiagnosticListener() {
            @Override
            public boolean handle(Diagnostic diagnostic, Environment context, int level) {
                if (level > Diagnostic.WARNING && diagnostic.getType() != DiagnosticType.unused) {
                    // Ignore all errors and warnings except for unused names
                    return false;
                }
                return true;
            }
        }, options);

        ParseResult result = compiler.compile(unit.getSource(), unit.getSourceFileName());

        Traversal traversal = result.getGenerator().getTraversal();
        List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
        List<AliasUsage> aliasUsages = traversal.getAliasUsages();
        List<Node> scopeCalls = traversal.getScopeCalls();

        assertEquals(3, aliasDefinitionsInOrder.size());
        assertEquals("goog.foo", aliasDefinitionsInOrder.get(0).getString());
        assertEquals("goog.foo", aliasDefinitionsInOrder.get(1).getString());
        assertEquals("goog.foo", aliasDefinitionsInOrder.get(2).getString());

        assertEquals(3, aliasUsages.size());
        assertEquals("test.foo", aliasUsages.get(0).getNode().getString());
        assertEquals("goog.foo", aliasUsages.get(1).getNode().getString());
        assertEquals("goog.foo", aliasUsages.get(2).getNode().getString());

        assertEquals(3, scopeCalls.size());
        for (Node scopeCall : scopeCalls) {
            assertEquals("goog.scope", scopeCall.getString());
        }

        // Check there are no errors reported
        assertEquals(0, result.getDiagnostics().size());

        // Check there are no unused names
        assertEquals(0, result.getDiagnosticsByCategory(DiagnosticCategory.UNUSED).size());
    }

    @Test
    public void testScopedAliasesErrors() throws Exception {
        String input = "package test;\n" +
                "\n" +
                "goog.scope(function() {\n" +
                "    function foo() { return \"foo\"; }\n" +
                "    return foo;\n" +
                "});\n" +
                "\n" +
                "goog.scope(function() {\n" +
                "    function foo() {}\n" +
                "    var bar = goog.foo;\n" +
                "    return bar;\n" +
                "});\n" +
                "\n" +
                "goog.scope(function() {\n" +
                "    function foo() {}\n" +
                "    var qux = goog.foo;\n" +
                "    var quux = goog.foo;\n" +
                "    return qux + quux;\n" +
                "});\n" +
                "\n" +
                "function main() {\n" +
                "}\n" +
                "\n" +
                "main();";

        CompilationUnit unit = new CompilationUnit(new DefaultEnvironment());
        unit.parse(input);

        Options options = new Options();
        options.addOption(AliasTransformation.aliasTransformationKey, true);

        DefaultCompiler compiler = new DefaultCompiler(new DiagnosticListener() {
            @Override
            public boolean handle(Diagnostic diagnostic, Environment context, int level) {
                if (level > Diagnostic.WARNING && diagnostic.getType() != DiagnosticType.UNUSED) {
                    // Ignore all errors and warnings except for unused names
                    return false;
                }
                DiagnosticType type = diagnostic.getType();
                if (type == GOOG_SCOPE_USED_IMPROPERLY ||
                    type == GOOG_SCOPE_HAS_BAD_PARAMETERS ||
                    type == GOOG_SCOPE_REFERENCES_THIS ||
                    type == GOOG_SCOPE_USES_RETURN ||
                    type == GOOG_SCOPE_USES_THROW) {
                    // Check for specific errors
                    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType().getName());
                    assertEquals(1, diagnostic.getStartPosition().getLineNumber());
                    assertEquals(1, diagnostic.getStartPosition().getColumnNumber());
                    assertEquals(input.indexOf("goog.scope(function() {\n"), diagnostic.getSourceFileName().indexOf(input));
                    assertEquals("goog.scope", diagnostic.getMessage());
                    assertEquals("The call to goog.scope must be alone in a single statement.", diagnostic.getExplanation());
                    return false;
                }
                return true;
            }
        }, options);

        ParseResult result = compiler.compile(unit.getSource(), unit.getSourceFileName());

        assertEquals(3, result.getDiagnostics().size());
        assertEquals(3, result.getDiagnosticsByCategory(DiagnosticCategory.ERROR).size());

        Diagnostic diagnostic1 = result.getDiagnostics().get(0);
        assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostic1.getType());

        Diagnostic diagnostic2 = result.getDiagnostics().get(1);
        assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostic2.getType());

        Diagnostic diagnostic3 = result.getDiagnostics().get(2);
        assertEquals(GOOG_SCOPE_USES_RETURN, diagnostic3.getType());
    }

    @Test
    public void testScopedAliasesAliasesRedefined() throws Exception {
        String input = "package test;\n" +
                "\n" +
                "goog.scope(function() {\n" +
                "    var foo = \"foo\";\n" +
                "    goog.scope(function() {\n" +
                "        var foo = \"bar\";\n" +
                "    });\n" +
                "});\n" +
                "\n" +
                "function main() {\n" +
                "    assertEquals(foo, \"foo\");\n" +
                "}\n" +
                "\n" +
                "main();";

        CompilationUnit unit = new CompilationUnit(new DefaultEnvironment());
        unit.parse(input);

        Options options = new Options();
        options.addOption(AliasTransformation.aliasTransformationKey, true);

        DefaultCompiler compiler = new DefaultCompiler(new DiagnosticListener() {
            @Override
            public boolean handle(Diagnostic diagnostic, Environment context, int level) {
                if (level > Diagnostic.WARNING && diagnostic.getType() != DiagnosticType.UNUSED) {
                    // Ignore all errors and warnings except for unused names
                    return false;
                }
                return true;
            }
        }, options);

        ParseResult result = compiler.compile(unit.getSource(), unit.getSourceFileName());

        assertEquals(1, result.getDiagnostics().size());
        assertEquals(1, result.getDiagnosticsByCategory(DiagnosticCategory.ERROR).size());

        Diagnostic diagnostic = result.getDiagnostics().get(0);
        assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostic.getType());
        assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostic.getType().getName());
        assertEquals(2, diagnostic.getStartPosition().getLineNumber());
        assertEquals(14, diagnostic.getStartPosition().getColumnNumber());
        assertEquals(input.indexOf("goog.scope(function() {\n"), diagnostic.getSourceFileName().indexOf(input));
        assertEquals("The alias 'foo' is assigned a value more than once.", diagnostic.getExplanation());
    }
}