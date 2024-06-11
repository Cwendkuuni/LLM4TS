package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RenamePrototypesTest {

    private RenamePrototypes renamePrototypes;
    private TokenStream tokenStream;

    @Before
    public void setUp() {
        tokenStream = new TokenStream(new StringBuilder());
        Node rootNode = compileTestCode("function Foo() {} Foo.prototype.bar = function() {};");
        renamePrototypes = new RenamePrototypes(null, true, null, null);
        renamePrototypes.process(null, rootNode);
    }

    @Test
    public void testProcess_SimplePrototypeProperty() {
        Map<String, String> expectedProperties = Maps.newHashMap();
        expectedProperties.put("bar", "bar_");

        Map<String, String> actualProperties = renamePrototypes.getPropertyMap().getProperties();
        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void testProcess_AggressiveRenaming() {
        Map<String, String> expectedProperties = Maps.newHashMap();
        expectedProperties.put("Foo", "Foo_");
        expectedProperties.put("bar", "bar__");

        compileTestCode("function Foo() {} Foo.prototype.bar = function() {};");
        Map<String, String> actualProperties = renamePrototypes.getPropertyMap().getProperties();
        assertEquals(expectedProperties, actualProperties);
    }

    @Test(expected = AssertionError.class)
    public void testProcess_ExternedName() {
        compileTestCode("String.prototype.toString = function() {};");
        renamePrototypes = new RenamePrototypes(null, true, null, null);
        renamePrototypes.process(null, tokenStream.getRootNode());
        fail("Expected an AssertionError when trying to rename an externed name");
    }

    @Test
    public void testProcess_PrivateName() {
        Map<String, String> expectedProperties = Maps.newHashMap();
        expectedProperties.put("_bar", "_bar");

        compileTestCode("function Foo() { var _bar = 0; } Foo.prototype._bar = function() {};");
        Map<String, String> actualProperties = renamePrototypes.getPropertyMap().getProperties();
        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void testProcess_ObjectLiteralProperty() {
        Map<String, String> expectedProperties = Maps.newHashMap();
        expectedProperties.put("foo", "foo_");

        compileTestCode("var obj = { foo: 0 };");
        renamePrototypes = new RenamePrototypes(null, true, null, null);
        renamePrototypes.process(null, tokenStream.getRootNode());
        Map<String, String> actualProperties = renamePrototypes.getPropertyMap().getProperties();
        assertEquals(expectedProperties, actualProperties);
    }

    @Test(expected = AssertionError.class)
    public void testProcess_ObjectLiteralPropertyWithQuotedName() {
        compileTestCode("var obj = { \"foo\": 0 };");
        renamePrototypes = new RenamePrototypes(null, true, null, null);
        renamePrototypes.process(null, tokenStream.getRootNode());
        fail("Expected an AssertionError when trying to rename an object literal property with a quoted name");
    }

    @Test
    public void testProcess_PrototypeAssignedToObjectLiteral() {
        Map<String, String> expectedProperties = Maps.newHashMap();
        expectedProperties.put("prototype", "prototype_");

        compileTestCode("var obj = { prototype: 0 }; Foo.prototype = obj;");
        renamePrototypes = new RenamePrototypes(null, true, null, null);
        renamePrototypes.process(null, tokenStream.getRootNode());
        Map<String, String> actualProperties = renamePrototypes.getPropertyMap().getProperties();
        assertEquals(expectedProperties, actualProperties);
    }

    private Node compileTestCode(String code) {
        TokenStream ts = new TokenStream(new StringBuilder(code));
        AbstractCompiler compiler = new AbstractCompiler();
        Node rootNode = compiler.parseTree(ts, null, null, null, null);
        return rootNode;
    }
}