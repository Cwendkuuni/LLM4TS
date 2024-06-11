package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.JSType;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(compiler, null);

    String sourceCode = "function MyClass() {}";
    Node externs = NodeUtil.parseJsCode(sourceCode);
    Node root = NodeUtil.parseJsCode("function test() { new MyClass(); }");
    typeChecker.process(externs, root);

    TreeSet<String> markerNames = new TreeSet<>(Comparator.comparing(s -> s));
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isFunctionDeclaration(n)) {
          FunctionType funcType = (FunctionType) n.getJSType();
          if (funcType.isConstructor()) {
            Node prototype = NodeUtil.getFunctionPrototype(n);
            for (ObjectType marker : funcType.getAllImplementedInterfaces()) {
              markerNames.add("implements__" + marker.getReferenceName());
            }
            markerNames.add("instance_of__" + funcType.getReferenceName());
          }
        }
      }
    });

    for (String markerName : markerNames) {
      assertTrue(iteratorHasNodeWithName(root, markerName));
    }
  }

  @Test
  public void testAddChecks() {
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(compiler, null);

    String sourceCode = "function MyClass() {} function test() {" +
        "function foo() {} return new MyClass(); }";
    Node externs = NodeUtil.parseJsCode(sourceCode);
    Node root = NodeUtil.parseJsCode(sourceCode);
    typeChecker.process(externs, root);

    TreeSet<String> checkerNames = new TreeSet<>(Comparator.comparing(s -> s));
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isFunction(n)) {
          FunctionType funcType = (FunctionType) n.getJSType();
          if (funcType.isConstructor()) {
            Node constructor = n;
            for (Node child = constructor.getFirstChild(); child != null;
                child = child.getNext()) {
              if (child.getType() == Token.VAR && child.getFirstChild().getText().equals("this")) {
                Node checkNode = child.getCallee();
                if (checkNode.getType() == Token.CALL) {
                  Node checkerName = checkNode.getFirstChild();
                  checkerNames.add(checkerName.getText());
                }
                break;
              }
            }
          }
        }
      }
    });

    for (String checkerName : checkerNames) {
      assertTrue(iteratorHasNodeWithName(root, "checkType." + checkerName));
    }
  }

  @Test
  public void testNullChecks() {
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(compiler, null);

    String sourceCode = "function test() { return null; }";
    Node externs = NodeUtil.parseJsCode("");
    Node root = NodeUtil.parseJsCode(sourceCode);
    typeChecker.process(externs, root);

    TreeSet<String> checkerNames = new TreeSet<>(Comparator.comparing(s -> s));
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isFunction(n)) {
          FunctionType funcType = (FunctionType) n.getJSType();
          if (funcType.isConstructor()) {
            Node constructor = n;
            for (Node child = constructor.getFirstChild(); child != null;
                child = child.getNext()) {
              if (child.getType() == Token.VAR && child.getFirstChild().getText().equals("this")) {
                Node checkNode = child.getCallee();
                if (checkNode.getType() == Token.CALL) {
                  Node checkerName = checkNode.getFirstChild();
                  checkerNames.add(checkerName.getText());
                  if (checkerName.getText().equals("nullChecker")) {
                    assertTrue(iteratorHasNodeWithName(root, "checkType.nullChecker"));
                    return;
                  }
                }
              }
            }
          }
        }
      }
    });

    assertNull(iteratorHasNodeWithName(root, "checkType.nullChecker"));
  }

  @Test
  public void testClassChecks() {
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(compiler, null);

    String sourceCode = "function MyClass() {} function test() {" +
        "function foo() {" +
        "class Bar {} return new MyClass() instanceof Bar;" +
        "} }";
    Node externs = NodeUtil.parseJsCode(sourceCode);
    Node root = NodeUtil.parseJsCode(sourceCode);
    typeChecker.process(externs, root);

    TreeSet<String> checkerNames = new TreeSet<>(Comparator.comparing(s -> s));
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isFunction(n)) {
          FunctionType funcType = (FunctionType) n.getJSType();
          if (funcType.isConstructor()) {
            Node constructor = n;
            for (Node child = constructor.getFirstChild(); child != null;
                child = child.getNext()) {
              if (child.getType() == Token.VAR && child.getFirstChild().getText().equals("this")) {
                Node checkNode = child.getCallee();
                if (checkNode.getType() == Token.CALL) {
                  Node checkerName = checkNode.getFirstChild();
                  checkerNames.add(checkerName.getText());
                  if (checkerName.getText().startsWith("classChecker")) {
                    String className = checkerName.getText().substring(10);
                    assertTrue(iteratorHasNodeWithName(root, "jscomp.typecheck.classChecker." + className));
                    return;
                  }
                }
              }
            }
          }
        }
      }
    });

    for (String checkerName : checkerNames) {
      if (!checkerName.startsWith("nullChecker") && !checkerName.startsWith("valueChecker")) {
        assertTrue(iteratorHasNodeWithName(root, "jscomp.typecheck.classChecker." + checkerName));
      }
    }
  }

  @Test
  public void testInterfaceChecks() {
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(compiler, null);

    String sourceCode = "interface MyInterface {} function test() {" +
        "function foo() {" +
        "class Bar implements MyInterface {} return new Bar() instanceof MyInterface;" +
        "} }";
    Node externs = NodeUtil.parseJsCode(sourceCode);
    Node root = NodeUtil.parseJsCode(sourceCode);
    typeChecker.process(externs, root);

    TreeSet<String> checkerNames = new TreeSet<>(Comparator.comparing(s -> s));
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isFunction(n)) {
          FunctionType funcType = (FunctionType) n.getJSType();
          if (funcType.isConstructor()) {
            Node constructor = n;
            for (Node child = constructor.getFirstChild(); child != null;
                child = child.getNext()) {
              if (child.getType() == Token.VAR && child.getFirstChild().getText().equals("this")) {
                Node checkNode = child.getCallee();
                if (checkNode.getType() == Token.CALL) {
                  Node checkerName = checkNode.getFirstChild();
                  checkerNames.add(checkerName.getText());
                  if (checkerName.getText().startsWith("interfaceChecker")) {
                    String interfaceName = checkerName.getText().substring(12);
                    assertTrue(iteratorHasNodeWithName(root, "jscomp.typecheck.interfaceChecker." + interfaceName));
                    return;
                  }
                }
              }
            }
          }
        }
      }
    });

    for (String checkerName : checkerNames) {
      if (checkerName.startsWith("classChecker")) continue;
      assertTrue(iteratorHasNodeWithName(root, "jscomp.typecheck.interfaceChecker." + checkerName));
    }
  }

  private static boolean iteratorHasNodeWithName(Node root, String name) {
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getText().equals(name)) {
        return true;
      }
    }
    return false;
  }
}