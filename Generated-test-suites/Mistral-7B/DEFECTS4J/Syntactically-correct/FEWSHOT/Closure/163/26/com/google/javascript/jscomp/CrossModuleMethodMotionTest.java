package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Logger;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.IR;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {
  @Test
  public void testMoveMethods() {
    Logger logger = Logger.getLogger(CrossModuleMethodMotion.class.getName());
    logger.setLevel(Logger.Level.FINEST);

    AbstractCompiler compiler = new AbstractCompiler();
    IdGenerator idGenerator = new IdGenerator();
    JSModuleGraph moduleGraph = new JSModuleGraph();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);

    // Create a simple module graph with two modules A and B
    // Module A has a prototype property b, which is a function
    // Module B doesn't have any prototype properties
    Node externs = IR.parse("class A { b() { return 'A'; } }");
    Node rootA = IR.parse("class A { b() { return 'A'; } }");
    Node moduleA = IR.parse("module A { export class A {} }");
    Node classA = IR.getChild(moduleA, 0);
    Node protoA = IR.getChild(classA, 0);
    IR.setParent(protoA, moduleA);
    IR.setParent(classA, moduleA);
    IR.setParent(moduleA, rootA);
    IR.setParent(rootA, externs);

    Node externsB = IR.parse("");
    Node rootB = IR.parse("");
    Node moduleB = IR.parse("module B { export default function bar() { return 'B'; } }");
    Node classB = IR.getChild(moduleB, 0);
    IR.setParent(classB, rootB);
    IR.setParent(moduleB, rootB);
    IR.setParent(rootB, externsB);

    moduleGraph.addModule(moduleA);
    moduleGraph.addModule(moduleB);

    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(pass);

    // Process the code with the CrossModuleMethodMotion pass
    passManager.process(externs, rootA);

    // The prototype method b should have been moved to module B
    Iterator<NameInfo> nameInfos = pass.analyzer.getAllNameInfo().iterator();
    NameInfo nameInfo = null;
    while (nameInfos.hasNext()) {
      nameInfo = nameInfos.next();
      if (nameInfo.getName().equals("A.b")) {
        break;
      }
    }
    assertTrue(nameInfo != null);
    Node unstubCall = IR.getChild(nameInfo.getDeepestModuleRef().getCode(), 0);
    assertTrue(unstubCall.getToken().getLineNumber() > 0);
    assertTrue(unstubCall.getToken().getColumnNumber() > 0);

    // The unstubbed function body should be present in module B
    Node functionBody = IR.getChild(
        IR.getChild(moduleB, 0), 1);
    assertTrue(functionBody.getToken().getLineNumber() > 0);
    assertTrue(functionBody.getToken().getColumnNumber() > 0);
    assertTrue(functionBody.getFirstChild().getText().equals("return 'B';"));

    // No stub declarations should be present in the top-most module
    Node topModule = IR.getParent(externs);
    assertFalse(topModule.getChild(0).getText().startsWith(STUB_METHOD_NAME));
    assertFalse(topModule.getChild(0).getText().startsWith(STUB_DECLARATIONS));
  }
}