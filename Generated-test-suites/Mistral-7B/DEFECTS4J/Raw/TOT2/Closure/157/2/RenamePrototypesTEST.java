import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperties() {
    String input = "function Foo() {} Foo.prototype.bar = function() {};";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    CompilerResults results = testRenamePrototypeProperties(input, options);
    assertEquals("function Foo() {} Foo.prototype.baz = function() {};", results.toString());
  }

  @Test
  public void testRenamePrototypePropertiesWithAggressiveRenaming() {
    String input = "function Foo() {} Foo.prototype.bar = function() {};";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap()) {
      @Override
      public boolean isAggressiveRenaming() {
        return true;
      }
    };
    CompilerResults results = testRenamePrototypeProperties(input, options);
    assertEquals("function Foo() {} Foo.prototype._bar = function() {};", results.toString());
  }

  @Test
  public void testRenamePrototypePropertiesWithPrivateNames() {
    String input = "function Foo() {} Foo.prototype._bar = function() {};";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    CompilerResults results = testRenamePrototypeProperties(input, options);
    assertEquals("function Foo() {} Foo.prototype._bar = function() {};", results.toString());
  }

  @Test
  public void testRenamePrototypePropertiesWithCustomRenaming() {
    String input = "function Foo() {} Foo.prototype.bar = function() {};";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap()) {
      @Override
      public String getRenamedName(String name) {
        return "baz_" + name;
      }
    };
    CompilerResults results = testRenamePrototypeProperties(input, options);
    assertEquals("function Foo() {} Foo.prototype.baz__bar = function() {};", results.toString());
  }

  @Test
  public void testRenamePrototypePropertiesWithReservedNames() {
    String input = "function Foo() {} Foo.prototype.indexOf = function() {};";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    assertThrows(IllegalArgumentException.class, () -> testRenamePrototypeProperties(input, options));
  }

  @Test
  public void testRenamePrototypePropertiesWithExternedNames() {
    String input = "function Foo() { export function bar() {} } Foo.prototype.bar = function() {};";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    CompilerResults results = testRenamePrototypeProperties(input, options);
    assertEquals("function Foo() { export function bar() {} } Foo.prototype.bar = function() {};", results.toString());
  }

  @Test
  public void testRenamePrototypePropertiesWithConflictingNames() {
    String input = "function Foo() {} Foo.prototype.foo = function() {}; Object.prototype.foo = function() {}; Foo.prototype.bar = function() {};";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    CompilerResults results = testRenamePrototypeProperties(input, options);
    assertEquals("function Foo() {} Foo.prototype.bar = function() {}; Object.prototype.foo = function() {};", results.toString());
  }

  @Test
  public void testRenameObjLitProperties() {
    String input = "var obj = { bar: 1 };";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    CompilerResults results = testRenameObjLitProperties(input, options);
    assertEquals("var obj = { baz: 1 };", results.toString());
  }

  @Test
  public void testRenameObjLitPropertiesWithAggressiveRenaming() {
    String input = "var obj = { bar: 1 };";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap()) {
      @Override
      public boolean isAggressiveRenaming() {
        return true;
      }
    };
    CompilerResults results = testRenameObjLitProperties(input, options);
    assertEquals("var obj = { _bar: 1 };", results.toString());
  }

  @Test
  public void testRenameObjLitPropertiesWithCustomRenaming() {
    String input = "var obj = { bar: 1 };";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap()) {
      @Override
      public String getRenamedName(String name) {
        return "baz_" + name;
      }
    };
    CompilerResults results = testRenameObjLitProperties(input, options);
    assertEquals("var obj = { baz__bar: 1 };", results.toString());
  }

  @Test
  public void testRenameObjLitPropertiesWithConflictingNames() {
    String input = "var obj = { bar: 1, foo: 2 }; var
  baz = { bar: 3 }; obj.bar = 4; baz.bar = 5;";
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    CompilerResults results = testRenameObjLitProperties(input, options);
    assertEquals("var obj = { baz__bar: 1, foo: 2 }; var baz = { baz__bar: 3 }; obj.baz__bar = 4; baz.baz__bar = 5;", results.toString());
  }

  private CompilerResults testRenamePrototypeProperties(String input, CompilerOptions options) {
    CompilerPass pass = new RenamePrototypes(new AbstractCompiler(), options.isAggressiveRenaming(), null, null);
    CompilerResults results = new CompilerResults();
    CompilerInput inputNode = new CompilerInput("test.js", input, "test.js", 1, 1, 1);
    pass.process(inputNode.getTree(), inputNode.getRootNode());
    return results;
  }

  private CompilerResults testRenameObjLitProperties(String input, CompilerOptions options) {
    CompilerPass pass = new RenamePrototypes(new AbstractCompiler(), options.isAggressiveRenaming(), null, null);
    CompilerResults results = new CompilerResults();
    CompilerInput inputNode = new CompilerInput("test.js", input, "test.js", 1, 1, 1);
    Node rootNode = new Node(Token.PROGRAM, 1, 1, 1);
    pass.process(inputNode.getTree(), rootNode);
    results.setRootNode(rootNode);
    results.setTree(inputNode.getTree());
    return results;
  }
}