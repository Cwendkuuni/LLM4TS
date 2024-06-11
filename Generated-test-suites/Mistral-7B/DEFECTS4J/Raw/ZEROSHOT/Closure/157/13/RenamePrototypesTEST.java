import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for the RenamePrototypes compiler pass
 */
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("foo.prototype.getBar = function() { return 1; };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node prototype = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.GETPROP && "prototype".equals(c.getString())) {
        prototype = c.getFirstChild();
        break;
      }
    }

    Preconditions.checkState(prototype != null);

    Node fn = prototype.getLastChild();
    assertEquals("getRenamedBar", fn.getString());
  }

  @Test
  public void testRenamePrototypeProperties() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("foo.prototype.getBar = function() { return 1; };\nfoo.prototype.getBaz = function() { return 2; };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node prototype = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.GETPROP && "prototype".equals(c.getString())) {
        prototype = c.getFirstChild();
        break;
      }
    }

    Preconditions.checkState(prototype != null);

    Node getBar = prototype.getFirstChild();
    Node getRenamedBar = getBar.getLastChild();
    assertEquals("getRenamedBar", getRenamedBar.getString());

    Node getBaz = getBar.getNext();
    Node getRenamedBaz = getBaz.getLastChild();
    assertEquals("getRenamedBaz", getRenamedBaz.getString());
  }

  @Test
  public void testRenameObjLitProperties() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("var foo = { getBar : function() { return 1; }, getBaz : function() { return 2; } };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node objLit = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.VAR) {
        String name = c.getFirstChild().getString();
        if ("foo".equals(name)) {
          objLit = c.getNext();
          break;
        }
      }
    }

    Preconditions.checkState(objLit != null);

    Node getBar = objLit.getFirstChild();
    Node getRenamedBar = getBar.getLastChild();
    assertEquals("getRenamedBar", getRenamedBar.getString());

    Node getBaz = getBar.getNext();
    Node getRenamedBaz = getBaz.getLastChild();
    assertEquals("getRenamedBaz", getRenamedBaz.getString());
  }

  @Test
  public void testRenamePrototypePropertiesWithReservedWords() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("foo.prototype.indexOf = function() { return 1; };\nfoo.prototype.lastIndexOf = function() { return 2; };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    // Should not rename "indexOf" and "lastIndexOf"
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node prototype = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.GETPROP && "prototype".equals(c.getString())) {
        prototype = c.getFirstChild();
        break;
      }
    }

    Preconditions.checkState(prototype != null);

    Node getIndexOf = prototype.getFirstChild();
    assertEquals("indexOf", getIndexOf.getString());

    Node getLastIndexOf = getIndexOf.getNext();
    assertEquals("lastIndexOf", getLastIndexOf.getString());
  }

  @Test
  public void testRenamePrototypePropertiesWithAggressiveRenaming() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("foo.prototype.toString = function() { return 1; };\nfoo.prototype.valueOf = function() { return 2; };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    // Should rename "toString" and "valueOf"
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node prototype = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.GETPROP && "prototype".equals(c.getString())) {
        prototype = c.getFirstChild();
        break;
      }
    }

    Preconditions.checkState(prototype != null);

    Node toString = prototype.getFirstChild();
    Node getRenamedToString = toString.getLastChild();
    assertNotEquals("toString", getRenamedToString.getString());

    Node valueOf = toString.getNext();
    Node getRenamedValueOf = valueOf.getLastChild();
    assertNotEquals("valueOf", getRenamedValueOf.getString());
  }

  @Test
  public void testRenamePrototypePropertiesWithAggressiveRenamingAndReservedWords() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("foo.prototype.toString = function() { return 1; };\nfoo.prototype.valueOf = function() { return 2; };\nfoo.prototype.lastIndexOf = function() { return 3; };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    // Should rename "toString" and "valueOf", but not "lastIndexOf"
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node prototype = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.GETPROP && "prototype".equals(c.getString())) {
        prototype = c.getFirstChild();
        break;
      }
    }

    Preconditions.checkState(prototype != null);

    Node toString = prototype.getFirstChild();
    Node getRenamedToString = toString.getLastChild();
    assertNotEquals("toString", getRenamedToString.getString());

    Node valueOf = toString.getNext();
    Node getRenamedValueOf = valueOf.getLastChild();
    assertNotEquals("valueOf", getRenamedValueOf.getString());

    Node lastIndexOf = valueOf.getNext();
    assertEquals("lastIndexOf", lastIndexOf.getString());
  }

  @Test
  public void testRenamePrototypePropertiesWithReservedNamesInInput() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("foo.prototype.toString = function() { return 1; };\nfoo.prototype.valueOf = function() { return 2; };\nfoo.prototype.indexOf = function() { return 3; };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    // Should not rename "indexOf"
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node prototype = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.GETPROP && "prototype".equals(c.getString())) {
        prototype = c.getFirstChild();
        break;
      }
    }

    Preconditions.checkState(prototype != null);

    Node toString = prototype.getFirstChild();
    Node getRenamedToString = toString.getLastChild();
    assertNotEquals("toString", getRenamedToString.getString());

    Node valueOf = toString.getNext();
    Node getRenamedValueOf = valueOf.getLastChild();
    assertNotEquals("valueOf", getRenamedValueOf.getString());

    Node indexOf = valueOf.getNext();
    assertEquals("indexOf", indexOf.getString());
  }

  @Test
  public void testRenamePrototypePropertiesWithSameNameInPrototypeAndObjLit() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("foo.prototype.getBar = function() { return 1; };\nvar foo = { getBar : function() { return 2; } };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    // Should not rename "getBar" since it is used in both prototype and obj lit
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node prototype = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.GETPROP && "prototype".equals(c.getString())) {
        prototype = c.getFirstChild();
        break;
      }
    }

    Node prototypeGetBar = prototype.getFirstChild();
    Node getRenamedPrototypeGetBar = prototypeGetBar.getLastChild();
    assertEquals("getBar", getRenamedPrototypeGetBar.getString());

    Node objLit = prototype.getNext();
    Node objLitGetBar = objLit.getFirstChild();
    Node getRenamedObjLitGetBar = objLitGetBar.getLastChild();
    assertEquals("getBar", getRenamedObjLitGetBar.getString());
  }

  @Test
  public void testRenamePrototypePropertiesWithDifferentNamesInPrototypeAndObjLit() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("foo.prototype.getBar = function() { return 1; };\nvar foo = { getBaz : function() { return 2; } };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    // Should rename "getBar" in prototype and leave "getBaz" in obj lit unchanged
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node prototype = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.GETPROP && "prototype".equals(c.getString())) {
        prototype = c.getFirstChild();
        break;
      }
    }

    Node prototypeGetBar = prototype.getFirstChild();
    Node getRenamedPrototypeGetBar = prototypeGetBar.getLastChild();
    assertNotEquals("getBar", getRenamedPrototypeGetBar.getString());

    Node objLit = prototype.getNext();
    Node objLitGetBaz = objLit.getFirstChild();
    Node getRenamedObjLitGetBaz = objLitGetBaz.getLastChild();
    assertEquals("getBaz", getRenamedObjLitGetBaz.getString());
  }

  @Test
  public void testRenamePrototypePropertiesWithAggressiveRenamingAndDifferentNamesInPrototypeAndObjLit() {
    AbstractCompiler compiler = new AbstractCompiler();
    TokenStream input = new TokenStream(new StringReader("foo.prototype.getBar = function() { return 1; };\nvar foo = { getBaz : function() { return 2; } };"));
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    // Should rename "getBar" in prototype and leave "getBaz" in obj lit unchanged
    CompilerResults result = compiler.process(input, null);

    assertEquals(AbstractCompiler.LifeCycleStage.NORMALIZED_OBFUSCATED,
                 result.getLifeCycleStage());

    Node root = result.getOutput();
    Node prototype = null;
    for (Node c = root; c != null; c = c.getNext()) {
      if (c.getType() == Token.GETPROP && "prototype".equals(c.getString())) {
        prototype = c.getFirstChild();
        break;
      }
    }

    Node prototypeGetBar = prototype.getFirstChild();
    Node getRenamedPrototypeGetBar = prototypeGetBar.getLastChild();
    String renamedPrototypeGetBar = getRenamedPrototypeGetBar.getString();
    assertNotEquals("getBar", getRenamedPrototypeGetBar.getString());

    Node objLit = prototype.getNext();
    Node objLitGetBaz = objLit.getFirstChild();
    Node getRenamedObjLitGetBaz = objLitGetBaz.getLastChild();
    assertEquals("getBaz", getRenamedObjLitGetBaz.getString());

    // Test that the renamed prototype property does not collide with the obj lit property
    assertEquals(renamedPrototypeGetBar, "getRenamedBar");
  }
}

class StringReader {
  private final String text;

  public StringReader(String text) {
    this.text = text;
  }

  @Override
  public int read(CharBuffer cb, int max) {
    int bytesRead = 0;
    for (int i = 0; i < text.length() && bytesRead < max; ++i) {
      char c = text.charAt(i);
      cb.append(c);
      bytesRead++;
    }
    return bytesRead;
  }

  @Override
  public synchronized void close() {}
}