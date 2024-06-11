package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testContextualRenamer() {
    AbstractCompiler compiler = new Compiler();
    Node rootNode = TokenStream.parseUnsafe(
        "function foo() {\n" +
        "  var x = 1;\n" +
        "  function bar() {\n" +
        "    var x = 2;\n" +
        "  }\n" +
        "  var x = 3;\n" +
        "  function baz() {\n" +
        "    var x = 4;\n" +
        "  }\n" +
        "}"
    );

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    compiler.addPass(renamer);
    renamer.makeUnique(rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    AbstractPostOrderCallback visitor = new AbstractPostOrderCallback() {
      private int count = 0;

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("x")) {
          count++;
        }
      }
    };

    NodeTraversal.traverse(compiler, rootNode, visitor);
    assertEquals(4, count);
  }

  @Test
  public void testInlineRenamer() {
    AbstractCompiler compiler = new Compiler();
    Node rootNode = TokenStream.parseUnsafe(
        "function foo() {\n" +
        "  var x = 1;\n" +
        "  function bar() {\n" +
        "    var x = 2;\n" +
        "  }\n" +
        "  var x = 3;\n" +
        "  function baz() {\n" +
        "    var x = 4;\n" +
        "  }\n" +
        "}"
    );

    Map<String, String> declarations = Maps.newHashMap();
    String idPrefix = "Renamed_";
    boolean removeConstness = false;

    MakeDeclaredNamesUnique inlineRenamer = new MakeDeclaredNamesUnique(
        () -> idPrefix + Integer.toString(declarations.size())) {
      @Override
      public void addDeclaredName(String name) {
        if (!declarations.containsKey(name)) {
          declarations.put(name, getUniqueName(name));
        }
      }
    };

    compiler.addPass(inlineRenamer);
    inlineRenamer.makeUnique(rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    AbstractPostOrderCallback visitor = new AbstractPostOrderCallback() {
      private int count = 0;

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("x")) {
          count++;
        }
      }
    };

    NodeTraversal.traverse(compiler, rootNode, visitor);
    assertEquals(4, count);

    for (Map.Entry<String, String> entry : declarations.entrySet()) {
      System.out.println(entry.getKey() + " -> " + entry.getValue());
    }
  }

  @Test
  public void testContextualRenamerWithGlobalScope() {
    AbstractCompiler compiler = new Compiler();
    Node rootNode = TokenStream.parseUnsafe(
        "function foo() {\n" +
        "  var x = 1;\n" +
        "  function bar() {\n" +
        "    var x = 2;\n" +
        "  }\n" +
        "  var x = 3;\n" +
        "  function baz() {\n" +
        "    var x = 4;\n" +
        "  }\n" +
        "}"
    );

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    compiler.addPass(renamer);
    renamer.makeUnique(rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    AbstractPostOrderCallback visitor = new AbstractPostOrderCallback() {
      private int count = 0;
      Set<String> globalNames = ImmutableSet.of("x");

      @Override
      public void enterScope(NodeTraversal t) {
        if (t.inGlobalScope()) {
          globalNames.addAll(getDeclaredNames(t.getScope()));
        }
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME) {
          String name = n.getString();
          if (globalNames.contains(name)) {
            count++;
          }
        }
      }

      private Set<String> getDeclaredNames(Scope scope) {
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (Var var : scope.getVars()) {
          builder.add(var.name);
        }
        return builder.build();
      }
    };

    NodeTraversal.traverse(compiler, rootNode, visitor);
    assertEquals(3, count);
  }

  @Test
  public void testContextualRenamerWithFunctionScope() {
    AbstractCompiler compiler = new Compiler();
    Node rootNode = TokenStream.parseUnsafe("function foo() {}");

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    compiler.addPass(renamer);

    Node functionNode = TokenStream.parseUnsafe(
        "function bar() {\n" +
        "  var x = 1;\n" +
        "  function baz() {\n" +
        "    var x = 2;\n" +
        "  }\n" +
        "  var x = 3;\n" +
        "}"
    );

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    traversal.push(functionNode);

    renamer.makeUnique(rootNode);

    NodeTraversal.traverse(compiler, rootNode, new AbstractPostOrderCallback() {
      private int count = 0;

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("x")) {
          count++;
        }
      }

      @Override
      public void exitScope(NodeTraversal t) {
        if (t.getScope().getRootNode() == functionNode) {
          assertEquals(3, count);
        }
      }
    });
  }

  @Test
  public void testContextualRenamerWithBlockScope() {
    AbstractCompiler compiler = new Compiler();
    Node rootNode = TokenStream.parseUnsafe("function foo() {\nvar x = 1;\n}");

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    compiler.addPass(renamer);

    Node blockNode = new Node(Token.BLOCK);
    blockNode.setLine(1);
    blockNode.setColumn(1);
    Node functionNode = TokenStream.parseUnsafe("function bar() {\n");
    functionNode.setParent(rootNode);
    functionNode.setLine(2);
    functionNode.setColumn(1);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    traversal.push(functionNode);
    traversal.push(blockNode);

    Node varNode1 = new Node(Token.VAR);
    varNode1.setParent(blockNode);
    varNode1.setLine(3);
    varNode1.setColumn(1);
    varNode1.setString("x");

    Node varNode2 = new Node(Token.VAR);
    varNode2.setParent(blockNode);
    varNode2.setLine(3);
    varNode2.setColumn(3);
    varNode2.setString("x");

    Node funcNode = new Node(Token.FUNCTION);
    funcNode.setParent(blockNode);
    funcNode.setLine(4);
    funcNode.setColumn(1);
    funcNode.setString("baz");
    funcNode.setType(Token.FUNCTION);

    NodeTraversal.traverse(compiler, rootNode, new AbstractPostOrderCallback() {
      private int count = 0;

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("x")) {
          count++;
        }
      }

      @Override
      public void exitScope(NodeTraversal t) {
        if (t.getScope().getRootNode() == blockNode) {
          assertEquals(3, count);
        }
      }
    });
  }

  @Test
  public void testContextualRenamerWithBlockScopeAndGlobalScope() {
    AbstractCompiler compiler = new Compiler();
    Node rootNode = TokenStream.parseUnsafe("function foo() {\nvar x = 1;\n}");

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    compiler.addPass(renamer);

    Node blockNode = new Node(Token.BLOCK);
    blockNode.setLine(1);
    blockNode.setColumn(1);
    Node functionNode = TokenStream.parseUnsafe("function bar() {\n");
    functionNode.setParent(rootNode);
    functionNode.setLine(2);
    functionNode.setColumn(1);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    traversal.push(functionNode);
    traversal.push(blockNode);

    Node varNode1 = new Node(Token.VAR);
    varNode1.setParent(blockNode);
    varNode1.setLine(3);
    varNode1.setColumn(1);
    varNode1.setString("x");

    Node varNode2 = new Node(Token.VAR);
    varNode2.setParent(blockNode);
    varNode2.setLine(3);
    varNode2.setColumn(3);
    varNode2.setString("x");

    Node funcNode = new Node(Token.FUNCTION);
    funcNode.setParent(blockNode);
    funcNode.setLine(4);
    funcNode.setColumn(1);
    funcNode.setString("baz");
    funcNode.setType(Token.FUNCTION);

    Node globalVarNode = new Node(Token.VAR);
    globalVarNode.setParent(rootNode);
    globalVarNode.setLine(5);
    globalVarNode.setColumn(1);
    globalVarNode.setString("x");

    NodeTraversal.traverse(compiler, rootNode, new AbstractPostOrderCallback() {
      private int blockScopeCount = 0;
      private int globalScopeCount = 0;

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("x")) {
          if (parent == blockNode) {
            blockScopeCount++;
          } else {
            globalScopeCount++;
          }
        }
      }

      @Override
      public void exitScope(NodeTraversal t) {
        if (t.getScope().getRootNode() == blockNode) {
          assertEquals(2, blockScopeCount);
        } else if (t.getScope().getRootNode() == rootNode) {
          assertEquals(1, globalScopeCount);
        }
      }
    });
  }
}