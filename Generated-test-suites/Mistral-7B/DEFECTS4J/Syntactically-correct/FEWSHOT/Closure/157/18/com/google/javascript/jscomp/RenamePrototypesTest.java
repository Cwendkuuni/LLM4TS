package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassCallback;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

@RunWith(JUnit4.class)
public class RenamePrototypesTest {

  @Test
  public void testProcess() {
    String input = "function Foo() {} Foo.prototype.bar = function() {}; Foo.prototype = {"
        + " getBar: function() { }, bar: 42 };";

    CompilerInput inputNode = new CompilerInput(input, "test.js");

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    passManager.addPass(renamePrototypes);

    Node root = inputNode.getAST();
    renamePrototypes.process(inputNode.getExterns(), root);

    NodeTraversal traversal = NodeTraversal.forFullTree(compiler, root);
    traversal.traverse(new CompilerPassCallback<Node>() {
      @Override
      public void processNode(NodeTraversal nt, Node node) {
        if (node.getType() == Token.GETPROP || node.getType() == Token.GETELEM) {
          Node firstChild = node.getFirstChild();
          assertNotNull("Expected a STRING node as first child", firstChild);
          assertEquals("bar", firstChild.getString());
        }
      }
    });
  }

  @Test
  public void testAggressiveRenaming() {
    String input = "function Foo() {} Foo.prototype.bar = function() {}; Foo.prototype = {"
        + " getBar: function() { }, bar: 42 };";

    CompilerInput inputNode = new CompilerInput(input, "test.js");

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    passManager.addPass(renamePrototypes);

    Node root = inputNode.getAST();
    renamePrototypes.process(inputNode.getExterns(), root);

    NodeTraversal traversal = NodeTraversal.forFullTree(compiler, root);
    traversal.traverse(new CompilerPassCallback<Node>() {
      @Override
      public void processNode(NodeTraversal nt, Node node) {
        if (node.getType() == Token.GETPROP || node.getType() == Token.GETELEM) {
          Node firstChild = node.getFirstChild();
          if (firstChild != null) {
            String oldName = firstChild.getString();
            if (oldName.equals("bar")) {
              String newName = renamePrototypes.getPropertyMap().lookupNewName("bar");
              assertEquals("Renamed name should not be null", newName, firstChild.getString());
            }
          }
        }
      }
    });
  }

  @Test
  public void testReservedNames() {
    String input = "function Foo() {} Foo.prototype.indexOf = function() {};"
        + " Foo.prototype.lastIndexOf = function() {}; Foo.prototype.toString = function() {};"
        + " Foo.prototype.valueOf = function() {}; Foo.prototype.bar = function() {};";

    CompilerInput inputNode = new CompilerInput(input, "test.js");

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    passManager.addPass(renamePrototypes);

    Node root = inputNode.getAST();
    renamePrototypes.process(inputNode.getExterns(), root);

    NodeTraversal traversal = NodeTraversal.forFullTree(compiler, root);
    traversal.traverse(new CompilerPassCallback<Node>() {
      @Override
      public void processNode(NodeTraversal nt, Node node) {
        if (node.getType() == Token.GETPROP || node.getType() == Token.GETELEM) {
          Node firstChild = node.getFirstChild();
          if (firstChild != null) {
            String oldName = firstChild.getString();
            if (Arrays.asList("indexOf", "lastIndexOf", "toString", "valueOf").contains(oldName)) {
              assertNull("Expected name to be null", firstChild.getString());
            } else {
              String newName = renamePrototypes.getPropertyMap().lookupNewName(oldName);
              if (newName == null) {
                assertNull("Expected name to be null", firstChild.getString());
              } else {
                assertEquals("Renamed name should match expected name", oldName, newName);
              }
            }
          }
        }
      }
    });
  }

  @Test
  public void testNoRename() {
    String input = "function Foo() {} Foo.prototype.bar = function() {};"
        + " Foo.prototype.getBar = function() {}; Foo.prototype.bar = 42;";

    CompilerInput inputNode = new CompilerInput(input, "test.js");

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    passManager.addPass(renamePrototypes);

    Node root = inputNode.getAST();
    renamePrototypes.process(inputNode.getExterns(), root);

    NodeTraversal traversal = NodeTraversal.forFullTree(compiler, root);
    traversal.traverse(new CompilerPassCallback<Node>() {
      @Override
      public void processNode(NodeTraversal nt, Node node) {
        if (node.getType() == Token.GETPROP || node.getType() == Token.GETELEM) {
          Node firstChild = node.getFirstChild();
          if (firstChild != null) {
            String oldName = firstChild.getString();
            if (oldName.equals("getBar")) {
              assertNull("Expected name to be null", firstChild.getString());
            } else {
              String newName = renamePrototypes.getPropertyMap().lookupNewName(oldName);
              if (newName == null) {
                assertEquals("Expected name to be unchanged", oldName, firstChild.getString());
              } else {
                assertNull("Expected name to be null", firstChild.getString());
              }
            }
          }
        }
      }
    });
  }

  @Test
  public void testPrototypeAssignment() {
    String input = "function Foo() {} Foo.prototype = { getBar: function() {} };";

    CompilerInput inputNode = new CompilerInput(input, "test.js");

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    passManager.addPass(renamePrototypes);

    Node root = inputNode.getAST();
    renamePrototypes.process(inputNode.getExterns(), root);

    NodeTraversal traversal = NodeTraversal.forFullTree(compiler, root);
    traversal.traverse(new CompilerPassCallback<Node>() {
      @Override
      public void processNode(NodeTraversal nt, Node node) {
        if (node.getType() == Token.GETPROP || node.getType() == Token.GETELEM) {
          Node firstChild = node.getFirstChild();
          if (firstChild != null && firstChild.getType() == Token.STRING) {
            String oldName = firstChild.getString();
            String newName = renamePrototypes.getPropertyMap().lookupNewName(oldName);
            assertEquals("Expected renamed name to match", oldName, newName);
          }
        }
      }
    });
  }

  @Test
  public void testObjLitAssignment() {
    String input = "function Foo() {} Foo.prototype = { getBar: function() {} };"
        + " Foo.prototype = { bar: 42 };";

    CompilerInput inputNode = new CompilerInput(input, "test.js");

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    passManager.addPass(renamePrototypes);

    Node root = inputNode.getAST();
    renamePrototypes.process(inputNode.getExterns(), root);

    NodeTraversal traversal = NodeTraversal.forFullTree(compiler, root);
    traversal.traverse(new CompilerPassCallback<Node>() {
      @Override
      public void processNode(NodeTraversal nt, Node node) {
        if (node.getType() == Token.GETPROP || node.getType() == Token.GETELEM) {
          Node firstChild = node.getFirstChild();
          if (firstChild != null && firstChild.getType() == Token.STRING) {
            String oldName = firstChild.getString();
            if (oldName.equals("bar")) {
              assertNull("Expected name to be null", firstChild.getString());
            } else {
              String newName = renamePrototypes.getPropertyMap().lookupNewName(oldName);
              if (newName == null) {
                assertEquals("Expected name to be unchanged", oldName, firstChild.getString());
              } else {
                assertNull("Expected name to be null", firstChild.getString());
              }
            }
          }
        }
      }
    });
  }
}