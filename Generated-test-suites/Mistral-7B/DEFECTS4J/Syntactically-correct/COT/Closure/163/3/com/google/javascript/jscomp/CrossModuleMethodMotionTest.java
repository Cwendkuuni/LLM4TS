import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.CompilerPassType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.analyzer.AnalyzePrototypeProperties;
import com.google.javascript.rhino.IR;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private CompilerPass crossModuleMethodMotion;
  private IdGenerator idGenerator;
  private JSModuleGraph moduleGraph;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions(true);
    compiler = new Compiler(options);
    idGenerator = new CrossModuleMethodMotion.IdGenerator();
    moduleGraph = compiler.getModuleGraph();
    crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
  }

  @Test
  public void testProcess_MultipleModules() {
    Node rootNode = compiler.parseTree("test.js");
    Node externRootNode = compiler.parseTree("externs.js");
    crossModuleMethodMotion.process(externRootNode, rootNode);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_NoModules() {
    Node rootNode = compiler.parseTree("test.js");
    Node externRootNode = compiler.parseTree("externs.js");
    crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, false);
    crossModuleMethodMotion.process(externRootNode, rootNode);
  }

  @Test
  public void testMoveMethods_MultipleModules() {
    Node rootNode = compiler.parseTree("test.js");
    Node externRootNode = compiler.parseTree("externs.js");
    crossModuleMethodMotion.process(externRootNode, rootNode);
    crossModuleMethodMotion.moveMethods(analyzer.getAllNameInfo());
  }

  @Test
  public void testMoveMethods_NoMethodsToMove() {
    Node rootNode = compiler.parseTree("test.js");
    Node externRootNode = compiler.parseTree("externs.js");
    crossModuleMethodMotion.process(externRootNode, rootNode);
    crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, false);
    crossModuleMethodMotion.moveMethods(ImmutableList.of());
  }

  @Test
  public void testMoveMethods_UnreferencedMethod() {
    Node rootNode = compiler.parseTree("test.js");
    Node externRootNode = compiler.parseTree("externs.js");
    NameInfo nameInfo = new NameInfo("Foo", IR.createName("Foo"), null);
    nameInfo.setDeepestCommonModuleRef(null);
    crossModuleMethodMotion.process(externRootNode, rootNode);
    crossModuleMethodMotion.moveMethods(ImmutableList.of(nameInfo));
  }

  @Test
  public void testMoveMethods_FunctionProperty_SameModule() {
    Node rootNode = compiler.parseTree("test.js");
    Node externRootNode = compiler.parseTree("externs.js");
    NameInfo nameInfo = new NameInfo("Foo", IR.createName("Foo"), null);
    nameInfo.setDeepestCommonModuleRef(moduleGraph.getEntryNodeModule());
    Property property = new Property(
        "prototype",
        IR.createName("prototype"),
        IR.createName("Foo"),
        IR.createName("b"),
        IR.createName("function () {}"),
        null,
        ImmutableList.of(),
        null);
    nameInfo.addSymbol(property);
    crossModuleMethodMotion.process(externRootNode, rootNode);
    crossModuleMethodMotion.moveMethods(ImmutableList.of(nameInfo));
    Node expectedFunction = IR.createName("function () {}");
    Node actualFunction = getFunctionByName("Foo.prototype.b");
    assertEquals(expectedFunction, actualFunction);
  }

  @Test
  public void testMoveMethods_FunctionProperty_DifferentModules() {
    Node rootNodeA = compiler.parseTree("testA.js");
    Node externRootA = compiler.parseTree("externsA.js");
    Node rootNodeB = compiler.parseTree("testB.js");
    Node externRootB = compiler.parseTree("externsB.js");
    NameInfo nameInfo = new NameInfo("FooA", IR.createName("FooA"), null);
    nameInfo.setDeepestCommonModuleRef(moduleGraph.getModule("testB.js"));
    Property property = new Property(
        "prototype",
        IR.createName("prototype"),
        IR.createName("FooA"),
        IR.createName("b"),
        IR.createName("function () {}"),
        null,
        ImmutableList.of(),
        null);
    nameInfo.addSymbol(property);
    crossModuleMethodMotion.process(externRootA, rootNodeA);
    crossModuleMethodMotion.process(externRootB, rootNodeB);
    crossModuleMethodMotion.moveMethods(ImmutableList.of(nameInfo));

    Node expectedStub = IR.call(
        IR.name("JSCompiler_unstubMethod"),
        IR.number(idGenerator.newId()),
        IR.createName("function () {}"));
    Node expectedFunction = IR.createName("function () {}");
    Node actualStub = getNodeByName("JSCompiler_unstubMethod_" + idGenerator.newId());
    Node actualFunction = getFunctionByName("FooA.prototype.b");

    assertEquals(expectedStub, actualStub);
    assertEquals(expectedFunction, actualFunction);
  }

  @Test(expected = AssertionError.class)
  public void testMoveMethods_GetterProperty() {
    Node rootNode = compiler.parseTree("test.js");
    Node externRootNode = compiler.parseTree("externs.js");
    NameInfo nameInfo = new NameInfo("Foo", IR.createName("Foo"), null);
    nameInfo.setDeepestCommonModuleRef(moduleGraph.getEntryNodeModule());
    Property property = new Property(
        "prototype",
        IR.createName("prototype"),
        IR.createName("Foo"),
        IR.createName("get b() {}"),
        null,
        null,
        ImmutableList.of(),
        null);
    nameInfo.addSymbol(property);
    crossModuleMethodMotion.process(externRootNode, rootNode);
    crossModuleMethodMotion.moveMethods(ImmutableList.of(nameInfo));
  }

  @Test
  public void testMoveMethods_SetterProperty() {
    Node rootNode = compiler.parseTree("test.js");
    Node externRootNode = compiler.parseTree("externs.js");
    NameInfo nameInfo = new NameInfo("Foo", IR.createName("Foo"), null);
    nameInfo.setDeepestCommonModuleRef(moduleGraph.getEntryNodeModule());
    Property property = new Property(
        "prototype",
        IR.createName("prototype"),
        IR.createName("Foo"),
        IR.createName("set b(value) {}"),
        null,
        null,
        ImmutableList.of(),
        null);
    nameInfo.addSymbol(property);
    crossModuleMethodMotion.process(externRootNode, rootNode);
    crossModuleMethodMotion.moveMethods(ImmutableList.of(nameInfo));
  }

  @Test
  public void testIdGenerator_HasGeneratedAnyIds() {
    IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    assertFalse(idGenerator.hasGeneratedAnyIds());
    idGenerator.newId();
    assertTrue(idGenerator.hasGeneratedAnyIds());
  }

  private Node getNodeByName(String name) {
    return compiler.getRootNode().lookupVariable(name, false, false);
  }

  private Node getFunctionByName(String name) {
    Node node = getNodeByName(name);
    if (node == null || node.getType() != Node.FUNCTION_DECL) {
      fail("Unexpected node type for name '" + name + "'.");
    }
    return node;
  }

  private AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(
      compiler, moduleGraph, true, false);
}