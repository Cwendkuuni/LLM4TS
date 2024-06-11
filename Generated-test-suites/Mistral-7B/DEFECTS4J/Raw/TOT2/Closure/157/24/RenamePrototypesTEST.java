import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.Rewriter;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenamePrototypesTest {

  @org.junit.Test
  public void testRenamePrototypeProperties() throws Exception {
    String input = "function Foo() {}\n" +
      "Foo.prototype.getBar = function() { return 1; };\n" +
      "Foo.prototype.getBaz = function() { return 2; };\n" +
      "var foo = new Foo();\n" +
      "assertEquals(1, foo.getBar());\n" +
      "assertEquals(2, foo.getBaz());";

    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);

    Parser parser = new Parser(new NodeFactory());
    TokenStream ts = new TokenStream(parser.parse(input, null, null));
    Compiler compiler = new Compiler(options, ts);
    compiler.addPass(new RenamePrototypes(compiler, true, null, null));

    Rewriter rewriter = new Rewriter(compiler, null);
    Node ast = rewriter.visit(compiler.getRootNode(), null);

    String output = new TokenStream(rewriter.tokenStream()).getSource();

    assertEquals(input, output);

    // Check that the prototype properties were renamed
    Property getBarProp = getProperty("getBar");
    Property getBazProp = getProperty("getBaz");

    assertEquals("getRenamedBar", getBarProp.newName);
    assertEquals("getRenamedBaz", getBazProp.newName);
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithExterns() throws Exception {
    String input = "function Foo() {}\n" +
      "Foo.prototype.getBar = function() { return 1; };\n" +
      "Foo.prototype.getBaz = function() { return 2; };\n" +
      "// extern getQuux function() { return 3; }\n" +
      "var foo = new Foo();\n" +
      "assertEquals(1, foo.getBar());\n" +
      "assertEquals(2, foo.getBaz());\n" +
      "assertEquals(3, getQuux());";

    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);

    Parser parser = new Parser(new NodeFactory());
    TokenStream ts = new TokenStream(parser.parse(input, null, null));
    Compiler compiler = new Compiler(options, ts);
    compiler.addPass(new RenamePrototypes(compiler, true, null, null));

    Rewriter rewriter = new Rewriter(compiler, null);
    Node ast = rewriter.visit(compiler.getRootNode(), null);

    String output = new TokenStream(rewriter.tokenStream()).getSource();

    assertEquals(input, output);

    // Check that the prototype properties were renamed, but getQuux was left alone
    Property getBarProp = getProperty("getBar");
    Property getBazProp = getProperty("getBaz");
    Property getQuuxProp = getProperty("getQuux");

    assertEquals("getRenamedBar", getBarProp.newName);
    assertEquals("getRenamedBaz", getBazProp.newName);
    assertEquals("getQuux", getQuuxProp.newName);
  }

  @org.junit.Test
  public void testRenameObjLitProperties() throws Exception {
    String input = "var obj = {\n" +
      "  getBar: function() { return 1; },\n" +
      "  getBaz: function() { return 2 },\n" +
      "};\n" +
      "assertEquals(1, obj.getBar());\n" +
      "assertEquals(2, obj.getBaz());";

    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);

    Parser parser = new Parser(new NodeFactory());
    TokenStream ts = new TokenStream(parser.parse(input, null, null));
    Compiler compiler = new Compiler(options, ts);
    compiler.addPass(new RenamePrototypes(compiler, true, null, null));

    Rewriter rewriter = new Rewriter(compiler, null);
    Node ast = rewriter.visit(compiler.getRootNode(), null);

    String output = new TokenStream(rewriter.tokenStream()).getSource();

    assertEquals(input, output);

    // Check that the obj lit properties were renamed
    Property getBarProp = getProperty("getBar");
    Property getBazProp = getProperty("getBaz");

    assertEquals("getRenamedBar", getBarProp.newName);
    assertEquals("getRenamedBaz", getBazProp.newName);
  }

  @org.junit.Test
  public void testDoNotRenamePrototypePropertiesWithLeadingUnderscore() throws Exception {
    String input = "function Foo() {}\n" +
      "Foo.prototype._getBar = function() { return 1; };\n" +
      "Foo.prototype._getBaz = function() { return 2; };\n" +
      "var foo = new Foo();\n" +
      "assertEquals(1, foo._getBar());\n" +
      "assertEquals(2, foo._getBaz());";

    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);

    Parser parser = new Parser(new NodeFactory());
    TokenStream ts = new TokenStream(parser.parse(input, null, null));
    Compiler compiler = new Compiler(options, ts);
    compiler.addPass(new RenamePrototypes(compiler, true, null, null));

    Rewriter rewriter = new Rewriter(compiler, null);
    Node ast = rewriter.visit(compiler.getRootNode(), null);

    String output = new TokenStream(rewriter.tokenStream()).getSource();

    assertEquals(input, output);

    // Check that the prototype properties with leading underscores were not renamed
    Property getBarProp = getProperty("_getBar");
    Property getBazProp = getProperty("_getBaz");

    assertEquals("_getBar", getBarProp.oldName);
    assertEquals("_getBaz", getBazProp.oldName);
    assertEquals(null, getBarProp.newName);
    assertEquals(null, getBazProp.newName);
  }

  @org.junit.Test
  public void testDoNotRenameObjLitPropertiesWithLeadingUnderscore() throws Exception {
    String input = "var obj = {\n" +
      "  _getBar: function() { return 1; },\n" +
      "  _getBaz: function() { return 2 },\n" +
      "};\n" +
      "assertEquals(1, obj._getBar());\n" +
      "assertEquals(2, obj._getBaz());";

    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);

    Parser parser = new Parser(new NodeFactory());
    TokenStream ts = new TokenStream(parser.parse(input, null, null));
    Compiler compiler = new Compiler(options, ts);
    compiler.addPass(new RenamePrototypes(compiler, true, null, null));

    Rewriter rewriter = new Rewriter(compiler, null);
    Node ast = rewriter.visit(compiler.getRootNode(), null);

    String output = new TokenStream(rewriter.tokenStream()).getSource();

    assertEquals(input, output);

    // Check that the obj lit properties with leading underscores were not renamed
    Property getBarProp = getProperty("_getBar");
    Property getBazProp = getProperty("_getBaz");

    assertEquals("_getBar", getBarProp.oldName);
    assertEquals("_getBaz", getBazProp.oldName);
    assertEquals(null, getBarProp.newName);
    assertEquals(null, getBazProp.newName);
  }

  @org.junit.Test
  public void testDoNotRenameExternedProperties() throws Exception {
    String input = "function Foo() {}\n" +
      "// extern getQuux function() { return 3; }\n" +
      "Foo.prototype.getQuux = function() { return 1; };\n" +
      "var foo = new Foo();\n" +
      "assertEquals(1, foo.getQuux());\n" +
      "assertEquals(3, getQuux());";

    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);

    Parser parser = new Parser(new NodeFactory());
    TokenStream ts = new TokenStream(parser.parse(input, null, null));
    Compiler compiler = new Compiler(options, ts);
    compiler.addPass(new RenamePrototypes(compiler, true, null, null));

    Rewriter rewriter = new Rewriter(compiler, null);
    Node ast = rewriter.visit(compiler.getRootNode(), null);

    String output = new TokenStream(rewriter.tokenStream()).getSource();

    assertEquals(input, output);

    // Check that the externed property was not renamed
    Property getQuuxProp = getProperty("getQuux");

    assertEquals("getQuux", getQuuxProp.oldName);
    assertEquals("getQuux", getQuuxProp.newName);
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithNoLeadingOrTrailingUnderscores() throws Exception {
    String input = "function Foo() {}\n" +
      "Foo.prototype.getFoo = function() { return 1; };\n" +
      "Foo.prototype.getBar = function() { return 2; };\n" +
      "var foo = new Foo();\n" +
      "assertEquals(1, foo.getFoo());\n" +
      "assertEquals(2, foo.getBar());";

    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);
    options.setAggressiveRenaming(false);

    Parser parser = new Parser(new NodeFactory());
    TokenStream ts = new TokenStream(parser.parse(input, null, null));
    Compiler compiler = new Compiler(options, ts);
    compiler.addPass(new RenamePrototypes(compiler, false, null, null));

    Rewriter rewriter = new Rewriter(compiler, null);
    Node ast = rewriter.visit(compiler.getRootNode(), null);

    String output = new TokenStream(rewriter.tokenStream()).getSource();

    assertEquals(input, output);

    // Check that the prototype properties without leading or trailing underscores were not renamed
    Property getFooProp = getProperty("getFoo");
    Property getBarProp = getProperty("getBar");

    assertEquals("getFoo", getFooProp.oldName);
    assertEquals("getFoo", getFooProp.newName);
    assertEquals("getBar", getBarProp.oldName);
    assertEquals("getBar", getBarProp.newName);
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithMixedLeadingUnderscores() throws Exception {
    String input = "function Foo() {}\n" +
      "Foo.prototype._getFoo = function() { return 1; };\n" +
      "Foo.prototype.getBar = function() { return 2; };\n" +
      "var foo = new Foo();\n" +
      "assertEquals(1, foo._getFoo());\n" +
      "assertEquals(2, foo.getBar());";

    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);
    options.setAggressiveRenaming(false);

    Parser parser = new Parser(new NodeFactory());
    TokenStream ts = new TokenStream(parser.parse(input, null, null));
    Compiler compiler = new Compiler(options, ts);
    compiler.addPass(new RenamePrototypes(compiler, false, null, null));

    Rewriter rewriter = new Rewriter(compiler, null);
    Node ast = rewriter.visit(compiler.getRootNode(), null);

    String output = new TokenStream(rewriter.tokenStream()).getSource();

    assertEquals(input, output);

    // Check that the prototype properties with mixed leading underscores were renamed
    Property getFooProp = getProperty("_getFoo");
    Property getBarProp = getProperty("getBar");

    assertEquals("_getRenamedFoo", getFooProp.newName);
    assertEquals("getBar", getBarProp.oldName);
    assertEquals("getBar", getBarProp.newName);
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithAggressiveRenaming() throws Exception {
    String input = "function Foo() {}\n" +
      "Foo.prototype.getFoo = function() { return 1; };\n" +
      "Foo.prototype.getBar = function() { return 2; };\n" +
      "var foo = new Foo();\n" +
      "assertEquals(1, foo.getFoo());\n" +
      "assertEquals(2, foo.getBar());";

    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);
    options.setAggressiveRenaming(true);

    Parser parser = new Parser(new NodeFactory());
    TokenStream ts = new TokenStream(parser.parse(input, null, null));
    Compiler compiler = new Compiler(options, ts);
    compiler.addPass(new RenamePrototypes(compiler, true, null, null));

    Rewriter rewriter = new Rewriter(compiler, null);
    Node ast = rewriter.visit(compiler.getRootNode(), null);

    String output = new TokenStream(rewriter.tokenStream()).getSource();

    assertEquals(input, output);

    // Check that the prototype properties were renamed aggressively
    Property getFooProp = getProperty("getFoo");
    Property getBarProp = getProperty("getBar");

    assertEquals("getAggressivelyRenamedFoo", getFooProp.newName);
    assertEquals("getAggressivelyRenamedBar", getBarProp.newName);
  }

  private Property getProperty(String name) {
    return getProperties().get(name);
  }

  private Map<String, Property> getProperties() {
    return ((RenamePrototypes) compiler.getPassManager().getPass(
             RenamePrototypes.class)).properties;
  }

  private Compiler compiler;
}