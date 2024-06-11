package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.DiagnosticType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CrossModuleMethodMotionTest {
  private AbstractCompiler compiler;
  private AnalyzePrototypeProperties analyzer;
  private JSModuleGraph moduleGraph;
  private CrossModuleMethodMotion pass;
  private IdGenerator idGenerator;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    moduleGraph = mock(JSModuleGraph.class);
    idGenerator = new IdGenerator();
    analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
  }

  @Test
  public void testProcess() {
    when(moduleGraph.getModuleCount()).thenReturn(3);

    pass.process(getNodeForExterns(), getNodeForRoot());

    verify(compiler, times(1)).report(any(DiagnosticType.class));
  }

  @Test
  public void testProcessNoModules() {
    when(moduleGraph.getModuleCount()).thenReturn(0);

    pass.process(getNodeForExterns(), getNodeForRoot());

    verify(compiler, never()).report(any(DiagnosticType.class));
  }

  @Test
  public void testMoveMethods() {
    Node externRoot = getNodeForExterns();
    Node root = getNodeForRoot();

    when(moduleGraph.getModuleCount()).thenReturn(3);
    when(moduleGraph.dependsOn(any(JSModule.class), any(JSModule.class)))
        .thenReturn(true);

    pass.moveMethods(analyzer.getAllNameInfo());

    List<NameInfo> allNameInfo = new ArrayList<>();
    allNameInfo.addAll(analyzer.getAllNameInfo());

    ArgumentCaptor<Collection<NameInfo>> nameInfoCaptor =
        ArgumentCaptor.forClass(Collection.class);

    verify(compiler).process(externRoot, root);

    verify(compiler).moveMethods(nameInfoCaptor.capture());

    List<NameInfo> capturedNameInfos = nameInfoCaptor.getValue();

    for (NameInfo nameInfo : capturedNameInfos) {
      if (nameInfo.isReferenced() && !nameInfo.readsClosureVariables()) {
        JSModule deepestCommonModuleRef = nameInfo.getDeepestCommonModuleRef();
        if (deepestCommonModuleRef != null) {
          Iterator<Symbol> declarations = nameInfo.getDeclarations().descendingIterator();
          while (declarations.hasNext()) {
            Symbol symbol = declarations.next();
            if (symbol instanceof Property) {
              Property prop = (Property) symbol;
              if (valueIsFunction(prop.getValue())
                  && moduleGraph.dependsOn(deepestCommonModuleRef, prop.getModule())) {
                Node value = prop.getValue();
                Node proto = prop.getPrototype();

                Node stubCall = getStubCall(idGenerator.newId());
                Node stubParent = value.getParent();
                stubParent.replaceChild(value, stubCall);

                Node unstubCall = getUnstubCall(idGenerator.newId(), prop.getValue());
                Node unstubParent = getNodeForCodeInsertion(deepestCommonModuleRef);
                unstubParent.addChildToFront(unstubCall);

                verify(compiler).reportCodeChange();
              }
            }
          }
        }
      }
    }
  }

  @Test
  public void testMoveMethodsNoMethodToMove() {
    Node externRoot = getNodeForExterns();
    Node root = getNodeForRoot();

    when(moduleGraph.getModuleCount()).thenReturn(3);
    when(moduleGraph.dependsOn(any(JSModule.class), any(JSModule.class)))
        .thenReturn(false);

    pass.moveMethods(analyzer.getAllNameInfo());

    List<NameInfo> allNameInfo = new ArrayList<>();
    allNameInfo.addAll(analyzer.getAllNameInfo());

    ArgumentCaptor<Collection<NameInfo>> nameInfoCaptor =
        ArgumentCaptor.forClass(Collection.class);

    verify(compiler).process(externRoot, root);

    verify(compiler, never()).moveMethods(any(Collection.class));
  }

  @Test
  public void testGenerateStubs() {
    when(moduleGraph.getModuleCount()).thenReturn(1);

    pass.moveMethods(analyzer.getAllNameInfo());

    Node declarations = compiler.getModuleRootNode().getLastChild();

    Node stubCode = declarations.getFirstChild();
    assertThat(stubCode.getFirstChild().getText()).isEqualTo("var JSCompiler_stubMap = []");
    assertThat(stubCode.getLastChild().getText()).isEqualTo(
        "function JSCompiler_unstubMethod(id, body) { return JSCompiler_stubMap[id] = body; }");
  }

  @Test
  public void testGenerateStubsMultipleDeclarations() {
    when(moduleGraph.getModuleCount()).thenReturn(1);

    analyzer
        .getAllNameInfo()
        .add(new NameInfo("test1", getNodeForFunctionDeclaration("test1"), null));
    analyzer
        .getAllNameInfo()
        .add(new NameInfo("test2", getNodeForFunctionDeclaration("test2"), null));

    pass.moveMethods(analyzer.getAllNameInfo());

    Node declarations = compiler.getModuleRootNode().getLastChild();

    Node stubCode = declarations.getFirstChild();
    assertThat(stubCode.getFirstChild().getText()).isEqualTo("var JSCompiler_stubMap = []");
    Node stubDeclaration = stubCode.getLastChild();

    List<Node> functions = new ArrayList<>();
    functions.add(getNodeForFunctionDeclaration("test1"));
    functions.add(getNodeForFunctionDeclaration("test2"));

    for (Node function : functions) {
      Node stubFunction = findStubFunction(stubDeclaration, function.getText());
      assertThat(stubFunction).isNotNull();
      Node body = stubFunction.getFirstChild().getLastChild();
      assertThat(body.getText()).isEqualTo(function.getText());
    }
  }

  private Node getNodeForExterns() {
    return new Node();
  }

  private Node getNodeForRoot() {
    return new Node();
  }

  private Node getStubCall(int id) {
    return IR.call(IR.name(STUB_METHOD_NAME), IR.number(id)).copyInformationFromForTree(null);
  }

  private Node getUnstubCall(int id, Node value) {
    return IR.call(IR.name(UNSTUB_METHOD_NAME), IR.number(id), value).copyInformationFromForTree(null);
  }

  private Node getNodeForCodeInsertion(JSModule module) {
    return IR.exprResult(IR.assign(IR.getprop(module.getRootNode(), "JSCompiler_stubMap"), IR.arrayLiteral())).copyInformationFromForTree(null);
  }

  private boolean valueIsFunction(Node node) {
    return node.isFunction();
  }

  private Node getNodeForFunctionDeclaration(String name) {
    return new NodeFunctionDeclaration(name);
  }

  private Node findStubFunction(Node stubDeclaration, String functionName) {
    for (Node child : stubDeclaration.getFirstChild().getChildren()) {
      if (child.isFunction() && child.getText().equals(functionName)) {
        return child;
      }
    }
    return null;
  }
}