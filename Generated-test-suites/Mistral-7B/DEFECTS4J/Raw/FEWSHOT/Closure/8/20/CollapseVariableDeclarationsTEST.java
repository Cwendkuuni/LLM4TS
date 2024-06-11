package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

/**
 * Test class for CollapseVariableDeclarations.
 */
public class CollapseVariableDeclarationsTest {
    @Test
    public void testCollapseVariableDeclarations() {
        // Given
        Node root = createNode("var a; var b = 1; var c = 2;");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("var a, b = 1, c = 2;");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithAssignments() {
        // Given
        Node root = createNode("var a = 1; var b = 2; a = 3; b = 4;");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("var a = 1, b = 2; a = 3, b = 4;");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithFunction() {
        // Given
        Node root = createNode("function foo() { var a; var b = 1; }");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("function foo() { var a, b = 1; }");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithNestedIf() {
        // Given
        Node root = createNode("if (true) { var a; } if (true) { var b; }");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("if (true) { var a; } if (true) { } var b;");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithEmptyLine() {
        // Given
        Node root = createNode("var a;\nvar b = 1;\n");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("var a, b = 1;");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithEmptyDeclaration() {
        // Given
        Node root = createNode("var ;");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("var ;");

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithMultipleDeclarationsPerFunction() {
        // Given
        Node root = createNode("function foo() { var a, b, c; }");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("function foo() { var a, b, c; }");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithEmptyFunction() {
        // Given
        Node root = createNode("function foo() {}");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("function foo() {}");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithTemplateLiterals() {
        // Given
        Node root = createNode("var a = `foo`; var b = `bar`;");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("var a = `foo`, b = `bar`;");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithDefaultValues() {
        // Given
        Node root = createNode("function foo(a = 1, b = 2) {}");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("function foo(a = 1, b = 2) {}");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithClassDeclaration() {
        // Given
        Node root = createNode("class Foo {}");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("class Foo {}");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithImportDeclaration() {
        // Given
        Node root = createNode("import 'foo'; import 'bar';");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("import 'foo'; import 'bar';");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithFunctionDeclaration() {
        // Given
        Node root = createNode("function foo(a) { var x = a; } function bar(a) { var y = a; }");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("function foo(a) { var x = a; } function bar(a) { var y = a; }");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithClassProperty() {
        // Given
        Node root = createNode("class Foo { var x; }");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("class Foo { var x; }");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithConstantDeclaration() {
        // Given
        Node root = createNode("const a = 1; const b = 2;");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("const a = 1, b = 2;");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithExportDefaultDeclaration() {
        // Given
        Node root = createNode("export default function foo() {} export default function bar() {}");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("export default function foo() {} export default function bar() {}");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithExportNamedDeclaration() {
        // Given
        Node root = createNode("export var a = 1; export var b = 2;");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("export var a = 1, b = 2;");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithExportAllDeclaration() {
        // Given
        Node root = createNode("export { a, b }; export { c, d };");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("export { a, b }, { c, d };");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithFunctionExpression() {
        // Given
        Node root = createNode("var x = function foo() {}; var y = function bar() {};");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("var x = function foo() {}, y = function bar() {};");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithLetStatement() {
        // Given
        Node root = createNode("let x = 1; let y = 2;");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("let x = 1, y = 2;");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void testCollapseVariableDeclarationsWithConstStatement() {
        // Given
        Node root = createNode("const x = 1; const y = 2;");

        // When
        CompilerPass pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        // Then
        Node result = root;
        Node expected = createNode("const x = 1, y = 2;");

        while (result.getNext() != null) {
            result = result.getNext();
            expected = expected.getNext();
        }

        assertThat(expected).isEqualTo(result);
    }

    private Node createNode(String js) {
        var compiler = new Compiler();
        compiler.parse(js, null, null);
        return compiler.getRootNode();
    }
}