package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;

import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.test_util.NodeMatcher;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique_simpleFunctionWithVariables_uniqueNamesGenerated() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    String jsCode = "function testFunction(a, b) {" +
        "var x = a * b;" +
        "return x;" +
        "}";

    Node rootNode = new TokenStream(jsCode.toCharArray(), 0, jsCode.length())
        .getRootNode();

    MakeDeclaredNamesUnique.traverse(null, rootNode, makeDeclaredNamesUnique);

    Node functionNode = NodeMatcher.findFunction("testFunction", rootNode);
    Node xNode = NodeMatcher.findVariable("x", functionNode);

    String expectedA = "a__" + Integer.toHexString(System.identityHashCode(makeDeclaredNamesUnique));
    String expectedB = "b__" + Integer.toHexString(System.identityHashCode(makeDeclaredNamesUnique));

    // Function name is not modified and remains "testFunction".
    assertThat(functionNode.getIdentifier().getName()).isEqualTo("testFunction");

    // Variable names are unique and have a suffix based on the number of
    // declarations of the same name.
    assertThat(xNode.getIdentifier().getName()).isEqualTo(expectedXName(expectedA, 0));
    assertThat(xNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedA);

    Node aNode = NodeMatcher.findVariable("a", functionNode);
    String expectedAName = expectedA;
    assertThat(aNode.getIdentifier().getName()).isEqualTo(expectedAName);
    assertThat(aNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedAName);

    Node bNode = NodeMatcher.findVariable("b", functionNode);
    String expectedBName = expectedB;
    assertThat(bNode.getIdentifier().getName()).isEqualTo(expectedBName);
    assertThat(bNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedBName);
  }

  @Test
  public void testMakeDeclaredNamesUnique_functionWithVariableAndFunctionDeclaration_uniqueNamesGenerated() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    String jsCode = "function testFunction(a, b) {" +
        "var x = a * b;" +
        "function innerFunction() {" +
        "var x = 42;" +
        "}" +
        "return x;" +
        "}";

    Node rootNode = new TokenStream(jsCode.toCharArray(), 0, jsCode.length())
        .getRootNode();

    MakeDeclaredNamesUnique.traverse(null, rootNode, makeDeclaredNamesUnique);

    Node functionNode = NodeMatcher.findFunction("testFunction", rootNode);
    Node innerFunctionNode = NodeMatcher.findFunction("innerFunction", functionNode);

    String expectedA = "a__" + Integer.toHexString(System.identityHashCode(makeDeclaredNamesUnique));
    String expectedB = "b__" + Integer.toHexString(System.identityHashCode(makeDeclaredNamesUnique));
    String expectedXInnerFunction = "xInnerFunction__" + Integer.toHexString(System.identityHashCode(makeDeclaredNamesUnique));

    // Function name is not modified and remains "testFunction".
    assertThat(functionNode.getIdentifier().getName()).isEqualTo("testFunction");

    // Variable names are unique and have a suffix based on the number of
    // declarations of the same name.
    Node xNode = NodeMatcher.findVariable("x", functionNode);
    assertThat(xNode.getIdentifier().getName())
        .isEqualTo(expectedXName(expectedA, 1));
    assertThat(xNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedA);

    Node aNode = NodeMatcher.findVariable("a", functionNode);
    String expectedAName = expectedA;
    assertThat(aNode.getIdentifier().getName()).isEqualTo(expectedAName);
    assertThat(aNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedAName);

    Node bNode = NodeMatcher.findVariable("b", functionNode);
    String expectedBName = expectedB;
    assertThat(bNode.getIdentifier().getName()).isEqualTo(expectedBName);
    assertThat(bNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedBName);

    Node innerFunctionXNode = NodeMatcher.findVariable("x", innerFunctionNode);
    String expectedXInnerFunctionName = expectedXInnerFunction;
    assertThat(innerFunctionXNode.getIdentifier().getName())
        .isEqualTo(expectedXInnerFunctionName);
    assertThat(innerFunctionXNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedXInnerFunctionName);
  }

  @Test
  public void testMakeDeclaredNamesUnique_functionWithGlobalVariable_uniqueNameGeneratedForGlobalVariable() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    String jsCode = "function testFunction() {" +
        "var globalVariable = 42;" +
        "return globalVariable;" +
        "}";

    Node rootNode = new TokenStream(jsCode.toCharArray(), 0, jsCode.length())
        .getRootNode();

    MakeDeclaredNamesUnique.traverse(null, rootNode, makeDeclaredNamesUnique);

    Node functionNode = NodeMatcher.findFunction("testFunction", rootNode);
    Node globalVariableNode = NodeMatcher.findVariable("globalVariable", rootNode);

    String expectedGlobalVariable = "globalVariable__" + Integer.toHexString(System.identityHashCode(makeDeclaredNamesUnique));

    // Function name is not modified and remains "testFunction".
    assertThat(functionNode.getIdentifier().getName()).isEqualTo("testFunction");

    // Global variable name is unique and has a suffix based on the number of
    // declarations of the same name.
    assertThat(globalVariableNode.getIdentifier().getName()).isEqualTo(expectedGlobalVariable);
    assertThat(globalVariableNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedGlobalVariable);
  }

  @Test
  public void testMakeDeclaredNamesUnique_functionWithGlobalVariableAndLocalVariable_uniqueNamesGenerated() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    String jsCode = "function testFunction() {" +
        "var globalVariable = 42;" +
        "var localVariable = globalVariable;" +
        "return localVariable;" +
        "}";

    Node rootNode = new TokenStream(jsCode.toCharArray(), 0, jsCode.length())
        .getRootNode();

    MakeDeclaredNamesUnique.traverse(null, rootNode, makeDeclaredNamesUnique);

    Node functionNode = NodeMatcher.findFunction("testFunction", rootNode);
    Node globalVariableNode = NodeMatcher.findVariable("globalVariable", rootNode);
    Node localVariableNode = NodeMatcher.findVariable("localVariable", functionNode);

    String expectedGlobalVariable = "globalVariable__" + Integer.toHexString(System.identityHashCode(makeDeclaredNamesUnique));
    String expectedLocalVariable = "localVariable__" + Integer.toHexString(System.identityHashCode(makeDeclaredNamesUnique));

    // Function name is not modified and remains "testFunction".
    assertThat(functionNode.getIdentifier().getName()).isEqualTo("testFunction");

    // Global variable name is unique and has a suffix based on the number of
    // declarations of the same name.
    assertThat(globalVariableNode.getIdentifier().getName()).isEqualTo(expectedGlobalVariable);
    assertThat(globalVariableNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedGlobalVariable);

    // Local variable name is unique and has a suffix based on the number of
    // declarations of the same name.
    assertThat(localVariableNode.getIdentifier().getName()).isEqualTo(expectedLocalVariable);
    assertThat(localVariableNode.getParentNode().getFirstChild().getString())
        .isEqualTo(expectedLocalVariable);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testMakeDeclaredNamesUnique_objectProperty_throwsUnsupportedOperationException() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    String jsCode = "var obj = {};" +
        "obj.property = 42";

    Node rootNode = new TokenStream(jsCode.toCharArray(), 0, jsCode.length())
        .getRootNode();

    MakeDeclaredNamesUnique.traverse(null, rootNode, makeDeclaredNamesUnique);

    // MakeDeclaredNamesUnique does not modify object properties.
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMakeDeclaredNamesUnique_invalidNode_throwsIllegalArgumentException() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    String jsCode = "";

    Node rootNode = new TokenStream(jsCode.toCharArray(), 0, jsCode.length())
        .getRootNode();

    // An empty Node should cause an IllegalArgumentException.
    MakeDeclaredNamesUnique.traverse(null, rootNode, makeDeclaredNamesUnique);
  }

  private String expectedXName(String name, int index) {
    return name + ContextualRenamer.UNIQUE_ID_SEPARATOR + index;
  }
}