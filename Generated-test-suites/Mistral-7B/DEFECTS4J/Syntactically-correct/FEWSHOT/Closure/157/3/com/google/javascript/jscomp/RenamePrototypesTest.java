package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

@RunWith(JUnit4.class)
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    // Set up a simple test case with a single prototype and an object literal that references it.
    String sourceCode = "function Foo() {} Foo.prototype.bar = function() { return 42; } var obj = { bar: Foo.prototype.bar };";
    Node rootNode = new TokenStream(sourceCode).parse();

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPasses(renamePrototypes);
    passManager.runPasses(rootNode);

    // Check that the prototype property was renamed
    Node prototypePropertyNode = rootNode.getFirstChild().getNext().getFirstChild();
    assertNotNull(prototypePropertyNode);
    assertEquals("b", prototypePropertyNode.getString());

    // Check that the object literal property references the renamed prototype property
    Node objNode = rootNode.getChild(1);
    assertNotNull(objNode);
    Node objPropertyNode = objNode.getFirstChild();
    assertNotNull(objPropertyNode);
    assertEquals("b", objPropertyNode.getString());
  }
}