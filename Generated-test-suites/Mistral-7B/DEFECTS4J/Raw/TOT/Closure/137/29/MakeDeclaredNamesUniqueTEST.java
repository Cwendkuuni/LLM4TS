package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node rootNode = createNode("function test() {}");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse(rootNode);

    verifyDeclaredNames(makeDeclaredNamesUnique, rootNode);
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithFunctionDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node rootNode = createNode("function test() {\n" +
        "  function innerFunction() {} \n" +
        "}");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse(rootNode);

    verifyDeclaredNames(makeDeclaredNamesUnique, rootNode);
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithVarDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node rootNode = createNode("function test() {\n" +
        "  var a = 5;\n" +
        "  var a = 10;\n" +
        "}");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse(rootNode);

    verifyDeclaredNames(makeDeclaredNamesUnique, rootNode);
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithNestedFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node rootNode = createNode("function test() {\n" +
        "  function outerFunction() {\n" +
        "    function innerFunction() {} \n" +
        "  }\n" +
        "}");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse(rootNode);

    verifyDeclaredNames(makeDeclaredNamesUnique, rootNode);
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithConstantDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node rootNode = createNode("function test() {\n" +
        "  const a = 5;\n" +
        "  var a = 10;\n" +
        "}");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse(rootNode);

    verifyDeclaredNames(makeDeclaredNamesUnique, rootNode);
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithGlobalDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node rootNode = createNode("var a = 5; function test() {}");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse(rootNode);

    verifyDeclaredNames(makeDeclaredNamesUnique, rootNode);
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithFunctionCall() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node rootNode = createNode("function test() {\n" +
        "  test(); \n" +
        "}");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse(rootNode);

    verifyDeclaredNames(makeDeclaredNamesUnique, rootNode);
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithException() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node rootNode = createNode("try {\n" +
        "  throw new Error('test'); \n" +
        "} catch (e) {}");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse(rootNode);

    verifyDeclaredNames(makeDeclaredNamesUnique, rootNode);
  }

  private void verifyDeclaredNames(MakeDeclaredNamesUnique makeDeclaredNamesUnique, Node node) {
    List<String> expectedDeclaredNames = ImmutableSet.of(
        "test", "innerFunction", "outerFunction"
    ).asList();

    Multiset<String> actualDeclaredNames = Sets.newHashSet();
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        String replacementName = makeDeclaredNamesUnique.getReplacementName(name);
        if (replacementName != null) {
          actualDeclaredNames.add(replacementName);
        } else {
          actualDeclaredNames.add(name);
        }
      }
      verifyDeclaredNames(makeDeclaredNamesUnique, child);
    }

    Iterator<String> iterator = actualDeclaredNames.iterator();
    List<String> actualOrderedDeclaredNames = Lists.newArrayList(iterator);

    assertThat(actualOrderedDeclaredNames).isEqualTo(expectedDeclaredNames);
  }

  private Node createNode(String sourceCode) {
    TokenStream tokenStream = new TokenStream(sourceCode, null);
    Node rootNode = tokenStream.parseProgram();
    return rootNode;
  }
}