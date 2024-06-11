package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.rhino.IR;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private AnalyzePrototypeProperties analyzer;
  private AbstractCompiler compiler;
  private IdGenerator idGenerator;
  private JSModule deepestCommonModuleRef;
  private CompilerPass pass;

  @Before
  public void setUp() {
    analyzer = mock(AnalyzePrototypeProperties.class);
    compiler = mock(AbstractCompiler.class);
    idGenerator = mock(IdGenerator.class);
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph(new JSModule[0]));
    when(compiler.parseSyntheticCode(anyString())).then((InvocationOnMock invocation) -> {
      Node node = new Node();
      node.setType(Node.SCRIPT);
      return node;
    });
    when(compiler.getNodeForCodeInsertion(any(JSModule.class))).then((InvocationOnMock invocation) -> {
      Node node = new Node();
      node.setType(Node.SCRIPT);
      return node;
    });

    pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    deepestCommonModuleRef = mock(JSModule.class);
  }

  @Test
  public void testProcess_noModules() {
    Node root = new Node();
    Node externRoot = new Node();
    pass.process(externRoot, root);
    verify(compiler, never()).report(any(JSError.class));
  }

  @Test
  public void testProcess_oneModule() {
    Node root = new Node();
    Node externRoot = new Node();
    JSModule module = new JSModule();
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph(module));
    pass.process(externRoot, root);
    verify(compiler, never()).report(any(JSError.class));
  }

  @Test
  public void testProcess_twoModules() {
    Node root = new Node();
    Node externRoot = new Node();
    JSModule module1 = new JSModule();
    JSModule module2 = new JSModule();
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph(module1, module2));

    // Test no name infos
    pass.process(externRoot, root);
    verify(compiler, never()).report(any(JSError.class));

    // Test name infos that are not referenced or read closure variables
    NameInfo nameInfo = mock(NameInfo.class);
    when(analyzer.getAllNameInfo()).thenReturn(new NameInfo[0]);
    when(analyzer.getAllNameInfo()).thenReturn(new NameInfo[] { nameInfo });
    when(nameInfo.isReferenced()).thenReturn(false);
    when(nameInfo.getDeclarations()).thenReturn(new Iterator[0]);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
    when(nameInfo.readsClosureVariables()).thenReturn(false);

    pass.process(externRoot, root);
    verify(compiler, never()).report(any(JSError.class));

    // Test name infos that are not functions
    when(nameInfo.getValue()).thenReturn(new Node());
    when(value.isFunction()).thenReturn(false);
    pass.process(externRoot, root);
    verify(compiler, never()).report(any(JSError.class));

    // Test name infos with null deepestCommonModuleRef
    when(nameInfo.isReferenced()).thenReturn(true);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(null);
    pass.process(externRoot, root);
    verify(compiler).report(eq(JSError.make(NULL_COMMON_MODULE_ERROR)));
  }

  @Test
  public void testMoveMethods() {
    Node root = new Node();
    Node externRoot = new Node();
    JSModule module1 = new JSModule();
    JSModule module2 = new JSModule();
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph(module1, module2));

    NameInfo nameInfo = mock(NameInfo.class);
    when(analyzer.getAllNameInfo()).thenReturn(new NameInfo[0]);
    when(analyzer.getAllNameInfo()).thenReturn(new NameInfo[] { nameInfo });
    when(nameInfo.isReferenced()).thenReturn(true);
    when(nameInfo.getDeclarations()).thenReturn(new Iterator[0]);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
    when(nameInfo.readsClosureVariables()).thenReturn(false);
    when(analyzer.getPrototype(anyString())).thenReturn(new Node());
    when(analyzer.getPrototypeProperty(anyString(), anyString())).thenReturn(new Property(new Node(), new Node(), new Node()));
    when(analyzer.getPrototypeProperty(anyString(), eq("prototype"))).thenReturn(new Property(new Node(), new Node(), new Node()));

    IdGenerator idGenerator = mock(IdGenerator.class);
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);
    when(idGenerator.newId()).thenReturn(0);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);

    pass.moveMethods(analyzer.getAllNameInfo());
    verify(compiler, never()).report(any(JSError.class));

    Property property = mock(Property.class);
    when(property.getValue()).thenReturn(new Node().setType(Node.FUNCTION_EXPR));

    NameInfo nameInfo1 = mock(NameInfo.class);
    when(nameInfo1.getDeepestCommonModuleRef()).thenReturn(module1);
    when(analyzer.getPrototypeProperty(eq("prototype"), eq("b"))).thenReturn(property);

    NameInfo nameInfo2 = mock(NameInfo.class);
    when(nameInfo2.getDeepestCommonModuleRef()).thenReturn(module2);

    when(analyzer.getAllNameInfo()).thenReturn(new NameInfo[] { nameInfo1, nameInfo2 });
    when(analyzer.getAllNameInfo()).thenReturn(new NameInfo[] { nameInfo1, nameInfo2, nameInfo });

    pass.moveMethods(analyzer.getAllNameInfo());
    verify(compiler).reportCodeChange();

    Node stubCall = new Node();
    Node unstubCall = new Node();
    Node unstubParent = new Node();

    when(compiler.parseSyntheticCode(anyString())).then((InvocationOnMock invocation) -> {
      Node node = new Node();
      node.setType(Node.SCRIPT);
      return node;
    });

    when(compiler.getNodeForCodeInsertion(any(JSModule.class))).then((InvocationOnMock invocation) -> {
      Node node = new Node();
      node.setType(Node.SCRIPT);
      return node;
    });

    pass.moveMethods(analyzer.getAllNameInfo());

    verify(compiler).getNodeForCodeInsertion(eq(null));
    verify(compiler).addChildrenToFront(eq(declarations.removeChildren()), any(Node.class));
  }

  @Test
  public void testIdGenerator() {
    IdGenerator idGenerator = mock(IdGenerator.class);
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);
    when(idGenerator.newId()).thenReturn(0);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(mock(AbstractCompiler.class), idGenerator, true);

    pass.moveMethods(anyCollection());
    verify(idGenerator).newId();

    when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);
    pass.moveMethods(anyCollection());
    verify(idGenerator, never()).newId();
  }
}