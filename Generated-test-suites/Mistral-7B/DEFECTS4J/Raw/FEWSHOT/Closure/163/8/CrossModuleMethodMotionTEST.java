package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Property;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Symbol;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Logger;
import com.google.javascript.jscomp.ModuleGraph;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

/**
 * Test class for CrossModuleMethodMotion.
 */
public class CrossModuleMethodMotionTest {

  private AnalyzePrototypeProperties analyzer;
  private IdGenerator idGenerator;
  private AbstractCompiler compiler;
  private Logger logger;
  private ModuleGraph moduleGraph;
  private JSModule rootModule;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    moduleGraph = mock(ModuleGraph.class);
    idGenerator = mock(IdGenerator.class);
    logger = mock(Logger.class);

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(moduleGraph.getModuleCount()).thenReturn(2);
    analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, true, false);
  }

  @Test
  public void testMoveMethods() {
    NameInfo nameInfo = new NameInfo("A", null, null, null, null, null, null, null, null, null);
    Property property = new Property("b", null, null, null, null, null, null, null, null, null, null);

    JSModule module1 = mock(JSModule.class);
    when(moduleGraph.getModuleForName("module1")).thenReturn(module1);

    JSModule module2 = mock(JSModule.class);
    when(moduleGraph.getModuleForName("module2")).thenReturn(module2);

    Node root = Node.root();
    when(moduleGraph.getModuleForName("root")).thenReturn(mock(JSModule.class));

    when(analyzer.getAllNameInfo()).thenReturn(new NameInfo[] { nameInfo });

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.moveMethods(analyzer.getAllNameInfo());

    assertNull(nameInfo.getDeclarations().iterator().next());

    Node stubCall = nameInfo.getDeclarations().iterator().next().getValue();
    assertNotNull(stubCall);

    Node unstubCall = findUnstubCall(root);
    assertNotNull(unstubCall);
  }

  private Node findUnstubCall(Node node) {
    if (node.isCall()) {
      Call call = (Call) node;
      if (call.getCallee().isName() && call.getCallee().asName().getIdent().equals(UNSTUB_METHOD_NAME)) {
        return call;
      }
    }

    for (int i = 0; i < node.getNumChildren(); i++) {
      Node child = node.getChild(i);
      Node unstubCall = findUnstubCall(child);
      if (unstubCall != null) {
        return unstubCall;
      }
    }

    return null;
  }
}