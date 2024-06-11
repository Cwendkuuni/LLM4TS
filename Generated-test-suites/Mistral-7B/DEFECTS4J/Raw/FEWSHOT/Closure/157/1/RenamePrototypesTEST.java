package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class RenamePrototypesTest {

  @Test
  public void testProcess() {
    // Test case with empty input
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
    Node root = new Node();
    Node externs = new Node();
    renamePrototypes.process(externs, root);

    // Test case with no properties to rename
    TokenStream ts = new TokenStream(null);
    Node input = new Node(Token.PROGRAM, null, ts);
    AbstractCompiler compiler = new AbstractCompiler(input, null, null, new LifeCycleStage());
    RenamePrototypes renamePrototypes1 = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes1.process(externs, root);

    // Test case with only prototype properties
    String prototypeCode =
        "function Foo() {} Foo.prototype.bar = function() {}; Foo.prototype.baz = function() {};";
    TokenStream tsPrototype = new TokenStream(prototypeCode);
    Node inputPrototype = new Node(Token.PROGRAM, null, tsPrototype);
    AbstractCompiler compilerPrototype = new AbstractCompiler(inputPrototype, null, null, new LifeCycleStage());
    RenamePrototypes renamePrototypes2 = new RenamePrototypes(compilerPrototype, false, null, null);
    renamePrototypes2.process(externs, root);

    // Test case with only object literal properties
    String objectLiteralCode =
        "var obj = { bar: function() {}, baz: function() {} };";
    TokenStream tsObjectLiteral = new TokenStream(objectLiteralCode);
    Node inputObjectLiteral = new Node(Token.PROGRAM, null, tsObjectLiteral);
    AbstractCompiler compilerObjectLiteral = new AbstractCompiler(inputObjectLiteral, null, null, new LifeCycleStage());
    RenamePrototypes renamePrototypes3 = new RenamePrototypes(compilerObjectLiteral, false, null, null);
    renamePrototypes3.process(externs, root);

    // Test case with both prototype and object literal properties
    String code =
        "function Foo() {} Foo.prototype.bar = function() {}; Foo.prototype.baz = function() {}; " +
        "var obj = { bar: function() {}, baz: function() {} };";
    TokenStream ts = new TokenStream(code);
    Node input = new Node(Token.PROGRAM, null, ts);
    AbstractCompiler compiler = new AbstractCompiler(input, null, null, new LifeCycleStage());
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(externs, root);

    // Test case with aggressive renaming
    String aggressiveCode =
        "function Foo() {} Foo.prototype.MycustomProp = function() {}; Foo.prototype.bar = function() {}; " +
        "var obj = { MycustomProp: function() {}, bar: function() {} };";
    TokenStream tsAggressive = new TokenStream(aggressiveCode);
    Node inputAggressive = new Node(Token.PROGRAM, null, tsAggressive);
    AbstractCompiler compilerAggressive = new AbstractCompiler(inputAggressive, null, null, new LifeCycleStage());
    RenamePrototypes renamePrototypesAggressive = new RenamePrototypes(compilerAggressive, true, null, null);
    renamePrototypesAggressive.process(externs, root);
  }

  @Test
  public void testGetPropertyMap() {
    String code =
        "function Foo() {} Foo.prototype.bar = function() {}; Foo.prototype.baz = function() {}; " +
        "var obj = { bar: function() {}, baz: function() {} };";
    TokenStream ts = new TokenStream(code);
    Node input = new Node(Token.PROGRAM, null, ts);
    AbstractCompiler compiler = new AbstractCompiler(input, null, null, new LifeCycleStage());
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(externs, root);
    VariableMap propertyMap = renamePrototypes.getPropertyMap();

    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("baz", "baz_1");
    expectedMap.put("bar", "bar_1");

    assertEquals(expectedMap, propertyMap);
  }
}