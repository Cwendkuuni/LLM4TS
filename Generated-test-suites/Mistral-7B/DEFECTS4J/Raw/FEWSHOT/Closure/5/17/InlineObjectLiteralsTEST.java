package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    CompilerPass pass = new InlineObjectLiterals(
        new AbstractCompiler(null), new AlwaysNameNodeSupplier()) {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
        new InliningBehavior(this) {
          @Override
          public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
            super.afterExitScope(t, referenceMap);
            // Test case for inlining an object literal with only one property
            checkInlineObject("var obj = {'foo': 'bar'};", "var foo = 'bar';");
            // Test case for inlining an object literal with multiple properties
            checkInlineObject("var obj = {'foo': 'bar1', 'baz': 'bar2'};",
                "var foo = 'bar1';\n" +
                "var baz = 'bar2';");
            // Test case for inlining an object literal with a self-referential property
            checkInlineObject("var obj = {" +
                "  'foo': {'bar': obj}," +
                "  'baz': 'qux'" +
                "};",
                "var foo = {" +
                "  'bar': {baz: 'qux'}," +
                "  'baz': 'qux'" +
                "};");
            // Test case for inlining an object literal with a cyclic reference
            checkInlineObject("var obj1 = {" +
                "  'foo': obj2," +
                "  'baz': 'qux'" +
                "};\n" +
                "var obj2 = {" +
                "  'foo': obj1," +
                "  'baz': 'quux'" +
                "};",
                "var obj1 = {" +
                "  'foo': {baz: 'quux'}," +
                "  'baz': 'qux'" +
                "};\n" +
                "var obj2 = {" +
                "  'foo': {baz: 'qux'}," +
                "  'baz': 'quux'" +
                "};");
            // Test case for inlining an object literal with only a single initializer
            checkInlineObject("var obj = {'foo': function() {}};",
                "var foo = function() {};");
            // Test case for inlining an object literal with multiple initializer expressions
            checkInlineObject("var obj = {" +
                "  'foo': {'bar': 1, 'baz': 2}," +
                "  'qux': function() {}};",
                "var foo = {" +
                "  'bar': 1," +
                "  'baz': 2" +
                "};\n" +
                "var qux = function() {};");
            // Test case for inlining an object literal with a getter property
            checkInlineObject("var obj = {" +
                "  get getFoo() {" +
                "    return 'bar';" +
                "  }," +
                "  'baz': 'qux'" +
                "};",
                "var getFoo;" +
                "var obj = {" +
                "  get getFoo: {" +
                "    value: function() {return 'bar';}" +
                "  }," +
                "  'baz': 'qux'" +
                "};" +
                "Object.defineProperty(obj, 'foo', getFoo);");
            // Test case for inlining an object literal with a setter property
            checkInlineObject("var obj = {" +
                "  set setFoo(val) {" +
                "    this.bar = val;" +
                "  }," +
                "  'baz': 'qux'" +
                "};",
                "var obj = {" +
                "  'baz': 'qux'" +
                "};" +
                "Object.defineProperty(obj, 'foo', {" +
                "  get: function() {return this.bar;}," +
                "  set: function(val) {this.bar = val;}" +
                "});");
          }
        }.process(externs, root);
      }
    }.process(null, null);

    // The root node of the processed code
    Node root = pass.getCompiler().getCurrentNode();
    assertEquals("", root.toString());
  }

  private void checkInlineObject(String code, String expected) {
    // Create a new compiler context for this test
    CompilerContext ctx = new CompilerContext();
    AbstractCompiler compiler = new AbstractCompiler(ctx);
    compiler.setCodingConvention(new CodingConvention() {
      @Override
      public boolean isExported(String name) {
        return false;
      }
    });
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT5);
    compiler.init(ctx, code, options);

    // Run the InlineObjectLiterals pass and check that the output is as expected
    Node originalCode = compiler.getCurrentNode();
    String output = compiler.getSourceFile().getContent();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new AlwaysNameNodeSupplier());
    pass.process(null, originalCode);
    assertEquals(expected, output);
  }
}

class AlwaysNameNodeSupplier implements Supplier<String> {
  @Override
  public String get() {
    return "ALWAYS_NAME";
  }
}