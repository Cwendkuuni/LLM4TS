package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

 private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
 private NodeTraversal nodeTraversal;
 private Node rootNode;

 @Before
 public void setUp() {
 makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 nodeTraversal = new NodeTraversal(new CompilerOptions());
 rootNode = NodeUtil.parseJs("function foo() {" +
 "var x = 1, y = 1, z = 1, x = 2;" +
 "}", null);
 nodeTraversal.setRootNode(rootNode);
 nodeTraversal.setParser(new Parser());
 nodeTraversal.setCompiler(new Compiler());
 nodeTraversal.setTraversalMode(TreeWalker.TRAVERSE);
 nodeTraversal.traverse(makeDeclaredNamesUnique);
 }

 @Test
 public void testSimpleRename() {
 assertEquals("x1", rootNode.getChild(1).getFirstChild().getString());
 assertEquals("y1", rootNode.getChild(3).getFirstChild().getString());
 assertEquals("z1", rootNode.getChild(5).getFirstChild().getString());
 }

 @Test
 public void testGlobalRename() {
 assertEquals("x", rootNode.getChild(1).getFirstChild().getString());
 assertEquals("y", rootNode.getChild(3).getFirstChild().getString());
 assertEquals("z", rootNode.getChild(5).getFirstChild().getString());
 }

 @Test
 public void testFunctionBlockRename() {
 Node functionNode = rootNode.getChild(0);
 Node functionBody = functionNode.getLastChild();
 assertEquals("x1", functionBody.getChild(0).getFirstChild().getString());
 assertEquals("y1", functionBody.getChild(2).getFirstChild().getString());
 assertEquals("z1", functionBody.getChild(4).getFirstChild().getString());
 }

 @Test
 public void testFunctionRename() {
 Node functionDeclaration = rootNode.getChild(1);
 Node functionName = functionDeclaration.getFirstChild();
 assertEquals("x1", functionName.getString());
 }

 @Test
 public void testFunctionParameterRename() {
 Node functionDeclaration = rootNode.getChild(1);
 Node functionParameters = functionDeclaration.getFirstChild();
 assertEquals("x1", functionParameters.getChild(0).getFirstChild().getString());
 assertEquals("y1", functionParameters.getChild(1).getFirstChild().getString());
 }

 @Test
 public void testDeclaredNameCollision() {
 rootNode = NodeUtil.parseJs("function foo() {" +
 "var x = 1, x = 2;" +
 "}", null);
 nodeTraversal = new NodeTraversal(new CompilerOptions());
 nodeTraversal.setRootNode(rootNode);
 nodeTraversal.setParser(new Parser());
 nodeTraversal.setCompiler(new Compiler());
 nodeTraversal.setTraversalMode(TreeWalker.TRAVERSE);
 nodeTraversal.traverse(makeDeclaredNamesUnique);
 assertEquals("x1", rootNode.getChild(1).getFirstChild().getString());
 }

 @Test(expected = RuntimeException.class)
 public void testNameCollisionWithGlobal() {
 rootNode = NodeUtil.parseJs("var x = 1; function foo() {" +
 "var x = 2;" +
 "}", null);
 nodeTraversal = new NodeTraversal(new CompilerOptions());
 nodeTraversal.setRootNode(rootNode);
 nodeTraversal.setParser(new Parser());
 nodeTraversal.setCompiler(new Compiler());
 nodeTraversal.setTraversalMode(TreeWalker.TRAVERSE);
 try {
 makeDeclaredNamesUnique.traverse(nodeTraversal);
 fail("Expected a RuntimeException");
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testNameCollisionWithFunctionParameter() {
 rootNode = NodeUtil.parseJs("function foo(x) {" +
 "var x = 2;" +
 "}", null);
 nodeTraversal = new NodeTraversal(new CompilerOptions());
 nodeTraversal.setRootNode(rootNode);
 nodeTraversal.setParser(new Parser());
 nodeTraversal.setCompiler(new Compiler());
 nodeTraversal.setTraversalMode(TreeWalker.TRAVERSE);
 try {
 makeDeclaredNamesUnique.traverse(nodeTraversal);
 fail("Expected a RuntimeException");
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testNameCollisionWithFunctionName() {
 rootNode = NodeUtil.parseJs("function foo() {" +
 "function bar() {" +
 "var x = 1; " +
 "}", null);
 nodeTraversal = new NodeTraversal(new CompilerOptions());
 nodeTraversal.setRootNode(rootNode);
 nodeTraversal.setParser(new Parser());
 nodeTraversal.setCompiler(new Compiler());
 nodeTraversal.setTraversalMode(TreeWalker.TRAVERSE);
 try {
 makeDeclaredNamesUnique.traverse(nodeTraversal);
 fail("Expected a RuntimeException");
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testNameCollisionWithVarBlock() {
 rootNode = NodeUtil.parseJs("var x = 1;" +
 "function foo() {" +
 "var x = 2;" +
 "}", null);
 nodeTraversal = new NodeTraversal(new CompilerOptions());
 nodeTraversal.setRootNode(rootNode);
 nodeTraversal.setParser(new Parser());
 nodeTraversal.setCompiler(new Compiler());
 nodeTraversal.setTraversalMode(TreeWalker.TRAVERSE);
 try {
 makeDeclaredNamesUnique.traverse(nodeTraversal);
 fail("Expected a RuntimeException");
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testExceptionRename() {
 rootNode = NodeUtil.parseJs("function foo() {" +
 "try {" +
 "  throw new Error(\"foo\");" +
 "} catch (e) {" +
 "  throw new Error(\"bar\");" +
 "}" +
 "}", null);
 nodeTraversal = new NodeTraversal(new CompilerOptions());
 nodeTraversal.setRootNode(rootNode);
 nodeTraversal.setParser(new Parser());
 nodeTraversal.setCompiler(new Compiler());
 nodeTraversal.setTraversalMode(TreeWalker.TRAVERSE);
 nodeTraversal.traverse(makeDeclaredNamesUnique);
 assertEquals("e1", rootNode.getChild(1).getLastChild().getChild(0)
     .getLastChild().getChild(0).getFirstChild().getString());
 assertEquals("e2", rootNode.getChild(1).getLastChild().getChild(1)
     .getFirstChild().getString());
 }

 @Test
 public void testExceptionInFunctionRename() {
 rootNode = NodeUtil.parseJs("function foo() {" +
 "try {" +
 "  throw new Error(\"foo\");" +
 "} catch (e) {" +
 "  function bar() {" +
 "    throw new Error(\"bar\");" +
 "  }" +
 "}" +
 "}", null);
 nodeTraversal = new NodeTraversal(new CompilerOptions());
 nodeTraversal.setRootNode(rootNode);
 nodeTraversal.setParser(new Parser());
 nodeTraversal.setCompiler(new Compiler());
 nodeTraversal.setTraversalMode(TreeWalker.TRAVERSE);
 nodeTraversal.traverse(makeDeclaredNamesUnique);
 assertEquals("e1", rootNode.getChild(1).getLastChild().getChild(0)
     .getLastChild().getChild(0).getFirstChild().getString());
 assertEquals("e2", rootNode.getChild(1).getLastChild().getChild(1)
     .getChild(0).getFirstChild().getString());
 }

 @Test
 public void testExceptionInFunctionParameterRename() {
 rootNode = NodeUtil.parseJs("function foo(e) {" +
 "try {" +
 "  throw new Error(\"foo\");" +
 "} catch (e) {" +
 "  function bar() {" +
 "    throw new Error(\"bar\");" +
 "  }" +
 "}" +
 "}", null);
 nodeTraversal = new NodeTraversal(new CompilerOptions());
 nodeTraversal.setRootNode(rootNode);
 nodeTraversal.setParser(new Parser());
 nodeTraversal.setCompiler(new Compiler());
 nodeTraversal.setTraversalMode(TreeWalker.TRAVERSE);
 nodeTraversal.traverse(makeDeclaredNamesUnique);
 assertEquals("e1", rootNode.getChild(1).getLastChild().getChild(0)
     .getLastChild().getChild(0).getFirstChild().getString());
 assertEquals("e2", rootNode.getChild(1).getLastChild().getChild(1)
     .getFirstChild().getString());
 }

}