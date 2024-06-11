package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testContextualRenamer() {
        // Test a simple case where no names need to be renamed.
        String code = "function foo() {} function bar() {}";
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        String transformedCode = makeDeclaredNamesUnique.process(code, null);
        assertEquals(code, transformedCode);

        // Test a case where names need to be renamed.
        code = "function foo() {} function foo() {}";
        transformedCode = makeDeclaredNamesUnique.process(code, null);
        assertEquals("function foo1() {} function foo2() {}", transformedCode);

        // Test a case where names need to be renamed and functions have parameters.
        code = "function foo(a) {} function foo(b) {}";
        transformedCode = makeDeclaredNamesUnique.process(code, null);
        assertEquals("function foo1(a) {} function foo2(b) {}", transformedCode);

        // Test a case where names need to be renamed and functions have recursive calls.
        code = "function foo(a) { function bar(b) { foo(b); } } function foo(a) { }";
        transformedCode = makeDeclaredNamesUnique.process(code, null);
        assertEquals("function foo1(a) { function bar1(b) { foo2(b); } } function foo2(a) { }", transformedCode);

        // Test a case where names need to be renamed and functions are nested.
        code = "function foo() { function bar() {} } function foo() { function bar() {} }";
        transformedCode = makeDeclaredNamesUnique.process(code, null);
        assertEquals("function foo1() { function bar1() {} } function foo2() { function bar2() {} ", transformedCode);
    }

    @Test
    public void testInlineRenamer() {
        // Test a simple case where no names need to be renamed.
        String code = "function foo() {} function bar() {}";
        MakeDeclaredNamesUnique.InlineRenamer makeDeclaredNamesUnique = new MakeDeclaredNamesUnique.InlineRenamer(
            () -> "_i", "", false);
        String transformedCode = makeDeclaredNamesUnique.process(code, null, new MakeDeclaredNamesUnique());
        assertEquals(code, transformedCode);

        // Test a case where names need to be renamed.
        code = "function foo() {} function foo() {}";
        transformedCode = makeDeclaredNamesUnique.process(code, null, new MakeDeclaredNamesUnique());
        assertEquals("function foo_i() {} function foo_i1() {}", transformedCode);
        assertEquals("function foo_i() {} function foo_i1() {}", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i"));
        assertEquals("function foo_i1() {} function foo_i() {}", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i1"));

        // Test a case where names need to be renamed and functions have parameters.
        code = "function foo(a) {} function foo(b) {}";
        transformedCode = makeDeclaredNamesUnique.process(code, null, new MakeDeclaredNamesUnique());
        assertEquals("function foo_i(a) {} function foo_i1(b) {}", transformedCode);
        assertEquals("function foo_i(a) {} function foo_i1(b) {}", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i"));
        assertEquals("function foo_i1(b) {} function foo_i(a) {}", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i1"));

        // Test a case where names need to be renamed and functions have recursive calls.
        code = "function foo(a) { function bar(b) { foo(b); } } function foo(a) { }";
        transformedCode = makeDeclaredNamesUnique.process(code, null, new MakeDeclaredNamesUnique());
        assertEquals("function foo_i(a) { function bar_i(b) { foo_i1(b); } } function foo_i1(a) { }", transformedCode);
        assertEquals("function foo_i(a) { function bar_i(b) { foo_i1(b); } } function foo_i1(a) { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i"));
        assertEquals("function foo_i1(a) { function bar_i(b) { foo_i2(b); } } function foo_i(a) { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i1"));
        assertEquals("function foo_i1(a) { function bar_i(b) { foo_i2(b); } } function foo_i(a) { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i1"));
        assertEquals("function foo_i2(b) { function bar_i(b) { foo_i3(b); } } function foo_i1(a) { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i2"));
        assertEquals("function foo_i2(b) { function bar_i(b) { foo_i3(b); } } function foo_i1(a) { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i1"));
        assertEquals("function foo_i3(b) { function bar_i(b) { foo_i(b); } } function foo_i1(a) { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i3"));
        assertEquals("function foo_i(a) { function bar_i(b) { foo_i(b); } } function foo_i1(a) { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i"));

        // Test a case where names need to be renamed and functions are nested.
        code = "function foo() { function bar() {} } function foo() { function bar() {} }";
        transformedCode = makeDeclaredNamesUnique.process(code, null, new MakeDeclaredNamesUnique());
        assertEquals("function foo_i() { function bar_i() {} } function foo_i1() { function bar_i1() {} ", transformedCode);
        assertEquals("function foo_i() { function bar_i() {} } function foo_i1() { function bar_i1() {} ", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i"));
        assertEquals("function foo_i1() { function bar_i1() {} } function foo_i() { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i1"));
        assertEquals("function foo_i() { function bar_i() {} } function foo_i1() { function bar_i() {} ", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i"));
        assertEquals("function foo_i1() { function bar_i() {} } function foo_i() { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i1"));

        // Test a case where names need to be renamed and constants are removed.
        code = "const foo = 1; function foo() {}";
        transformedCode = makeDeclaredNamesUnique.process(code, null, new MakeDeclaredNamesUnique());
        assertTrue(makeDeclaredNamesUnique.getRenamer().getChildRenamer().stripConstIfReplaced());
        assertEquals("function _i(a) { const _i1 = 1; } function foo_i(a) { }", transformedCode);
        assertNull(makeDeclaredNamesUnique.getRenamer().getReplacementName("foo"));
        assertEquals("function _i(a) { const _i1 = 1; } function foo_i(a) { }", makeDeclaredNamesUnique.getRenamer().getReplacementName("foo_i"));

        // Test a case where names need to be renamed and constants are not removed.
        code = "const foo = 1; function foo() {}";
        MakeDeclaredNamesUnique.InlineRenamer makeDeclaredNamesUnique2 = new MakeDeclaredNamesUnique.InlineRenamer(() -> "_i2", "", false);
        transformedCode = makeDeclaredNamesUnique2.process(code, null, new MakeDeclaredNamesUnique());
        assertFalse(makeDeclaredNamesUnique2.getRenamer().getChildRenamer().stripConstIfReplaced());
        assertEquals("const _i = 1; function foo_i(a) { }", transformedCode);
        assertEquals("const _i = 1; function foo_i(a) { }", makeDeclaredNamesUnique2.getRenamer().getReplacementName("foo"));
    }

    @Test
    public void testGetOrginalName() {
        String name = "foo$$bar$$baz";
        String originalName = GetOrginalNameUtil.getOrginalName(name);
        assertEquals("foo", originalName);
    }

    @Test
    public void testGetNameSuffix() {
        String name = "foo$$bar$$baz";
        String suffix = GetOrginalNameUtil.getNameSuffix(name, name.lastIndexOf("$$"));
        assertEquals("bar", suffix);
    }

    @Test
    public void testGetNameSuffixWithoutSeparator() {
        String name = "foo";
        String suffix = GetOrginalNameUtil.getNameSuffix(name, -1);
        assertNull(suffix);
    }

    @Test
    public void testGetOrginalNameWithoutSeparator() {
        String name = "foo";
        String originalName = GetOrginalNameUtil.getOrginalName(name);
        assertEquals("foo", originalName);
    }

    private static class GetOrginalNameUtil {
        static String getOrginalName(String name) {
            int index = name.lastIndexOf("$$");
            if (index != -1) {
                return name.substring(0, index);
            }
            return name;
        }

        static String getNameSuffix(String name, int index) {
            if (index == -1) {
                throw new IndexOutOfBoundsException();
            }
            return name.substring(index + 1, name.length());
        }
    }

    private MakeDeclaredNamesUnique getRenamer() {
        return new MakeDeclaredNamesUnique();
    }
}