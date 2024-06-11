package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CrossModuleMethodMotion.IdGenerator;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Symbol;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.DiagnosticType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CrossModuleMethodMotionTest {
  private Compiler compiler;
  private IdGenerator idGenerator;
  private CrossModuleMethodMotion pass;
  private JSModule deepestCommonModuleRef;

  @Mock
  private JSModule mockModule;

  @Before
  public void setUp() {
    compiler = new Compiler();
    idGenerator = new CrossModuleMethodMotion.IdGenerator();
    pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    deepestCommonModuleRef = mockModule;
  }

  @Test
  public void testProcessNullExternRoot() {
    assertNull(pass.process(null, null));
  }

  @Test
  public void testProcessNullRoot() {
    assertNull(pass.process(null, null));
  }

  @Test
  public void testProcessNoModules() {
    pass.process(Node.nullLiteral(), Node.nullLiteral());
    assertTrue(pass.getAnalyzer().getAllNameInfo().isEmpty());
  }

  @Test
  public void testProcessOneModule() {
    pass.process(Node.nullLiteral(), Node.nullLiteral());
    assertTrue(pass.getAnalyzer().getAllNameInfo().isEmpty());
  }

  @Test
  public void testMoveMethodsEmptyCollection() {
    pass.moveMethods(Collections.emptyList());
  }

  @Test
  public void testProcessWithNullDeepestCommonModuleRef() {
    pass.analyzer = new AnalyzePrototypeProperties(compiler, null, false, false);
    pass.process(Node.nullLiteral(), Node.nullLiteral());
    compiler.reportErrorCountShouldBe(1);
    compiler.assertErrorMessageEquals("JSC_INTERNAL_ERROR_MODULE_DEPEND",
        1);
  }

  @Test
  public void testMoveMethodsWithNoNameInfo() {
    pass.analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false,
        false);
    pass.moveMethods(Collections.emptyList());
  }

  @Test
  public void testMoveMethodsWithUnreferencedNameInfo() {
    JSModule module = new JSModule();
    pass.analyzer.addNameInfo(new NameInfo("A", module, null, null, null));
    pass.moveMethods(Collections.singletonList(pass.analyzer.getNameInfo("A")));
  }

  @Test
  public void testMoveMethodsWithReadOnlyNameInfo() {
    JSModule module = new JSModule();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null,
        null, null, property);
    NameInfo nameInfo = new NameInfo("A", module, symbol, null, null);
    pass.analyzer.addNameInfo(nameInfo);
    pass.moveMethods(Collections.singletonList(nameInfo));
  }

  @Test
  public void testMoveMethodsWithNoMethodToMove() {
    JSModule module = new JSModule();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null,
        null, null, property);
    NameInfo nameInfo = new NameInfo("A", module, symbol, null, null);
    pass.analyzer.addNameInfo(nameInfo);
    pass.moveMethods(Collections.singletonList(nameInfo));
  }

  @Test
  public void testMoveMethodsWithMethodNotFunction() {
    JSModule module = new JSModule();
    Property property = new Property(Node.name("b"), Node.numberLiteral(1), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null, null,
        null, property);
    NameInfo nameInfo = new NameInfo("A", module, symbol, null, null);
    pass.analyzer.addNameInfo(nameInfo);
    pass.moveMethods(Collections.singletonList(nameInfo));
  }

  @Test
  public void testMoveMethodsWithNoDeepestCommonModule() {
    JSModule sourceModule = new JSModule();
    JSModule destinationModule = new JSModule();
    Node sourceProto = Node.objectLiteral();
    Node destProto = Node.objectLiteral();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null, null,
        null, property);
    NameInfo nameInfo = new NameInfo("A", sourceModule, symbol, null, null);
    pass.analyzer.addNameInfo(nameInfo);
    pass.moveMethods(Collections.singletonList(nameInfo));
    assertFalse(property.getValue().isFunction());
    assertNull(deepestCommonModuleRef.getPrototype());
  }

  @Test
  public void testMoveMethodsWithMethodNotDeclaredInDestinationModule() {
    JSModule sourceModule = new JSModule();
    JSModule destinationModule = new JSModule();
    Node sourceProto = Node.objectLiteral();
    Node destProto = Node.objectLiteral();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null, null,
        null, property);
    NameInfo nameInfo = new NameInfo("A", sourceModule, symbol, null, null);
    pass.analyzer.addNameInfo(nameInfo);
    pass.moveMethods(Collections.singletonList(nameInfo));
    nodeAssertions.assertNodesEqual(
        "function JSCompiler_unstubMethod(id, body) {" +
            "  return JSCompiler_unstubMap[id] = body;" +
        "}",
        deepestCommonModuleRef.getBody().getChild(0));
  }

  @Test
  public void testMoveMethodsWithSameModule() {
    JSModule sourceModule = new JSModule();
    JSModule destinationModule = sourceModule;
    Node sourceProto = Node.objectLiteral();
    Node destProto = Node.objectLiteral();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null, null,
        null, property);
    NameInfo nameInfo = new NameInfo("A", sourceModule, symbol, null, null);
    pass.analyzer.addNameInfo(nameInfo);
    pass.moveMethods(Collections.singletonList(nameInfo));
    assertNull(deepestCommonModuleRef.getPrototype());
  }

  @Test
  public void testMoveMethodsWithMethodInDeepestCommonAncestor() {
    JSModule grandparentModule = new JSModule();
    JSModule parentModule = new JSModule();
    JSModule childModule = new JSModule();
    JSModule destinationModule = childModule;
    Node grandparentProto = Node.objectLiteral();
    Node parentProto = Node.objectLiteral();
    Node childProto = Node.objectLiteral();
    Node destinationProto = Node.objectLiteral();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null, null,
        null, property);
    NameInfo grandparentNameInfo = new NameInfo("Grandparent", grandparentModule,
        symbol, null, null);
    NameInfo parentNameInfo = new NameInfo("Parent", parentModule, symbol,
        grandparentNameInfo, null);
    NameInfo childNameInfo = new NameInfo("Child", childModule, symbol,
        parentNameInfo, null);
    NameInfo destinationNameInfo = new NameInfo("Destination", destinationModule,
        symbol, null, null);
    pass.analyzer.addNameInfo(grandparentNameInfo);
    pass.analyzer.addNameInfo(parentNameInfo);
    pass.analyzer.addNameInfo(childNameInfo);
    pass.analyzer.addNameInfo(destinationNameInfo);
    pass.moveMethods(Collections.singletonList(destinationNameInfo));
    assertSame(parentProto, childProto);
  }

  @Test
  public void testMoveMethodsWithMethodInAncestor() {
    JSModule grandparentModule = new JSModule();
    JSModule parentModule = new JSModule();
    JSModule childModule = new JSModule();
    JSModule destinationModule = parentModule;
    Node grandparentProto = Node.objectLiteral();
    Node parentProto = Node.objectLiteral();
    Node childProto = Node.objectLiteral();
    Node destinationProto = Node.objectLiteral();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null, null,
        null, property);
    NameInfo grandparentNameInfo = new NameInfo("Grandparent", grandparentModule,
        symbol, null, null);
    NameInfo parentNameInfo = new NameInfo("Parent", parentModule, symbol,
        grandparentNameInfo, null);
    NameInfo childNameInfo = new NameInfo("Child", childModule, symbol,
        parentNameInfo, null);
    NameInfo destinationNameInfo = new NameInfo("Destination", destinationModule,
        symbol, null, null);
    pass.analyzer.addNameInfo(grandparentNameInfo);
    pass.analyzer.addNameInfo(parentNameInfo);
    pass.analyzer.addNameInfo(childNameInfo);
    pass.analyzer.addNameInfo(destinationNameInfo);
    pass.moveMethods(Collections.singletonList(destinationNameInfo));
    Node unstubCall = deepestCommonModuleRef.getBody().getChild(1);
    assertSame(grandparentProto, unstubCall.getFirstChild().getChild(0)
        .getFirstChild().getChild(0).getFirstChild().getFirstChild()
        .getChild(0));
  }

  @Test
  public void testMoveMethodsWithMethodInSelf() {
    JSModule sourceModule = new JSModule();
    JSModule destinationModule = sourceModule;
    Node sourceProto = Node.objectLiteral();
    Node destProto = Node.objectLiteral();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null, null,
        null, property);
    NameInfo nameInfo = new NameInfo("A", sourceModule, symbol, null, null);
    pass.analyzer.addNameInfo(nameInfo);
    pass.moveMethods(Collections.singletonList(nameInfo));
    nodeAssertions.assertNodesEqual(
        "function JSCompiler_unstubMethod(id, body) {" +
            "  return JSCompiler_unstubMap[id] = body;" +
        "}",
        deepestCommonModuleRef.getBody().getChild(0));
  }

  @Test
  public void testMoveMethodsWithMethodInDescendant() {
    JSModule grandparentModule = new JSModule();
    JSModule parentModule = new JSModule();
    JSModule childModule = new JSModule();
    JSModule destinationModule = grandparentModule;
    Node grandparentProto = Node.objectLiteral();
    Node parentProto = Node.objectLiteral();
    Node childProto = Node.objectLiteral();
    Node destinationProto = Node.objectLiteral();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null, null,
        null, property);
    NameInfo grandparentNameInfo = new NameInfo("Grandparent", grandparentModule,
        symbol, null, null);
    NameInfo parentNameInfo = new NameInfo("Parent", parentModule, symbol,
        grandparentNameInfo, null);
    NameInfo childNameInfo = new NameInfo("Child", childModule, symbol,
        parentNameInfo, null);
    NameInfo destinationNameInfo = new NameInfo("Destination", destinationModule,
        symbol, null, null);
    pass.analyzer.addNameInfo(grandparentNameInfo);
    pass.analyzer.addNameInfo(parentNameInfo);
    pass.analyzer.addNameInfo(childNameInfo);
    pass.analyzer.addNameInfo(destinationNameInfo);
    pass.moveMethods(Collections.singletonList(destinationNameInfo));
    nodeAssertions.assertNodesEqual(
        "function JSCompiler_unstubMethod(id, body) {" +
            "  return JSCompiler_unstubMap[id] = body;" +
        "}",
        grandparentProto.getLastChild());
  }

  @Test
  public void testMoveMethodsWithMethodInAncestorWithSelf() {
    JSModule grandparentModule = new JSModule();
    JSModule parentModule = new JSModule();
    JSModule childModule = new JSModule();
    JSModule destinationModule = parentModule;
    Node grandparentProto = Node.objectLiteral();
    Node parentProto = Node.objectLiteral();
    Node childProto = Node.objectLiteral();
    Node destinationProto = Node.objectLiteral();
    Property property = new Property(Node.name("b"), Node.nullLiteral(), null,
        null, null, Node.nullLiteral());
    Symbol symbol = new Symbol(Node.name("b"), Node.SCOPE_PROTOTYPE, null, null,
        null, property);
    NameInfo grandparentNameInfo = new NameInfo("Grandparent", grandparentModule,
        symbol, null, null);
    NameInfo parentNameInfo = new NameInfo("Parent", parentModule, symbol,
        grandparentNameInfo, null);
    NameInfo childNameInfo = new NameInfo("Child", childModule, symbol,
        parentNameInfo, null);
    NameInfo destinationNameInfo = new NameInfo("Destination", destinationModule,
        symbol, null, null);
    pass.analyzer.addNameInfo(grandparentNameInfo);
    pass.analyzer.addNameInfo(parentNameInfo);
    pass.analyzer.addNameInfo(childNameInfo);
    pass.analyzer.addNameInfo(destinationNameInfo);
    pass.moveMethods(Collections.singletonList(destinationNameInfo));
    Node unstubCall = deepestCommonModuleRef.getBody().getChild(1);
    Node stubCall = unstubCall.getFirstChild();
    Node parentProtoOfStub = stubCall.getFirstChild().getFirstChild()
        .getChild(0).getFirstChild().getFirstChild().getFirstChild();
    Node destinationProtoOfStub = parentProtoOfStub.getFirstChild();
    assertSame(grandparentProto, destinationProtoOfStub);
  }

  @Test
  public void testGenerateStubDeclarations() {
    pass.process(Node.nullLiteral(), Node.nullLiteral());
    nodeAssertions.assertNodesEqual(
        "var JSCompiler_stubMap = [];" +
            "function JSCompiler_stubMethod(JSCompiler_stubMethod_id) {" +
            "  return function() {" +
            "    return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(" +
            "        this, arguments);" +
        "  };",
        deepestCommonModuleRef.getBody());
  }
}